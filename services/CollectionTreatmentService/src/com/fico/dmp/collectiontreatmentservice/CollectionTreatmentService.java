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

import io.swagger.client.model.CollectionBillingAccountRef;
import io.swagger.client.model.CollectionBillingAccountRefCreate;
import io.swagger.client.model.CollectionBillingAccountRefUpdate;
import io.swagger.client.model.CollectionTreatment;

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
    public CollectionTreatment getCollectionTreatment(@PathVariable("id") String id, String partitionKey) throws Exception {
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

}