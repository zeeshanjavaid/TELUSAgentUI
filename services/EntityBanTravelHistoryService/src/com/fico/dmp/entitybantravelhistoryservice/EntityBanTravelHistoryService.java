/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.entitybantravelhistoryservice;

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

import io.swagger.client.model.CollectionBillingAccountRef;
import io.swagger.client.model.CollectionEntity;
import io.swagger.client.model.CollectionEntityBillingAccountRefMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.fico.dmp.collectionentityservice.CollectionEntityService;
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
    	List<BanTravelHistoryModel> banTravelHistoryModelList = new ArrayList<BanTravelHistoryModel>();
    	if(!CollectionUtils.isEmpty(collectionEntityBillingAccountRefList)) {
        	//billingAcctRefIds = collectionEntityBillingAccountRefList.stream().map(t -> t.getBillingAccountRef().getId()).collect(Collectors.toList());
        	
        	for (CollectionEntityBillingAccountRefMap collectionEntityBillingAccountRefMap : collectionEntityBillingAccountRefList) {
        		if(collectionEntityBillingAccountRefMap.getValidFor().getEndDateTime() != null) {
        		BanTravelHistoryModel banTravelHistoryModel = new BanTravelHistoryModel();
        		banTravelHistoryModel.setBillingAccountRefId(collectionEntityBillingAccountRefMap.getBillingAccountRef().getId());
        		banTravelHistoryModel.setTransferInDT(collectionEntityBillingAccountRefMap.getValidFor().getStartDateTime().toString());
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
    				banTravelHistoryModel.setBanStatusDT(collectionBillingAccountRef.getBillingAccount().getStateDate().toString());
    				banTravelHistoryModel.setClosingCycle(collectionBillingAccountRef.getClosingCycle());
    				banTravelHistoryModel.setLastUpdatedBy(collectionBillingAccountRef.getAuditInfo().getLastUpdatedBy());
    			});
				
			}
    		
    	}
    	
    	logger.info("banTravelHistoryModelList----"+banTravelHistoryModelList.toString());
    	
    	return banTravelHistoryModelList;
    }

}