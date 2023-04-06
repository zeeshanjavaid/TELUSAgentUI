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
import io.swagger.client.model.AssignedActionsResponseArray;

import io.swagger.client.model.EntityContactsResponse;
import io.swagger.client.model.EntityDetailsResponse;
import io.swagger.client.model.EntityBanDetailsResponse;

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



    
    ///searchEntity
    @RequestMapping(value = "/entitySearch", method = {RequestMethod.GET})
    public EntitySearchResponseArray getEntitySearch(@RequestParam(required = true)String inputType, @RequestParam(required = true)String inputValue, @RequestParam(required = true)String level, @RequestParam(required = true)String searchMatchCriteria, @RequestParam(required = true)String billingSystem, Integer offset, Integer limit) throws Exception  {

        return objectMapper.readValue("[{\"banId\": 224434,\"banName\": \"Air Canada Toronto\",\"billingSystem\": \"CES\",\"rcId\": 323223,\"cbucId\": 1323232,\"entityId\": 67666,\"entityName\": \"Air Canada Ontario\",\"entityType\": \"RCID\",\"entityOwner\": \"Agent123\",\"dntlFlag\": true},{\"banId\": 24343,\"banName\": \"Rexdale Pharmacy\",\"billingSystem\": \"CES\",\"rcId\": 224232,\"cbucId\": 3232232,\"entityId\": 676667,\"entityName\": \"Rexdale\",\"entityType\": \"RCID\",\"entityOwner\": \"Agent345\",\"dntlFlag\": false},    {\"banId\": 44343,\"banName\": \"first Shop\",\"billingSystem\": \"CES\",\"rcId\": 222456,\"cbucId\": 3232233,\"entityId\": 676689,\"entityName\": \"first\",\"entityType\": \"CBUCID\",\"entityOwner\": \"Abc xyz\",\"dntlFlag\": true},{\"banId\": 44353,\"banName\": \"Rexdale here\",\"billingSystem\": \"CES\",\"rcId\": 23456,\"cbucId\": 323211,\"entityId\": 57609,\"entityName\": \"Rexdale\",\"entityType\": \"BAN\",\"entityOwner\": \"Agent345\",\"dntlFlag\": true},{\"banId\": 44343,\"banName\": \"first Shop\",\"billingSystem\": \"CESE\",\"rcId\": 222456,\"cbucId\": 3232233,\"entityId\": 676689,\"entityName\": \"first\",\"entityType\": \"CBUCID\",\"entityOwner\": \"Abc xyz\",\"dntlFlag\": true},{\"banId\": 84303,\"banName\": \"Titan 235\",\"billingSystem\": \"CESE\",\"rcId\": 222456,\"cbucId\": 3232233,\"entityId\": 67666,\"entityName\": \"Ent\",\"entityType\": \"RCID\",\"entityOwner\": \"Tom Bill\",\"dntlFlag\": true},{\"banId\": 74345,\"banName\": \"Number character\",\"billingSystem\": \"CES\",\"rcId\": 2224890,\"cbucId\": 3230933,\"entityId\": 6787866,\"entityName\": \"Num\",\"entityType\": \"CBUCID\",\"entityOwner\": \"Venus Bill\",\"dntlFlag\": false},{\"banId\": 56581,\"banName\": \"Sixth entity\",\"billingSystem\": \"CES\",\"rcId\": 222780,\"cbucId\": 3290933,\"entityId\": 670966,\"entityName\": \"Number\",\"entityType\": \"BAN\",\"entityOwner\": \"Harry Jack\",\"dntlFlag\": false},{\"banId\": 73581,\"banName\": \"Raven Bay\",\"billingSystem\": \"CES\",\"rcId\": 222780,\"cbucId\": 3290933,\"entityId\": 670966,\"entityName\": \"Number\",\"entityType\": \"BAN\",\"entityOwner\": \"Ham 345\",\"dntlFlag\": false},{\"banId\": 534781,\"banName\": \"This is ban Name for the banID 55781, CES\",\"billingSystem\": \"CES\",\"rcId\": 0,\"cbucId\": 32586,\"entityId\": 1209066,\"entityName\": \"letter\",\"entityType\": \"RCID\",\"entityOwner\": \"Veronica Shell\",\"dntlFlag\": false},{\"banId\": 55581,\"banName\": \"Shelly890\",\"billingSystem\": \"CES\",\"rcId\": 90900,\"cbucId\": 322343,\"entityId\": 27654,\"entityName\": \"Shell78\",\"entityType\": \"BAN\",\"entityOwner\": \"Owner\",\"dntlFlag\": true},{\"banId\": 557821,\"banName\": \"Raven bay\",\"billingSystem\": \"CES\",\"rcId\": 9056910,\"cbucId\": 32443,\"entityId\": 27854,\"entityName\": \"ent name\",\"entityType\": \"CBUCID\",\"entityOwner\": \"EMt Owner\",\"dntlFlag\": true},{\"banId\": 5781,\"banName\": \"Shril 890\",\"billingSystem\": \"CES\",\"rcId\": 90890,\"cbucId\": 3243,\"entityId\": 2234,\"entityName\": \"Vero Roll\",\"entityType\": \"CBUCID\",\"entityOwner\": \"Shri\",\"dntlFlag\": true},{\"banId\": 576881,\"banName\": \"Raven bay\",\"billingSystem\": \"CES CES\",\"rcId\": 908340,\"cbucId\": 31243,\"entityId\": 22254,\"entityName\": \"ent name\",\"entityType\": \"CBUCID\",\"entityOwner\": \"EMt Owner\",\"dntlFlag\": true},{\"banId\": 98765,\"banName\": \"Raven bay\",\"billingSystem\": \"CES CES\",\"rcId\": 72390,\"cbucId\": 329943,\"entityId\": 27054,\"entityName\": \"ent name\",\"entityType\": \"CBUCID\",\"entityOwner\": \"EMt Owner\",\"dntlFlag\": false},{\"banId\": 8796,\"banName\": \"Testing 123 Lookup\",\"billingSystem\": \"CES\",\"rcId\": 91090,\"cbucId\": 32783,\"entityId\": 290,\"entityName\": \"search66\",\"entityType\": \"RCID\",\"entityOwner\": \"EMt Owner\",\"dntlFlag\": false},{\"banId\": 87096,\"banName\": \"John Kay\",\"billingSystem\": \"CES\",\"rcId\": 91120,\"cbucId\": 56797,\"entityId\": 555,\"entityName\": \"Faly341\",\"entityType\": \"Account Group\",\"entityOwner\": \"EMt Owner\",\"dntlFlag\": true},{\"banId\": 16096,\"banName\": \"Johny Brown kay\",\"billingSystem\": \"CES\",\"rcId\": 89020,\"cbucId\": 56,\"entityId\": 6605,\"entityName\": \"Dalerex\",\"entityType\": \"RCID\",\"entityOwner\": \"Entity#89\",\"dntlFlag\": true},{\"banId\": 906,\"banName\": \"US Entity\",\"billingSystem\": \"CES\",\"rcId\": 8820,\"cbucId\": 906,\"entityId\": 898989,\"entityName\": \"US\",\"entityType\": \"CBUCID\",\"entityOwner\": \"JoyBill45\",\"dntlFlag\": true},{\"banId\": 16096,\"banName\": \"Sheron product\",\"billingSystem\": \"CES\",\"rcId\": 9090020,\"cbucId\": 126,\"entityId\": 1,\"entityName\": \"Shron\",\"entityType\": \"RCID\",\"entityOwner\": \"Entity Owning\",\"dntlFlag\": true},{\"banId\": 16,\"banName\": \"Sheron product\",\"billingSystem\": \"CES\",\"rcId\": 50020,\"cbucId\": 126,\"entityId\": 1,\"entityName\": \"Shron\",\"entityType\": \"Account Group\",\"entityOwner\": \"Katie78\",\"dntlFlag\": false},{\"banId\": 1906,\"banName\": \"Zeb xuv\",\"billingSystem\": \"CES\",\"rcId\": 47834,\"cbucId\": 196,\"entityId\": 100,\"entityName\": \"Zeby\",\"entityType\": \"CBUCID\",\"entityOwner\": \"Shaolin@678\",\"dntlFlag\": false},{\"banId\": 896,\"banName\": \"Quera Desire\",\"billingSystem\": \"CES\",\"rcId\": 378,\"cbucId\": 8996,\"entityId\": 90900,\"entityName\": \"Query\",\"entityType\": \"CBUCID\",\"entityOwner\": \"Eric Shellon\",\"dntlFlag\": false},{\"banId\": 9006,\"banName\": \"Swift BAN name\",\"billingSystem\": \"CES\",\"rcId\": 674,\"cbucId\": 7096,\"entityId\": 45677,\"entityName\": \"Swery Elik\",\"entityType\": \"BAN\",\"entityOwner\": \"Navon Sam713\",\"dntlFlag\": false},{\"banId\": 834906,\"banName\": \"Marian banname\",\"billingSystem\": \"CES\",\"rcId\": 7834,\"cbucId\": 566,\"entityId\": 10956,\"entityName\": \"Marry ghf\",\"entityType\": \"Account Group\",\"entityOwner\": \"Marina Fred\",\"dntlFlag\": true}]",EntitySearchResponseArray.class);
        // return new Object(); 
    }
    
    
    ///assignedEntitiesInEntityView
    @RequestMapping(value = "/assignedEntitiesInEntityView", method = {RequestMethod.GET})
    public AssignedEntitiesInEntityViewResponseArray getAssignedEntitiesInEntityView(@RequestParam(required = true) String agentId , @RequestParam(required = true) String workCategory, Integer offset, Integer limit) throws Exception  {

        return objectMapper.readValue("[{\"entityId\":6766677,\"entityType\":\"RCID\",\"rcId\":224343,\"cbucId\":7232323,\"entityName\":\"Air Canada\",\"totalBan\":10,\"totalDelinquentBans\":5,\"risk\":\"Low\",\"entityValue\":\"Low\",\"entityCollectionStatus\":\"Open\",\"manualFlag\":false,\"lastTreatment\":\"SUSP\",\"currentAr\":10,\"ar30Days\":30,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"totalAr\":400,\"totalOverDue\":390,\"entityOwnerId\":\"John123\",\"primeWorkCategory\":\"aliqua eu ut\",\"portfolioCategory\":\"SMB\",\"portfolioSubCategory\":\"PUBLIC LARGE\",\"ftnp\":true,\"disputeFlag\":true},{\"entityId\":6766678,\"entityType\":\"CBUCID\",\"rcId\":224344,\"cbucId\":723223,\"entityName\":\"Air Canada2\",\"totalBan\":10,\"totalDelinquentBans\":5,\"risk\":\"Low\",\"entityValue\":\"Low\",\"entityCollectionStatus\":\"Open\",\"manualFlag\":false,\"lastTreatment\":\"SUSP\",\"currentAr\":10,\"ar30Days\":30,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"totalAr\":400,\"totalOverDue\":390,\"entityOwnerId\":\"John123\",\"primeWorkCategory\":\"reprehenderit commodo\",\"portfolioCategory\":\"SMB\",\"portfolioSubCategory\":\"PUBLIC LARGE\",\"ftnp\":true,\"disputeFlag\":true}]",AssignedEntitiesInEntityViewResponseArray.class);
        // return new Object(); 
    }
    
        ///assignedEntitiesInEntityView
    @RequestMapping(value = "/assignedEntitiesInClassicView", method = {RequestMethod.GET})
    public AssignedEntitiesInEntityViewResponseArray getassignedEntitiesInClassicView(@RequestParam(required = true) String agentId , @RequestParam(required = true) String workCategory, Integer offset, Integer limit) throws Exception  {

        return objectMapper.readValue("[{\"banId\":256645999,\"banName\":\"NORTHLAND PROPERTIES CORPORATION\",\"cbucId\":761846,\"rcId\":392931,\"billingSystem\":\"CES\",\"currentAr\":10,\"ar30Days\":30,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"totalAr\":400,\"totalOverDue\":390,\"lastPaymentDate\":\"2022-08-29T09:12:33.001Z\",\"paymentMethod\":\"Card\",\"odRemaining\":2344390.88,\"acctStatus\":\"O\",\"statusDate\":\"2022-08-29T09:12:33.001Z\",\"dispute\":2344390.88,\"language\":\"EN\",\"marketSubSegment\":\"CBU\",\"province\":\"BC\",\"cbu\":\"BC\",\"cbucidName\":\"Air Canada\",\"rcidName\":\"NORTHLAND PROPERTIES CORPORATION\",\"subPortfolio\":\"RO-ACCOUNT\",\"entityId\":6766677,\"entityStatus\":\"In Collection\",\"entityType\":\"CBUCID\",\"entityRisk\":\"Low\",\"entityValue\":\"5\",\"entityOwnerId\":\"John123\"},{\"banId\":256645900,\"banName\":\"NORTHLAND PROPERTIES CORPORATION2\",\"cbucId\":761846,\"rcId\":392931,\"billingSystem\":\"CES\",\"currentAr\":10,\"ar30Days\":30,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"totalAr\":400,\"totalOverDue\":390,\"lastPaymentDate\":\"2022-08-29T09:12:33.001Z\",\"paymentMethod\":\"Card\",\"odRemaining\":390,\"acctStatus\":\"O\",\"statusDate\":\"2022-08-29T09:12:33.001Z\",\"dispute\":90.88,\"language\":\"EN\",\"marketSubSegment\":\"CBU\",\"province\":\"BC\",\"cbu\":\"BC\",\"cbucidName\":\"Air Canada\",\"rcidName\":\"NORTHLAND PROPERTIES CORPORATION2\",\"subPortfolio\":\"RO-ACCOUNT\",\"entityId\":6766677,\"entityStatus\":\"In Collection\",\"entityType\":\"CBUCID\",\"entityRisk\":\"Low\",\"entityValue\":\"5\",\"entityOwnerId\":\"John123\"}]",AssignedEntitiesInEntityViewResponseArray.class);
        // return new Object(); 
    }
    
//assignedActions - Get Action List by Agent ID and WorkCategory ID
//FAWB Actions List Page. FAWB page where agent can see assigned actions

    @RequestMapping(value = "/assignedActions", method = {RequestMethod.GET})
    public AssignedActionsResponseArray getAssignedActions(@RequestParam(required = true) String agentId , @RequestParam(required = true) String workCategory) throws Exception  {

        return objectMapper.readValue("[{\"assignedAgent\":\"John\",\"createdBy\":\"System\",\"status\":\"Open\",\"dueDate\":\"2022-08-29T09:12:33.001Z\",\"prioirty\":\"High\",\"actionType\":\"SUSP\",\"actionDescription\":\"Payment Arrangement Reminder\",\"entityStatus\":\"in-collection\",\"entityType\":\"BAN\",\"entityName\":\"OBTEC Property Management\",\"entityId\":23432432},{\"assignedAgent\":\"John\",\"createdBy\":\"System\",\"status\":\"Open\",\"dueDate\":\"2022-08-29T09:12:33.001Z\",\"prioirty\":\"High\",\"actionType\":\"SUSP\",\"actionDescription\":\"Payment Arrangement Reminder\",\"entityStatus\":\"in-collection\",\"entityType\":\"RCID\",\"entityName\":\"OBTEC Property Management2\",\"entityId\":23432433}]",AssignedActionsResponseArray.class);
    }

    
//entityContacts
    @RequestMapping(value = "/entityContacts", method = {RequestMethod.GET})
    public EntityContactsResponse getEntityContacts(@RequestParam(required = true) String entityId) throws Exception  {

        return objectMapper.readValue("{\"mailingContacts\":[{\"entityId\":2323232,\"banId\":243,\"banName\":\"Air Canada\",\"billingSystem\":\"CES9\",\"streetNumber\":\"121\",\"streetName\":\"King St\",\"city\":\"Toronto\",\"postalCode\":\"L1L0B1\",\"province\":\"ON\",\"notification\":false},{\"entityId\":2323232,\"banId\":244,\"banName\":\"Air Canada2\",\"billingSystem\":\"CES9\",\"streetNumber\":\"122\",\"streetName\":\"King St\",\"city\":\"Vancouver\",\"postalCode\":\"L1L0B1\",\"province\":\"BC\",\"notification\":false}],\"digitalContacts\":[{\"entityId\":2323232,\"contactId\":1,\"rcId\":223244343,\"banId\":243,\"sourceOfContact\":\"TCM\",\"contactForNotices\":true,\"telusContacts\":true,\"title\":\"Mr\",\"firstName\":\"Ryan\",\"lastName\":\"Smith\",\"email\":\"Smith@yahoo.com\",\"workNumber\":\"9054164166\",\"mobileNumber\":\"4164146423\"},{\"entityId\":2323232,\"contactId\":2,\"rcId\":223244343,\"banId\":244,\"sourceOfContact\":\"TCM\",\"contactForNotices\":true,\"telusContacts\":true,\"title\":\"Mr\",\"firstName\":\"Ryan\",\"lastName\":\"Smith2\",\"email\":\"Smith2@yahoo.com\",\"workNumber\":\"9054164168\",\"mobileNumber\":\"4164146424\"}]}",EntityContactsResponse.class);
    }

    
    
//entityBanDetails
    @RequestMapping(value = "/entityBanDetails", method = {RequestMethod.GET})
    public List<EntityBanDetailsResponse> getEntityBanDetails(String entityId) throws Exception  {

        return objectMapper.readValue("[{\"entityId\":6766677,\"banId\":2244343,\"banStatus\":\"In Collection\",\"banName\":\"Air Canada\",\"banArAmount\":234,\"banOverdueAmount\":234,\"suppresionFlag\":true,\"disputeFlag\":false},{\"entityId\":6766677,\"banId\":2244344,\"banStatus\":\"In Collection\",\"banName\":\"Air Canada2\",\"banArAmount\":22,\"banOverdueAmount\":10,\"suppresionFlag\":true,\"disputeFlag\":false}]",
        objectMapper.getTypeFactory().constructCollectionType(List.class, EntityBanDetailsResponse.class));
        // return new Object(); 
    }
    
//entityDetails
    @RequestMapping(value = "/entityDetails", method = {RequestMethod.GET})
    public List<EntityDetailsResponse> getEntityDetails(String entityId) throws Exception  {

        return objectMapper.readValue("{\"entityDetails\":{\"entityId\":6766677,\"entityType\":\"RCID\",\"rcId\":224343,\"cbucId\":7232323,\"entityName\":\"Air Canada\",\"totalBan\":10,\"totalDelinquentBans\":5,\"risk\":\"Low\",\"entityValue\":\"Low\",\"entityCollectionStatus\":\"Open\",\"manualFlag\":false,\"lastTreatment\":\"SUSP\",\"currentAr\":10,\"ar30Days\":30,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"ar180Days\":1,\"ar180DaysPlus\":2,\"totalAr\":403,\"totalOverDue\":393,\"entityOwnerId\":\"John123\",\"primeWorkCategory\":\"aliqua eu ut\",\"portfolioCategory\":\"SMB\",\"portfolioSubCategory\":\"PUBLIC LARGE\",\"ftnp\":true,\"disputeFlag\":true},\"banDetails\":[{\"banId\":256645999,\"banName\":\"NORTHLAND PROPERTIES CORPORATION\",\"cbucId\":761846,\"rcId\":392931,\"billingSystem\":\"CES\",\"currentAr\":10,\"ar30Days\":30,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"ar180Days\":1,\"ar180DaysPlus\":2,\"totalAr\":403,\"totalOverDue\":393,\"lastPaymentDate\":\"2022-08-29T09:12:33.001Z\",\"paymentMethod\":\"Card\",\"odRemaining\":2344390.88,\"acctStatus\":\"O\",\"acctStatusDate\":\"2022-08-29T09:12:33.001Z\",\"acctType\":\"B\",\"acctSubType\":\"I\",\"dispute\":2344390.88,\"language\":\"EN\",\"marketSubSegment\":\"CBU\",\"province\":\"BC\",\"cbu\":\"BC\",\"cbucidName\":\"Air Canada\",\"rcidName\":\"NORTHLAND PROPERTIES CORPORATION\",\"subPortfolio\":\"RO-ACCOUNT\",\"entityId\":6766677,\"entityStatus\":\"In Collection\",\"entityType\":\"CBUCID\",\"entityRisk\":\"Low\",\"entityValue\":\"5\",\"entityOwnerId\":\"John123\"},{\"banId\":256645900,\"banName\":\"NORTHLAND PROPERTIES CORPORATION2\",\"cbucId\":761846,\"rcId\":392931,\"billingSystem\":\"CES\",\"currentAr\":10,\"ar30Days\":30,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"ar180Days\":1,\"ar180DaysPlus\":2,\"totalAr\":403,\"totalOverDue\":393,\"lastPaymentDate\":\"2022-08-29T09:12:33.001Z\",\"paymentMethod\":\"Card\",\"odRemaining\":390,\"acctStatus\":\"O\",\"acctStatusDate\":\"2022-08-29T09:12:33.001Z\",\"acctType\":\"B\",\"acctSubType\":\"I\",\"dispute\":90.88,\"language\":\"EN\",\"marketSubSegment\":\"CBU\",\"province\":\"BC\",\"cbu\":\"BC\",\"cbucidName\":\"Air Canada\",\"rcidName\":\"NORTHLAND PROPERTIES CORPORATION2\",\"subPortfolio\":\"RO-ACCOUNT\",\"entityId\":6766677,\"entityStatus\":\"In Collection\",\"entityType\":\"CBUCID\",\"entityRisk\":\"Low\",\"entityValue\":\"5\",\"entityOwnerId\":\"John123\"}]}",
        objectMapper.getTypeFactory().constructCollectionType(List.class, EntityDetailsResponse.class));
        // return new Object(); 
    }
    

}