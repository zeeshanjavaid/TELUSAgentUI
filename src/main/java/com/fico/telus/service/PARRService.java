package com.fico.telus.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fico.pscomponent.util.PropertiesUtil;

import io.swagger.client.model.CollectionPaymentArrangement;

@Service
public class PARRService {
	
	private static final Logger logger = LoggerFactory.getLogger(PARRService.class);

	private static final String IS_PARR_STUB_ENABLED = "IS_PARR_STUB_ENABLED";
	
	private static final String PARR_ENDPOINT_URL = "PARR_ENDPOINT_URL";

	@Autowired
	private PropertiesUtil propertiesUtil;
	
	@Autowired
	private TelusAPIConnectivityService telusAPIConnectivityService;
	
	private boolean isParrStubEnabled;
	
	private String parrEndPointUrl;
	
	@Autowired
	@Qualifier("customObjectMapper")
	private ObjectMapper mapper;

	@PostConstruct
	public void init() {

		this.isParrStubEnabled = Boolean.valueOf(propertyValueFrom(IS_PARR_STUB_ENABLED,"true"));
		this.parrEndPointUrl = propertyValueFrom(PARR_ENDPOINT_URL, "");
	}
	
	private String propertyValueFrom(String propertyName, String defaulValueIfNull)
    {
        String propertyValue = propertiesUtil.getPropertyValue(propertyName);
        if (propertyValue == null) {
            logger.info("property value is null, using default");
            propertyValue = defaulValueIfNull;
        }
        return propertyValue;
    }

	public CollectionPaymentArrangement createPaymentArrangement(CollectionPaymentArrangement collectionPaymentArrangement) throws Exception {

		if(isParrStubEnabled) {
			
			return collectionPaymentArrangement;
		} else {
			
			String requestPayload = mapper.writeValueAsString(collectionPaymentArrangement);			
			String responseStr = telusAPIConnectivityService.executeTelusAPI(requestPayload, this.parrEndPointUrl, "POST");
			collectionPaymentArrangement = mapper.readValue(responseStr, CollectionPaymentArrangement.class);
		}
		return collectionPaymentArrangement;
	}

	public CollectionPaymentArrangement updatePaymentArrangement(
			CollectionPaymentArrangement collectionPaymentArrangement) throws Exception {

		if (isParrStubEnabled) {

			return collectionPaymentArrangement;
		} else {

			String requestPayload = mapper.writeValueAsString(collectionPaymentArrangement);
			String responseStr = telusAPIConnectivityService.executeTelusAPI(requestPayload, this.parrEndPointUrl,
					"PATCH");
			collectionPaymentArrangement = mapper.readValue(responseStr, CollectionPaymentArrangement.class);
		}
		return collectionPaymentArrangement;
	}
	
	public List<CollectionPaymentArrangement> getPaymentArrangements(
			String entityId) throws Exception {

		List<CollectionPaymentArrangement> collectionPaymentArrangements = new ArrayList<CollectionPaymentArrangement>();
		if (isParrStubEnabled) {

			collectionPaymentArrangements = mapper.readValue("[{\"id\":1,\"href\":\"BASE_URL/paymentArrangement/1\",\"allBillingAccountIncludedIndicator\":true,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccountRefs\":[{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"@referredType\":\"CollectionBillingAccountRef\",\"@type\":\"EntityRef\"}],\"collectionEntity\":{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"@referredType\":\"CollectionEntity\",\"@type\":\"EntityRef\"},\"evaluationResult\":\"string\",\"expectedPaymentAmountToDate\":100.0,\"installments\":[{\"id\":1,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"date\":\"2023-01-01\",\"evaluationResult\":\"string\",\"evaluationTimestamp\":\"2023-01-01T09:00:00.00Z\",\"sequenceId\":1,\"validityIndicator\":true,\"@type\":\"CollectionPaymentInstallment\"}],\"receivedPaymentAmountToDate\":0.0,\"recurrence\":\"MONTHLY\",\"statuses\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"reason\":\"string\",\"status\":\"string\",\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.00Z\"},\"@type\":\"CollectionPaymentArrangementStatus\"}],\"@type\":\"CollectionPaymentArrangement\"}]",
					mapper.getTypeFactory().constructCollectionType(List.class, CollectionPaymentArrangement.class));
		} else {

			String responseStr = telusAPIConnectivityService.executeTelusAPI(null, this.parrEndPointUrl + "?id="+ entityId,
					"GET");
			collectionPaymentArrangements = mapper.readValue(responseStr,
					mapper.getTypeFactory().constructCollectionType(List.class, CollectionPaymentArrangement.class));
		}
		return collectionPaymentArrangements;
	}
	
	public CollectionPaymentArrangement getPaymentArrangement(Integer parrId, Boolean history) throws Exception  {
		
		CollectionPaymentArrangement collectionPaymentArrangement = null;
		if (isParrStubEnabled) {

			collectionPaymentArrangement = mapper.readValue("{\"id\":1,\"href\":\"BASE_URL/paymentArrangement/1\",\"allBillingAccountIncludedIndicator\":false,\"amount\":100.0,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"billingAccountRefs\":[{\"id\":1,\"href\":\"BASE_URL/billingAccountRef/1\",\"@referredType\":\"CollectionBillingAccountRef\",\"@type\":\"EntityRef\"}],\"collectionEntity\":{\"id\":1,\"href\":\"BASE_URL/entity/1\",\"@referredType\":\"CollectionEntity\",\"@type\":\"EntityRef\"},\"evaluationResult\":\"string\",\"statuses\":[{\"id\":1,\"auditInfo\":{\"createdBy\":\"t123456\",\"createdDateTime\":\"2023-01-01T09:00:00.00Z\",\"dataSource\":\"fico-app-123\",\"lastUpdatedBy\":\"t123456\",\"lastUpdatedDateTime\":\"2023-01-01T09:00:00.00Z\",\"@type\":\"AuditInfo\"},\"reason\":\"string\",\"status\":\"string\",\"validFor\":{\"startDateTime\":\"2023-01-01T09:00:00.00Z\"},\"@type\":\"CollectionPaymentArrangementStatus\"}],\"@type\":\"CollectionPaymentArrangement\"}",CollectionPaymentArrangement.class);
		} else {

			String responseStr = telusAPIConnectivityService.executeTelusAPI(null, this.parrEndPointUrl + "?id="+ parrId,
					"GET");
			collectionPaymentArrangement = mapper.readValue(responseStr,
					mapper.getTypeFactory().constructCollectionType(List.class, CollectionPaymentArrangement.class));
		}
		return collectionPaymentArrangement;
	}

}
