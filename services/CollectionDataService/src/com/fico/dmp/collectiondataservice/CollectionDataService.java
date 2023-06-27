/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.collectiondataservice;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.HttpMethod;

import com.fico.pscomponent.util.PropertiesUtil;
import com.fico.telus.service.TelusAPIConnectivityService;
import com.fico.telus.utility.URIConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;
import com.fico.qb.query.builder.support.utils.spring.StringUtils;


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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.client.model.EntityBanDetailsResponse;
import io.swagger.client.model.EntitySearchResponseArray;
import io.swagger.client.model.TeamsActionViewResponse;
import io.swagger.client.model.AssignedEntitiesInEntityViewResponseArray;
import io.swagger.client.model.AssignedEntitiesInClassicViewResponseArray;
//import io.swagger.client.model.AssignedActionsResponseArray;

import io.swagger.client.model.EntityContactsResponse;
import io.swagger.client.model.EntityDetailsResponse;
import io.swagger.client.model.EntityBanDetailsResponse;

import io.swagger.client.model.CollectionDispute;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import com.fico.core.util.ObjectMapperConfig;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;




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


    @Autowired
    private PropertiesUtil propertiesUtil;

    @Autowired
    private TelusAPIConnectivityService telusAPIConnectivityService;


    private static final String IS_COLLDATAMGMT_STUB_ENABLED = "IS_COLLDATAMGMT_STUB_ENABLED";

    private static final String COLLDATAMGMT_ENDPOINT_URL = "COLLDATAMGMT_ENDPOINT_BASEURL";

    private static final String COLLDATAMGMT_ENDPOINT_SCOPE = "COLLDATAMGMT_ENDPOINT_SCOPE";

    private boolean isStubEnabled;

    private String entityDataEndPointUrl;

    private String entitySvcAuthScope;


    private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(CollectionDataService.class.getName());

    @Autowired
    private SecurityService securityService;
    
    private final ObjectMapper objectMapper = new ObjectMapperConfig().customObjectMapper();
    @PostConstruct
    public void init() {

        this.isStubEnabled = Boolean.valueOf(propertyValueFrom(IS_COLLDATAMGMT_STUB_ENABLED, "false"));
        this.entityDataEndPointUrl = propertyValueFrom(COLLDATAMGMT_ENDPOINT_URL, URIConstant.COLLECTION_ENTITY_DATA_SERVICE_URL);
        this.entitySvcAuthScope = propertyValueFrom(COLLDATAMGMT_ENDPOINT_SCOPE, "3162");

    }

    private String propertyValueFrom(String propertyName, String defaulValueIfNull) {
        String propertyValue = propertiesUtil.getPropertyValue(propertyName);
        if (propertyValue == null) {
            logger.info("property value is null, using default");
            propertyValue = defaulValueIfNull;
        }
        return propertyValue;
    }



    
    ///searchEntity
    @RequestMapping(value = "/entitySearch", method = {RequestMethod.GET})
    public EntitySearchResponseArray getEntitySearch(@RequestParam(required = true)String inputType, @RequestParam(required = true)String inputValue, @RequestParam(required = true)String level, @RequestParam(required = true)String searchMatchCriteria, @RequestParam(required = true)String billingSystem, Integer offset, Integer limit) throws Exception  {

        if (isStubEnabled) {

                return objectMapper.readValue("[{\"banId\": 224434,\"banName\": \"Air Canada Toronto\",\"billingSystem\": \"CES\",\"rcId\": 323223,\"cbucId\": 1323232,\"entityId\": 67666,\"entityName\": \"Air Canada Ontario\",\"entityType\": \"RCID\",\"entityOwner\": \"Agent123\",\"dntlFlag\": true},{\"banId\": 24343,\"banName\": \"Rexdale Pharmacy\",\"billingSystem\": \"CES\",\"rcId\": 224232,\"cbucId\": 3232232,\"entityId\": 676667,\"entityName\": \"Rexdale\",\"entityType\": \"RCID\",\"entityOwner\": \"Agent345\",\"dntlFlag\": false},    {\"banId\": 44343,\"banName\": \"first Shop\",\"billingSystem\": \"CES\",\"rcId\": 222456,\"cbucId\": 3232233,\"entityId\": 676689,\"entityName\": \"first\",\"entityType\": \"CBUCID\",\"entityOwner\": \"Abc xyz\",\"dntlFlag\": true},{\"banId\": 44353,\"banName\": \"Rexdale here\",\"billingSystem\": \"CES\",\"rcId\": 23456,\"cbucId\": 323211,\"entityId\": 57609,\"entityName\": \"Rexdale\",\"entityType\": \"BAN\",\"entityOwner\": \"Agent345\",\"dntlFlag\": true},{\"banId\": 44343,\"banName\": \"first Shop\",\"billingSystem\": \"CESE\",\"rcId\": 222456,\"cbucId\": 3232233,\"entityId\": 676689,\"entityName\": \"first\",\"entityType\": \"CBUCID\",\"entityOwner\": \"Abc xyz\",\"dntlFlag\": true},{\"banId\": 84303,\"banName\": \"Titan 235\",\"billingSystem\": \"CESE\",\"rcId\": 222456,\"cbucId\": 3232233,\"entityId\": 67666,\"entityName\": \"Ent\",\"entityType\": \"RCID\",\"entityOwner\": \"Tom Bill\",\"dntlFlag\": true},{\"banId\": 74345,\"banName\": \"Number character\",\"billingSystem\": \"CES\",\"rcId\": 2224890,\"cbucId\": 3230933,\"entityId\": 6787866,\"entityName\": \"Num\",\"entityType\": \"CBUCID\",\"entityOwner\": \"Venus Bill\",\"dntlFlag\": false},{\"banId\": 56581,\"banName\": \"Sixth entity\",\"billingSystem\": \"CES\",\"rcId\": 222780,\"cbucId\": 3290933,\"entityId\": 670966,\"entityName\": \"Number\",\"entityType\": \"BAN\",\"entityOwner\": \"Harry Jack\",\"dntlFlag\": false},{\"banId\": 73581,\"banName\": \"Raven Bay\",\"billingSystem\": \"CES\",\"rcId\": 222780,\"cbucId\": 3290933,\"entityId\": 670966,\"entityName\": \"Number\",\"entityType\": \"BAN\",\"entityOwner\": \"Ham 345\",\"dntlFlag\": false},{\"banId\": 534781,\"banName\": \"This is ban Name for the banID 55781, CES\",\"billingSystem\": \"CES\",\"rcId\": 0,\"cbucId\": 32586,\"entityId\": 1209066,\"entityName\": \"letter\",\"entityType\": \"RCID\",\"entityOwner\": \"Veronica Shell\",\"dntlFlag\": false},{\"banId\": 55581,\"banName\": \"Shelly890\",\"billingSystem\": \"CES\",\"rcId\": 90900,\"cbucId\": 322343,\"entityId\": 27654,\"entityName\": \"Shell78\",\"entityType\": \"BAN\",\"entityOwner\": \"Owner\",\"dntlFlag\": true},{\"banId\": 557821,\"banName\": \"Raven bay\",\"billingSystem\": \"CES\",\"rcId\": 9056910,\"cbucId\": 32443,\"entityId\": 27854,\"entityName\": \"ent name\",\"entityType\": \"CBUCID\",\"entityOwner\": \"EMt Owner\",\"dntlFlag\": true},{\"banId\": 5781,\"banName\": \"Shril 890\",\"billingSystem\": \"CES\",\"rcId\": 90890,\"cbucId\": 3243,\"entityId\": 2234,\"entityName\": \"Vero Roll\",\"entityType\": \"CBUCID\",\"entityOwner\": \"Shri\",\"dntlFlag\": true},{\"banId\": 576881,\"banName\": \"Raven bay\",\"billingSystem\": \"CES CES\",\"rcId\": 908340,\"cbucId\": 31243,\"entityId\": 22254,\"entityName\": \"ent name\",\"entityType\": \"CBUCID\",\"entityOwner\": \"EMt Owner\",\"dntlFlag\": true},{\"banId\": 98765,\"banName\": \"Raven bay\",\"billingSystem\": \"CES CES\",\"rcId\": 72390,\"cbucId\": 329943,\"entityId\": 27054,\"entityName\": \"ent name\",\"entityType\": \"CBUCID\",\"entityOwner\": \"EMt Owner\",\"dntlFlag\": false},{\"banId\": 8796,\"banName\": \"Testing 123 Lookup\",\"billingSystem\": \"CES\",\"rcId\": 91090,\"cbucId\": 32783,\"entityId\": 290,\"entityName\": \"search66\",\"entityType\": \"RCID\",\"entityOwner\": \"EMt Owner\",\"dntlFlag\": false},{\"banId\": 87096,\"banName\": \"John Kay\",\"billingSystem\": \"CES\",\"rcId\": 91120,\"cbucId\": 56797,\"entityId\": 555,\"entityName\": \"Faly341\",\"entityType\": \"Account Group\",\"entityOwner\": \"EMt Owner\",\"dntlFlag\": true},{\"banId\": 16096,\"banName\": \"Johny Brown kay\",\"billingSystem\": \"CES\",\"rcId\": 89020,\"cbucId\": 56,\"entityId\": 6605,\"entityName\": \"Dalerex\",\"entityType\": \"RCID\",\"entityOwner\": \"Entity#89\",\"dntlFlag\": true},{\"banId\": 906,\"banName\": \"US Entity\",\"billingSystem\": \"CES\",\"rcId\": 8820,\"cbucId\": 906,\"entityId\": 898989,\"entityName\": \"US\",\"entityType\": \"CBUCID\",\"entityOwner\": \"JoyBill45\",\"dntlFlag\": true},{\"banId\": 16096,\"banName\": \"Sheron product\",\"billingSystem\": \"CES\",\"rcId\": 9090020,\"cbucId\": 126,\"entityId\": 1,\"entityName\": \"Shron\",\"entityType\": \"RCID\",\"entityOwner\": \"Entity Owning\",\"dntlFlag\": true},{\"banId\": 16,\"banName\": \"Sheron product\",\"billingSystem\": \"CES\",\"rcId\": 50020,\"cbucId\": 126,\"entityId\": 1,\"entityName\": \"Shron\",\"entityType\": \"Account Group\",\"entityOwner\": \"Katie78\",\"dntlFlag\": false},{\"banId\": 1906,\"banName\": \"Zeb xuv\",\"billingSystem\": \"CES\",\"rcId\": 47834,\"cbucId\": 196,\"entityId\": 100,\"entityName\": \"Zeby\",\"entityType\": \"CBUCID\",\"entityOwner\": \"Shaolin@678\",\"dntlFlag\": false},{\"banId\": 896,\"banName\": \"Quera Desire\",\"billingSystem\": \"CES\",\"rcId\": 378,\"cbucId\": 8996,\"entityId\": 90900,\"entityName\": \"Query\",\"entityType\": \"CBUCID\",\"entityOwner\": \"Eric Shellon\",\"dntlFlag\": false},{\"banId\": 9006,\"banName\": \"Swift BAN name\",\"billingSystem\": \"CES\",\"rcId\": 674,\"cbucId\": 7096,\"entityId\": 45677,\"entityName\": \"Swery Elik\",\"entityType\": \"BAN\",\"entityOwner\": \"Navon Sam713\",\"dntlFlag\": false},{\"banId\": 834906,\"banName\": \"Marian banname\",\"billingSystem\": \"CES\",\"rcId\": 7834,\"cbucId\": 566,\"entityId\": 10956,\"entityName\": \"Marry ghf\",\"entityType\": \"Account Group\",\"entityOwner\": \"Marina Fred\",\"dntlFlag\": true}]", EntitySearchResponseArray.class);

            }else{

            logger.info("::::::::Calling  entity data endpoint call ::::::::");
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(entityDataEndPointUrl+URIConstant.ApiMapping.ENTITY_SEARCH)
                    .queryParam("inputType", inputType)
                    .queryParam("inputValue", URLEncoder.encode(inputValue,"UTF-8"))
                    .queryParam("level",level)
                    .queryParam("searchMatchCriteria",searchMatchCriteria)
                    .queryParam("billingSystem",billingSystem)
                    .queryParam("limit",20);

            String responseStr = telusAPIConnectivityService.executeTelusAPI(null,builder.toUriString(), HttpMethod.GET, entitySvcAuthScope);
            logger.info("::::::::Entity data endpoint call success ::::::::");
            logger.info("Resoinse---"+ responseStr);
           return objectMapper.readValue(responseStr,EntitySearchResponseArray.class);

        }
    }
    
    
    ///assignedEntitiesInEntityView
    @RequestMapping(value = "/assignedEntitiesInEntityView", method = {RequestMethod.GET})
    public AssignedEntitiesInEntityViewResponseArray getAssignedEntitiesInEntityView(@RequestParam(required = true) String entityOwner, @RequestParam(required = true) String workCategory,@RequestParam(required = true) String portfolio,@RequestParam(required = true) String billingSystem,@RequestParam(required = true) String collectionStatus, Integer offset, Integer limit) throws Exception  {

        return objectMapper.readValue("[{\"entityId\":6766677,\"entityType\":\"RCID\",\"rcId\":\"224343\",\"cbucId\":\"7232323\",\"entityName\":\"Air Canada\",\"totalBan\":10,\"totalDelinquentBans\":5,\"risk\":\"Low\",\"entityValue\":\"Low\",\"entityCollectionStatus\":\"Open\",\"manualFlag\":false,\"lastTreatment\":\"SUSP\",\"currentAr\":10,\"ar30Days\":30,\"ar60Days\":60,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"ar180Days\":1,\"ar180DaysPlus\":2,\"totalAr\":403,\"totalOverDue\":393,\"entityOwnerId\":\"John123\",\"primeWorkCategory\":\"aliqua eu ut\",\"portfolioCategory\":\"SMB\",\"portfolioSubCategory\":\"PUBLIC LARGE\",\"ftnp\":true,\"disputeFlag\":true,\"odRemaining\":2344390.88,\"openActionDate\":\"2022-08-19\"},{\"entityId\":6766678,\"entityType\":\"CBUCID\",\"rcId\":\"224344\",\"cbucId\":\"723223\",\"entityName\":\"Air Canada2\",\"totalBan\":10,\"totalDelinquentBans\":5,\"risk\":\"Low\",\"entityValue\":\"Low\",\"entityCollectionStatus\":\"Open\",\"manualFlag\":false,\"lastTreatment\":\"SUSP\",\"currentAr\":10,\"ar30Days\":30,\"ar60Days\":60,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"ar180Days\":1,\"ar180DaysPlus\":2,\"totalAr\":403,\"totalOverDue\":393,\"entityOwnerId\":\"John123\",\"primeWorkCategory\":\"reprehenderit commodo\",\"portfolioCategory\":\"SMB\",\"portfolioSubCategory\":\"PUBLIC LARGE\",\"ftnp\":true,\"disputeFlag\":true,\"odRemaining\":2344390.88,\"openActionDate\":\"2022-08-19\"}]",AssignedEntitiesInEntityViewResponseArray.class);
        // return new Object(); 
    }
    
        ///assignedEntitiesInEntityView
    @RequestMapping(value = "/assignedEntitiesInClassicView", method = {RequestMethod.GET})
    public AssignedEntitiesInEntityViewResponseArray getassignedEntitiesInClassicView(@RequestParam(required = true) String entityOwner, @RequestParam(required = true) String workCategory,@RequestParam(required = true) String portfolio,@RequestParam(required = true) String billingSystem,@RequestParam(required = true) String collectionStatus, Integer offset, Integer limit) throws Exception  {

        return objectMapper.readValue("[{\"banId\":\"256645999\",\"banName\":\"NORTHLAND PROPERTIES CORPORATION\",\"cbucId\":\"761846\",\"rcId\":\"392931\",\"billingSystem\":\"CES\",\"currentAr\":10,\"ar30Days\":30,\"ar60Days\":60,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"ar180Days\":1,\"ar180DaysPlus\":2,\"totalAr\":403,\"totalOverDue\":393,\"lastPaymentDate\":\"2022-08-19\",\"paymentMethod\":\"Card\",\"odRemaining\":2344390.88,\"acctStatus\":\"O\",\"acctStatusDate\":\"2022-08-29\",\"acctType\":\"B\",\"acctSubType\":\"I\",\"dispute\":2344390.88,\"language\":\"EN\",\"marketSubSegment\":\"CBU\",\"province\":\"BC\",\"cbu\":\"BC\",\"cbucidName\":\"Air Canada\",\"rcidName\":\"NORTHLAND PROPERTIES CORPORATION\",\"subPortfolio\":\"RO-ACCOUNT\",\"entityId\":6766677,\"entityStatus\":\"In Collection\",\"entityType\":\"CBUCID\",\"entityRisk\":\"Low\",\"entityValue\":\"5\",\"entityOwnerId\":\"John123\",\"banCollectionStatus\":\"In-Collection\",\"closingDate\":\"2022-12-12\",\"closingCycle\":6,\"suppresionFlag\":false},{\"banId\":\"256645900\",\"banName\":\"NORTHLAND PROPERTIES CORPORATION2\",\"cbucId\":\"761846\",\"rcId\":\"392931\",\"billingSystem\":\"CES\",\"currentAr\":10,\"ar30Days\":30,\"ar60Days\":60,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"ar180Days\":1,\"ar180DaysPlus\":2,\"totalAr\":403,\"totalOverDue\":393,\"lastPaymentDate\":\"2022-08-29\",\"paymentMethod\":\"Card\",\"odRemaining\":390,\"acctStatus\":\"O\",\"acctStatusDate\":\"2022-08-29\",\"acctType\":\"B\",\"acctSubType\":\"I\",\"dispute\":90.88,\"language\":\"EN\",\"marketSubSegment\":\"CBU\",\"province\":\"BC\",\"cbu\":\"BC\",\"cbucidName\":\"Air Canada\",\"rcidName\":\"NORTHLAND PROPERTIES CORPORATION2\",\"subPortfolio\":\"RO-ACCOUNT\",\"entityId\":6766677,\"entityStatus\":\"In Collection\",\"entityType\":\"CBUCID\",\"entityRisk\":\"Low\",\"entityValue\":\"5\",\"entityOwnerId\":\"John123\",\"banCollectionStatus\":\"In-Collection\",\"closingDate\":\"2022-12-12\",\"closingCycle\":6,\"suppresionFlag\":false}]",AssignedEntitiesInEntityViewResponseArray.class);
        // return new Object(); 
    }
    
//assignedActions - Get Action List by Agent ID and WorkCategory ID
//FAWB Actions List Page. FAWB page where agent can see assigned actions

 /*   @RequestMapping(value = "/assignedActions", method = {RequestMethod.GET})
    public AssignedActionsResponseArray getAssignedActions(@RequestParam(required = true) String agentId , @RequestParam(required = true) String workCategory) throws Exception  {

        return objectMapper.readValue("[{\"assignedAgent\":\"parry.sound\",\"createdBy\":\"john.doe\",\"status\":\"PENDING\",\"dueDate\":\"2023-03-02\",\"prioirty\":\"TOP\",\"actionType\":\"SUS\",\"actionDescription\":\"step 300102\",\"entityType\":\"RCID\",\"entityName\":\"entity1\",\"entityId\":1},{\"assignedAgent\":\"parry.sound\",\"createdBy\":\"john.doe\",\"status\":\"PENDING\",\"dueDate\":\"2023-03-03\",\"prioirty\":\"TOP\",\"actionType\":\"SUS\",\"actionDescription\":\"step 300103\",\"entityType\":\"RCID\",\"entityName\":\"entity1\",\"entityId\":1},{\"assignedAgent\":\"parry.sound\",\"createdBy\":\"john.doe\",\"status\":\"PENDING\",\"dueDate\":\"2023-03-04\",\"prioirty\":\"TOP\",\"actionType\":\"SUS\",\"actionDescription\":\"step 300104\",\"entityType\":\"RCID\",\"entityName\":\"entity1\",\"entityId\":1},{\"assignedAgent\":\"parry.sound\",\"createdBy\":\"john.doe\",\"status\":\"PENDING\",\"dueDate\":\"2023-04-02\",\"prioirty\":\"TOP\",\"actionType\":\"SUS\",\"actionDescription\":\"step 400102\",\"entityType\":\"CBUCID\",\"entityName\":\"entity2\",\"entityId\":2},{\"assignedAgent\":\"parry.sound\",\"createdBy\":\"john.doe\",\"status\":\"PENDING\",\"dueDate\":\"2023-04-03\",\"prioirty\":\"TOP\",\"actionType\":\"SUS\",\"actionDescription\":\"step 400103\",\"entityType\":\"CBUCID\",\"entityName\":\"entity2\",\"entityId\":2},{\"assignedAgent\":\"parry.sound\",\"createdBy\":\"john.doe\",\"status\":\"PENDING\",\"dueDate\":\"2023-04-04\",\"prioirty\":\"TOP\",\"actionType\":\"SUS\",\"actionDescription\":\"step 400104\",\"entityType\":\"CBUCID\",\"entityName\":\"entity2\",\"entityId\":2},{\"createdBy\":\"john.doe\",\"status\":\"PENDING\",\"dueDate\":\"2023-04-05\",\"prioirty\":\"TOP\",\"actionType\":\"SUS\",\"actionDescription\":\"step 400105\",\"entityType\":\"CBUCID\",\"entityName\":\"entity2\",\"entityId\":2}]",AssignedActionsResponseArray.class);
    } */

    
//entityContacts
    @RequestMapping(value = "/entityContacts", method = {RequestMethod.GET})
    public EntityContactsResponse getEntityContacts(@RequestParam(required = true) Integer entityId) throws Exception  {
    	Boolean entityContactStub = true;
    	 if (entityContactStub) {
        return objectMapper.readValue("{\"mailingContacts\":[{\"entityId\":1,\"banId\":\"1\",\"banName\":\"BACCT1\",\"billingSystem\":\"CES9\",\"careOf\":\"CareOf\",\"unitNumber\":\"Unit Number\",\"streetNumber\":\"111\",\"streetNumberSuffix\":\"streetSuffix\",\"streetName\":\"Britannia Ave\",\"streetType\":\"streetType\",\"streetDirection\":\"stDirection\",\"city\":\"Oshawa\",\"postalCode\":\"L1L 0B4\",\"province\":\"ON\"},{\"entityId\":1,\"banId\":\"1\",\"banName\":\"BACCT1\",\"billingSystem\":\"CES9\",\"careOf\":\"CareOf\",\"streetDirection\":\"stDirection\",\"poBox\":\"P O Box 123\",\"postalCode\":\"L1L 0B4\",\"province\":\"ON\"}],\"digitalContacts\":[{\"contactId\":1,\"entityId\":1,\"rcId\":\"rc-name-12345\",\"sourceOfContact\":\"TCM\",\"contactForNotices\":false,\"telusContacts\":false,\"title\":\"Mr.\",\"firstName\":\"John1\",\"lastName\":\"Doe1\",\"email\":\"john1.doe1@telus.com\",\"workNumber\":\"9059974001\",\"mobileNumber\":\"5149979001\",\"faxNumber\":\"9059974008\",\"workPhoneExt\":\"511\"},{\"contactId\":2,\"entityId\":1,\"rcId\":\"rc-name-12345\",\"sourceOfContact\":\"TCM\",\"contactForNotices\":false,\"telusContacts\":false,\"title\":\"Mr.\",\"firstName\":\"John2\",\"lastName\":\"Doe2\",\"email\":\"john2.doe2@telus.com\",\"workNumber\":\"9059974002\",\"mobileNumber\":\"5149979002\",\"faxNumber\":\"9059974008\",\"workPhoneExt\":\"311\"},{\"contactId\":3,\"entityId\":1,\"rcId\":\"rc-name-12345\",\"sourceOfContact\":\"FAWB\",\"contactForNotices\":true,\"telusContacts\":true,\"title\":\"Mr.\",\"firstName\":\"Kevin\",\"lastName\":\"Smith\",\"email\":\"Kevin.Smith@telus.com\",\"workNumber\":\"9059974002\",\"mobileNumber\":\"5149979002\",\"faxNumber\":\"9059974008\",\"workPhoneExt\":\"211\"}]}",EntityContactsResponse.class);
    	 }
    	 else{

             logger.info("::::::::Calling  entityContacts endpoint call ::::::::");
             UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(entityDataEndPointUrl+URIConstant.ApiMapping.ENTITY_CONTACTS)
                     .queryParam("entityId", entityId);

             String responseStr = telusAPIConnectivityService.executeTelusAPI(null,builder.toUriString(), HttpMethod.GET, entitySvcAuthScope);
             logger.info("::::::::Entity data endpoint call success ::::::::");
             logger.info("Resoinse---"+ responseStr);
            return objectMapper.readValue(responseStr,EntityContactsResponse.class);

         }
    	 
    	 }

    
    
//entityBanDetails
    @RequestMapping(value = "/entityBanDetails", method = {RequestMethod.GET})
    public List<EntityBanDetailsResponse> getEntityBanDetails(Integer entityId, Integer offset, Integer limit) throws Exception  {
    	if (isStubEnabled) {
        return objectMapper.readValue("[{\"entityId\":1,\"banId\":\"1\",\"banMapRefId\":1,\"banStatus\":\"C\",\"banName\":\"BACCT1\",\"banArAmount\":-132.22,\"banOverdueAmount\":-133.77,\"lineOfBusiness\":\"WLN\",\"closingDate\":\"2022-12-12\",\"closingCycle\":6,\"suppresionFlag\":false,\"disputeFlag\":true},{\"entityId\":2,\"banId\":\"2\",\"banMapRefId\":2,\"banStatus\":\"D\",\"banName\":\"BACCT2\",\"banArAmount\":150.22,\"banOverdueAmount\":160.77,\"lineOfBusiness\":\"WLN\",\"closingDate\":\"2022-12-12\",\"closingCycle\":6,\"suppresionFlag\":false,\"disputeFlag\":true}]",
        objectMapper.getTypeFactory().constructCollectionType(List.class, EntityBanDetailsResponse.class));
    	}else {
    		 logger.info("::::::::Calling  entityBanDetails endpoint call ::::::::");
             UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(entityDataEndPointUrl+URIConstant.ApiMapping.ENTITY_BAN_DETAILS)
                     .queryParam("entityId", entityId);
             String responseStr = telusAPIConnectivityService.executeTelusAPI(null,builder.toUriString(), HttpMethod.GET, entitySvcAuthScope);
             logger.info("::::::::Entity Ban details endpoint call success ::::::::");
             logger.info("Response payload---"+ responseStr);
             List<EntityBanDetailsResponse> entityBanDetailsResponseList = objectMapper.readValue(responseStr,
     				objectMapper.getTypeFactory().constructCollectionType(List.class, EntityBanDetailsResponse.class));
             return entityBanDetailsResponseList;
    	}
    }
    
//entityDetails
    @RequestMapping(value = "/entityDetails", method = {RequestMethod.GET})
    public List<EntityDetailsResponse> getEntityDetails(String entityId) throws Exception  {

 //if (isStubEnabled) {
        return objectMapper.readValue("[{\"entityDetails\":{\"entityId\":6766677,\"entityType\":\"RCID\",\"rcId\":\"224343\",\"cbucId\":\"7232323\",\"entityName\":\"Air Canada\",\"totalBan\":10,\"totalDelinquentBans\":5,\"risk\":\"Low\",\"entityValue\":\"Low\",\"entityCollectionStatus\":\"Open\",\"manualFlag\":false,\"lastTreatment\":\"SUSP\",\"currentAr\":10,\"ar30Days\":30,\"ar60Days\":60,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"ar180Days\":1,\"ar180DaysPlus\":2,\"totalAr\":403,\"totalOverDue\":393,\"entityOwnerId\":\"John123\",\"primeWorkCategory\":\"aliqua eu ut\",\"portfolioCategory\":\"SMB\",\"portfolioSubCategory\":\"PUBLIC LARGE\",\"ftnp\":true,\"disputeFlag\":true},\"banDetails\":[{\"banId\":256645999,\"banName\":\"NORTHLAND PROPERTIES CORPORATION\",\"cbucId\":\"761846\",\"rcId\":\"392931\",\"billingSystem\":\"CES\",\"currentAr\":10,\"ar30Days\":30,\"ar60Days\":60,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"ar180Days\":1,\"ar180DaysPlus\":2,\"totalAr\":403,\"totalOverDue\":393,\"lastPaymentDate\":\"2022-08-29\",\"paymentMethod\":\"Card\",\"odRemaining\":2344390.88,\"acctStatus\":\"O\",\"acctStatusDate\":\"2022-08-29\",\"acctType\":\"B\",\"acctSubType\":\"I\",\"dispute\":2344390.88,\"language\":\"EN\",\"marketSubSegment\":\"CBU\",\"province\":\"BC\",\"cbu\":\"BC\",\"cbucidName\":\"Air Canada\",\"rcidName\":\"NORTHLAND PROPERTIES CORPORATION\",\"subPortfolio\":\"RO-ACCOUNT\",\"entityId\":6766677,\"entityStatus\":\"In Collection\",\"entityType\":\"CBUCID\",\"entityRisk\":\"Low\",\"entityValue\":\"5\",\"entityOwnerId\":\"John123\",\"banCollectionStatus\":\"In-Collection\",\"closingDate\":\"2022-12-12\",\"closingCycle\":6,\"suppresionFlag\":false},{\"banId\":\"256645900\",\"banName\":\"NORTHLAND PROPERTIES CORPORATION2\",\"cbucId\":\"761846\",\"rcId\":\"392931\",\"billingSystem\":\"CES\",\"currentAr\":10,\"ar30Days\":30,\"ar60Days\":60,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"ar180Days\":1,\"ar180DaysPlus\":2,\"totalAr\":403,\"totalOverDue\":393,\"lastPaymentDate\":\"2022-08-29\",\"paymentMethod\":\"Card\",\"odRemaining\":390,\"acctStatus\":\"O\",\"acctStatusDate\":\"2022-08-29\",\"acctType\":\"B\",\"acctSubType\":\"I\",\"dispute\":90.88,\"language\":\"EN\",\"marketSubSegment\":\"CBU\",\"province\":\"BC\",\"cbu\":\"BC\",\"cbucidName\":\"Air Canada\",\"rcidName\":\"NORTHLAND PROPERTIES CORPORATION2\",\"subPortfolio\":\"RO-ACCOUNT\",\"entityId\":6766677,\"entityStatus\":\"In Collection\",\"entityType\":\"CBUCID\",\"entityRisk\":\"Low\",\"entityValue\":\"5\",\"entityOwnerId\":\"John123\",\"banCollectionStatus\":\"In-Collection\",\"closingDate\":\"2022-12-12\",\"closingCycle\":6,\"suppresionFlag\":false}]}]",
        objectMapper.getTypeFactory().constructCollectionType(List.class, EntityDetailsResponse.class));
        // return new Object(); 
    
    // } else {
    //         logger.info("::::::::Calling  entity details endpoint call ::::::::");
    //         UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(entityDataEndPointUrl + URIConstant.ApiMapping.ENTITY_DETAILS)
    //                 .queryParam("entityId", entityId);

    //         String responseStr = telusAPIConnectivityService.executeTelusAPI(null, builder.toUriString(), HttpMethod.GET, entitySvcAuthScope);
    //         logger.info("::::::::Entity details endpoint call success ::::::::");
    //         logger.info("Resoinse---" + responseStr);
            
    //         if(!StringUtils.isEmpty(responseStr)) {
    //             return objectMapper.readValue(responseStr, new TypeReference<List<EntityDetailsResponse>>() {
    //             });
    //         }else{
    //           return   new ArrayList<EntityDetailsResponse>();
    //         }


    //     }


    }
    
    @RequestMapping(value = "/actionViewByTeam", method = {RequestMethod.GET})
    public List<TeamsActionViewResponse> getActionViewByTeam(String assignedAgent, String assignedTeam,String entityOwner, Date fromDueDate, Date toDueDate, String actionType, String status, String workCategory, String viewType, Integer offset, Integer limit) throws Exception {
    	 return objectMapper.readValue("[{\"actionId\":6766677,\"entityId\":6766677,\"entityName\":\"Air Canada\",\"entityType\":\"RCID\",\"collectionStatus\":\"In-collection\",\"actionType\":\"Suspend\",\"actionDescription\":\"Suspension\",\"priority\":\"High\",\"dueDate\":\"2022-08-29\",\"status\":\"Open\",\"assignedTeam\":\"TIG\",\"assignedAgent\":\"John 123\",\"workCategory\":\"workCategory1\",\"totalAr\":400,\"totalOverDue\":390},{\"actionId\":6766678,\"entityId\":6766677,\"entityName\":\"Air Canada\",\"entityType\":\"RCID\",\"collectionStatus\":\"In-collection\",\"actionType\":\"Suspend\",\"actionDescription\":\"Suspension\",\"priority\":\"High\",\"dueDate\":\"2022-08-29\",\"status\":\"Open\",\"assignedTeam\":\"TIG\",\"assignedAgent\":\"John 123\",\"workCategory\":\"workCategory2\",\"totalAr\":500,\"totalOverDue\":490},{\"actionId\":6766679,\"entityId\":6766677,\"entityName\":\"Air Canada\",\"entityType\":\"RCID\",\"collectionStatus\":\"In-collection\",\"actionType\":\"Suspend\",\"actionDescription\":\"Suspension\",\"priority\":\"High\",\"dueDate\":\"2022-08-29\",\"status\":\"Open\",\"assignedTeam\":\"TIG\",\"assignedAgent\":\"John 123\",\"workCategory\":\"workCategory3\",\"totalAr\":600,\"totalOverDue\":690}]",
    		        objectMapper.getTypeFactory().constructCollectionType(List.class, TeamsActionViewResponse.class));
    }

}