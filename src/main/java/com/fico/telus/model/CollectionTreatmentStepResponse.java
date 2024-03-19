package com.fico.telus.model;

//import io.swagger.client.model.Characteristic;
//import io.swagger.client.model.CollectionTreatmentAuditInfo;
//import io.swagger.client.model.CollectionTreatmentEntityRef;
//import io.swagger.client.model.TelusChannel;

import telus.cdo.cnc.collmgmt.colltreatmentmgmt.model.AuditInfo;
import telus.cdo.cnc.collmgmt.colltreatmentmgmt.model.Characteristic;
import telus.cdo.cnc.collmgmt.colltreatmentmgmt.model.EntityRef;
import telus.cdo.cnc.collmgmt.colltreatmentmgmt.model.TelusChannel;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

public class CollectionTreatmentStepResponse {

    private AuditInfo auditInfo = null;

    private TelusChannel channel = null;

    private Long id = null;

    private LocalDate partitionKey = null;

    private EntityRef collectionTreatment = null;

    private LocalDate stepDate = null;

    private List<EntityRef> billingAccountRefs = null;

    private Boolean manualStepIndicator = null;

    private String status = null;

    private String stepTypeCode = null;

    private String languageCode = null;

    private String assignedAgentId = null;

    private String priority = null;

    private String reasonCode = null;

    private String assignedTeam = null;

    private List<Characteristic> additionalCharacteristics = null;

    private String comment = null;

    private URI href = null;

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

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public TelusChannel getChannel() {
        return channel;
    }

    public void setChannel(TelusChannel channel) {
        this.channel = channel;
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

    public EntityRef getCollectionTreatment() {
        return collectionTreatment;
    }

    public void setCollectionTreatment(EntityRef collectionTreatment) {
        this.collectionTreatment = collectionTreatment;
    }

    public LocalDate getStepDate() {
        return stepDate;
    }

    public void setStepDate(LocalDate stepDate) {
        this.stepDate = stepDate;
    }

    public List<EntityRef> getBillingAccountRefs() {
        return billingAccountRefs;
    }

    public void setBillingAccountRefs(List<EntityRef> billingAccountRefs) {
        this.billingAccountRefs = billingAccountRefs;
    }

    public Boolean getManualStepIndicator() {
        return manualStepIndicator;
    }

    public void setManualStepIndicator(Boolean manualStepIndicator) {
        this.manualStepIndicator = manualStepIndicator;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStepTypeCode() {
        return stepTypeCode;
    }

    public void setStepTypeCode(String stepTypeCode) {
        this.stepTypeCode = stepTypeCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getAssignedAgentId() {
        return assignedAgentId;
    }

    public void setAssignedAgentId(String assignedAgentId) {
        this.assignedAgentId = assignedAgentId;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getAssignedTeam() {
        return assignedTeam;
    }

    public void setAssignedTeam(String assignedTeam) {
        this.assignedTeam = assignedTeam;
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

    public URI getHref() {
        return href;
    }

    public void setHref(URI href) {
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
