package com.fico.telus.model;

import java.time.LocalDate;
import java.util.Date;


public class DisputeModel {


	private Integer id;
	private String ban;
	private String banName;
	private String billingSystem;
	private Double disputeAmount;
	private Boolean collectionExclusion;
	private String status;
	private String createdBy;
	private Date createdDateTime;
	private String updatedBy;
	private Date updatedDateTime;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getBan() {
		return ban;
	}
	public void setBan(String ban) {
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

	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Override
	public String toString() {
		return "DisputeModel [disputeId=" + id + ", ban=" + ban + ", banName=" + banName + ", billingSystem="
				+ billingSystem + ", disputeAmount=" + disputeAmount + ", collectionExclusion=" + collectionExclusion
				+ ", status=" + status + ", createdBy=" + createdBy + ", createdDateTime=" + createdDateTime
				+ ", updatedBy=" + updatedBy + ", updatedDateTime=" + updatedDateTime + "]";
	}
	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public Date getUpdatedDateTime() {
		return updatedDateTime;
	}
	public void setUpdatedDateTime(Date updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

}
