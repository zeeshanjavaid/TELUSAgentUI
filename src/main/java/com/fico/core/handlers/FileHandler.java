package com.fico.core.handlers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jose4j.json.internal.json_simple.JSONArray;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.parser.JSONParser;
import org.jose4j.json.internal.json_simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fico.core.services.ActivityServiceBS;
import com.fico.core.services.ActivityServiceBS.ActivityLogResponseWrapper;
import com.fico.core.services.DomainValueServiceBS;
import com.fico.dmp.telusagentuidb.AuditDataChange;
import com.fico.dmp.telusagentuidb.DomainValue;
import com.fico.dmp.telusagentuidb.DomainValueDescription;
import com.fico.dmp.telusagentuidb.DomainValueRelation;
import com.fico.dmp.telusagentuidb.DomainValueType;
import com.fico.dmp.telusagentuidb.DomainValueTypeRelationship;
import com.fico.dmp.telusagentuidb.models.query.QueryActivityLogResponse;
import com.fico.dmp.telusagentuidb.models.query.QueryGetDomainValueByIdResponse;
import com.fico.dmp.telusagentuidb.service.AuditDataChangeService;
import com.fico.dmp.telusagentuidb.service.DomainValueDescriptionService;
import com.fico.dmp.telusagentuidb.service.DomainValueRelationService;
import com.fico.dmp.telusagentuidb.service.DomainValueService;
import com.fico.dmp.telusagentuidb.service.DomainValueTypeRelationshipService;
import com.fico.dmp.telusagentuidb.service.DomainValueTypeService;
import com.fico.ps.exception.FileHandlerException;
import com.fico.ps.hermes.audit.ChangeData;
import com.fico.ps.hermes.util.JacksonUtility;
import com.fico.pscomponent.handlers.UserManagementHandler;

@Service("handler.FileHandler")
public class FileHandler {

	@Autowired
	@Qualifier("TELUSAgentUIDB.DomainValueService")
	private DomainValueService domainValueService;

	@Autowired
	@Qualifier("TELUSAgentUIDB.DomainValueTypeService")
	private DomainValueTypeService domainValueTypeService;

	@Autowired
	@Qualifier("TELUSAgentUIDB.DomainValueTypeRelationshipService")
	private DomainValueTypeRelationshipService domainValueTypeRelationshipService;

	@Autowired
	@Qualifier("TELUSAgentUIDB.DomainValueRelationService")
	private DomainValueRelationService domainValueRelationService;

	@Autowired
	@Qualifier("TELUSAgentUIDB.DomainValueDescriptionService")
	private DomainValueDescriptionService domainValueDescriptionService;

	@Autowired
	@Qualifier("facade.DomainValueServiceBS")
	private DomainValueServiceBS domainValueServiceBS;
	
	@Autowired
    AuditDataChangeService auditDataChangeService;

	@Autowired
	@Qualifier("facade.ActivityServiceBS")
	private ActivityServiceBS activityServiceBS;

	@Autowired
	@Qualifier("handler.RoleGroupUserSeedHandler")
	private RoleGroupUserSeedHandler roleGroupUserSeedHandler;

	@Autowired
	private UserManagementHandler userManagementHandler;

	private static final Logger logger = LoggerFactory.getLogger(FileHandler.class);

	//this is an upper limit imposed on the total number of rows to be processed on any excel file imports
	private static final int MAX_LOOPS = 100000; 
	
	// Exception message when FILE is empty
	public static final String FILE_EMPTY_EXCEPTION_MSG = "Uploaded file is empty";
	// Exception message when FILE is not in valid format
	public static final String FILE_FORMAT_INVALID_EXCEPTION_MSG = "File format is not valid, please select a valid file";
	// Exception message when Excel FILE doesn't contains valid tabs/columns
	public static final String FILE_TAB_COLUMNS_EXCEPTION_MSG = "File uploaded must contain specific tabs and columns";
	// Exception message when Excel FILE doesn't contains specific sheet
	public static final String MISSING_SHEET_EXCEPTION_MSG = "File uploaded is missing required sheet";

	// Workbook sheet names for DV type and type relationship bootstrap
	private static final String DOMAINVALUE_TYPE_SHEET = "DomainValueType";
	private static final String DOMAINVALUE_TYPE_RELATION_SHEET = "DomainValueTypeRelationship";
	private static final String DOMAINVALUE_SHEET = "DomainValue";
	private static final String DOMAINVALUE_TRANSLATION_SHEET = "DomainValueTranslation";
	
	// Workbook sheet names for Data Change History
	private static final String DATA_CHANGE_HISTORY_SHEET = "Data Change History Download";
    private static final String ACTION_ADD = "CREATE";
    private static final String ACTION_DELETE = "DELETE";
    private static final String ACTION_UPDATE = "UPDATE";

	// Workbook sheet names for Role -> Group -> User & related associations seed
	private static final String PERMISSION_SHEET = "Permissions";
	private static final String ROLE_SHEET = "Roles";
	private static final String GROUP_SHEET = "Groups";
	private static final String USER_SHEET = "Users";
	private static final String ROLE_PERMISSION_SHEET = "Role_Permission";
	private static final String GROUP_ROLE_SHEET = "Group_Role";
	private static final String GROUP_USER_SHEET = "Group_User";
	
	private JSONArray jsonArrObject = null;

	/**
	 * Handler method that consumes the supplied {@link MultipartFile} file uploaded
	 * from the UI frontend and then processes the information and stores it under
	 * the {@link DomainValueType} and {@link DomainValueTypeRelationship} entities
	 * 
	 * @param multipartFile
	 * @throws Exception
	 */
	public void loadDomainValueTypeAndRelationship(MultipartFile multipartFile) throws Exception {
		if (multipartFile == null)
			throw new Exception(
					"Exception occurred at method 'loadDomainValueTypeAndRelationship'. Supplied file is null!");
		else {
			if (multipartFile.isEmpty())
				throw new Exception(
						"Exception occurred at method 'loadDomainValueTypeAndRelationship'. Supplied file is empty!");
			else {
				final InputStream fileInpuInputStream = multipartFile.getInputStream();

				// create the workbook instance from the supplied Excel file
				XSSFWorkbook dvTypeWorkBook = new XSSFWorkbook(fileInpuInputStream);

				// check if supplied excel file is not empty
				if (checkExcelSheetEmpty(dvTypeWorkBook.getSheet(DOMAINVALUE_TYPE_SHEET))) {
					// close the workbook instance
					dvTypeWorkBook.close();
					throw new Exception(FILE_EMPTY_EXCEPTION_MSG);
				} else {
					// 1. Bootstrap the domain value type sheet first
					XSSFSheet dvTypeSheet = dvTypeWorkBook.getSheet(DOMAINVALUE_TYPE_SHEET);
					if (dvTypeSheet != null) {
						Iterator<Row> dvTypeRowIterator = dvTypeSheet.iterator();

						// for each rows in this sheet
						if(logger.isInfoEnabled())
							logger.info("-----Processing contents of sheet '{}'", DOMAINVALUE_TYPE_SHEET);

						int rowCounter = 1;
						while (dvTypeRowIterator.hasNext() && rowCounter <= MAX_LOOPS) {
							final Row row = dvTypeRowIterator.next();
							if (row.getRowNum() == 0)
								; // skipping first row as it represents header in sheet
							else {
								final String dvType_CODE = (row.getCell(0) != null)
										? row.getCell(0).getStringCellValue()
										: null;
								final String dvType_DESC = (row.getCell(1) != null)
										? row.getCell(1).getStringCellValue()
										: null;

								if ((dvType_CODE == null || dvType_CODE.trim().length() == 0)
										|| (dvType_DESC == null || dvType_DESC.trim().length() == 0)) {
									if(logger.isInfoEnabled())
										logger.info("-----Skipping DVType row as either CODE or DESCRIPTION or both are missing!");
								} else {
									// check for DV TYPE for the current CODE value
									List<DomainValueType> dvTypeDTOlist = domainValueTypeService
											.findAll("code='" + dvType_CODE + "'", null).toList();
									if (dvTypeDTOlist == null || dvTypeDTOlist.isEmpty()) {
										// create the DV type
										DomainValueType domainValueTypeDTO = new DomainValueType();
										domainValueTypeDTO.setCode(dvType_CODE);
										domainValueTypeDTO.setDescription(dvType_DESC);
										domainValueTypeDTO.setCreatedOn(
												new Timestamp(Calendar.getInstance().getTime().getTime()));
										domainValueTypeDTO.setUpdatedOn(null);
										/* createdBy user ID -> some info needed */

										domainValueTypeDTO = domainValueTypeService.create(domainValueTypeDTO);
										if(logger.isInfoEnabled())
											logger.info("------DomainValueType with CODE [{}] created!", dvType_CODE);
									} else {
										// update the DV type
										DomainValueType domainValueTypeDTO = dvTypeDTOlist.get(0);
										domainValueTypeDTO.setDescription(dvType_DESC);
										domainValueTypeDTO.setUpdatedOn(
												new Timestamp(Calendar.getInstance().getTime().getTime()));
										/* updatedBy user ID -> some info needed */

										domainValueTypeDTO = domainValueTypeService.update(domainValueTypeDTO);
										if(logger.isInfoEnabled())
											logger.info("------DomainValueType with CODE [{}] updated!", dvType_CODE);
									}
									if(logger.isInfoEnabled())
										logger.info("-----DomainValueType with CODE [{}], DESCRIPTION [{}] has been processed!",dvType_CODE, dvType_DESC);
								}
							}
							
							if (row.getRowNum() != 0)
								rowCounter += 1;
						} // row processing for DVType sheet completed
						if(logger.isInfoEnabled())
							logger.info("-----Processing completed for sheet '{}'", DOMAINVALUE_TYPE_SHEET);

						// 2. Bootstrap the domain value type relationship sheet
						XSSFSheet dvTypeRelationSheet = dvTypeWorkBook.getSheet(DOMAINVALUE_TYPE_RELATION_SHEET);
						if (dvTypeRelationSheet != null) {
							Iterator<Row> dvTypeRltnRowIterator = dvTypeRelationSheet.iterator();

							// for each rows in this sheet
							if(logger.isInfoEnabled())
								logger.info("-----Processing contents of sheet '{}'", DOMAINVALUE_TYPE_RELATION_SHEET);

							rowCounter = 1;
							while (dvTypeRltnRowIterator.hasNext() && rowCounter <= MAX_LOOPS) {
								final Row row = dvTypeRltnRowIterator.next();
								if (row.getRowNum() == 0)
									; // skipping first row as it represents header in sheet
								else {
									final String dvType_CODE = (row.getCell(0) != null)
											? row.getCell(0).getStringCellValue()
											: null;
									final String parent1_dvType_CODE = (row.getCell(1) != null)
											? row.getCell(1).getStringCellValue()
											: null;
									final String parent2_dvType_CODE = (row.getCell(2) != null)
											? row.getCell(2).getStringCellValue()
											: null;

									// check at least one parent information present and child DVType code present
									if ((dvType_CODE == null || dvType_CODE.trim().length() == 0)
											|| ((parent1_dvType_CODE == null
													|| parent1_dvType_CODE.trim().length() == 0)
													&& (parent2_dvType_CODE == null
															|| parent2_dvType_CODE.trim().length() == 0)))
										;
									else {
										DomainValueType domainValueTypeDTO = null;
										List<DomainValueType> dvTypeDTOList = domainValueTypeService
												.findAll("code='" + dvType_CODE + "'", null).toList();

										// check if such a DV Type exists
										if (dvTypeDTOList == null || dvTypeDTOList.isEmpty())
											continue;
										else
											domainValueTypeDTO = dvTypeDTOList.get(0);

										// only if DVType exists with CODE, process the relationship
										if (domainValueTypeDTO != null) {
											// get DVType instance of parent1 CODE
											List<DomainValueType> parent1DVTypeDTOList = domainValueTypeService.findAll(
													"code='" + (parent1_dvType_CODE == null ? "" : parent1_dvType_CODE)
															+ "'",
													null).toList();
											DomainValueType parent1DVTypeDTO = (parent1DVTypeDTOList == null
													|| parent1DVTypeDTOList.isEmpty()) ? null
															: parent1DVTypeDTOList.get(0);

											// get DVType instance of parent2 CODE
											List<DomainValueType> parent2DVTypeDTOList = domainValueTypeService.findAll(
													"code='" + (parent2_dvType_CODE == null ? "" : parent2_dvType_CODE)
															+ "'",
													null).toList();
											DomainValueType parent2DVTypeDTO = (parent2DVTypeDTOList == null
													|| parent2DVTypeDTOList.isEmpty()) ? null
															: parent2DVTypeDTOList.get(0);

											// find relationship for current DVType code
											List<DomainValueTypeRelationship> dvTypeRltnDTOList = domainValueTypeRelationshipService
													.findAll("domainValueTypeId=" + domainValueTypeDTO.getId(), null)
													.toList();

											if (dvTypeRltnDTOList == null || dvTypeRltnDTOList.isEmpty()) {
												// create scenarios
												if (parent1DVTypeDTO == null) {
													if(logger.isInfoEnabled())
														logger.info(
															"------Skipping DVType relationship for child DVType [{}] as parent1 DVType not found corresponding CODE!",
															dvType_CODE);
													continue;
												}

												if (parent1DVTypeDTO != null
														&& (parent2_dvType_CODE != null && parent2DVTypeDTO == null)) {
													if(logger.isInfoEnabled())
														logger.info(
															"------Skipping DVType relationship for child DVType [{}] as parent1 found but DVType not found for parent2 against corresponding CODE!",
															dvType_CODE);
													continue;
												}

												DomainValueTypeRelationship domainValueTypeRelationshipDTO = null;
												if (parent1DVTypeDTO != null && (parent2_dvType_CODE == null
														|| parent2_dvType_CODE.trim().length() == 0)) {
													domainValueTypeRelationshipDTO = new DomainValueTypeRelationship();
													domainValueTypeRelationshipDTO
															.setDomainValueTypeId(domainValueTypeDTO.getId());
													domainValueTypeRelationshipDTO
															.setParentDomainValueTypeId1(parent1DVTypeDTO.getId());
													domainValueTypeRelationshipDTO.setParentDomainValueTypeId2(null);

													// create this relationship
													domainValueTypeRelationshipDTO = domainValueTypeRelationshipService
															.create(domainValueTypeRelationshipDTO);
													if(logger.isInfoEnabled())
														logger.info("------Relationship for child DVType [{}] created",
															dvType_CODE);
												}

												if (parent1DVTypeDTO != null && parent2DVTypeDTO != null) {
													domainValueTypeRelationshipDTO = new DomainValueTypeRelationship();
													domainValueTypeRelationshipDTO
															.setDomainValueTypeId(domainValueTypeDTO.getId());
													domainValueTypeRelationshipDTO
															.setParentDomainValueTypeId1(parent1DVTypeDTO.getId());
													domainValueTypeRelationshipDTO
															.setParentDomainValueTypeId2(parent2DVTypeDTO.getId());

													// create this relationship
													domainValueTypeRelationshipDTO = domainValueTypeRelationshipService
															.create(domainValueTypeRelationshipDTO);
													if(logger.isInfoEnabled())
														logger.info("------Relationship for child DVType [{}] created",
															dvType_CODE);
												}

											} else {
												// update scenarios
												if (parent1DVTypeDTO == null) {
													if(logger.isInfoEnabled())
														logger.info(
															"------Skipping DVType relationship for child DVType [{}] as parent1 DVType not found corresponding CODE!",
															dvType_CODE);
													continue;
												}

												if (parent1DVTypeDTO != null
														&& (parent2_dvType_CODE != null && parent2DVTypeDTO == null)) {
													if(logger.isInfoEnabled())
														logger.info(
															"------Skipping DVType relationship for child DVType [{}] as parent1 found but DVType not found for parent2 against corresponding CODE!",
															dvType_CODE);
													continue;
												}

												if (parent1DVTypeDTO != null && (parent2_dvType_CODE == null
														|| parent2_dvType_CODE.trim().length() == 0)) {
													DomainValueTypeRelationship fetchedDVTypeRltn = dvTypeRltnDTOList
															.get(0);
													if (fetchedDVTypeRltn.getParentDomainValueTypeId2() != null)
														if(logger.isInfoEnabled())
															logger.info(
																"------Skipping DVType relationship for child DVType [{}] as existing DB relation data contains parent2 but sheet ROW doesn't specifies parent2!",
																dvType_CODE);
													else {
														DomainValueType parent1DVType = domainValueTypeService.findById(
																fetchedDVTypeRltn.getParentDomainValueTypeId1());
														if (parent1DVType == null)
															;
														if (!parent1DVType.getCode().equals(parent1DVTypeDTO.getCode()))
															;
													}
												}

												if (parent1DVTypeDTO != null && parent2DVTypeDTO != null) {
													DomainValueTypeRelationship fetchedDVTypeRltn = dvTypeRltnDTOList
															.get(0);
													if (fetchedDVTypeRltn.getParentDomainValueTypeId2() == null)
														if(logger.isInfoEnabled())
															logger.info(
																"------Skipping DVType relationship for child DVType [{}] as existing DB relation data doesn't contains parent2 but sheet ROW specifies parent2!",
																dvType_CODE);
													else {
														DomainValueType parent1DVType = domainValueTypeService.findById(
																fetchedDVTypeRltn.getParentDomainValueTypeId1());
														DomainValueType parent2DVType = domainValueTypeService.findById(
																fetchedDVTypeRltn.getParentDomainValueTypeId2());
														if (parent1DVType == null || parent2DVType == null)
															;
														if (!parent1DVType.getCode().equals(parent1DVTypeDTO.getCode())
																|| !parent2DVType.getCode()
																		.equals(parent2DVTypeDTO.getCode()))
															;
													}
												}
											}

										} else
											if(logger.isInfoEnabled())
												logger.info(
													"------------DomainValueType with CODE [{}] not found. Skipping DVType relationship for such DVType codes!",
													dvType_CODE);
									}
									if(logger.isInfoEnabled())
										logger.info(
											"-----DomainValueTypeRelationship with child DVType CODE [{}] has been processed!",
											dvType_CODE);
								}
								if (row.getRowNum() != 0)
									rowCounter += 1;
							} // row processing for DVTypeRelation sheet completed
							if(logger.isInfoEnabled())
								logger.info("-----Processing completed for sheet '{}'", DOMAINVALUE_TYPE_RELATION_SHEET);
						} else {
							// close the workbook instance
							dvTypeWorkBook.close();
							throw new Exception(
									"Exception occurred at method 'loadDomainValueTypeAndRelationship'. Sheet ["
											+ DOMAINVALUE_TYPE_RELATION_SHEET
											+ "] is missing or not present at workbook!");
						}
					} else {
						// close the workbook instance
						dvTypeWorkBook.close();
						throw new Exception("Exception occurred at method 'loadDomainValueTypeAndRelationship'. Sheet ["
								+ DOMAINVALUE_TYPE_SHEET + "] is missing or not present at workbook!");
					}
				} // end of IF-ELSE (check for excel file empty)

				// close the workbook instance
				dvTypeWorkBook.close();
			}
		}
	}

	/**
	 * Exports all the DomainValues within the system
	 * 
	 * @return The DomainValue export as a {@link XSSFWorkbook} instance
	 * @throws Exception
	 */
	public XSSFWorkbook exportDomainValueFromSystem() throws Exception {
        //logger.info("-----Inside exportDomainValueFromSystem!");
		int dvSheetRowNum = 1, dvTranSheetRowNum = 1;
		XSSFWorkbook dvExportWorkbook = null;
		/*
		 * The order of export of DomainValue(s) is dependent upon the order of DV types
		 * as in the system
		 */
		List<DomainValueType> domainValueTypeDTOList = domainValueServiceBS.getDomainValueTypeByDescription("", 0, Integer.MAX_VALUE, "id ASC").toList();
		
		if (domainValueTypeDTOList == null ){ //|| domainValueTypeDTOList.isEmpty()
			if(logger.isInfoEnabled())
				logger.info("-----No DomainValueType(s) found in the system!");
		}else {
		   
			// set up workbook & sheets
			dvExportWorkbook = new XSSFWorkbook();
			XSSFSheet dvSheet = dvExportWorkbook.createSheet(DOMAINVALUE_SHEET);
			XSSFSheet dvTranslationSheet = dvExportWorkbook.createSheet(DOMAINVALUE_TRANSLATION_SHEET);

			// for both sheets add the header rows
			// 1. add header ROW for DV sheet
			XSSFRow dvSheetHeaderRow = dvSheet.createRow(0);
			// 1.1 create CELLs for this header ROW
			XSSFCell dvTypeCell = dvSheetHeaderRow.createCell(0, CellType.STRING);
			dvTypeCell.setCellValue("Type");
			XSSFCell dvParent1DVtypeCell = dvSheetHeaderRow.createCell(1, CellType.STRING);
			dvParent1DVtypeCell.setCellValue("Parent1Type");
			XSSFCell dvParent1DVCodeCell = dvSheetHeaderRow.createCell(2, CellType.STRING);
			dvParent1DVCodeCell.setCellValue("Parent1Code");
			XSSFCell dvParent2DVtypeCell = dvSheetHeaderRow.createCell(3, CellType.STRING);
			dvParent2DVtypeCell.setCellValue("Parent2Type");
			XSSFCell dvParent2DVCodeCell = dvSheetHeaderRow.createCell(4, CellType.STRING);
			dvParent2DVCodeCell.setCellValue("Parent2Code");
			XSSFCell dvIsDefaultCell = dvSheetHeaderRow.createCell(5, CellType.STRING);
			dvIsDefaultCell.setCellValue("IsDefault");
			XSSFCell dvIsActiveCell = dvSheetHeaderRow.createCell(6, CellType.STRING);
			dvIsActiveCell.setCellValue("IsActive");
			XSSFCell dvLabelCell = dvSheetHeaderRow.createCell(7, CellType.STRING);
			dvLabelCell.setCellValue("Code");
			XSSFCell dvOrderCell = dvSheetHeaderRow.createCell(8, CellType.STRING);
			dvOrderCell.setCellValue("Order");

			// 2. add header ROW for DV translation sheet
			XSSFRow dvTranHeaderRow = dvTranslationSheet.createRow(0);
			// 2.1 create CELLs for this header ROW
			XSSFCell dvTCell = dvTranHeaderRow.createCell(0, CellType.STRING);
			dvTCell.setCellValue("Type");
			XSSFCell dvCodeCell = dvTranHeaderRow.createCell(1, CellType.STRING);
			dvCodeCell.setCellValue("DomainValueCode");
			XSSFCell dvLocaleCell = dvTranHeaderRow.createCell(2, CellType.STRING);
			dvLocaleCell.setCellValue("Locale");
			XSSFCell dvDescriptionCell = dvTranHeaderRow.createCell(3, CellType.STRING);
			dvDescriptionCell.setCellValue("Description");


			Iterator<DomainValueType> dvTypeDTOIterator = domainValueTypeDTOList.iterator();

			// processing for each DV type in the system
			while (dvTypeDTOIterator.hasNext()) {
				final DomainValueType currentDVType = dvTypeDTOIterator.next();
                logger.info("-----Processing DomainValueType '{}'",
										currentDVType.getCode());
				String parent1Type = null, parent1Code = null, parent2Type = null, parent2Code = null;

				// find the type relationship
				List<DomainValueTypeRelationship> dvTypeRelationshipList = domainValueTypeRelationshipService
						.findAll("domainValueTypeId=" + currentDVType.getId(), null).toList();

				if (dvTypeRelationshipList == null || dvTypeRelationshipList.isEmpty()){
					;
				}else {
					final DomainValueTypeRelationship dvTypeRelationship = dvTypeRelationshipList.get(0);

					// for parent1
					if (dvTypeRelationship.getParentDomainValueTypeId1() != null
							&& dvTypeRelationship.getParentDomainValueTypeId1() != 0) {
						DomainValueType parent1DVtypeDTO = domainValueTypeService
								.findById(dvTypeRelationship.getParentDomainValueTypeId1());
						if (parent1DVtypeDTO == null){
							;
						}else{
							parent1Type = parent1DVtypeDTO.getCode();
						}
					}

					// for parent2
					if (dvTypeRelationship.getParentDomainValueTypeId2() != null
							&& dvTypeRelationship.getParentDomainValueTypeId2() != 0) {
						DomainValueType parent2DVtypeDTO = domainValueTypeService
								.findById(dvTypeRelationship.getParentDomainValueTypeId2());
						if (parent2DVtypeDTO == null){
							;
						}else{
							parent2Type = parent2DVtypeDTO.getCode();
						}
					}
				}

				// find all the 'ACTIVE' DomainValues against this type
				List<DomainValue> domainValueDTOList = domainValueService
						.findAll("domainValueType=" + currentDVType.getId() + " and isActive=true",
								PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "id")))
						.toList();

				if (domainValueDTOList == null || domainValueDTOList.isEmpty()){
					if(logger.isInfoEnabled()){
						logger.info("-----No DomainValue for type '{}' was found!", currentDVType.getCode());}
				}else {
					if(logger.isInfoEnabled()){
						logger.info("-----Processing all DomainValue against type '{}'", currentDVType.getCode());}

					Iterator<DomainValue> domainValueIterator = domainValueDTOList.iterator();

					while (domainValueIterator.hasNext()) {
						DomainValue currentDV = domainValueIterator.next();

						// processing relations only in case of AT LEAST one parent is present
						if (parent1Type != null) {
							// find the DV relations if any
							List<DomainValueRelation> domainValueRelationList = domainValueRelationService.findAll(
									"domainValueId=" + currentDV.getId(),
									PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "domainValueId")))
									.toList();

							if (domainValueRelationList == null || domainValueRelationList.isEmpty()){
								;
							}else {
								if(logger.isInfoEnabled()){
									logger.info("-----Processing all DomainValueRelation against DomainValue '{}'",
										currentDV.getCode());}

								Iterator<DomainValueRelation> dvRelationIterator = domainValueRelationList.iterator();

								while (dvRelationIterator.hasNext()) {
									parent1Code = null;
									parent2Code = null;
									DomainValueRelation currentDVRelation = dvRelationIterator.next();

									// parent1 DV code
									if (currentDVRelation.getParentDomainValueId1() != null
											&& currentDVRelation.getParentDomainValueId1() != 0) {
										DomainValue parent1DVDTO = domainValueService
												.findById(currentDVRelation.getParentDomainValueId1());
										if (parent1DVDTO == null){
											;
										}else{
											parent1Code = parent1DVDTO.getCode();
											}
									}

									// parent2 DV code
									if (currentDVRelation.getParentDomainValueId2() != null
											&& currentDVRelation.getParentDomainValueId2() != 0) {
										DomainValue parent2DVDTO = domainValueService
												.findById(currentDVRelation.getParentDomainValueId2());
										if (parent2DVDTO == null){
											;
										}else{
											parent2Code = parent2DVDTO.getCode();
											}
									}

									// create the sheet ROW for this relation
									XSSFRow DVRow = dvSheet.createRow(dvSheetRowNum);
									dvSheetRowNum += 1;

									// add contents for this ROW
									XSSFCell typeCell = DVRow.createCell(0, CellType.STRING);
									typeCell.setCellValue(
											currentDVType.getCode() == null ? "" :currentDVType.getCode());
									XSSFCell Parent1DVtypeCell = DVRow.createCell(1, CellType.STRING);
									Parent1DVtypeCell.setCellValue(parent1Type == null ? "" : parent1Type);
									
									final Object p1CodeVal = setNumericCellValue(parent1Code);
									if(p1CodeVal == null) {
										XSSFCell Parent1DVCodeCell = DVRow.createCell(2, CellType.STRING);
										Parent1DVCodeCell.setCellValue(parent1Code == null ? "" : parent1Code);
									} else {
										XSSFCell Parent1DVCodeCell = DVRow.createCell(2, CellType.NUMERIC);
										Parent1DVCodeCell.setCellValue(p1CodeVal instanceof Double ? ((Double) p1CodeVal) : ((Long) p1CodeVal));
									}
									
									XSSFCell Parent2DVtypeCell = DVRow.createCell(3, CellType.STRING);
									Parent2DVtypeCell.setCellValue(parent2Type == null ? "" : parent2Type);
									
									final Object p2CodeVal = setNumericCellValue(parent2Code);
									if(p2CodeVal == null) {
										XSSFCell Parent2DVCodeCell = DVRow.createCell(4, CellType.STRING);
										Parent2DVCodeCell.setCellValue(parent2Code == null ? "" : parent2Code);
									} else {
										XSSFCell Parent2DVCodeCell = DVRow.createCell(4, CellType.NUMERIC);
										Parent2DVCodeCell.setCellValue(p2CodeVal instanceof Double ? ((Double) p2CodeVal) : ((Long) p2CodeVal));
									}
									
									XSSFCell IsDefaultCell = DVRow.createCell(5, CellType.BOOLEAN);
									IsDefaultCell.setCellValue(currentDV.getIsDefault() == null ? false
											: currentDV.getIsDefault().booleanValue());
									XSSFCell IsActiveCell = DVRow.createCell(6, CellType.BOOLEAN);
									IsActiveCell.setCellValue(currentDV.getIsActive() == null ? false
											: currentDV.getIsActive().booleanValue());
									
									final Object curDVCodeVal = setNumericCellValue(currentDV.getCode());
									if(curDVCodeVal == null) {
										XSSFCell CodeCell = DVRow.createCell(7, CellType.STRING);
										CodeCell.setCellValue(currentDV.getCode() == null ? "" : currentDV.getCode());
									} else {
										XSSFCell CodeCell = DVRow.createCell(7, CellType.NUMERIC);
										CodeCell.setCellValue(curDVCodeVal instanceof Double ? ((Double) curDVCodeVal) : ((Long) curDVCodeVal));
									}
									
									XSSFCell OrderCell = DVRow.createCell(8, CellType.NUMERIC);
									OrderCell.setCellValue(currentDV.getRankOrder() == null ? -1 : currentDV.getRankOrder());

								} // end of processing DV relation iterator
							}
						} else {
							// no relation - add only the DV as it is
							// create the sheet ROW for this relation
							XSSFRow DVRow = dvSheet.createRow(dvSheetRowNum);
							dvSheetRowNum += 1;

							// add contents for this ROW
							XSSFCell typeCell = DVRow.createCell(0, CellType.STRING);
							typeCell.setCellValue(currentDVType.getCode() == null ? "" : currentDVType.getCode());
							XSSFCell Parent1DVtypeCell = DVRow.createCell(1, CellType.STRING);
							Parent1DVtypeCell.setCellValue("");
							XSSFCell Parent1DVCodeCell = DVRow.createCell(2, CellType.STRING);
							Parent1DVCodeCell.setCellValue("");
							XSSFCell Parent2DVtypeCell = DVRow.createCell(3, CellType.STRING);
							Parent2DVtypeCell.setCellValue("");
							XSSFCell Parent2DVCodeCell = DVRow.createCell(4, CellType.STRING);
							Parent2DVCodeCell.setCellValue("");
							XSSFCell IsDefaultCell = DVRow.createCell(5, CellType.BOOLEAN);
							IsDefaultCell.setCellValue(
									currentDV.getIsDefault() == null ? false : currentDV.getIsDefault().booleanValue());
							XSSFCell IsActiveCell = DVRow.createCell(6, CellType.BOOLEAN);
							IsActiveCell.setCellValue(
									currentDV.getIsActive() == null ? false : currentDV.getIsActive().booleanValue());
							
							final Object curDVCodeVal = setNumericCellValue(currentDV.getCode());
							if(curDVCodeVal == null) {
								XSSFCell CodeCell = DVRow.createCell(7, CellType.STRING);
								CodeCell.setCellValue(currentDV.getCode() == null ? "" : currentDV.getCode());
							} else {
								XSSFCell CodeCell = DVRow.createCell(7, CellType.NUMERIC);
								CodeCell.setCellValue(curDVCodeVal instanceof Double ? ((Double) curDVCodeVal) : ((Long) curDVCodeVal));
							}
							
							XSSFCell OrderCell = DVRow.createCell(8, CellType.NUMERIC);
							OrderCell.setCellValue(currentDV.getRankOrder() == null ? -1 : currentDV.getRankOrder());
						}

						// get the DV descriptions
						List<DomainValueDescription> dVDescriptionList = domainValueDescriptionService.findAll(
								"domainValueId=" + currentDV.getId(),
								PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "domainValueId")))
								.toList();

						if (dVDescriptionList == null || dVDescriptionList.isEmpty()){
							;
						}else {
							if(logger.isInfoEnabled()){
								logger.info("-----Processing all DomainValueDescription against DomainValue '{}'",
									currentDV.getCode());
							}
							Iterator<DomainValueDescription> dvDescriptionIterator = dVDescriptionList.iterator();

							while (dvDescriptionIterator.hasNext()) {
								DomainValueDescription currentDVDescription = dvDescriptionIterator.next();

								// create the sheet ROW for this description
								XSSFRow dvTransRow = dvTranslationSheet.createRow(dvTranSheetRowNum);
								dvTranSheetRowNum += 1;

								// add contents for this ROW
								XSSFCell TypeCell = dvTransRow.createCell(0, CellType.STRING);
								TypeCell.setCellValue(currentDVType.getCode() == null ? "" : currentDVType.getCode());
								
								final Object curDVCodeVal = setNumericCellValue(currentDV.getCode());
								if(curDVCodeVal == null) {
									XSSFCell CodeCell = dvTransRow.createCell(1, CellType.STRING);
									CodeCell.setCellValue(currentDV.getCode() == null ? "" : currentDV.getCode());
								} else {
									XSSFCell CodeCell = dvTransRow.createCell(1, CellType.NUMERIC);
									CodeCell.setCellValue(curDVCodeVal instanceof Double ? ((Double) curDVCodeVal) : ((Long) curDVCodeVal));
								}
								
								XSSFCell LocaleCell = dvTransRow.createCell(2, CellType.STRING);
								LocaleCell.setCellValue(currentDVDescription.getLocale() == null ? ""
										: currentDVDescription.getLocale());
								
								final Object curDVDscCodeVal = setNumericCellValue(currentDVDescription.getDescription());
								if(curDVDscCodeVal == null) {
									XSSFCell DescriptionCell = dvTransRow.createCell(3, CellType.STRING);
									DescriptionCell.setCellValue(currentDVDescription.getDescription() == null ? ""
											: currentDVDescription.getDescription());
								} else {
									XSSFCell DescriptionCell = dvTransRow.createCell(3, CellType.NUMERIC);
									DescriptionCell.setCellValue(curDVDscCodeVal instanceof Double ? ((Double) curDVDscCodeVal) : ((Long) curDVDscCodeVal));
								}

							} // end of DV description processing iterator
						}
						if(logger.isInfoEnabled())
							logger.info("-----Processing completed for DomainValue '{}'", currentDV.getCode());

					} // end of DV processing iterator
				}
				if(logger.isInfoEnabled())
					logger.info("-----Processing completed for DomainValueType '{}'", currentDVType.getCode());

			} // end of DVtype processing iterator
		}

		return dvExportWorkbook;
	}

	/**
	 * Exports all the DataChangeHistory within the system
	 * 
	 * @return The DataChangeHistory export as a {@link XSSFWorkbook} instance
	 * @throws Exception
	 */
	public XSSFWorkbook exportDataChangeHistoryFromSystem(String exportType, Integer applicationId, String userLocale) throws Exception {
        logger.info("-----Inside exportDataChangeHistoryFromSystem!");
		int dcSheetRowNum = 1;
		XSSFWorkbook dcExportWorkbook = null;
		Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.unsorted());
		Page<AuditDataChange> auditDataChangeList = auditDataChangeService.findAll("applicationId =" + applicationId, pageable);
		
		JacksonUtility utility = new JacksonUtility();
        utility.initialize();
        jsonArrObject = readJsonFile();
		
		if (auditDataChangeList == null ){ 
			if(logger.isInfoEnabled())
				logger.info("-----No auditDataChange(s) found in the system!");
		}else {
		   
			// set up workbook & sheets
			dcExportWorkbook = new XSSFWorkbook();
			XSSFSheet dcSheet = dcExportWorkbook.createSheet(DATA_CHANGE_HISTORY_SHEET);

			// for both sheets add the header rows
			// 1. add header ROW for DC sheet
			XSSFRow dcSheetHeaderRow = dcSheet.createRow(0);
			// 1.1 create CELLs for this header ROW
			XSSFCell dcAppNumberCell = dcSheetHeaderRow.createCell(0, CellType.STRING);
			dcAppNumberCell.setCellValue("Application Number");
			XSSFCell dcUserNameCell = dcSheetHeaderRow.createCell(1, CellType.STRING);
			dcUserNameCell.setCellValue("User Name");
			XSSFCell dcDateTime = dcSheetHeaderRow.createCell(2, CellType.STRING);
			dcDateTime.setCellValue("Datetime");
			XSSFCell dcAction = dcSheetHeaderRow.createCell(3, CellType.STRING);
			dcAction.setCellValue("Action");
			XSSFCell dcEntity = dcSheetHeaderRow.createCell(4, CellType.STRING);
			dcEntity.setCellValue("Entity");
			XSSFCell dcEntityID = dcSheetHeaderRow.createCell(5, CellType.STRING);
			dcEntityID.setCellValue("Entity ID");
			XSSFCell dcFieldName = dcSheetHeaderRow.createCell(6, CellType.STRING);
			dcFieldName.setCellValue("Field Name");
			XSSFCell dcOldValue = dcSheetHeaderRow.createCell(7, CellType.STRING);
			dcOldValue.setCellValue("Old Value");
			XSSFCell dcNewValue = dcSheetHeaderRow.createCell(8, CellType.STRING);
			dcNewValue.setCellValue("New Value");

			Iterator<AuditDataChange> dataChangeIterator = auditDataChangeList.iterator();

			// processing for each DataChangeHistory type in the system
			while (dataChangeIterator.hasNext()) {
				final AuditDataChange currentData = dataChangeIterator.next();
	            ChangeData[] changedatasArr = (ChangeData[]) utility.deSerialize(currentData.getChangeData(), ChangeData[].class);
                logger.info("-----Processing Change Data '{}'",	currentData.getId());
                
                switch (currentData.getAction()) {
	                case ACTION_ADD:
	                    for (ChangeData changeData : changedatasArr) {
	                    	dcSheetRowNum = addRow(dcSheetRowNum, dcSheet, currentData, changeData, userLocale, ACTION_ADD);
	                    }
	                    break;
	                case ACTION_UPDATE:
	                    for (ChangeData changeData : changedatasArr) {
	                    	dcSheetRowNum = addRow(dcSheetRowNum, dcSheet, currentData, changeData, userLocale, ACTION_UPDATE);
	                    }
	                    break;
	                case ACTION_DELETE:
	                	addRow(dcSheetRowNum, dcSheet, currentData, null, userLocale, ACTION_DELETE);
	                    break;
                }
				
			} // end of DC processing iterator
		}

		return dcExportWorkbook;
	}
	
	private int addRow(int dcSheetRowNum, XSSFSheet dcSheet, AuditDataChange currentData, ChangeData changeData, String userLocale, String action) throws NumberFormatException, Exception {
		// create the sheet ROW for this relation
		XSSFRow dCRow = dcSheet.createRow(dcSheetRowNum);
		dcSheetRowNum += 1;
		
		// add contents for this ROW
		XSSFCell appNumberCell = dCRow.createCell(0, CellType.STRING);
		appNumberCell.setCellValue(currentData.getApplication().getApplicationNumber());
		XSSFCell userNameCell = dCRow.createCell(1, CellType.STRING);
		userNameCell.setCellValue(currentData.getUser().getFirstName() + " " + currentData.getUser().getLastName());
		XSSFCell dateTimeCell = dCRow.createCell(2, CellType.STRING);
		dateTimeCell.setCellValue(currentData.getCreatedOn());
		XSSFCell actionCell = dCRow.createCell(3, CellType.STRING);
		actionCell.setCellValue(currentData.getAction());
		XSSFCell entityCell = dCRow.createCell(4, CellType.STRING);
		entityCell.setCellValue(currentData.getEntityName());
		XSSFCell entityIdCell = dCRow.createCell(5, CellType.NUMERIC);
		entityIdCell.setCellValue(currentData.getEntityId());
		XSSFCell fieldNameCell = dCRow.createCell(6, CellType.STRING);
		fieldNameCell.setCellValue(action == ACTION_DELETE ? "" : changeData.getPropertyName());
		XSSFCell oldValueCell = dCRow.createCell(7, CellType.STRING);
		switch(action) {
			case ACTION_ADD: ;
			case ACTION_DELETE : oldValueCell.setCellValue("");
				break;
			case ACTION_UPDATE: oldValueCell.setCellValue(checkAndReturnDVCodeFromID(jsonArrObject, currentData.getDomainClassName(), changeData.getPropertyName(), changeData.getNewValue(), userLocale));
		}
			
		XSSFCell newValueCell = dCRow.createCell(7, CellType.STRING);
		if(action == ACTION_DELETE)
			newValueCell.setCellValue("");
		else
			newValueCell.setCellValue(checkAndReturnDVCodeFromID(jsonArrObject, currentData.getDomainClassName(), changeData.getPropertyName(), changeData.getNewValue(), userLocale));
		
		return dcSheetRowNum;
	}

	/**
	 * @apiNote This method doesn't validates the integrity of the supplied domain
	 *          value upload excel file. Please call method
	 *          <b>'checkDVUploadFileIsValid'</b> to ensure the validity of this
	 *          upload file wherever this import method is called<br>
	 *          ---------------------------------------------------------------------
	 *          <br>
	 *          Imports all the DomainValue(s), DomainValueRelation(s) and
	 *          DomainValueDescription(s) into the system from the supplied source
	 *          excel file
	 * @param importFile
	 * @throws Exception
	 */
	public void importDomainValuesIntoSystem(MultipartFile importFile) throws Exception {

		XSSFWorkbook dvWorkbook = new XSSFWorkbook(importFile.getInputStream());

		// process the DomainValue sheet
		XSSFSheet dvSheet = dvWorkbook.getSheet(DOMAINVALUE_SHEET);
		XSSFSheet dvTranslationSheet = dvWorkbook.getSheet(DOMAINVALUE_TRANSLATION_SHEET);
		if(logger.isInfoEnabled())
			logger.info("-----Processing started for sheet '{}'", DOMAINVALUE_SHEET);

		Iterator<Row> dvSheetRowIterator = dvSheet.iterator();

		int rowCounter = 1;
		while (dvSheetRowIterator.hasNext() && rowCounter <= MAX_LOOPS) {
			Row currentDVRow = dvSheetRowIterator.next();

			if (currentDVRow.getRowNum() == 0)
				; // skip the header row
			else {
				final String dvType = (currentDVRow.getCell(0) == null) ? null
						: currentDVRow.getCell(0).getStringCellValue();
				final String dvCode = (currentDVRow.getCell(7) == null) ? null
						: (currentDVRow.getCell(7).getCellType().equals(CellType.NUMERIC) ? 
								fetchNumericCellValue(currentDVRow.getCell(7).getNumericCellValue())
									: currentDVRow.getCell(7).getStringCellValue());
				final String parent1Type = (currentDVRow.getCell(1) == null) ? null
						: currentDVRow.getCell(1).getStringCellValue();
				final String parent1Code = (currentDVRow.getCell(2) == null) ? null
						: (currentDVRow.getCell(2).getCellType().equals(CellType.NUMERIC) ?
								fetchNumericCellValue(currentDVRow.getCell(2).getNumericCellValue())	
									: currentDVRow.getCell(2).getStringCellValue());
				final String parent2Type = (currentDVRow.getCell(3) == null) ? null
						: currentDVRow.getCell(3).getStringCellValue();
				final String parent2Code = (currentDVRow.getCell(4) == null) ? null
						: (currentDVRow.getCell(4).getCellType().equals(CellType.NUMERIC) ?
								fetchNumericCellValue(currentDVRow.getCell(4).getNumericCellValue())
									: currentDVRow.getCell(4).getStringCellValue());

				// check if a non-empty DV type value is provided
				if (dvType == null || dvType.trim().equals("")) {
					if(logger.isInfoEnabled())
						logger.info("Skipping ROW processing as supplied DomainValue type is null or empty!");
					continue;
				}

				if (dvCode == null || dvCode.trim().equals("")) {
					if(logger.isInfoEnabled())
						logger.info("Skipping ROW processing as supplied DomainValue code is null or empty!");
					continue;
				}
				if(logger.isInfoEnabled())
					logger.info("-----Processing started for DV code '{}' with DV type '{}'", dvCode, dvType);

				List<DomainValueType> domainValueTypeList = domainValueTypeService.findAll("code='" + dvType + "'",
						PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "id"))).toList();

				// check if DV type with given DV type code exists for this DV record
				if (domainValueTypeList == null || domainValueTypeList.isEmpty())
					;
				else {
					DomainValueType dvTypeDTO = domainValueTypeList.get(0);
					if(logger.isInfoEnabled())
						logger.info("DomainValueType against code '{}' found!", dvTypeDTO.getCode());

					// find the DV type relation if any
					List<DomainValueTypeRelationship> dvTypeRelationList = domainValueTypeRelationshipService.findAll(
							"domainValueTypeId=" + dvTypeDTO.getId(),
							PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "domainValueTypeId")))
							.toList();

					// check when relation present for corresponding DV type
					if (dvTypeRelationList != null && !dvTypeRelationList.isEmpty()) {
						if(logger.isInfoEnabled())
							logger.info("Type relationship exists for DomainValueType with code '{}'", dvTypeDTO.getCode());

						DomainValueTypeRelationship dvTypeRelationDTO = dvTypeRelationList.get(0);

						// check for number of parents
						// 1. Two parents exist
						if (dvTypeRelationDTO.getParentDomainValueTypeId1() != null
								&& dvTypeRelationDTO.getParentDomainValueTypeId2() != null) {
							if (parent1Type != null && parent1Code != null && parent2Type != null && parent2Code != null
									&& !parent1Type.trim().equals("") && !parent1Code.trim().equals("")
									&& !parent2Type.trim().equals("") && !parent2Code.trim().equals("")) {

								// fetch DV types for both parents
								List<DomainValueType> p1DVTypeDTOList = domainValueTypeService
										.findAll("code='" + parent1Type + "'",
												PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "id")))
										.toList();

								List<DomainValueType> p2DVTypeDTOList = domainValueTypeService
										.findAll("code='" + parent2Type + "'",
												PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "id")))
										.toList();

								if (p1DVTypeDTOList == null || p2DVTypeDTOList == null || p1DVTypeDTOList.isEmpty()
										|| p2DVTypeDTOList.isEmpty())
									;
								else {
									DomainValueType p1DVTypeDTO = p1DVTypeDTOList.get(0);
									DomainValueType p2DVTypeDTO = p2DVTypeDTOList.get(0);

									List<DomainValue> p1DVDTOList = domainValueService.findAll(
											"code='" + parent1Code + "' and domainValueType=" + p1DVTypeDTO.getId(),
											PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "id")))
											.toList();

									List<DomainValue> p2DVDTOList = domainValueService.findAll(
											"code='" + parent2Code + "' and domainValueType=" + p2DVTypeDTO.getId(),
											PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "id")))
											.toList();

									if (p1DVDTOList == null || p2DVDTOList == null || p1DVDTOList.isEmpty()
											|| p2DVTypeDTOList.isEmpty())
										;
									else {
										DomainValue p1DVDTO = p1DVDTOList.get(0);
										DomainValue p2DVDTO = p2DVDTOList.get(0);

										List<DomainValue> domainValueList = domainValueService.findAll(
												"code='" + dvCode + "' and domainValueType=" + dvTypeDTO.getId(),
												PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "id")))
												.toList();

										if (domainValueList == null || domainValueList.isEmpty()) {
											// create the DV record
											DomainValue dVtoCreate = new DomainValue();
											dVtoCreate.setCode(dvCode);
											dVtoCreate.setCreatedOn(
													new Timestamp(Calendar.getInstance().getTime().getTime()));
											dVtoCreate.setIsActive((currentDVRow.getCell(6) == null) ? false
													: currentDVRow.getCell(6).getBooleanCellValue());
											dVtoCreate.setIsDefault((currentDVRow.getCell(5) == null) ? false
													: currentDVRow.getCell(5).getBooleanCellValue());
											dVtoCreate.setRankOrder((currentDVRow.getCell(8) == null) ? null
													: (currentDVRow.getCell(8).getCellType().equals(CellType.NUMERIC)
															? (((int) currentDVRow.getCell(8).getNumericCellValue()) == -1 ?
																	null : ((int) currentDVRow.getCell(8).getNumericCellValue()))
																		: convertStringToInteger(currentDVRow.getCell(8).getStringCellValue())));
											dVtoCreate.setDomainValueType(dvTypeDTO.getId());
											// User user = userManagementHandler.getLoggedInUser();
											Integer userId = (userManagementHandler.getLoggedInUser() != null
													? userManagementHandler.getLoggedInUser().getId()
													: null);
											dVtoCreate.setCreatedBy(userId);
											dVtoCreate = domainValueService.create(dVtoCreate);
											if(logger.isInfoEnabled())
												logger.info("DomainValue record with code '{}' created", dvCode);

											// create the DV relation
											DomainValueRelation dvRelationToCreate = new DomainValueRelation();
											dvRelationToCreate.setDomainValueId(dVtoCreate.getId());
											dvRelationToCreate.setParentDomainValueId1(p1DVDTO.getId());
											dvRelationToCreate.setParentDomainValueId2(p2DVDTO.getId());
											dvRelationToCreate = domainValueRelationService.create(dvRelationToCreate);
											if(logger.isInfoEnabled())
												logger.info(
													"DomainValue relation for parent1 '{}' and parent2 '{}' DV code '{}' created",
													parent1Code, parent2Code, dvCode);
										} else {
											DomainValue dVDTOtoProcess = domainValueList.get(0);
											List<DomainValueRelation> dvRelationList = domainValueRelationService
													.findAll(
															"domainValueId=" + dVDTOtoProcess.getId()
																	+ " and parentDomainValueId1=" + p1DVDTO.getId()
																	+ " and parentDomainValueId2=" + p2DVDTO.getId(),
															PageRequest.of(0, Integer.MAX_VALUE,
																	Sort.by(Sort.Direction.ASC, "domainValueId")))
													.toList();

											if (dvRelationList == null || dvRelationList.isEmpty()) {
												// update the DV record
												dVDTOtoProcess.setIsActive((currentDVRow.getCell(6) == null) ? false
														: currentDVRow.getCell(6).getBooleanCellValue());
												dVDTOtoProcess.setIsDefault((currentDVRow.getCell(5) == null) ? false
														: currentDVRow.getCell(5).getBooleanCellValue());
												dVDTOtoProcess.setRankOrder((currentDVRow.getCell(8) == null) ? null
														: (currentDVRow.getCell(8).getCellType().equals(CellType.NUMERIC)
																? (((int) currentDVRow.getCell(8).getNumericCellValue()) == -1 ?
																		null : ((int) currentDVRow.getCell(8).getNumericCellValue()))
																			: convertStringToInteger(currentDVRow.getCell(8).getStringCellValue())));
												dVDTOtoProcess.setUpdatedOn(
														new Timestamp(Calendar.getInstance().getTime().getTime()));
												Integer userId = (userManagementHandler.getLoggedInUser() != null
														? userManagementHandler.getLoggedInUser().getId()
														: null);
												dVDTOtoProcess.setUpdatedBy(userId);
												dVDTOtoProcess = domainValueService.update(dVDTOtoProcess);
												if(logger.isInfoEnabled())
													logger.info("DomainValue record with code '{}' updated", dvCode);

												// create this new relation
												DomainValueRelation dvRelationToCreate = new DomainValueRelation();
												dvRelationToCreate.setDomainValueId(dVDTOtoProcess.getId());
												dvRelationToCreate.setParentDomainValueId1(p1DVDTO.getId());
												dvRelationToCreate.setParentDomainValueId2(p2DVDTO.getId());
												dvRelationToCreate = domainValueRelationService
														.create(dvRelationToCreate);
												if(logger.isInfoEnabled())
													logger.info(
														"DomainValue relation for parent1 '{}' and parent2 '{}' DV code '{}' created",
														parent1Code, parent2Code, dvCode);
											} else {
												DomainValueRelation dvRelationDTO_currentDV = dvRelationList.get(0);

												if (dvRelationDTO_currentDV.getParentDomainValueId1() == null
														|| dvRelationDTO_currentDV.getParentDomainValueId2() == null)
													;
												else {
													if (p1DVDTO.getId() == dvRelationDTO_currentDV
															.getParentDomainValueId1()
															&& p2DVDTO.getId() == dvRelationDTO_currentDV
																	.getParentDomainValueId2()) {
														// update the DV record
														dVDTOtoProcess.setIsActive((currentDVRow.getCell(6) == null)
																? false
																: currentDVRow.getCell(6).getBooleanCellValue());
														dVDTOtoProcess.setIsDefault((currentDVRow.getCell(5) == null)
																? false
																: currentDVRow.getCell(5).getBooleanCellValue());
														dVDTOtoProcess.setRankOrder((currentDVRow.getCell(8) == null) ? null
																: (currentDVRow.getCell(8).getCellType().equals(CellType.NUMERIC)
																		? (((int) currentDVRow.getCell(8).getNumericCellValue()) == -1 ?
																				null : ((int) currentDVRow.getCell(8).getNumericCellValue()))
																					: convertStringToInteger(currentDVRow.getCell(8).getStringCellValue())));
														dVDTOtoProcess.setUpdatedOn(new Timestamp(
																Calendar.getInstance().getTime().getTime()));
														Integer userId = (userManagementHandler
																.getLoggedInUser() != null
																		? userManagementHandler.getLoggedInUser()
																				.getId()
																		: null);
														dVDTOtoProcess.setUpdatedBy(userId);
														dVDTOtoProcess = domainValueService.update(dVDTOtoProcess);
														if(logger.isInfoEnabled())
															logger.info("DomainValue record with code '{}' updated",
																dvCode);
													}
												}
											}
										}
									}
								}
							}
						}

						// 2. when only one parent dependency exists
						if (dvTypeRelationDTO.getParentDomainValueTypeId1() != null
								&& dvTypeRelationDTO.getParentDomainValueTypeId2() == null) {
							if (parent1Type == null || parent1Code == null || parent1Code.isEmpty()
									|| parent1Type.isEmpty())
								continue;
							if ((parent2Type != null && !parent2Type.isEmpty())
									|| (parent2Code != null && !parent2Code.isEmpty()))
								continue;

							// fetch DV types for both parents
							List<DomainValueType> p1DVTypeDTOList = domainValueTypeService
									.findAll("code='" + parent1Type + "'",
											PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "id")))
									.toList();

							if (p1DVTypeDTOList == null || p1DVTypeDTOList.isEmpty())
								;
							else {
								DomainValueType p1DVTypeDTO = p1DVTypeDTOList.get(0);

								List<DomainValue> p1DVDTOList = domainValueService
										.findAll(
												"code='" + parent1Code + "' and domainValueType=" + p1DVTypeDTO.getId(),
												PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "id")))
										.toList();

								if (p1DVDTOList == null || p1DVDTOList.isEmpty())
									;
								else {
									DomainValue p1DVDTO = p1DVDTOList.get(0);

									List<DomainValue> domainValueList = domainValueService.findAll(
											"code='" + dvCode + "' and domainValueType=" + dvTypeDTO.getId(),
											PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "id")))
											.toList();

									if (domainValueList == null || domainValueList.isEmpty()) {
										// create the DV record
										DomainValue dVtoCreate = new DomainValue();
										dVtoCreate.setCode(dvCode);
										dVtoCreate.setCreatedOn(
												new Timestamp(Calendar.getInstance().getTime().getTime()));
										Integer userId = (userManagementHandler.getLoggedInUser() != null
												? userManagementHandler.getLoggedInUser().getId()
												: null);
										dVtoCreate.setCreatedBy(userId);
										dVtoCreate.setIsActive((currentDVRow.getCell(6) == null) ? false
												: currentDVRow.getCell(6).getBooleanCellValue());
										dVtoCreate.setIsDefault((currentDVRow.getCell(5) == null) ? false
												: currentDVRow.getCell(5).getBooleanCellValue());
										dVtoCreate.setRankOrder((currentDVRow.getCell(8) == null) ? null
												: (currentDVRow.getCell(8).getCellType().equals(CellType.NUMERIC)
														? (((int) currentDVRow.getCell(8).getNumericCellValue()) == -1 ?
																null : ((int) currentDVRow.getCell(8).getNumericCellValue()))
																	: convertStringToInteger(currentDVRow.getCell(8).getStringCellValue())));
										dVtoCreate.setDomainValueType(dvTypeDTO.getId());
										dVtoCreate = domainValueService.create(dVtoCreate);
										if(logger.isInfoEnabled())
											logger.info("DomainValue record with code '{}' created", dvCode);

										// create the DV relation
										DomainValueRelation dvRelationToCreate = new DomainValueRelation();
										dvRelationToCreate.setDomainValueId(dVtoCreate.getId());
										dvRelationToCreate.setParentDomainValueId1(p1DVDTO.getId());
										dvRelationToCreate.setParentDomainValueId2(null);
										dvRelationToCreate = domainValueRelationService.create(dvRelationToCreate);
										if(logger.isInfoEnabled())
											logger.info("DomainValue relation for parent1 '{}' for DV code '{}' created",
												parent1Code, dvCode);
									} else {
										DomainValue dVDTOtoProcess = domainValueList.get(0);
										List<DomainValueRelation> dvRelationList = domainValueRelationService.findAll(
												"domainValueId=" + dVDTOtoProcess.getId() + " and parentDomainValueId1="
														+ p1DVDTO.getId(),
												PageRequest.of(0, Integer.MAX_VALUE,
														Sort.by(Sort.Direction.ASC, "domainValueId")))
												.toList();

										if (dvRelationList == null || dvRelationList.isEmpty()) {
											// update the DV record
											dVDTOtoProcess.setIsActive((currentDVRow.getCell(6) == null) ? false
													: currentDVRow.getCell(6).getBooleanCellValue());
											dVDTOtoProcess.setIsDefault((currentDVRow.getCell(5) == null) ? false
													: currentDVRow.getCell(5).getBooleanCellValue());
											dVDTOtoProcess.setRankOrder((currentDVRow.getCell(8) == null) ? null
													: (currentDVRow.getCell(8).getCellType().equals(CellType.NUMERIC)
															? (((int) currentDVRow.getCell(8).getNumericCellValue()) == -1 ?
																	null : ((int) currentDVRow.getCell(8).getNumericCellValue()))
																		: convertStringToInteger(currentDVRow.getCell(8).getStringCellValue())));
											dVDTOtoProcess.setUpdatedOn(
													new Timestamp(Calendar.getInstance().getTime().getTime()));
											Integer userId = (userManagementHandler.getLoggedInUser() != null
													? userManagementHandler.getLoggedInUser().getId()
													: null);
											dVDTOtoProcess.setUpdatedBy(userId);
											dVDTOtoProcess = domainValueService.update(dVDTOtoProcess);
											if(logger.isInfoEnabled())
												logger.info("DomainValue record with code '{}' updated", dvCode);

											// create the DV relation
											DomainValueRelation dvRelationToCreate = new DomainValueRelation();
											dvRelationToCreate.setDomainValueId(dVDTOtoProcess.getId());
											dvRelationToCreate.setParentDomainValueId1(p1DVDTO.getId());
											dvRelationToCreate.setParentDomainValueId2(null);
											dvRelationToCreate = domainValueRelationService.create(dvRelationToCreate);
											if(logger.isInfoEnabled())
												logger.info(
													"DomainValue relation for parent1 '{}' for DV code '{}' created",
													parent1Code, dvCode);
										} else {
											DomainValueRelation dvRelationDTO_currentDV = dvRelationList.get(0);

											if (dvRelationDTO_currentDV.getParentDomainValueId1() == null)
												;
											else {
												if (p1DVDTO.getId() == dvRelationDTO_currentDV
														.getParentDomainValueId1()) {
													// update the DV record
													dVDTOtoProcess.setIsActive((currentDVRow.getCell(6) == null) ? false
															: currentDVRow.getCell(6).getBooleanCellValue());
													dVDTOtoProcess
															.setIsDefault((currentDVRow.getCell(5) == null) ? false
																	: currentDVRow.getCell(5).getBooleanCellValue());
													dVDTOtoProcess.setRankOrder((currentDVRow.getCell(8) == null) ? null
															: (currentDVRow.getCell(8).getCellType().equals(CellType.NUMERIC)
																	? (((int) currentDVRow.getCell(8).getNumericCellValue()) == -1 ?
																			null : ((int) currentDVRow.getCell(8).getNumericCellValue()))
																				: convertStringToInteger(currentDVRow.getCell(8).getStringCellValue())));
													dVDTOtoProcess.setUpdatedOn(
															new Timestamp(Calendar.getInstance().getTime().getTime()));
													Integer userId = (userManagementHandler.getLoggedInUser() != null
															? userManagementHandler.getLoggedInUser().getId()
															: null);
													dVDTOtoProcess.setUpdatedBy(userId);
													dVDTOtoProcess = domainValueService.update(dVDTOtoProcess);
													if(logger.isInfoEnabled())
														logger.info("DomainValue record with code '{}' updated", dvCode);
												}
											}
										}
									}
								}
							}
						}

					}
					// when DV type relationship doesn't exists
					else {
						if(logger.isInfoEnabled())
							logger.info("No Type relationship exists for DomainValueType with code '{}'",
								dvTypeDTO.getCode());

						if ((parent1Type != null && !parent1Type.isEmpty())
								|| (parent2Type != null && !parent2Type.isEmpty())
								|| (parent1Code != null && !parent1Code.isEmpty())
								|| (parent2Code != null && !parent2Code.isEmpty()))
							;
						else {
							List<DomainValue> domainValueList = domainValueService
									.findAll("code='" + dvCode + "' and domainValueType=" + dvTypeDTO.getId(),
											PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "id")))
									.toList();

							if (domainValueList == null || domainValueList.isEmpty()) {
								// create the DV record
								DomainValue dVtoCreate = new DomainValue();
								dVtoCreate.setCode(dvCode);
								dVtoCreate.setCreatedOn(new Timestamp(Calendar.getInstance().getTime().getTime()));
								Integer userId = (userManagementHandler.getLoggedInUser() != null
										? userManagementHandler.getLoggedInUser().getId()
										: null);
								dVtoCreate.setCreatedBy(userId);
								dVtoCreate.setIsActive((currentDVRow.getCell(6) == null) ? false
										: currentDVRow.getCell(6).getBooleanCellValue());
								dVtoCreate.setIsDefault((currentDVRow.getCell(5) == null) ? false
										: currentDVRow.getCell(5).getBooleanCellValue());
								dVtoCreate.setRankOrder((currentDVRow.getCell(8) == null) ? null
										: (currentDVRow.getCell(8).getCellType().equals(CellType.NUMERIC)
												? (((int) currentDVRow.getCell(8).getNumericCellValue()) == -1 ?
														null : ((int) currentDVRow.getCell(8).getNumericCellValue()))
															: convertStringToInteger(currentDVRow.getCell(8).getStringCellValue())));
								dVtoCreate.setDomainValueType(dvTypeDTO.getId());
								
								dVtoCreate = domainValueService.create(dVtoCreate);
								if(logger.isInfoEnabled())
									logger.info("DomainValue record with code '{}' created", dvCode);
							} else {
								DomainValue dVDTOtoProcess = domainValueList.get(0);
								List<DomainValueRelation> dvRelationList = domainValueRelationService
										.findAll("domainValueId=" + dVDTOtoProcess.getId(), PageRequest.of(0,
												Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "domainValueId")))
										.toList();

								// only if no relation for this DV exists in system
								if (dvRelationList == null || dvRelationList.isEmpty()) {
									// update the DV record
									dVDTOtoProcess.setIsActive((currentDVRow.getCell(6) == null) ? false
											: currentDVRow.getCell(6).getBooleanCellValue());
									dVDTOtoProcess.setIsDefault((currentDVRow.getCell(5) == null) ? false
											: currentDVRow.getCell(5).getBooleanCellValue());
									dVDTOtoProcess.setRankOrder((currentDVRow.getCell(8) == null) ? null
											: (currentDVRow.getCell(8).getCellType().equals(CellType.NUMERIC)
													? (((int) currentDVRow.getCell(8).getNumericCellValue()) == -1 ?
															null : ((int) currentDVRow.getCell(8).getNumericCellValue()))
																: convertStringToInteger(currentDVRow.getCell(8).getStringCellValue())));
									dVDTOtoProcess
											.setUpdatedOn(new Timestamp(Calendar.getInstance().getTime().getTime()));
									Integer userId = (userManagementHandler.getLoggedInUser() != null
											? userManagementHandler.getLoggedInUser().getId()
											: null);
									dVDTOtoProcess.setUpdatedBy(userId);
									dVDTOtoProcess = domainValueService.update(dVDTOtoProcess);
									if(logger.isInfoEnabled())
										logger.info("DomainValue record with code '{}' updated", dvCode);
								}
							}
						}
					}
				}
				if(logger.isInfoEnabled())
					logger.info("-----Processing completed for DV code '{}' with DV type '{}'", dvCode, dvType);
			}
			if (currentDVRow.getRowNum() != 0)
				rowCounter += 1;
		} // end of processing -> DV sheet ROW(s)
		
		if(logger.isInfoEnabled())
			logger.info("-----Processing completed for sheet '{}'", DOMAINVALUE_SHEET);

		// process the DV translation sheet
		if(logger.isInfoEnabled())
			logger.info("-----Processing started for sheet '{}'", DOMAINVALUE_TRANSLATION_SHEET);

		Iterator<Row> dvTransRowIterator = dvTranslationSheet.iterator();

		rowCounter = 1;
		while (dvTransRowIterator.hasNext() && rowCounter <= MAX_LOOPS) {
			Row currentTransRow = dvTransRowIterator.next();

			if (currentTransRow.getRowNum() == 0)
				; // skip the header row
			else {
				final String dvType = currentTransRow.getCell(0) == null ? null
						: currentTransRow.getCell(0).getStringCellValue();
				final String dvCode = currentTransRow.getCell(1) == null ? null
						: (currentTransRow.getCell(1).getCellType().equals(CellType.NUMERIC) ?
								fetchNumericCellValue(currentTransRow.getCell(1).getNumericCellValue())
									: currentTransRow.getCell(1).getStringCellValue());
				final String locale = currentTransRow.getCell(2) == null ? null
						: currentTransRow.getCell(2).getStringCellValue();
				final String description = currentTransRow.getCell(3) == null ? null
						: (currentTransRow.getCell(3).getCellType().equals(CellType.NUMERIC) ?
								fetchNumericCellValue(currentTransRow.getCell(3).getNumericCellValue())
									: currentTransRow.getCell(3).getStringCellValue());

				if (dvType == null || dvType.trim().length() == 0 || dvCode == null || dvCode.trim().length() == 0
						|| locale == null || locale.trim().length() == 0 || description == null
						|| description.trim().length() == 0)
					;
				else {
					if(logger.isInfoEnabled())
						logger.info("-----Processing started for DV description under locale '{}' against DV code '{}'",
							locale, dvCode);

					List<DomainValueType> dvTypeDTOList = domainValueTypeService.findAll("code='" + dvType + "'",
							PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "id"))).toList();

					// check for valid DV type for this DV translation record
					if (dvTypeDTOList == null || dvTypeDTOList.isEmpty())
						;
					else {
						DomainValueType dvTypeDTO = dvTypeDTOList.get(0);

						List<DomainValue> domainValueList = domainValueService
								.findAll("code='" + dvCode + "' and domainValueType=" + dvTypeDTO.getId(),
										PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "id")))
								.toList();

						if (domainValueList == null || domainValueList.isEmpty())
							;
						else {
							DomainValue dvToProcess = domainValueList.get(0);
							List<DomainValueDescription> dvDescDTOList = domainValueDescriptionService.findAll(
									"domainValueId=" + dvToProcess.getId() + " and locale='" + locale + "'",
									PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "domainValueId")))
									.toList();

							if (dvDescDTOList == null || dvDescDTOList.isEmpty()) {
								// create the DV description record
								DomainValueDescription dvDescToCreate = new DomainValueDescription();
								dvDescToCreate.setDomainValueId(dvToProcess.getId());
								dvDescToCreate.setLocale(locale);
								dvDescToCreate.setDescription(description);
								dvDescToCreate = domainValueDescriptionService.create(dvDescToCreate);
								if(logger.isInfoEnabled())
									logger.info("DV description under locale '{}' against DV code '{}' created", locale,
										dvCode);
							} else {
								DomainValueDescription dvDescToUpdate = dvDescDTOList.get(0);

								// update the DV description record
								dvDescToUpdate.setDescription(description);
								dvDescToUpdate = domainValueDescriptionService.update(dvDescToUpdate);
								if(logger.isInfoEnabled())
									logger.info("DV description under locale '{}' against DV code '{}' updated", locale,
										dvCode);
							}
						}
					}
					if(logger.isInfoEnabled())
						logger.info("-----Processing completed for DV description under locale '{}' against DV code '{}'",
							locale, dvCode);
				}
			}
			if (currentTransRow.getRowNum() != 0)
				rowCounter += 1;
		} // end of processing -> DV translation sheet ROW(s)
		
		if(logger.isInfoEnabled())
			logger.info("-----Processing completed for sheet '{}'", DOMAINVALUE_TRANSLATION_SHEET);

		// close the workbook
		dvWorkbook.close();
	}

	/**
	 * Checks if the supplied domain value upload file is valid
	 * 
	 * @param importFile The supplied mutlipart upload DomainValue upload file
	 * @throws Exception
	 */
	public void checkDVUploadFileIsValid(MultipartFile importFile) throws Exception {

		if (importFile == null || importFile.isEmpty())
			throw new FileHandlerException(FILE_EMPTY_EXCEPTION_MSG);

		final String fileExtension = FilenameUtils.getExtension(importFile.getOriginalFilename());

		if (fileExtension == null || !fileExtension.toLowerCase().equals("xlsx"))
			throw new FileHandlerException(FILE_FORMAT_INVALID_EXCEPTION_MSG);

		else {
			XSSFWorkbook dvWorkbook = new XSSFWorkbook(importFile.getInputStream());

			// process the DomainValue sheet
			XSSFSheet dvSheet = dvWorkbook.getSheet(DOMAINVALUE_SHEET);
			XSSFSheet dvTranslationSheet = dvWorkbook.getSheet(DOMAINVALUE_TRANSLATION_SHEET);

			// check if valid sheets/tabs are present in supplied file
			if (dvSheet == null || checkExcelSheetEmpty(dvSheet) || dvTranslationSheet == null
					|| checkExcelSheetEmpty(dvTranslationSheet)) {
				dvWorkbook.close();
				throw new FileHandlerException(FILE_FORMAT_INVALID_EXCEPTION_MSG);
			} else {
				// check for valid columns in each sheet
				Row dvSheetHeaderRow = dvSheet.getRow(0);
				Row dvTransSheetHeaderRow = dvTranslationSheet.getRow(0);

				if (dvSheetHeaderRow == null || dvTransSheetHeaderRow == null) {
					dvWorkbook.close();
					throw new FileHandlerException(FILE_FORMAT_INVALID_EXCEPTION_MSG);
				} else {
					// 1. check for DV sheet for proper header columns
					final String dvTypeCodeCell = dvSheetHeaderRow.getCell(0) == null ? null
							: dvSheetHeaderRow.getCell(0).getStringCellValue();
					final String dvParent1TypeCell = dvSheetHeaderRow.getCell(1) == null ? null
							: dvSheetHeaderRow.getCell(1).getStringCellValue();
					final String dvParent1CodeCell = dvSheetHeaderRow.getCell(2) == null ? null
							: dvSheetHeaderRow.getCell(2).getStringCellValue();
					final String dvParent2TypeCell = dvSheetHeaderRow.getCell(3) == null ? null
							: dvSheetHeaderRow.getCell(3).getStringCellValue();
					final String dvParent2CodeCell = dvSheetHeaderRow.getCell(4) == null ? null
							: dvSheetHeaderRow.getCell(4).getStringCellValue();
					final String dvIsDefaultCell = dvSheetHeaderRow.getCell(5) == null ? null
							: dvSheetHeaderRow.getCell(5).getStringCellValue();
					final String dvIsActiveCell = dvSheetHeaderRow.getCell(6) == null ? null
							: dvSheetHeaderRow.getCell(6).getStringCellValue();
					final String dvCodeCell = dvSheetHeaderRow.getCell(7) == null ? null
							: dvSheetHeaderRow.getCell(7).getStringCellValue();
					final String dvOrderCell = dvSheetHeaderRow.getCell(8) == null ? null
							: dvSheetHeaderRow.getCell(8).getStringCellValue();

					// 2. check for DV translation sheet for proper header columns
					final String dvTransTypeCodeCell = dvTransSheetHeaderRow.getCell(0) == null ? null
							: dvTransSheetHeaderRow.getCell(0).getStringCellValue();
					final String dvTransCodeCell = dvTransSheetHeaderRow.getCell(1) == null ? null
							: dvTransSheetHeaderRow.getCell(1).getStringCellValue();
					final String dvTransLocaleCell = dvTransSheetHeaderRow.getCell(2) == null ? null
							: dvTransSheetHeaderRow.getCell(2).getStringCellValue();
					final String dvTransDescCell = dvTransSheetHeaderRow.getCell(3) == null ? null
							: dvTransSheetHeaderRow.getCell(3).getStringCellValue();

					// check for validity of tabs/columns
					if (dvTypeCodeCell == null || dvParent1TypeCell == null || dvParent1CodeCell == null
							|| dvParent2TypeCell == null || dvParent2CodeCell == null || dvIsDefaultCell == null
							|| dvIsActiveCell == null || dvCodeCell == null || dvOrderCell == null
							|| dvTransTypeCodeCell == null || dvTransCodeCell == null || dvTransLocaleCell == null
							|| dvTransDescCell == null || !dvTypeCodeCell.toLowerCase().equals("type")
							|| !dvParent1TypeCell.toLowerCase().equals("parent1type")
							|| !dvParent1CodeCell.toLowerCase().equals("parent1code")
							|| !dvParent2TypeCell.toLowerCase().equals("parent2type")
							|| !dvParent2CodeCell.toLowerCase().equals("parent2code")
							|| !dvCodeCell.toLowerCase().equals("code")
							|| !dvIsDefaultCell.toLowerCase().equals("isdefault")
							|| !dvIsActiveCell.toLowerCase().equals("isactive")
							|| !dvOrderCell.toLowerCase().equals("order")
							|| !dvTransTypeCodeCell.toLowerCase().equals("type")
							|| !dvTransCodeCell.toLowerCase().equals("domainvaluecode")
							|| !dvTransLocaleCell.toLowerCase().equals("locale")
							|| !dvTransDescCell.toLowerCase().equals("description")) {
						dvWorkbook.close();
						throw new FileHandlerException(FILE_TAB_COLUMNS_EXCEPTION_MSG);
					}
				}
			}
		}
	}

	/**
	 * Exports the list of activity logs from the specified export type
	 * <b>[ACTIVITY_LOG/APPLICATION_HISTORY_LOG]</b>. The contents exported are
	 * either the current page data as in UI or full dataset as in system
	 * 
	 * @param exportType
	 * @param userLocale
	 * @param applicationNumber
	 * @param activityType
	 * @param activityName
	 * @param isAppHistory
	 * @param createdDateStart
	 * @param createdDateEnd
	 * @param pageNumber
	 * @param pageSize
	 * @param sortProperties
	 * @return
	 * @throws Exception
	 */
	public XSSFWorkbook exportActivityLogInformation(String exportType, String userLocale, String applicationNumber,
			Integer activityType, String activityName, Boolean isAppHistory, Timestamp createdDateStart,
			Timestamp createdDateEnd, Integer pageNumber, Integer pageSize, String sortProperties) throws Exception {

		boolean callExtract = true;
		XSSFWorkbook activityLogWB = null;
		int rowNum = 0;

		// setting up the initial sheet header cells
		String sheetName = (exportType == null || exportType.isEmpty()) ? "Log" : exportType;
		activityLogWB = new XSSFWorkbook();
		XSSFSheet workbookSheet = activityLogWB.createSheet(sheetName);

		// create header row & corresponding cells
		XSSFRow headerRow = workbookSheet.createRow(rowNum);
		XSSFCell applicationNumberCell = headerRow.createCell(0, CellType.STRING);
		applicationNumberCell.setCellValue("Application Number");
		XSSFCell activityTypeCell = headerRow.createCell(1, CellType.STRING);
		activityTypeCell.setCellValue("Activity Type");
		XSSFCell activityNameCell = headerRow.createCell(2, CellType.STRING);
		activityNameCell.setCellValue("Activity Name");
		XSSFCell descriptionCell = headerRow.createCell(3, CellType.STRING);
		descriptionCell.setCellValue("Description");
		XSSFCell applicationStatusCell = headerRow.createCell(4, CellType.STRING);
		applicationStatusCell.setCellValue("Application Status");
		XSSFCell startDTCell = headerRow.createCell(5, CellType.STRING);
		startDTCell.setCellValue("Start Date and Time");
		XSSFCell endDTCell = headerRow.createCell(6, CellType.STRING);
		endDTCell.setCellValue("End Date and Time");
		XSSFCell durationCell = headerRow.createCell(7, CellType.STRING);
		durationCell.setCellValue("Duration (ms)");
		XSSFCell usernameCell = headerRow.createCell(8, CellType.STRING);
		usernameCell.setCellValue("User Name");

		// check if call to extract API is needed
		if (applicationNumber == null && pageNumber == null && pageSize == null && sortProperties == null) {
			if (isAppHistory == null || isAppHistory == false) {
				if (activityType == null && activityName == null && createdDateStart == null
						&& createdDateStart == null)
					callExtract = false;
			} else
				callExtract = false;
		}

		if (callExtract) {
			// call the API to fetch the activity logs
			ActivityLogResponseWrapper activityLogWrapper = activityServiceBS.getActivityEventLogs(userLocale,
					applicationNumber, activityType, activityName, isAppHistory, createdDateStart, createdDateEnd,
					pageNumber, pageSize, sortProperties);

			if (activityLogWrapper != null && activityLogWrapper.getActivityList() != null
					&& !activityLogWrapper.getActivityList().isEmpty()) {
				// process the activity list contents
				for (QueryActivityLogResponse activityLog : activityLogWrapper.getActivityList()) {
					rowNum += 1;

					// create content row & corresponding cells for this current activity record
					XSSFRow activityContentRow = workbookSheet.createRow(rowNum);
					XSSFCell appNumContentCell = activityContentRow.createCell(0, CellType.STRING);
					appNumContentCell.setCellValue(activityLog.getApplicationNumber());
					XSSFCell actTypeContentCell = activityContentRow.createCell(1, CellType.STRING);
					actTypeContentCell.setCellValue(activityLog.getActivityTypeDesc());
					XSSFCell actNameContentCell = activityContentRow.createCell(2, CellType.STRING);
					actNameContentCell.setCellValue(activityLog.getActivityName());
					XSSFCell descContentCell = activityContentRow.createCell(3, CellType.STRING);
					descContentCell.setCellValue(activityLog.getActivityDesc());
					XSSFCell appStatusContentCell = activityContentRow.createCell(4, CellType.STRING);
					appStatusContentCell.setCellValue(activityLog.getAppStatusDesc());
					XSSFCell startDTContentCell = activityContentRow.createCell(5, CellType.STRING);
					startDTContentCell
							.setCellValue(
									(activityLog.getActivityStartTime() != null)
											? (new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a")
													.format(activityLog.getActivityStartTime()))
											: null);
					XSSFCell endDTContentCell = activityContentRow.createCell(6, CellType.STRING);
					endDTContentCell
							.setCellValue(
									(activityLog.getActivityEndTime() != null)
											? (new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a")
													.format(activityLog.getActivityEndTime()))
											: null);
					XSSFCell durationContentCell = activityContentRow.createCell(7, CellType.STRING);
					durationContentCell.setCellValue(
							(activityLog.getActivityDuration() != null) ? activityLog.getActivityDuration().toString()
									: null);
					XSSFCell usernameContentCell = activityContentRow.createCell(8, CellType.STRING);
					usernameContentCell.setCellValue(activityLog.getActivityPerformedBy());
				}
			}
		}

		return activityLogWB;
	}

	/**
	 * Method to import the specified set of <b>ROLE, GROUP, ROLE_PERMISSION,
	 * GROUP_ROLE and USER_GROUP</b>
	 * 
	 * @param seedFile The uploaded excel file to import data from
	 */
	public void importRoleGroupUserWithAssociations(MultipartFile seedFile) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'importRoleGroupUserWithAssociations'");

		InputStream fileInStream = null;
		XSSFWorkbook seedWorkbook = null;

		if (seedFile == null || seedFile.isEmpty())
			throw new FileHandlerException(FILE_EMPTY_EXCEPTION_MSG);

		else {
			final String fileExtension = FilenameUtils.getExtension(seedFile.getOriginalFilename());
			if (fileExtension == null || !fileExtension.toLowerCase().equals("xlsx"))
				throw new FileHandlerException(FILE_FORMAT_INVALID_EXCEPTION_MSG);
			else {
				fileInStream = seedFile.getInputStream();
				seedWorkbook = new XSSFWorkbook(fileInStream);
				this.roleGroupUserSeedHandler.checkIfRoleGroupUserSeedValid(seedWorkbook);

				// processing import
				final XSSFSheet permissionSheet = seedWorkbook.getSheet(PERMISSION_SHEET);
				final XSSFSheet roleSheet = seedWorkbook.getSheet(ROLE_SHEET);
				final XSSFSheet userSheet = seedWorkbook.getSheet(USER_SHEET);
				final XSSFSheet rolePermissionSheet = seedWorkbook.getSheet(ROLE_PERMISSION_SHEET);
				final XSSFSheet groupSheet = seedWorkbook.getSheet(GROUP_SHEET);
				final XSSFSheet groupRoleSheet = seedWorkbook.getSheet(GROUP_ROLE_SHEET);
				final XSSFSheet groupUserSheet = seedWorkbook.getSheet(GROUP_USER_SHEET);

				this.roleGroupUserSeedHandler.importRoleGroupUserAndAssociations(permissionSheet, roleSheet, userSheet,
						rolePermissionSheet, groupSheet, groupRoleSheet, groupUserSheet);
				if(logger.isInfoEnabled())
					logger.info("### Completed seeding of PERMISSION, ROLE, GROUP, USER and associated relations ###");
			}
		}

		if (seedWorkbook != null)
			seedWorkbook.close();
		if (fileInStream != null)
			fileInStream.close();
	}

	/**
	 * Method to export the system maintained <b>PERMISSION, ROLE, GROUP, USER,
	 * ROLE_PERMISSION, GROUP_ROLE and USER_GROUP</b>
	 * 
	 * @return
	 * @throws Exception
	 */
	public XSSFWorkbook exportRoleGroupUserWithAssociations() throws Exception {

		XSSFWorkbook exportWorkbook = new XSSFWorkbook();
		final XSSFSheet permissionSheet = exportWorkbook.createSheet(PERMISSION_SHEET);
		final XSSFSheet roleSheet = exportWorkbook.createSheet(ROLE_SHEET);
		final XSSFSheet rolePermissionSheet = exportWorkbook.createSheet(ROLE_PERMISSION_SHEET);
		final XSSFSheet groupSheet = exportWorkbook.createSheet(GROUP_SHEET);
		final XSSFSheet groupRoleSheet = exportWorkbook.createSheet(GROUP_ROLE_SHEET);
		final XSSFSheet groupUserSheet = exportWorkbook.createSheet(GROUP_USER_SHEET);
		final XSSFSheet userSheet = exportWorkbook.createSheet(USER_SHEET);

		this.roleGroupUserSeedHandler.exportRoleGroupUserAndAssociations(permissionSheet, roleSheet,
				rolePermissionSheet, groupSheet, groupRoleSheet, groupUserSheet, userSheet);

		return exportWorkbook;
	}
	
	/**
	 * Method to import the specified set of PERMISSIONs
	 * 
	 * @param seedFile The uploaded excel file to import data from
	 */
	public void importPermissions(MultipartFile seedFile) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'importPermissions'");
		
		InputStream fileInStream = null;
		XSSFWorkbook seedWorkbook = null;

		if (seedFile == null || seedFile.isEmpty())
			throw new FileHandlerException(FILE_EMPTY_EXCEPTION_MSG);

		else {
			final String fileExtension = FilenameUtils.getExtension(seedFile.getOriginalFilename());
			if (fileExtension == null || !fileExtension.toLowerCase().equals("xlsx"))
				throw new FileHandlerException(FILE_FORMAT_INVALID_EXCEPTION_MSG);
			else {
				fileInStream = seedFile.getInputStream();
				seedWorkbook = new XSSFWorkbook(fileInStream);
				this.roleGroupUserSeedHandler.checkIfPermissionSeedValid(seedWorkbook);

				// processing import
				final XSSFSheet permissionSheet = seedWorkbook.getSheet(PERMISSION_SHEET);
				
				this.roleGroupUserSeedHandler.importPermissionsFromSheet(permissionSheet);

			}
		}

		if (seedWorkbook != null)
			seedWorkbook.close();
		if (fileInStream != null)
			fileInStream.close();
	}
	
	/**
	 * Method to import the specified set of ROLE & ROLE_PERMISSION
	 * 
	 * @param seedFile The uploaded excel file to import data from
	 */
	public void importRoleAndAssociations(MultipartFile seedFile) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'importRoleAndAssociations'");
		
		InputStream fileInStream = null;
		XSSFWorkbook seedWorkbook = null;

		if (seedFile == null || seedFile.isEmpty())
			throw new FileHandlerException(FILE_EMPTY_EXCEPTION_MSG);

		else {
			final String fileExtension = FilenameUtils.getExtension(seedFile.getOriginalFilename());
			if (fileExtension == null || !fileExtension.toLowerCase().equals("xlsx"))
				throw new FileHandlerException(FILE_FORMAT_INVALID_EXCEPTION_MSG);
			else {
				fileInStream = seedFile.getInputStream();
				seedWorkbook = new XSSFWorkbook(fileInStream);
				this.roleGroupUserSeedHandler.checkIfRoleAndAssociationsSeedValid(seedWorkbook);

				// processing import
				final XSSFSheet roleSheet = seedWorkbook.getSheet(ROLE_SHEET);
				final XSSFSheet rolePermissionSheet = seedWorkbook.getSheet(ROLE_PERMISSION_SHEET);
				
				this.roleGroupUserSeedHandler.importRoleAndPermissionAssociationFromSheet(roleSheet, rolePermissionSheet);
				if(logger.isInfoEnabled())
					logger.info("### Completed seeding of ROLE and related associations ###");
			}
		}

		if (seedWorkbook != null)
			seedWorkbook.close();
		if (fileInStream != null)
			fileInStream.close();
	}
	
	/**
	 * Method to import the specified set of GROUP, GROUP_ROLE & USER_GROUP
	 * 
	 * @param seedFile The uploaded excel file to import data from
	 */
	public void importGroupAndAssociations(MultipartFile seedFile) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'importGroupAndAssociations'");
		
		InputStream fileInStream = null;
		XSSFWorkbook seedWorkbook = null;

		if (seedFile == null || seedFile.isEmpty())
			throw new FileHandlerException(FILE_EMPTY_EXCEPTION_MSG);

		else {
			final String fileExtension = FilenameUtils.getExtension(seedFile.getOriginalFilename());
			if (fileExtension == null || !fileExtension.toLowerCase().equals("xlsx"))
				throw new FileHandlerException(FILE_FORMAT_INVALID_EXCEPTION_MSG);
			else {
				fileInStream = seedFile.getInputStream();
				seedWorkbook = new XSSFWorkbook(fileInStream);
				this.roleGroupUserSeedHandler.checkIfGroupAndAssociationsSeedValid(seedWorkbook);

				// processing import
				final XSSFSheet groupSheet = seedWorkbook.getSheet(GROUP_SHEET);
				final XSSFSheet groupRoleSheet = seedWorkbook.getSheet(GROUP_ROLE_SHEET);
				final XSSFSheet userGroupSheet = seedWorkbook.getSheet(GROUP_USER_SHEET);
				
				this.roleGroupUserSeedHandler.importGroupAndRelatedAssociationFromSheet(groupSheet, groupRoleSheet, userGroupSheet);
				
				logger.info("### Completed seeding of GROUP and related associations ###");
			}
		}

		if (seedWorkbook != null)
			seedWorkbook.close();
		if (fileInStream != null)
			fileInStream.close();
	}
	
	/**
	 * Method to import the specified set of USERs
	 * 
	 * @param seedFile The uploaded excel file to import data from
	 */
	public void importUsers(MultipartFile seedFile) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'importUsers'");
		
		InputStream fileInStream = null;
		XSSFWorkbook seedWorkbook = null;

		if (seedFile == null || seedFile.isEmpty())
			throw new FileHandlerException(FILE_EMPTY_EXCEPTION_MSG);

		else {
			final String fileExtension = FilenameUtils.getExtension(seedFile.getOriginalFilename());
			if (fileExtension == null || !fileExtension.toLowerCase().equals("xlsx"))
				throw new FileHandlerException(FILE_FORMAT_INVALID_EXCEPTION_MSG);
			else {
				fileInStream = seedFile.getInputStream();
				seedWorkbook = new XSSFWorkbook(fileInStream);
				this.roleGroupUserSeedHandler.checkIfUserSeedValid(seedWorkbook);

				// processing import
				final XSSFSheet userSheet = seedWorkbook.getSheet(USER_SHEET);
				
				this.roleGroupUserSeedHandler.importUsersFromSheet(userSheet);
				if(logger.isInfoEnabled())
					logger.info("### Completed seeding of USER ###");
			}
		}

		if (seedWorkbook != null)
			seedWorkbook.close();
		if (fileInStream != null)
			fileInStream.close();
	}

	/**
	 * Method to export the system maintained PERMISSIONs
	 * 
	 * @return
	 * @throws Exception
	 */
	public XSSFWorkbook exportPermissions() throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'exportPermissions'");

		XSSFWorkbook exportWorkbook = new XSSFWorkbook();
		final XSSFSheet permissionSheet = exportWorkbook.createSheet(PERMISSION_SHEET);

		this.roleGroupUserSeedHandler.exportPermissionsFromSystem(permissionSheet);

		return exportWorkbook;
	}
	
	/**
	 * Method to export the system maintained ROLE & ROLE_PERMISSION
	 * 
	 * @return
	 * @throws Exception
	 */
	public XSSFWorkbook exportRoleAndAssocations() throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'exportRoleAndAssocations'");

		XSSFWorkbook exportWorkbook = new XSSFWorkbook();
		final XSSFSheet roleSheet = exportWorkbook.createSheet(ROLE_SHEET);
		final XSSFSheet rolePermissionSheet = exportWorkbook.createSheet(ROLE_PERMISSION_SHEET);

		this.roleGroupUserSeedHandler.exportRoleAndPermissionAssociationFromSheet(roleSheet, rolePermissionSheet);

		return exportWorkbook;
	}
	
	/**
	 * Method to export the system maintained GROUP, GROUP_ROLE & USER_GROUP
	 * 
	 * @return
	 * @throws Exception
	 */
	public XSSFWorkbook exportGroupAndAssocations() throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'exportGroupAndAssocations'");

		XSSFWorkbook exportWorkbook = new XSSFWorkbook();
		final XSSFSheet groupSheet = exportWorkbook.createSheet(GROUP_SHEET);
		final XSSFSheet groupRoleSheet = exportWorkbook.createSheet(GROUP_ROLE_SHEET);
		final XSSFSheet userGroupSheet = exportWorkbook.createSheet(GROUP_USER_SHEET);

		this.roleGroupUserSeedHandler.exportGroupAndRelatedAssociationFromSheet(groupSheet, groupRoleSheet, userGroupSheet);

		return exportWorkbook;
	}
	
	/**
	 * Method to export the system maintained USERs
	 * 
	 * @return
	 * @throws Exception
	 */
	public XSSFWorkbook exportUsers() throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'exportUsers'");

		XSSFWorkbook exportWorkbook = new XSSFWorkbook();
		final XSSFSheet userSheet = exportWorkbook.createSheet(USER_SHEET);

		this.roleGroupUserSeedHandler.exportUsersFromSystem(userSheet);

		return exportWorkbook;
	}

	/**
	 * Checks if the supplied sheet of a workbook is empty (i.e.,) contains no
	 * <b>ROWs</b> or only one <b>ROW</b>. A single <b>ROW</b> is treated as the
	 * header row
	 * 
	 * @param sheet
	 * @return
	 */
	private boolean checkExcelSheetEmpty(XSSFSheet sheet) {
		boolean isEmpty = true;
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'checkExcelSheetEmpty'. Checking if sheet is empty");

		if (sheet == null)
			;
		else {
			Iterator<Row> sheetRows = sheet.iterator();
			while (sheetRows.hasNext()) {
				Row row = sheetRows.next();
				Iterator<Cell> rowCells = row.iterator();
				while (rowCells.hasNext()) {
					Cell cell = rowCells.next();
					if (cell == null || cell.getCellType() == CellType.BLANK)
						;
					else {
						isEmpty = false;
					}
				} // end of CELL iteration

				if (row.getRowNum() == 0 && isEmpty == false) {
					isEmpty = true;
				}

			} // end of ROW iteration
		}
		if(logger.isInfoEnabled())
			logger.info(
				"-----Inside method 'checkExcelSheetEmpty'. Supplied sheet is " + (isEmpty ? "" : "NOT ") + "empty");
		return isEmpty;
	}

	/**
	 * Converts the String value to an NUMERIC cell value. Can be used for sheet cells in case of
	 * export where the stored value in system needs to be exported as Numeric cells
	 * @param value
	 * @return
	 */
	private Object setNumericCellValue(String value) {
		try {
			if(value == null || value.isEmpty())
				return null;
			else {
				final Double doubleValue = Double.valueOf(value);
				final String doubleStr = doubleValue.toString();
				if(doubleStr.contains(".") && !Pattern.compile("\\d+\\.[0]+").matcher(doubleStr).matches())
					return doubleValue;
				else {
					long longValueFromDouble = doubleValue.longValue();
					return Long.valueOf(longValueFromDouble);
				}
			}
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Returns the correct representation of a NUMERIC cell value
	 * @param doubleValue
	 * @return
	 */
	private String fetchNumericCellValue(double doubleValue) {
		String doubleStr = String.valueOf(doubleValue);
		if(doubleStr.contains(".") && !Pattern.compile("\\d+\\.[0]+").matcher(doubleStr).matches())
			return String.valueOf(doubleValue);
		else {
			long longValueFromDouble = (long) doubleValue;
			return String.valueOf(longValueFromDouble);
		}
	}
	
	/**
	 * Converts a string representing an integer value to Integer type, otherwise null
	 * @param value
	 * @return
	 */
	private Integer convertStringToInteger(String value) {
		try {
			if(value == null || value.isEmpty())
				return null;
			else
				return Integer.parseInt(value);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	private String checkAndReturnDVCodeFromID(JSONArray jsonArrObject, String domainClassName, String propertyName, String newValue, String userLocale) throws NumberFormatException, Exception {
        
   	 if(logger.isDebugEnabled())
   		 logger.debug("EntityName:" + domainClassName + " PropertyName:" + propertyName + " newValue:" + newValue);
   
       for (Object obj : jsonArrObject) {
           JSONObject jsonObj = (JSONObject) obj;

           String entity = (String) jsonObj.get("domainClassName");

           if (entity.trim().equals(domainClassName.trim())) {
               JSONArray fields = (JSONArray) jsonObj.get("Fields");
               for (Object fieldObj : fields) {
                   String field = (String) fieldObj;
                   if (field.toUpperCase().trim().equals(propertyName.toUpperCase().trim())) {
                       if(newValue!=null && !newValue.equals("0")) {
                           newValue = getDVCodefromDVId(newValue, userLocale);
                       }
                       break;
                   }
               }
           }
       }
       return (newValue == null || newValue == "") ? "" : newValue;
   }

   private String getDVCodefromDVId(String newValue, String userLocale) {
   	 if(logger.isDebugEnabled())
   		 logger.debug("getDVCodefromDVId called on : " + newValue);
       QueryGetDomainValueByIdResponse queryGetDomainValueByIdResponse;
       try {
           queryGetDomainValueByIdResponse = domainValueServiceBS.getDomainValueByDomainValueId(Integer.parseInt(newValue), userLocale);
       } catch (Exception e) {
       	 if(logger.isErrorEnabled())
       		 logger.error("Error fetching code from id:" + newValue, e);
           return newValue;
       }
       return queryGetDomainValueByIdResponse.getCode();
   }
	
	private JSONArray readJsonFile() throws Exception {
        JSONParser parser = new JSONParser();
        JSONArray arr = new JSONArray();
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("DomainValueFields.json");
            Reader reader = new InputStreamReader(inputStream);
            arr = (JSONArray) parser.parse(reader);
        } catch (FileNotFoundException e) {
        	if(logger.isErrorEnabled())
        		logger.error("File not found while reading file", e);
            throw new Exception(e);
        } catch (IOException e) {
        	if(logger.isErrorEnabled())
        		logger.error("Fatal error occured in readJsonFile", e);
            throw new Exception(e);
        } catch (ParseException e) {
        	if(logger.isErrorEnabled())
        		logger.error("Fatal error occured in parsing the JSON file", e);
            throw new Exception(e);
        }
        return arr;
    }
}
