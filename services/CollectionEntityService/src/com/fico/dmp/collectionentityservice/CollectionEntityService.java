/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.collectionentityservice;
import com.fico.telus.utility.URIConstant;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;

import org.springframework.beans.factory.annotation.Autowired;


import javax.servlet.http.HttpServletRequest;
import javax.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;

import org.springframework.beans.factory.annotation.Autowired;


import com.wavemaker.runtime.security.SecurityService;
import com.wavemaker.runtime.service.annotations.ExposeToClient;
import com.wavemaker.runtime.service.annotations.HideFromClient;
import com.wavemaker.tools.api.core.annotations.WMAccessVisibility;
import com.wavemaker.tools.api.core.models.AccessSpecifier;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;

import com.wordnik.swagger.annotations.ApiOperation;

import java.util.List;
import io.swagger.client.model.CollectionPaymentArrangement;
import io.swagger.client.model.CollectionPaymentArrangementCreate;
import io.swagger.client.model.CollectionPaymentArrangementUpdate;
import io.swagger.client.model.CollectionBillingAccountRef;
import io.swagger.client.model.CollectionBillingAccountRefCreate;
import io.swagger.client.model.CollectionBillingAccountRefUpdate;
import io.swagger.client.model.CollectionEntityArray;
import io.swagger.client.model.CollectionEntity;
import io.swagger.client.model.CollectionEntityCreate; 
import io.swagger.client.model.CollectionEntityUpdate;
import io.swagger.client.model.CollectionContactArray;
import io.swagger.client.model.CollectionContact;
import io.swagger.client.model.CollectionContactCreate; 
import io.swagger.client.model.CollectionContactUpdate; 
//import io.swagger.client.model.CollectionSuppression;
//import io.swagger.client.model.CollectionSuppressionCreate;
//import io.swagger.client.model.CollectionSuppressionUpdate;
//import io.swagger.client.model.CollectionSuppressionArray;
import io.swagger.client.model.CollectionDispute;
import io.swagger.client.model.CollectionDisputeUpdate;
import io.swagger.client.model.CollectionDisputeCreate;
import io.swagger.client.model.EntityRef;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.fico.core.util.ObjectMapperConfig;
import com.fico.telus.service.PARRService;
import org.springframework.stereotype.Service;
import com.fico.pscomponent.util.PropertiesUtil;
import com.fico.telus.service.TelusAPIConnectivityService;
import org.springframework.web.util.UriComponentsBuilder;


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
public class CollectionEntityService {

    private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(CollectionEntityService.class.getName());

     private final ObjectMapper objectMapper = new ObjectMapperConfig().customObjectMapper();
     
    @Autowired
    private SecurityService securityService;

	private static final String IS_ENTITYSVC_STUB_ENABLED = "IS_ENTITYSVC_STUB_ENABLED";

	private static final String ENTITYSVC_ENDPOINT_URL = "ENTITYSVC_ENDPOINT_URL";
	
	private static final String ENTITYSVC_ENDPOINT_SCOPE = "ENTITYSVC_ENDPOINT_SCOPE";

	
	@Autowired
	private PropertiesUtil propertiesUtil;

	@Autowired
	private TelusAPIConnectivityService telusAPIConnectivityService;

    @Autowired
    private PARRService parrService;

	private boolean isStubEnabled;

	private String parrEndPointUrl;
	
	private String entitySvcAuthScope;


	@PostConstruct
	public void init() {

		this.isStubEnabled = Boolean.valueOf(propertyValueFrom(IS_ENTITYSVC_STUB_ENABLED, "false"));
		this.parrEndPointUrl = propertyValueFrom(ENTITYSVC_ENDPOINT_URL, URIConstant.COLLECTION_ENTITY_SERVICE_URL);
		this.entitySvcAuthScope = propertyValueFrom(ENTITYSVC_ENDPOINT_SCOPE, "3161");

	}

	private String propertyValueFrom(String propertyName, String defaulValueIfNull) {
		String propertyValue = propertiesUtil.getPropertyValue(propertyName);
		if (propertyValue == null) {
			logger.info("property value is null, using default");
			propertyValue = defaulValueIfNull;
		}
		return propertyValue;
	}
	
    //Billing Account Ref    
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
     public List<CollectionBillingAccountRef> getBillingAccountRef(String fields,Integer offset, Integer limit, String ban, Integer entityId, Integer id) throws Exception  {

        return objectMapper.readValue("[{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccount\":{\"id\":\"12345\",\"accountGroupId\":1,\"accountType\":\"B\",\"accountSubType\":\"I\",\"name\":\"A Company\",\"state\":\"C\",\"stateDate\":\"2022-12-12\",\"@type\":\"BillingAccount\"},\"billingSystemId\":10,\"billingSystemName\":\"CES9\",\"ceaseReason\":null,\"closingCycle\":6,\"collectionStatus\":\"PRECOLL\",\"collectionStatusDate\":\"2022-12-12\",\"fraudIndicator\":false,\"involuntaryCeasedIndicator\":false,\"lineOfBusiness\":\"WLN\",\"previousCollectionEntity\":{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"@referredType\":\"CollectionEntity\",\"@type\":\"EntityRef\"},\"suppressionReason\":null,\"suppressionValidFor\":null,\"writeOffIndicator\":false,\"@type\":\"CollectionBillingAccountRef\"}]",
         objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionBillingAccountRef.class));
         
    }
    
    // @ApiOperation(value = "Returns the AccessLog instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionBillingAccountRef getBillingAccountRef(@PathVariable("id") Integer id, String fields) throws Exception  {

       return objectMapper.readValue("{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccount\":{\"id\":\"12345\",\"accountGroupId\":1,\"accountType\":\"B\",\"accountSubType\":\"I\",\"name\":\"A Company\",\"state\":\"O\",\"stateDate\":\"2022-12-12\",\"@type\":\"BillingAccount\"},\"billingSystemId\":10,\"billingSystemName\":\"CES9\",\"closingCycle\":6,\"collectionStatus\":\"PRECOLL\",\"collectionStatusDate\":\"2022-12-12\",\"fraudIndicator\":false,\"involuntaryCeasedIndicator\":false,\"lineOfBusiness\":\"WLN\",\"writeOffIndicator\":false,\"@type\":\"CollectionBillingAccountRef\"}",CollectionBillingAccountRef.class);
    }    
    
    public CollectionBillingAccountRefCreate  addBillingAccountRef( CollectionBillingAccountRefCreate   collectionBillingAccountRefCreate ) throws Exception  {
    
        return collectionBillingAccountRefCreate ;
    }
    
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PATCH)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionBillingAccountRefUpdate updateBillingAccountRef(@PathVariable("id") Integer id, CollectionBillingAccountRefUpdate  collectionBillingAccountRefUpdate) throws Exception {
        return collectionBillingAccountRefUpdate;
    }

//Collection Entity    

    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @RequestMapping(value =  URIConstant.ApiMapping.GET_ENTITY, method = {RequestMethod.GET})
    public List<CollectionEntity> getCollectionEntity(String fields,Integer offset, Integer limit,  String ban, String rcid,String cbucid,String entityId,String agentId,String workCategory,String sortBy) throws Exception  {
        
        if (isStubEnabled) {
            return objectMapper.readValue("[{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"agentId\":\"agent1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccountRefMaps\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccountRef\":{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"@referredType\":\"CollectionBillingAccountRef\",\"@type\":\"EntityRef\"},\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.00Z\"},\"@type\":\"CollectionEntityBillingAccountMap\"}],\"characteristics\":[{\"name\":\"name\",\"value\":\"value\",\"@type\":\"Characteristic\"}],\"collectionEpisodes\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"endReason\":null,\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.00Z\"},\"@type\":\"CollectionEpisode\"}],\"collectionStatus\":\"INCOLL\",\"collectionStatusDateTime\":\"2023-01-01T09:00:00.00Z\",\"contacts\":[{\"id\":1,\"href\":\"BASE_URL/contact/1\",\"@referredType\":\"CollectionContact\",\"@type\":\"EntityRef\"}],\"customerRisk\":\"H\",\"customerRiskId\":59,\"customerValue\":\"L\",\"customerValueId\":10,\"delinquentCycle\":6,\"engagedCustomerParty\":{\"cbucid\":\"123\",\"cbuCode\":\"cbuCode123\",\"cbuName\":\"cbu-name-123\",\"organizationType\":\"CBU\",\"@type\":\"Organization\"},\"engagedRegionalCustomerParty\":{\"organizationType\":\"RC\",\"rcid\":\"rc-12345\",\"rcName\":\"rc-name-12345\",\"portfolioCategory\":\"PUBLIC\",\"portfolioSubCategory\":\"PUBLIC LARGE\",\"subMarketSegment\":\"string\",\"@type\":\"Organization\"},\"exclusionIndicatorCharacter\":\"string\",\"exclusionIndicatorInteger\":1,\"lineOfBusiness\":\"WLN\",\"manualTreatmentIndicator\":false,\"name\":\"Entity 1\",\"notTouchListIndicator\":false,\"paymentArrangement\":{\"id\":1,\"href\":\"BASE_URL/paymentArrangement/1\",\"@referredType\":\"CollectionPaymentArrangement\",\"@type\":\"EntityRef\"},\"randomDigit\":1,\"relatedEntity\":{\"id\":\"1\",\"role\":\"RCID\",\"@type\":\"RelatedEntity\"},\"tenure\":29,\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.00Z\"},\"workCategory\":\"string\",\"@type\":\"CollectionEntity\"}]",
            objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionEntity.class));
        }else {
         			logger.info("::::::::Calling  entity endpoint call ::::::::");

                String responseStr = telusAPIConnectivityService.executeTelusAPI(null,this.parrEndPointUrl + URIConstant.ApiMapping.GET_ENTITY + "?id=" + entityId, "GET", entitySvcAuthScope);
                	logger.info("::::::::Entity endpoint call success ::::::::");
                	logger.info("Resoinse---"+ responseStr);
                List<CollectionEntity> collectionEntityList = objectMapper.readValue(responseStr,
					objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionEntity.class));
								logger.info(":::::::: Completed Calling  entity endpoint call ::::::::");
                return collectionEntityList;
        }
    }    
          // @ApiOperation(value = "Returns the AccessLog instance associated with the given id.")
        // @RequestMapping(value = "/entity", method = {RequestMethod.GET})
        @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
        @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
        public CollectionEntity getCollectionEntity(@PathVariable("id") Integer id, Boolean history) throws Exception  {

       return objectMapper.readValue("{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"agentId\":\"agent1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccountRefMaps\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccountRef\":{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"@referredType\":\"CollectionBillingAccountRef\",\"@type\":\"EntityRef\"},\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.00Z\"},\"@type\":\"CollectionEntityBillingAccountMap\"}],\"characteristics\":[{\"name\":\"name\",\"value\":\"value\",\"@type\":\"Characteristic\"}],\"collectionStatus\":\"PRECOLL\",\"collectionStatusDateTime\":\"2023-01-01T09:00:00.00Z\",\"customerRisk\":\"H\",\"customerRiskId\":59,\"customerValue\":\"L\",\"customerValueId\":10,\"delinquentCycle\":6,\"engagedCustomerParty\":{\"cbucid\":\"123\",\"cbuCode\":\"cbuCode123\",\"cbuName\":\"cbu-name-123\",\"organizationType\":\"CBU\",\"@type\":\"Organization\"},\"engagedRegionalCustomerParty\":{\"organizationType\":\"RC\",\"rcid\":\"rc-12345\",\"rcName\":\"rc-name-12345\",\"portfolioCategory\":\"PUBLIC\",\"portfolioSubCategory\":\"PUBLIC LARGE\",\"subMarketSegment\":\"string\",\"@type\":\"Organization\"},\"exclusionIndicatorCharacter\":\"string\",\"exclusionIndicatorInteger\":1,\"lineOfBusiness\":\"WLN\",\"manualTreatmentIndicator\":false,\"name\":\"Entity 1\",\"notTouchListIndicator\":false,\"randomDigit\":1,\"relatedEntity\":{\"id\":\"1\",\"role\":\"RCID\",\"@type\":\"RelatedEntity\"},\"tenure\":29,\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.00Z\"},\"workCategory\":\"string\",\"@type\":\"CollectionEntity\"}",CollectionEntity.class);
    }    
    
    public CollectionEntityCreate   addCollectionEntity( CollectionEntityCreate    collectionEntityCreate  ) throws Exception  {
    
        return collectionEntityCreate  ;
    }
    
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PATCH)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionEntityUpdate  updateCollectionEntity(@PathVariable("id") Integer id, CollectionEntityUpdate   collectionEntityUpdate ) throws Exception {
        return collectionEntityUpdate ;
    }



///contact 

    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public List<CollectionContact> getContact(String fields, Integer offset, Integer limit, Integer entityId) throws Exception  {

        return objectMapper.readValue("[{\"id\":1,\"href\":\"BASE_URL/contact/1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"comment\":\"This is collection contact comment 1\",\"email\":\"john.doe@telus.com\",\"collectionEntity\":{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"@referredType\":\"CollectionEntity\",\"@type\":\"EntityRef\"},\"faxNumnber\":\"9059979999\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"mobilePhoneNumber\":\"5149979999\",\"notificationIndicator\":true,\"telusContactIndicator\":true,\"title\":\"Mr.\",\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.00Z\"},\"workPhoneNumber\":\"9059979797\",\"@type\":\"CollectionContact\"}]",
        objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionContact.class));
    }    
          // @ApiOperation(value = "Returns the AccessLog instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionContact getContact(@PathVariable("id") Integer id, String fields) throws Exception  {

       return objectMapper.readValue("{\"id\":1,\"href\":\"BASE_URL/contact/1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"comment\":\"This is collection contact comment 1\",\"email\":\"john.doe@telus.com\",\"collectionEntity\":{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"@referredType\":\"CollectionEntity\",\"@type\":\"EntityRef\"},\"faxNumnber\":\"9059979999\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"mobilePhoneNumber\":\"5149979999\",\"notificationIndicator\":true,\"telusContactIndicator\":true,\"title\":\"Mr.\",\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.00Z\"},\"workPhoneNumber\":\"9059979797\",\"@type\":\"CollectionContact\"}",CollectionContact.class);
    }    
    
    public CollectionContactCreate   addContact( CollectionContactCreate    collectionContactCreate  ) throws Exception  {
    
        return collectionContactCreate  ;
    }
    
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PATCH)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionContactUpdate  updateContact(@PathVariable("id") Integer id, CollectionContactUpdate   collectionContactUpdate ) throws Exception {
        return collectionContactUpdate ;
    }
    //PAAR
  @RequestMapping(value = URIConstant.ApiMapping.GET_PARR, method = {RequestMethod.GET})
    public List<CollectionPaymentArrangement> getPaymentArrangements(String fields, Integer offset, Integer limit, String agentId, String entityId, String entityRisk, String evaluation, String status, String createdBy, String createdFrom, String createdTo) throws Exception  {
    	
        // return objectMapper.readValue("[{\"id\":1,\"href\":\"BASE_URL/paymentArrangement/1\",\"allBillingAccountIncludedIndicator\":true,\"amount\":100.0,\"billingAccountMaps\":[{\"id\":1,\"billingAccountRef\":{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"billingAccount\":{\"id\":\"12345\",\"accountGroupId\":1,\"accountType\":\"B\",\"accountSubType\":\"I\",\"name\":\"A Company\",\"state\":\"O\"},\"billingSystemId\":10,\"billingSystemName\":\"CES9\",\"closingCycle\":6,\"collectionStatus\":\"INCOLL\",\"fraudIndicator\":false,\"involuntaryCeasedIndicator\":false,\"writeOffIndicator\":false},\"validityIndicator\":true}],\"collectionEntity\":{\"id\":1,\"href\":\"BASE_URL/entity/1\"},\"comment\":\"string\",\"evaluationResult\":\"string\",\"expectedPaymentAmountToDate\":100.0,\"installments\":[{\"id\":1,\"amount\":100.0,\"evaluationResult\":\"string\",\"sequenceId\":1,\"validityIndicator\":true}],\"receivedPaymentAmountToDate\":0.0,\"recurrence\":\"MONTHLY\",\"statuses\":[{\"id\":1,\"reason\":\"string\",\"status\":\"string\"}]}]",
		/*
		 * return objectMapper.
		 * readValue("[{\"id\":1,\"href\":\"BASE_URL/paymentArrangement/1\",\"allBillingAccountIncludedIndicator\":true,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccountMaps\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccountRef\":{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccount\":{\"id\":\"12345\",\"accountGroupId\":1,\"accountType\":\"B\",\"accountSubType\":\"I\",\"name\":\"A Company\",\"state\":\"O\",\"@type\":\"BillingAccount\"},\"billingSystemId\":10,\"billingSystemName\":\"CES9\",\"closingCycle\":6,\"collectionStatus\":\"INCOLL\",\"fraudIndicator\":false,\"involuntaryCeasedIndicator\":false,\"writeOffIndicator\":false,\"@type\":\"CollectionBillingAccountRef\"},\"validityIndicator\":true,\"@type\":\"CollectionPaymentArrangementBillingAccountMap\"}],\"collectionEntity\":{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"@referredType\":\"CollectionEntity\",\"@type\":\"EntityRef\"},\"comment\":\"string\",\"evaluationResult\":\"string\",\"expectedPaymentAmountToDate\":100.0,\"installments\":[{\"id\":1,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"date\":\"2023-01-01\",\"evaluationResult\":\"string\",\"evaluationTimestamp\":\"2023-01-01T09:00:00.001Z\",\"sequenceId\":1,\"validityIndicator\":true,\"@type\":\"CollectionPaymentInstallment\"}],\"receivedPaymentAmountToDate\":0.0,\"recurrence\":\"MONTHLY\",\"statuses\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"reason\":\"string\",\"status\":\"string\",\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.001Z\"},\"@type\":\"CollectionPaymentArrangementStatus\"}],\"@type\":\"CollectionPaymentArrangement\"}]"
		 * , objectMapper.getTypeFactory().constructCollectionType(List.class,
		 * CollectionPaymentArrangement.class));
		 */
        // return new Object(); 
         UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(parrEndPointUrl+URIConstant.ApiMapping.GET_PARR)
              .queryParam("id=in:", entityId);
              // .queryParam("entityRisk",entityRisk);
    	return parrService.getPaymentArrangements(entityId,builder.toUriString());
    }
        // @ApiOperation(value = "Returns the AccessLog instance associated with the given id.")
        @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
        @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
        public CollectionPaymentArrangement getPaymentArrangementById(@PathVariable("id") Integer id, Boolean history) throws Exception  {

        // return objectMapper.readValue("[{\"id\":1,\"href\":\"BASE_URL/paymentArrangement/1\",\"allBillingAccountIncludedIndicator\":true,\"amount\":100.0,\"billingAccountMaps\":[{\"id\":1,\"billingAccountRef\":{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"billingAccount\":{\"id\":\"12345\",\"accountGroupId\":1,\"accountType\":\"B\",\"accountSubType\":\"I\",\"name\":\"A Company\",\"state\":\"O\"},\"billingSystemId\":10,\"billingSystemName\":\"CES9\",\"closingCycle\":6,\"collectionStatus\":\"INCOLL\",\"fraudIndicator\":false,\"involuntaryCeasedIndicator\":false,\"writeOffIndicator\":false},\"validityIndicator\":true}],\"collectionEntity\":{\"id\":1,\"href\":\"BASE_URL/entity/1\"},\"comment\":\"string\",\"evaluationResult\":\"string\",\"expectedPaymentAmountToDate\":100.0,\"installments\":[{\"id\":1,\"amount\":100.0,\"evaluationResult\":\"string\",\"sequenceId\":1,\"validityIndicator\":true}],\"receivedPaymentAmountToDate\":0.0,\"recurrence\":\"MONTHLY\",\"statuses\":[{\"id\":1,\"reason\":\"string\",\"status\":\"string\"}]}]",
       // return objectMapper.readValue("{\"id\":1,\"href\":\"BASE_URL/paymentArrangement/1\",\"allBillingAccountIncludedIndicator\":true,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccountMaps\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccountRef\":{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccount\":{\"id\":\"12345\",\"accountGroupId\":1,\"accountType\":\"B\",\"accountSubType\":\"I\",\"name\":\"A Company\",\"state\":\"O\",\"@type\":\"BillingAccount\"},\"billingSystemId\":10,\"billingSystemName\":\"CES9\",\"closingCycle\":6,\"collectionStatus\":\"INCOLL\",\"fraudIndicator\":false,\"involuntaryCeasedIndicator\":false,\"writeOffIndicator\":false,\"@type\":\"CollectionBillingAccountRef\"},\"validityIndicator\":true,\"@type\":\"CollectionPaymentArrangementBillingAccountMap\"}],\"collectionEntity\":{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"@referredType\":\"CollectionEntity\",\"@type\":\"EntityRef\"},\"comment\":\"string\",\"evaluationResult\":\"string\",\"expectedPaymentAmountToDate\":100.0,\"installments\":[{\"id\":1,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"date\":\"2023-01-01\",\"evaluationResult\":\"string\",\"evaluationTimestamp\":\"2023-01-01T09:00:00.001Z\",\"sequenceId\":1,\"validityIndicator\":true,\"@type\":\"CollectionPaymentInstallment\"}],\"receivedPaymentAmountToDate\":0.0,\"recurrence\":\"MONTHLY\",\"statuses\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"reason\":\"string\",\"status\":\"string\",\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.001Z\"},\"@type\":\"CollectionPaymentArrangementStatus\"}],\"@type\":\"CollectionPaymentArrangement\"}",CollectionPaymentArrangement.class);
        return parrService.getPaymentArrangement(id, history);
        	// return new Object(); 
    }    
        
    public CollectionPaymentArrangement addPaymentArrangement(CollectionPaymentArrangementCreate  collectionPaymentArrangementCreate, String entityId) throws Exception  {
    
		/*
		 * logger.info(":::::::::::In add Parr, parr amount::::::::::::::::::" +
		 * collectionPaymentArrangementCreate.getAmount());
		 * logger.info(":::::::::::In add Parr, parr amount::::::::::::::::::" +
		 * collectionPaymentArrangementCreate.getInstallments()); if
		 * (collectionPaymentArrangementCreate.getInstallments() != null) {
		 * logger.info(":::::::::::In add Parr, parr amount::::::::::::::::::" +
		 * collectionPaymentArrangementCreate.getInstallments().get(0).getAmount()); }
		 */
        CollectionPaymentArrangement collectionPaymentArrangement = parrService.createPaymentArrangement(collectionPaymentArrangementCreate, entityId);
        return collectionPaymentArrangement;
        
    }
    
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionPaymentArrangement updatePaymentArrangement(@PathVariable("id") Integer id,CollectionPaymentArrangementUpdate collectionPaymentArrangementUpdate) throws Exception  {
        
		/*
		 * logger.info(":::::::::::In add Parr, parr amount::::::::::::::::::" +
		 * collectionPaymentArrangementCreate.getAmount()); if
		 * (collectionPaymentArrangementCreate.getInstallments() != null) {
		 * logger.info(":::::::::::In add Parr, parr amount::::::::::::::::::" +
		 * collectionPaymentArrangementCreate.getInstallments().get(0).getAmount()); }
		 */
        CollectionPaymentArrangement collectionPaymentArrangement = parrService.updatePaymentArrangement(collectionPaymentArrangementUpdate);
        return collectionPaymentArrangement;        
    }
    
    public CollectionPaymentArrangement updateParrStatus(Integer parrId, String status, String comments) throws Exception {
        
        CollectionPaymentArrangement collectionPaymentArrangement = parrService.updateParrStatus(parrId, status, comments);
        return collectionPaymentArrangement;
    }
    
//dispute

    @RequestMapping(value = "/dispute", method = {RequestMethod.GET})
    public List<CollectionDispute> getdispute(String fields,Integer offset, Integer limit, Integer banRefId, Boolean history) throws Exception  {
    	if (isStubEnabled) {
        return objectMapper.readValue("[{\"id\":1,\"href\":\"BASE_URL/dispute/1\",\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccountRef\":{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"@referredType\":\"CollectionBillingAccountRef\",\"@type\":\"EntityRef\"},\"billingAdjustmentRequestId\":\"string\",\"chargeType\":\"One-time charge\",\"comment\":\"Collection dispute comment 1\",\"customerEmail\":\"John.Snow@telus.com\",\"collectionExclusionIndicator\":false,\"disputePrime\":\"string\",\"disputeReason\":\"BILLED CHARGES (DEEMED) INCORRECT\",\"product\":\"Business Connect\",\"status\":\"OPEN\",\"statusDateTime\":\"2023-01-01T09:00:00.00Z\",\"statusReason\":\"string\",\"@type\":\"CollectionDispute\"}]",
        objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionDispute.class));
    	}else {
 			logger.info("::::::::Calling  entity endpoint call ::::::::");

            String responseStr = telusAPIConnectivityService.executeTelusAPI(null,this.parrEndPointUrl + URIConstant.ApiMapping.GET_DISPUTE, "GET", entitySvcAuthScope);
            	logger.info("::::::::Entity endpoint call success ::::::::");
            	logger.info("Resoinse---"+ responseStr);
            	List<CollectionDispute> collectionDisputeList = objectMapper.readValue(responseStr,
				objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionDispute.class));
							logger.info(":::::::: Completed Calling  entity endpoint call ::::::::");
            return collectionDisputeList;
    }
        // return new Object(); 
    }
    
    public CollectionDisputeCreate addDispute( CollectionDisputeCreate  collectionDispute) throws Exception  {

        return collectionDispute;
        
    }

    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionDispute getdisputeById(@PathVariable("id") Integer id, String fields, Boolean history) throws Exception  { 
        Boolean isDisputeStubEnabled = true;
    	if (isDisputeStubEnabled) {
    	return objectMapper.readValue("{\"id\":1,\"href\":\"BASE_URL/dispute/1\",\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccountRef\":{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"@referredType\":\"CollectionBillingAccountRef\",\"@type\":\"EntityRef\"},\"billingAdjustmentRequestId\":\"string\",\"chargeType\":\"One-time charge\",\"comment\":\"Collection dispute comment 1\",\"customerEmail\":\"John.Snow@telus.com\",\"collectionExclusionIndicator\":false,\"disputePrime\":\"string\",\"disputeReason\":\"BILLED CHARGES (DEEMED) INCORRECT\",\"product\":\"Business Connect\",\"status\":\"OPEN\",\"statusDateTime\":\"2023-01-01T09:00:00.00Z\",\"statusReason\":\"string\",\"@type\":\"CollectionDispute\"}",CollectionDispute.class);
    	}else {
    		logger.info("::::::::Calling  entity endpoint call ::::::::");
    		logger.info("Id in getdisputeById is...."+id);
            String responseStr = telusAPIConnectivityService.executeTelusAPI(null,this.parrEndPointUrl + URIConstant.ApiMapping.GET_DISPUTE + "/" + id, "GET", entitySvcAuthScope);
            	logger.info("::::::::Entity endpoint call success ::::::::");
            	logger.info("Resoinse---"+ responseStr);
            	CollectionDispute collectionDispute = objectMapper.readValue(responseStr,CollectionDispute.class);
							logger.info(":::::::: Completed Calling  entity endpoint call ::::::::");
            return collectionDispute;
    	}
    
    }    

    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PATCH)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionDisputeUpdate updateDispute(@PathVariable("id") Integer id,  CollectionDisputeUpdate  collectionDispute) throws Exception  {
    
        return collectionDispute;
        
    }

}