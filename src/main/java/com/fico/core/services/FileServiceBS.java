package com.fico.core.services;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.sql.Timestamp;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fico.core.handlers.FileHandler;
import com.fico.dmp.telusagentuidb.DomainValueType;
import com.fico.dmp.telusagentuidb.DomainValueTypeRelationship;

@Service("facade.FileServiceBS")
public class FileServiceBS {
	
	@Autowired
	@Qualifier("handler.FileHandler")
	private FileHandler fileHandler;
	
	private static final Logger logger = LoggerFactory.getLogger(FileServiceBS.class);
	
	/**
	 * Bootstraps {@link DomainValueType} and {@link DomainValueTypeRelationship} from supplied file
	 * @param multipartFile
	 * @throws Exception
	 */
	public void loadDomainValueTypeAndRelations(MultipartFile multipartFile) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'loadDomainValueTypeAndRelations'. DomainValueType and TypeRelationship bootstrap started");
		fileHandler.loadDomainValueTypeAndRelationship(multipartFile);
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'loadDomainValueTypeAndRelations'. DomainValueType and TypeRelationship bootstrap completed");
	}
	
	/**
	 * Export the system maintained DomainValue(s) as a Base64 encoded string
	 * @return
	 * @throws Exception
	 */
	public String exportDomainValues() throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'exportDomainValues' .... processing export!");
		
		XSSFWorkbook wbook = fileHandler.exportDomainValueFromSystem();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		wbook.write(byteArrayOutputStream);
		String finalResponse = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'exportDomainValues' .... export completed!");
		//ArrayUtils.toObject(byteArrayOutputStream.toByteArray());
		
		return finalResponse; 
	}
	
	/**
	 * Import all DomainValues and associated data in the system
	 * @param importFile
	 * @throws Exception
	 */
	public void importDomainValues(MultipartFile importFile) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'importDomainValues' .... processing import!");
		
		fileHandler.importDomainValuesIntoSystem(importFile);
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'importDomainValues' .... import completed!");
	}
	
	/**
	 * Validates the supplied domain value upload file
	 * @param importFile
	 * @throws Exception
	 */
	public void checkDVUploadFileIsValid(MultipartFile importFile) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'checkDVUploadFileIsValid' .... checking domain value upload file!");
		
		fileHandler.checkDVUploadFileIsValid(importFile);
	}
	
	/**
	 * Exports activity log information based on supplied filters
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
	public String exportActivityLog(String exportType, String userLocale, String applicationNumber, Integer activityType, String activityName, Boolean isAppHistory, Timestamp createdDateStart, Timestamp createdDateEnd, Integer pageNumber,
			Integer pageSize, String sortProperties) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'exportActivityLog' .... processing export!");
		
		XSSFWorkbook exportWB = fileHandler.exportActivityLogInformation(exportType, userLocale, applicationNumber, activityType,
				activityName, isAppHistory, createdDateStart, createdDateEnd, pageNumber, pageSize, sortProperties);
		if(exportWB == null)
			return null;
		else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			exportWB.write(bos);
			if(logger.isInfoEnabled())
				logger.info("-----Inside method 'exportActivityLog' .... export completed!");
			return Base64.getEncoder().encodeToString(bos.toByteArray());
		}
	}
	
	/**
	 * Exports DataChangeHistory log information based on supplied filters
	 * @param exportType
	 * @param userLocale
	 * @param applicationId
	 * @return
	 * @throws Exception
	 */
	public String exportDataChangeHistoryLog(String exportType, String userLocale, Integer applicationId) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'exportDataChangeHistoryLog' .... processing export!");
		
		XSSFWorkbook exportWB = fileHandler.exportDataChangeHistoryFromSystem(exportType, applicationId, userLocale);
		if(exportWB == null)
			return null;
		else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			exportWB.write(bos);
			if(logger.isInfoEnabled())
				logger.info("-----Inside method 'exportActivityLog' .... export completed!");
			return Base64.getEncoder().encodeToString(bos.toByteArray());
		}
	}
	
	/**
	 * Method to import the specified set of <b>ROLE, GROUP, ROLE_PERMISSION, GROUP_ROLE and USER_GROUP</b>
	 * @param seedFile
	 * The uploaded excel file to import data from
	 * @return
	 */
	public void importRoleGroupUserWithAssociations(MultipartFile seedFile) throws Exception {
		fileHandler.importRoleGroupUserWithAssociations(seedFile);
	}
	
	/**
	 * Method to export the system maintained <b>PERMISSION, ROLE, GROUP, USER, ROLE_PERMISSION, GROUP_ROLE and USER_GROUP</b>
	 * <br><br>
	 * <b><i>Note: The contents of the export file will be returned as a Base-64 encoded string</i></b>
	 * @return
	 * @throws Exception
	 */
	public String exportRoleGroupUserWithAssociations_Inline() throws Exception {
		String encodedContents = null;
		final XSSFWorkbook exportWB = fileHandler.exportRoleGroupUserWithAssociations();
		
		if(exportWB != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			exportWB.write(bos);
			exportWB.close();
			encodedContents = Base64.getEncoder().encodeToString(bos.toByteArray());
		}
		
		return encodedContents;
	}
	
	/**
	 * Method to export the system maintained <b>PERMISSION, ROLE, GROUP, USER, ROLE_PERMISSION, GROUP_ROLE and USER_GROUP</b>
	 * <br><br>
	 * <b><i>Note: The export will be a downloadable file</i></b>
	 * @return
	 * @throws Exception
	 */
	public ByteArrayResource exportRoleGroupUserWithAssociations_Downloadable() throws Exception {
		ByteArrayResource br = null;
		final XSSFWorkbook exportWB = fileHandler.exportRoleGroupUserWithAssociations();
		
		if(exportWB != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			exportWB.write(bos);
			exportWB.close();
			br = new ByteArrayResource(bos.toByteArray());
		}
		
		return br;
	}
	
	/**
	 * Method to import the specified set of <b>PERMISSION</b>
	 * @param seedFile
	 * The uploaded excel file to import data from
	 * @return
	 */
	public void importPermissions(MultipartFile seedFile) throws Exception {
		fileHandler.importPermissions(seedFile);
	}
	
	/**
	 * Method to import the specified set of <b>ROLE and ROLE_PERMISSION</b>
	 * @param seedFile
	 * The uploaded excel file to import data from
	 * @return
	 */
	public void importRoleAndAssociations(MultipartFile seedFile) throws Exception {
		fileHandler.importRoleAndAssociations(seedFile);
	}
	
	/**
	 * Method to import the specified set of <b>GROUP, GROUP_ROLE, USER_GROUP</b>
	 * @param seedFile
	 * The uploaded excel file to import data from
	 * @return
	 */
	public void importGroupAndAssocations(MultipartFile seedFile) throws Exception {
		fileHandler.importGroupAndAssociations(seedFile);
	}
	
	/**
	 * Method to import the specified set of <b>USER</b>
	 * @param seedFile
	 * The uploaded excel file to import data from
	 * @return
	 */
	public void importUsers(MultipartFile seedFile) throws Exception {
		fileHandler.importUsers(seedFile);
	}
	
	/**
	 * Method to export the system maintained <b>PERMISSION</b>
	 * <br><br>
	 * <b><i>Note: The export will be a downloadable file</i></b>
	 * @return
	 * @throws Exception
	 */
	public ByteArrayResource exportPermissions_Downloadable() throws Exception {
		ByteArrayResource br = null;
		final XSSFWorkbook exportWB = fileHandler.exportPermissions();
		
		if(exportWB != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			exportWB.write(bos);
			exportWB.close();
			br = new ByteArrayResource(bos.toByteArray());
		}
		
		return br;
	}
	
	/**
	 * Method to export the system maintained <b>ROLE and ROLE_PERMISSION</b>
	 * <br><br>
	 * <b><i>Note: The export will be a downloadable file</i></b>
	 * @return
	 * @throws Exception
	 */
	public ByteArrayResource exportRoleAndAssociations_Downloadable() throws Exception {
		ByteArrayResource br = null;
		final XSSFWorkbook exportWB = fileHandler.exportRoleAndAssocations();
		
		if(exportWB != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			exportWB.write(bos);
			exportWB.close();
			br = new ByteArrayResource(bos.toByteArray());
		}
		
		return br;
	}
	
	/**
	 * Method to export the system maintained <b>GROUP, GROUP_ROLE and USER_GROUP</b>
	 * <br><br>
	 * <b><i>Note: The export will be a downloadable file</i></b>
	 * @return
	 * @throws Exception
	 */
	public ByteArrayResource exportGroupAndAssociations_Downloadable() throws Exception {
		ByteArrayResource br = null;
		final XSSFWorkbook exportWB = fileHandler.exportGroupAndAssocations();
		
		if(exportWB != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			exportWB.write(bos);
			exportWB.close();
			br = new ByteArrayResource(bos.toByteArray());
		}
		
		return br;
	}
	
	/**
	 * Method to export the system maintained <b>USER</b>
	 * <br><br>
	 * <b><i>Note: The export will be a downloadable file</i></b>
	 * @return
	 * @throws Exception
	 */
	public ByteArrayResource exportUsers_Downloadable() throws Exception {
		ByteArrayResource br = null;
		final XSSFWorkbook exportWB = fileHandler.exportUsers();
		
		if(exportWB != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			exportWB.write(bos);
			exportWB.close();
			br = new ByteArrayResource(bos.toByteArray());
		}
		
		return br;
	}
}
