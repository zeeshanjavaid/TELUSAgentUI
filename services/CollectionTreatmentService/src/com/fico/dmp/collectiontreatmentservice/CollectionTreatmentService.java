/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.collectiontreatmentservice;

import java.util.List;

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

import io.swagger.client.model.CollectionActivityLog;
import io.swagger.client.model.CollectionBillingAccountRef;
import io.swagger.client.model.CollectionBillingAccountRefCreate;
import io.swagger.client.model.CollectionBillingAccountRefUpdate;
import io.swagger.client.model.CollectionTreatment;
import io.swagger.client.model.CollectionTreatmentStep;
import io.swagger.client.model.CollectionTreatmentStepCreate;
import io.swagger.client.model.CollectionTreatmentStepUpdate;

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

    private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(CollectionTreatmentService.class.getName());

    @Autowired
    private SecurityService securityService;

    private final ObjectMapper objectMapper = new ObjectMapperConfig().customObjectMapper();
    
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public List<CollectionTreatment> getCollectionTreatment(Integer collectionEntityId,String fields,Integer offset, Integer limit, Boolean history) throws Exception  {
    	return objectMapper.readValue("[{\"id\":1889,\"collectionEntity\":{\"id\":666,\"name\":\"Food Basics\",\"href\":\"{BASE_URL}/entity/666\",\"@baseType\":\"CollectionEntity\",\"@type\":\"CollectionEntity\",\"@referredType\":\"CollectionEntity\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionEntity.swagger.json\"},\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13 14:30:45\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30 14:30:45\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"https\"},\"nextStepDueDate\":\"2023-12-31\",\"collectionTreatmentSteps\":[{\"id\":68891,\"name\":\"Step 68891\",\"href\":\"{BASE_URL}/collectionTreatmentStep/68891\",\"@baseType\":\"CollectionTreatmentStep\",\"@type\":\"CollectionTreatmentStep\",\"@referredType\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"},{\"id\":68892,\"name\":\"Step 68892\",\"href\":\"{BASE_URL}/collectionTreatmentStep/68892\",\"@baseType\":\"CollectionTreatmentStep\",\"@type\":\"CollectionTreatmentStep\",\"@referredType\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"}],\"cureThreshold\":3,\"dmPathID\":\"A DM Path ID example\",\"lastAssessmentProcessType\":\"BILLCALL\",\"lastCollectionAssessmentId\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"scenarioId\":\"Scenario information 2234753222\",\"additionalCharacteristics\":[{\"name\":\"source\",\"value\":\"head office\",\"@baseType\":\"CollectionTreatmentStep\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"},{\"name\":\"assigned agent\",\"value\":\"offshore\",\"@baseType\":\"CollectionTreatmentStep\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"}]}]",
    	         objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionTreatment.class));
    	
    	
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
    
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public List<CollectionTreatmentStep> getCollectionTreatmentStep(Integer collectionTreatmentStepId, Integer collectionEntityId,String type,String createdDate,String createdBy, String status,String assignedAgentId, String assignedTeam, String fields, Integer offset, Integer limit) throws Exception  {
    	return objectMapper.readValue("[{\"id\":300102,\"stepDate\":\"2023-03-02\",\"billingAccountIdRefs\":[{\"id\":72907342,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/72907342\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":14384583,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/14384583\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":282424,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/282424\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"}],\"status\":\"PENDING\",\"stepTypeCode\":\"SUS\",\"languageCode\":\"en\",\"assignedAgentId\":\"909123\",\"priority\":\"Low\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"BC\",\"comment\":\"comment 300102\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/300102\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":300103,\"stepDate\":\"2023-03-03\",\"billingAccountIdRefs\":[{\"id\":193,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/193\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"}],\"status\":\"PENDING\",\"stepTypeCode\":\"CALL-IB\",\"languageCode\":\"en\",\"assignedAgentId\":\"909123\",\"priority\":\"Medium\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"BC\",\"comment\":\"comment 300103\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/300103\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":300104,\"stepDate\":\"2023-03-04\",\"status\":\"PENDING\",\"stepTypeCode\":\"CALL-OB\",\"languageCode\":\"en\",\"assignedAgentId\":\"888888\",\"priority\":\"High\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"LOC\",\"comment\":\"comment 300104\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/300104\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":400102,\"stepDate\":\"2023-04-02\",\"billingAccountIdRefs\":[{\"id\":4,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/4\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":58,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/58\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":6434,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/6434\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":432,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/432\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"}],\"status\":\"PENDING\",\"stepTypeCode\":\"CALL-IB\",\"languageCode\":\"en\",\"assignedAgentId\":\"888888\",\"priority\":\"Low\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"LOC\",\"comment\":\"comment 400102\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/400102\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":400103,\"stepDate\":\"2023-04-03\",\"status\":\"PENDING\",\"stepTypeCode\":\"SUS\",\"languageCode\":\"en\",\"assignedAgentId\":\"parry.sound\",\"priority\":\"TOP\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"weq\",\"comment\":\"comment 400103\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/400103\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":400104,\"stepDate\":\"2023-04-04\",\"billingAccountIdRefs\":[{\"id\":7,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/7\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":9,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/9\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":6,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/6\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"}],\"status\":\"PENDING\",\"stepTypeCode\":\"SUS\",\"languageCode\":\"en\",\"assignedAgentId\":\"parry.sound\",\"priority\":\"TOP\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"jhmmn\",\"comment\":\"comment 400104\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/400104\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":400105,\"stepDate\":\"2023-04-05\",\"billingAccountIdRefs\":[{\"id\":13434,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/13434\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"}],\"status\":\"PENDING\",\"stepTypeCode\":\"SUS\",\"languageCode\":\"en\",\"assignedAgentId\":\"parry.sound\",\"priority\":\"TOP\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"fddfcb\",\"comment\":\"comment 400105\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/400105\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"}]",
    	         objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionTreatmentStep.class));
    }
    
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionTreatmentStep getCollectionTreatmentStepById(@PathVariable("id") String id, String partitionKey) throws Exception {
    	return objectMapper.readValue("{\"id\":100102,\"collectionTreatment\":{\"id\":4001,\"name\":\"Treatment for Air Canada\",\"href\":\"href\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"CollectionTreatment\",\"@schemaLocation\":\"href\"},\"stepDate\":\"2023-03-04\",\"stepTypeCode\":\"SOS\",\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13 14:30:45\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30 14:30:45\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"href\"},\"billingAccountIdRefs\":[{\"id\":\"72907342\",\"href\":\"{BASE_URL}/billingAccountId/72907342\",\"name\":\"Air Canada Toronto office\",\"@baseType\":\"billingAccountId\",\"@referredType\":\"billingAccountId\",\"@schemaLocation\":\"{BASE_URL}/schema/BillingAccount.swagger.json\",\"@type\":\"billingAccountId\"}],\"manualStepIndicator\":false,\"status\":\"PENDING\",\"languageCode\":\"en\",\"assignedAgentId\":\"909123\",\"priority\":\"TOP\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"Offshore\",\"additionalCharacteristics\":[{\"name\":\"Dry run\",\"value\":\"Yes\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"href\"},{\"name\":\"Region\",\"value\":\"GTA\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"href\"}],\"comment\":\"This is a collection treatment step\",\"href\":\"href\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"href\"}",CollectionTreatmentStep.class);
    }
    
    public CollectionTreatmentStep addCollectionTreatmentStep(CollectionTreatmentStepCreate collectionTreatmentStepCreate ) throws Exception  {
        CollectionTreatmentStep collectionTreatmentStep = new CollectionTreatmentStep();
        return collectionTreatmentStep;
    }
    
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PATCH)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionTreatmentStep updateCollectionTreatmentStep(@PathVariable("id") String id, String partitionKey, CollectionTreatmentStepUpdate  collectionTreatmentStepUpdate) throws Exception {
        CollectionTreatmentStep collectionTreatmentStep = new CollectionTreatmentStep();
        return collectionTreatmentStep;
    }
    

    
    
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public List<CollectionActivityLog> getCollectionActivityLog(Integer collectionEntityId, String type, String category, String createdDate,String createdBy, String status, String assignedTo, String assignedTeam, String completionDate, String fields, Integer offset, Integer limit, Boolean history) throws Exception  {
    	return objectMapper.readValue("[{\"id\":678532,\"collectionEntity\":{\"id\":666,\"name\":\"Air Canada\",\"href\":\"{BASE_URL}/entity/666\",\"@baseType\":\"CollectionEntity\",\"@type\":\"CollectionEntity\",\"@referredType\":\"CollectionEntity\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionEntity.swagger.json\"},\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"},\"collectionActivityCompletionTimestamp\":\"2023-03-30T09:00:00.00Z\",\"relatedBusinessEntityId\":\"32465656\",\"relatedBusinessEntityType\":\"COLL\",\"relatedBusinessEntityStatus\":\"WIP\",\"relatedBusinessEntityCreatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"relatedBusinessEntityCreatedBy\":\"SYSTEM\",\"relatedBusinessEntityAssignedTo\":\"SOMEONE\",\"relatedBusinessEntityAssignedTeam\":\"COLLTEAM\",\"billingAccountIdRefs\":[{\"id\":\"72907342\",\"href\":\"{BASE_URL}/billingAccountId/72907342\",\"name\":\"Air Canada Toronto office\",\"@baseType\":\"billingAccountId\",\"@referredType\":\"billingAccountId\",\"@schemaLocation\":\"{BASE_URL}/schema/BillingAccount.swagger.json\",\"@type\":\"billingAccountId\"}],\"collectionActivityType\":\"SUSPEND\",\"activityReason\":\"Haven’t got the payment for 1 month\",\"additionalCharacteristics\":[{\"name\":\"Dry run\",\"value\":\"Yes\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"Schema\"},{\"name\":\"Region\",\"value\":\"GTA\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"Schema\"}],\"comment\":\"This has to be collected\",\"href\":\"{BASE_URL}/CollectionActivityLog/678532\",\"@baseType\":\"CollectionActivityLog\",\"@type\":\"CollectionActivityLog\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"}]",
    	         objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionActivityLog.class));
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

}