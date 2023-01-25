/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.lookupfilebs;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.fico.core.services.LookupFileServiceBS;
import com.fico.core.services.LookupFileServiceBS.CreateUpdateLookupFileResponseWrapper;
import com.fico.core.services.LookupFileServiceBS.ListLookupFileResponseWrapper;
import com.fico.core.services.LookupFileServiceBS.ListLookupMetadataResponseWrapper;
import com.fico.core.services.LookupFileServiceBS.LookupFileInfoWrapper;
import com.fico.ps.model.CustomPageDataWrapper;
import com.wavemaker.runtime.security.SecurityService;
import com.wavemaker.runtime.service.annotations.ExposeToClient;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;

//import com.fico.dmp.lookupfilebs.model.*;

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
public class LookupFileBS {

    private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(LookupFileBS.class.getName());

    @Autowired
    private SecurityService securityService;
    
    @Autowired
    @Qualifier("facade.LookupServiceBS")
    private LookupFileServiceBS lookupFileService;
    
    


    
  public CreateUpdateLookupFileResponseWrapper createLookupFile(String identifier ,String name,String description, MultipartFile excelFile, String createdBy, String updatedBy)
    {
    	CreateUpdateLookupFileResponseWrapper result =null;
    	try {
			result= lookupFileService.createLookupFile(identifier, name, description, excelFile,createdBy,updatedBy);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(logger.isErrorEnabled())
				logger.error("Unexpected io error occurred while creating lookup file", e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while creating lookup file", e);
		}
    	
    	return result;
    }
    
    
    public CustomPageDataWrapper<ListLookupFileResponseWrapper> getLookupFileList(Map<String,String> queryParams)
    {
    	
    	CustomPageDataWrapper<ListLookupFileResponseWrapper> result= null;
    	try {
			result= lookupFileService.getLookupFileList(queryParams);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(logger.isErrorEnabled())
				logger.error("Unexpected io error occurred while listing lookup files", e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while listing lookup files", e);
		}
    	
    	return result;
    }
    
    
    public ListLookupMetadataResponseWrapper getLookupFileMetadata(String lookupId)
    {
    	ListLookupMetadataResponseWrapper result = new ListLookupMetadataResponseWrapper();
    	try {
			result= lookupFileService.getLookFileMetadata(lookupId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(logger.isErrorEnabled())
				logger.error("Unexpected io error occurred while fetching lookup metadata", e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while fetching lookup metadata", e);
		}
    	
    	return result;
    	
    	
    }
    
    
    public ResponseEntity<Object> getLookupFileById(String lookupIdentifier)
    {
    	ResponseEntity<Object> response=null;
    	try {
    		response= lookupFileService.getLookupFileById(lookupIdentifier);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(logger.isErrorEnabled())
				logger.error("Unexpected io error occurred while fetching lookup metadata", e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while fetching lookup metadata", e);
			}
	    	return response;
	    	
	    }
	    
    
    
    public LookupFileInfoWrapper getLookupFileInfoById(String lookupIdentifier)
    {
    	
    	LookupFileInfoWrapper response= null;
    	try {
    		response= lookupFileService.getLookupFileInfoById(lookupIdentifier);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(logger.isErrorEnabled())
				logger.error("Unexpected io error occurred while fetching lookup metadata", e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while fetching lookup metadata", e);
		}
    	return response;
    	
    }
    

}