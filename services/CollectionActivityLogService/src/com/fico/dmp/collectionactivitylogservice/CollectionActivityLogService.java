/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.collectionactivitylogservice;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.HttpMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wavemaker.runtime.security.SecurityService;
import com.wavemaker.runtime.service.annotations.ExposeToClient;
import com.wavemaker.runtime.service.annotations.HideFromClient;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;
import com.wavemaker.tools.api.core.annotations.WMAccessVisibility;
import com.wavemaker.tools.api.core.models.AccessSpecifier;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import telus.cdo.cnc.collmgmt.collactivitylogmgmt.model.CollectionActivityLog;
import telus.cdo.cnc.collmgmt.collactivitylogmgmt.model.Characteristic;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.BillingAccount;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.CollectionBillingAccountRef;
import telus.cdo.cnc.collmgmt.colltreatmentmgmt.model.CollectionTreatment;
import telus.cdo.cnc.collmgmt.colltreatmentmgmt.model.CollectionTreatmentStep;
import telus.cdo.cnc.collmgmt.colltreatmentmgmt.model.CollectionTreatmentStepCreate;
import telus.cdo.cnc.collmgmt.colltreatmentmgmt.model.CollectionTreatmentStepUpdate;

import com.fico.core.util.ObjectMapperConfig;
import com.fico.dmp.collectionentityservice.CollectionEntityService;
import com.fico.dmp.commonutilityservice.CommonUtilityService;
import com.fico.pscomponent.util.PropertiesUtil;
import com.fico.telus.model.CollectionActivityLogRes;
import com.fico.telus.model.OrderMgmtHistoryResponse;
import com.fico.telus.service.TelusAPIConnectivityService;
import com.fico.telus.utility.URIConstant;

//import com.fico.dmp.collectionactivitylogservice.model.*;

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
public class CollectionActivityLogService {
    private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(CollectionActivityLogService.class.getName());

    private static final String COLLACTIVITYLOG_ENDPOINT_URL = "COLLACTIVITYLOG_ENDPOINT_BASEURL";
    private static final String COLLACTIVITYLOG_ENDPOINT_SCOPE = "COLLACTIVITYLOG_ENDPOINT_SCOPE";

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

    private String collActivityLogEndPointUrl;
    private String collActivityLogSvcAuthScope;
    private final ObjectMapper objectMapper = new ObjectMapperConfig().customObjectMapper();
    
    @PostConstruct
    public void init() {
        this.collActivityLogEndPointUrl = propertyValueFrom(COLLACTIVITYLOG_ENDPOINT_URL, URIConstant.COLLECTION_ACTIVITY_LOG_SERVICE_URL);
        this.collActivityLogSvcAuthScope = propertyValueFrom(COLLACTIVITYLOG_ENDPOINT_SCOPE, "4466");
    }

    private String propertyValueFrom(String propertyName, String defaulValueIfNull) {
        String propertyValue = propertiesUtil.getPropertyValue(propertyName);
        if (propertyValue == null) {
            logger.info("property value is null, using default");
            propertyValue = defaulValueIfNull;
        }
        return propertyValue;
    }
    

    /**
     * This is sample java operation that accepts an input from the caller and responds with "Hello".
     *
     * SecurityService that is Autowired will provide access to the security context of the caller. It has methods like isAuthenticated(),
     * getUserName() and getUserId() etc which returns the information based on the caller context.
     *
     * Methods in this class can declare HttpServletRequest, HttpServletResponse as input parameters to access the
     * caller's request/response objects respectively. These parameters will be injected when request is made (during API invocation).
     */
    public String sampleJavaOperation(String name, HttpServletRequest request) {
        logger.debug("Starting sample operation with request url " + request.getRequestURL().toString());

        String result = null;
        if (securityService.isAuthenticated()) {
            result = "Hello " + name + ", You are logged in as "+  securityService.getLoggedInUser().getUserName();
        } else {
            result = "Hello " + name + ", You are not authenticated yet!";
        }
        logger.debug("Returning {}", result);
        return result;
    }
    
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public List<CollectionActivityLogRes> getCollectionActivityLog(Integer collectionEntityId, String businessEntityEventType,String relatedBusinessEntitySubType, String relatedBusinessEntityContentId, String relatedBusinessEntityId,String relatedBusinessEntityType, String relatedBusinessEntityStatus,String relatedBusinessEntityCreatedDate, String relatedBusinessEntityCreatedBy, String relatedBusinessEntityAssignedTo, String relatedBusinessEntityAssignedTeam, String groupBy, String aggFunc, String aggProp, String fields, Integer offset, Integer limit) throws Exception  {

        List<CollectionActivityLog> collectionActivityLogRes= new ArrayList<>();
        String totalNoOfElement=null;
            
            if(collectionEntityId!=null){
                
                String encodeAssignedTeam=null;
                String encodedStatus=null;
            if(relatedBusinessEntityAssignedTeam!=null)
            {
                 // encodeAssignedTeam = URLEncoder.encode(relatedBusinessEntityAssignedTeam, "UTF-8");
                 encodeAssignedTeam=relatedBusinessEntityAssignedTeam;
            }else{
                encodeAssignedTeam=relatedBusinessEntityAssignedTeam;
            }
            
            if(relatedBusinessEntityStatus!=null)
            {
                 // encodedStatus = URLEncoder.encode(relatedBusinessEntityStatus, "UTF-8");
                 encodedStatus=relatedBusinessEntityStatus;
            }else{
                encodedStatus=relatedBusinessEntityStatus;
            }
            String[] notcTypeCodes = { "NOTC1-PMTR", "NOTC2-OD", "NOTC3-DIST", "NOTC4-CANL", "NOTC5-ACTMGR", "NOTC6-REFERRAL" };
            if (relatedBusinessEntitySubType != null) {
                if (relatedBusinessEntitySubType.equalsIgnoreCase("NOTC")) {
                    relatedBusinessEntitySubType = "NOTC,NOTICE";//String.join(",", notcTypeCodes);
                }
            }

            logger.info("::::::::Calling  Coll Activity log endpoint call ::::::::");
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(collActivityLogEndPointUrl + URIConstant.ApiMapping.GET_COLL_ACTIVITY_LOG)
                   .queryParam("collectionEntityId", collectionEntityId)
                    .queryParam("businessEntityEventType", businessEntityEventType)
                    .queryParam("relatedBusinessEntitySubType", relatedBusinessEntitySubType)
                    .queryParam("relatedBusinessEntityContentId", relatedBusinessEntityContentId)
                    .queryParam("relatedBusinessEntityId", relatedBusinessEntityId)
                    .queryParam("relatedBusinessEntityType", relatedBusinessEntityType)
                    .queryParam("relatedBusinessEntityStatus", encodedStatus)
                    .queryParam("relatedBusinessEntityCreatedDate", relatedBusinessEntityCreatedDate)
                    .queryParam("relatedBusinessEntityCreatedBy", relatedBusinessEntityCreatedBy)
                    .queryParam("relatedBusinessEntityAssignedTo", relatedBusinessEntityAssignedTo)
                    .queryParam("relatedBusinessEntityAssignedTeam", encodeAssignedTeam)
                    .queryParam("groupBy", groupBy)
                    .queryParam("aggFunc", aggFunc)
                    .queryParam("aggProp", aggProp)
                    .queryParam("fields", fields)
                    .queryParam("offset", offset)
                    .queryParam("limit",limit);
                   
            logger.info("Calling Url---" + builder.toUriString());

            // String responseStr = telusAPIConnectivityService.executeTelusAPI(null, builder.toUriString(), HttpMethod.GET, collTreatmentSvcAuthScope);
            ResponseEntity<String> responseFromTelus = telusAPIConnectivityService.executeTelusAPIAndGetResponseWithHeader(null, builder.toUriString(), HttpMethod.GET, collActivityLogSvcAuthScope);
                String result = responseFromTelus.getBody();
                HttpHeaders headers1 = responseFromTelus.getHeaders();
                totalNoOfElement = headers1.getFirst("x-total-count");
            logger.info("::::::::Coll Activity log endpoint call success ::::::::");
            logger.info("Coll Activity log Response---" + result);
            //  return objectMapper.readValue(responseStr, CollectionTreatmentStep.class);
           collectionActivityLogRes= objectMapper.readValue(result, new TypeReference<List<CollectionActivityLog>>() {

           });
            }
            
            for (CollectionActivityLog collectionActivityLog : collectionActivityLogRes) {
            	if(collectionActivityLog.getCollectionActivityPerformedBy().equals("tcm-collections-parr-eval-batch") || collectionActivityLog.getCollectionActivityPerformedBy().equals("SAS Batch Job")) {
            		collectionActivityLog.setCollectionActivityPerformedBy(collectionActivityLog.getCollectionActivityPerformedBy());
            	}else if(collectionActivityLog.getCollectionActivityPerformedBy().equals("system")){
            	  collectionActivityLog.setCollectionActivityPerformedBy(collectionActivityLog.getCollectionActivityPerformedBy());

            	}
            	else {
            		collectionActivityLog.setCollectionActivityPerformedBy(commonUtilityService.getNameUsingEmpId(collectionActivityLog.getCollectionActivityPerformedBy()));
            	}
            	
            	if(collectionActivityLog.getRelatedBusinessEntityCreatedBy().equals("tcm-collections-parr-eval-batch") || collectionActivityLog.getRelatedBusinessEntityCreatedBy().equals("SAS Batch Job")) {
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

    private List<CollectionActivityLogRes> convertTelusResToFawb(List<CollectionActivityLog> collectionActivityLogRes, int totalNoOfElement) {
        List<CollectionActivityLogRes> collectionActivityLogResList=new ArrayList<>();
        for(CollectionActivityLog collectionActivityLog:collectionActivityLogRes)
        {
            CollectionActivityLogRes collectionActivityLogRes1=new CollectionActivityLogRes();
            collectionActivityLogRes1.setId(collectionActivityLog.getId());
            collectionActivityLogRes1.setContentTypeCode(getCharacteristicValueByName(collectionActivityLog.getAdditionalCharacteristics(),"contentTypeCode"));
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
            collectionActivityLogRes1.setBillingAccountRefs(collectionActivityLog.getBillingAccountRefs());
            collectionActivityLogRes1.setBusinessEntityEventType(collectionActivityLog.getBusinessEntityEventType());
            collectionActivityLogRes1.setRelatedBusinessEntitySubType(collectionActivityLog.getRelatedBusinessEntitySubType());
            collectionActivityLogRes1.setActivityReason(collectionActivityLog.getActivityReason());
            collectionActivityLogRes1.setAdditionalCharacteristics(collectionActivityLog.getAdditionalCharacteristics());
            collectionActivityLogRes1.setComment(collectionActivityLog.getComment());
            collectionActivityLogRes1.setPartitionKey(collectionActivityLog.getPartitionKey());
            collectionActivityLogRes1.setDataSourceId(collectionActivityLog.getDataSourceId());
            collectionActivityLogRes1.setHref(collectionActivityLog.getHref());
            collectionActivityLogRes1.setBaseType(collectionActivityLog.getAtBaseType());
            collectionActivityLogRes1.setType(collectionActivityLog.getAtType());
            collectionActivityLogRes1.setSchemaLocation(collectionActivityLog.getAtSchemaLocation());
            collectionActivityLogRes1.setTotalNoOfElement(totalNoOfElement);
            collectionActivityLogRes1.setContentTypeCode(getCharacteristicValueByName(collectionActivityLog.getAdditionalCharacteristics(),"contentTypeCode"));
        logger.info("Content Type Code : " + collectionActivityLogRes1.getContentTypeCode());
            collectionActivityLogResList.add(collectionActivityLogRes1);
        }
        return collectionActivityLogResList;
    }

    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public CollectionActivityLog getCollectionActivityLogById(@PathVariable("id") String id, String partitionKey) throws Exception {
    	return objectMapper.readValue("{\"id\":678532,\"collectionEntity\":{\"id\":666,\"name\":\"Air Canada\",\"href\":\"{BASE_URL}/entity/666\",\"@baseType\":\"CollectionEntity\",\"@type\":\"CollectionEntity\",\"@referredType\":\"CollectionEntity\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionEntity.swagger.json\"},\"auditInfo\":{\"createdBy\":\"Agent98782\",\"createdTimestamp\":\"2023-01-13 14:30:45\",\"dataSource\":\"FICO\",\"lastUpdatedBy\":\"Agent86293\",\"lastUpdatedTimestamp\":\"2023-03-30 14:30:45\",\"@baseType\":\"AuditInfo\",\"@type\":\"AuditInfo\",\"@schemaLocation\":\"schemaLocation\"},\"collectionActivityCompletionTimestamp\":\"2023-03-30 14:30:45\",\"relatedBusinessEntityId\":\"32465656\",\"relatedBusinessEntityType\":\"COLL\",\"relatedBusinessEntityStatus\":\"WIP\",\"relatedBusinessEntityCreatedTimestamp\":\"2023-03-30 14:30:45\",\"relatedBusinessEntityCreatedBy\":\"SYSTEM\",\"relatedBusinessEntityAssignedTo\":\"SOMEONE\",\"relatedBusinessEntityAssignedTeam\":\"COLLTEAM\",\"billingAccountRefs\":[{\"id\":\"72907342\",\"href\":\"{BASE_URL}/billingAccountId/72907342\",\"name\":\"Air Canada Toronto office\",\"@baseType\":\"billingAccountId\",\"@referredType\":\"billingAccountId\",\"@schemaLocation\":\"{BASE_URL}/schema/BillingAccount.swagger.json\",\"@type\":\"billingAccountId\"}],\"collectionActivityType\":\"SUSPEND\",\"activityReason\":\"Haven’t got the payment for 1 month\",\"additionalCharacteristics\":[{\"name\":\"Dry run\",\"value\":\"Yes\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"schemaLocation\"},{\"name\":\"Region\",\"value\":\"GTA\",\"@baseType\":\"EntityRef\",\"@type\":\"EntityRef\",\"@schemaLocation\":\"schemaLocation\"}],\"comment\":\"This has to be collected\",\"href\":\"{BASE_URL}/CollectionActivityLog/678532\",\"@baseType\":\"CollectionActivityLog\",\"@type\":\"CollectionActivityLog\",\"@schemaLocation\":\"{BASE_URL}/schema/CollectionTreatment.swagger.json\"}",CollectionActivityLog.class);
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
        
        List<CollectionActivityLogRes> collectionActivityLogList = getCollectionActivityLog(collectionEntityId, collectionActivityType,null,null,relatedBusinessEntityId, relatedBusinessEntityType, relatedBusinessEntityStatus, relatedBusinessEntityCreatedDate, relatedBusinessEntityCreatedBy, relatedBusinessEntityAssignedTo, relatedBusinessEntityAssignedTeam, "","","", fields, offset, limit);


       if (!collectionActivityLogList.isEmpty()) {
            for (CollectionActivityLogRes collectionActivityLog : collectionActivityLogList) {
                OrderMgmtHistoryResponse orderMgmtHistoryResponse = new OrderMgmtHistoryResponse();
                if(collectionActivityLog.getBillingAccountRefs()!=null) {
                    banIds = collectionActivityLog.getBillingAccountRefs().stream().map(a -> a.getId().toString()).collect(Collectors.toList());
                    
                    logger.info("Ban IDs for History" + banIds);

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
        List<CollectionActivityLogRes> collectionActivityLogList = getCollectionActivityLog(collectionEntityId, collectionActivityType, null, null, relatedBusinessEntityId, relatedBusinessEntityType, relatedBusinessEntityStatus, relatedBusinessEntityCreatedDate, relatedBusinessEntityCreatedBy, relatedBusinessEntityAssignedTo, relatedBusinessEntityAssignedTeam, "","","", fields, offset, limit);
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
        List<CollectionActivityLogRes> collectionActivityLogList = getCollectionActivityLog(collectionEntityId, collectionActivityType,null,null,relatedBusinessEntityId, relatedBusinessEntityType, relatedBusinessEntityStatus, relatedBusinessEntityCreatedDate, relatedBusinessEntityCreatedBy, relatedBusinessEntityAssignedTo, relatedBusinessEntityAssignedTeam, "","","", fields, offset, limit);
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


private String getCharacteristicValueByName(List<Characteristic> characteristics, String name) {
    // Check if characteristics list is null or empty, return empty string if true
    if (characteristics == null || characteristics.isEmpty()) {
        return "";
    }

    // Use Stream API to search for the characteristic with the given name
    return characteristics.stream()
            .filter(characteristic -> name.equals(characteristic.getName()) && characteristic.getValue() != null)
            .map(characteristic -> (String) characteristic.getValue())  // Cast to String
            .findFirst()
            .orElse("");  // Return empty string if not found
}


}