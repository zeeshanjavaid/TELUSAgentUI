package com.fico.telus.model;

import java.time.LocalDate;

public class AssignedEntitiesInClassicModel {

	private String banId = null;
	private String banName = null;
	private String cbucId = null;
	private String rcId = null;
	private String billingSystem = null;
	private String currentAr = null;
	private String ar30Days = null;
	private String ar60Days = null;
	private Double ar90Days = null;
	private Double ar120Days = null;
	private Double ar150Days = null;
	private Double ar180Days = null;
	private Double ar180DaysPlus = null;
	private String ar90DaysPlus = null;
	private String totalAr = null;
	private String totalOverDue = null;
	private LocalDate lastPaymentDate = null;
	private String odRemaining = null;
	private String acctStatus = null;
	private LocalDate acctStatusDate = null;
	private String acctType = null;
	private String acctSubType = null;
	private String disputeAmount = null;
	private Boolean suppresionFlag = null;
	private String language = null;
	private String marketSubSegment = null;
	private String province = null;
	private String cbu = null;
	private String cbucidName = null;
	private String rcidName = null;
	private String portfolioCategory = null;
	private String portfolioSubCategory = null;
	private Integer entityId = null;
	private String entityStatus = null;
	private String entityType = null;
	private String entityRisk = null;
	private String entityValue = null;
	private String entityOwnerId = null;
	private String banCollectionStatus = null;
	private LocalDate closingDate = null;
	private Integer closingCycle = null;
	private String assignedTeam = null;
	private Integer totalNumberOfElement;
	
	
	public String getBanId() {
		return banId;
	}
	public void setBanId(String banId) {
		this.banId = banId;
	}
	public String getBanName() {
		return banName;
	}
	public void setBanName(String banName) {
		this.banName = banName;
	}
	public String getCbucId() {
		return cbucId;
	}
	public void setCbucId(String cbucId) {
		this.cbucId = cbucId;
	}
	public String getRcId() {
		return rcId;
	}
	public void setRcId(String rcId) {
		this.rcId = rcId;
	}
	public String getBillingSystem() {
		return billingSystem;
	}
	public void setBillingSystem(String billingSystem) {
		this.billingSystem = billingSystem;
	}
	
	public Double getAr90Days() {
		return ar90Days;
	}
	public void setAr90Days(Double ar90Days) {
		this.ar90Days = ar90Days;
	}
	public Double getAr120Days() {
		return ar120Days;
	}
	public void setAr120Days(Double ar120Days) {
		this.ar120Days = ar120Days;
	}
	public Double getAr150Days() {
		return ar150Days;
	}
	public void setAr150Days(Double ar150Days) {
		this.ar150Days = ar150Days;
	}
	public Double getAr180Days() {
		return ar180Days;
	}
	public void setAr180Days(Double ar180Days) {
		this.ar180Days = ar180Days;
	}
	public Double getAr180DaysPlus() {
		return ar180DaysPlus;
	}
	public void setAr180DaysPlus(Double ar180DaysPlus) {
		this.ar180DaysPlus = ar180DaysPlus;
	}
	
	public LocalDate getLastPaymentDate() {
		return lastPaymentDate;
	}
	public void setLastPaymentDate(LocalDate lastPaymentDate) {
		this.lastPaymentDate = lastPaymentDate;
	}

	public String getOdRemaining() {
		return odRemaining;
	}
	public void setOdRemaining(String odRemaining) {
		this.odRemaining = odRemaining;
	}
	public String getAcctStatus() {
		return acctStatus;
	}
	public void setAcctStatus(String acctStatus) {
		this.acctStatus = acctStatus;
	}
	public LocalDate getAcctStatusDate() {
		return acctStatusDate;
	}
	public void setAcctStatusDate(LocalDate acctStatusDate) {
		this.acctStatusDate = acctStatusDate;
	}
	public String getAcctType() {
		return acctType;
	}
	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}
	public String getAcctSubType() {
		return acctSubType;
	}
	public void setAcctSubType(String acctSubType) {
		this.acctSubType = acctSubType;
	}

	public Boolean getSuppresionFlag() {
		return suppresionFlag;
	}
	public void setSuppresionFlag(Boolean suppresionFlag) {
		this.suppresionFlag = suppresionFlag;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getMarketSubSegment() {
		return marketSubSegment;
	}
	public void setMarketSubSegment(String marketSubSegment) {
		this.marketSubSegment = marketSubSegment;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCbu() {
		return cbu;
	}
	public void setCbu(String cbu) {
		this.cbu = cbu;
	}
	public String getCbucidName() {
		return cbucidName;
	}
	public void setCbucidName(String cbucidName) {
		this.cbucidName = cbucidName;
	}
	public String getRcidName() {
		return rcidName;
	}
	public void setRcidName(String rcidName) {
		this.rcidName = rcidName;
	}

	public String getPortfolioCategory() {
		return portfolioCategory;
	}
	public void setPortfolioCategory(String portfolioCategory) {
		this.portfolioCategory = portfolioCategory;
	}
	public String getPortfolioSubCategory() {
		return portfolioSubCategory;
	}
	public void setPortfolioSubCategory(String portfolioSubCategory) {
		this.portfolioSubCategory = portfolioSubCategory;
	}
	public Integer getEntityId() {
		return entityId;
	}
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
	public String getEntityStatus() {
		return entityStatus;
	}
	public void setEntityStatus(String entityStatus) {
		this.entityStatus = entityStatus;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public String getEntityRisk() {
		return entityRisk;
	}
	public void setEntityRisk(String entityRisk) {
		this.entityRisk = entityRisk;
	}
	public String getEntityValue() {
		return entityValue;
	}
	public void setEntityValue(String entityValue) {
		this.entityValue = entityValue;
	}
	public String getEntityOwnerId() {
		return entityOwnerId;
	}
	public void setEntityOwnerId(String entityOwnerId) {
		this.entityOwnerId = entityOwnerId;
	}
	public String getBanCollectionStatus() {
		return banCollectionStatus;
	}
	public void setBanCollectionStatus(String banCollectionStatus) {
		this.banCollectionStatus = banCollectionStatus;
	}
	public LocalDate getClosingDate() {
		return closingDate;
	}
	public void setClosingDate(LocalDate closingDate) {
		this.closingDate = closingDate;
	}
	public Integer getClosingCycle() {
		return closingCycle;
	}
	public void setClosingCycle(Integer closingCycle) {
		this.closingCycle = closingCycle;
	}
	public String getAssignedTeam() {
		return assignedTeam;
	}
	public void setAssignedTeam(String assignedTeam) {
		this.assignedTeam = assignedTeam;
	}
	public Integer getTotalNumberOfElement() {
		return totalNumberOfElement;
	}
	public void setTotalNumberOfElement(Integer totalNumberOfElement) {
		this.totalNumberOfElement = totalNumberOfElement;
	}
	public String getCurrentAr() {
		return currentAr;
	}
	public void setCurrentAr(String currentAr) {
		this.currentAr = currentAr;
	}
	public String getAr30Days() {
		return ar30Days;
	}
	public void setAr30Days(String ar30Days) {
		this.ar30Days = ar30Days;
	}
	public String getAr60Days() {
		return ar60Days;
	}
	public void setAr60Days(String ar60Days) {
		this.ar60Days = ar60Days;
	}
	public String getAr90DaysPlus() {
		return ar90DaysPlus;
	}
	public void setAr90DaysPlus(String ar90DaysPlus) {
		this.ar90DaysPlus = ar90DaysPlus;
	}
	public String getTotalAr() {
		return totalAr;
	}
	public void setTotalAr(String totalAr) {
		this.totalAr = totalAr;
	}
	public String getTotalOverDue() {
		return totalOverDue;
	}
	public void setTotalOverDue(String totalOverDue) {
		this.totalOverDue = totalOverDue;
	}
	public String getDisputeAmount() {
		return disputeAmount;
	}
	public void setDisputeAmount(String disputeAmount) {
		this.disputeAmount = disputeAmount;
	}

}
