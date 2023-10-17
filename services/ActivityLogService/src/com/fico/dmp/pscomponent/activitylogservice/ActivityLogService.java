/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.pscomponent.activitylogservice;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fico.core.services.ActivityServiceBS;
import com.fico.core.services.ActivityServiceBS.ActivityLogResponseWrapper;
import com.fico.core.services.ActivityServiceBS.ActivityType;
import com.fico.ps.model.workflow.ProcessExecutionHistoryVO;
import com.wavemaker.runtime.service.annotations.ExposeToClient;


@ExposeToClient
public class ActivityLogService {

    @Autowired
	@Qualifier("facade.ActivityServiceBS")
	private ActivityServiceBS activityServiceBS;
    
    
    private static final Logger logger = LoggerFactory.getLogger(ActivityLogService.class);

   /**
    * Saves FAWB event history as activity to be viewed under activity log/application history page
    * @param applicationId
    * @param applicationStatus
    * @param activityName
    * @param activityDesc
    * @param activityStatus
    * @param activityUser
    * @param activityStartTime
    * @param activityEndTime
    * @param isActivityInError
    * @param dataPayload
    * @param activityType
    * @return
    */
    public ResponseEntity<Object> saveFAWBEventHistory(Integer applicationId, Integer applicationStatus, String activityName,
			String activityDesc, String activityStatus, String activityUser, Timestamp activityStartTime,
			Timestamp activityEndTime, boolean isActivityInError, Byte[] dataPayload, Boolean isRequestPayload, ActivityType activityType) {
    	try {
    		activityServiceBS.saveFAWBEventHistory(applicationId, applicationStatus, activityName, activityDesc, activityStatus,
    				activityUser, activityStartTime, activityEndTime, isActivityInError, dataPayload, isRequestPayload, activityType);
    		return new ResponseEntity<>(HttpStatus.OK);
    	}
    	catch (Exception e) {
    		if(logger.isErrorEnabled())
    			logger.error("Unexpected error occurred while saving FAWB event history", e);
			return new ResponseEntity<Object>(new com.fico.ps.model.Error("WS-GROUP-0000","Unknown error occurred."), HttpStatus.BAD_REQUEST);
		}
 	}
    
    /**
     * Saves ProcessDesginer event history as activity to be viewed under activity log/application history page
     * @param applicationId
     * @param applicationStatus
     * @param transactionHistory
     * @param subProcessTypesToExtract
     * @return
     */
    public ResponseEntity<Object> saveProcessDesignerEventHistory(Integer applicationId, Integer applicationStatus,
			ProcessExecutionHistoryVO transactionHistory, ActivityType... subProcessTypesToExtract) {
    	try {
    		activityServiceBS.saveProcessDesignerEventHistory(applicationId, applicationStatus, transactionHistory, subProcessTypesToExtract);
    		return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while saving ProcessDesigner event history", e);
			return new ResponseEntity<Object>(new com.fico.ps.model.Error("WS-GROUP-0000","Unknown error occurred."), HttpStatus.BAD_REQUEST);
    	}
    }

    /**
     * Returns the list of activity logs from the system
     * @param userLocale
     * @param applicationNumber
     * @param activityType
     * @param activityName
     * @param createdDateStart
     * @param createdDateEnd
     * @param pageNumber
     * @param pageSize
     * @param sortProperties
     * @return
     */
    public ActivityLogResponseWrapper getActivityEventLogs(String userLocale, String applicationNumber, Integer activityType, String activityName, Boolean isAppHistory, Timestamp createdDateStart, Timestamp createdDateEnd, Integer pageNumber,
    Integer pageSize, String sortProperties) {
    	ActivityLogResponseWrapper searchResponse = null;
    	try {
			searchResponse = activityServiceBS.getActivityEventLogs(userLocale, applicationNumber, activityType, activityName, isAppHistory, createdDateStart, createdDateEnd, pageNumber, pageSize, sortProperties);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while fetching Activity log information", e);
    	}
    	

    	return searchResponse;
    }
    
    
    
    
}
