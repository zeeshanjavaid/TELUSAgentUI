package com.fico.telus.model;

import java.time.OffsetDateTime;

public class DisputeModel {

	private Integer disputeId;
	private Integer ban;
	private String banName;
	private String billingSystem;
	private Double disputeAmount;
	private Boolean collectionExclusion;
	private String status;
	private String createdBy;
	private OffsetDateTime createdDateTime;
	private String updatedBy;
	private OffsetDateTime updatedDateTime;
	
	
	
	public Integer getDisputeId() {
		return disputeId;
	}
	public void setDisputeId(Integer disputeId) {
		this.disputeId = disputeId;
	}
	public Integer getBan() {
		return ban;
	}
	public void setBan(Integer ban) {
		this.ban = ban;
	}
	public String getBanName() {
		return banName;
	}
	public void setBanName(String banName) {
		this.banName = banName;
	}
	public String getBillingSystem() {
		return billingSystem;
	}
	public void setBillingSystem(String billingSystem) {
		this.billingSystem = billingSystem;
	}
	public Double getDisputeAmount() {
		return disputeAmount;
	}
	public void setDisputeAmount(Double disputeAmount) {
		this.disputeAmount = disputeAmount;
	}
	public Boolean getCollectionExclusion() {
		return collectionExclusion;
	}
	public void setCollectionExclusion(Boolean collectionExclusion) {
		this.collectionExclusion = collectionExclusion;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public OffsetDateTime getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(OffsetDateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public OffsetDateTime getUpdatedDateTime() {
		return updatedDateTime;
	}
	public void setUpdatedDateTime(OffsetDateTime updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}
}
