package com.fico.telus.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import com.fico.telus.utility.URIConstant;
import com.fico.telus.model.ParrResWithHeader;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fico.pscomponent.util.PropertiesUtil;

//import io.swagger.client.model.AuditInfo;
//import io.swagger.client.model.CollectionPaymentArrangement;
//import io.swagger.client.model.CollectionPaymentArrangementCreate;
////import io.swagger.client.model.CollectionPaymentArrangementStatus;
//import io.swagger.client.model.CollectionPaymentArrangementUpdate;
//import io.swagger.client.model.EntityRef;

import telus.cdo.cnc.collmgmt.collentitymgmt.model.CollectionPaymentArrangement;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.CollectionPaymentArrangementCreate;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.CollectionPaymentArrangementUpdate;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.EntityRef;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.fico.dmp.commonutilityservice.CommonUtilityService;
import com.fico.core.util.ObjectMapperConfig;





@Service
public class PARRService {
    
         private final ObjectMapper objectMapper = new ObjectMapperConfig().customObjectMapper();


	private static final Logger logger = LoggerFactory.getLogger(PARRService.class);

	private static final String IS_ENTITYSVC_STUB_ENABLED = "IS_ENTITYSVC_STUB_ENABLED";

	private static final String ENTITYSVC_ENDPOINT_URL = "ENTITYSVC_ENDPOINT_BASEURL";
		private static final String ENTITYSVC_ENDPOINT_SCOPE = "ENTITYSVC_ENDPOINT_SCOPE";


	@Autowired
	private PropertiesUtil propertiesUtil;

	@Autowired
	private TelusAPIConnectivityService telusAPIConnectivityService;
	
	@Autowired
	private CommonUtilityService commonUtilityService;


	private boolean isParrStubEnabled;

	private String parrEndPointUrl;
	
	private String entitySvcAuthScope;

	@Autowired
	@Qualifier("customObjectMapper")
	private ObjectMapper mapper;

	@PostConstruct
	public void init() {

	this.isParrStubEnabled = Boolean.valueOf(propertyValueFrom(IS_ENTITYSVC_STUB_ENABLED, "false"));
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

	public CollectionPaymentArrangementCreate createPaymentArrangement(
			CollectionPaymentArrangementCreate collectionPaymentArrangementCreate, String entityId) throws Exception {

    
		if (isParrStubEnabled) {
		    
		    EntityRef entityRef =  new EntityRef();
			entityRef.setId(entityId);
			collectionPaymentArrangementCreate.setCollectionEntity(entityRef);
			if (collectionPaymentArrangementCreate.getBillingAccountRefs().size() > 0) {
				logger.info(":::::::BillingAccountRefs :::::::::::: {}", collectionPaymentArrangementCreate.getBillingAccountRefs().get(0).getName());
			}
			String requestPayload = mapper.writeValueAsString(collectionPaymentArrangementCreate);
            logger.info(":::::::In stub collectionPaymentArrangementCreate requestPayload :::::\n::::::: {}", requestPayload);
			return new CollectionPaymentArrangementCreate();
		} else {

			// collectionCommonService.setAuditinfo(collectionPaymentArrangement.getAuditInfo(),
			// true);
			logger.info("::::::::Calling telus API to create PARR:::::\n::::::: {}");

			//EntityRef entityRef =  new EntityRef();
			//entityRef.setId(entityId);
			//collectionPaymentArrangementCreate.setCollectionEntity(entityRef);
			String requestPayload = mapper.writeValueAsString(collectionPaymentArrangementCreate);
			logger.info("::::::::collectionPaymentArrangementCreate requestPayload :::::\n::::::: {}", requestPayload);

			String responseStr = telusAPIConnectivityService.executeTelusAPI(requestPayload, this.parrEndPointUrl+URIConstant.ApiMapping.GET_PARR,
					"POST","3161");
						logger.info("::::::::Response from Success Telus  API- CREATE PARR:::::\n::::::: {}",responseStr);
			CollectionPaymentArrangementCreate collectionPaymentArrangement = mapper.readValue(responseStr,CollectionPaymentArrangementCreate.class);
			return collectionPaymentArrangement;
			
		
		}
	}
	
	public CollectionPaymentArrangementUpdate updateParrStatus(Integer parrId, String status, String comments) throws Exception {
		
	//	CollectionPaymentArrangementStatus parrStatus = new CollectionPaymentArrangementStatus();
	//	List<CollectionPaymentArrangementStatus> statuses = new ArrayList<CollectionPaymentArrangementStatus>();
	//	parrStatus.setStatus(status);
	//	parrStatus.setAuditInfo(collectionCommonService.UpdateAuditInfo(new AuditInfo(), true));
	//	statuses.add(parrStatus);
		CollectionPaymentArrangementUpdate collectionPaymentArrangementUpdate = new CollectionPaymentArrangementUpdate();
	//	collectionPaymentArrangementUpdate.setStatuses(statuses);
		collectionPaymentArrangementUpdate.setId(parrId);
		CollectionPaymentArrangementUpdate collectionPaymentArrangement = updatePaymentArrangement(collectionPaymentArrangementUpdate,parrId);
		return collectionPaymentArrangement;
	}

public CollectionPaymentArrangementUpdate updatePaymentArrangement(
			CollectionPaymentArrangementUpdate collectionPaymentArrangementUpdate, Integer id) throws Exception {

		if (isParrStubEnabled) {
			
			String requestPayload = mapper.writeValueAsString(collectionPaymentArrangementUpdate);
            logger.info(":::::::In stub collectionPaymentArrangementUpdate requestPayload :::::\n::::::: {}", requestPayload);
			return new CollectionPaymentArrangementUpdate();
		} else {

			// collectionCommonService.setAuditinfo(collectionPaymentArrangement.getAuditInfo(),
			// false);
			String requestPayload = mapper.writeValueAsString(collectionPaymentArrangementUpdate);
			logger.info("::::::::collectionPaymentArrangementUpdate requestPayload ::::::::{}", requestPayload);
	   //  	UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(parrEndPointUrl+ URIConstant.ApiMapping.GET_PARR)
				// 	.queryParam("id", id);
			String responseStr = telusAPIConnectivityService.executeTelusAPI(requestPayload, this.parrEndPointUrl+URIConstant.ApiMapping.GET_PARR+"/"+id,
					"PATCH","3161");
				logger.info("::::::::Response from Success Telus  API UPDATE PARR:::::\n::::::: {}",responseStr);

				CollectionPaymentArrangementUpdate collectionPaymentArrangement = mapper.readValue(responseStr,CollectionPaymentArrangementUpdate.class);
			return collectionPaymentArrangement;
		}
	}

public ParrResWithHeader getPaymentArrangements(String entityId, String parrEndPointUrlArg) throws Exception {

ParrResWithHeader parrResWithHeader=new ParrResWithHeader();
	List<CollectionPaymentArrangement> responseObjectLis=new ArrayList<>();
	//	List<CollectionPaymentArrangement> collectionPaymentArrangements = new ArrayList<CollectionPaymentArrangement>();
		if (isParrStubEnabled) {

			responseObjectLis = mapper.readValue(
					"[{\"id\":1,\"href\":\"BASE_URL/paymentArrangement/1\",\"allBillingAccountIncludedIndicator\":true,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccountMaps\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccountRef\":{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccount\":{\"id\":\"12345\",\"accountGroupId\":1,\"accountType\":\"B\",\"accountSubType\":\"I\",\"name\":\"A Company\",\"state\":\"O\",\"@type\":\"BillingAccount\"},\"billingSystemId\":10,\"billingSystemName\":\"CES9\",\"closingCycle\":6,\"collectionStatus\":\"INCOLL\",\"fraudIndicator\":false,\"involuntaryCeasedIndicator\":false,\"writeOffIndicator\":false,\"@type\":\"CollectionBillingAccountRef\"},\"validityIndicator\":true,\"@type\":\"CollectionPaymentArrangementBillingAccountMap\"}],\"collectionEntity\":{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"@referredType\":\"CollectionEntity\",\"@type\":\"EntityRef\"},\"comment\":\"string\",\"evaluationResult\":\"string\",\"expectedPaymentAmountToDate\":100.0,\"installments\":[{\"id\":1,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"date\":\"2023-01-01\",\"evaluationResult\":\"string\",\"evaluationTimestamp\":\"2023-01-01T09:00:00.001Z\",\"sequenceId\":1,\"validityIndicator\":true,\"@type\":\"CollectionPaymentInstallment\"}],\"receivedPaymentAmountToDate\":0.0,\"recurrence\":\"MONTHLY\",\"statuses\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"reason\":\"string\",\"status\":\"string\",\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.001Z\"},\"@type\":\"CollectionPaymentArrangementStatus\"}],\"@type\":\"CollectionPaymentArrangement\"},{\"id\":2,\"href\":\"BASE_URL/paymentArrangement/1\",\"allBillingAccountIncludedIndicator\":true,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccountMaps\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccountRef\":{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccount\":{\"id\":\"12345\",\"accountGroupId\":1,\"accountType\":\"B\",\"accountSubType\":\"I\",\"name\":\"A Company\",\"state\":\"O\",\"@type\":\"BillingAccount\"},\"billingSystemId\":10,\"billingSystemName\":\"CES9\",\"closingCycle\":6,\"collectionStatus\":\"INCOLL\",\"fraudIndicator\":false,\"involuntaryCeasedIndicator\":false,\"writeOffIndicator\":false,\"@type\":\"CollectionBillingAccountRef\"},\"validityIndicator\":true,\"@type\":\"CollectionPaymentArrangementBillingAccountMap\"}],\"collectionEntity\":{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"@referredType\":\"CollectionEntity\",\"@type\":\"EntityRef\"},\"comment\":\"string\",\"evaluationResult\":\"string\",\"expectedPaymentAmountToDate\":100.0,\"installments\":[{\"id\":1,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"date\":\"2023-01-01\",\"evaluationResult\":\"string\",\"evaluationTimestamp\":\"2023-01-01T09:00:00.001Z\",\"sequenceId\":1,\"validityIndicator\":true,\"@type\":\"CollectionPaymentInstallment\"}],\"receivedPaymentAmountToDate\":0.0,\"recurrence\":\"MONTHLY\",\"statuses\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"reason\":\"string\",\"status\":\"string\",\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.001Z\"},\"@type\":\"CollectionPaymentArrangementStatus\"}],\"@type\":\"CollectionPaymentArrangement\"},{\"id\":3,\"href\":\"BASE_URL/paymentArrangement/1\",\"allBillingAccountIncludedIndicator\":true,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccountMaps\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccountRef\":{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccount\":{\"id\":\"12345\",\"accountGroupId\":1,\"accountType\":\"B\",\"accountSubType\":\"I\",\"name\":\"A Company\",\"state\":\"O\",\"@type\":\"BillingAccount\"},\"billingSystemId\":10,\"billingSystemName\":\"CES9\",\"closingCycle\":6,\"collectionStatus\":\"INCOLL\",\"fraudIndicator\":false,\"involuntaryCeasedIndicator\":false,\"writeOffIndicator\":false,\"@type\":\"CollectionBillingAccountRef\"},\"validityIndicator\":true,\"@type\":\"CollectionPaymentArrangementBillingAccountMap\"}],\"collectionEntity\":{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"@referredType\":\"CollectionEntity\",\"@type\":\"EntityRef\"},\"comment\":\"string\",\"evaluationResult\":\"string\",\"expectedPaymentAmountToDate\":100.0,\"installments\":[{\"id\":1,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"date\":\"2023-01-01\",\"evaluationResult\":\"string\",\"evaluationTimestamp\":\"2023-01-01T09:00:00.001Z\",\"sequenceId\":1,\"validityIndicator\":true,\"@type\":\"CollectionPaymentInstallment\"}],\"receivedPaymentAmountToDate\":0.0,\"recurrence\":\"MONTHLY\",\"statuses\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"reason\":\"string\",\"status\":\"string\",\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.001Z\"},\"@type\":\"CollectionPaymentArrangementStatus\"}],\"@type\":\"CollectionPaymentArrangement\"}]",

					mapper.getTypeFactory().constructCollectionType(List.class, CollectionPaymentArrangement.class));

		} else {
		    
// 		     logger.info("Htting telus API :::::::::::::::::::::::");
// 		     logger.info("parrEndPointUrl--------"+parrEndPointUrlArg);
	
// 			String responseStr = telusAPIConnectivityService.executeTelusAPI(null,
// 					parrEndPointUrlArg, "GET","3161");
// 					 logger.info("PARR TELUS RESPONSE:: " + responseStr);
// 			collectionPaymentArrangements = mapper.readValue(responseStr,
// 					mapper.getTypeFactory().constructCollectionType(List.class, CollectionPaymentArrangement.class));

        	ResponseEntity<String> responseFromTelus = telusAPIConnectivityService.executeTelusAPIAndGetResponseWithHeader(null, parrEndPointUrlArg, "GET", entitySvcAuthScope);
			String result=responseFromTelus.getBody();
			logger.info("PARR TELUS RESPONSE:: " + result);
			HttpHeaders headers1=responseFromTelus.getHeaders();
			String totalNoOfElement=headers1.getFirst("x-total-count");
			responseObjectLis= objectMapper.readValue(result, objectMapper.getTypeFactory().constructCollectionType(List.class, CollectionPaymentArrangement.class));
		   responseObjectLis=	setCreatedAndUpdatedBy(responseObjectLis);
			parrResWithHeader.setTotalNumberOfElement(Integer.parseInt(totalNoOfElement));
			parrResWithHeader.setResponseObjectList(responseObjectLis);
		  
		}
		
	return	parrResWithHeader;

  //return	setCreatedAndUpdatedBy(collectionPaymentArrangements);
}

public CollectionPaymentArrangement getPaymentArrangement(Integer parrId, Boolean history) throws Exception {

		CollectionPaymentArrangement collectionPaymentArrangement = null;
		if (isParrStubEnabled) {

			collectionPaymentArrangement = mapper.readValue(
					"{\"id\":1,\"href\":\"BASE_URL/paymentArrangement/1\",\"allBillingAccountIncludedIndicator\":false,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccountRefs\":[{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"@referredType\":\"CollectionBillingAccountRef\",\"@type\":\"EntityRef\"}],\"collectionEntity\":{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"@referredType\":\"CollectionEntity\",\"@type\":\"EntityRef\"},\"comment\":\"Collection payment arrangement comment 1\",\"evaluationResult\":\"string\",\"expectedPaymentAmountToDate\":0.0,\"installments\":[{\"id\":1,\"amount\":50.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"date\":\"2023-01-01\",\"sequenceId\":1,\"@type\":\"CollectionPaymentInstallment\"},{\"id\":2,\"amount\":50.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"date\":\"2023-02-01\",\"sequenceId\":2,\"@type\":\"CollectionPaymentInstallment\"}],\"receivedPaymentAmountToDate\":0.0,\"recurrence\":\"MONTHLY\",\"status\":\"string\",\"statusDateTime\":\"2023-01-01T09:00:00.00Z\",\"statusReason\":\"string\",\"@type\":\"CollectionPaymentArrangement\"}",
					CollectionPaymentArrangement.class);
		} else {

			String responseStr = telusAPIConnectivityService.executeTelusAPI(null,
					this.parrEndPointUrl + URIConstant.ApiMapping.GET_PARR+ "/" + parrId, "GET","3161");
			collectionPaymentArrangement = mapper.readValue(responseStr,CollectionPaymentArrangement.class);
		}
		return collectionPaymentArrangement;
}
	
	
private List<CollectionPaymentArrangement> setCreatedAndUpdatedBy(List<CollectionPaymentArrangement> collectionPaymentArrangements) {

		for(CollectionPaymentArrangement collectionPaymentArrangement:collectionPaymentArrangements)
		{
			collectionPaymentArrangement.getAuditInfo().setCreatedBy(commonUtilityService.getNameUsingEmpId(collectionPaymentArrangement.getAuditInfo().getCreatedBy()));
			if(collectionPaymentArrangement.getAuditInfo().getLastUpdatedBy() != null) {
				if(collectionPaymentArrangement.getAuditInfo().getLastUpdatedBy().equals("tcm-collections-parr-eval-batch")) {
					collectionPaymentArrangement.getAuditInfo().setLastUpdatedBy(collectionPaymentArrangement.getAuditInfo().getLastUpdatedBy());
				}else {
					collectionPaymentArrangement.getAuditInfo().setLastUpdatedBy(commonUtilityService.getNameUsingEmpId(collectionPaymentArrangement.getAuditInfo().getLastUpdatedBy()));
				}
			}

		}

		return collectionPaymentArrangements;
	}

}
