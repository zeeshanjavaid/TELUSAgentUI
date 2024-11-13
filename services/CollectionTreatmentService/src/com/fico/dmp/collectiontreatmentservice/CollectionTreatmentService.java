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
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fico.core.util.ObjectMapperConfig;
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
import java.util.Arrays;
import com.fico.dmp.commonutilityservice.CommonUtilityService;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fico.telus.model.CollectionTreatmentStepResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import com.fico.telus.model.CollectionActivityLogRes;

import telus.cdo.cnc.collmgmt.collactivitylogmgmt.model.CollectionActivityLog;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.BillingAccount;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.CollectionBillingAccountRef;
import telus.cdo.cnc.collmgmt.colltreatmentmgmt.model.CollectionTreatment;
import telus.cdo.cnc.collmgmt.colltreatmentmgmt.model.CollectionTreatmentStep;
import telus.cdo.cnc.collmgmt.colltreatmentmgmt.model.CollectionTreatmentStepCreate;
import telus.cdo.cnc.collmgmt.colltreatmentmgmt.model.CollectionTreatmentStepUpdate;


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

    private static final String IS_COLLTREATMENT_STUB_ENABLED = "IS_COLLTREATMENT_STUB_ENABLED";
    private static final String COLLTREATMENT_ENDPOINT_URL = "COLLTREATMENT_ENDPOINT_BASEURL";
    private static final String COLLTREATMENT_ENDPOINT_SCOPE = "COLLTREATMENT_ENDPOINT_SCOPE";

    @Autowired
    private TelusAPIConnectivityService telusAPIConnectivityService;
    @Autowired
    private CollectionEntityService collectionEntityService;
    @Autowired
    private CommonUtilityService commonUtilityService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private PropertiesUtil propertiesUtil;

    private boolean isStubEnabled;
    private String collectionTreatmentEndPointUrl;
    private String collTreatmentSvcAuthScope;
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
    public List<CollectionTreatment> getCollectionTreatment(String entityId, Boolean active, String fields, Integer offset, Integer limit, Boolean history) throws Exception {
        if (isStubEnabled) {
            return objectMapper.readValue("[{\"id\":1889,\"collectionEntity\":{\"id\":666,\"name\":\"Food Basics\",\"href\":\"{BASE_URL}/entity/666\",\"@baseType\":\"CollectionEntity\",\"@type\":\"CollectionEntity\",\"@referredType\":\"CollectionEntity\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionEntity.swagger.json\"},\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"https\"},\"nextStepDueDate\":\"2023-12-31\",\"collectionTreatmentSteps\":[{\"id\":68891,\"name\":\"Step 68891\",\"href\":\"{BASE_URL}/collectionTreatmentStep/68891\",\"@baseType\":\"CollectionTreatmentStep\",\"@type\":\"CollectionTreatmentStep\",\"@referredType\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"},{\"id\":68892,\"name\":\"Step 68892\",\"href\":\"{BASE_URL}/collectionTreatmentStep/68892\",\"@baseType\":\"CollectionTreatmentStep\",\"@type\":\"CollectionTreatmentStep\",\"@referredType\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"}],\"cureThreshold\":3,\"dmPathID\":\"A DM Path ID example\",\"lastAssessmentProcessType\":\"BILLCALL\",\"lastCollectionAssessmentId\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"scenarioId\":\"Scenario information 2234753222\",\"additionalCharacteristics\":[{\"name\":\"source\",\"value\":\"head office\",\"@baseType\":\"CollectionTreatmentStep\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"},{\"name\":\"assigned agent\",\"value\":\"offshore\",\"@baseType\":\"CollectionTreatmentStep\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"}]}]", objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionTreatment.class));
        } else {
            List<CollectionTreatment> collectionTreatmentStepList = new ArrayList<>();
            if (entityId != null) {
                logger.info(":::::::: Searching collection treatment ::::::::");
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(collectionTreatmentEndPointUrl + URIConstant.ApiMapping.GET_COLLECTION_TREATMENT);
                if (entityId != null) {
                    builder = builder.queryParam("entityId", entityId);
                }
                if (active != null) {
                    builder = builder.queryParam("active", active);
                }
                if (fields != null) {
                    builder = builder.queryParam("fields", fields);
                }
                if (offset != null) {
                    builder = builder.queryParam("offset", offset);
                }
                if (limit != null) {
                    builder = builder.queryParam("limit", limit);
                }
                if (history != null) {
                    builder = builder.queryParam("history", history);
                }
                String uriStr = builder.toUriString();
                logger.info("Calling Url: " + uriStr);
                String responseStr = telusAPIConnectivityService.executeTelusAPI(null, uriStr, HttpMethod.GET, collTreatmentSvcAuthScope);
                logger.info("::::::::Searching collection treatment success ::::::::");
                logger.info("Resoinse---" + responseStr);
                // return objectMapper.readValue(responseStr, CollectionTreatmentStep.class);
                collectionTreatmentStepList = objectMapper.readValue(responseStr, new TypeReference<List<CollectionTreatment>>() {});
            }
            return collectionTreatmentStepList;
        }
    }

    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionTreatment getCollectionTreatmentById(@PathVariable("id") String id, String partitionKey) throws Exception {
        return objectMapper.readValue("{\"id\":1889,\"collectionEntity\":{\"id\":666,\"name\":\"Food Basics\",\"href\":\"{BASE_URL}/entity/666\",\"@baseType\":\"CollectionEntity\",\"@type\":\"CollectionEntity\",\"@referredType\":\"CollectionEntity\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionEntity.swagger.json\"},\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13 14:30:45\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30 14:30:45\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"schema\"},\"nextStepDueDate\":\"2023-12-31\",\"collectionTreatmentSteps\":[{\"id\":68891,\"name\":\"Step 68891\",\"href\":\"{BASE_URL}/collectionTreatmentStep/68891\",\"@baseType\":\"CollectionTreatmentStep\",\"@type\":\"CollectionTreatmentStep\",\"@referredType\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"},{\"id\":68892,\"name\":\"Step 68892\",\"href\":\"{BASE_URL}/collectionTreatmentStep/68892\",\"@baseType\":\"CollectionTreatmentStep\",\"@type\":\"CollectionTreatmentStep\",\"@referredType\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"}],\"cureThreshold\":3,\"dmPathID\":\"A DM Path ID example\",\"lastAssessmentProcessType\":\"BILLCALL\",\"lastCollectionAssessmentId\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"scenarioId\":\"Scenario information 2234753222\",\"additionalCharacteristics\":[{\"name\":\"source\",\"value\":\"head office\",\"@baseType\":\"CollectionTreatmentStep\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"},{\"name\":\"assigned agent\",\"value\":\"offshore\",\"@baseType\":\"CollectionTreatmentStep\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"}]}", CollectionTreatment.class);
    }

    public CollectionTreatment addCollectionTreatment(CollectionTreatment collectionTreatment) throws Exception {
        return collectionTreatment;
    }

    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PATCH)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionTreatment updateCollectionTreatment(@PathVariable("id") String id, CollectionTreatment collectionTreatment) throws Exception {
        return collectionTreatment;
    }

    // @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public List<CollectionTreatmentStepResponse> getCollectionTreatmentStep(Boolean IsOdManagement, String collectionTreatmentStepId, String entityId, String typeCode, String contentTypeCode, String createdDate, String createdBy, String status, String assignedAgentId, String assignedTeam, String fields, Integer offset, Integer limit) throws Exception {
        // Commented telus live api
        List<CollectionTreatmentStep> collectionTreatmentStepList = new ArrayList<>();
        logger.info("::::::::In getCollTeatmentStep call :::::::: ", IsOdManagement);

        if (isStubEnabled) {
            return objectMapper.readValue("[{\"id\":300102,\"stepDate\":\"2023-03-02\",\"billingAccountRefs\":[{\"id\":72907342,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/72907342\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":14384583,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/14384583\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":282424,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/282424\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"}],\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"},\"status\":\"PENDING\",\"stepTypeCode\":\"SUS\",\"languageCode\":\"en\",\"assignedAgentId\":\"48154\",\"priority\":\"Low\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"TD OM CHECK 45\",\"comment\":\"comment 300102\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/300102\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":300103,\"stepDate\":\"2023-03-03\",\"billingAccountRefs\":[{\"id\":193,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/193\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"}],\"additionalCharacteristics\":[{\"name\":\"CustomerName\",\"value\":\"John\"},{\"name\":\"PhoneNumber\",\"value\":\"884585\"},{\"name\":\"CallDuration\",\"value\":\"5\"}],\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"},\"status\":\"PENDING\",\"stepTypeCode\":\"CALL-IB\",\"languageCode\":\"en\",\"assignedAgentId\":\"48154\",\"priority\":\"Medium\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"TD OM CHECK 45\",\"comment\":\"comment 300103\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/300103\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":300104,\"stepDate\":\"2023-03-04\",\"status\":\"PENDING\",\"stepTypeCode\":\"CALL-OB\",\"languageCode\":\"en\",\"assignedAgentId\":\"13\",\"priority\":\"High\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"TD OM CHECK 45\",\"comment\":\"comment 300104\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/300104\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\",\"additionalCharacteristics\":[{\"name\":\"ReachedCustomer\",\"value\":\"Y\"},{\"name\":\"Phone\",\"value\":\"124545\"}]},{\"id\":300112,\"stepDate\":\"2023-03-04\",\"status\":\"PENDING\",\"stepTypeCode\":\"CALL-OB\",\"languageCode\":\"en\",\"assignedAgentId\":\"\",\"priority\":\"High\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"TD OM CHECK 45\",\"comment\":\"comment 300104\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/300104\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\",\"additionalCharacteristics\":[{\"name\":\"ReachedCustomer\",\"value\":\"N\"},{\"name\":\"Phone\",\"value\":\"124545\"},{\"name\":\"Outcome\",\"value\":\"Left voicemail\"}],\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"}},{\"id\":300113,\"stepDate\":\"2023-03-04\",\"status\":\"PENDING\",\"stepTypeCode\":\"NOTC1-PMTR\",\"languageCode\":\"en\",\"assignedAgentId\":\"13\",\"priority\":\"High\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"TD OM CHECK 45\",\"comment\":\"comment 300104\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/300104\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\",\"additionalCharacteristics\":[{\"name\":\"CustomerName\",\"value\":\"Kiran\"},{\"name\":\"EmailAddress\",\"value\":\"john.cena@telus.com\"}],\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"}},{\"id\":3001456,\"stepDate\":\"2023-03-04\",\"status\":\"PENDING\",\"stepTypeCode\":\"NOTC2-OD\",\"languageCode\":\"en\",\"assignedAgentId\":\"13\",\"priority\":\"High\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"TD OM CHECK 45\",\"comment\":\"comment 300104\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/300104\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\",\"additionalCharacteristics\":[{\"name\":\"CustomerName\",\"value\":\"John\"},{\"name\":\"EmailAddress\",\"value\":\"john.cena@telus.com\"}],\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"}},{\"id\":300153,\"stepDate\":\"2023-03-04\",\"status\":\"PENDING\",\"stepTypeCode\":\"NOTC3-DIST\",\"languageCode\":\"en\",\"assignedAgentId\":\"13\",\"priority\":\"High\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"TD OM CHECK 45\",\"comment\":\"comment 300104\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/300104\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\",\"additionalCharacteristics\":[{\"name\":\"CustomerName\",\"value\":\"Kiran\"},{\"name\":\"EmailAddress\",\"value\":\"john.cena@telus.com\"}],\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"}},{\"id\":300223,\"stepDate\":\"2023-03-04\",\"status\":\"PENDING\",\"stepTypeCode\":\"NOTC4-CANL\",\"languageCode\":\"en\",\"assignedAgentId\":\"13\",\"priority\":\"High\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"TD OM CHECK 45\",\"comment\":\"comment 300104\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/300104\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\",\"additionalCharacteristics\":[{\"name\":\"CustomerName\",\"value\":\"Kiran\"},{\"name\":\"EmailAddress\",\"value\":\"john.cena@telus.com\"}],\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"}},{\"id\":400102,\"stepDate\":\"2023-04-02\",\"billingAccountRefs\":[{\"id\":4,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/4\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":58,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/58\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":6434,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/6434\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":432,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/432\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"}],\"additionalCharacteristics\":[{\"name\":\"EmailAddress\",\"value\":\"Kevin.snow@telus.com\"}],\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"},\"status\":\"PENDING\",\"stepTypeCode\":\"EM-IN\",\"languageCode\":\"en\",\"assignedAgentId\":\"13\",\"priority\":\"Low\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"TD OM CHECK 45\",\"comment\":\"comment 400102\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/400102\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":400213,\"stepDate\":\"2023-04-02\",\"billingAccountRefs\":[{\"id\":4,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/4\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":58,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/58\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":6434,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/6434\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":432,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/432\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"}],\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"},\"status\":\"PENDING\",\"stepTypeCode\":\"FOLLOWUP\",\"languageCode\":\"en\",\"assignedAgentId\":\"13\",\"priority\":\"Low\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"TD OM CHECK 45\",\"comment\":\"comment 400102\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/400102\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":400103,\"stepDate\":\"2023-04-03\",\"status\":\"PENDING\",\"stepTypeCode\":\"SUS\",\"languageCode\":\"en\",\"assignedAgentId\":\"parry.sound\",\"priority\":\"TOP\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"weq\",\"comment\":\"comment 400103\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/400103\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\",\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"}},{\"id\":400104,\"stepDate\":\"2023-04-04\",\"billingAccountRefs\":[{\"id\":7,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/7\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":9,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/9\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":6,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/6\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"}],\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"},\"status\":\"PENDING\",\"stepTypeCode\":\"SUS\",\"languageCode\":\"en\",\"assignedAgentId\":\"parry.sound\",\"priority\":\"TOP\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"jhmmn\",\"comment\":\"comment 400104\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/400104\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"},{\"id\":400105,\"stepDate\":\"2023-04-05\",\"billingAccountRefs\":[{\"id\":13434,\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collEntityMgmt/v1/billingAccountRef/13434\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"EntityRef\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"}],\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13T09:00:00.00Z\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30T09:00:00.00Z\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"Schema\"},\"status\":\"PENDING\",\"stepTypeCode\":\"SUS\",\"languageCode\":\"en\",\"assignedAgentId\":\"parry.sound\",\"priority\":\"TOP\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"fddfcb\",\"comment\":\"comment 400105\",\"href\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/collectionTreatmentStep/400105\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"https://apigw-public-yul-np-002.cloudapps.telus.com/collTreatmentMgmt/v1/postman/schemas/CollectionTreatment.swagger.json\"}]", objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionTreatmentStep.class));
        } else {
            String encodeAssignedTeam = null;
            String encodedStatus = null;
            if (assignedTeam != null) {
                // encodeAssignedTeam = URLEncoder.encode(assignedTeam, "UTF-8");
                encodeAssignedTeam = assignedTeam;
            } else {
                encodeAssignedTeam = assignedTeam;
            }
            if (status != null) {
                // encodedStatus = URLEncoder.encode(status, "UTF-8");
                encodedStatus = status;
            } else {
                encodedStatus = status;
            }
            String[] notcTypeCodes = { "NOTC1-PMTR", "NOTC2-OD", "NOTC3-DIST", "NOTC4-CANL", "NOTC5-ACTMGR", "NOTC6-REFERRAL" };
            logger.info("::::::::Status ::::::::" + encodedStatus);

            logger.info("::::::::Calling Get Coll Treatment step data endpoint call ::::::::");
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(collectionTreatmentEndPointUrl + URIConstant.ApiMapping.GET_COLLECTION_TREATMENT_STEP);
            if (collectionTreatmentStepId != null) {
                builder = builder.queryParam("id", collectionTreatmentStepId);
            }
            if (entityId != null) {
                builder = builder.queryParam("entityId", entityId);
            }
            if (typeCode != null) {
                if (typeCode.equalsIgnoreCase("NOTC")) {
                    typeCode = "in:" + String.join(",", notcTypeCodes);
                }
                builder = builder.queryParam("typeCode", typeCode);
            }
            if (contentTypeCode != null) {
                builder = builder.queryParam("contentTypeCode", contentTypeCode);
            }
            if (createdDate != null) {
                builder = builder.queryParam("createdDate", createdDate);
            }
            if (createdBy != null) {
                builder = builder.queryParam("createdBy", createdBy);
            }
            if (encodedStatus != null) {
                builder = builder.queryParam("status", encodedStatus);
            }
            if (assignedAgentId != null) {
                builder = builder.queryParam("assignedAgentId", assignedAgentId);
            }
            if (encodeAssignedTeam != null) {
                builder = builder.queryParam("assignedTeam", encodeAssignedTeam);
            }
            if (offset != null) {
                builder = builder.queryParam("offset", offset);
            }
            if (limit != null) {
                builder = builder.queryParam("limit", limit);
            }
            if (fields != null) {
                builder = builder.queryParam("fields", fields);
            }

            URI uri = builder.build(false).toUri();

            logger.info("Calling Url---" + builder.toUriString());

            ResponseEntity<String> responseFromTelus = telusAPIConnectivityService.executeTelusAPIAndGetResponseWithHeaderForSpecialChar(null, uri, HttpMethod.GET, collTreatmentSvcAuthScope);

            String result = responseFromTelus.getBody();
            HttpHeaders headers1 = responseFromTelus.getHeaders();
            String totalNoOfElement = headers1.getFirst("x-total-count");
            logger.info("::::::::Get Coll Treatment step data endpoint call success ::::::::");
            logger.info("Response---" + result);
            collectionTreatmentStepList = objectMapper.readValue(result, new TypeReference<List<CollectionTreatmentStep>>() {});
            List<CollectionTreatmentStepResponse> collectionTreatmentStepResponseList = new ArrayList<>();
            collectionTreatmentStepResponseList = convertTelusApiResponseToFawbCustomResponseForCollTStep(collectionTreatmentStepList, Integer.parseInt(totalNoOfElement));

            logger.info("::::::::Get Coll Treatment step data endpoint call success ::::::::");

            collectionTreatmentStepResponseList.stream().forEach(a -> a.setAssignedAgentId(commonUtilityService.getNameUsingEmpId(a.getAssignedAgentId())));
            // collectionTreatmentStepResponseList.stream().forEach(a->a.getAuditInfo().setCreatedBy(commonUtilityService.getNameUsingEmpId(a.getAuditInfo().getCreatedBy())));
            collectionTreatmentStepResponseList.stream().forEach(a -> {
                if (a.getAuditInfo().getCreatedBy().equalsIgnoreCase("SAS Batch Job")) {
                    a.getAuditInfo().setCreatedBy(a.getAuditInfo().getCreatedBy());
                } else {
                    a.getAuditInfo().setCreatedBy(commonUtilityService.getNameUsingEmpId(a.getAuditInfo().getCreatedBy()));
                }
            });
            collectionTreatmentStepResponseList.stream().forEach(a -> {
                if (Arrays.stream(notcTypeCodes).anyMatch(s -> s.equalsIgnoreCase(a.getStepTypeCode()))) {
                    a.setStepTypeCode("NOTC");
                }
            });
            return collectionTreatmentStepResponseList;
        }
    }

    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionTreatmentStep getCollectionTreatmentStepById(@PathVariable("id") String id, String partitionKey) throws Exception {
        return objectMapper.readValue("{\"id\":100102,\"collectionTreatment\":{\"id\":4001,\"name\":\"Treatment for Air Canada\",\"href\":\"href\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@referredType\":\"CollectionTreatment\",\"@schemaLocation\":\"href\"},\"stepDate\":\"2023-03-04\",\"stepTypeCode\":\"SOS\",\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13 14:30:45\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30 14:30:45\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"href\"},\"billingAccountRefs\":[{\"id\":\"72907342\",\"href\":\"{BASE_URL}/billingAccountId/72907342\",\"name\":\"Air Canada Toronto office\",\"@baseType\":\"billingAccountId\",\"@referredType\":\"billingAccountId\",\"@schemaLocation\":\"{BASE_URL}/schema/BillingAccount.swagger.json\",\"@type\":\"billingAccountId\"}],\"manualStepIndicator\":false,\"status\":\"PENDING\",\"languageCode\":\"en\",\"assignedAgentId\":\"909123\",\"priority\":\"TOP\",\"reasonCode\":\"Need to collect\",\"assignedTeam\":\"Offshore\",\"additionalCharacteristics\":[{\"name\":\"Dry run\",\"value\":\"Yes\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"href\"},{\"name\":\"Region\",\"value\":\"GTA\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"href\"}],\"comment\":\"This is a collection treatment step\",\"href\":\"href\",\"@baseType\":\"CollectionTreatment\",\"@type\":\"CollectionTreatmentStep\",\"@schemaLocation\":\"href\"}", CollectionTreatmentStep.class);
    }

    public CollectionTreatmentStep addCollectionTreatmentStep(CollectionTreatmentStepCreate collectionTreatmentStepCreate) throws Exception {
        objectMapper.setSerializationInclusion(Include.NON_EMPTY);
        CollectionTreatmentStep collectionTreatmentStep = new CollectionTreatmentStep();
        String requestPayload = objectMapper.writeValueAsString(collectionTreatmentStepCreate);
        logger.info(":::::Before calling Create coll treatment step- RequestPayload :::", requestPayload);
        String responseStr = telusAPIConnectivityService.executeTelusAPI(requestPayload, collectionTreatmentEndPointUrl + URIConstant.ApiMapping.GET_COLLECTION_TREATMENT_STEP, "POST", collTreatmentSvcAuthScope);
        logger.info("::::::::Response from Success Telus  API- Create coll treatment step:::::\n::::::: {}", responseStr);
        collectionTreatmentStep = objectMapper.readValue(responseStr, CollectionTreatmentStep.class);
        return collectionTreatmentStep;
    }

    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PATCH)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionTreatmentStep updateCollectionTreatmentStep(@PathVariable("id") String id, String partitionKey, CollectionTreatmentStepUpdate collectionTreatmentStepUpdate) throws Exception {
        logger.info(String.format("::::: Start Updating Step ID '%s', partition key '%s'::: ", id, partitionKey));
        collectionTreatmentStepUpdate.setId(Long.parseLong(id));
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(collectionTreatmentEndPointUrl + URIConstant.ApiMapping.GET_COLLECTION_TREATMENT_STEP + "/" + id)
                .queryParam("partitionKey", partitionKey);
        String requestPayload = objectMapper.writeValueAsString(collectionTreatmentStepUpdate);
        logger.info(":::::Before calling Update coll treatment step - RequestPayload ::: " + requestPayload);
        String responseStr = telusAPIConnectivityService.executeTelusAPI(requestPayload, builder.toUriString(), "PATCH", collTreatmentSvcAuthScope);
        logger.info("::::::::ResponsePayload from Success Telus  API - Update coll treatment step:::::\n::::::: {} " + responseStr);
        CollectionTreatmentStep collectionTreatmentStep = objectMapper.readValue(responseStr, CollectionTreatmentStep.class);
        logger.info(":::::End Updating Step ID ::: " + id);
        return collectionTreatmentStep;
    }

    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PATCH)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public int updateCollectionTreatmentStepInBulk(List<String> ids, List<String> partitionKeys, CollectionTreatmentStepUpdate collectionTreatmentStepUpdate) throws Exception {
        logger.info(":::::Start Bulk Update :::");
        logger.info("Bulk Update Steps List is : ", ids);
        int failed = 0;
        for (int i = 0; i < ids.size(); i++) {
            String id = ids.get(i);
            logger.info(":::::Start Updating Step ID :::", id);
            try {
                collectionTreatmentStepUpdate.setId(Long.parseLong(id));
                // Patch Api call to get update Collection treatment oblject
                UriComponentsBuilder builder = UriComponentsBuilder
                        .fromHttpUrl(collectionTreatmentEndPointUrl + URIConstant.ApiMapping.GET_COLLECTION_TREATMENT_STEP + "/" + id)
                        .queryParam("partitionKey", partitionKeys.get(i));
                String requestPayload = objectMapper.writeValueAsString(collectionTreatmentStepUpdate);
                logger.info(":::::Before calling Update coll treatment step- RequestPayload ::: " + requestPayload);
                String responseStr = telusAPIConnectivityService.executeTelusAPI(requestPayload, builder.toUriString(), "PATCH", collTreatmentSvcAuthScope);
                logger.info("::::::::ResponsePayload from Success Telus  API- Update coll treatment step:::::\n::::::: {} " + responseStr);
                logger.info(":::::End Updating Step ID ::: " + id);
            } catch (Exception e) {
                logger.info(":::::Error Updating Step ID :::", id);
                logger.info(e.getMessage());
                failed++;
            }
        }
        logger.info(":::::End Bulk Update :::");
        return failed;
    }

    private List<CollectionTreatmentStepResponse> convertTelusApiResponseToFawbCustomResponseForCollTStep(List<CollectionTreatmentStep> collectionTreatmentStepList, Integer totalNoOfElement) {
        List<CollectionTreatmentStepResponse> collectionTreatmentStepResponseList = new ArrayList<>();
        for (CollectionTreatmentStep collectionTreatmentStep : collectionTreatmentStepList) {
            CollectionTreatmentStepResponse collectionTreatmentStepResponse = new CollectionTreatmentStepResponse();
            // collectionTreatmentStepResponse.setChannel(collectionTreatmentStep.getChannel());
            collectionTreatmentStepResponse.setId(collectionTreatmentStep.getId());
            collectionTreatmentStepResponse.setPartitionKey(collectionTreatmentStep.getPartitionKey());
            collectionTreatmentStepResponse.setHref(collectionTreatmentStep.getHref());
            collectionTreatmentStepResponse.setAdditionalCharacteristics(collectionTreatmentStep.getAdditionalCharacteristics());
            collectionTreatmentStepResponse.setAssignedAgentId(collectionTreatmentStep.getAssignedAgentId());
            collectionTreatmentStepResponse.setAssignedTeam(collectionTreatmentStep.getAssignedTeam());
            collectionTreatmentStepResponse.setAuditInfo(collectionTreatmentStep.getAuditInfo());
            collectionTreatmentStepResponse.setBillingAccountRefs(collectionTreatmentStep.getBillingAccountRefs());
            collectionTreatmentStepResponse.setCollectionTreatment(collectionTreatmentStep.getCollectionTreatment());
            collectionTreatmentStepResponse.setCollectionTreatmentPath(collectionTreatmentStep.getCollectionTreatmentPath());
            collectionTreatmentStepResponse.setComment(collectionTreatmentStep.getComment());
            collectionTreatmentStepResponse.setContentTypeCode(collectionTreatmentStep.getContentTypeCode());
            collectionTreatmentStepResponse.setDeliverSystemName(collectionTreatmentStep.getDeliverSystemName());
            collectionTreatmentStepResponse.setDeliverSystemObjectId(collectionTreatmentStep.getDeliverSystemObjectId());
            collectionTreatmentStepResponse.setDeliverSystemObjectType(collectionTreatmentStep.getDeliverSystemObjectType());
            collectionTreatmentStepResponse.setPriority(collectionTreatmentStep.getPriority());
            collectionTreatmentStepResponse.setQueueId(collectionTreatmentStep.getQueueId());
            collectionTreatmentStepResponse.setReasonCode(collectionTreatmentStep.getReasonCode());
            collectionTreatmentStepResponse.setStatus(collectionTreatmentStep.getStatus());
            collectionTreatmentStepResponse.setStatusDateTime(collectionTreatmentStep.getStatusDateTime());
            collectionTreatmentStepResponse.setStepCreationMethod(collectionTreatmentStep.getStepCreationMethod());
            collectionTreatmentStepResponse.setStepDate(collectionTreatmentStep.getStepDate());
            collectionTreatmentStepResponse.setStepSequenceNumber(collectionTreatmentStep.getStepSequenceNumber());
            collectionTreatmentStepResponse.setStepTypeCode(collectionTreatmentStep.getStepTypeCode());
            collectionTreatmentStepResponse.setBaseType(collectionTreatmentStep.getAtBaseType());
            collectionTreatmentStepResponse.setType(collectionTreatmentStep.getAtType());
            collectionTreatmentStepResponse.setSchemaLocation(collectionTreatmentStep.getAtSchemaLocation());
            collectionTreatmentStepResponse.setAssignedPersonForDefaultValue(collectionTreatmentStep.getAssignedAgentId());
            collectionTreatmentStepResponse.setTotalNumberOfElement(totalNoOfElement);
            collectionTreatmentStepResponse.setContentTypeCode(collectionTreatmentStep.getContentTypeCode());
            collectionTreatmentStepResponseList.add(collectionTreatmentStepResponse);
        }
        return collectionTreatmentStepResponseList;
    }

}
