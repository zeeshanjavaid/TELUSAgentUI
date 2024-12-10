package com.fico.telus.model;

import telus.cdo.cnc.collmgmt.colltreatmentmgmt.model.AuditInfo;
import telus.cdo.cnc.collmgmt.colltreatmentmgmt.model.Characteristic;
import telus.cdo.cnc.collmgmt.colltreatmentmgmt.model.EntityRef;
import telus.cdo.cnc.collmgmt.colltreatmentmgmt.model.TelusChannel;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public class CollectionTreatmentStepResponse {

    private Long id = null;

    private LocalDate partitionKey = null;
    
    private URI href = null;
    
    private List<Characteristic> additionalCharacteristics = null;

    private String assignedAgentId = null;

    private String assignedTeam = null;

    private AuditInfo auditInfo = null;

    private List<EntityRef> billingAccountRefs = null;

    private EntityRef collectionTreatment = null;
    
    private EntityRef collectionTreatmentPath = null;

    private String comment = null;

    private String contentTypeCode = null;

    private String deliverSystemName = null;

    private String deliverSystemObjectId = null;

    private String deliverSystemObjectType = null;

    private String priority = null;

    private String queueId = null;

    private String reasonCode = null;

    private String status = null;

    private OffsetDateTime statusDateTime = null;

    private String stepCreationMethod = null;

    private LocalDate stepDate = null;

    private Integer stepSequenceNumber = null;

    private String stepTypeCode = null;

    private TelusChannel channel = null;

    private String baseType = null;

    private String type = null;

    private URI schemaLocation = null;

    private String assignedPersonForDefaultValue;
    
    private Integer totalNumberOfElement;

    public Integer getTotalNumberOfElement() {
        return totalNumberOfElement;
    }

    public void setTotalNumberOfElement(Integer totalNumberOfElement) {
        this.totalNumberOfElement = totalNumberOfElement;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPartitionKey() {
        return partitionKey;
    }

    public void setPartitionKey(LocalDate partitionKey) {
        this.partitionKey = partitionKey;
    }

    public URI getHref() {
        return href;
    }

    public void setHref(URI href) {
        this.href = href;
    }

    public List<Characteristic> getAdditionalCharacteristics() {
        return additionalCharacteristics;
    }

    public void setAdditionalCharacteristics(List<Characteristic> additionalCharacteristics) {
        this.additionalCharacteristics = additionalCharacteristics;
    }

    public String getAssignedAgentId() {
        return assignedAgentId;
    }

    public void setAssignedAgentId(String assignedAgentId) {
        this.assignedAgentId = assignedAgentId;
    }

    public String getAssignedTeam() {
        return assignedTeam;
    }

    public void setAssignedTeam(String assignedTeam) {
        this.assignedTeam = assignedTeam;
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

    public EntityRef getCollectionTreatment() {
        return collectionTreatment;
    }

    public void setCollectionTreatment(EntityRef collectionTreatment) {
        this.collectionTreatment = collectionTreatment;
    }

    public EntityRef getCollectionTreatmentPath() {
        return collectionTreatmentPath;
    }

    public void setCollectionTreatmentPath(EntityRef collectionTreatmentPath) {
        this.collectionTreatmentPath = collectionTreatmentPath;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getContentTypeCode() {
        return contentTypeCode;
    }

    public void setContentTypeCode(String contentTypeCode) {
        this.contentTypeCode = contentTypeCode;
    }

    public String getDeliverSystemName() {
        return deliverSystemName;
    }

    public void setDeliverSystemName(String deliverSystemName) {
        this.deliverSystemName = deliverSystemName;
    }
    public String getDeliverSystemObjectId() {
        return deliverSystemObjectId;
    }

    public void setDeliverSystemObjectId(String deliverSystemObjectId) {
        this.deliverSystemObjectId = deliverSystemObjectId;
    }
    public String getDeliverSystemObjectType() {
        return deliverSystemObjectType;
    }

    public void setDeliverSystemObjectType(String deliverSystemObjectType) {
        this.deliverSystemObjectType = deliverSystemObjectType;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
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

    public String getStepCreationMethod() {
        return stepCreationMethod;
    }

    public void setStepCreationMethod(String stepCreationMethod) {
        this.stepCreationMethod = stepCreationMethod;
    }

    public LocalDate getStepDate() {
        return stepDate;
    }

    public void setStepDate(LocalDate stepDate) {
        this.stepDate = stepDate;
    }

    public Integer getStepSequenceNumber() {
        return stepSequenceNumber;
    }

    public void setStepSequenceNumber(Integer stepSequenceNumber) {
        this.stepSequenceNumber = stepSequenceNumber;
    }

    public String getStepTypeCode() {
        return stepTypeCode;
    }

    public void setStepTypeCode(String stepTypeCode) {
        this.stepTypeCode = stepTypeCode;
    }

    public TelusChannel getChannel() {
        return channel;
    }

    public void setChannel(TelusChannel channel) {
        this.channel = channel;
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

    public URI getSchemaLocation() {
        return schemaLocation;
    }

    public void setSchemaLocation(URI schemaLocation) {
        this.schemaLocation = schemaLocation;
    }

    public String getAssignedPersonForDefaultValue() {
        return assignedPersonForDefaultValue;
    }

    public void setAssignedPersonForDefaultValue(String assignedPersonForDefaultValue) {
        this.assignedPersonForDefaultValue = assignedPersonForDefaultValue;
    }
}
