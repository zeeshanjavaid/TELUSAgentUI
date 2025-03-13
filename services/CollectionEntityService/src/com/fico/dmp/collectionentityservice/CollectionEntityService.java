/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.collectionentityservice;
import com.fico.telus.utility.URIConstant;
import com.sun.xml.bind.v2.runtime.output.Encoded;
import com.fico.telus.model.CollectionPaymentArrangementResponse;
import com.fico.telus.model.DisputeResWithHeader;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;

import org.springframework.beans.factory.annotation.Autowired;

import com.fico.telus.model.ParrResWithHeader;

import javax.servlet.http.HttpServletRequest;
import javax.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;

import org.springframework.beans.factory.annotation.Autowired;
import com.fico.telus.model.DisputeResWithHeader;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;



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
import java.util.Optional;

//import io.swagger.client.model.CollectionPaymentArrangement;
//import io.swagger.client.model.CollectionPaymentArrangementCreate;
//import io.swagger.client.model.CollectionPaymentArrangementUpdate;
//import io.swagger.client.model.CollectionBillingAccountRef;
//import io.swagger.client.model.CollectionBillingAccountRefCreate;
//import io.swagger.client.model.CollectionBillingAccountRefUpdate;
//import io.swagger.client.model.CollectionEntityArray;
//import io.swagger.client.model.CollectionEntity;
//import io.swagger.client.model.CollectionEntityCreate;
//import io.swagger.client.model.CollectionEntityUpdate;
//import io.swagger.client.model.CollectionContactArray;
//import io.swagger.client.model.CollectionContact;
//import io.swagger.client.model.CollectionContactCreate;
//import io.swagger.client.model.CollectionContactUpdate;
////import io.swagger.client.model.CollectionSuppression;
////import io.swagger.client.model.CollectionSuppressionCreate;
////import io.swagger.client.model.CollectionSuppressionUpdate;
////import io.swagger.client.model.CollectionSuppressionArray;
//import io.swagger.client.model.CollectionDispute;
//import io.swagger.client.model.CollectionDisputeUpdate;
//import io.swagger.client.model.CollectionDisputeCreate;
//import io.swagger.client.model.EntityRef;

import telus.cdo.cnc.collmgmt.collentitymgmt.model.CollectionBillingAccountRef;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.CollectionBillingAccountRefCreate;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.CollectionBillingAccountRefUpdate;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.CollectionContact;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.CollectionContactArray;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.CollectionContactCreate;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.CollectionContactUpdate;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.CollectionDispute;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.CollectionDisputeCreate;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.CollectionDisputeUpdate;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.CollectionEntity;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.CollectionEntityArray;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.CollectionEntityCreate;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.CollectionEntityUpdate;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.CollectionPaymentArrangement;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.CollectionPaymentArrangementCreate;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.CollectionPaymentArrangementUpdate;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.EntityRef;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.util.List;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.fico.core.util.ObjectMapperConfig;
import com.fico.telus.service.PARRService;
import org.springframework.stereotype.Service;
import com.fico.pscomponent.util.PropertiesUtil;
import com.fico.telus.service.TelusAPIConnectivityService;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;




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

	private static final String ENTITYSVC_ENDPOINT_URL = "ENTITYSVC_ENDPOINT_BASEURL";
	
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
	
	@Autowired
	@Qualifier("customObjectMapper")
	private ObjectMapper mapper;


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
    @RequestMapping(value =  URIConstant.ApiMapping.GET_BILLING_ACCOUNT_REF, method = {RequestMethod.GET})
     public List<CollectionBillingAccountRef> getBillingAccountRef(String fields,Integer offset, Integer limit, String ban, String entityId, String id) throws Exception  {
    	 if (isStubEnabled) {
        return objectMapper.readValue("[{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccount\":{\"id\":\"12345\",\"accountGroupId\":1,\"accountType\":\"B\",\"accountSubType\":\"I\",\"name\":\"A Company\",\"state\":\"C\",\"stateDate\":\"2022-12-12\",\"@type\":\"BillingAccount\"},\"billingSystemId\":10,\"billingSystemName\":\"CES9\",\"ceaseReason\":null,\"closingCycle\":6,\"collectionStatus\":\"PRECOLL\",\"collectionStatusDate\":\"2022-12-12\",\"fraudIndicator\":false,\"involuntaryCeasedIndicator\":false,\"lineOfBusiness\":\"WLN\",\"previousCollectionEntity\":{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"@referredType\":\"CollectionEntity\",\"@type\":\"EntityRef\"},\"suppressionReason\":null,\"suppressionValidFor\":null,\"writeOffIndicator\":false,\"@type\":\"CollectionBillingAccountRef\"}]",
         objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionBillingAccountRef.class));
    	 }
    	 else {
    	    // System.out.println(id);
    	     logger.info("Checking id value "+id);

  			logger.info("::::::::Calling  getBillingAccountRef method ::::::::");

  			String idStr = null;
  			if(id != null) {
  				idStr = "in:"+id;
 			}
 			
 			String banStr = null;
  			if(ban != null) {
  				banStr = "eq:"+ban;
 			}
  			
  			
             UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.parrEndPointUrl + URIConstant.ApiMapping.GET_BILLING_ACCOUNT_REF)
            		 .queryParamIfPresent("fields", Optional.ofNullable(fields))
                     .queryParamIfPresent("ban", Optional.ofNullable(banStr))
                     .queryParamIfPresent("entityId",Optional.ofNullable(entityId))
                     .queryParamIfPresent("id",Optional.ofNullable(idStr))
                     .queryParamIfPresent("offset",Optional.ofNullable(offset))
                     .queryParamIfPresent("limit",Optional.ofNullable(limit));
                     
                     	logger.info("::::::::Billing Account Reference endpoint call success ::::::::",builder.toUriString());

         String responseStr = telusAPIConnectivityService.executeTelusAPI(null,builder.toUriString(), "GET", entitySvcAuthScope);
         	logger.info("::::::::Billing Account Reference endpoint call success ::::::::");
         	logger.info("Response---"+ responseStr);
         	List<CollectionBillingAccountRef> collectionBillingAccountRefList = objectMapper.readValue(responseStr,
				objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionBillingAccountRef.class));
							logger.info(":::::::: Completed Calling  entity endpoint call ::::::::");
         return collectionBillingAccountRefList;
 }
         
    }
    
    // @ApiOperation(value = "Returns the AccessLog instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionBillingAccountRef getBillingAccountRefById(@PathVariable("id") Integer id, String fields) throws Exception  {

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
         			String entityStr = null;
         			if(entityId != null) {
         				entityStr = "in:"+entityId;
         			}
         			
         			String cbucidStr = null;
         			if(cbucid != null) {
         				cbucidStr = "eq:"+cbucid;
         			}
         			
         			//UriComponentsBuilder.fromHttpUrl(this.parrEndPointUrl + URIConstant.ApiMapping.GET_ENTITY).queryParamIfPresent(name, value)
         			
                    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.parrEndPointUrl + URIConstant.ApiMapping.GET_ENTITY)
                            .queryParamIfPresent("id", Optional.ofNullable(entityStr))
                            .queryParamIfPresent("cbucid", Optional.ofNullable(cbucidStr))
                            .queryParamIfPresent("offset", Optional.ofNullable(offset))
                            .queryParamIfPresent("limit", Optional.ofNullable(limit));

                String responseStr = telusAPIConnectivityService.executeTelusAPI(null,builder.toUriString(), "GET", entitySvcAuthScope);
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
        public CollectionEntity getCollectionEntityById(@PathVariable("id") Integer id, Boolean history) throws Exception  {
        	if (isStubEnabled) {
        		return objectMapper.readValue("{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"agentId\":\"agent1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccountRefMaps\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccountRef\":{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"@referredType\":\"CollectionBillingAccountRef\",\"@type\":\"EntityRef\"},\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.00Z\"},\"@type\":\"CollectionEntityBillingAccountMap\"}],\"characteristics\":[{\"name\":\"name\",\"value\":\"value\",\"@type\":\"Characteristic\"}],\"collectionStatus\":\"PRECOLL\",\"collectionStatusDateTime\":\"2023-01-01T09:00:00.00Z\",\"customerRisk\":\"H\",\"customerRiskId\":59,\"customerValue\":\"L\",\"customerValueId\":10,\"delinquentCycle\":6,\"engagedCustomerParty\":{\"cbucid\":\"123\",\"cbuCode\":\"cbuCode123\",\"cbuName\":\"cbu-name-123\",\"organizationType\":\"CBU\",\"@type\":\"Organization\"},\"engagedRegionalCustomerParty\":{\"organizationType\":\"RC\",\"rcid\":\"rc-12345\",\"rcName\":\"rc-name-12345\",\"portfolioCategory\":\"PUBLIC\",\"portfolioSubCategory\":\"PUBLIC LARGE\",\"subMarketSegment\":\"string\",\"@type\":\"Organization\"},\"exclusionIndicatorCharacter\":\"string\",\"exclusionIndicatorInteger\":1,\"lineOfBusiness\":\"WLN\",\"manualTreatmentIndicator\":false,\"name\":\"Entity 1\",\"notTouchListIndicator\":false,\"randomDigit\":1,\"relatedEntity\":{\"id\":\"1\",\"role\":\"RCID\",\"@type\":\"RelatedEntity\"},\"tenure\":29,\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.00Z\"},\"workCategory\":\"string\",\"@type\":\"CollectionEntity\"}",CollectionEntity.class);
        	}else {
        		logger.info("::::::::Calling  getCollectionEntityById API ::::::::");
        		
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.parrEndPointUrl + URIConstant.ApiMapping.GET_ENTITY)
                        .queryParam("history", history)
                        .path("/"+id);

                String responseStr = telusAPIConnectivityService.executeTelusAPI(null,builder.toUriString(), "GET", entitySvcAuthScope);
                	logger.info("::::::::Entity endpoint call success ::::::::");
                	logger.info("Response from API---"+ responseStr);
               CollectionEntity collectionEntity = objectMapper.readValue(responseStr,CollectionEntity.class);
								logger.info(":::::::: Completed Calling  entity endpoint call ::::::::");
        		return collectionEntity;
        	}
       
    }    
    
    public CollectionEntity addCollectionEntity( CollectionEntityCreate collectionEntityCreate) throws Exception  {
    	logger.info("::::::::Inside  addCollectionEntity::::::::");
    	String requestPayload = objectMapper.writeValueAsString(collectionEntityCreate);
    	String responseStr = telusAPIConnectivityService.executeTelusAPI(requestPayload, this.parrEndPointUrl + URIConstant.ApiMapping.GET_ENTITY, "POST", entitySvcAuthScope);
    	logger.info("::::::::Response from Success Telus  API- ADD CollectionEntity:::::\n::::::: {}",responseStr);
    	CollectionEntity collectionEntityResponse = objectMapper.readValue(responseStr,CollectionEntity.class); 
        return collectionEntityResponse;
    }
    
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PATCH)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionEntityUpdate  updateCollectionEntity(@PathVariable("id") Integer id, CollectionEntityUpdate collectionEntityUpdate ) throws Exception {
    	 logger.info("Inside updateCollectionEntity method for entityId = "+id);
     	String requestPayload = objectMapper.writeValueAsString(collectionEntityUpdate);
     	//requestPayload = requestPayload.replace("\"NULL\"", "null");
     	logger.info("::::::::Request payload for Update Collection entity:::::"+requestPayload);
     	logger.info("URL is : "+this.parrEndPointUrl);
     	String responseStr = telusAPIConnectivityService.executeTelusAPI(requestPayload, this.parrEndPointUrl + URIConstant.ApiMapping.GET_ENTITY + "/" + id, "PATCH", entitySvcAuthScope);
     	logger.info("::::::::Response from Success Telus  API- Update entity:::::\n::::::: {}",responseStr);
     	CollectionEntityUpdate collectionEntityUpdateResponse = objectMapper.readValue(responseStr,CollectionEntityUpdate.class);
    	return collectionEntityUpdateResponse;
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
    public CollectionContact getContactById(@PathVariable("id") Integer id, String fields) throws Exception  {
    	if (isStubEnabled) {
       return objectMapper.readValue("{\"id\":1,\"href\":\"BASE_URL/contact/1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"comment\":\"This is collection contact comment 1\",\"email\":\"john.doe@telus.com\",\"collectionEntity\":{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"@referredType\":\"CollectionEntity\",\"@type\":\"EntityRef\"},\"faxNumnber\":\"9059979999\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"mobilePhoneNumber\":\"5149979999\",\"notificationIndicator\":true,\"telusContactIndicator\":true,\"title\":\"Mr.\",\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.00Z\"},\"workPhoneNumber\":\"9059979797\",\"@type\":\"CollectionContact\"}",CollectionContact.class);
    	}else {
    		logger.info("::::::::Calling  entity endpoint call ::::::::");
    		logger.info("Id in getContactById is...."+id);
            String responseStr = telusAPIConnectivityService.executeTelusAPI(null,this.parrEndPointUrl + URIConstant.ApiMapping.GET_CONTACT + "/" + id, "GET", entitySvcAuthScope);
            	logger.info("::::::::Entity endpoint call success ::::::::");
            	logger.info("Response from ContactById---"+ responseStr);
            	CollectionContact collectionContact = objectMapper.readValue(responseStr,CollectionContact.class);
							logger.info(":::::::: Completed Calling  entity endpoint call ::::::::");
            return collectionContact;
    	}
    }    
    
    public CollectionContactCreate   addContact(CollectionContactCreate collectionContactCreate) throws Exception  {
    	logger.info("::::::::Inside Add contact API");
    	String requestPayload = objectMapper.writeValueAsString(collectionContactCreate);
    	String responseStr = telusAPIConnectivityService.executeTelusAPI(requestPayload, this.parrEndPointUrl + URIConstant.ApiMapping.GET_CONTACT, "POST", entitySvcAuthScope);
    	logger.info("::::::::Response from Success Telus  API- ADD CONTACT:::::\n::::::: {}",responseStr);
    	CollectionContactCreate collectionContactCreateResponse = objectMapper.readValue(responseStr,CollectionContactCreate.class);
        return collectionContactCreateResponse;
    }
    
     @RequestMapping(value = "/{id:.+}", method = RequestMethod.PATCH)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionContactUpdate  updateContact(@PathVariable("id") Integer id, CollectionContactUpdate   collectionContactUpdate ) throws Exception {
    	 logger.info("Inside updateContact method"+id);
     	String requestPayload = objectMapper.writeValueAsString(collectionContactUpdate);
     	logger.info("requestPayload for update Contact---"+requestPayload);
     	String responseStr = telusAPIConnectivityService.executeTelusAPI(requestPayload, this.parrEndPointUrl + URIConstant.ApiMapping.GET_CONTACT + "/" + id, "PATCH", entitySvcAuthScope);
     	logger.info("::::::::Response from Success Telus  API- Update contact:::::\n::::::: {}",responseStr);
     	CollectionContactUpdate collectionContactUpdateResponse = objectMapper.readValue(responseStr,CollectionContactUpdate.class);
     	return collectionContactUpdateResponse;
    }
    //PAAR
  @RequestMapping(value = URIConstant.ApiMapping.GET_PARR, method = {RequestMethod.GET})
    public List<CollectionPaymentArrangementResponse> getPaymentArrangements(String fields, Integer offset, Integer limit, String agentId, String entityId, String entityRisk, String evaluation, String status, String createdBy, String createdFrom, String createdTo) throws Exception  {
    	
	  String statusStr = null;
		if(status != null) {
			statusStr = "eq:"+status;
		}
		
		String entityIdStr = null;
		if(entityId != null) {
			entityIdStr = "eq:"+entityId;
		}
		
		
         UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(parrEndPointUrl+URIConstant.ApiMapping.GET_PARR)
              .queryParam("fields", fields)
               .queryParam("agentId", agentId)
                .queryParam("entityRisk", entityRisk)
                 .queryParam("evaluation", evaluation)
                 .queryParamIfPresent("status",Optional.ofNullable(statusStr))
                   .queryParam("createdBy", createdBy)
                    .queryParam("createdFrom", createdFrom)
                     .queryParam("createdTo", createdTo)
                     .queryParamIfPresent("entityId",Optional.ofNullable(entityIdStr))
                      .queryParam("offset",offset)
                     .queryParam("limit",limit);
              // .queryParam("entityRisk",entityRisk);
    //	return parrService.getPaymentArrangements(entityId,builder.toUriString());
    		  ParrResWithHeader paymentArrangements = parrService.getPaymentArrangements(entityId, builder.toUriString());
    		  	  return convertTelusResponseToFawbResponse(paymentArrangements);


    }
    
    
    
private List<CollectionPaymentArrangementResponse> convertTelusResponseToFawbResponse(ParrResWithHeader paymentArrangements) {

		List<CollectionPaymentArrangementResponse> collectionPaymentArrangementResponseList = new ArrayList<>();
		if (!paymentArrangements.getResponseObjectList().isEmpty()){
			for (CollectionPaymentArrangement collectionPaymentArrangement : paymentArrangements.getResponseObjectList()) {
				CollectionPaymentArrangementResponse collectionPaymentArrangementResponse = new CollectionPaymentArrangementResponse();
				collectionPaymentArrangementResponse.setId(collectionPaymentArrangement.getId());
				collectionPaymentArrangementResponse.setHref(collectionPaymentArrangement.getHref());
				collectionPaymentArrangementResponse.setAllBillingAccountIncludedIndicator(collectionPaymentArrangement.getAllBillingAccountIncludedIndicator());
				collectionPaymentArrangementResponse.setAmount(collectionPaymentArrangement.getAmount());
				collectionPaymentArrangementResponse.setAuditInfo(collectionPaymentArrangement.getAuditInfo());
				collectionPaymentArrangementResponse.setBillingAccountRefs(collectionPaymentArrangement.getBillingAccountRefs());
				collectionPaymentArrangementResponse.setCollectionEntity(collectionPaymentArrangement.getCollectionEntity());
				collectionPaymentArrangementResponse.setComment(collectionPaymentArrangement.getComment());
				collectionPaymentArrangementResponse.setEvaluationResult(collectionPaymentArrangement.getEvaluationResult());
				collectionPaymentArrangementResponse.setExpectedPaymentAmountToDate(collectionPaymentArrangement.getExpectedPaymentAmountToDate());
				collectionPaymentArrangementResponse.setInstallments(collectionPaymentArrangement.getInstallments());
				collectionPaymentArrangementResponse.setReceivedPaymentAmountToDate(collectionPaymentArrangement.getReceivedPaymentAmountToDate());
				collectionPaymentArrangementResponse.setRecurrence(collectionPaymentArrangement.getRecurrence());
				collectionPaymentArrangementResponse.setStatus(collectionPaymentArrangement.getStatus());
				collectionPaymentArrangementResponse.setStatusDateTime(collectionPaymentArrangement.getStatusDateTime());
				collectionPaymentArrangementResponse.setStatusReason(collectionPaymentArrangement.getStatusReason());
				collectionPaymentArrangementResponse.setBaseType(collectionPaymentArrangement.getAtBaseType());
				collectionPaymentArrangementResponse.setSchemaLocation(collectionPaymentArrangement.getAtSchemaLocation());
				collectionPaymentArrangementResponse.setType(collectionPaymentArrangement.getAtType());
				collectionPaymentArrangementResponse.setTotalNumberOfElement(paymentArrangements.getTotalNumberOfElement());
				collectionPaymentArrangementResponseList.add(collectionPaymentArrangementResponse);
			}
	}

		return collectionPaymentArrangementResponseList;

	}
  
  
  @RequestMapping(value = URIConstant.ApiMapping.GET_PARR, method = {RequestMethod.GET})
  public ParrResWithHeader getPaymentArrangementsForParrReport(String fields, Integer offset, Integer limit, String agentId, String entityId, String entityRisk, String evaluation, String status, String createdBy, String createdFrom, String createdTo) throws Exception  {
  	
	  String statusStr = null;
		if(status != null) {
			statusStr = "eq:"+status;
		}
		
		String entityIdStr = null;
		if(entityId != null) {
			entityIdStr = "eq:"+entityId;
		}
		
		String entityRiskStr = null;
		if(entityRisk != null) {
			entityRiskStr = "eq:"+entityRisk;
		}
		
		String evaluationStr = null;
		if(evaluation != null) {
			evaluationStr = "eq:"+evaluation;
		}
		
		String createdByStr = null;
		if(createdBy != null) {
			createdByStr = "in:"+createdBy;
		}
		
		String createdFromStr = null;
		if(createdFrom != null) {
			createdFromStr = "eq:"+createdFrom;
		}

		String createdToStr = null;
		if(createdTo != null) {
			createdToStr = "eq:"+createdTo;
		}
		
	  
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(parrEndPointUrl+URIConstant.ApiMapping.GET_PARR)
            .queryParamIfPresent("fields", Optional.ofNullable(fields))
             .queryParamIfPresent("agentId", Optional.ofNullable(agentId))
              .queryParamIfPresent("entityRisk", Optional.ofNullable(entityRiskStr))
               .queryParamIfPresent("evaluation", Optional.ofNullable(evaluationStr))
                .queryParamIfPresent("status", Optional.ofNullable(statusStr))
                 .queryParamIfPresent("createdBy", Optional.ofNullable(createdByStr))
                  .queryParamIfPresent("createdFrom", Optional.ofNullable(createdFromStr))
                  .queryParamIfPresent("limit", Optional.ofNullable(limit))
                  .queryParamIfPresent("offset",Optional.ofNullable(offset))
                   .queryParamIfPresent("entityId",Optional.ofNullable(entityIdStr))
                   .queryParamIfPresent("createdTo", Optional.ofNullable(createdToStr));
                   
                   logger.info("Htting telus API for Parr Report :::::::::::::::::::::::");
	  logger.info("parrEndPointUrl--------"+builder.toUriString());
	  
	  
	  ParrResWithHeader parrResWithHeader=new ParrResWithHeader();

	  List<CollectionPaymentArrangement> responseObjectLis;
	  ResponseEntity<String> responseFromTelus = telusAPIConnectivityService.executeTelusAPIAndGetResponseWithHeader(null, builder.toUriString(), "GET", entitySvcAuthScope);
	  String result=responseFromTelus.getBody();
	  HttpHeaders headers1=responseFromTelus.getHeaders();
	  String totalNoOfElement=headers1.getFirst("x-total-count");
	 responseObjectLis= objectMapper.readValue(result, objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionPaymentArrangement.class));
     parrResWithHeader.setTotalNumberOfElement(Integer.parseInt(totalNoOfElement));
	 parrResWithHeader.setResponseObjectList(responseObjectLis);
	  return parrResWithHeader;
	  //    if(entityId != null) {

  }
        // @ApiOperation(value = "Returns the AccessLog instance associated with the given id.")
        @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
        @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
        public CollectionPaymentArrangement getPaymentArrangementById(@PathVariable("id") Integer id, Boolean history) throws Exception  {

        return parrService.getPaymentArrangement(id, history);
        	// return new Object(); 
    }    
        
    public CollectionPaymentArrangementCreate addPaymentArrangement(CollectionPaymentArrangementCreate  collectionPaymentArrangementCreate, String entityId) throws Exception  {
    
        CollectionPaymentArrangementCreate collectionPaymentArrangement = parrService.createPaymentArrangement(collectionPaymentArrangementCreate, entityId);
        return collectionPaymentArrangement;
        
    }
    
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionPaymentArrangementUpdate updatePaymentArrangement(@PathVariable("id") Integer id,CollectionPaymentArrangementUpdate collectionPaymentArrangementUpdate) throws Exception  {
        
    	CollectionPaymentArrangementUpdate collectionPaymentArrangement = parrService.updatePaymentArrangement(collectionPaymentArrangementUpdate,id);
        return collectionPaymentArrangement;        
    }
    
    public CollectionPaymentArrangementUpdate updateParrStatus(Integer parrId, String status, String comments) throws Exception {
        
    	CollectionPaymentArrangementUpdate collectionPaymentArrangement = parrService.updateParrStatus(parrId, status, comments);
        return collectionPaymentArrangement;
    }
    
//dispute

    @RequestMapping(value = "/dispute", method = {RequestMethod.GET})
    public DisputeResWithHeader getdispute(String fields,Integer offset, Integer limit, String baRefId, String entityId) throws Exception  {
    	if (isStubEnabled) {
        return objectMapper.readValue("[{\"id\":1,\"href\":\"BASE_URL/dispute/1\",\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccountRef\":{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"@referredType\":\"CollectionBillingAccountRef\",\"@type\":\"EntityRef\"},\"billingAdjustmentRequestId\":\"string\",\"chargeType\":\"One-time charge\",\"comment\":\"Collection dispute comment 1\",\"customerEmail\":\"John.Snow@telus.com\",\"collectionExclusionIndicator\":false,\"disputePrime\":\"string\",\"disputeReason\":\"BILLED CHARGES (DEEMED) INCORRECT\",\"product\":\"Business Connect\",\"status\":\"OPEN\",\"statusDateTime\":\"2023-01-01T09:00:00.00Z\",\"statusReason\":\"string\",\"@type\":\"CollectionDispute\"}]",
        objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionDispute.class));
    	}else {
 			logger.info("::::::::Calling  entity endpoint call ::::::::");
 			
 			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.parrEndPointUrl + URIConstant.ApiMapping.GET_DISPUTE)
                    .queryParam("entityId","eq:"+entityId)
                     .queryParam("offset",offset)
                     .queryParam("limit",limit);
 			if(entityId != null) {
    //         String responseStr = telusAPIConnectivityService.executeTelusAPI(null,builder.toUriString(), "GET", entitySvcAuthScope);
    //         	logger.info("::::::::Entity endpoint call success ::::::::");
    //         	logger.info("Resoinse---"+ responseStr);
    //         	List<CollectionDispute> collectionDisputeList = objectMapper.readValue(responseStr,
				// objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionDispute.class));
				// 			logger.info(":::::::: Completed Calling  entity endpoint call ::::::::");
    //         return collectionDisputeList;
         DisputeResWithHeader disputeResWithHeader=new DisputeResWithHeader();

				List<CollectionDispute> responseObjectLis;
				ResponseEntity<String> responseFromTelus = telusAPIConnectivityService.executeTelusAPIAndGetResponseWithHeader(null, builder.toUriString(), "GET", entitySvcAuthScope);
				String result=responseFromTelus.getBody();
				HttpHeaders headers1=responseFromTelus.getHeaders();
				String totalNoOfElement=headers1.getFirst("x-total-count");
				responseObjectLis= objectMapper.readValue(result, objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionDispute.class));
				disputeResWithHeader.setTotalNumberOfElement(Integer.parseInt(totalNoOfElement));
				disputeResWithHeader.setResponseObjectList(responseObjectLis);
				return disputeResWithHeader;
    	}
    }
         return null; 
    }
    
    public CollectionDisputeCreate addDispute( CollectionDisputeCreate  collectionDispute) throws Exception  {
    	String requestPayload = objectMapper.writeValueAsString(collectionDispute);
    	String responseStr = telusAPIConnectivityService.executeTelusAPI(requestPayload, this.parrEndPointUrl + URIConstant.ApiMapping.GET_DISPUTE, "POST", entitySvcAuthScope);
    	logger.info("::::::::Response from Success Telus  API- ADD Dispute:::::\n::::::: {}",responseStr);
    	CollectionDisputeCreate collectionDisputeCreate = objectMapper.readValue(responseStr,CollectionDisputeCreate.class);
        return collectionDisputeCreate;
        
    }

    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionDispute getdisputeById(@PathVariable("id") Integer id, String fields, Boolean history) throws Exception  { 
    	if (isStubEnabled) {
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

   // @RequestMapping(value = "/{id:.+}", method = RequestMethod.PATCH)
   // @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionDisputeUpdate updateDispute(@PathVariable("id") Integer id,  CollectionDisputeUpdate  collectionDispute) throws Exception  {
        logger.info("Inside update Dispute method"+id);
    	String requestPayload = objectMapper.writeValueAsString(collectionDispute);
    	String responseStr = telusAPIConnectivityService.executeTelusAPI(requestPayload, this.parrEndPointUrl + URIConstant.ApiMapping.GET_DISPUTE + "/" + id, "PATCH", entitySvcAuthScope);
    	logger.info("::::::::Response from Success Telus  API- ADD Dispute:::::\n::::::: {}",responseStr);
    	CollectionDisputeUpdate collectionDisputeUpdate = objectMapper.readValue(responseStr,CollectionDisputeUpdate.class);
    	return collectionDisputeUpdate;
        
    }


}
