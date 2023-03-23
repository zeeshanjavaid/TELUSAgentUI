/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.collectiondataservice;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;

import org.springframework.beans.factory.annotation.Autowired;

import com.wavemaker.runtime.security.SecurityService;
import com.wavemaker.runtime.service.annotations.ExposeToClient;
import com.wavemaker.runtime.service.annotations.HideFromClient;

//import com.fico.dmp.collectiondataservice.model.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.wordnik.swagger.annotations.ApiOperation;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.client.model.EntityBanDetailsResponse;
import io.swagger.client.model.EntitySearchResponseArray;
import io.swagger.client.model.AssignedEntitiesInEntityViewResponseArray;
import io.swagger.client.model.AssignedEntitiesInClassicViewResponseArray;

import io.swagger.client.model.CollectionDispute;
import java.util.List;

import com.fico.core.util.ObjectMapperConfig;


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
public class CollectionDataService {

    private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(CollectionDataService.class.getName());

    @Autowired
    private SecurityService securityService;
    
    private final ObjectMapper objectMapper = new ObjectMapperConfig().customObjectMapper();


    /**
     * This is sample java operation that accepts an input from the caller and responds with "Hello".
     *
     * SecurityService that is Autowired will provide access to the security context of the caller. It has methods like isAuthenticated(),
     * getUserName() and getUserId() etc which returns the information based on the caller context.
     *
     * Methods in this class can declare HttpServletRequest, HttpServletResponse as input parameters to access the
     * caller's request/response objects respectively. These parameters will be injected when request is made (during API invocation).
     */
     @RequestMapping(value = "/entityBanDetails", method = {RequestMethod.GET})
    public List<EntityBanDetailsResponse> getEntityBanDetails(String entityId) throws Exception  {


        return objectMapper.readValue("[{\"entityId\":6766677,\"banId\":2244343,\"banStatus\":\"In Collection\",\"banName\":\"Air Canada\",\"banArAmount\":234,\"banOverdueAmount\":234,\"suppresionFlag\":true,\"disputeFlag\":false},{\"entityId\":6766677,\"banId\":2244344,\"banStatus\":\"In Collection\",\"banName\":\"Air Canada2\",\"banArAmount\":22,\"banOverdueAmount\":10,\"suppresionFlag\":true,\"disputeFlag\":false}]",
        objectMapper.getTypeFactory().constructCollectionType(List.class, EntityBanDetailsResponse.class));
        // return new Object(); 
    }
    
    @RequestMapping(value = "/dispute", method = {RequestMethod.GET})
    public List<CollectionDispute> getdispute(String banRefId) throws Exception  {

        return objectMapper.readValue("[{\"id\":1,\"href\":\"BASE_URL/dispute/1\",\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccountRef\":{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"@referredType\":\"CollectionBillingAccountRef\",\"@type\":\"EntityRef\"},\"billingAdjustmentRequestId\":\"string\",\"chargeType\":\"One-time charge\",\"customerContactName\":\"John Snow\",\"collectionExclusionIndicator\":false,\"comment\":\"string\",\"disputePrime\":\"string\",\"disputeReason\":\"BILLED CHARGES (DEEMED) INCORRECT\",\"product\":\"Business Connect\",\"statuses\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"reason\":\"string\",\"status\":\"OPEN\",\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.00Z\"},\"@type\":\"CollectionDisputeStatus\"}],\"@type\":\"CollectionDispute\"}]",
        objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionDispute.class));
        // return new Object(); 
    }
    
    ///entitySearch
    @RequestMapping(value = "/entitySearch", method = {RequestMethod.GET})
    public EntitySearchResponseArray getEntitySearch(String inputType, String inputValue, String searchMatchCriteria, String billingSystem, Integer offset, Integer limit) throws Exception  {

        return objectMapper.readValue("[{\"banId\":224434,\"banName\":\"Air Canada Toronto\",\"billingSystem\":\"CES\",\"cbucId\":1323232,\"dntlFlag\":true,\"entityId\":67666,\"entityName\":\"Air Canada Ontario\",\"entityOwner\":\"Agent123\",\"entityType\":\"RCID\",\"rcId\":323223},{\"banId\":44343,\"banName\":\"Rexdale Pharmacy\",\"billingSystem\":\"CES\",\"cbucId\":3232232,\"dntlFlag\":true,\"entityId\":676667,\"entityName\":\"Rexdale\",\"entityOwner\":\"Agent345\",\"entityType\":\"RCID\",\"rcId\":224232}]",EntitySearchResponseArray.class);
        // return new Object(); 
    }
    
    
    ///assignedEntitiesInEntityView
    @RequestMapping(value = "/assignedEntitiesInEntityView", method = {RequestMethod.GET})
    public AssignedEntitiesInEntityViewResponseArray getAssignedEntitiesInEntityView(@RequestParam(required = true) String agentId , @RequestParam(required = true) String workCategory , Integer offset, Integer limit) throws Exception  {

        return objectMapper.readValue("[{\"entityId\":6766677,\"entityType\":\"RCID\",\"rcId\":224343,\"cbucId\":7232323,\"entityName\":\"Air Canada\",\"totalBan\":10,\"totalDelinquentBans\":5,\"risk\":\"Low\",\"entityValue\":\"Low\",\"entityCollectionStatus\":\"Open\",\"manualFlag\":false,\"lastTreatment\":\"SUSP\",\"currentAr\":10,\"ar30Days\":30,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"totalAr\":400,\"totalOverDue\":390,\"entityOwnerId\":\"John123\",\"primeWorkCategory\":\"aliqua eu ut\",\"portfolioCategory\":\"SMB\",\"portfolioSubCategory\":\"PUBLIC LARGE\",\"ftnp\":true,\"disputeFlag\":true},{\"entityId\":6766678,\"entityType\":\"CBUCID\",\"rcId\":224344,\"cbucId\":723223,\"entityName\":\"Air Canada2\",\"totalBan\":10,\"totalDelinquentBans\":5,\"risk\":\"Low\",\"entityValue\":\"Low\",\"entityCollectionStatus\":\"Open\",\"manualFlag\":false,\"lastTreatment\":\"SUSP\",\"currentAr\":10,\"ar30Days\":30,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"totalAr\":400,\"totalOverDue\":390,\"entityOwnerId\":\"John123\",\"primeWorkCategory\":\"reprehenderit commodo\",\"portfolioCategory\":\"SMB\",\"portfolioSubCategory\":\"PUBLIC LARGE\",\"ftnp\":true,\"disputeFlag\":true}]",AssignedEntitiesInEntityViewResponseArray.class);
        // return new Object(); 
    }
    
        ///assignedEntitiesInEntityView
    @RequestMapping(value = "/assignedEntitiesInEntityView", method = {RequestMethod.GET})
    public AssignedEntitiesInEntityViewResponseArray getassignedEntitiesInClassicView(@RequestParam(required = true) String agentId , @RequestParam(required = true) String workCategory , Integer offset, Integer limit) throws Exception  {

        return objectMapper.readValue("[{\"banId\":256645999,\"banName\":\"NORTHLAND PROPERTIES CORPORATION\",\"cbucId\":761846,\"rcId\":392931,\"billingSystem\":\"CES\",\"currentAr\":10,\"ar30Days\":30,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"totalAr\":400,\"totalOverDue\":390,\"lastPaymentDate\":\"2022-08-29T09:12:33.001Z\",\"paymentMethod\":\"Card\",\"odRemaining\":2344390.88,\"acctStatus\":\"O\",\"statusDate\":\"2022-08-29T09:12:33.001Z\",\"dispute\":2344390.88,\"language\":\"EN\",\"marketSubSegment\":\"CBU\",\"province\":\"BC\",\"cbu\":\"BC\",\"cbucidName\":\"Air Canada\",\"rcidName\":\"NORTHLAND PROPERTIES CORPORATION\",\"subPortfolio\":\"RO-ACCOUNT\",\"entityId\":6766677,\"entityStatus\":\"In Collection\",\"entityType\":\"CBUCID\",\"entityRisk\":\"Low\",\"entityValue\":\"5\",\"entityOwnerId\":\"John123\"},{\"banId\":256645900,\"banName\":\"NORTHLAND PROPERTIES CORPORATION2\",\"cbucId\":761846,\"rcId\":392931,\"billingSystem\":\"CES\",\"currentAr\":10,\"ar30Days\":30,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"totalAr\":400,\"totalOverDue\":390,\"lastPaymentDate\":\"2022-08-29T09:12:33.001Z\",\"paymentMethod\":\"Card\",\"odRemaining\":390,\"acctStatus\":\"O\",\"statusDate\":\"2022-08-29T09:12:33.001Z\",\"dispute\":90.88,\"language\":\"EN\",\"marketSubSegment\":\"CBU\",\"province\":\"BC\",\"cbu\":\"BC\",\"cbucidName\":\"Air Canada\",\"rcidName\":\"NORTHLAND PROPERTIES CORPORATION2\",\"subPortfolio\":\"RO-ACCOUNT\",\"entityId\":6766677,\"entityStatus\":\"In Collection\",\"entityType\":\"CBUCID\",\"entityRisk\":\"Low\",\"entityValue\":\"5\",\"entityOwnerId\":\"John123\"}]",AssignedEntitiesInEntityViewResponseArray.class);
        // return new Object(); 
    }
    
     public CollectionDispute addDispute( CollectionDispute  collectionDispute) throws Exception  {
    
        // logger.info(":::::::::::In add Parr, parr amount::::::::::::::::::" + collectionPaymentArrangementCreate.getAmount());
        // logger.info(":::::::::::In add Parr, parr amount::::::::::::::::::" + collectionPaymentArrangementCreate.getInstallments());
        // if (collectionPaymentArrangementCreate.getInstallments() != null) {
        //     logger.info(":::::::::::In add Parr, parr amount::::::::::::::::::" + collectionPaymentArrangementCreate.getInstallments().get(0).getAmount());
        // }
        return collectionDispute;
        
    }


}