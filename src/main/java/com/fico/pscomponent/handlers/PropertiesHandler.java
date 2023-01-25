package com.fico.pscomponent.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fico.dmp.context.DMPContext;
import com.fico.dmp.telusagentuidb.FawbPropertySource;
import com.fico.dmp.telusagentuidb.service.FawbPropertySourceService;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.wavemaker.runtime.data.exception.EntityNotFoundException;

@Service
public class PropertiesHandler {

	private static final String XLSX = "xlsx";
	private static final String CSV = "csv";

	private static final Logger logger = LoggerFactory.getLogger(PropertiesHandler.class);

	@Value("classpath:default-properties-design.csv")
	Resource defaultPropertiesFileDesign;

	@Value("classpath:default-properties-staging.csv")
	Resource defaultPropertiesFileStaging;

	@Value("classpath:default-properties-prod.csv")
	Resource defaultPropertiesFileProd;

	@Autowired
	private FawbPropertySourceService fawbPropertySourceService;

	public Map<String, String> getProperties() {
		Long count = fawbPropertySourceService.count("");
		Pageable pageable = PageRequest.of(0, count.intValue());
		Page<FawbPropertySource> pageFawbPropertySource = fawbPropertySourceService.findAll("", pageable);
		if (pageFawbPropertySource.hasContent()) {
			List<FawbPropertySource> propertyList = pageFawbPropertySource.getContent();
			Map<String, String> propertiesMap = propertyList.stream().collect(
					Collectors.toMap(FawbPropertySource::getPropertyKey, FawbPropertySource::getPropertyValue));
			return propertiesMap;
		}
		return null;
	}

    @Cacheable("getPropertyValueByName")
	public String getPropertyValueByName(String name) {
		try {
			FawbPropertySource propertySource = fawbPropertySourceService.getByPropertyKey(name);
			return propertySource.getPropertyValue();
		} catch (EntityNotFoundException e) {
			logger.error("Property Key {} does not exist", name);
		}
		return null;
	}

	// To load the default properties from default-properties.csv to reset the
	// properties to initial state
	public LoadResponse loadDefaultProperties() {
		String dmpEnvironment = DMPContext.getDmpEnvironment();
		Resource resourceToUse = defaultPropertiesFileDesign;
		if (!Objects.isNull(dmpEnvironment)) {

			if (dmpEnvironment.toLowerCase().equals("staging")) {
				resourceToUse = defaultPropertiesFileStaging;
			}

			if (dmpEnvironment.toLowerCase().equals("production")) {
				resourceToUse = defaultPropertiesFileProd;
			}
		}

		try (InputStream inputStream = resourceToUse.getInputStream()) {
			return loadProperties(CSV, inputStream);
		} catch (IOException e) {
			logger.error("Error loading default-properties.csv ", e);
			LoadResponse response = new LoadResponse();
			response.setSuccess(false);
			response.setErrorMessage("Error loading default file");
			return response;
		}
	}

	// To load the properties from the user supplied file
	public LoadResponse loadCustomProperties(MultipartFile file) {
		String fileType = FilenameUtils.getExtension(file.getOriginalFilename());
		try (InputStream inputStream = file.getInputStream()) {
			return loadProperties(fileType, inputStream);
		} catch (IOException e) {
			logger.error("Error loading user supplied file ", e);
			LoadResponse response = new LoadResponse();
			response.setSuccess(false);
			response.setErrorMessage("Error loading file " + file.getName());
			return response;
		}
	}

    @CacheEvict(value="getPropertyValueByName",allEntries=true)
	private LoadResponse loadProperties(String fileType, InputStream inputStream) {
		LoadResponse response = new LoadResponse();
		List<String[]> records = null;

		if (fileType.equals(XLSX)) {
			records = getExcelRecords(inputStream);
		} else {
			records = getRecords(inputStream);
		}

        logger.info("----------Records---------- {}", records);
		if (Objects.isNull(records)) {
			response.setSuccess(false);
			response.setErrorMessage("Missing Records");
			return response;
		}

		try {
			for (String[] record : records) {
				FawbPropertySource fawbPropertySource = new FawbPropertySource();
				String key = record[0];
				String value = record[1];
				try {
					if (!Objects.isNull(key) && !Objects.isNull(value)) {
						fawbPropertySource = fawbPropertySourceService.getByPropertyKey(key);
						if (!fawbPropertySource.getPropertyValue().equals(value)) {
							fawbPropertySource.setPropertyValue(value);
							fawbPropertySourceService.update(fawbPropertySource);
							logger.info("Updated property for key {} with value {}", key, value);
						}
					}
				} catch (EntityNotFoundException e) {
					fawbPropertySource.setPropertyKey(key);
					fawbPropertySource.setPropertyValue(value);
					fawbPropertySourceService.create(fawbPropertySource);
					logger.info("Created Property with key {} and value {}", key, value);
				}
			}
			response.setSuccess(true);
			return response;
		} catch (Exception e) {
			logger.error("Exception processing csv records ", e);
			response.setSuccess(false);
			response.setErrorMessage(e.getLocalizedMessage());
			return response;
		}
	}

	//read csv records
	private List<String[]> getRecords(InputStream inputStream) {
		try (InputStreamReader reader = new InputStreamReader(inputStream);
				CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {
			return csvReader.readAll();
		} catch (Exception e) {
			logger.error("Error loading CSV file ", e);
			return null;
		}
	}

	//read excel records
	private List<String[]> getExcelRecords(InputStream inputStream) {
		try(Workbook workbook = new XSSFWorkbook(inputStream)) {
			Sheet sheet = workbook.getSheetAt(0);
			int i = 0;
			List<String[]> rowList = new ArrayList<String[]>();
			for (Row row : sheet) {
				String[] rowArray = new String[2];

				// Skip the header
				if (i == 0) {
					i++;
					continue;
				}

				Cell propertyKeyCell = row.getCell(0);
				Cell propertyValueCell = row.getCell(1);

				if (!Objects.isNull(propertyKeyCell) && !Objects.isNull(propertyValueCell)) {
					DataFormatter formatter = new DataFormatter();
					String propertyKey = formatter.formatCellValue(propertyKeyCell);
					String propertyValue = formatter.formatCellValue(propertyValueCell);
					rowArray[0] = propertyKey;
					rowArray[1] = propertyValue;
					rowList.add(rowArray);
				}
				i++;
			}
			return rowList;
		} catch (IOException e) {
			logger.error("Error loading .xlsx file ", e);
			return null;
		}
	}

	public static class LoadResponse {
		boolean success;
		String errorMessage;

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}

	}
}
