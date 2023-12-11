package com.fico.telus.model;

import io.swagger.client.model.Characteristic;
import io.swagger.client.model.CollectionTreatmentEntityRef;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public class CollectionActivityLogRes {


    private BigDecimal id = null;

    private CollectionTreatmentEntityRef collectionEntity = null;

    private OffsetDateTime collectionActivityTimestamp = null;

    private BigDecimal relatedBusinessEntityId = null;

    private String relatedBusinessEntityType = null;

    private String relatedBusinessEntityStatus = null;

    private OffsetDateTime relatedBusinessEntityCreatedTimestamp = null;

    private String relatedBusinessEntityCreatedBy = null;

    private String relatedBusinessEntityAssignedTo = null;

    private String relatedBusinessEntityAssignedTeam = null;

    private LocalDate relatedBusinessEntityDueDate = null;

    private String collectionActivityPerformedBy = null;

    private List<CollectionTreatmentEntityRef> billingAccountIdRefs = null;

    private String businessEntityEventType = null;

    private String relatedBusinessEntitySubType = null;

    private String activityReason = null;

    private List<Characteristic> additionalCharacteristics = null;

    private String comment = null;

    private LocalDate partitionKey = null;

    private String dataSourceId = null;

    private String href = null;

    private String baseType = null;

    private String type = null;

    private String schemaLocation = null;

    private Integer totalNoOfElement;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public CollectionTreatmentEntityRef getCollectionEntity() {
        return collectionEntity;
    }

    public void setCollectionEntity(CollectionTreatmentEntityRef collectionEntity) {
        this.collectionEntity = collectionEntity;
    }

    public OffsetDateTime getCollectionActivityTimestamp() {
        return collectionActivityTimestamp;
    }

    public void setCollectionActivityTimestamp(OffsetDateTime collectionActivityTimestamp) {
        this.collectionActivityTimestamp = collectionActivityTimestamp;
    }

    public BigDecimal getRelatedBusinessEntityId() {
        return relatedBusinessEntityId;
    }

    public void setRelatedBusinessEntityId(BigDecimal relatedBusinessEntityId) {
        this.relatedBusinessEntityId = relatedBusinessEntityId;
    }

    public String getRelatedBusinessEntityType() {
        return relatedBusinessEntityType;
    }

    public void setRelatedBusinessEntityType(String relatedBusinessEntityType) {
        this.relatedBusinessEntityType = relatedBusinessEntityType;
    }

    public String getRelatedBusinessEntityStatus() {
        return relatedBusinessEntityStatus;
    }

    public void setRelatedBusinessEntityStatus(String relatedBusinessEntityStatus) {
        this.relatedBusinessEntityStatus = relatedBusinessEntityStatus;
    }

    public OffsetDateTime getRelatedBusinessEntityCreatedTimestamp() {
        return relatedBusinessEntityCreatedTimestamp;
    }

    public void setRelatedBusinessEntityCreatedTimestamp(OffsetDateTime relatedBusinessEntityCreatedTimestamp) {
        this.relatedBusinessEntityCreatedTimestamp = relatedBusinessEntityCreatedTimestamp;
    }

    public String getRelatedBusinessEntityCreatedBy() {
        return relatedBusinessEntityCreatedBy;
    }

    public void setRelatedBusinessEntityCreatedBy(String relatedBusinessEntityCreatedBy) {
        this.relatedBusinessEntityCreatedBy = relatedBusinessEntityCreatedBy;
    }

    public String getRelatedBusinessEntityAssignedTo() {
        return relatedBusinessEntityAssignedTo;
    }

    public void setRelatedBusinessEntityAssignedTo(String relatedBusinessEntityAssignedTo) {
        this.relatedBusinessEntityAssignedTo = relatedBusinessEntityAssignedTo;
    }

    public String getRelatedBusinessEntityAssignedTeam() {
        return relatedBusinessEntityAssignedTeam;
    }

    public void setRelatedBusinessEntityAssignedTeam(String relatedBusinessEntityAssignedTeam) {
        this.relatedBusinessEntityAssignedTeam = relatedBusinessEntityAssignedTeam;
    }

    public LocalDate getRelatedBusinessEntityDueDate() {
        return relatedBusinessEntityDueDate;
    }

    public void setRelatedBusinessEntityDueDate(LocalDate relatedBusinessEntityDueDate) {
        this.relatedBusinessEntityDueDate = relatedBusinessEntityDueDate;
    }

    public String getCollectionActivityPerformedBy() {
        return collectionActivityPerformedBy;
    }

    public void setCollectionActivityPerformedBy(String collectionActivityPerformedBy) {
        this.collectionActivityPerformedBy = collectionActivityPerformedBy;
    }

    public List<CollectionTreatmentEntityRef> getBillingAccountIdRefs() {
        return billingAccountIdRefs;
    }

    public void setBillingAccountIdRefs(List<CollectionTreatmentEntityRef> billingAccountIdRefs) {
        this.billingAccountIdRefs = billingAccountIdRefs;
    }

    public String getBusinessEntityEventType() {
        return businessEntityEventType;
    }

    public void setBusinessEntityEventType(String businessEntityEventType) {
        this.businessEntityEventType = businessEntityEventType;
    }

    public String getRelatedBusinessEntitySubType() {
        return relatedBusinessEntitySubType;
    }

    public void setRelatedBusinessEntitySubType(String relatedBusinessEntitySubType) {
        this.relatedBusinessEntitySubType = relatedBusinessEntitySubType;
    }

    public String getActivityReason() {
        return activityReason;
    }

    public void setActivityReason(String activityReason) {
        this.activityReason = activityReason;
    }

    public List<Characteristic> getAdditionalCharacteristics() {
        return additionalCharacteristics;
    }

    public void setAdditionalCharacteristics(List<Characteristic> additionalCharacteristics) {
        this.additionalCharacteristics = additionalCharacteristics;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getPartitionKey() {
        return partitionKey;
    }

    public void setPartitionKey(LocalDate partitionKey) {
        this.partitionKey = partitionKey;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getBaseType() {
        return baseType;
    }

    public void setBaseType(String baseType) {
        this.baseType = baseType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSchemaLocation() {
        return schemaLocation;
    }

    public void setSchemaLocation(String schemaLocation) {
        this.schemaLocation = schemaLocation;
    }

    public Integer getTotalNoOfElement() {
        return totalNoOfElement;
    }

    public void setTotalNoOfElement(Integer totalNoOfElement) {
        this.totalNoOfElement = totalNoOfElement;
    }
}
