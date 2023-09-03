/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.collectiondataservice;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.HttpMethod;

import com.fico.pscomponent.util.PropertiesUtil;
import com.fico.telus.model.AssignedEntitiesInClassicModel;
import com.fico.telus.model.AssignedEntitiesInEntityModel;
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
import io.swagger.client.model.AssignedEntitiesInClassicViewResponse;
import io.swagger.client.model.AssignedEntitiesInClassicViewResponseArray;
//import io.swagger.client.model.AssignedActionsResponseArray;
import io.swagger.client.model.AssignedEntitiesInEntityViewResponse;
import io.swagger.client.model.EntityContactsResponse;
import io.swagger.client.model.EntityDetailsResponse;
import io.swagger.client.model.EntityBanDetailsResponse;

import io.swagger.client.model.CollectionDispute;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Date;

import com.fico.core.util.ObjectMapperConfig;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.net.URLDecoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fico.dmp.commonutilityservice.CommonUtilityService;
import com.fico.telus.model.LookUpResponseWithTeamName;
import io.swagger.client.model.*;







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
	private CommonUtilityService commonUtilityService;


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
    public List<LookUpResponseWithTeamName> getEntitySearch(@RequestParam(required = true)String inputType, @RequestParam(required = true)String inputValue, @RequestParam(required = true)String level, @RequestParam(required = true)String searchMatchCriteria, @RequestParam(required = true)String billingSystem, Integer offset, Integer limit) throws Exception  {

        if (isStubEnabled) {
            			EntitySearchResponseArray entitySearchResponseArray=new EntitySearchResponseArray();


                entitySearchResponseArray= objectMapper.readValue("[{\"banId\": 224434,\"banName\": \"Air Canada Toronto\",\"billingSystem\": \"CES\",\"rcId\": 323223,\"cbucId\": 1323232,\"entityId\": 67666,\"entityName\": \"Air Canada Ontario\",\"entityType\": \"RCID\",\"entityOwner\": \"Agent123\",\"dntlFlag\": true},{\"banId\": 24343,\"banName\": \"Rexdale Pharmacy\",\"billingSystem\": \"CES\",\"rcId\": 224232,\"cbucId\": 3232232,\"entityId\": 676667,\"entityName\": \"Rexdale\",\"entityType\": \"RCID\",\"entityOwner\": \"Agent345\",\"dntlFlag\": false},    {\"banId\": 44343,\"banName\": \"first Shop\",\"billingSystem\": \"CES\",\"rcId\": 222456,\"cbucId\": 3232233,\"entityId\": 676689,\"entityName\": \"first\",\"entityType\": \"CBUCID\",\"entityOwner\": \"Abc xyz\",\"dntlFlag\": true},{\"banId\": 44353,\"banName\": \"Rexdale here\",\"billingSystem\": \"CES\",\"rcId\": 23456,\"cbucId\": 323211,\"entityId\": 57609,\"entityName\": \"Rexdale\",\"entityType\": \"BAN\",\"entityOwner\": \"Agent345\",\"dntlFlag\": true},{\"banId\": 44343,\"banName\": \"first Shop\",\"billingSystem\": \"CESE\",\"rcId\": 222456,\"cbucId\": 3232233,\"entityId\": 676689,\"entityName\": \"first\",\"entityType\": \"CBUCID\",\"entityOwner\": \"Abc xyz\",\"dntlFlag\": true},{\"banId\": 84303,\"banName\": \"Titan 235\",\"billingSystem\": \"CESE\",\"rcId\": 222456,\"cbucId\": 3232233,\"entityId\": 67666,\"entityName\": \"Ent\",\"entityType\": \"RCID\",\"entityOwner\": \"Tom Bill\",\"dntlFlag\": true},{\"banId\": 74345,\"banName\": \"Number character\",\"billingSystem\": \"CES\",\"rcId\": 2224890,\"cbucId\": 3230933,\"entityId\": 6787866,\"entityName\": \"Num\",\"entityType\": \"CBUCID\",\"entityOwner\": \"Venus Bill\",\"dntlFlag\": false},{\"banId\": 56581,\"banName\": \"Sixth entity\",\"billingSystem\": \"CES\",\"rcId\": 222780,\"cbucId\": 3290933,\"entityId\": 670966,\"entityName\": \"Number\",\"entityType\": \"BAN\",\"entityOwner\": \"Harry Jack\",\"dntlFlag\": false},{\"banId\": 73581,\"banName\": \"Raven Bay\",\"billingSystem\": \"CES\",\"rcId\": 222780,\"cbucId\": 3290933,\"entityId\": 670966,\"entityName\": \"Number\",\"entityType\": \"BAN\",\"entityOwner\": \"Ham 345\",\"dntlFlag\": false},{\"banId\": 534781,\"banName\": \"This is ban Name for the banID 55781, CES\",\"billingSystem\": \"CES\",\"rcId\": 0,\"cbucId\": 32586,\"entityId\": 1209066,\"entityName\": \"letter\",\"entityType\": \"RCID\",\"entityOwner\": \"Veronica Shell\",\"dntlFlag\": false},{\"banId\": 55581,\"banName\": \"Shelly890\",\"billingSystem\": \"CES\",\"rcId\": 90900,\"cbucId\": 322343,\"entityId\": 27654,\"entityName\": \"Shell78\",\"entityType\": \"BAN\",\"entityOwner\": \"Owner\",\"dntlFlag\": true},{\"banId\": 557821,\"banName\": \"Raven bay\",\"billingSystem\": \"CES\",\"rcId\": 9056910,\"cbucId\": 32443,\"entityId\": 27854,\"entityName\": \"ent name\",\"entityType\": \"CBUCID\",\"entityOwner\": \"EMt Owner\",\"dntlFlag\": true},{\"banId\": 5781,\"banName\": \"Shril 890\",\"billingSystem\": \"CES\",\"rcId\": 90890,\"cbucId\": 3243,\"entityId\": 2234,\"entityName\": \"Vero Roll\",\"entityType\": \"CBUCID\",\"entityOwner\": \"Shri\",\"dntlFlag\": true},{\"banId\": 576881,\"banName\": \"Raven bay\",\"billingSystem\": \"CES CES\",\"rcId\": 908340,\"cbucId\": 31243,\"entityId\": 22254,\"entityName\": \"ent name\",\"entityType\": \"CBUCID\",\"entityOwner\": \"EMt Owner\",\"dntlFlag\": true},{\"banId\": 98765,\"banName\": \"Raven bay\",\"billingSystem\": \"CES CES\",\"rcId\": 72390,\"cbucId\": 329943,\"entityId\": 27054,\"entityName\": \"ent name\",\"entityType\": \"CBUCID\",\"entityOwner\": \"EMt Owner\",\"dntlFlag\": false},{\"banId\": 8796,\"banName\": \"Testing 123 Lookup\",\"billingSystem\": \"CES\",\"rcId\": 91090,\"cbucId\": 32783,\"entityId\": 290,\"entityName\": \"search66\",\"entityType\": \"RCID\",\"entityOwner\": \"EMt Owner\",\"dntlFlag\": false},{\"banId\": 87096,\"banName\": \"John Kay\",\"billingSystem\": \"CES\",\"rcId\": 91120,\"cbucId\": 56797,\"entityId\": 555,\"entityName\": \"Faly341\",\"entityType\": \"Account Group\",\"entityOwner\": \"EMt Owner\",\"dntlFlag\": true},{\"banId\": 16096,\"banName\": \"Johny Brown kay\",\"billingSystem\": \"CES\",\"rcId\": 89020,\"cbucId\": 56,\"entityId\": 6605,\"entityName\": \"Dalerex\",\"entityType\": \"RCID\",\"entityOwner\": \"Entity#89\",\"dntlFlag\": true},{\"banId\": 906,\"banName\": \"US Entity\",\"billingSystem\": \"CES\",\"rcId\": 8820,\"cbucId\": 906,\"entityId\": 898989,\"entityName\": \"US\",\"entityType\": \"CBUCID\",\"entityOwner\": \"JoyBill45\",\"dntlFlag\": true},{\"banId\": 16096,\"banName\": \"Sheron product\",\"billingSystem\": \"CES\",\"rcId\": 9090020,\"cbucId\": 126,\"entityId\": 1,\"entityName\": \"Shron\",\"entityType\": \"RCID\",\"entityOwner\": \"Entity Owning\",\"dntlFlag\": true},{\"banId\": 16,\"banName\": \"Sheron product\",\"billingSystem\": \"CES\",\"rcId\": 50020,\"cbucId\": 126,\"entityId\": 1,\"entityName\": \"Shron\",\"entityType\": \"Account Group\",\"entityOwner\": \"Katie78\",\"dntlFlag\": false},{\"banId\": 1906,\"banName\": \"Zeb xuv\",\"billingSystem\": \"CES\",\"rcId\": 47834,\"cbucId\": 196,\"entityId\": 100,\"entityName\": \"Zeby\",\"entityType\": \"CBUCID\",\"entityOwner\": \"Shaolin@678\",\"dntlFlag\": false},{\"banId\": 896,\"banName\": \"Quera Desire\",\"billingSystem\": \"CES\",\"rcId\": 378,\"cbucId\": 8996,\"entityId\": 90900,\"entityName\": \"Query\",\"entityType\": \"CBUCID\",\"entityOwner\": \"Eric Shellon\",\"dntlFlag\": false},{\"banId\": 9006,\"banName\": \"Swift BAN name\",\"billingSystem\": \"CES\",\"rcId\": 674,\"cbucId\": 7096,\"entityId\": 45677,\"entityName\": \"Swery Elik\",\"entityType\": \"BAN\",\"entityOwner\": \"Navon Sam713\",\"dntlFlag\": false},{\"banId\": 834906,\"banName\": \"Marian banname\",\"billingSystem\": \"CES\",\"rcId\": 7834,\"cbucId\": 566,\"entityId\": 10956,\"entityName\": \"Marry ghf\",\"entityType\": \"Account Group\",\"entityOwner\": \"Marina Fred\",\"dntlFlag\": true}]", EntitySearchResponseArray.class);
                
                return  setTeamNameBasedOnEmplId(entitySearchResponseArray);

            }else{

            logger.info("::::::::Calling  entity data endpoint call ::::::::");
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(entityDataEndPointUrl+URIConstant.ApiMapping.ENTITY_SEARCH)
                    .queryParam("inputType", inputType)
                    .queryParam("inputValue", URLEncoder.encode(inputValue,"UTF-8"))
                    .queryParam("level",level)
                    .queryParam("searchMatchCriteria",searchMatchCriteria)
                    .queryParam("billingSystem",billingSystem);
                    

            String responseStr = telusAPIConnectivityService.executeTelusAPI(null,builder.toUriString(), HttpMethod.GET, entitySvcAuthScope);
            logger.info("::::::::Entity data endpoint call success ::::::::");
            logger.info("Resoinse---"+ responseStr);
       //    return objectMapper.readValue(responseStr,EntitySearchResponseArray.class);
           		 return  setTeamNameBasedOnEmplId(objectMapper.readValue(responseStr,EntitySearchResponseArray.class));


        }
    }
    
    
    ///assignedEntitiesInEntityView
    @RequestMapping(value = "/assignedEntitiesInEntityView", method = {RequestMethod.GET})
    public List<AssignedEntitiesInEntityModel> getAssignedEntitiesInEntityView(@RequestParam(required = true) String entityOwner, @RequestParam(required = true) String workCategory,@RequestParam(required = true) String portfolio,@RequestParam(required = true) String billingSystem,@RequestParam(required = true) String collectionStatus, Integer offset, Integer limit) throws Exception  {

    	 if (isStubEnabled) {
        return objectMapper.readValue("[{\"entityId\":6766677,\"entityType\":\"RCID\",\"rcId\":\"224343\",\"cbucId\":\"7232323\",\"entityName\":\"Air Canada\",\"totalBan\":10,\"totalDelinquentBans\":5,\"risk\":\"Low\",\"entityValue\":\"Low\",\"entityCollectionStatus\":\"Open\",\"manualFlag\":false,\"lastTreatment\":\"SUSP\",\"currentAr\":10,\"ar30Days\":30,\"ar60Days\":60,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"ar180Days\":1,\"ar180DaysPlus\":2,\"totalAr\":403,\"totalOverDue\":393,\"entityOwnerId\":\"John123\",\"primeWorkCategory\":\"aliqua eu ut\",\"portfolioCategory\":\"SMB\",\"portfolioSubCategory\":\"PUBLIC LARGE\",\"ftnp\":true,\"disputeFlag\":true,\"odRemaining\":2344390.88,\"openActionDate\":\"2022-08-19\"},{\"entityId\":6766678,\"entityType\":\"CBUCID\",\"rcId\":\"224344\",\"cbucId\":\"723223\",\"entityName\":\"Air Canada2\",\"totalBan\":10,\"totalDelinquentBans\":5,\"risk\":\"Low\",\"entityValue\":\"Low\",\"entityCollectionStatus\":\"Open\",\"manualFlag\":false,\"lastTreatment\":\"SUSP\",\"currentAr\":10,\"ar30Days\":30,\"ar60Days\":60,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"ar180Days\":1,\"ar180DaysPlus\":2,\"totalAr\":403,\"totalOverDue\":393,\"entityOwnerId\":\"John123\",\"primeWorkCategory\":\"reprehenderit commodo\",\"portfolioCategory\":\"SMB\",\"portfolioSubCategory\":\"PUBLIC LARGE\",\"ftnp\":true,\"disputeFlag\":true,\"odRemaining\":2344390.88,\"openActionDate\":\"2022-08-19\"}]",
        		objectMapper.getTypeFactory().constructCollectionType(List.class, AssignedEntitiesInEntityViewResponse.class));
    	 }else {
    		 logger.info("::::::::Calling  entity data endpoint call ::::::::");
    		 
    		
    	
             UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(entityDataEndPointUrl+URIConstant.ApiMapping.ASSIGNED_ENTITIES_IN_ENTITY_VIEW)
                     .queryParamIfPresent("entityOwner", Optional.ofNullable(entityOwner))
                     .queryParamIfPresent("workCategory", Optional.ofNullable(workCategory))
                     .queryParamIfPresent("portfolio", Optional.ofNullable(portfolio))
                     .queryParamIfPresent("billingSystem", Optional.ofNullable(billingSystem))
                     .queryParamIfPresent("collectionStatus", Optional.ofNullable(collectionStatus))
                     .queryParamIfPresent("limit", Optional.ofNullable(limit));
             
             String endPointString = builder.toUriString().replace("%7C", "|").replace("%20", " ");
            
             String responseStr = telusAPIConnectivityService.executeTelusAPI(null,endPointString, HttpMethod.GET, entitySvcAuthScope);
             logger.info("::::::::Entity data endpoint call success ::::::::");
             logger.info("Response---"+ responseStr);
             
             List<AssignedEntitiesInEntityViewResponse> AssignedEntitiesInEntityViewResponseList = objectMapper.readValue(responseStr,objectMapper.getTypeFactory().constructCollectionType(List.class, AssignedEntitiesInEntityViewResponse.class));
            
             List<AssignedEntitiesInEntityModel> assignedEntitiesInEntityModelList = new ArrayList<AssignedEntitiesInEntityModel>();
             for (AssignedEntitiesInEntityViewResponse assignedEntitiesInEntityViewResponse : AssignedEntitiesInEntityViewResponseList) {
            	 AssignedEntitiesInEntityModel assignedEntitiesInEntityModel = new AssignedEntitiesInEntityModel();
            	 assignedEntitiesInEntityModel.setEntityId(assignedEntitiesInEntityViewResponse.getEntityId());
            	 assignedEntitiesInEntityModel.setEntityType(assignedEntitiesInEntityViewResponse.getEntityType());
            	 assignedEntitiesInEntityModel.setRcId(assignedEntitiesInEntityViewResponse.getRcId());
            	 assignedEntitiesInEntityModel.setCbucId(assignedEntitiesInEntityViewResponse.getCbucId());
            	 assignedEntitiesInEntityModel.setEntityName(assignedEntitiesInEntityViewResponse.getEntityName());
            	 assignedEntitiesInEntityModel.setTotalBan(assignedEntitiesInEntityViewResponse.getTotalBan());
            	 assignedEntitiesInEntityModel.setTotalDelinquentBans(assignedEntitiesInEntityViewResponse.getTotalDelinquentBans());
            	 assignedEntitiesInEntityModel.setRisk(assignedEntitiesInEntityViewResponse.getRisk());
            	 assignedEntitiesInEntityModel.setEntityValue(assignedEntitiesInEntityViewResponse.getEntityValue());
            	 assignedEntitiesInEntityModel.setEntityCollectionStatus(assignedEntitiesInEntityViewResponse.getEntityCollectionStatus());
            	 assignedEntitiesInEntityModel.setManualFlag(assignedEntitiesInEntityViewResponse.isManualFlag());
            	 assignedEntitiesInEntityModel.setLastTreatment(assignedEntitiesInEntityViewResponse.getLastTreatment());
            	 assignedEntitiesInEntityModel.setCurrentAr(assignedEntitiesInEntityViewResponse.getCurrentAr());
            	 assignedEntitiesInEntityModel.setAr30Days(assignedEntitiesInEntityViewResponse.getAr30Days());
            	 assignedEntitiesInEntityModel.setAr60Days(assignedEntitiesInEntityViewResponse.getAr60Days());
            	 assignedEntitiesInEntityModel.setAr90Days(assignedEntitiesInEntityViewResponse.getAr90Days());
            	 assignedEntitiesInEntityModel.setAr120Days(assignedEntitiesInEntityViewResponse.getAr120Days());
            	 assignedEntitiesInEntityModel.setAr150Days(assignedEntitiesInEntityViewResponse.getAr150Days());
            	 assignedEntitiesInEntityModel.setAr180Days(assignedEntitiesInEntityViewResponse.getAr180Days());
            	 assignedEntitiesInEntityModel.setAr180DaysPlus(assignedEntitiesInEntityViewResponse.getAr180DaysPlus());
            	 //Added logic to add all Ar which are greater than 90 days.
            	 Double ar90DayPlus = assignedEntitiesInEntityViewResponse.getAr90Days() + assignedEntitiesInEntityViewResponse.getAr120Days() + assignedEntitiesInEntityViewResponse.getAr150Days() 
            	 + assignedEntitiesInEntityViewResponse.getAr180Days() + assignedEntitiesInEntityViewResponse.getAr180DaysPlus();
            	 assignedEntitiesInEntityModel.setAr90DaysPlus(ar90DayPlus);
            	 assignedEntitiesInEntityModel.setTotalAr(assignedEntitiesInEntityViewResponse.getTotalAr());
            	 assignedEntitiesInEntityModel.setTotalOverDue(assignedEntitiesInEntityViewResponse.getTotalOverDue());
            	 assignedEntitiesInEntityModel.setOdRemaining(assignedEntitiesInEntityViewResponse.getOdRemaining());
            	 assignedEntitiesInEntityModel.setEntityOwnerId(assignedEntitiesInEntityViewResponse.getEntityOwnerId());
            	 assignedEntitiesInEntityModel.setPrimeWorkCategory(assignedEntitiesInEntityViewResponse.getPrimeWorkCategory());
            	 assignedEntitiesInEntityModel.setPortfolioCategory(assignedEntitiesInEntityViewResponse.getPortfolioCategory());
            	 assignedEntitiesInEntityModel.setPortfolioSubCategory(assignedEntitiesInEntityViewResponse.getPortfolioSubCategory());
            	 assignedEntitiesInEntityModel.setFtnp(assignedEntitiesInEntityViewResponse.isFtnp());
            	 assignedEntitiesInEntityModel.setDisputeFlag(assignedEntitiesInEntityViewResponse.isDisputeFlag());
            	 assignedEntitiesInEntityModel.setOpenActionDate(assignedEntitiesInEntityViewResponse.getOpenActionDate());
            	 assignedEntitiesInEntityModelList.add(assignedEntitiesInEntityModel);
			}
             
             return assignedEntitiesInEntityModelList;
    	 }
        // return new Object(); 
    }
    
        ///assignedEntitiesInEntityView
    @RequestMapping(value = "/assignedEntitiesInClassicView", method = {RequestMethod.GET})
    public List<AssignedEntitiesInClassicModel> getassignedEntitiesInClassicView(@RequestParam(required = true) String entityOwner, @RequestParam(required = true) String workCategory,@RequestParam(required = true) String portfolio,@RequestParam(required = true) String billingSystem,@RequestParam(required = true) String collectionStatus, Integer offset, Integer limit) throws Exception  {

    	 if (isStubEnabled) {
        return objectMapper.readValue("[{\"banId\":\"256645999\",\"banName\":\"NORTHLAND PROPERTIES CORPORATION\",\"cbucId\":\"761846\",\"rcId\":\"392931\",\"billingSystem\":\"CES\",\"currentAr\":10,\"ar30Days\":30,\"ar60Days\":60,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"ar180Days\":1,\"ar180DaysPlus\":2,\"totalAr\":403,\"totalOverDue\":393,\"lastPaymentDate\":\"2022-08-19\",\"paymentMethod\":\"Card\",\"odRemaining\":2344390.88,\"acctStatus\":\"O\",\"acctStatusDate\":\"2022-08-29\",\"acctType\":\"B\",\"acctSubType\":\"I\",\"dispute\":2344390.88,\"language\":\"EN\",\"marketSubSegment\":\"CBU\",\"province\":\"BC\",\"cbu\":\"BC\",\"cbucidName\":\"Air Canada\",\"rcidName\":\"NORTHLAND PROPERTIES CORPORATION\",\"subPortfolio\":\"RO-ACCOUNT\",\"entityId\":6766677,\"entityStatus\":\"In Collection\",\"entityType\":\"CBUCID\",\"entityRisk\":\"Low\",\"entityValue\":\"5\",\"entityOwnerId\":\"John123\",\"banCollectionStatus\":\"In-Collection\",\"closingDate\":\"2022-12-12\",\"closingCycle\":6,\"suppresionFlag\":false},{\"banId\":\"256645900\",\"banName\":\"NORTHLAND PROPERTIES CORPORATION2\",\"cbucId\":\"761846\",\"rcId\":\"392931\",\"billingSystem\":\"CES\",\"currentAr\":10,\"ar30Days\":30,\"ar60Days\":60,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"ar180Days\":1,\"ar180DaysPlus\":2,\"totalAr\":403,\"totalOverDue\":393,\"lastPaymentDate\":\"2022-08-29\",\"paymentMethod\":\"Card\",\"odRemaining\":390,\"acctStatus\":\"O\",\"acctStatusDate\":\"2022-08-29\",\"acctType\":\"B\",\"acctSubType\":\"I\",\"dispute\":90.88,\"language\":\"EN\",\"marketSubSegment\":\"CBU\",\"province\":\"BC\",\"cbu\":\"BC\",\"cbucidName\":\"Air Canada\",\"rcidName\":\"NORTHLAND PROPERTIES CORPORATION2\",\"subPortfolio\":\"RO-ACCOUNT\",\"entityId\":6766677,\"entityStatus\":\"In Collection\",\"entityType\":\"CBUCID\",\"entityRisk\":\"Low\",\"entityValue\":\"5\",\"entityOwnerId\":\"John123\",\"banCollectionStatus\":\"In-Collection\",\"closingDate\":\"2022-12-12\",\"closingCycle\":6,\"suppresionFlag\":false}]",
        		objectMapper.getTypeFactory().constructCollectionType(List.class, AssignedEntitiesInClassicViewResponse.class));
    	 }else {
    		 logger.info("::::::::Calling  entity data endpoint call ::::::::");
    		
    		 
             UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(entityDataEndPointUrl+URIConstant.ApiMapping.ASSIGNED_ENTITIES_IN_CLASSIC_VIEW)
                     .queryParamIfPresent("entityOwner", Optional.ofNullable(entityOwner))
                     .queryParamIfPresent("workCategory", Optional.ofNullable(workCategory))
                     .queryParamIfPresent("portfolio", Optional.ofNullable(portfolio))
                     .queryParamIfPresent("billingSystem", Optional.ofNullable(billingSystem))
                     .queryParamIfPresent("collectionStatus", Optional.ofNullable(collectionStatus));

             String endPointString = builder.toUriString().replace("%7C", "|").replace("%20", " ");
             
             String responseStr = telusAPIConnectivityService.executeTelusAPI(null,endPointString, HttpMethod.GET, entitySvcAuthScope);
             logger.info("::::::::Entity data endpoint call success ::::::::");
             logger.info("Response---"+ responseStr);
             
             List<AssignedEntitiesInClassicViewResponse> assignedEntitiesInClassicViewResList = objectMapper.readValue(responseStr,objectMapper.getTypeFactory().constructCollectionType(List.class, AssignedEntitiesInClassicViewResponse.class));
             
             List<AssignedEntitiesInClassicModel> assignedEntitiesInClassicModelList = new ArrayList<AssignedEntitiesInClassicModel>();
             for (AssignedEntitiesInClassicViewResponse assignedEntitiesInClassicViewResponse : assignedEntitiesInClassicViewResList) {
				AssignedEntitiesInClassicModel assignedEntitiesInClassicModel = new AssignedEntitiesInClassicModel();
				assignedEntitiesInClassicModel.setBanId(assignedEntitiesInClassicViewResponse.getBanId());
				assignedEntitiesInClassicModel.setBanName(assignedEntitiesInClassicViewResponse.getBanName());
				assignedEntitiesInClassicModel.setCbucId(assignedEntitiesInClassicViewResponse.getCbucId());
				assignedEntitiesInClassicModel.setRcId(assignedEntitiesInClassicViewResponse.getRcId());
				assignedEntitiesInClassicModel.setBillingSystem(assignedEntitiesInClassicViewResponse.getBillingSystem());
				assignedEntitiesInClassicModel.setCurrentAr(assignedEntitiesInClassicViewResponse.getCurrentAr());
				assignedEntitiesInClassicModel.setAr30Days(assignedEntitiesInClassicViewResponse.getAr30Days());
				assignedEntitiesInClassicModel.setAr60Days(assignedEntitiesInClassicViewResponse.getAr60Days());
				assignedEntitiesInClassicModel.setAr90Days(assignedEntitiesInClassicViewResponse.getAr90Days());
				assignedEntitiesInClassicModel.setAr120Days(assignedEntitiesInClassicViewResponse.getAr120Days());
				assignedEntitiesInClassicModel.setAr150Days(assignedEntitiesInClassicViewResponse.getAr150Days());
				assignedEntitiesInClassicModel.setAr180Days(assignedEntitiesInClassicViewResponse.getAr180Days());
				assignedEntitiesInClassicModel.setAr180DaysPlus(assignedEntitiesInClassicViewResponse.getAr180DaysPlus());
				Double ar90DaysPlus = assignedEntitiesInClassicViewResponse.getAr90Days() + assignedEntitiesInClassicViewResponse.getAr120Days() +
						assignedEntitiesInClassicViewResponse.getAr150Days() + assignedEntitiesInClassicViewResponse.getAr180Days() + assignedEntitiesInClassicViewResponse.getAr180DaysPlus();
				
				assignedEntitiesInClassicModel.setAr90DaysPlus(ar90DaysPlus);
				assignedEntitiesInClassicModel.setTotalAr(assignedEntitiesInClassicViewResponse.getTotalAr());
				assignedEntitiesInClassicModel.setTotalOverDue(assignedEntitiesInClassicViewResponse.getTotalOverDue());
				assignedEntitiesInClassicModel.setLastPaymentDate(assignedEntitiesInClassicViewResponse.getLastPaymentDate());
				assignedEntitiesInClassicModel.setOdRemaining(assignedEntitiesInClassicViewResponse.getOdRemaining());
				assignedEntitiesInClassicModel.setAcctStatus(assignedEntitiesInClassicViewResponse.getAcctStatus());
				assignedEntitiesInClassicModel.setAcctStatusDate(assignedEntitiesInClassicViewResponse.getAcctStatusDate());
				assignedEntitiesInClassicModel.setAcctType(assignedEntitiesInClassicViewResponse.getAcctType());
				assignedEntitiesInClassicModel.setAcctSubType(assignedEntitiesInClassicViewResponse.getAcctSubType());
				assignedEntitiesInClassicModel.setDisputeAmount(assignedEntitiesInClassicViewResponse.getDisputeAmount());
				assignedEntitiesInClassicModel.setSuppresionFlag(assignedEntitiesInClassicViewResponse.isSuppresionFlag());
				assignedEntitiesInClassicModel.setLanguage(assignedEntitiesInClassicViewResponse.getLanguage());
				assignedEntitiesInClassicModel.setMarketSubSegment(assignedEntitiesInClassicViewResponse.getMarketSubSegment());
				assignedEntitiesInClassicModel.setProvince(assignedEntitiesInClassicViewResponse.getProvince());
				assignedEntitiesInClassicModel.setCbu(assignedEntitiesInClassicViewResponse.getCbu());
				assignedEntitiesInClassicModel.setCbucidName(assignedEntitiesInClassicViewResponse.getCbucidName());
				assignedEntitiesInClassicModel.setRcidName(assignedEntitiesInClassicViewResponse.getRcidName());
				//assignedEntitiesInClassicModel.setSubPortfolio(assignedEntitiesInClassicViewResponse.getSubPortfolio());
				assignedEntitiesInClassicModel.setPortfolioCategory(assignedEntitiesInClassicViewResponse.getPortfolioCategory());
				assignedEntitiesInClassicModel.setPortfolioSubCategory(assignedEntitiesInClassicViewResponse.getPortfolioSubCategory());
				assignedEntitiesInClassicModel.setEntityId(assignedEntitiesInClassicViewResponse.getEntityId());
				assignedEntitiesInClassicModel.setEntityStatus(assignedEntitiesInClassicViewResponse.getEntityCollectionStatus());
				assignedEntitiesInClassicModel.setEntityType(assignedEntitiesInClassicViewResponse.getEntityType());
				assignedEntitiesInClassicModel.setEntityRisk(assignedEntitiesInClassicViewResponse.getEntityRisk());
				assignedEntitiesInClassicModel.setEntityValue(assignedEntitiesInClassicViewResponse.getEntityValue());
				assignedEntitiesInClassicModel.setEntityOwnerId(assignedEntitiesInClassicViewResponse.getEntityOwnerId());
				assignedEntitiesInClassicModel.setBanCollectionStatus(assignedEntitiesInClassicViewResponse.getBanCollectionStatus());
				assignedEntitiesInClassicModel.setClosingDate(assignedEntitiesInClassicViewResponse.getClosingDate());
				assignedEntitiesInClassicModel.setClosingCycle(assignedEntitiesInClassicViewResponse.getClosingCycle());
				assignedEntitiesInClassicModelList.add(assignedEntitiesInClassicModel);
			}
             
            return assignedEntitiesInClassicModelList;
    	 }
    }
    

//entityContacts
    @RequestMapping(value = "/entityContacts", method = {RequestMethod.GET})
    public EntityContactsResponse getEntityContacts(@RequestParam(required = true) Integer entityId) throws Exception  {
    	 if (isStubEnabled) {
        return objectMapper.readValue("{\"mailingContacts\":[{\"entityId\":1,\"banId\":\"1\",\"banName\":\"BACCT1\",\"billingSystem\":\"CES9\",\"careOf\":\"CareOf\",\"unitNumber\":\"Unit Number\",\"streetNumber\":\"111\",\"streetNumberSuffix\":\"streetSuffix\",\"streetName\":\"Britannia Ave\",\"streetType\":\"streetType\",\"streetDirection\":\"stDirection\",\"city\":\"Oshawa\",\"postalCode\":\"L1L 0B4\",\"province\":\"ON\"},{\"entityId\":1,\"banId\":\"1\",\"banName\":\"BACCT1\",\"billingSystem\":\"CES9\",\"careOf\":\"CareOf\",\"streetDirection\":\"stDirection\",\"poBox\":\"P O Box 123\",\"postalCode\":\"L1L 0B4\",\"province\":\"ON\"}],\"digitalContacts\":[{\"contactId\":1,\"entityId\":1,\"rcId\":\"rc-name-12345\",\"sourceOfContact\":\"TCM\",\"contactForNotices\":false,\"telusContacts\":false,\"title\":\"Mr.\",\"firstName\":\"John1\",\"lastName\":\"Doe1\",\"email\":\"john1.doe1@telus.com\",\"workNumber\":\"9059974001\",\"mobileNumber\":\"5149979001\",\"faxNumber\":\"9059974008\",\"workPhoneExt\":\"511\"},{\"contactId\":2,\"entityId\":1,\"rcId\":\"rc-name-12345\",\"sourceOfContact\":\"TCM\",\"contactForNotices\":false,\"telusContacts\":false,\"title\":\"Mr.\",\"firstName\":\"John2\",\"lastName\":\"Doe2\",\"email\":\"john2.doe2@telus.com\",\"workNumber\":\"9059974002\",\"mobileNumber\":\"5149979002\",\"faxNumber\":\"9059974008\",\"workPhoneExt\":\"311\"},{\"contactId\":3,\"entityId\":1,\"rcId\":\"rc-name-12345\",\"sourceOfContact\":\"FAWB\",\"contactForNotices\":true,\"telusContacts\":true,\"title\":\"Mr.\",\"firstName\":\"Kevin\",\"lastName\":\"Smith\",\"email\":\"Kevin.Smith@telus.com\",\"workNumber\":\"9059974002\",\"mobileNumber\":\"5149979002\",\"faxNumber\":\"9059974008\",\"workPhoneExt\":\"211\"}]}",EntityContactsResponse.class);
    	 }
    	 else{

             logger.info("::::::::Calling  entityContacts endpoint call ::::::::");
             UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(entityDataEndPointUrl+URIConstant.ApiMapping.ENTITY_CONTACTS)
                     .queryParam("entityId", entityId);
             if(entityId != null) {
	             String responseStr = telusAPIConnectivityService.executeTelusAPI(null,builder.toUriString(), HttpMethod.GET, entitySvcAuthScope);
	             logger.info("::::::::Entity data endpoint call success ::::::::");
	             logger.info("Resoinse---"+ responseStr);
	             EntityContactsResponse entityContactsResponse = objectMapper.readValue(responseStr,EntityContactsResponse.class);
	             return entityContactsResponse;
             }

         }
		return null;
    	 
    	 }

    
    
//entityBanDetails
    @RequestMapping(value = "/entityBanDetails", method = {RequestMethod.GET})
    public List<EntityBanDetailsResponse> getEntityBanDetails(Integer entityId, Integer offset, Integer limit) throws Exception  {
    	if (isStubEnabled) {
        return objectMapper.readValue("[{\"entityId\":1,\"banId\":\"1\",\"banRefId\":1,\"banStatus\":\"C\",\"banName\":\"BACCT1\",\"banArAmount\":-132.22,\"banOverdueAmount\":-133.77,\"lineOfBusiness\":\"WLN\",\"closingDate\":\"2022-12-12\",\"closingCycle\":6,\"suppresionFlag\":false,\"disputeFlag\":true},{\"entityId\":2,\"banId\":\"2\",\"banRefId\":2,\"banStatus\":\"D\",\"banName\":\"BACCT2\",\"banArAmount\":150.22,\"banOverdueAmount\":160.77,\"lineOfBusiness\":\"WLN\",\"closingDate\":\"2022-12-12\",\"closingCycle\":6,\"suppresionFlag\":false,\"disputeFlag\":true}]",
        objectMapper.getTypeFactory().constructCollectionType(List.class, EntityBanDetailsResponse.class));
    	}else {
    	    List<EntityBanDetailsResponse> entityBanDetailsResponseList=new ArrayList();
    		 logger.info("::::::::Calling  entityBanDetails endpoint call ::::::::");
             UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(entityDataEndPointUrl+URIConstant.ApiMapping.ENTITY_BAN_DETAILS)
                     .queryParam("entityId", entityId);
             String responseStr = telusAPIConnectivityService.executeTelusAPI(null,builder.toUriString(), HttpMethod.GET, entitySvcAuthScope);
             logger.info("::::::::Entity Ban details endpoint call success ::::::::");
             logger.info("Response payload for Ban Details---"+ responseStr);
             if(responseStr!=null){
              entityBanDetailsResponseList = objectMapper.readValue(responseStr,
     				objectMapper.getTypeFactory().constructCollectionType(List.class, EntityBanDetailsResponse.class));
             }
             return entityBanDetailsResponseList;
    	}
    }
    
//entityDetails
    @RequestMapping(value = "/entityDetails", method = {RequestMethod.GET})
    public EntityDetailsResponse getEntityDetails(String entityId) throws Exception  {

 if (isStubEnabled) {
        return objectMapper.readValue("{\"entityDetails\":{\"entityId\":6766677,\"entityType\":\"RCID\",\"rcId\":\"224343\",\"cbucId\":\"123\",\"entityName\":\"Air Canada\",\"totalBan\":10,\"totalDelinquentBans\":5,\"risk\":\"Low\",\"entityValue\":\"Low\",\"entityCollectionStatus\":\"Open\",\"manualFlag\":false,\"lastTreatment\":\"SUSP\",\"currentAr\":10,\"ar30Days\":30,\"ar60Days\":60,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"ar180Days\":1,\"ar180DaysPlus\":2,\"totalAr\":403,\"totalOverDue\":393,\"entityOwnerId\":\"John123\",\"primeWorkCategory\":\"aliqua eu ut\",\"portfolioCategory\":\"SMB\",\"portfolioSubCategory\":\"PUBLIC LARGE\",\"ftnp\":true,\"disputeFlag\":true},\"banDetails\":[{\"banId\":256645999,\"banName\":\"NORTHLAND PROPERTIES CORPORATION\",\"cbucId\":\"761846\",\"rcId\":\"392931\",\"billingSystem\":\"CES\",\"currentAr\":10,\"ar30Days\":30,\"ar60Days\":60,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"ar180Days\":1,\"ar180DaysPlus\":2,\"totalAr\":403,\"totalOverDue\":393,\"lastPaymentDate\":\"2022-08-29\",\"paymentMethod\":\"Card\",\"odRemaining\":2344390.88,\"acctStatus\":\"O\",\"acctStatusDate\":\"2022-08-29\",\"acctType\":\"B\",\"acctSubType\":\"I\",\"dispute\":2344390.88,\"language\":\"EN\",\"marketSubSegment\":\"CBU\",\"province\":\"BC\",\"cbu\":\"BC\",\"cbucidName\":\"Air Canada\",\"rcidName\":\"NORTHLAND PROPERTIES CORPORATION\",\"subPortfolio\":\"RO-ACCOUNT\",\"entityId\":6766677,\"entityStatus\":\"In Collection\",\"entityType\":\"CBUCID\",\"entityRisk\":\"Low\",\"entityValue\":\"5\",\"entityOwnerId\":\"John123\",\"banCollectionStatus\":\"In-Collection\",\"closingDate\":\"2022-12-12\",\"closingCycle\":6,\"suppresionFlag\":false},{\"banId\":\"256645900\",\"banName\":\"NORTHLAND PROPERTIES CORPORATION2\",\"cbucId\":\"761846\",\"rcId\":\"392931\",\"billingSystem\":\"CES\",\"currentAr\":10,\"ar30Days\":30,\"ar60Days\":60,\"ar90Days\":90,\"ar120Days\":120,\"ar150Days\":150,\"ar180Days\":1,\"ar180DaysPlus\":2,\"totalAr\":403,\"totalOverDue\":393,\"lastPaymentDate\":\"2022-08-29\",\"paymentMethod\":\"Card\",\"odRemaining\":390,\"acctStatus\":\"O\",\"acctStatusDate\":\"2022-08-29\",\"acctType\":\"B\",\"acctSubType\":\"I\",\"dispute\":90.88,\"language\":\"EN\",\"marketSubSegment\":\"CBU\",\"province\":\"BC\",\"cbu\":\"BC\",\"cbucidName\":\"Air Canada\",\"rcidName\":\"NORTHLAND PROPERTIES CORPORATION2\",\"subPortfolio\":\"RO-ACCOUNT\",\"entityId\":6766677,\"entityStatus\":\"In Collection\",\"entityType\":\"CBUCID\",\"entityRisk\":\"Low\",\"entityValue\":\"5\",\"entityOwnerId\":\"John123\",\"banCollectionStatus\":\"In-Collection\",\"closingDate\":\"2022-12-12\",\"closingCycle\":6,\"suppresionFlag\":false}]}",
        EntityDetailsResponse.class);
    
     } else {
             logger.info("::::::::Calling  entity details endpoint call ::::::::");
             UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(entityDataEndPointUrl + URIConstant.ApiMapping.ENTITY_DETAILS)
            		 .queryParam("entityId", entityId);

             String responseStr = telusAPIConnectivityService.executeTelusAPI(null, builder.toUriString(), HttpMethod.GET, entitySvcAuthScope);
             logger.info("::::::::Entity details endpoint call success ::::::::");
            logger.info("Resoinse---" + responseStr);
            
            if(!StringUtils.isEmpty(responseStr)) {
                 return objectMapper.readValue(responseStr,  EntityDetailsResponse.class);
             }else{
                return null;
             }


        }


    }
    
    @RequestMapping(value = "/actionViewByTeam", method = {RequestMethod.GET})
    public List<TeamsActionViewResponse> getActionViewByTeam(String assignedAgent, String assignedTeam,String entityOwner, Date fromDueDate, Date toDueDate, String actionType, String status, String workCategory, String viewType, Integer offset, Integer limit) throws Exception {
    	Boolean isActionViewByTeamStubEnabled = true;
    	//IMP - change this once the data comes back from API.
    	if(isActionViewByTeamStubEnabled) {
    	return objectMapper.readValue("[{\"actionId\":6766677,\"entityId\":1,\"entityName\":\"Air Canada\",\"entityType\":\"RCID\",\"collectionStatus\":\"In-collection\",\"actionType\":\"Suspend\",\"actionDescription\":\"Suspension\",\"priority\":\"High\",\"dueDate\":\"2022-08-29\",\"status\":\"Open\",\"assignedTeam\":\"TIG\",\"assignedAgent\":\"John 123\",\"workCategory\":\"workCategory1\",\"totalAr\":400,\"totalOverDue\":390},{\"actionId\":6766678,\"entityId\":2,\"entityName\":\"Air Canada\",\"entityType\":\"RCID\",\"collectionStatus\":\"In-collection\",\"actionType\":\"Suspend\",\"actionDescription\":\"Suspension\",\"priority\":\"High\",\"dueDate\":\"2022-08-29\",\"status\":\"Open\",\"assignedTeam\":\"TIG\",\"assignedAgent\":\"John 123\",\"workCategory\":\"workCategory2\",\"totalAr\":500,\"totalOverDue\":490},{\"actionId\":6766679,\"entityId\":16,\"entityName\":\"Air Canada\",\"entityType\":\"RCID\",\"collectionStatus\":\"In-collection\",\"actionType\":\"Suspend\",\"actionDescription\":\"Suspension\",\"priority\":\"High\",\"dueDate\":\"2022-08-29\",\"status\":\"Open\",\"assignedTeam\":\"TIG\",\"assignedAgent\":\"John 123\",\"workCategory\":\"workCategory3\",\"totalAr\":600,\"totalOverDue\":690},{\"actionId\":67662,\"entityId\":2,\"entityName\":\"Air Canada\",\"entityOwner\":\"Ayushi Deshmukh\",\"entityType\":\"CBUCID\",\"collectionStatus\":\"In-collection\",\"actionType\":\"cbucid\",\"actionDescription\":\"Suspension\",\"priority\":\"Medium\",\"dueDate\":\"2022-08-29\",\"status\":\"Request Created\",\"assignedTeam\":\"TD OM CHECK\",\"assignedAgent\":\"John 345\",\"workCategory\":\"IN\",\"totalAr\":440,\"totalOverDue\":356},{\"actionId\":37662,\"entityId\":16,\"entityName\":\"Air Canada3\",\"entityOwner\":\"Ravi Kumar\",\"entityType\":\"RCID\",\"collectionStatus\":\"In-collection\",\"actionType\":\"cbucid33\",\"actionDescription\":\"CEASE\",\"priority\":\"High\",\"dueDate\":\"2022-06-30\",\"status\":\"Request Created\",\"assignedTeam\":\"TD OM CHECK\",\"assignedAgent\":\"John 789\",\"workCategory\":\"AR Excluded\",\"totalAr\":480,\"totalOverDue\":956},{\"actionId\":651662,\"entityId\":17,\"entityName\":\"Air Canada\",\"entityOwner\":\"Ayushi Deshmukh\",\"entityType\":\"Entity\",\"collectionStatus\":\"In-collection\",\"actionType\":\"cbucid\",\"actionDescription\":\"Restore\",\"priority\":\"Low\",\"dueDate\":\"2022-10-29\",\"status\":\"Request Created\",\"assignedTeam\":\"recheck\",\"assignedAgent\":\"John 123\",\"workCategory\":\"WLN PARTNER EN\",\"totalAr\":460,\"totalOverDue\":676},{\"actionId\":67342,\"entityId\":18,\"entityName\":\"Air Canada90\",\"entityOwner\":\"Yamini Sharma\",\"entityType\":\"BAN\",\"collectionStatus\":\"In-collection\",\"actionType\":\"cbucid\",\"actionDescription\":\"Suspension\",\"priority\":\"High\",\"dueDate\":\"2022-08-29\",\"status\":\"Order fulfilled\",\"assignedTeam\":\"TD OM CHECK\",\"assignedAgent\":\"John 123\",\"workCategory\":\"WLN PARTNER FR\",\"totalAr\":4900,\"totalOverDue\":3560},{\"actionId\":7662,\"entityId\":19,\"entityName\":\"Air Canada\",\"entityOwner\":\"Aarya Willy\",\"entityType\":\"CBUCID\",\"collectionStatus\":\"In-collection\",\"actionType\":\"cbucid\",\"actionDescription\":\"CEASE\",\"priority\":\"Medium\",\"dueDate\":\"2023-01-29\",\"status\":\"Request Created\",\"assignedTeam\":\"RETEST\",\"assignedAgent\":\"John 123\",\"workCategory\":\"WLN SMALL EN\",\"totalAr\":4440,\"totalOverDue\":3506},{\"actionId\":674462,\"entityId\":20,\"entityName\":\"Air Canada\",\"entityOwner\":\"Sachin Balgi\",\"entityType\":\"RCID\",\"collectionStatus\":\"In-collection\",\"actionType\":\"cbucid\",\"actionDescription\":\"Res'\",\"priority\":\"Low\",\"dueDate\":\"2022-08-09\",\"status\":\"Request Created\",\"assignedTeam\":\"TD OM CHECK\",\"assignedAgent\":\"John 123\",\"workCategory\":\"WLN PARTNER EN\",\"totalAr\":44990,\"totalOverDue\":33356},{\"actionId\":696682,\"entityId\":21,\"entityName\":\"Air Canada\",\"entityOwner\":\"Aary Bill\",\"entityType\":\"Entity\",\"collectionStatus\":\"In-collection123\",\"actionType\":\"RCID\",\"actionDescription\":\"Suspension\",\"priority\":\"High\",\"dueDate\":\"2022-05-29\",\"status\":\"Request Created\",\"assignedTeam\":\"TD OM CHECK\",\"assignedAgent\":\"John 123\",\"workCategory\":\"AR Excluded\",\"totalAr\":44780,\"totalOverDue\":6356},{\"actionId\":6767762,\"entityId\":22,\"entityName\":\"Air Canada\",\"entityOwner\":\"Sequence test\",\"entityType\":\"BAN\",\"collectionStatus\":\"In-collect\",\"actionType\":\"cbucid\",\"actionDescription\":\"Suspension\",\"priority\":\"Medium\",\"dueDate\":\"2022-01-29\",\"status\":\"Request Created\",\"assignedTeam\":\"TD OM CHECK\",\"assignedAgent\":\"Johny 123\",\"workCategory\":\"WLN LARGE FR\",\"totalAr\":4540,\"totalOverDue\":1356},{\"actionId\":676562,\"entityId\":22,\"entityName\":\"Air Canada\",\"entityOwner\":\"Hemlatha Loganathrao\",\"entityType\":\"RCID\",\"collectionStatus\":\"In-collection\",\"actionType\":\"cbucid\",\"actionDescription\":\"Suspension\",\"priority\":\"Low\",\"dueDate\":\"2022-08-09\",\"status\":\"Request Assigned\",\"assignedTeam\":\"TEST\",\"assignedAgent\":\"John 678\",\"workCategory\":\"WLN SMALL EN\",\"totalAr\":9000,\"totalOverDue\":1356},{\"actionId\":689662,\"entityId\":1,\"entityName\":\"Air Canada\",\"entityOwner\":\"Ravi Kumar\",\"entityType\":\"BAN\",\"collectionStatus\":\"In-collection\",\"actionType\":\"cbucid\",\"actionDescription\":\"Suspension\",\"priority\":\"High\",\"dueDate\":\"2022-08-29\",\"status\":\"Request Created\",\"assignedTeam\":\"sds\",\"assignedAgent\":\"John 123\",\"workCategory\":\"IN\",\"totalAr\":4400,\"totalOverDue\":13569},{\"actionId\":600662,\"entityId\":2,\"entityName\":\"ABC Canada\",\"entityOwner\":\"Deepak Vishwanath\",\"entityType\":\"CBUCID\",\"collectionStatus\":\"In-collection1\",\"actionType\":\"cbucid45\",\"actionDescription\":\"CEASE\",\"priority\":\"Low\",\"dueDate\":\"2022-01-29\",\"status\":\"Request Created\",\"assignedTeam\":\"BC Team\",\"assignedAgent\":\"Roy Bill\",\"workCategory\":\"WLN PARTNER FR\",\"totalAr\":49900,\"totalOverDue\":29569},{\"actionId\":68963,\"entityId\":16,\"entityName\":\"Air Region\",\"entityOwner\":\"Ingrid Altamirano\",\"entityType\":\"Entity\",\"collectionStatus\":\"In-collections2\",\"actionType\":\"cbucid56\",\"actionDescription\":\"Suspension\",\"priority\":\"High\",\"dueDate\":\"2022-08-19\",\"status\":\"Request Assigned\",\"assignedTeam\":\"TD OM CHECK\",\"assignedAgent\":\"Johny\",\"workCategory\":\"WLN PARTNER EN\",\"totalAr\":4450,\"totalOverDue\":10569},{\"actionId\":68562,\"entityId\":17,\"entityName\":\"Cana AIR\",\"entityOwner\":\"Fourth User45\",\"entityType\":\"RCID\",\"collectionStatus\":\"In-collection3\",\"actionType\":\"cbucid90\",\"actionDescription\":\"Restore\",\"priority\":\"Medium\",\"dueDate\":\"2022-09-09\",\"status\":\"Request Created\",\"assignedTeam\":\"HHH\",\"assignedAgent\":\"Vernoica\",\"workCategory\":\"AR Excluded\",\"totalAr\":448980,\"totalOverDue\":569},{\"actionId\":61662,\"entityId\":18,\"entityName\":\"TIG CANADA\",\"entityOwner\":\"Second user\",\"entityType\":\"BAN\",\"collectionStatus\":\"In-collection11\",\"actionType\":\"cbucid23\",\"actionDescription\":\"Suspension\",\"priority\":\"High\",\"dueDate\":\"2022-12-02\",\"status\":\"Open\",\"assignedTeam\":\"XYZ\",\"assignedAgent\":\"Ravial\",\"workCategory\":\"WLN LARGE FR\",\"totalAr\":40,\"totalOverDue\":19},{\"actionId\":6896621,\"entityId\":19,\"entityName\":\"AC AIT\",\"entityOwner\":\"Third User\",\"entityType\":\"CBUCID\",\"collectionStatus\":\"In-collection88\",\"actionType\":\"cbu2id\",\"actionDescription\":\"CEASE\",\"priority\":\"Medium\",\"dueDate\":\"2023-08-08\",\"status\":\"Request Created\",\"assignedTeam\":\"RETEST\",\"assignedAgent\":\"John 123\",\"workCategory\":\"IN\",\"totalAr\":9400,\"totalOverDue\":13699},{\"actionId\":6892,\"entityId\":20,\"entityName\":\"SOUT AIR\",\"entityOwner\":\"Rachel Win12\",\"entityType\":\"RCID\",\"collectionStatus\":\"In-collect\",\"actionType\":\"cbud\",\"actionDescription\":\"Restore\",\"priority\":\"Low\",\"dueDate\":\"2024-11-01\",\"status\":\"Request Assigned\",\"assignedTeam\":\"MESSAGE\",\"assignedAgent\":\"Foy Johny\",\"workCategory\":\"WLN SMALL EN\",\"totalAr\":47800,\"totalOverDue\":179},{\"actionId\":6092,\"entityId\":21,\"entityName\":\"AIG xyz\",\"entityOwner\":\"Franklin Dev\",\"entityType\":\"Entity\",\"collectionStatus\":\"In-COLL\",\"actionType\":\"cbd\",\"actionDescription\":\"Suspension\",\"priority\":\"High\",\"dueDate\":\"2023-08-03\",\"status\":\"Order fulfilled\",\"assignedTeam\":\"sds\",\"assignedAgent\":\"BOB Winy\",\"workCategory\":\"WLN PARTNER FR\",\"totalAr\":4890,\"totalOverDue\":9999},{\"actionId\":111162,\"entityId\":22,\"entityName\":\"AIR NORAM\",\"entityOwner\":\"Retest User\",\"entityType\":\"BAN\",\"collectionStatus\":\"COLL_IN\",\"actionType\":\"rcid\",\"actionDescription\":\"CEASE\",\"priority\":\"Medium\",\"dueDate\":\"2022-08-29\",\"status\":\"Request Created\",\"assignedTeam\":\"Supporting Analyst Team\",\"assignedAgent\":\"Rachel Oly\",\"workCategory\":\"WLN SMALL EN\",\"totalAr\":44005,\"totalOverDue\":119069},{\"actionId\":602444,\"entityId\":21,\"entityName\":\"CAN AIR\",\"entityOwner\":\"Ross Bill\",\"entityType\":\"CBUCID\",\"collectionStatus\":\"Out-COLL\",\"actionType\":\"entity\",\"actionDescription\":\"Restore\",\"priority\":\"Low\",\"dueDate\":\"2024-05-31\",\"status\":\"Request Assigned\",\"assignedTeam\":\"create team\",\"assignedAgent\":\"Jolly BRON\",\"workCategory\":\"WLN PARTNER EN\",\"totalAr\":440098,\"totalOverDue\":83569},{\"actionId\":66662,\"entityId\":20,\"entityName\":\"GIF CANADA\",\"entityOwner\":\"Pearl Win\",\"entityType\":\"RCID\",\"collectionStatus\":\"In-action\",\"actionType\":\"ban\",\"actionDescription\":\"Suspension\",\"priority\":\"High\",\"dueDate\":\"2023-12-09\",\"status\":\"Order fulfilled\",\"assignedTeam\":\"TI Guatemala\",\"assignedAgent\":\"PEARL BOSS\",\"workCategory\":\"AR Excluded\",\"totalAr\":90400,\"totalOverDue\":100769},{\"actionId\":6190902,\"entityId\":21,\"entityName\":\"AIR_BIM\",\"entityOwner\":\"Ross Bill\",\"entityType\":\"Entity\",\"collectionStatus\":\"WLN_COLL\",\"actionType\":\"rcid\",\"actionDescription\":\"CEASE\",\"priority\":\"Medium\",\"dueDate\":\"2021-02-04\",\"status\":\"Request Assigned\",\"assignedTeam\":\"TEST\",\"assignedAgent\":\"DAVID BILL\",\"workCategory\":\"WLN LARGE FR\",\"totalAr\":489890,\"totalOverDue\":10469},{\"actionId\":6732,\"entityId\":1,\"entityName\":\"Air CAN\",\"entityOwner\":\"Second user\",\"entityType\":\"CBUCID\",\"collectionStatus\":\"In-co\",\"actionType\":\"cbuc\",\"actionDescription\":\"Suspension\",\"priority\":\"High\",\"dueDate\":\"2012-08-29\",\"status\":\"Open\",\"assignedTeam\":\"TD OM CHECK\",\"assignedAgent\":\"John 34\",\"workCategory\":\"WLN PARTNER FR\",\"totalAr\":44900,\"totalOverDue\":35689},{\"actionId\":67,\"entityId\":2,\"entityName\":\"Air ad\",\"entityOwner\":\"Deepak Vishwanath\",\"entityType\":\"RCID\",\"collectionStatus\":\"In-colln\",\"actionType\":\"c45\",\"actionDescription\":\"Cease\",\"priority\":\"Medium\",\"dueDate\":\"2022-08-19\",\"status\":\"Request Created\",\"assignedTeam\":\"BC Team\",\"assignedAgent\":\"Jyu23\",\"workCategory\":\"WLN PARTNER FR\",\"totalAr\":4540,\"totalOverDue\":356260},{\"actionId\":6791010,\"entityId\":16,\"entityName\":\"Ai12ada\",\"entityOwner\":\"Ingrid Altamirano\",\"entityType\":\"Entity\",\"collectionStatus\":\"In-23lection\",\"actionType\":\"cbucid\",\"actionDescription\":\"Suspension\",\"priority\":\"Medium\",\"dueDate\":\"2022-08-29\",\"status\":\"Request Created\",\"assignedTeam\":\"TD OM CHECK\",\"assignedAgent\":\"John 345\",\"workCategory\":\"IN\",\"totalAr\":440,\"totalOverDue\":356},{\"actionId\":60072,\"entityId\":17,\"entityName\":\"Air C78\",\"entityOwner\":\"Fourth User45\",\"entityType\":\"BAN\",\"collectionStatus\":\"In-colion\",\"actionType\":\"cbu78\",\"actionDescription\":\"Cease\",\"priority\":\"Low\",\"dueDate\":\"2023-01-02\",\"status\":\"Request Assigned\",\"assignedTeam\":\"BC Team\",\"assignedAgent\":\"BOBY 345\",\"workCategory\":\"WLN PARTNER FR\",\"totalAr\":4656,\"totalOverDue\":35667},{\"actionId\":61162,\"entityId\":18,\"entityName\":\"Air Cany\",\"entityOwner\":\"Second user\",\"entityType\":\"RCID\",\"collectionStatus\":\"In-collect90\",\"actionType\":\"cb88id\",\"actionDescription\":\"Restore\",\"priority\":\"High\",\"dueDate\":\"2023-08-09\",\"status\":\"Request Created\",\"assignedTeam\":\"Request Assigned\",\"assignedAgent\":\"Johny boby 345\",\"workCategory\":\"WLN SMALL EN\",\"totalAr\":478780,\"totalOverDue\":35611},{\"actionId\":675562,\"entityId\":19,\"entityName\":\"Aada CAN\",\"entityOwner\":\"Third User\",\"entityType\":\"Entity\",\"collectionStatus\":\"In-COLLECT\",\"actionType\":\"cb789\",\"actionDescription\":\"Suspension\",\"priority\":\"Medium\",\"dueDate\":\"2022-12-02\",\"status\":\"Request Assigned\",\"assignedTeam\":\"HHH\",\"assignedAgent\":\"Monica\",\"workCategory\":\"WLN PARTNER EN\",\"totalAr\":477790,\"totalOverDue\":35601},{\"actionId\":62,\"entityId\":20,\"entityName\":\"Air Canada2434\",\"entityOwner\":\"Rachel Win12\",\"entityType\":\"BAN\",\"collectionStatus\":\"COLLECTION_IN\",\"actionType\":\"cbucid789\",\"actionDescription\":\"Cease\",\"priority\":\"Low\",\"dueDate\":\"2022-11-08\",\"status\":\"Order fulfilled\",\"assignedTeam\":\"MESSAGE\",\"assignedAgent\":\"PRERNA 23\",\"workCategory\":\"AR Excluded\",\"totalAr\":4880,\"totalOverDue\":37855},{\"actionId\":61162,\"entityId\":21,\"entityName\":\"Air AFR\",\"entityOwner\":\"Franklin Dev\",\"entityType\":\"CBUCID\",\"collectionStatus\":\"In-23ion\",\"actionType\":\"entity\",\"actionDescription\":\"Restore\",\"priority\":\"High\",\"dueDate\":\"2022-10-30\",\"status\":\"Open\",\"assignedTeam\":\"XYZ\",\"assignedAgent\":\"BOBY BILL45\",\"workCategory\":\"WLN SMALL FR\",\"totalAr\":1440,\"totalOverDue\":6},{\"actionId\":1162000,\"entityId\":22,\"entityName\":\"Air Canada678\",\"entityOwner\":\"Ayushi Deshmukh\",\"entityType\":\"CBUCID\",\"collectionStatus\":\"In-collection\",\"actionType\":\"cbucid\",\"actionDescription\":\"Suspension\",\"priority\":\"Medium\",\"dueDate\":\"2022-08-29\",\"status\":\"Request Created\",\"assignedTeam\":\"TD OM CHECK\",\"assignedAgent\":\"John 345\",\"workCategory\":\"IN\",\"totalAr\":441110,\"totalOverDue\":3561111},{\"actionId\":670090,\"entityId\":23,\"entityName\":\"Air 234\",\"entityOwner\":\"Ross Bill\",\"entityType\":\"RCID\",\"collectionStatus\":\"In-collection\",\"actionType\":\"cbucid\",\"actionDescription\":\"Suspension\",\"priority\":\"Medium\",\"dueDate\":\"2022-08-29\",\"status\":\"Request Created\",\"assignedTeam\":\"RETEST\",\"assignedAgent\":\"Jonny DOD\",\"workCategory\":\"IN\",\"totalAr\":4490090900,\"totalOverDue\":3561111},{\"actionId\":6909062,\"entityId\":24,\"entityName\":\"Air AIR 234\",\"entityOwner\":\"Pearl Win\",\"entityType\":\"Entity\",\"collectionStatus\":\"In-collion status\",\"actionType\":\"cbucid7890\",\"actionDescription\":\"Cease\",\"priority\":\"Low\",\"dueDate\":\"2024-05-17\",\"status\":\"Request Assigned\",\"assignedTeam\":\"BC Team\",\"assignedAgent\":\"Johny BILL 345\",\"workCategory\":\"WLN PARTNER FR\",\"totalAr\":4406635,\"totalOverDue\":3561212},{\"actionId\":679090,\"entityId\":25,\"entityName\":\"Air Canadayy\",\"entityOwner\":\"Ingrid Altamirano\",\"entityType\":\"BAN\",\"collectionStatus\":\"In-coll-In\",\"actionType\":\"rcid\",\"actionDescription\":\"Restore\",\"priority\":\"High\",\"dueDate\":\"2022-05-31\",\"status\":\"Request Assigned\",\"assignedTeam\":\"create team\",\"assignedAgent\":\"Jerry Bill\",\"workCategory\":\"TI Guatemala\",\"totalAr\":440000,\"totalOverDue\":3156},{\"actionId\":12662,\"entityId\":1,\"entityName\":\"CAN_AIR\",\"entityOwner\":\"Ayushi Deshmukh\",\"entityType\":\"CBUCID\",\"collectionStatus\":\"Fourth User45\",\"actionType\":\"entity\",\"actionDescription\":\"Suspension\",\"priority\":\"Medium\",\"dueDate\":\"2022-08-29\",\"status\":\"Request Assigned\",\"assignedTeam\":\"TEST\",\"assignedAgent\":\"Berry Jason\",\"workCategory\":\"IN\",\"totalAr\":4466770,\"totalOverDue\":358886},{\"actionId\":666662,\"entityId\":2,\"entityName\":\"PART-EN\",\"entityOwner\":\"Ingrid Altamirano\",\"entityType\":\"RCID\",\"collectionStatus\":\"In-collections\",\"actionType\":\"ban\",\"actionDescription\":\"Cease\",\"priority\":\"Low\",\"dueDate\":\"2023-06-04\",\"status\":\"Order fulfilled\",\"assignedTeam\":\"BC Team\",\"assignedAgent\":\"Johny 5677\",\"workCategory\":\"WLN PARTNER FR\",\"totalAr\":448860,\"totalOverDue\":3565353},{\"actionId\":61162,\"entityId\":16,\"entityName\":\"Air Canada\",\"entityOwner\":\"Franklin Dev\",\"entityType\":\"Entity\",\"collectionStatus\":\"In-collsT\",\"actionType\":\"entity\",\"actionDescription\":\"Restore\",\"priority\":\"High\",\"dueDate\":\"2022-05-10\",\"status\":\"Request Assigned\",\"assignedTeam\":\"TD OM CHECK\",\"assignedAgent\":\"David\",\"workCategory\":\"AR Excluded\",\"totalAr\":4400088,\"totalOverDue\":3561},{\"actionId\":6711162,\"entityId\":17,\"entityName\":\"Air123 Canada\",\"entityOwner\":\"Rachel Win12\",\"entityType\":\"BAN\",\"collectionStatus\":\"COLL-IN_IN234\",\"actionType\":\"cbucid\",\"actionDescription\":\"Suspension\",\"priority\":\"Medium\",\"dueDate\":\"2022-04-29\",\"status\":\"Order fulfilled\",\"assignedTeam\":\"HHH\",\"assignedAgent\":\"Johnyy 3450\",\"workCategory\":\"WLN LARGE FR\",\"totalAr\":40,\"totalOverDue\":6},{\"actionId\":6718182,\"entityId\":18,\"entityName\":\"Cana-air\",\"entityOwner\":\"Retest User\",\"entityType\":\"Entity\",\"collectionStatus\":\"sus-coll\",\"actionType\":\"rcid\",\"actionDescription\":\"Cease\",\"priority\":\"Low\",\"dueDate\":\"2022-08-08\",\"status\":\"Order fulfilled\",\"assignedTeam\":\"MESSAGE\",\"assignedAgent\":\"Poll Winy\",\"workCategory\":\"WLN SMALL FR\",\"totalAr\":4454640,\"totalOverDue\":66},{\"actionId\":6552,\"entityId\":19,\"entityName\":\"Canada-collection\",\"entityOwner\":\"Pearl Win\",\"entityType\":\"BAN\",\"collectionStatus\":\"In-collects\",\"actionType\":\"cbucid100\",\"actionDescription\":\"Restore\",\"priority\":\"High\",\"dueDate\":\"2022-10-28\",\"status\":\"Request Created\",\"assignedTeam\":\"Order fulfilled\",\"assignedAgent\":\"Ronyy\",\"workCategory\":\"IN\",\"totalAr\":44110,\"totalOverDue\":3561111},{\"actionId\":67662,\"entityId\":20,\"entityName\":\"Air Canada\",\"entityOwner\":\"Ayushi Deshmukh\",\"entityType\":\"CBUCID\",\"collectionStatus\":\"In-collection\",\"actionType\":\"cbucid\",\"actionDescription\":\"Suspension\",\"priority\":\"Medium\",\"dueDate\":\"2022-08-18\",\"status\":\"Request\",\"assignedTeam\":\"HHH\",\"assignedAgent\":\"Sandy\",\"workCategory\":\"IN\",\"totalAr\":1240,\"totalOverDue\":39996},{\"actionId\":611162,\"entityId\":21,\"entityName\":\"Comma-in\",\"entityOwner\":\"Ayushi Deshmukh\",\"entityType\":\"CBUCID\",\"collectionStatus\":\"In-collection\",\"actionType\":\"cbucid\",\"actionDescription\":\"Suspension\",\"priority\":\"Medium\",\"dueDate\":\"2022-12-19\",\"status\":\"Request Assigned\",\"assignedTeam\":\"AR Excluded\",\"assignedAgent\":\"John 345\",\"workCategory\":\"IN\",\"totalAr\":448880,\"totalOverDue\":11356},{\"actionId\":67002,\"entityId\":22,\"entityName\":\"GIF Canada\",\"entityOwner\":\"Rachel Win12\",\"entityType\":\"RCID\",\"collectionStatus\":\"In-collection\",\"actionType\":\"cbucid\",\"actionDescription\":\"Suspension\",\"priority\":\"Medium\",\"dueDate\":\"2022-08-29\",\"status\":\"Request Created\",\"assignedTeam\":\"TD OM CHECK\",\"assignedAgent\":\"John 345\",\"workCategory\":\"IN\",\"totalAr\":440,\"totalOverDue\":356},{\"actionId\":67779,\"entityId\":23,\"entityName\":\"Air xyz\",\"entityOwner\":\"Rachel Win12\",\"entityType\":\"BAN\",\"collectionStatus\":\"In-COLLECT-req\",\"actionType\":\"ban\",\"actionDescription\":\"Cease\",\"priority\":\"Low\",\"dueDate\":\"2022-07-30\",\"status\":\"Request Created\",\"assignedTeam\":\"OM\",\"assignedAgent\":\"BOBY CHARY\",\"workCategory\":\"AR Excludeded\",\"totalAr\":4199,\"totalOverDue\":677356},{\"actionId\":67662,\"entityId\":24,\"entityName\":\"Air Canada\",\"entityOwner\":\"Ayushi Deshmukh\",\"entityType\":\"CBUCID\",\"collectionStatus\":\"In-collection\",\"actionType\":\"cbucid\",\"actionDescription\":\"Suspension\",\"priority\":\"Medium\",\"dueDate\":\"2022-08-29\",\"status\":\"Request Created\",\"assignedTeam\":\"TD OM CHECK\",\"assignedAgent\":\"John 345\",\"workCategory\":\"IN\",\"totalAr\":440,\"totalOverDue\":356},{\"actionId\":5645,\"entityId\":25,\"entityName\":\"Entity-in\",\"entityOwner\":\"Franklin Dev\",\"entityType\":\"BAN\",\"collectionStatus\":\"In-collection\",\"actionType\":\"entity\",\"actionDescription\":\"Suspension\",\"priority\":\"Medium\",\"dueDate\":\"2022-08-18\",\"status\":\"Request Created\",\"assignedTeam\":\"TD OM CHECK\",\"assignedAgent\":\"John 345\",\"workCategory\":\"IN\",\"totalAr\":440,\"totalOverDue\":356},{\"actionId\":67662,\"entityId\":1,\"entityName\":\"Air Canada\",\"entityOwner\":\"WILL\",\"entityType\":\"CBUCID\",\"collectionStatus\":\"In-collection\",\"actionType\":\"cbucid\",\"actionDescription\":\"Suspension\",\"priority\":\"Medium\",\"dueDate\":\"2022-08-29\",\"status\":\"Request Created\",\"assignedTeam\":\"create team\",\"assignedAgent\":\"John 345\",\"workCategory\":\"WLN LARGE FR\",\"totalAr\":440,\"totalOverDue\":356},{\"actionId\":676699992,\"entityId\":2,\"entityName\":\"Air Canaday\",\"entityOwner\":\"Ross Bill\",\"entityType\":\"CBUCID\",\"collectionStatus\":\"In-collection\",\"actionType\":\"cbucid\",\"actionDescription\":\"Restore\",\"priority\":\"Medium\",\"dueDate\":\"2022-12-07\",\"status\":\"Request Created\",\"assignedTeam\":\"TD OM CHECK\",\"assignedAgent\":\"ABC XYZ\",\"workCategory\":\"IN\",\"totalAr\":4770,\"totalOverDue\":3561},{\"actionId\":600002,\"entityId\":16,\"entityName\":\"Canada-air0\",\"entityOwner\":\"Retest User\",\"entityType\":\"CBUCID\",\"collectionStatus\":\"In-collection\",\"actionType\":\"cbucid\",\"actionDescription\":\"Suspension\",\"priority\":\"Medium\",\"dueDate\":\"2022-08-29\",\"status\":\"Request Created\",\"assignedTeam\":\"TD OM CHECK\",\"assignedAgent\":\"Poll BILLL\",\"workCategory\":\"IN\",\"totalAr\":44022,\"totalOverDue\":356222},{\"actionId\":1112,\"entityId\":17,\"entityName\":\"Air1123 Canada\",\"entityOwner\":\"Ayushi Deshmukh\",\"entityType\":\"CBUCID\",\"collectionStatus\":\"In-collection\",\"actionType\":\"cbucid\",\"actionDescription\":\"Suspension\",\"priority\":\"low\",\"dueDate\":\"2022-08-29\",\"status\":\"Request Created\",\"assignedTeam\":\"OM CHECK\",\"assignedAgent\":\"John 345\",\"workCategory\":\"IN\",\"totalAr\":44880,\"totalOverDue\":3561345},{\"actionId\":670062,\"entityId\":18,\"entityName\":\"Air Canada\",\"entityOwner\":\"Ayushi\",\"entityType\":\"CBUCID\",\"collectionStatus\":\"In-collection\",\"actionType\":\"cbucid\",\"actionDescription\":\"Cease\",\"priority\":\"Medium\",\"dueDate\":\"2022-03-19\",\"status\":\"Request Assigned\",\"assignedTeam\":\"TD OM CHECK\",\"assignedAgent\":\"John 345\",\"workCategory\":\"IN\",\"totalAr\":498940,\"totalOverDue\":356},{\"actionId\":6112,\"entityId\":19,\"entityName\":\"REQ AIR\",\"entityOwner\":\"Poll Win\",\"entityType\":\"CBUCID\",\"collectionStatus\":\"Out-collection\",\"actionType\":\"cbucid\",\"actionDescription\":\"Suspension\",\"priority\":\"High\",\"dueDate\":\"2022-08-29\",\"status\":\"Request Created\",\"assignedTeam\":\"HHH\",\"assignedAgent\":\"John 345\",\"workCategory\":\"WLN PARTNER FR\",\"totalAr\":4490,\"totalOverDue\":35600000}]",
    		        objectMapper.getTypeFactory().constructCollectionType(List.class, TeamsActionViewResponse.class));
    	}else {
    		logger.info("::::::::Calling  actionViewByTeam endpoint call ::::::::");
    		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(entityDataEndPointUrl + URIConstant.ApiMapping.ENTITY_DETAILS)
    			                          .queryParam("assignedAgent", assignedAgent)
    			                          .queryParam("assignedTeam", assignedTeam)
    									  .queryParam("entityOwner", entityOwner);
    		String responseStr = telusAPIConnectivityService.executeTelusAPI(null, builder.toUriString(), HttpMethod.GET, entitySvcAuthScope);
    		 logger.info("::::::::TeamsActionViewResponse endpoint call Success ::::::::");
    		 logger.info("Response payload for getActionViewByTeam---"+ responseStr);
    		 List<TeamsActionViewResponse> teamsActionViewResponseList = new ArrayList<TeamsActionViewResponse>();
    		 if(!StringUtils.isEmpty(responseStr)) {
    			 teamsActionViewResponseList = objectMapper.readValue(responseStr,
      				objectMapper.getTypeFactory().constructCollectionType(List.class, TeamsActionViewResponse.class));
    		 }
    		 return teamsActionViewResponseList;
    }
    }
    
    
    
    
   private List<LookUpResponseWithTeamName> setTeamNameBasedOnEmplId(EntitySearchResponseArray entitySearchResponseArray)
     	{
 
	    	List<LookUpResponseWithTeamName> lookUpResponseWithTeamNamesList=new ArrayList<>();
	    	if(!entitySearchResponseArray.isEmpty())
	    	{
		    	for(EntitySearchResponse entitySearchResponse:entitySearchResponseArray)
		    	{

			    	LookUpResponseWithTeamName lookUpResponseWithTeamName=new LookUpResponseWithTeamName();
				    lookUpResponseWithTeamName.setEntityId(entitySearchResponse.getEntityId());
				    lookUpResponseWithTeamName.setEntityName(entitySearchResponse.getEntityName());
				    lookUpResponseWithTeamName.setEntityType(entitySearchResponse.getEntityType());
				    lookUpResponseWithTeamName.setCbucId(entitySearchResponse.getCbucId());
				    lookUpResponseWithTeamName.setRcId(entitySearchResponse.getRcId());
				    lookUpResponseWithTeamName.setBanId(entitySearchResponse.getBanId());
				    lookUpResponseWithTeamName.setBanName(entitySearchResponse.getBanName());
				    lookUpResponseWithTeamName.setBillingSystem(entitySearchResponse.getBillingSystem());

    				if(entitySearchResponse.getEntityOwner()!=null)
	    			{
		    			lookUpResponseWithTeamName.setTeamName(commonUtilityService.getTeamIdUsingEmpId(entitySearchResponse.getEntityOwner()));
			    	}

				    lookUpResponseWithTeamName.setEntityOwner(entitySearchResponse.getEntityOwner());
				    lookUpResponseWithTeamName.setDntlFlag(entitySearchResponse.isDntlFlag());

				    lookUpResponseWithTeamNamesList.add(lookUpResponseWithTeamName);    
			}
		}

		return lookUpResponseWithTeamNamesList;
	}
}