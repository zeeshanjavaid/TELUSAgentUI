/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.collectiontreatmentservice;
import java.nio.charset.StandardCharsets;
import java.net.URI;

import java.net.URLEncoder;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fico.pscomponent.util.PropertiesUtil;
import com.fico.telus.service.TelusAPIConnectivityService;
import com.fico.telus.utility.URIConstant;
import io.swagger.client.model.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fico.core.util.ObjectMapperConfig;
//import com.fico.dmp.collectiontreatmentservice.model.*;
import com.wavemaker.runtime.security.SecurityService;
import com.wavemaker.runtime.service.annotations.ExposeToClient;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;
import com.wavemaker.tools.api.core.annotations.WMAccessVisibility;
import com.wavemaker.tools.api.core.models.AccessSpecifier;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.ws.rs.HttpMethod;
import java.math.BigDecimal;
import java.util.stream.Collectors;
import com.fico.dmp.collectionentityservice.CollectionEntityService;
import com.fico.telus.model.OrderMgmtHistoryResponse;
import java.util.List;
import java.util.ArrayList;
import com.fico.dmp.commonutilityservice.CommonUtilityService;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fico.telus.model.CollectionTreatmentStepResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import com.fico.telus.model.CollectionActivityLogRes;










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
public class CollectionTreatmentService {

    private static final String IS_COLLTREATMENT_STUB_ENABLED = "IS_COLLTREATMENT_STUB_ENABLED";

    private static final String COLLTREATMENT_ENDPOINT_URL = "COLLTREATMENT_ENDPOINT_BASEURL";

    private static final String COLLTREATMENT_ENDPOINT_SCOPE = "COLLTREATMENT_ENDPOINT_SCOPE";

    private boolean isStubEnabled;

    @Autowired
    private TelusAPIConnectivityService telusAPIConnectivityService;
    
        @Autowired
    private CollectionEntityService collectionEntityService;
      @Autowired
    private CommonUtilityService commonUtilityService;



    private String collectionTreatmentEndPointUrl;

    private String collTreatmentSvcAuthScope;

    private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(CollectionTreatmentService.class.getName());

    @Autowired
    private SecurityService securityService;
    @Autowired
    private PropertiesUtil propertiesUtil;

    private final ObjectMapper objectMapper = new ObjectMapperConfig().customObjectMapper();

    @PostConstruct
    public void init() {

        this.isStubEnabled = Boolean.valueOf(propertyValueFrom(IS_COLLTREATMENT_STUB_ENABLED, "false"));
        this.collectionTreatmentEndPointUrl = propertyValueFrom(COLLTREATMENT_ENDPOINT_URL, URIConstant.COLLECTION_TREATMENT_STEP_SERVICE_URL);
        this.collTreatmentSvcAuthScope = propertyValueFrom(COLLTREATMENT_ENDPOINT_SCOPE, "3353");

    }

    private String propertyValueFrom(String propertyName, String defaulValueIfNull) {
        String propertyValue = propertiesUtil.getPropertyValue(propertyName);
        if (propertyValue == null) {
            logger.info("property value is null, using default");
            propertyValue = defaulValueIfNull;
        }
        return propertyValue;
    }
    
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public List<CollectionTreatment> getCollectionTreatment(Integer collectionEntityId,String fields,Integer offset, Integer limit, Boolean history) throws Exception  {
        
        if (isStubEnabled) {
    	return objectMapper.readValue("[{\"id\":1889,\"collectionEntity\":{\"id\":666,\"name\":\"Food Basics\",\"href\":\"{BASE_URL}/entity/666\",\"@baseType\":\"CollectionEntity\",\"@type\":\"CollectionEntity\",\"@referredType\":\"CollectionEntity\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionEntity.swagger.json\"},\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"https\"},\"nextStepDueDate\":\"2023-12-31\",\"collectionTreatmentSteps\":[{\"id\":68891,\"name\":\"Step 68891\",\"href\":\"{BASE_URL}/collectionTreatmentStep/68891\",\"@baseType\":\"CollectionTreatmentStep\",\"@type\":\"CollectionTreatmentStep\",\"@referredType\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"},{\"id\":68892,\"name\":\"Step 68892\",\"href\":\"{BASE_URL}/collectionTreatmentStep/68892\",\"@baseType\":\"CollectionTreatmentStep\",\"@type\":\"CollectionTreatmentStep\",\"@referredType\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"}],\"cureThreshold\":3,\"dmPathID\":\"A DM Path ID example\",\"lastAssessmentProcessType\":\"BILLCALL\",\"lastCollectionAssessmentId\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"scenarioId\":\"Scenario information 2234753222\",\"additionalCharacteristics\":[{\"name\":\"source\",\"value\":\"head office\",\"@baseType\":\"CollectionTreatmentStep\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"},{\"name\":\"assigned agent\",\"value\":\"offshore\",\"@baseType\":\"CollectionTreatmentStep\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"}]}]",
    	         objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionTreatment.class));
        }else{

           List<CollectionTreatment> collectionTreatmentStepList=new ArrayList<>();
           if(collectionEntityId!=null){
            logger.info("::::::::Calling  entity data endpoint call ::::::::");
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(collectionTreatmentEndPointUrl+URIConstant.ApiMapping.GET_COLLECTION_TREATMENT)
                    .queryParam("collectionEntityId",collectionEntityId )
                     .queryParam("fields",fields )
                      .queryParam("offset",offset )
                      .queryParam("limit",limit )
                    .queryParam("history",history);
                   // .queryParam("limit",20);
            String responseStr = telusAPIConnectivityService.executeTelusAPI(null,builder.toUriString(), HttpMethod.GET, collTreatmentSvcAuthScope);
            logger.info("::::::::Entity data endpoint call success ::::::::");
            logger.info("Resoinse---"+ responseStr);
          //  return objectMapper.readValue(responseStr, CollectionTreatmentStep.class);
             collectionTreatmentStepList= objectMapper.readValue(responseStr, new TypeReference<List<CollectionTreatment>>(){});

        }
        
        
        return collectionTreatmentStepList;
        
    }
        
       
    	
    }
    
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionTreatment getCollectionTreatmentById(@PathVariable("id") String id, String partitionKey) throws Exception {
    	return objectMapper.readValue("{\"id\":1889,\"collectionEntity\":{\"id\":666,\"name\":\"Food Basics\",\"href\":\"{BASE_URL}/entity/666\",\"@baseType\":\"CollectionEntity\",\"@type\":\"CollectionEntity\",\"@referredType\":\"CollectionEntity\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionEntity.swagger.json\"},\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13 14:30:45\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30 14:30:45\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"schema\"},\"nextStepDueDate\":\"2023-12-31\",\"collectionTreatmentSteps\":[{\"id\":68891,\"name\":\"Step 68891\",\"href\":\"{BASE_URL}/collectionTreatmentStep/68891\",\"@baseType\":\"CollectionTreatmentStep\",\"@type\":\"CollectionTreatmentStep\",\"@referredType\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"},{\"id\":68892,\"name\":\"Step 68892\",\"href\":\"{BASE_URL}/collectionTreatmentStep/68892\",\"@baseType\":\"CollectionTreatmentStep\",\"@type\":\"CollectionTreatmentStep\",\"@referredType\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"}],\"cureThreshold\":3,\"dmPathID\":\"A DM Path ID example\",\"lastAssessmentProcessType\":\"BILLCALL\",\"lastCollectionAssessmentId\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"scenarioId\":\"Scenario information 2234753222\",\"additionalCharacteristics\":[{\"name\":\"source\",\"value\":\"head office\",\"@baseType\":\"CollectionTreatmentStep\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"},{\"name\":\"assigned agent\",\"value\":\"offshore\",\"@baseType\":\"CollectionTreatmentStep\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"}]}",CollectionTreatment.class);
    }
    
    public CollectionTreatment  addCollectionTreatment(CollectionTreatment collectionTreatment ) throws Exception  {
        return collectionTreatment;
    }
    
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PATCH)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionTreatment updateCollectionTreatment(@PathVariable("id") String id, CollectionTreatment  collectionTreatment) throws Exception {
        return collectionTreatment;
    }
    
    //@WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public List<CollectionTreatmentStepResponse> getCollectionTreatmentStep(Boolean IsOdManagement, Integer collectionTreatmentStepId, Integer collectionEntityId,String type,String createdDate,String createdBy, String status,String assignedAgentId, String assignedTeam, String fields, Integer offset, Integer limit) throws Exception  {
        //Commented telus live api 
                            List<CollectionTreatmentStep> collectionTreatmentStepList=new ArrayList<>();
                            
                            logger.info("::::::::In getCollTeatmentStep call :::::::: ",IsOdManagement);

    	if (isStubEnabled) {
    	return objectMapper.readValue("[{\"id\":300102,\"stepDate\":\"2023-03-02\",\"billingAccountIdRefs\":[{\"id\":72907342,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/72907342\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":14384583,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/14384583\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":282424,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/282424\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"}],\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"},\"status\":\"PENDING\",\"stepTypeCode\":\"SUS\",\"languageCode\":\"en\",\"assignedAgentId\":\"48154\",\"priority\":\"Low\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"TD OM CHECK 45\",\"comment\":\"comment 300102\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/300102\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":300103,\"stepDate\":\"2023-03-03\",\"billingAccountIdRefs\":[{\"id\":193,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/193\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"}],\"additionalCharacteristics\":[{\"name\":\"CustomerName\",\"value\":\"John\"},{\"name\":\"PhoneNumber\",\"value\":\"884585\"},{\"name\":\"CallDuration\",\"value\":\"5\"}],\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"},\"status\":\"PENDING\",\"stepTypeCode\":\"CALL-IB\",\"languageCode\":\"en\",\"assignedAgentId\":\"48154\",\"priority\":\"Medium\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"TD OM CHECK 45\",\"comment\":\"comment 300103\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/300103\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":300104,\"stepDate\":\"2023-03-04\",\"status\":\"PENDING\",\"stepTypeCode\":\"CALL-OB\",\"languageCode\":\"en\",\"assignedAgentId\":\"13\",\"priority\":\"High\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"TD OM CHECK 45\",\"comment\":\"comment 300104\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/300104\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\",\"additionalCharacteristics\":[{\"name\":\"ReachedCustomer\",\"value\":\"Y\"},{\"name\":\"Phone\",\"value\":\"124545\"}]},{\"id\":300112,\"stepDate\":\"2023-03-04\",\"status\":\"PENDING\",\"stepTypeCode\":\"CALL-OB\",\"languageCode\":\"en\",\"assignedAgentId\":\"\",\"priority\":\"High\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"TD OM CHECK 45\",\"comment\":\"comment 300104\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/300104\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\",\"additionalCharacteristics\":[{\"name\":\"ReachedCustomer\",\"value\":\"N\"},{\"name\":\"Phone\",\"value\":\"124545\"},{\"name\":\"Outcome\",\"value\":\"Left voicemail\"}],\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"}},{\"id\":300113,\"stepDate\":\"2023-03-04\",\"status\":\"PENDING\",\"stepTypeCode\":\"NOTC1-PMTR\",\"languageCode\":\"en\",\"assignedAgentId\":\"13\",\"priority\":\"High\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"TD OM CHECK 45\",\"comment\":\"comment 300104\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/300104\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\",\"additionalCharacteristics\":[{\"name\":\"CustomerName\",\"value\":\"Kiran\"},{\"name\":\"EmailAddress\",\"value\":\"john.cena@telus.com\"}],\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"}},{\"id\":3001456,\"stepDate\":\"2023-03-04\",\"status\":\"PENDING\",\"stepTypeCode\":\"NOTC2-OD\",\"languageCode\":\"en\",\"assignedAgentId\":\"13\",\"priority\":\"High\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"TD OM CHECK 45\",\"comment\":\"comment 300104\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/300104\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\",\"additionalCharacteristics\":[{\"name\":\"CustomerName\",\"value\":\"John\"},{\"name\":\"EmailAddress\",\"value\":\"john.cena@telus.com\"}],\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"}},{\"id\":300153,\"stepDate\":\"2023-03-04\",\"status\":\"PENDING\",\"stepTypeCode\":\"NOTC3-DIST\",\"languageCode\":\"en\",\"assignedAgentId\":\"13\",\"priority\":\"High\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"TD OM CHECK 45\",\"comment\":\"comment 300104\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/300104\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\",\"additionalCharacteristics\":[{\"name\":\"CustomerName\",\"value\":\"Kiran\"},{\"name\":\"EmailAddress\",\"value\":\"john.cena@telus.com\"}],\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"}},{\"id\":300223,\"stepDate\":\"2023-03-04\",\"status\":\"PENDING\",\"stepTypeCode\":\"NOTC4-CANL\",\"languageCode\":\"en\",\"assignedAgentId\":\"13\",\"priority\":\"High\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"TD OM CHECK 45\",\"comment\":\"comment 300104\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/300104\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\",\"additionalCharacteristics\":[{\"name\":\"CustomerName\",\"value\":\"Kiran\"},{\"name\":\"EmailAddress\",\"value\":\"john.cena@telus.com\"}],\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"}},{\"id\":400102,\"stepDate\":\"2023-04-02\",\"billingAccountIdRefs\":[{\"id\":4,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/4\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":58,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/58\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":6434,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/6434\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":432,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/432\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"}],\"additionalCharacteristics\":[{\"name\":\"EmailAddress\",\"value\":\"Kevin.snow@telus.com\"}],\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"},\"status\":\"PENDING\",\"stepTypeCode\":\"EM-IN\",\"languageCode\":\"en\",\"assignedAgentId\":\"13\",\"priority\":\"Low\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"TD OM CHECK 45\",\"comment\":\"comment 400102\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/400102\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":400213,\"stepDate\":\"2023-04-02\",\"billingAccountIdRefs\":[{\"id\":4,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/4\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":58,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/58\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":6434,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/6434\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":432,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/432\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"}],\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"},\"status\":\"PENDING\",\"stepTypeCode\":\"FOLLOWUP\",\"languageCode\":\"en\",\"assignedAgentId\":\"13\",\"priority\":\"Low\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"TD OM CHECK 45\",\"comment\":\"comment 400102\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/400102\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":400103,\"stepDate\":\"2023-04-03\",\"status\":\"PENDING\",\"stepTypeCode\":\"SUS\",\"languageCode\":\"en\",\"assignedAgentId\":\"parry.sound\",\"priority\":\"TOP\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"weq\",\"comment\":\"comment 400103\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/400103\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\",\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"}},{\"id\":400104,\"stepDate\":\"2023-04-04\",\"billingAccountIdRefs\":[{\"id\":7,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/7\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":9,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/9\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":6,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/6\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"}],\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"},\"status\":\"PENDING\",\"stepTypeCode\":\"SUS\",\"languageCode\":\"en\",\"assignedAgentId\":\"parry.sound\",\"priority\":\"TOP\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"jhmmn\",\"comment\":\"comment 400104\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/400104\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":400105,\"stepDate\":\"2023-04-05\",\"billingAccountIdRefs\":[{\"id\":13434,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/13434\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"}],\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"},\"status\":\"PENDING\",\"stepTypeCode\":\"SUS\",\"languageCode\":\"en\",\"assignedAgentId\":\"parry.sound\",\"priority\":\"TOP\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"fddfcb\",\"comment\":\"comment 400105\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/400105\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"}]",
    	         objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionTreatmentStep.class));
    }else{
        
          String encodeAssignedTeam=null;
          String encodedStatus=null;
            if(assignedTeam!=null)
            {
                 encodeAssignedTeam = URLEncoder.encode(assignedTeam, "UTF-8");
            }else{
                encodeAssignedTeam=assignedTeam;
            }
            if(status!=null)
            {
                 encodedStatus = URLEncoder.encode(status, "UTF-8");
            }else{
                encodedStatus=status;
            }
            
             logger.info("::::::::Status ::::::::"+encodedStatus);

            logger.info("::::::::Calling Get Coll Treatment step data endpoint call ::::::::");
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(collectionTreatmentEndPointUrl+URIConstant.ApiMapping.GET_COLLECTION_TREATMENT_STEP)
                    .queryParam("collectionTreatmentStepId", collectionTreatmentStepId)
                    .queryParam("collectionEntityId",collectionEntityId )
                    .queryParam("type",type)
                    .queryParam("createdDate",createdDate)
                    .queryParam("createdBy",createdBy)
                    .queryParam("status",encodedStatus)
                    .queryParam("assignedAgentId",assignedAgentId)
                    .queryParam("assignedTeam",encodeAssignedTeam)
                    .queryParam("offset",offset)
                    .queryParam("limit",limit)
                    .queryParam("fields",fields);
                     URI uri = builder.build(false).toUri();
                  
                   logger.info("Calling Url---"+ builder.toUriString());
        
         
                  ResponseEntity<String> responseFromTelus = telusAPIConnectivityService.executeTelusAPIAndGetResponseWithHeaderForSpecialChar(null,uri, HttpMethod.GET, collTreatmentSvcAuthScope);
                       
                       
            String result=responseFromTelus.getBody();
            HttpHeaders headers1=responseFromTelus.getHeaders();
            String totalNoOfElement=headers1.getFirst("x-total-count");
            logger.info("::::::::Get Coll Treatment step data endpoint call success ::::::::");
            logger.info("Response---"+ result);
             collectionTreatmentStepList= objectMapper.readValue(result, new TypeReference<List<CollectionTreatmentStep>>(){});
                         List<CollectionTreatmentStepResponse> collectionTreatmentStepResponseList=new ArrayList<>();
                         collectionTreatmentStepResponseList=  convertTelusApiResponseToFawbCustomResponseForCollTStep(collectionTreatmentStepList,Integer.parseInt(totalNoOfElement));

            logger.info("::::::::Get Coll Treatment step data endpoint call success ::::::::");
            
             collectionTreatmentStepResponseList.stream().forEach(a->a.setAssignedAgentId(commonUtilityService.getNameUsingEmpId(a.getAssignedAgentId())));
             collectionTreatmentStepResponseList.stream().forEach(a->a.getAuditInfo().setCreatedBy(commonUtilityService.getNameUsingEmpId(a.getAuditInfo().getCreatedBy())));
       
            return collectionTreatmentStepResponseList;
        }

    }
    
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionTreatmentStep getCollectionTreatmentStepById(@PathVariable("id") String id, String partitionKey) throws Exception {
    	return objectMapper.readValue("{\"id\":100102,\"collectionTreatment\":{\"id\":4001,\"name\":\"Treatment for Air Canada\",\"href\":\"href\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"CollectionTreatment\",\"@schemaLocation\":\"href\"},\"stepDate\":\"2023-03-04\",\"stepTypeCode\":\"SOS\",\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13 14:30:45\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30 14:30:45\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"href\"},\"billingAccountIdRefs\":[{\"id\":\"72907342\",\"href\":\"{BASE_URL}/billingAccountId/72907342\",\"name\":\"Air Canada Toronto office\",\"@baseType\":\"billingAccountId\",\"@referredType\":\"billingAccountId\",\"@schemaLocation\":\"{BASE_URL}/schema/BillingAccount.swagger.json\",\"@type\":\"billingAccountId\"}],\"manualStepIndicator\":false,\"status\":\"PENDING\",\"languageCode\":\"en\",\"assignedAgentId\":\"909123\",\"priority\":\"TOP\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"Offshore\",\"additionalCharacteristics\":[{\"name\":\"Dry run\",\"value\":\"Yes\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"href\"},{\"name\":\"Region\",\"value\":\"GTA\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"href\"}],\"comment\":\"This is a collection treatment step\",\"href\":\"href\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"href\"}",CollectionTreatmentStep.class);
    }
    
    public CollectionTreatmentStep addCollectionTreatmentStep(CollectionTreatmentStepCreate collectionTreatmentStepCreate ) throws Exception  {
               objectMapper.setSerializationInclusion(Include.NON_EMPTY);

        CollectionTreatmentStep collectionTreatmentStep = new CollectionTreatmentStep();
        String requestPayload = objectMapper.writeValueAsString(collectionTreatmentStepCreate);
        logger.info(":::::Before calling Create coll treatment step- RequestPayload :::",requestPayload);
        String responseStr = telusAPIConnectivityService.executeTelusAPI(requestPayload, collectionTreatmentEndPointUrl+URIConstant.ApiMapping.GET_COLLECTION_TREATMENT_STEP,
                "POST",collTreatmentSvcAuthScope);
        logger.info("::::::::Response from Success Telus  API- Create coll treatment step:::::\n::::::: {}",responseStr);
        collectionTreatmentStep = objectMapper.readValue(responseStr,
                CollectionTreatmentStep.class);
        return collectionTreatmentStep;
    }
    
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PATCH)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionTreatmentStep updateCollectionTreatmentStep(@PathVariable("id") String id, String partitionKey,String collectionEntityId, CollectionTreatmentStepUpdate  collectionTreatmentStepUpdate) throws Exception {
        
        List<CollectionTreatmentStep> collectionTreatmentStepList = new ArrayList();
        
        //GET Api call to get existing Collection treatment oblject
         UriComponentsBuilder builder1 = UriComponentsBuilder.fromHttpUrl(collectionTreatmentEndPointUrl+URIConstant.ApiMapping.GET_COLLECTION_TREATMENT_STEP)
                    .queryParam("collectionTreatmentStepId", id)
                    .queryParam("collectionEntityId",collectionEntityId );
                    logger.info("Calling Url---"+ builder1.toUriString());
            String responseStr1 = telusAPIConnectivityService.executeTelusAPI(null,builder1.toUriString(), HttpMethod.GET, collTreatmentSvcAuthScope);
            logger.info("::::::::Get Coll Treatment step data endpoint call success ::::::::");
            logger.info("Resoinse---"+ responseStr1);
             collectionTreatmentStepList= objectMapper.readValue(responseStr1, new TypeReference<List<CollectionTreatmentStep>>(){});
           

        CollectionTreatmentStep collectionTreatmentStep = new CollectionTreatmentStep();
        if(!collectionTreatmentStepList.isEmpty())
        {
             collectionTreatmentStep = collectionTreatmentStepList.get(0);
        }
        collectionTreatmentStepUpdate.setId(BigDecimal.valueOf(Long.parseLong(id)));
           
           
          
           //Patch Api call to get update Collection treatment oblject
           UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(collectionTreatmentEndPointUrl+URIConstant.ApiMapping.GET_COLLECTION_TREATMENT_STEP+"/"+id)
                .queryParam("partitionKey",collectionTreatmentStep.getPartitionKey().toString());
        String requestPayload = objectMapper.writeValueAsString(collectionTreatmentStepUpdate);
        logger.info(":::::Before calling Update coll treatment step- RequestPayload :::",requestPayload);
        String responseStr = telusAPIConnectivityService.executeTelusAPI(requestPayload, builder.toUriString(),
                "PATCH",collTreatmentSvcAuthScope);
        logger.info("::::::::Response from Success Telus  API- Update coll treatment step:::::\n::::::: {}",responseStr);
         collectionTreatmentStep = objectMapper.readValue(responseStr,
                 CollectionTreatmentStep.class);
        
        return collectionTreatmentStep;
    }
    

    
    
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public List<CollectionActivityLogRes> getCollectionActivityLog(Integer collectionEntityId, String businessEntityEventType,String relatedBusinessEntitySubType, String relatedBusinessEntityId,String relatedBusinessEntityType, String relatedBusinessEntityStatus,String relatedBusinessEntityCreatedDate, String relatedBusinessEntityCreatedBy, String relatedBusinessEntityAssignedTo, String relatedBusinessEntityAssignedTeam,String fields, Integer offset, Integer limit) throws Exception  {
       if (isStubEnabled) {

        return objectMapper.readValue("[{\"id\":678532,\"collectionEntity\":{\"id\":666,\"name\":\"Air Canada\",\"href\":\"{BASE_URL}/entity/666\",\"@baseType\":\"CollectionEntity\",\"@type\":\"CollectionEntity\",\"@referredType\":\"CollectionEntity\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionEntity.swagger.json\"},\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"},\"collectionActivityTimestamp\":\"2023-03-30T09:00:00.00Z\",\"relatedBusinessEntityId\":\"32465656\",\"relatedBusinessEntityType\":\"COLL\",\"relatedBusinessEntityStatus\":\"WIP\",\"relatedBusinessEntityCreatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"relatedBusinessEntityCreatedBy\":\"SYSTEM\",\"relatedBusinessEntityAssignedTo\":\"SOMEONE\",\"relatedBusinessEntityAssignedTeam\":\"COLLTEAM\",\"billingAccountIdRefs\":[{\"id\":\"72907342\",\"href\":\"{BASE_URL}/billingAccountId/72907342\",\"name\":\"Air Canada Toronto office\",\"@baseType\":\"billingAccountId\",\"@referredType\":\"billingAccountId\",\"@schemaLocation\":\"{BASE_URL}/schema/BillingAccount.swagger.json\",\"@type\":\"billingAccountId\"}],\"collectionActivityType\":\"SUSPEND\",\"activityReason\":\"Haven’t got the payment for 1 month\",\"additionalCharacteristics\":[{\"name\":\"Dry run\",\"value\":\"Yes\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"Schema\"},{\"name\":\"Region\",\"value\":\"GTA\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"Schema\"}],\"comment\":\"This has to be collected\",\"href\":\"{BASE_URL}/CollectionActivityLog/678532\",\"@baseType\":\"CollectionActivityLog\",\"@type\":\"CollectionActivityLog\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"},{\"id\":678533,\"collectionEntity\":{\"id\":666,\"name\":\"Air Canada\",\"href\":\"{BASE_URL}/entity/666\",\"@baseType\":\"CollectionEntity\",\"@type\":\"CollectionEntity\",\"@referredType\":\"CollectionEntity\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionEntity.swagger.json\"},\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"},\"collectionActivityTimestamp\":\"2023-03-30T09:00:00.00Z\",\"relatedBusinessEntityId\":\"32465656\",\"relatedBusinessEntityType\":\"COLL\",\"relatedBusinessEntityStatus\":\"WIP\",\"relatedBusinessEntityCreatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"relatedBusinessEntityCreatedBy\":\"SYSTEM\",\"relatedBusinessEntityAssignedTo\":\"SOMEONE\",\"relatedBusinessEntityAssignedTeam\":\"COLLTEAM\",\"billingAccountIdRefs\":[{\"id\":\"72907342\",\"href\":\"{BASE_URL}/billingAccountId/72907342\",\"name\":\"Air Canada Toronto office\",\"@baseType\":\"billingAccountId\",\"@referredType\":\"billingAccountId\",\"@schemaLocation\":\"{BASE_URL}/schema/BillingAccount.swagger.json\",\"@type\":\"billingAccountId\"}],\"collectionActivityType\":\"CALL-IB\",\"activityReason\":\"Haven’t got the payment for 1 month\",\"additionalCharacteristics\":[{\"name\":\"CustomerName\",\"value\":\"John\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"Schema\"},{\"name\":\"PhoneNumber\",\"value\":\"678912\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"Schema\"},{\"name\":\"CallDuration\",\"value\":\"2\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"Schema\"}],\"comment\":\"This has to be collected\",\"href\":\"{BASE_URL}/CollectionActivityLog/678532\",\"@baseType\":\"CollectionActivityLog\",\"@type\":\"CollectionActivityLog\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"},{\"id\":678544,\"collectionEntity\":{\"id\":666,\"name\":\"Air Canada\",\"href\":\"{BASE_URL}/entity/666\",\"@baseType\":\"CollectionEntity\",\"@type\":\"CollectionEntity\",\"@referredType\":\"CollectionEntity\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionEntity.swagger.json\"},\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"},\"collectionActivityTimestamp\":\"2023-03-30T09:00:00.00Z\",\"relatedBusinessEntityId\":\"32465656\",\"relatedBusinessEntityType\":\"COLL\",\"relatedBusinessEntityStatus\":\"WIP\",\"relatedBusinessEntityCreatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"relatedBusinessEntityCreatedBy\":\"SYSTEM\",\"relatedBusinessEntityAssignedTo\":\"SOMEONE\",\"relatedBusinessEntityAssignedTeam\":\"COLLTEAM\",\"billingAccountIdRefs\":[{\"id\":\"72907342\",\"href\":\"{BASE_URL}/billingAccountId/72907342\",\"name\":\"Air Canada Toronto office\",\"@baseType\":\"billingAccountId\",\"@referredType\":\"billingAccountId\",\"@schemaLocation\":\"{BASE_URL}/schema/BillingAccount.swagger.json\",\"@type\":\"billingAccountId\"}],\"collectionActivityType\":\"NOTC2-OD\",\"activityReason\":\"Haven’t got the payment for 1 month\",\"additionalCharacteristics\":[{\"name\":\"CustomerName\",\"value\":\"John\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"Schema\"},{\"name\":\"EmailAddress\",\"value\":\"john@telus.com\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"Schema\"},{\"name\":\"CallDuration\",\"value\":\"2\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"Schema\"}],\"comment\":\"This has to be collected\",\"href\":\"{BASE_URL}/CollectionActivityLog/678532\",\"@baseType\":\"CollectionActivityLog\",\"@type\":\"CollectionActivityLog\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"},{\"id\":678573,\"collectionEntity\":{\"id\":666,\"name\":\"Air Canada\",\"href\":\"{BASE_URL}/entity/666\",\"@baseType\":\"CollectionEntity\",\"@type\":\"CollectionEntity\",\"@referredType\":\"CollectionEntity\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionEntity.swagger.json\"},\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"},\"collectionActivityTimestamp\":\"2023-03-30T09:00:00.00Z\",\"relatedBusinessEntityId\":\"32465656\",\"relatedBusinessEntityType\":\"COLL\",\"relatedBusinessEntityStatus\":\"WIP\",\"relatedBusinessEntityCreatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"relatedBusinessEntityCreatedBy\":\"SYSTEM\",\"relatedBusinessEntityAssignedTo\":\"SOMEONE\",\"relatedBusinessEntityAssignedTeam\":\"COLLTEAM\",\"billingAccountIdRefs\":[{\"id\":\"72907342\",\"href\":\"{BASE_URL}/billingAccountId/72907342\",\"name\":\"Air Canada Toronto office\",\"@baseType\":\"billingAccountId\",\"@referredType\":\"billingAccountId\",\"@schemaLocation\":\"{BASE_URL}/schema/BillingAccount.swagger.json\",\"@type\":\"billingAccountId\"}],\"collectionActivityType\":\"NOTC1-PMTR\",\"activityReason\":\"Haven’t got the payment for 1 month\",\"additionalCharacteristics\":[{\"name\":\"CustomerName\",\"value\":\"John\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"Schema\"},{\"name\":\"Email\",\"value\":\"john@telus.com\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"Schema\"},{\"name\":\"CallDuration\",\"value\":\"2\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"Schema\"}],\"comment\":\"This has to be collected\",\"href\":\"{BASE_URL}/CollectionActivityLog/678532\",\"@baseType\":\"CollectionActivityLog\",\"@type\":\"CollectionActivityLog\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"},{\"id\":678533,\"collectionEntity\":{\"id\":666,\"name\":\"Air Canada\",\"href\":\"{BASE_URL}/entity/666\",\"@baseType\":\"CollectionEntity\",\"@type\":\"CollectionEntity\",\"@referredType\":\"CollectionEntity\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionEntity.swagger.json\"},\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"},\"collectionActivityTimestamp\":\"2023-03-30T09:00:00.00Z\",\"relatedBusinessEntityId\":\"32465656\",\"relatedBusinessEntityType\":\"COLL\",\"relatedBusinessEntityStatus\":\"WIP\",\"relatedBusinessEntityCreatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"relatedBusinessEntityCreatedBy\":\"SYSTEM\",\"relatedBusinessEntityAssignedTo\":\"SOMEONE\",\"relatedBusinessEntityAssignedTeam\":\"COLLTEAM\",\"billingAccountIdRefs\":[{\"id\":\"72907342\",\"href\":\"{BASE_URL}/billingAccountId/72907342\",\"name\":\"Air Canada Toronto office\",\"@baseType\":\"billingAccountId\",\"@referredType\":\"billingAccountId\",\"@schemaLocation\":\"{BASE_URL}/schema/BillingAccount.swagger.json\",\"@type\":\"billingAccountId\"}],\"collectionActivityType\":\"CALL-OB\",\"activityReason\":\"Haven’t got the payment for 1 month\",\"additionalCharacteristics\":[{\"name\":\"ReachedCustomer\",\"value\":\"Y\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"Schema\"},{\"name\":\"PhoneNumber\",\"value\":\"345267\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"Schema\"}],\"comment\":\"This has to be collected\",\"href\":\"{BASE_URL}/CollectionActivityLog/678532\",\"@baseType\":\"CollectionActivityLog\",\"@type\":\"CollectionActivityLog\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"},{\"id\":678533,\"collectionEntity\":{\"id\":666,\"name\":\"Air Canada\",\"href\":\"{BASE_URL}/entity/666\",\"@baseType\":\"CollectionEntity\",\"@type\":\"CollectionEntity\",\"@referredType\":\"CollectionEntity\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionEntity.swagger.json\"},\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"},\"collectionActivityTimestamp\":\"2023-03-30T09:00:00.00Z\",\"relatedBusinessEntityId\":\"32465656\",\"relatedBusinessEntityType\":\"COLL\",\"relatedBusinessEntityStatus\":\"WIP\",\"relatedBusinessEntityCreatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"relatedBusinessEntityCreatedBy\":\"SYSTEM\",\"relatedBusinessEntityAssignedTo\":\"SOMEONE\",\"relatedBusinessEntityAssignedTeam\":\"COLLTEAM\",\"billingAccountIdRefs\":[{\"id\":\"72907342\",\"href\":\"{BASE_URL}/billingAccountId/72907342\",\"name\":\"Air Canada Toronto office\",\"@baseType\":\"billingAccountId\",\"@referredType\":\"billingAccountId\",\"@schemaLocation\":\"{BASE_URL}/schema/BillingAccount.swagger.json\",\"@type\":\"billingAccountId\"}],\"collectionActivityType\":\"CALL-OB\",\"activityReason\":\"Haven’t got the payment for 1 month\",\"additionalCharacteristics\":[{\"name\":\"ReachedCustomer\",\"value\":\"N\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"Schema\"},{\"name\":\"Outcome\",\"value\":\"left voicemail\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"Schema\"},{\"name\":\"PhoneNumber\",\"value\":\"54672\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"Schema\"}],\"comment\":\"This has to be collected\",\"href\":\"{BASE_URL}/CollectionActivityLog/678532\",\"@baseType\":\"CollectionActivityLog\",\"@type\":\"CollectionActivityLog\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"},{\"id\":678533,\"collectionEntity\":{\"id\":666,\"name\":\"Air Canada\",\"href\":\"{BASE_URL}/entity/666\",\"@baseType\":\"CollectionEntity\",\"@type\":\"CollectionEntity\",\"@referredType\":\"CollectionEntity\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionEntity.swagger.json\"},\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"},\"collectionActivityTimestamp\":\"2023-03-30T09:00:00.00Z\",\"relatedBusinessEntityId\":\"32465656\",\"relatedBusinessEntityType\":\"COLL\",\"relatedBusinessEntityStatus\":\"WIP\",\"relatedBusinessEntityCreatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"relatedBusinessEntityCreatedBy\":\"SYSTEM\",\"relatedBusinessEntityAssignedTo\":\"SOMEONE\",\"relatedBusinessEntityAssignedTeam\":\"COLLTEAM\",\"billingAccountIdRefs\":[{\"id\":\"72907342\",\"href\":\"{BASE_URL}/billingAccountId/72907342\",\"name\":\"Air Canada Toronto office\",\"@baseType\":\"billingAccountId\",\"@referredType\":\"billingAccountId\",\"@schemaLocation\":\"{BASE_URL}/schema/BillingAccount.swagger.json\",\"@type\":\"billingAccountId\"}],\"collectionActivityType\":\"EM-IN\",\"activityReason\":\"Haven’t got the payment for 1 month\",\"additionalCharacteristics\":[{\"name\":\"EmailAddress\",\"value\":\"john@telus.com\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"Schema\"},{\"name\":\"Region\",\"value\":\"GTA\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"Schema\"}],\"comment\":\"This has to be collected\",\"href\":\"{BASE_URL}/CollectionActivityLog/678532\",\"@baseType\":\"CollectionActivityLog\",\"@type\":\"CollectionActivityLog\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"},{\"id\":678533,\"collectionEntity\":{\"id\":666,\"name\":\"Air Canada\",\"href\":\"{BASE_URL}/entity/666\",\"@baseType\":\"CollectionEntity\",\"@type\":\"CollectionEntity\",\"@referredType\":\"CollectionEntity\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionEntity.swagger.json\"},\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"},\"collectionActivityTimestamp\":\"2023-03-30T09:00:00.00Z\",\"relatedBusinessEntityId\":\"32465656\",\"relatedBusinessEntityType\":\"COLL\",\"relatedBusinessEntityStatus\":\"WIP\",\"relatedBusinessEntityCreatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"relatedBusinessEntityCreatedBy\":\"SYSTEM\",\"relatedBusinessEntityAssignedTo\":\"SOMEONE\",\"relatedBusinessEntityAssignedTeam\":\"COLLTEAM\",\"billingAccountIdRefs\":[{\"id\":\"72907342\",\"href\":\"{BASE_URL}/billingAccountId/72907342\",\"name\":\"Air Canada Toronto office\",\"@baseType\":\"billingAccountId\",\"@referredType\":\"billingAccountId\",\"@schemaLocation\":\"{BASE_URL}/schema/BillingAccount.swagger.json\",\"@type\":\"billingAccountId\"}],\"collectionActivityType\":\"FOLLOWUP\",\"activityReason\":\"Haven’t got the payment for 1 month\",\"additionalCharacteristics\":[{\"name\":\"Dry run\",\"value\":\"Yes\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"Schema\"},{\"name\":\"Region\",\"value\":\"GTA\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"Schema\"}],\"comment\":\"This has to be collected\",\"href\":\"{BASE_URL}/CollectionActivityLog/678532\",\"@baseType\":\"CollectionActivityLog\",\"@type\":\"CollectionActivityLog\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"}]",
                objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionActivityLog.class));

        } else {
            
            List<CollectionActivityLog> collectionActivityLogRes= new ArrayList<>();
           String  totalNoOfElement=null;
            
            if(collectionEntityId!=null){
                
                String encodeAssignedTeam=null;
                String encodedStatus=null;
            if(relatedBusinessEntityAssignedTeam!=null)
            {
                 encodeAssignedTeam = URLEncoder.encode(relatedBusinessEntityAssignedTeam, "UTF-8");
            }else{
                encodeAssignedTeam=relatedBusinessEntityAssignedTeam;
            }
            
            if(relatedBusinessEntityStatus!=null)
            {
                 encodedStatus = URLEncoder.encode(relatedBusinessEntityStatus, "UTF-8");
            }else{
                encodedStatus=relatedBusinessEntityStatus;
            }


            logger.info("::::::::Calling  Coll Activity log endpoint call ::::::::");
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(collectionTreatmentEndPointUrl + URIConstant.ApiMapping.GET_COLL_ACTIVITY_LOG)
                   .queryParam("collectionEntityId", collectionEntityId)
                    .queryParam("businessEntityEventType", businessEntityEventType)
                    .queryParam("relatedBusinessEntitySubType", relatedBusinessEntitySubType)
                    .queryParam("relatedBusinessEntityId", relatedBusinessEntityId)
                    .queryParam("relatedBusinessEntityType", relatedBusinessEntityType)
                    .queryParam("relatedBusinessEntityStatus", encodedStatus)
                    .queryParam("relatedBusinessEntityCreatedDate", relatedBusinessEntityCreatedDate)
                    .queryParam("relatedBusinessEntityCreatedBy", relatedBusinessEntityCreatedBy)
                    .queryParam("relatedBusinessEntityAssignedTo", relatedBusinessEntityAssignedTo)
                    .queryParam("relatedBusinessEntityAssignedTeam", encodeAssignedTeam)
                    .queryParam("relatedBusinessEntityId", relatedBusinessEntityId)
                    .queryParam("fields", fields)
                    .queryParam("offset", offset)
                    .queryParam("limit",limit);
                   

            // String responseStr = telusAPIConnectivityService.executeTelusAPI(null, builder.toUriString(), HttpMethod.GET, collTreatmentSvcAuthScope);
            ResponseEntity<String> responseFromTelus = telusAPIConnectivityService.executeTelusAPIAndGetResponseWithHeader(null, builder.toUriString(), HttpMethod.GET, collTreatmentSvcAuthScope);
                String result = responseFromTelus.getBody();
                HttpHeaders headers1 = responseFromTelus.getHeaders();
                totalNoOfElement = headers1.getFirst("x-total-count");
            logger.info("::::::::Coll Activity log endpoint call success ::::::::");
            logger.info("Coll Activity log Resoinse---" + result);
            //  return objectMapper.readValue(responseStr, CollectionTreatmentStep.class);
           collectionActivityLogRes= objectMapper.readValue(result, new TypeReference<List<CollectionActivityLog>>() {

           });
            }
            
            for (CollectionActivityLog collectionActivityLog : collectionActivityLogRes) {
            	if(collectionActivityLog.getCollectionActivityPerformedBy().equals("tcm-collections-parr-eval-batch")) {
            		collectionActivityLog.setCollectionActivityPerformedBy(collectionActivityLog.getCollectionActivityPerformedBy());
            	}else if(collectionActivityLog.getCollectionActivityPerformedBy().equals("system")){
            	  collectionActivityLog.setCollectionActivityPerformedBy(collectionActivityLog.getCollectionActivityPerformedBy());

            	}
            	else {
            		collectionActivityLog.setCollectionActivityPerformedBy(commonUtilityService.getNameUsingEmpId(collectionActivityLog.getCollectionActivityPerformedBy()));
            	}
            	
            	if(collectionActivityLog.getRelatedBusinessEntityCreatedBy().equals("tcm-collections-parr-eval-batch")) {
            		collectionActivityLog.setRelatedBusinessEntityCreatedBy(collectionActivityLog.getRelatedBusinessEntityCreatedBy());
            	}else if(collectionActivityLog.getRelatedBusinessEntityCreatedBy().equals("system")){
            	   collectionActivityLog.setRelatedBusinessEntityCreatedBy(collectionActivityLog.getRelatedBusinessEntityCreatedBy());

            	}
            	else {
            		collectionActivityLog.setRelatedBusinessEntityCreatedBy(commonUtilityService.getNameUsingEmpId(collectionActivityLog.getRelatedBusinessEntityCreatedBy()));
            	}
            	
            	collectionActivityLog.setRelatedBusinessEntityAssignedTo(commonUtilityService.getNameUsingEmpId(collectionActivityLog.getRelatedBusinessEntityAssignedTo()));
			}
        //collectionActivityLogRes.stream().forEach(a->a.setRelatedBusinessEntityCreatedBy(commonUtilityService.getNameUsingEmpId(a.getRelatedBusinessEntityCreatedBy())));
       // collectionActivityLogRes.stream().forEach(a->a.setRelatedBusinessEntityAssignedTo(commonUtilityService.getNameUsingEmpId(a.getRelatedBusinessEntityAssignedTo())));
        //collectionActivityLogRes.stream().forEach(a->a.setCollectionActivityPerformedBy(commonUtilityService.getNameUsingEmpId(a.getCollectionActivityPerformedBy())));

            return convertTelusResToFawb(collectionActivityLogRes,Integer.parseInt(totalNoOfElement));
           // return collectionActivityLogRes;
        }
    }
    
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionActivityLog getCollectionActivityLogById(@PathVariable("id") String id, String partitionKey) throws Exception {
    	return objectMapper.readValue("{\"id\":678532,\"collectionEntity\":{\"id\":666,\"name\":\"Air Canada\",\"href\":\"{BASE_URL}/entity/666\",\"@baseType\":\"CollectionEntity\",\"@type\":\"CollectionEntity\",\"@referredType\":\"CollectionEntity\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionEntity.swagger.json\"},\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13 14:30:45\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30 14:30:45\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"schemaLocation\"},\"collectionActivityCompletionTimestamp\":\"2023-03-30 14:30:45\",\"relatedBusinessEntityId\":\"32465656\",\"relatedBusinessEntityType\":\"COLL\",\"relatedBusinessEntityStatus\":\"WIP\",\"relatedBusinessEntityCreatedTimestamp\":\"2023-03-30 14:30:45\",\"relatedBusinessEntityCreatedBy\":\"SYSTEM\",\"relatedBusinessEntityAssignedTo\":\"SOMEONE\",\"relatedBusinessEntityAssignedTeam\":\"COLLTEAM\",\"billingAccountIdRefs\":[{\"id\":\"72907342\",\"href\":\"{BASE_URL}/billingAccountId/72907342\",\"name\":\"Air Canada Toronto office\",\"@baseType\":\"billingAccountId\",\"@referredType\":\"billingAccountId\",\"@schemaLocation\":\"{BASE_URL}/schema/BillingAccount.swagger.json\",\"@type\":\"billingAccountId\"}],\"collectionActivityType\":\"SUSPEND\",\"activityReason\":\"Haven’t got the payment for 1 month\",\"additionalCharacteristics\":[{\"name\":\"Dry run\",\"value\":\"Yes\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"schemaLocation\"},{\"name\":\"Region\",\"value\":\"GTA\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"schemaLocation\"}],\"comment\":\"This has to be collected\",\"href\":\"{BASE_URL}/CollectionActivityLog/678532\",\"@baseType\":\"CollectionActivityLog\",\"@type\":\"CollectionActivityLog\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"}",CollectionActivityLog.class);
    }
    
    public CollectionActivityLog  addCollectionActivityLog(CollectionActivityLog collectionActivityLog ) throws Exception  {
        return collectionActivityLog;
    }
    
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PATCH)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionActivityLog updateCollectionActivityLog(@PathVariable("id") String id, CollectionActivityLog  collectionActivityLog) throws Exception {
        return collectionActivityLog;
    }
    
     
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public List<OrderMgmtHistoryResponse> getCollectionHistoryView(Integer collectionEntityId, String collectionActivityType, String relatedBusinessEntityId,String relatedBusinessEntityType, String relatedBusinessEntityStatus,String relatedBusinessEntityCreatedDate, String relatedBusinessEntityCreatedBy, String relatedBusinessEntityAssignedTo, String relatedBusinessEntityAssignedTeam,String fields, Integer offset, Integer limit) throws Exception {
                logger.info("Entering in od history");

        List<OrderMgmtHistoryResponse> orderMgmtHistoryResponseList = new ArrayList<>();
        List<String> banIds = new ArrayList<>();
        List<String> banRefIds = new ArrayList<>();
       // List<CollectionTreatmentStep> collectionTreatmentStep = getCollectionTreatmentStep(false, id, null, null, null, null, null, null, null, null, null, null);
        
                List<CollectionActivityLogRes> collectionActivityLogList = getCollectionActivityLog(collectionEntityId, collectionActivityType,null,relatedBusinessEntityId, relatedBusinessEntityType, relatedBusinessEntityStatus, relatedBusinessEntityCreatedDate, relatedBusinessEntityCreatedBy, relatedBusinessEntityAssignedTo, relatedBusinessEntityAssignedTeam, fields, offset, limit);


       if (!collectionActivityLogList.isEmpty()) {
            for (CollectionActivityLogRes collectionActivityLog : collectionActivityLogList) {
                OrderMgmtHistoryResponse orderMgmtHistoryResponse = new OrderMgmtHistoryResponse();
                if(collectionActivityLog.getBillingAccountIdRefs()!=null) {
                    banIds = collectionActivityLog.getBillingAccountIdRefs().stream().map(a -> a.getId().toString()).collect(Collectors.toList());
                    
                    logger.info("Ban IDs for History"+banIds);

                    List<CollectionBillingAccountRef> billingAccountRef = collectionEntityService.getBillingAccountRef(null, null, null, null, null, String.join(",", banIds));
                  //  banRefIds = billingAccountRef.stream().map(a -> a.getId()).collect(Collectors.toList());
                    List<BillingAccount> billingAccountRefIds = billingAccountRef.stream().map(a -> a.getBillingAccount()).collect(Collectors.toList());
                    banRefIds =billingAccountRefIds.stream().map(a->a.getId()).collect(Collectors.toList());



                }

                orderMgmtHistoryResponse.setActionId(collectionActivityLog.getRelatedBusinessEntityId());
                orderMgmtHistoryResponse.setStatus(collectionActivityLog.getRelatedBusinessEntityStatus());
                orderMgmtHistoryResponse.setAssignedTo(collectionActivityLog.getRelatedBusinessEntityAssignedTo());
                orderMgmtHistoryResponse.setGetAssignedTeam(collectionActivityLog.getRelatedBusinessEntityAssignedTeam());
                orderMgmtHistoryResponse.setUpdatedBy(collectionActivityLog.getCollectionActivityPerformedBy());
                orderMgmtHistoryResponse.setCreatedBy(collectionActivityLog.getRelatedBusinessEntityCreatedBy());

                orderMgmtHistoryResponse.setUpdatedOn(collectionActivityLog.getCollectionActivityTimestamp().toString());
               if(relatedBusinessEntityType.equalsIgnoreCase("CollectionTreatmentStep")) {
                      orderMgmtHistoryResponse.setDueDate(collectionActivityLog.getRelatedBusinessEntityDueDate().toString());
                }
                  orderMgmtHistoryResponse.setComment(collectionActivityLog.getComment());
                orderMgmtHistoryResponse.setEventType(collectionActivityLog.getBusinessEntityEventType());
                orderMgmtHistoryResponse.setBanList(banRefIds);

                orderMgmtHistoryResponseList.add(orderMgmtHistoryResponse);


            }
       }

        return orderMgmtHistoryResponseList;
    }
    
    
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public List<OrderMgmtHistoryResponse> getDisputeHistoryView(Integer collectionEntityId, String collectionActivityType, String relatedBusinessEntityId,String relatedBusinessEntityType, String relatedBusinessEntityStatus,String relatedBusinessEntityCreatedDate, String relatedBusinessEntityCreatedBy, String relatedBusinessEntityAssignedTo, String relatedBusinessEntityAssignedTeam,String fields, Integer offset, Integer limit) throws Exception {
                logger.info("Entering in getDisputeHistory");

        List<OrderMgmtHistoryResponse> orderMgmtHistoryResponseList = new ArrayList<>();
  
       // List<CollectionTreatmentStep> collectionTreatmentStep = getCollectionTreatmentStep(false, id, null, null, null, null, null, null, null, null, null, null);
        
       List<CollectionActivityLogRes> collectionActivityLogList = getCollectionActivityLog(collectionEntityId, collectionActivityType,null,relatedBusinessEntityId, relatedBusinessEntityType, relatedBusinessEntityStatus, relatedBusinessEntityCreatedDate, relatedBusinessEntityCreatedBy, relatedBusinessEntityAssignedTo, relatedBusinessEntityAssignedTeam, fields, offset, limit);


       if (!collectionActivityLogList.isEmpty()) {
            for (CollectionActivityLogRes collectionActivityLog : collectionActivityLogList) {
                OrderMgmtHistoryResponse orderMgmtHistoryResponse = new OrderMgmtHistoryResponse();
                
                orderMgmtHistoryResponse.setActionId(collectionActivityLog.getRelatedBusinessEntityId());
                orderMgmtHistoryResponse.setStatus(collectionActivityLog.getRelatedBusinessEntityStatus());
                orderMgmtHistoryResponse.setAssignedTo(collectionActivityLog.getRelatedBusinessEntityAssignedTo());
                orderMgmtHistoryResponse.setGetAssignedTeam(collectionActivityLog.getRelatedBusinessEntityAssignedTeam());
                orderMgmtHistoryResponse.setUpdatedBy(collectionActivityLog.getCollectionActivityPerformedBy());
                orderMgmtHistoryResponse.setCreatedBy(collectionActivityLog.getRelatedBusinessEntityCreatedBy());
                orderMgmtHistoryResponse.setUpdatedOn(collectionActivityLog.getCollectionActivityTimestamp().toString());
                orderMgmtHistoryResponse.setComment(collectionActivityLog.getComment());
                orderMgmtHistoryResponse.setEventType(collectionActivityLog.getBusinessEntityEventType());
                orderMgmtHistoryResponseList.add(orderMgmtHistoryResponse);
            }
       }

        return orderMgmtHistoryResponseList;
    }
    
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public List<OrderMgmtHistoryResponse> getParrHistoryView(Integer collectionEntityId, String collectionActivityType, String relatedBusinessEntityId,String relatedBusinessEntityType, String relatedBusinessEntityStatus,String relatedBusinessEntityCreatedDate, String relatedBusinessEntityCreatedBy, String relatedBusinessEntityAssignedTo, String relatedBusinessEntityAssignedTeam,String fields, Integer offset, Integer limit) throws Exception {
                logger.info("Entering in getParrHistoryView");

        List<OrderMgmtHistoryResponse> orderMgmtHistoryResponseList = new ArrayList<>();
  
       // List<CollectionTreatmentStep> collectionTreatmentStep = getCollectionTreatmentStep(false, id, null, null, null, null, null, null, null, null, null, null);
        
       List<CollectionActivityLogRes> collectionActivityLogList = getCollectionActivityLog(collectionEntityId, collectionActivityType,null,relatedBusinessEntityId, relatedBusinessEntityType, relatedBusinessEntityStatus, relatedBusinessEntityCreatedDate, relatedBusinessEntityCreatedBy, relatedBusinessEntityAssignedTo, relatedBusinessEntityAssignedTeam, fields, offset, limit);


       if (!collectionActivityLogList.isEmpty()) {
            for (CollectionActivityLogRes collectionActivityLog : collectionActivityLogList) {
                OrderMgmtHistoryResponse orderMgmtHistoryResponse = new OrderMgmtHistoryResponse();
                
                orderMgmtHistoryResponse.setActionId(collectionActivityLog.getRelatedBusinessEntityId());
                orderMgmtHistoryResponse.setStatus(collectionActivityLog.getRelatedBusinessEntityStatus());
                orderMgmtHistoryResponse.setAssignedTo(collectionActivityLog.getRelatedBusinessEntityAssignedTo());
                orderMgmtHistoryResponse.setGetAssignedTeam(collectionActivityLog.getRelatedBusinessEntityAssignedTeam());
                orderMgmtHistoryResponse.setUpdatedBy(collectionActivityLog.getCollectionActivityPerformedBy());
                orderMgmtHistoryResponse.setCreatedBy(collectionActivityLog.getRelatedBusinessEntityCreatedBy());
                orderMgmtHistoryResponse.setUpdatedOn(collectionActivityLog.getCollectionActivityTimestamp().toString());
                orderMgmtHistoryResponse.setComment(collectionActivityLog.getComment());
                orderMgmtHistoryResponse.setEventType(collectionActivityLog.getBusinessEntityEventType());
                orderMgmtHistoryResponseList.add(orderMgmtHistoryResponse);
            }
       }

        return orderMgmtHistoryResponseList;
    }
    
    
    
 private List<CollectionTreatmentStepResponse> convertTelusApiResponseToFawbCustomResponseForCollTStep(List<CollectionTreatmentStep> collectionTreatmentStepList,Integer totalNoOfElement) {
        List<CollectionTreatmentStepResponse> collectionTreatmentStepResponseList=new ArrayList<>();
        for(CollectionTreatmentStep collectionTreatmentStep:collectionTreatmentStepList)
        {
            CollectionTreatmentStepResponse collectionTreatmentStepResponse=new CollectionTreatmentStepResponse();
            collectionTreatmentStepResponse.setAuditInfo(collectionTreatmentStep.getAuditInfo());
            collectionTreatmentStepResponse.setChannel(collectionTreatmentStep.getChannel());
            collectionTreatmentStepResponse.setId(collectionTreatmentStep.getId());
            collectionTreatmentStepResponse.setPartitionKey(collectionTreatmentStep.getPartitionKey());
            collectionTreatmentStepResponse.setCollectionTreatment(collectionTreatmentStep.getCollectionTreatment());
            collectionTreatmentStepResponse.setStepDate(collectionTreatmentStep.getStepDate());
            collectionTreatmentStepResponse.setBillingAccountIdRefs(collectionTreatmentStep.getBillingAccountIdRefs());
            collectionTreatmentStepResponse.setManualStepIndicator(collectionTreatmentStep.isManualStepIndicator());
            collectionTreatmentStepResponse.setStatus(collectionTreatmentStep.getStatus());
            collectionTreatmentStepResponse.setStepTypeCode(collectionTreatmentStep.getStepTypeCode());
            collectionTreatmentStepResponse.setLanguageCode(collectionTreatmentStep.getLanguageCode());
            collectionTreatmentStepResponse.setAssignedAgentId(collectionTreatmentStep.getAssignedAgentId());
            collectionTreatmentStepResponse.setPriority(collectionTreatmentStep.getPriority());
            collectionTreatmentStepResponse.setReasonCode(collectionTreatmentStep.getReasonCode());
            collectionTreatmentStepResponse.setAssignedTeam(collectionTreatmentStep.getAssignedTeam());
            collectionTreatmentStepResponse.setAdditionalCharacteristics(collectionTreatmentStep.getAdditionalCharacteristics());
            collectionTreatmentStepResponse.setComment(collectionTreatmentStep.getComment());
            collectionTreatmentStepResponse.setHref(collectionTreatmentStep.getHref());
            collectionTreatmentStepResponse.setBaseType(collectionTreatmentStep.getBaseType());
            collectionTreatmentStepResponse.setType(collectionTreatmentStep.getType());
            collectionTreatmentStepResponse.setSchemaLocation(collectionTreatmentStep.getSchemaLocation());
            collectionTreatmentStepResponse.setAssignedPersonForDefaultValue(collectionTreatmentStep.getAssignedAgentId());
            collectionTreatmentStepResponse.setTotalNumberOfElement(totalNoOfElement);

            collectionTreatmentStepResponseList.add(collectionTreatmentStepResponse);

        }
        
        return collectionTreatmentStepResponseList;

    }
    
    
    
private List<CollectionActivityLogRes> convertTelusResToFawb(List<CollectionActivityLog> collectionActivityLogRes, int totalNoOfElement) {

        List<CollectionActivityLogRes> collectionActivityLogResList=new ArrayList<>();
        for(CollectionActivityLog collectionActivityLog:collectionActivityLogRes)
        {
            CollectionActivityLogRes collectionActivityLogRes1=new CollectionActivityLogRes();
            collectionActivityLogRes1.setId(collectionActivityLog.getId());
            collectionActivityLogRes1.setCollectionEntity(collectionActivityLog.getCollectionEntity());
            collectionActivityLogRes1.setCollectionActivityTimestamp(collectionActivityLog.getCollectionActivityTimestamp());
            collectionActivityLogRes1.setRelatedBusinessEntityId(collectionActivityLog.getRelatedBusinessEntityId());
            collectionActivityLogRes1.setRelatedBusinessEntityType(collectionActivityLog.getRelatedBusinessEntityType());
            collectionActivityLogRes1.setRelatedBusinessEntityStatus(collectionActivityLog.getRelatedBusinessEntityStatus());
            collectionActivityLogRes1.setRelatedBusinessEntityCreatedTimestamp(collectionActivityLog.getRelatedBusinessEntityCreatedTimestamp());
            collectionActivityLogRes1.setRelatedBusinessEntityCreatedBy(collectionActivityLog.getRelatedBusinessEntityCreatedBy());
            collectionActivityLogRes1.setRelatedBusinessEntityAssignedTo(collectionActivityLog.getRelatedBusinessEntityAssignedTo());
            collectionActivityLogRes1.setRelatedBusinessEntityAssignedTeam(collectionActivityLog.getRelatedBusinessEntityAssignedTeam());
            collectionActivityLogRes1.setRelatedBusinessEntityDueDate(collectionActivityLog.getRelatedBusinessEntityDueDate());
            collectionActivityLogRes1.setCollectionActivityPerformedBy(collectionActivityLog.getCollectionActivityPerformedBy());
            collectionActivityLogRes1.setBillingAccountIdRefs(collectionActivityLog.getBillingAccountIdRefs());
            collectionActivityLogRes1.setBusinessEntityEventType(collectionActivityLog.getBusinessEntityEventType());
            collectionActivityLogRes1.setRelatedBusinessEntitySubType(collectionActivityLog.getRelatedBusinessEntitySubType());
            collectionActivityLogRes1.setActivityReason(collectionActivityLog.getActivityReason());
            collectionActivityLogRes1.setAdditionalCharacteristics(collectionActivityLog.getAdditionalCharacteristics());
            collectionActivityLogRes1.setComment(collectionActivityLog.getComment());
            collectionActivityLogRes1.setPartitionKey(collectionActivityLog.getPartitionKey());
            collectionActivityLogRes1.setDataSourceId(collectionActivityLog.getDataSourceId());
            collectionActivityLogRes1.setHref(collectionActivityLog.getHref());
            collectionActivityLogRes1.setBaseType(collectionActivityLog.getBaseType());
            collectionActivityLogRes1.setType(collectionActivityLog.getType());
            collectionActivityLogRes1.setSchemaLocation(collectionActivityLog.getSchemaLocation());
            collectionActivityLogRes1.setTotalNoOfElement(totalNoOfElement);
            collectionActivityLogResList.add(collectionActivityLogRes1);

        }

        return collectionActivityLogResList;
    }
    

}