/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.entitybantravelhistoryservice;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;

import io.swagger.client.model.BillingAccount.StateEnum;
import io.swagger.client.model.CollectionBillingAccountRef;
import io.swagger.client.model.CollectionEntity;
import io.swagger.client.model.CollectionEntityBillingAccountRefMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.fico.dmp.collectionentityservice.CollectionEntityService;
import com.fico.dmp.commonutilityservice.CommonUtilityService;
import com.fico.qb.query.builder.support.utils.spring.CollectionUtils;
import com.fico.telus.model.BanTravelHistoryModel;
import com.wavemaker.runtime.security.SecurityService;
import com.wavemaker.runtime.service.annotations.ExposeToClient;
import com.wavemaker.runtime.service.annotations.HideFromClient;

//import com.fico.dmp.entitybantravelhistoryservice.model.*;

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
public class EntityBanTravelHistoryService {

    private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(EntityBanTravelHistoryService.class.getName());

    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private CollectionEntityService collectionEntityService;
    
    @Autowired
    private CommonUtilityService commonUtilityService;

    /**
     * This is sample java operation that accepts an input from the caller and responds with "Hello".
     *
     * SecurityService that is Autowired will provide access to the security context of the caller. It has methods like isAuthenticated(),
     * getUserName() and getUserId() etc which returns the information based on the caller context.
     *
     * Methods in this class can declare HttpServletRequest, HttpServletResponse as input parameters to access the
     * caller's request/response objects respectively. These parameters will be injected when request is made (during API invocation).
     * @throws Exception 
     */
    public List<BanTravelHistoryModel> fetchEntityBanTravelHistory(Integer id) throws Exception {
    	CollectionEntity collectionEntity = collectionEntityService.getCollectionEntityById(id, true);
    	List<CollectionEntityBillingAccountRefMap> collectionEntityBillingAccountRefList = collectionEntity.getBillingAccountRefMaps();
    	List<String> billingAcctRefIds = new ArrayList<String>();
    	
    	OffsetDateTime entityStartOffsetDateTime = collectionEntity.getValidFor().getStartDateTime();
    	int entityDateTimeYear = entityStartOffsetDateTime.getYear();
    	int entityDateTimeMonth = entityStartOffsetDateTime.getMonthValue();
    	int entityDateTimeDay = entityStartOffsetDateTime.getDayOfMonth();
    	int entityDateTimeHour = entityStartOffsetDateTime.getHour();
    	int entityDateTimeMin = entityStartOffsetDateTime.getMinute();
    	
    	logger.info("Entity level--"+entityDateTimeYear+""+entityDateTimeMonth+""+entityDateTimeDay+""+entityDateTimeHour+""+entityDateTimeMin);
    	
    	
    	List<BanTravelHistoryModel> banTravelHistoryModelList = new ArrayList<BanTravelHistoryModel>();
    	if(!CollectionUtils.isEmpty(collectionEntityBillingAccountRefList)) {
        	//billingAcctRefIds = collectionEntityBillingAccountRefList.stream().map(t -> t.getBillingAccountRef().getId()).collect(Collectors.toList());
        	
        	for (CollectionEntityBillingAccountRefMap collectionEntityBillingAccountRefMap : collectionEntityBillingAccountRefList) {
            	OffsetDateTime entityBillingAccRefOffsetDateTime = collectionEntityBillingAccountRefMap.getValidFor().getStartDateTime();
            	int entityBillingAccRefDateTimeYear = entityBillingAccRefOffsetDateTime.getYear();
            	int entityBillingAccRefDateTimeMonth = entityBillingAccRefOffsetDateTime.getMonthValue();
            	int entityBillingAccRefDateTimeDay = entityBillingAccRefOffsetDateTime.getDayOfMonth();
            	int entityBillingAccRefDateTimeHour = entityBillingAccRefOffsetDateTime.getHour();
            	int entityBillingAccRefDateTimeMin = entityBillingAccRefOffsetDateTime.getMinute();
            	
            	logger.info("BillingAccount level--"+entityBillingAccRefDateTimeYear+""+entityBillingAccRefDateTimeMonth+""+entityBillingAccRefDateTimeDay+""+entityBillingAccRefDateTimeHour+""+entityBillingAccRefDateTimeMin);
            	
            	boolean eligibleForTransferBanHistory = false;
            	if(entityBillingAccRefDateTimeYear == entityDateTimeYear && entityBillingAccRefDateTimeMonth == entityDateTimeMonth && entityBillingAccRefDateTimeDay == entityDateTimeDay) {
            		if(entityBillingAccRefDateTimeHour != entityDateTimeHour) {
            			eligibleForTransferBanHistory = true;
            		}else {
            			eligibleForTransferBanHistory = false;
            		}
            	}else {
            		eligibleForTransferBanHistory = true;
            	}
            	
        		if(eligibleForTransferBanHistory || collectionEntityBillingAccountRefMap.getValidFor().getEndDateTime() != null) {
	        		BanTravelHistoryModel banTravelHistoryModel = new BanTravelHistoryModel();
	        		banTravelHistoryModel.setBillingAccountRefId(collectionEntityBillingAccountRefMap.getBillingAccountRef().getId());
	        		banTravelHistoryModel.setTransferInDT(collectionEntityBillingAccountRefMap.getValidFor().getStartDateTime().toString());
	        		banTravelHistoryModel.setLastUpdatedBy(commonUtilityService.getNameUsingEmpId(collectionEntity.getAuditInfo().getLastUpdatedBy()));
	        		if(collectionEntityBillingAccountRefMap.getValidFor().getEndDateTime() != null)
	        		banTravelHistoryModel.setTransferOutDT(collectionEntityBillingAccountRefMap.getValidFor().getEndDateTime().toString());
	        		billingAcctRefIds.add(collectionEntityBillingAccountRefMap.getBillingAccountRef().getId());
	        		banTravelHistoryModelList.add(banTravelHistoryModel);
        		}
			}
    	}
    	
    	String billingAcctRefIdsInListAsString = null;
    	if(billingAcctRefIds != null && billingAcctRefIds.size() > 0) {
    		 billingAcctRefIdsInListAsString = billingAcctRefIds.stream().map(String::valueOf).collect(Collectors.joining(","));
    		logger.info("billingAcctRefIdsInList----"+billingAcctRefIdsInListAsString);
    	}
    	List<CollectionBillingAccountRef> collectionBillingAccountRefList =  collectionEntityService.getBillingAccountRef(null, null, null, null, null, billingAcctRefIdsInListAsString);
    	
    	if(!CollectionUtils.isEmpty(collectionBillingAccountRefList)) {
    		
    		for (BanTravelHistoryModel banTravelHistoryModel : banTravelHistoryModelList) {
    			
    			collectionBillingAccountRefList.stream().filter(cel -> cel.getId().equals(Integer.valueOf(banTravelHistoryModel.getBillingAccountRefId()))).forEach(collectionBillingAccountRef -> {
    				banTravelHistoryModel.setBanId(collectionBillingAccountRef.getBillingAccount().getId());
    				banTravelHistoryModel.setBanStatus(collectionBillingAccountRef.getBillingAccount().getState().toString());
    				if(collectionBillingAccountRef.getBillingAccount().getStateDate() != null) {
    						banTravelHistoryModel.setBanStatusDT(collectionBillingAccountRef.getBillingAccount().getStateDate().toString());
    				}
    				
    				if(collectionBillingAccountRef.getBillingAccount().getState() == StateEnum.O) {
    					banTravelHistoryModel.setClosingCycle(null);
    				}else {
    					banTravelHistoryModel.setClosingCycle(collectionBillingAccountRef.getClosingCycle());
    				}

    				
    				
    			});
				
			}
    		
    	}
    	
    	logger.info("banTravelHistoryModelList----"+banTravelHistoryModelList.toString());
    	
    	return banTravelHistoryModelList;
    }

}