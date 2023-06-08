package com.fico.telus.model;

public class BanTravelHistoryModel {
	
	private String banId;
	private String transferInDT;
	private String transferOutDT;
	private String banStatus;
	private String banStatusDT;
	private Integer closingCycle;
	private String lastUpdatedBy;
	private String billingAccountRefId;
	
	
	public String getBanId() {
		return banId;
	}
	public void setBanId(String banId) {
		this.banId = banId;
	}

	public String getBanStatus() {
		return banStatus;
	}
	public void setBanStatus(String banStatus) {
		this.banStatus = banStatus;
	}
	public String getBanStatusDT() {
		return banStatusDT;
	}
	public void setBanStatusDT(String banStatusDT) {
		this.banStatusDT = banStatusDT;
	}
	public Integer getClosingCycle() {
		return closingCycle;
	}
	public void setClosingCycle(Integer closingCycle) {
		this.closingCycle = closingCycle;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public String getTransferInDT() {
		return transferInDT;
	}
	public void setTransferInDT(String transferInDT) {
		this.transferInDT = transferInDT;
	}
	public String getTransferOutDT() {
		return transferOutDT;
	}
	public void setTransferOutDT(String transferOutDT) {
		this.transferOutDT = transferOutDT;
	}
	public String getBillingAccountRefId() {
		return billingAccountRefId;
	}
	public void setBillingAccountRefId(String billingAccountRefId) {
		this.billingAccountRefId = billingAccountRefId;
	}
	@Override
	public String toString() {
		return "BanTravelHistoryModel [banId=" + banId + ", transferInDT=" + transferInDT + ", transferOutDT="
				+ transferOutDT + ", banStatus=" + banStatus + ", banStatusDT=" + banStatusDT + ", closingCycle="
				+ closingCycle + ", lastUpdatedBy=" + lastUpdatedBy + ", billingAccountRefId=" + billingAccountRefId
				+ "]";
	}


}
