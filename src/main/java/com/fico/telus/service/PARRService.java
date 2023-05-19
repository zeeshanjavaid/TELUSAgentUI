package com.fico.telus.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fico.pscomponent.util.PropertiesUtil;

import io.swagger.client.model.AuditInfo;
import io.swagger.client.model.CollectionPaymentArrangement;
import io.swagger.client.model.CollectionPaymentArrangementCreate;
//import io.swagger.client.model.CollectionPaymentArrangementStatus;
import io.swagger.client.model.CollectionPaymentArrangementUpdate;
import io.swagger.client.model.EntityRef;

@Service
public class PARRService {

	private static final Logger logger = LoggerFactory.getLogger(PARRService.class);

	private static final String IS_PARR_STUB_ENABLED = "IS_PARR_STUB_ENABLED";

	private static final String PARR_ENDPOINT_URL = "PARR_ENDPOINT_URL";

	@Autowired
	private PropertiesUtil propertiesUtil;

	@Autowired
	private TelusAPIConnectivityService telusAPIConnectivityService;

	@Autowired
	private CollectionCommonService collectionCommonService;

	private boolean isParrStubEnabled;

	private String parrEndPointUrl;

	@Autowired
	@Qualifier("customObjectMapper")
	private ObjectMapper mapper;

	@PostConstruct
	public void init() {

		this.isParrStubEnabled = Boolean.valueOf(propertyValueFrom(IS_PARR_STUB_ENABLED, "false"));
		this.parrEndPointUrl = propertyValueFrom(PARR_ENDPOINT_URL, "https://apigw-public-yul-np-002.cloudapps.telus.com/customer/collectionEntityMgmt/v1/billingAccountRef");
	}

	private String propertyValueFrom(String propertyName, String defaulValueIfNull) {
		String propertyValue = propertiesUtil.getPropertyValue(propertyName);
		if (propertyValue == null) {
			logger.info("property value is null, using default");
			propertyValue = defaulValueIfNull;
		}
		return propertyValue;
	}

	public CollectionPaymentArrangement createPaymentArrangement(
			CollectionPaymentArrangementCreate collectionPaymentArrangementCreate, String entityId) throws Exception {

    
		if (isParrStubEnabled) {
		    
		    EntityRef entityRef =  new EntityRef();
			entityRef.setId(Long.valueOf(entityId));
			collectionPaymentArrangementCreate.setCollectionEntity(entityRef);
			if (collectionPaymentArrangementCreate.getBillingAccountRefs().size() > 0) {
				logger.info(":::::::BillingAccountRefs :::::::::::: {}", collectionPaymentArrangementCreate.getBillingAccountRefs().get(0).getName());
			}
			String requestPayload = mapper.writeValueAsString(collectionPaymentArrangementCreate);
            logger.info(":::::::In stub collectionPaymentArrangementCreate requestPayload :::::\n::::::: {}", requestPayload);
			return new CollectionPaymentArrangement();
		} else {

			// collectionCommonService.setAuditinfo(collectionPaymentArrangement.getAuditInfo(),
			// true);
			EntityRef entityRef =  new EntityRef();
			entityRef.setId(Long.valueOf(entityId));
			collectionPaymentArrangementCreate.setCollectionEntity(entityRef);
			String requestPayload = mapper.writeValueAsString(collectionPaymentArrangementCreate);
			logger.info("::::::::collectionPaymentArrangementCreate requestPayload :::::\n::::::: {}", requestPayload);
			String responseStr = telusAPIConnectivityService.executeTelusAPI(requestPayload, this.parrEndPointUrl,
					"POST","3161");
			CollectionPaymentArrangement collectionPaymentArrangement = mapper.readValue(responseStr,
					CollectionPaymentArrangement.class);
			return collectionPaymentArrangement;
		}
	}
	
	public CollectionPaymentArrangement updateParrStatus(Integer parrId, String status, String comments) throws Exception {
		
	//	CollectionPaymentArrangementStatus parrStatus = new CollectionPaymentArrangementStatus();
	//	List<CollectionPaymentArrangementStatus> statuses = new ArrayList<CollectionPaymentArrangementStatus>();
	//	parrStatus.setStatus(status);
	//	parrStatus.setAuditInfo(collectionCommonService.UpdateAuditInfo(new AuditInfo(), true));
	//	statuses.add(parrStatus);
		CollectionPaymentArrangementUpdate collectionPaymentArrangementUpdate = new CollectionPaymentArrangementUpdate();
	//	collectionPaymentArrangementUpdate.setStatuses(statuses);
		collectionPaymentArrangementUpdate.setId(parrId);
		CollectionPaymentArrangement collectionPaymentArrangement = updatePaymentArrangement(collectionPaymentArrangementUpdate);
		return collectionPaymentArrangement;
	}

	public CollectionPaymentArrangement updatePaymentArrangement(
			CollectionPaymentArrangementUpdate collectionPaymentArrangementUpdate) throws Exception {

		if (isParrStubEnabled) {
			
			String requestPayload = mapper.writeValueAsString(collectionPaymentArrangementUpdate);
            logger.info(":::::::In stub collectionPaymentArrangementUpdate requestPayload :::::\n::::::: {}", requestPayload);
			return new CollectionPaymentArrangement();
		} else {

			// collectionCommonService.setAuditinfo(collectionPaymentArrangement.getAuditInfo(),
			// false);
			String requestPayload = mapper.writeValueAsString(collectionPaymentArrangementUpdate);
			logger.info("::::::::collectionPaymentArrangementUpdate requestPayload ::::::::{}", requestPayload);
			String responseStr = telusAPIConnectivityService.executeTelusAPI(requestPayload, this.parrEndPointUrl,
					"PATCH","3161");
			CollectionPaymentArrangement collectionPaymentArrangement = mapper.readValue(responseStr,
					CollectionPaymentArrangement.class);
			return collectionPaymentArrangement;
		}
	}

	public List<CollectionPaymentArrangement> getPaymentArrangements(String entityId) throws Exception {

		List<CollectionPaymentArrangement> collectionPaymentArrangements = new ArrayList<CollectionPaymentArrangement>();
		if (isParrStubEnabled) {

			collectionPaymentArrangements = mapper.readValue(
					"[{\"id\":1,\"href\":\"BASE_URL/paymentArrangement/1\",\"allBillingAccountIncludedIndicator\":true,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccountMaps\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccountRef\":{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccount\":{\"id\":\"12345\",\"accountGroupId\":1,\"accountType\":\"B\",\"accountSubType\":\"I\",\"name\":\"A Company\",\"state\":\"O\",\"@type\":\"BillingAccount\"},\"billingSystemId\":10,\"billingSystemName\":\"CES9\",\"closingCycle\":6,\"collectionStatus\":\"INCOLL\",\"fraudIndicator\":false,\"involuntaryCeasedIndicator\":false,\"writeOffIndicator\":false,\"@type\":\"CollectionBillingAccountRef\"},\"validityIndicator\":true,\"@type\":\"CollectionPaymentArrangementBillingAccountMap\"}],\"collectionEntity\":{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"@referredType\":\"CollectionEntity\",\"@type\":\"EntityRef\"},\"comment\":\"string\",\"evaluationResult\":\"string\",\"expectedPaymentAmountToDate\":100.0,\"installments\":[{\"id\":1,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"date\":\"2023-01-01\",\"evaluationResult\":\"string\",\"evaluationTimestamp\":\"2023-01-01T09:00:00.001Z\",\"sequenceId\":1,\"validityIndicator\":true,\"@type\":\"CollectionPaymentInstallment\"}],\"receivedPaymentAmountToDate\":0.0,\"recurrence\":\"MONTHLY\",\"statuses\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"reason\":\"string\",\"status\":\"string\",\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.001Z\"},\"@type\":\"CollectionPaymentArrangementStatus\"}],\"@type\":\"CollectionPaymentArrangement\"},{\"id\":2,\"href\":\"BASE_URL/paymentArrangement/1\",\"allBillingAccountIncludedIndicator\":true,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccountMaps\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccountRef\":{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccount\":{\"id\":\"12345\",\"accountGroupId\":1,\"accountType\":\"B\",\"accountSubType\":\"I\",\"name\":\"A Company\",\"state\":\"O\",\"@type\":\"BillingAccount\"},\"billingSystemId\":10,\"billingSystemName\":\"CES9\",\"closingCycle\":6,\"collectionStatus\":\"INCOLL\",\"fraudIndicator\":false,\"involuntaryCeasedIndicator\":false,\"writeOffIndicator\":false,\"@type\":\"CollectionBillingAccountRef\"},\"validityIndicator\":true,\"@type\":\"CollectionPaymentArrangementBillingAccountMap\"}],\"collectionEntity\":{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"@referredType\":\"CollectionEntity\",\"@type\":\"EntityRef\"},\"comment\":\"string\",\"evaluationResult\":\"string\",\"expectedPaymentAmountToDate\":100.0,\"installments\":[{\"id\":1,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"date\":\"2023-01-01\",\"evaluationResult\":\"string\",\"evaluationTimestamp\":\"2023-01-01T09:00:00.001Z\",\"sequenceId\":1,\"validityIndicator\":true,\"@type\":\"CollectionPaymentInstallment\"}],\"receivedPaymentAmountToDate\":0.0,\"recurrence\":\"MONTHLY\",\"statuses\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"reason\":\"string\",\"status\":\"string\",\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.001Z\"},\"@type\":\"CollectionPaymentArrangementStatus\"}],\"@type\":\"CollectionPaymentArrangement\"},{\"id\":3,\"href\":\"BASE_URL/paymentArrangement/1\",\"allBillingAccountIncludedIndicator\":true,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccountMaps\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccountRef\":{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccount\":{\"id\":\"12345\",\"accountGroupId\":1,\"accountType\":\"B\",\"accountSubType\":\"I\",\"name\":\"A Company\",\"state\":\"O\",\"@type\":\"BillingAccount\"},\"billingSystemId\":10,\"billingSystemName\":\"CES9\",\"closingCycle\":6,\"collectionStatus\":\"INCOLL\",\"fraudIndicator\":false,\"involuntaryCeasedIndicator\":false,\"writeOffIndicator\":false,\"@type\":\"CollectionBillingAccountRef\"},\"validityIndicator\":true,\"@type\":\"CollectionPaymentArrangementBillingAccountMap\"}],\"collectionEntity\":{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"@referredType\":\"CollectionEntity\",\"@type\":\"EntityRef\"},\"comment\":\"string\",\"evaluationResult\":\"string\",\"expectedPaymentAmountToDate\":100.0,\"installments\":[{\"id\":1,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"date\":\"2023-01-01\",\"evaluationResult\":\"string\",\"evaluationTimestamp\":\"2023-01-01T09:00:00.001Z\",\"sequenceId\":1,\"validityIndicator\":true,\"@type\":\"CollectionPaymentInstallment\"}],\"receivedPaymentAmountToDate\":0.0,\"recurrence\":\"MONTHLY\",\"statuses\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"reason\":\"string\",\"status\":\"string\",\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.001Z\"},\"@type\":\"CollectionPaymentArrangementStatus\"}],\"@type\":\"CollectionPaymentArrangement\"}]",

					mapper.getTypeFactory().constructCollectionType(List.class, CollectionPaymentArrangement.class));

		} else {

			String responseStr = telusAPIConnectivityService.executeTelusAPI(null,
					this.parrEndPointUrl + "?id=" + entityId, "GET","3161");
			collectionPaymentArrangements = mapper.readValue(responseStr,
					mapper.getTypeFactory().constructCollectionType(List.class, CollectionPaymentArrangement.class));
		}
		return collectionPaymentArrangements;
	}

	public CollectionPaymentArrangement getPaymentArrangement(Integer parrId, Boolean history) throws Exception {

		CollectionPaymentArrangement collectionPaymentArrangement = null;
		if (isParrStubEnabled) {

			collectionPaymentArrangement = mapper.readValue(
					"{\"id\":1,\"href\":\"BASE_URL/paymentArrangement/1\",\"allBillingAccountIncludedIndicator\":true,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"billingAccountRefs\":[{\"id\":678,\"name\":\"Air Canada1\"},{\"id\":550,\"name\":\"Air India\"}],\"collectionEntity\":{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"@referredType\":\"CollectionEntity\",\"@type\":\"EntityRef\"},\"comment\":\"string\",\"evaluationResult\":\"string\",\"expectedPaymentAmountToDate\":100.0,\"installments\":[{\"id\":1,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"date\":\"2023-01-01\",\"evaluationResult\":\"string\",\"evaluationTimestamp\":\"2023-01-01T09:00:00.001Z\",\"sequenceId\":1,\"validityIndicator\":true,\"@type\":\"CollectionPaymentInstallment\"}],\"receivedPaymentAmountToDate\":0.0,\"recurrence\":\"MONTHLY\",\"statuses\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.001Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.001Z\",\"@type\":\"AuditInfo\"},\"reason\":\"string\",\"status\":\"string\",\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.001Z\"},\"@type\":\"CollectionPaymentArrangementStatus\"}],\"@type\":\"CollectionPaymentArrangement\"}",
					CollectionPaymentArrangement.class);
		} else {

			String responseStr = telusAPIConnectivityService.executeTelusAPI(null,
					this.parrEndPointUrl + "?id=" + parrId, "GET","3161");
			collectionPaymentArrangement = mapper.readValue(responseStr,
					mapper.getTypeFactory().constructCollectionType(List.class, CollectionPaymentArrangement.class));
		}
		return collectionPaymentArrangement;
	}

}
