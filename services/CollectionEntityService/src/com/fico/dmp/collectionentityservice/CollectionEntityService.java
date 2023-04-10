/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.collectionentityservice;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;

import org.springframework.beans.factory.annotation.Autowired;


import javax.servlet.http.HttpServletRequest;

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
import io.swagger.client.model.CollectionSuppression;
import io.swagger.client.model.CollectionSuppressionCreate;
import io.swagger.client.model.CollectionSuppressionUpdate;
import io.swagger.client.model.CollectionSuppressionArray;
import io.swagger.client.model.CollectionDispute;
import io.swagger.client.model.CollectionDisputeUpdate;
import io.swagger.client.model.CollectionDisputeCreate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
public class CollectionEntityService {

    private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(CollectionEntityService.class.getName());

     private final ObjectMapper objectMapper = new ObjectMapperConfig().customObjectMapper();
     
    @Autowired
    private SecurityService securityService;

    //Billing Account Ref    
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public List<CollectionBillingAccountRef> getBillingAccountRef(String ban, Boolean history) throws Exception  {

        return objectMapper.readValue("[{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"auditInfo\":{\"createdBy\":\"9\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"9\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\"},\"billingAccount\":{\"id\":\"12345\",\"accountGroupId\":1,\"accountType\":\"B\",\"accountSubType\":\"I\",\"name\":\"A Company\",\"state\":\"O\"},\"billingSystemId\":10,\"billingSystemName\":\"CES9\",\"closingCycle\":6,\"collectionStatus\":\"PRECOLL\",\"fraudIndicator\":false,\"involuntaryCeasedIndicator\":false,\"writeOffIndicator\":false},{\"id\":2,\"href\":\"BASE_URL/billingAccountRef/1\",\"auditInfo\":{\"createdBy\":\"9\",\"createdDateTime\":\"2022-05-05T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"9\",\"lastUpdatedDateTime\":\"2022-05-05T09:00:00.00Z\"},\"billingAccount\":{\"id\":\"12345\",\"accountGroupId\":1,\"accountType\":\"B\",\"accountSubType\":\"I\",\"name\":\"B Company\",\"state\":\"O\"},\"billingSystemId\":10,\"billingSystemName\":\"CES9\",\"closingCycle\":7,\"collectionStatus\":\"PRECOLL\",\"fraudIndicator\":false,\"involuntaryCeasedIndicator\":false,\"writeOffIndicator\":false},{\"id\":3,\"href\":\"BASE_URL/billingAccountRef/1\",\"auditInfo\":{\"createdBy\":\"9\",\"createdDateTime\":\"2021-11-04T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"9\",\"lastUpdatedDateTime\":\"2021-11-04T09:00:00.00Z\"},\"billingAccount\":{\"id\":\"12345\",\"accountGroupId\":1,\"accountType\":\"B\",\"accountSubType\":\"I\",\"name\":\"C Company\",\"state\":\"O\"},\"billingSystemId\":10,\"billingSystemName\":\"CES9\",\"closingCycle\":8,\"collectionStatus\":\"PRECOLL\",\"fraudIndicator\":false,\"involuntaryCeasedIndicator\":false,\"writeOffIndicator\":false},{\"id\":4,\"href\":\"BASE_URL/billingAccountRef/1\",\"auditInfo\":{\"createdBy\":\"9\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"9\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\"},\"billingAccount\":{\"id\":\"12345\",\"accountGroupId\":1,\"accountType\":\"B\",\"accountSubType\":\"I\",\"name\":\"D Company\",\"state\":\"O\"},\"billingSystemId\":10,\"billingSystemName\":\"CES9\",\"closingCycle\":6,\"collectionStatus\":\"PRECOLL\",\"fraudIndicator\":false,\"involuntaryCeasedIndicator\":false,\"writeOffIndicator\":false},{\"id\":5,\"href\":\"BASE_URL/billingAccountRef/1\",\"auditInfo\":{\"createdBy\":\"9\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"9\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\"},\"billingAccount\":{\"id\":\"12345\",\"accountGroupId\":1,\"accountType\":\"B\",\"accountSubType\":\"I\",\"name\":\"E Company\",\"state\":\"O\"},\"billingSystemId\":10,\"billingSystemName\":\"CES9\",\"closingCycle\":9,\"collectionStatus\":\"PRECOLL\",\"fraudIndicator\":false,\"involuntaryCeasedIndicator\":false,\"writeOffIndicator\":false},{\"id\":6,\"href\":\"BASE_URL/billingAccountRef/1\",\"auditInfo\":{\"createdBy\":\"9\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"9\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\"},\"billingAccount\":{\"id\":\"12345\",\"accountGroupId\":1,\"accountType\":\"B\",\"accountSubType\":\"I\",\"name\":\"F Company\",\"state\":\"O\"},\"billingSystemId\":10,\"billingSystemName\":\"CES9\",\"closingCycle\":10,\"collectionStatus\":\"PRECOLL\",\"fraudIndicator\":false,\"involuntaryCeasedIndicator\":false,\"writeOffIndicator\":false}]",
        objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionBillingAccountRef.class));
    }    
          // @ApiOperation(value = "Returns the AccessLog instance associated with the given id.")
        @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
        @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
        public CollectionBillingAccountRef getBillingAccountRef(@PathVariable("id") Integer id, Boolean history) throws Exception  {

       return objectMapper.readValue("[{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"auditInfo\":{\"createdBy\":\"9\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"9\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\"},\"billingAccount\":{\"id\":\"12345\",\"accountGroupId\":1,\"accountType\":\"B\",\"accountSubType\":\"I\",\"name\":\"A Company\",\"state\":\"O\"},\"billingSystemId\":10,\"billingSystemName\":\"CES9\",\"closingCycle\":6,\"collectionStatus\":\"PRECOLL\",\"fraudIndicator\":false,\"involuntaryCeasedIndicator\":false,\"writeOffIndicator\":false}]",CollectionBillingAccountRef.class);
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
    public List<CollectionEntityArray> getCollectionEntityArray(String ban, Boolean history) throws Exception  {

        return objectMapper.readValue("[{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"agentId\":\"agent1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccountMaps\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccountRef\":{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccount\":{\"id\":\"12345\",\"accountGroupId\":1,\"accountType\":\"B\",\"accountSubType\":\"I\",\"name\":\"A Company\",\"state\":\"O\",\"@type\":\"BillingAccount\"},\"billingSystemId\":10,\"billingSystemName\":\"CES9\",\"closingCycle\":6,\"collectionStatus\":\"PRECOLL\",\"fraudIndicator\":false,\"involuntaryCeasedIndicator\":false,\"writeOffIndicator\":false,\"@type\":\"CollectionBillingAccountRef\"},\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.00Z\"},\"@type\":\"CollectionEntityBillingAccountMap\"}],\"characteristics\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"name\":\"string\",\"value\":\"string\",\"@type\":\"Characteristic\"}],\"collectionStatuses\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"code\":\"PRECOLL\",\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.00Z\"},\"@type\":\"CollectionStatus\"}],\"contacts\":[{\"id\":1,\"href\":\"BASE_URL/contact/1\",\"@referredType\":\"CollectionContact\",\"@type\":\"EntityRef\"}],\"customerRisk\":\"H\",\"customerRiskId\":59,\"customerValue\":\"L\",\"customerValueId\":10,\"delinquentCycle\":6,\"engagedCustomerParty\":{\"cbucid\":\"123\",\"cbuCode\":\"cbuCode123\",\"cbuName\":\"cbu-name-123\",\"organizationType\":\"CBU\",\"@type\":\"Organization\"},\"engagedRegionalCustomerParty\":{\"organizationType\":\"RC\",\"rcid\":\"rc-12345\",\"rcName\":\"rc-name-12345\",\"portfolioCategory\":\"PUBLIC\",\"portfolioSubCategory\":\"PUBLIC LARGE\",\"subMarketSegment\":\"string\",\"@type\":\"Organization\"},\"exclusionIndicatorCharacter\":\"string\",\"exclusionIndicatorInteger\":1,\"manualTreatmentIndicator\":false,\"name\":\"string\",\"notTouchListIndicator\":false,\"paymentArrangement\":{\"id\":1,\"href\":\"BASE_URL/paymentArrangement/1\",\"@referredType\":\"CollectionPaymentArrangement\",\"@type\":\"EntityRef\"},\"relatedEntity\":{\"id\":\"1\",\"role\":\"RCID\",\"@type\":\"RelatedEntity\"},\"tenure\":29,\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.00Z\"},\"workCategory\":\"string\",\"@type\":\"CollectionEntity\"}]",
        objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionEntityArray.class));
    }    
          // @ApiOperation(value = "Returns the AccessLog instance associated with the given id.")
        @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
        @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
        public CollectionEntity getCollectionEntityArray(@PathVariable("id") Integer id, Boolean history) throws Exception  {

       return objectMapper.readValue("{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"agentId\":\"agent1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccountMaps\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccountRef\":{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccount\":{\"id\":\"12345\",\"accountGroupId\":1,\"accountType\":\"B\",\"accountSubType\":\"I\",\"name\":\"A Company\",\"state\":\"O\",\"@type\":\"BillingAccount\"},\"billingSystemId\":10,\"billingSystemName\":\"CES9\",\"closingCycle\":6,\"collectionStatus\":\"PRECOLL\",\"fraudIndicator\":false,\"involuntaryCeasedIndicator\":false,\"writeOffIndicator\":false,\"@type\":\"CollectionBillingAccountRef\"},\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.00Z\"},\"@type\":\"CollectionEntityBillingAccountMap\"}],\"characteristics\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"name\":\"string\",\"value\":\"string\",\"@type\":\"Characteristic\"}],\"collectionStatuses\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"code\":\"PRECOLL\",\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.00Z\"},\"@type\":\"CollectionStatus\"}],\"contacts\":[{\"id\":1,\"href\":\"BASE_URL/contact/1\",\"@referredType\":\"CollectionContact\",\"@type\":\"EntityRef\"}],\"customerRisk\":\"H\",\"customerRiskId\":59,\"customerValue\":\"L\",\"customerValueId\":10,\"delinquentCycle\":6,\"engagedCustomerParty\":{\"cbucid\":\"123\",\"cbuCode\":\"cbuCode123\",\"cbuName\":\"cbu-name-123\",\"organizationType\":\"CBU\",\"@type\":\"Organization\"},\"engagedRegionalCustomerParty\":{\"organizationType\":\"RC\",\"rcid\":\"rc-12345\",\"rcName\":\"rc-name-12345\",\"portfolioCategory\":\"PUBLIC\",\"portfolioSubCategory\":\"PUBLIC LARGE\",\"subMarketSegment\":\"string\",\"@type\":\"Organization\"},\"exclusionIndicatorCharacter\":\"string\",\"exclusionIndicatorInteger\":1,\"manualTreatmentIndicator\":false,\"name\":\"string\",\"notTouchListIndicator\":false,\"paymentArrangement\":{\"id\":1,\"href\":\"BASE_URL/paymentArrangement/1\",\"@referredType\":\"CollectionPaymentArrangement\",\"@type\":\"EntityRef\"},\"relatedEntity\":{\"id\":\"1\",\"role\":\"RCID\",\"@type\":\"RelatedEntity\"},\"tenure\":29,\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.00Z\"},\"workCategory\":\"string\",\"@type\":\"CollectionEntity\"}",CollectionEntity.class);
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
    public List<CollectionContactArray> getCollectionContact(String ban, Boolean history) throws Exception  {

        return objectMapper.readValue("[{\"id\":1,\"href\":\"BASE_URL/contact/1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"comments\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"@type\":\"AuditInfo\"},\"text\":\"This is collection contact comment 1\",\"@type\":\"Comment\"}],\"email\":\"john.doe@telus.com\",\"collectionEntity\":{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"@referredType\":\"CollectionEntity\",\"@type\":\"EntityRef\"},\"faxNumnber\":\"9059979999\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"mobilePhoneNumber\":\"5149979999\",\"notificationIndicator\":true,\"telusContactIndicator\":true,\"title\":\"Mr.\",\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.00Z\"},\"workPhoneNumber\":\"9059979797\",\"@type\":\"CollectionContact\"}]",
        objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionContactArray.class));
    }    
          // @ApiOperation(value = "Returns the AccessLog instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionContact getCollectionContact(@PathVariable("id") Integer id, Boolean history) throws Exception  {

       return objectMapper.readValue("{\"comments\":[{\"text\":\"This is collection contact comment 1\"}],\"email\":\"john.doe@telus.com\",\"collectionEntity\":{\"id\":1},\"faxNumnber\":\"9059979999\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"mobilePhoneNumber\":\"5149979999\",\"notificationIndicator\":true,\"telusContactIndicator\":true,\"title\":\"Mr.\",\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.00Z\"},\"workPhoneNumber\":\"9059979797\",\"channel\":{\"originatorAppId\":\"fico-app-123\",\"userId\":\"t123456\"}}",CollectionContact.class);
    }    
    
    public CollectionContactCreate   addCollectionContact( CollectionContactCreate    collectionContactCreate  ) throws Exception  {
    
        return collectionContactCreate  ;
    }
    
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PATCH)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionContactUpdate  updateCollectionContact(@PathVariable("id") Integer id, CollectionContactUpdate   collectionContactUpdate ) throws Exception {
        return collectionContactUpdate ;
    }
    //PAAR
    @RequestMapping(value = "/paymentArrangement", method = {RequestMethod.GET})
    public List<CollectionPaymentArrangement> getPaymentArrangements(String entityId) throws Exception  {

        return objectMapper.readValue("[{\"id\":1,\"href\":\"BASE_URL/paymentArrangement/1\",\"allBillingAccountIncludedIndicator\":true,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccountMaps\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccountRef\":{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccount\":{\"id\":\"12345\",\"accountGroupId\":1,\"accountType\":\"B\",\"accountSubType\":\"I\",\"name\":\"A Company\",\"state\":\"O\",\"@type\":\"BillingAccount\"},\"billingSystemId\":10,\"billingSystemName\":\"CES9\",\"closingCycle\":6,\"collectionStatus\":\"INCOLL\",\"fraudIndicator\":false,\"involuntaryCeasedIndicator\":false,\"writeOffIndicator\":false,\"@type\":\"CollectionBillingAccountRef\"},\"validityIndicator\":true,\"@type\":\"CollectionPaymentArrangementBillingAccountMap\"}],\"collectionEntity\":{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"@referredType\":\"CollectionEntity\",\"@type\":\"EntityRef\"},\"comment\":\"string\",\"evaluationResult\":\"string\",\"expectedPaymentAmountToDate\":100.0,\"installments\":[{\"id\":1,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"date\":\"2023-01-01\",\"evaluationResult\":\"string\",\"evaluationTimestamp\":\"2023-01-01T09:00:00.001Z\",\"sequenceId\":1,\"validityIndicator\":true,\"@type\":\"CollectionPaymentInstallment\"}],\"receivedPaymentAmountToDate\":0.0,\"recurrence\":\"MONTHLY\",\"statuses\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"reason\":\"string\",\"status\":\"string\",\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.001Z\"},\"@type\":\"CollectionPaymentArrangementStatus\"}],\"@type\":\"CollectionPaymentArrangement\"}]",
        objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionPaymentArrangement.class));
        // return new Object(); 
    }
    
    // @ApiOperation(value = "Returns the AccessLog instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionPaymentArrangement getPaymentArrangement(@PathVariable("id") Integer id, Boolean history) throws Exception  {

        return objectMapper.readValue("{\"id\":1,\"href\":\"BASE_URL/paymentArrangement/1\",\"allBillingAccountIncludedIndicator\":true,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccountMaps\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccountRef\":{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccount\":{\"id\":\"12345\",\"accountGroupId\":1,\"accountType\":\"B\",\"accountSubType\":\"I\",\"name\":\"A Company\",\"state\":\"O\",\"@type\":\"BillingAccount\"},\"billingSystemId\":10,\"billingSystemName\":\"CES9\",\"closingCycle\":6,\"collectionStatus\":\"INCOLL\",\"fraudIndicator\":false,\"involuntaryCeasedIndicator\":false,\"writeOffIndicator\":false,\"@type\":\"CollectionBillingAccountRef\"},\"validityIndicator\":true,\"@type\":\"CollectionPaymentArrangementBillingAccountMap\"}],\"collectionEntity\":{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"@referredType\":\"CollectionEntity\",\"@type\":\"EntityRef\"},\"comment\":\"string\",\"evaluationResult\":\"string\",\"expectedPaymentAmountToDate\":100.0,\"installments\":[{\"id\":1,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"date\":\"2023-01-01\",\"evaluationResult\":\"string\",\"evaluationTimestamp\":\"2023-01-01T09:00:00.001Z\",\"sequenceId\":1,\"validityIndicator\":true,\"@type\":\"CollectionPaymentInstallment\"}],\"receivedPaymentAmountToDate\":0.0,\"recurrence\":\"MONTHLY\",\"statuses\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"reason\":\"string\",\"status\":\"string\",\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.001Z\"},\"@type\":\"CollectionPaymentArrangementStatus\"}],\"@type\":\"CollectionPaymentArrangement\"}",CollectionPaymentArrangement.class);
        // return new Object(); 
    }    
    
    public CollectionPaymentArrangementCreate addPaymentArrangement( CollectionPaymentArrangementCreate  collectionPaymentArrangementCreate) throws Exception  {
    
        logger.info(":::::::::::In add Parr, parr amount::::::::::::::::::" + collectionPaymentArrangementCreate.getAmount());
        logger.info(":::::::::::In add Parr, parr amount::::::::::::::::::" + collectionPaymentArrangementCreate.getInstallments());
        if (collectionPaymentArrangementCreate.getInstallments() != null) {
            logger.info(":::::::::::In add Parr, parr amount::::::::::::::::::" + collectionPaymentArrangementCreate.getInstallments().get(0).getAmount());
        }
        return collectionPaymentArrangementCreate;
        
    }
    
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PATCH)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionPaymentArrangementUpdate updatePaymentArrangement(@PathVariable("id") Integer id,CollectionPaymentArrangementUpdate collectionPaymentArrangementUpdate ) throws Exception  {
        
        logger.info(":::::::::::In add Parr, parr amount::::::::::::::::::" + collectionPaymentArrangementUpdate.getAmount());
        if (collectionPaymentArrangementUpdate.getInstallments() != null) {
            logger.info(":::::::::::In add Parr, parr amount::::::::::::::::::" + collectionPaymentArrangementUpdate.getInstallments().get(0).getAmount());
        }
        return collectionPaymentArrangementUpdate;
    }
    
//dispute

    @RequestMapping(value = "/dispute", method = {RequestMethod.GET})
    public List<CollectionDispute> getdispute(String fields,Integer offset, Integer limit, Integer banRefId, Boolean history) throws Exception  {

        return objectMapper.readValue("[{\"id\":1,\"href\":\"BASE_URL/dispute/1\",\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccountRef\":{\"id\":25664599,\"href\":\"BASE_URL/billingAccountRef/1\",\"@referredType\":\"CollectionBillingAccountRef\",\"@type\":\"EntityRef\"},\"billingAdjustmentRequestId\":\"string\",\"chargeType\":\"One-time charge\",\"customerContactName\":\"John Snow\",\"collectionExclusionIndicator\":false,\"comment\":\"string\",\"disputePrime\":\"string\",\"disputeReason\":\"BILLED CHARGES (DEEMED) INCORRECT\",\"product\":\"Business Connect\",\"statuses\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"reason\":\"string\",\"status\":\"OPEN\",\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.00Z\"},\"@type\":\"CollectionDisputeStatus\"}],\"@type\":\"CollectionDispute\"},{\"id\":2,\"href\":\"BASE_URL/dispute/2\",\"amount\":200.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-02T09:00:00.00Z\",\"dataSource\":\"fico-app-124\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-02T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccountRef\":{\"id\":25664600,\"href\":\"BASE_URL/billingAccountRef/2\",\"@referredType\":\"CollectionBillingAccountRef\",\"@type\":\"EntityRef\"},\"billingAdjustmentRequestId\":\"string\",\"chargeType\":\"Recurring billing @ 100%\",\"customerContactName\":\"Kevin Smith\",\"collectionExclusionIndicator\":true,\"comment\":\"comments\",\"disputePrime\":\"prime\",\"disputeReason\":\"PRICE / VALUE DISSATISFACTION\",\"product\":\"Fiber Internet\",\"statuses\":[{\"id\":2,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-02T09:00:00.00Z\",\"dataSource\":\"fico-app-124\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-02T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"reason\":\"reason\",\"status\":\"OPEN\",\"validFor\":{\"startDateTime\":\"2023-01-02T09:00:00.00Z\"},\"@type\":\"CollectionDisputeStatus\"}],\"@type\":\"CollectionDispute\"}]",
        objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionDispute.class));
        // return new Object(); 
    }
    
    public CollectionDisputeCreate addDispute( CollectionDisputeCreate  collectionDispute) throws Exception  {

        return collectionDispute;
        
    }

    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionDispute getdispute(@PathVariable("id") Integer id, String fields, Boolean history) throws Exception  {
     
       return objectMapper.readValue("{\"id\":1,\"href\":\"BASE_URL/dispute/1\",\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccountRef\":{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"@referredType\":\"CollectionBillingAccountRef\",\"@type\":\"EntityRef\"},\"billingAdjustmentRequestId\":\"string\",\"chargeType\":\"One-time charge\",\"customerEmail\":\"John.Snow@telus.com\",\"collectionExclusionIndicator\":false,\"comments\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"@type\":\"AuditInfo\"},\"text\":\"This is collection dispute comment 1\",\"@type\":\"Comment\"}],\"disputePrime\":\"string\",\"disputeReason\":\"BILLED CHARGES (DEEMED) INCORRECT\",\"product\":\"Business Connect\",\"statuses\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"comments\":[{\"id\":2,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"@type\":\"AuditInfo\"},\"text\":\"This is collection dispute status comment 1\",\"@type\":\"Comment\"}],\"reason\":\"string\",\"status\":\"OPEN\",\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.00Z\"},\"@type\":\"CollectionDisputeStatus\"}],\"@type\":\"CollectionDispute\"}",CollectionDispute.class);
    }    

    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PATCH)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionDisputeUpdate updateDispute(@PathVariable("id") Integer id,  CollectionDisputeUpdate  collectionDispute) throws Exception  {
    
        return collectionDispute;
        
    }

//suppression
   @RequestMapping(value = "/suppression", method = {RequestMethod.GET})
    public List<CollectionSuppressionArray> getSuppression(String banRefId) throws Exception  {

        return objectMapper.readValue("[{\"id\":1,\"href\":\"BASE_URL/suppression/1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccountRef\":{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"@referredType\":\"CollectionBillingAccountRef\",\"@type\":\"EntityRef\"},\"reason\":\"string\",\"effectiveDateRange\":{\"endDate\":\"2023-01-10\",\"startDate\":\"2023-01-01\"},\"@type\":\"CollectionSuppression\"}]",
        objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionSuppressionArray.class));
        // return new Object(); 
    }
    
    
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionSuppression getSuppression(@PathVariable("id") Integer id, String fields, Boolean history) throws Exception  {
     
       return objectMapper.readValue("{\"id\":1,\"href\":\"BASE_URL/suppression/1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccountRef\":{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"@referredType\":\"CollectionBillingAccountRef\",\"@type\":\"EntityRef\"},\"reason\":\"string\",\"effectiveDateRange\":{\"endDate\":\"2023-01-10\",\"startDate\":\"2023-01-01\"},\"@type\":\"CollectionSuppression\"}",CollectionSuppression.class);
    }    

    public CollectionSuppressionCreate addSuppression( CollectionSuppressionCreate  collectionSuppressionCreate) throws Exception  {

        return collectionSuppressionCreate;
    }
    
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PATCH)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionSuppressionUpdate updateSuppression(@PathVariable("id") Integer id,  CollectionSuppressionUpdate  collectionSuppressionUpdate) throws Exception  {
    
        return collectionSuppressionUpdate;
    }

}