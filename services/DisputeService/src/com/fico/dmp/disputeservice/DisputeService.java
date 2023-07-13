/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.disputeservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;

import io.swagger.client.model.CollectionBillingAccountRef;
import io.swagger.client.model.CollectionDispute;

import org.springframework.beans.factory.annotation.Autowired;

import com.fico.dmp.collectionentityservice.CollectionEntityService;
import com.fico.qb.query.builder.support.utils.spring.CollectionUtils;
import com.fico.telus.model.DisputeModel;
import com.wavemaker.runtime.security.SecurityService;
import com.wavemaker.runtime.service.annotations.ExposeToClient;
import com.wavemaker.runtime.service.annotations.HideFromClient;

//import com.fico.dmp.disputeservice.model.*;

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
public class DisputeService {

    private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(DisputeService.class.getName());

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
     */
    public List<DisputeModel> getAllDisputes() {
        logger.info("Inside retrieveAllDisputes method");
        List<DisputeModel> disputeModelList = new ArrayList<DisputeModel>();
        try {
        	List<CollectionDispute> collectionDisputeList = collectionEntityService.getdispute(null, null, null, null, null);
        	
        	List<String> billingAcctRefIds = new ArrayList<String>();
        	if(!CollectionUtils.isEmpty(collectionDisputeList)) {
        		billingAcctRefIds = collectionDisputeList.stream().map(t -> t.getBillingAccountRef().getId()).collect(Collectors.toList());
        		
        		for (CollectionDispute collectionDispute : collectionDisputeList) {
        			DisputeModel disputeModel = new DisputeModel();
        			disputeModel.setId(collectionDispute.getId());
        			disputeModel.setBan(collectionDispute.getBillingAccountRef().getId());
        			disputeModel.setDisputeAmount(collectionDispute.getAmount());
        			disputeModel.setCollectionExclusion(collectionDispute.isCollectionExclusionIndicator());
        			disputeModel.setStatus(collectionDispute.getStatus());
        			disputeModel.setCreatedBy(collectionDispute.getAuditInfo().getCreatedBy());
        			disputeModel.setCreatedDateTime(Date.from(collectionDispute.getAuditInfo().getCreatedDateTime().toInstant()));
        			disputeModel.setUpdatedBy(collectionDispute.getAuditInfo().getLastUpdatedBy());
        			disputeModel.setUpdatedDateTime(Date.from(collectionDispute.getAuditInfo().getLastUpdatedDateTime().toInstant()));
        			disputeModelList.add(disputeModel);
				}
        		
        	}
        	
        	String billingAcctRefIdsInListAsString = billingAcctRefIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        	logger.info("billingAcctRefIdsInList----"+billingAcctRefIdsInListAsString);
        	String fields = "id,billingAccount.id,billingAccount.name,billingSystemName";
        	//String idInQuery = billingAcctRefIdsInListAsString;
        	List<CollectionBillingAccountRef> collectionBillingAccountRefList =  collectionEntityService.getBillingAccountRef(fields, null, null, null, null, billingAcctRefIdsInListAsString);
        	if(!CollectionUtils.isEmpty(collectionBillingAccountRefList)) {
        		for (DisputeModel disputeModel : disputeModelList) {
        			collectionBillingAccountRefList.stream().filter(cel -> cel.getId().equals(Integer.valueOf(disputeModel.getBan()))).forEach(collectionBillingAccountRef -> {
        				disputeModel.setBanName(collectionBillingAccountRef.getBillingAccount().getName());
        				disputeModel.setBillingSystem(collectionBillingAccountRef.getBillingSystemName());
        			});
				}
        		
        		logger.info("disputeModelList---"+disputeModelList.toString());
        	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return disputeModelList;
        
    }

}