package com.fico.telus.model;

//import io.swagger.client.model.AuditInfo;
//import io.swagger.client.model.CollectionPaymentInstallment;
//import io.swagger.client.model.EntityRef;

import telus.cdo.cnc.collmgmt.collentitymgmt.model.AuditInfo;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.CollectionPaymentInstallment;
import telus.cdo.cnc.collmgmt.collentitymgmt.model.EntityRef;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class CollectionPaymentArrangementResponse {


    private Integer id = null;

    private URI href = null;

    private Boolean allBillingAccountIncludedIndicator = null;

    private Double amount = null;

    private AuditInfo auditInfo = null;

    private List<EntityRef> billingAccountRefs = null;

    private EntityRef collectionEntity = null;

    private String comment = null;

    private String evaluationResult = null;

    private Double expectedPaymentAmountToDate = null;

    private List<CollectionPaymentInstallment> installments = new ArrayList();

    private Double receivedPaymentAmountToDate = null;

    private String recurrence = null;

    private String status = null;

    private OffsetDateTime statusDateTime = null;

    private String statusReason = null;

    private String baseType = null;

    private URI schemaLocation = null;

    private String type = null;

    private Integer totalNumberOfElement;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public URI getHref() {
        return href;
    }

    public void setHref(URI href) {
        this.href = href;
    }

    public Boolean getAllBillingAccountIncludedIndicator() {
        return allBillingAccountIncludedIndicator;
    }

    public void setAllBillingAccountIncludedIndicator(Boolean allBillingAccountIncludedIndicator) {
        this.allBillingAccountIncludedIndicator = allBillingAccountIncludedIndicator;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public List<EntityRef> getBillingAccountRefs() {
        return billingAccountRefs;
    }

    public void setBillingAccountRefs(List<EntityRef> billingAccountRefs) {
        this.billingAccountRefs = billingAccountRefs;
    }

    public EntityRef getCollectionEntity() {
        return collectionEntity;
    }

    public void setCollectionEntity(EntityRef collectionEntity) {
        this.collectionEntity = collectionEntity;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEvaluationResult() {
        return evaluationResult;
    }

    public void setEvaluationResult(String evaluationResult) {
        this.evaluationResult = evaluationResult;
    }

    public Double getExpectedPaymentAmountToDate() {
        return expectedPaymentAmountToDate;
    }

    public void setExpectedPaymentAmountToDate(Double expectedPaymentAmountToDate) {
        this.expectedPaymentAmountToDate = expectedPaymentAmountToDate;
    }

    public List<CollectionPaymentInstallment> getInstallments() {
        return installments;
    }

    public void setInstallments(List<CollectionPaymentInstallment> installments) {
        this.installments = installments;
    }

    public Double getReceivedPaymentAmountToDate() {
        return receivedPaymentAmountToDate;
    }

    public void setReceivedPaymentAmountToDate(Double receivedPaymentAmountToDate) {
        this.receivedPaymentAmountToDate = receivedPaymentAmountToDate;
    }

    public String getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(String recurrence) {
        this.recurrence = recurrence;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OffsetDateTime getStatusDateTime() {
        return statusDateTime;
    }

    public void setStatusDateTime(OffsetDateTime statusDateTime) {
        this.statusDateTime = statusDateTime;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public String getBaseType() {
        return baseType;
    }

    public void setBaseType(String baseType) {
        this.baseType = baseType;
    }

    public URI getSchemaLocation() {
        return schemaLocation;
    }

    public void setSchemaLocation(URI schemaLocation) {
        this.schemaLocation = schemaLocation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTotalNumberOfElement() {
        return totalNumberOfElement;
    }

    public void setTotalNumberOfElement(Integer totalNumberOfElement) {
        this.totalNumberOfElement = totalNumberOfElement;
    }
}
