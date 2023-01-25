/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.filerelatedbs;

import java.util.List;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.fico.core.handlers.FileHandler;
import com.fico.core.services.FileServiceBS;
import com.fico.ps.exception.FileHandlerException;
import com.wavemaker.runtime.security.SecurityService;
import com.wavemaker.runtime.service.annotations.ExposeToClient;
import com.wavemaker.runtime.service.annotations.HideFromClient;

//import com.fico.dmp.filerelatedbs.model.*;

/**
 * This is a singleton class with all its public methods exposed as REST APIs via generated controller class.
 * To avoid exposing an API for a particular public method, annotate it with @HideFromClient.
 *
 * Method names will play a major role in defining the Http Method for the generated APIs. For example, a method name
 * that starts with delete/remove, will make the API exposed as Http Method "DELETE".
 *
 * Method Parameters of type primitives (including java.lang.String) will be exposed as Query Parameters &
 * Complex Types/Objects will become part of the Request body in the generated API.
 */
@ExposeToClient
public class FileRelatedBS {

    private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(FileRelatedBS.class.getName());

    @Autowired
    private SecurityService securityService;
    
    @Autowired
    @Qualifier("facade.FileServiceBS")
    private FileServiceBS fileServiceBS;


    
    /**
     * Bootstraps domain value types and their relationship data from the uploaded excel workbook
     * @param bootstrapExcel
     * @return
     */
    public ResponseEntity<Object> uploadDomainValueTypeAndRelations(MultipartFile bootstrapExcel) {
    	try {
			fileServiceBS.loadDomainValueTypeAndRelations(bootstrapExcel);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while bootstraping DomainValueType and DomainValueTypeRelationship.",e);
			
			//is the exception for FILE empty
			if((e.getMessage() != null) && e.getMessage().equals(FileHandler.FILE_EMPTY_EXCEPTION_MSG))
				return new ResponseEntity<Object>(FileHandler.FILE_EMPTY_EXCEPTION_MSG, HttpStatus.OK);
			else
				return new ResponseEntity<Object>(new com.fico.ps.model.Error("WS-GROUP-0000","Unknown error occurred."), HttpStatus.BAD_REQUEST);
		}
    	
    	return new ResponseEntity<Object>("SUCCESS", HttpStatus.OK);
    }
    
    /**
     * Extract DomainValues from the system
     * @return
     */
    public ResponseEntity<Object> extractDomainValues() {
    	String exportContents = null;
    	try {
			exportContents = fileServiceBS.exportDomainValues();
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while exporting DomainValues.",e);
		}
    	
    	return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
    	.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"DomainValueExport.xlsx\"")
        .body(exportContents);
    }
    
    /**
     * Import/load DomainValues into the system
     * @return
     */
    public ResponseEntity<Object> loadDomainValues(MultipartFile importFile) {
    	try {
			fileServiceBS.importDomainValues(importFile);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while importing DomainValues.",e);
	
			return new ResponseEntity<Object>(new com.fico.ps.model.Error("WS-GROUP-0000","Unknown error occurred."), HttpStatus.BAD_REQUEST);
		}
    	
    	return new ResponseEntity<Object>("SUCCESS", HttpStatus.OK);
    }
    
    /**
     * Validates the domain value upload file supplied
     * @param importFile
     * @return
     */
    public String checkDomainValueUploadFileIsValid(MultipartFile importFile) {
    	String validationMessage = "OK";
    	try {
			fileServiceBS.checkDVUploadFileIsValid(importFile);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occured while validating DomainValues upload file.", e);
			
			if(e instanceof FileHandlerException)
				validationMessage = e.getMessage() != null ? e.getMessage() : "Unknown error occurred. Please try again later";
			else
				validationMessage = "Unknown error occurred. Please try again later";
		}
    	
    	return validationMessage;
    }
    
    /**
     * Extract activity log information from the system based on associated filters
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
     */
    public ResponseEntity<Object> extractActivityLog(String exportType, String userLocale, String applicationNumber, Integer activityType, String activityName, Boolean isAppHistory, Timestamp createdDateStart, Timestamp createdDateEnd, Integer pageNumber,
			Integer pageSize, String sortProperties ) {
    	try {
			final String exportContent = fileServiceBS.exportActivityLog(exportType, userLocale, applicationNumber, activityType, activityName, isAppHistory,
					createdDateStart, createdDateEnd, pageNumber, pageSize, sortProperties);
			return new ResponseEntity<Object>(exportContent, HttpStatus.OK);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while extracting Activity logs");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
    }
    
    /**
     * Extract Data Change History log information from the system based on associated filters
     * @param exportType
     * @param userLocale
     * @param applicationId
     * @return
     */
    public ResponseEntity<Object> extractDataChangeHistoryLog(String exportType, String userLocale, Integer applicationId) {
    	try {
    		logger.info("-----------calling extractDataChangeHistoryLog-----------------");
			final String exportContent = fileServiceBS.exportDataChangeHistoryLog(exportType, userLocale, applicationId);
			return new ResponseEntity<Object>(exportContent, HttpStatus.OK);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while extracting Activity logs");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
    }
    
    /**
	 * Method to import the specified set of <b>ROLE, GROUP, ROLE_PERMISSION, GROUP_ROLE and USER_GROUP</b>
	 * @param seedFile
	 * The uploaded excel file to import data from
	 * @return
	 */
    public ResponseEntity<String> importRoleGroupUserWithAssociations(MultipartFile seedFile) {
    	try {
    		fileServiceBS.importRoleGroupUserWithAssociations(seedFile);
    		return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while importing Roles, Groups and related assocaitions", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
    }
    
    /**
	 * Method to export the system maintained <b>PERMISSION, ROLE, GROUP, USER, ROLE_PERMISSION, GROUP_ROLE and USER_GROUP</b>
	 * <br><br>
	 * <b><i>Note: The contents of the export file will be returned as a Base-64 encoded string</i></b>
	 * @return
	 * @throws Exception
	 */
	public ResponseEntity<String> exportRoleGroupUserWithAssociations_Inline() {
		try {
			final String encodedContents = fileServiceBS.exportRoleGroupUserWithAssociations_Inline();
			
			if(encodedContents != null)
				return new ResponseEntity<String>(encodedContents, HttpStatus.OK);
			else
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while exporting Roles, Groups and related associations", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Method to export the system maintained <b>PERMISSION, ROLE, GROUP, USER, ROLE_PERMISSION, GROUP_ROLE and USER_GROUP</b>
	 * <br><br>
	 * <b><i>Note: The export will be a downloadable excel file</i></b>
	 * @return
	 * @throws Exception
	 */
	public ResponseEntity<ByteArrayResource> downloadRoleGroupUserWithAssociations() {
		try {
			final ByteArrayResource br = fileServiceBS.exportRoleGroupUserWithAssociations_Downloadable();
			
			if(br != null) {
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setContentType(new MediaType("application", "force-download"));
				httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"PermissionRoleGroupUser_Export.xlsx\"");
				return new ResponseEntity<ByteArrayResource>(br, httpHeaders, HttpStatus.OK);
			}
			else
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while downloading Roles, Groups and related associations", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	/**
	 * Method to import the specified set of <b>PERMISSION</b>
	 * @param seedFile
	 * The uploaded excel file to import data from
	 * @return
	 */
    public ResponseEntity<String> importPermissions(MultipartFile seedFile) {
    	try {
    		fileServiceBS.importPermissions(seedFile);
    		return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while importing PERMISSIONs", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
    }
    
    /**
	 * Method to import the specified set of <b>Role & associations</b>
	 * @param seedFile
	 * The uploaded excel file to import data from
	 * @return
	 */
    public ResponseEntity<String> importRoleAndAssociations(MultipartFile seedFile) {
    	try {
    		fileServiceBS.importRoleAndAssociations(seedFile);
    		return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while importing ROLE & associations", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
    }
    
    /**
	 * Method to import the specified set of <b>Group & associations</b>
	 * @param seedFile
	 * The uploaded excel file to import data from
	 * @return
	 */
    public ResponseEntity<String> importGroupAndAssociations(MultipartFile seedFile) {
    	try {
    		fileServiceBS.importGroupAndAssocations(seedFile);;
    		return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while importing GROUP & associations", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
    }
    
    /**
	 * Method to import the specified set of <b>USER</b>
	 * @param seedFile
	 * The uploaded excel file to import data from
	 * @return
	 */
    public ResponseEntity<String> importUsers(MultipartFile seedFile) {
    	try {
    		fileServiceBS.importUsers(seedFile);
    		return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while importing USERs", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
    }
    
    /**
	 * Method to export the system maintained <b>PERMISSION</b>
	 * <br><br>
	 * <b><i>Note: The export will be a downloadable excel file</i></b>
	 * @return
	 * @throws Exception
	 */
	public ResponseEntity<ByteArrayResource> downloadPermissions() {
		try {
			final ByteArrayResource br = fileServiceBS.exportPermissions_Downloadable();
			
			if(br != null) {
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setContentType(new MediaType("application", "force-download"));
				httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Permissions_Export.xlsx\"");
				return new ResponseEntity<ByteArrayResource>(br, httpHeaders, HttpStatus.OK);
			}
			else
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while downloading PERMISSIONs", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Method to export the system maintained <b>Role & Associations</b>
	 * <br><br>
	 * <b><i>Note: The export will be a downloadable excel file</i></b>
	 * @return
	 * @throws Exception
	 */
	public ResponseEntity<ByteArrayResource> downloadRoleAndAssociations() {
		try {
			final ByteArrayResource br = fileServiceBS.exportRoleAndAssociations_Downloadable();
			
			if(br != null) {
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setContentType(new MediaType("application", "force-download"));
				httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"RoleAndAssociations_Export.xlsx\"");
				return new ResponseEntity<ByteArrayResource>(br, httpHeaders, HttpStatus.OK);
			}
			else
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while downloading ROLE & associations", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Method to export the system maintained <b>Group & Associations</b>
	 * <br><br>
	 * <b><i>Note: The export will be a downloadable excel file</i></b>
	 * @return
	 * @throws Exception
	 */
	public ResponseEntity<ByteArrayResource> downloadGroupAndAssociations() {
		try {
			final ByteArrayResource br = fileServiceBS.exportGroupAndAssociations_Downloadable();
			
			if(br != null) {
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setContentType(new MediaType("application", "force-download"));
				httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"GroupAndAssociations_Export.xlsx\"");
				return new ResponseEntity<ByteArrayResource>(br, httpHeaders, HttpStatus.OK);
			}
			else
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while downloading GROUP & associations", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Method to export the system maintained <b>USERs</b>
	 * <br><br>
	 * <b><i>Note: The export will be a downloadable excel file</i></b>
	 * @return
	 * @throws Exception
	 */
	public ResponseEntity<ByteArrayResource> downloadUsers() {
		try {
			final ByteArrayResource br = fileServiceBS.exportUsers_Downloadable();
			
			if(br != null) {
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setContentType(new MediaType("application", "force-download"));
				httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Users_Export.xlsx\"");
				return new ResponseEntity<ByteArrayResource>(br, httpHeaders, HttpStatus.OK);
			}
			else
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while downloading USERs", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}