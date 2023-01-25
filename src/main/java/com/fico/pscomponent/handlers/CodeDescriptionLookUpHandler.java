package com.fico.pscomponent.handlers;

import org.springframework.stereotype.Service;

@Service
public class CodeDescriptionLookUpHandler {/*

	private static final Logger logger = LoggerFactory.getLogger(PropertiesHandler.class);

	@Value("classpath:Code_Description_Look_up.xlsx")
	Resource defaultCodeLookupFile;

	@Autowired
	private CodeDescriptionLookupService codeDescriptionLookupService;

	public CodeDescriptionLookup getCodeLookUpByCodeAndCategory(int code, String category) {
		CodeDescriptionLookup codeDescriptionLookup = codeDescriptionLookupService.getByCodeAndCategory(code, category);
		return codeDescriptionLookup;
	}

	public LoadResponse loadDefaultLookup() {
		try(InputStream inputStream = defaultCodeLookupFile.getInputStream();) {
			return loadCodeLookUp(inputStream);
		} catch(IOException e) {
			logger.error("Error loading default-properties.csv ", e);
			LoadResponse response = new LoadResponse();
			response.setSuccess(false);
			response.setErrorList(Arrays.asList("Error reading from default file"));
			return response;
		}
	}

	public LoadResponse loadCustomLookup(MultipartFile file) {
		try(InputStream inputStream = file.getInputStream();) {
			return loadCodeLookUp(inputStream);
		} catch(IOException e) {
			logger.error("Error loading custom file ", e);
			LoadResponse response = new LoadResponse();
			response.setSuccess(false);
			response.setErrorList(Arrays.asList("Error reading from file "+ file.getName()));
			return response;
		}
	}

	private LoadResponse loadCodeLookUp(InputStream inputStream) {
		logger.debug("---Invoked loadCode Lookup handler------");
		LoadResponse response = new LoadResponse();
		try (Workbook workbook = new XSSFWorkbook(inputStream)) {
			List<String> errorMessageList = new ArrayList<String>();

			Sheet idaScoreRegionCodeSheet = workbook.getSheetAt(0);
			logger.debug("Reading sheet {}", idaScoreRegionCodeSheet.getSheetName());
			LoadSheetResponse idaScoreReasonCodeResponse = processSheet(idaScoreRegionCodeSheet, CATEGORY.IDA_SCORE_REASON_CODES);
			if (!idaScoreReasonCodeResponse.isSuccess()) {
				errorMessageList.add(idaScoreReasonCodeResponse.getErrorMessage());
			}

			Sheet idaSyntheticReasonCodeSheet = workbook.getSheetAt(1);
			logger.debug("Reading sheet {}", idaSyntheticReasonCodeSheet.getSheetName());
			LoadSheetResponse idaSyntheticReasonCodeResponse = processSheet(idaSyntheticReasonCodeSheet, CATEGORY.IDA_SYNTHETIC__REASON_CODES);
			if (!idaSyntheticReasonCodeResponse.isSuccess()) {
				errorMessageList.add(idaSyntheticReasonCodeResponse.getErrorMessage());
			}

			Sheet workStatusDefinitionSheet = workbook.getSheetAt(2);
			logger.debug("Reading sheet {}", workStatusDefinitionSheet.getSheetName());
			LoadSheetResponse workStatusDefinitionResponse = processSheet(workStatusDefinitionSheet, CATEGORY.WORST_STATUS_DEFINITION);
			if (!workStatusDefinitionResponse.isSuccess()) {
				errorMessageList.add(workStatusDefinitionResponse.getErrorMessage());
			}

			if (idaScoreReasonCodeResponse.isSuccess() && idaSyntheticReasonCodeResponse.isSuccess() && workStatusDefinitionResponse.isSuccess()) {
				response.setSuccess(true);
			} else {
				response.setSuccess(false);
				response.setErrorList(errorMessageList);
			}
			return response;
		} catch (IOException e) {
			logger.error("Error loading lookup file ", e);
			response.setSuccess(false);
			response.setErrorList(Arrays.asList("Error while loading file"));
			return response;
		}
	}

	private LoadSheetResponse processSheet(Sheet sheet, CATEGORY category) {
		logger.debug("--Invoked processSheet---");
		LoadSheetResponse response = new LoadSheetResponse();
		try {
			for (Row row : sheet) {

				//skip first 2 rows
				if (row.getRowNum() == 0 || row.getRowNum() == 1)
					continue;

				Cell codeCell = row.getCell(0);
				Cell nameCell = null;
				Cell descriptionCell = null;
				DataFormatter formatter = new DataFormatter();
				if (CATEGORY.WORST_STATUS_DEFINITION.equals(category)) {
					nameCell = row.getCell(1);
					descriptionCell = row.getCell(2);
					if (!Objects.isNull(codeCell) && !Objects.isNull(nameCell) && !Objects.isNull(descriptionCell)) {
						int code = Integer.parseInt(formatter.formatCellValue(codeCell));
						String name = formatter.formatCellValue(nameCell);
						String description = formatter.formatCellValue(descriptionCell);
						logger.debug("Code {} Name {} Description {}", code, name, description);
						createOrUpdateLookup(code, description, name, category);
					}
				} else {
					descriptionCell = row.getCell(1);
					if (!Objects.isNull(codeCell) && !Objects.isNull(descriptionCell)) {
						int code = Integer.parseInt(formatter.formatCellValue(codeCell));
						String description = formatter.formatCellValue(descriptionCell);
						logger.debug("Code {} Description {}", code, description);
						createOrUpdateLookup(code, description, null, category);
					}
				}
			}
			response.setSuccess(true);
			return response;
		} catch (Exception e) {
			logger.error("Error processing excel sheet", e);
			response.setSuccess(false);
			response.setErrorMessage("Error processing sheet " + sheet.getSheetName());
			return response;
		}
	}

	public boolean deActivate(int code, String category) {
		return updateActiveStatus(code, category, false);
	}

	public boolean activate(int code, String category) {
		return updateActiveStatus(code, category, true);
	}

	private boolean updateActiveStatus(int code, String category, boolean active) {
		CodeDescriptionLookup codeDescriptionLookup = this.getCodeLookUpByCodeAndCategory(code, category);
		if (!Objects.isNull(codeDescriptionLookup)) {
			codeDescriptionLookup.setIsActive(active);
			codeDescriptionLookupService.update(codeDescriptionLookup);
			return true;
		}
		return false;
	}

	private void createOrUpdateLookup(int code, String description, String name, CATEGORY category) {
		CodeDescriptionLookup codeDescriptionLookup = null;
		try {
			codeDescriptionLookup = codeDescriptionLookupService.getByCodeAndCategory(code, category.getName());
			codeDescriptionLookup.setDescription(description);
			if (!Objects.isNull(name)) {
				codeDescriptionLookup.setName(name);
			}
			codeDescriptionLookup.setIsActive(true);
			codeDescriptionLookupService.update(codeDescriptionLookup);
		} catch (EntityNotFoundException e) {
			codeDescriptionLookup = new CodeDescriptionLookup();
			codeDescriptionLookup.setCode(code);
			codeDescriptionLookup.setDescription(description);
			codeDescriptionLookup.setCategory(category.getName());
			if (!Objects.isNull(name)) {
				codeDescriptionLookup.setName(name);
			}
			codeDescriptionLookup.setIsActive(true);
			codeDescriptionLookupService.create(codeDescriptionLookup);
		}
	}

	private static class LoadSheetResponse {
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

	public static class LoadResponse {
		boolean success;
		List<String> errorList;

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public List<String> getErrorList() {
			return errorList;
		}

		public void setErrorList(List<String> errorList) {
			this.errorList = errorList;
		}

	}

	public enum CATEGORY {
		IDA_SCORE_REASON_CODES("IDA Score Reason Codes"),
		IDA_SYNTHETIC__REASON_CODES("IDA Synthetic Reason Codes"),
		WORST_STATUS_DEFINITION("Worst Status definition");

		private String name;

		private CATEGORY(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
*/}
