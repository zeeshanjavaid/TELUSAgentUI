package com.fico.telus.model;

import java.time.LocalDate;

public class AssignedEntitiesInEntityModel {

	private Integer entityId = null;
	private String entityType = null;
	private String rcId = null;
	private String cbucId = null;
	private String entityName = null;
	private Integer totalBan = null;
	private Integer totalDelinquentBans = null;
	private String risk = null;
	private String entityValue = null;
	private String entityCollectionStatus = null;
	private Boolean manualFlag = null;
	private String lastTreatment = null;
	private Double currentAr = null;
	private Double ar30Days = null;
	private Double ar60Days = null;
	private Double ar90Days = null;
	private Double ar120Days = null;
	private Double ar150Days = null;
	private Double ar180Days = null;
	private Double ar180DaysPlus = null;
	private Double ar90DaysPlus = null;
	private Double totalAr = null;
	private Double totalOverDue = null;
	private String odRemaining = null;
	private String entityOwnerId = null;
	private String primeWorkCategory = null;
	private String portfolioCategory = null;
	private String portfolioSubCategory = null;
	private Boolean ftnp = null;
	private Boolean disputeFlag = null;
	private LocalDate openActionDate = null;
	private Integer totalNumberOfElement;
	
	
	public Integer getEntityId() {
		return entityId;
	}
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public String getRcId() {
		return rcId;
	}
	public void setRcId(String rcId) {
		this.rcId = rcId;
	}
	public String getCbucId() {
		return cbucId;
	}
	public void setCbucId(String cbucId) {
		this.cbucId = cbucId;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public Integer getTotalBan() {
		return totalBan;
	}
	public void setTotalBan(Integer totalBan) {
		this.totalBan = totalBan;
	}
	public Integer getTotalDelinquentBans() {
		return totalDelinquentBans;
	}
	public void setTotalDelinquentBans(Integer totalDelinquentBans) {
		this.totalDelinquentBans = totalDelinquentBans;
	}
	public String getRisk() {
		return risk;
	}
	public void setRisk(String risk) {
		this.risk = risk;
	}
	public String getEntityValue() {
		return entityValue;
	}
	public void setEntityValue(String entityValue) {
		this.entityValue = entityValue;
	}
	public String getEntityCollectionStatus() {
		return entityCollectionStatus;
	}
	public void setEntityCollectionStatus(String entityCollectionStatus) {
		this.entityCollectionStatus = entityCollectionStatus;
	}
	public Boolean getManualFlag() {
		return manualFlag;
	}
	public void setManualFlag(Boolean manualFlag) {
		this.manualFlag = manualFlag;
	}
	public String getLastTreatment() {
		return lastTreatment;
	}
	public void setLastTreatment(String lastTreatment) {
		this.lastTreatment = lastTreatment;
	}
	public Double getCurrentAr() {
		return currentAr;
	}
	public void setCurrentAr(Double currentAr) {
		this.currentAr = currentAr;
	}
	public Double getAr30Days() {
		return ar30Days;
	}
	public void setAr30Days(Double ar30Days) {
		this.ar30Days = ar30Days;
	}
	public Double getAr60Days() {
		return ar60Days;
	}
	public void setAr60Days(Double ar60Days) {
		this.ar60Days = ar60Days;
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
	public Double getAr90DaysPlus() {
		return ar90DaysPlus;
	}
	public void setAr90DaysPlus(Double ar90DaysPlus) {
		this.ar90DaysPlus = ar90DaysPlus;
	}
	public Double getTotalAr() {
		return totalAr;
	}
	public void setTotalAr(Double totalAr) {
		this.totalAr = totalAr;
	}
	public Double getTotalOverDue() {
		return totalOverDue;
	}
	public void setTotalOverDue(Double totalOverDue) {
		this.totalOverDue = totalOverDue;
	}

	public String getEntityOwnerId() {
		return entityOwnerId;
	}
	public void setEntityOwnerId(String entityOwnerId) {
		this.entityOwnerId = entityOwnerId;
	}
	public String getPrimeWorkCategory() {
		return primeWorkCategory;
	}
	public void setPrimeWorkCategory(String primeWorkCategory) {
		this.primeWorkCategory = primeWorkCategory;
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
	public Boolean getFtnp() {
		return ftnp;
	}
	public void setFtnp(Boolean ftnp) {
		this.ftnp = ftnp;
	}
	public Boolean getDisputeFlag() {
		return disputeFlag;
	}
	public void setDisputeFlag(Boolean disputeFlag) {
		this.disputeFlag = disputeFlag;
	}
	public LocalDate getOpenActionDate() {
		return openActionDate;
	}
	public void setOpenActionDate(LocalDate openActionDate) {
		this.openActionDate = openActionDate;
	}
	public String getOdRemaining() {
		return odRemaining;
	}
	public void setOdRemaining(String odRemaining) {
		this.odRemaining = odRemaining;
	}
	public Integer getTotalNumberOfElement() {
		return totalNumberOfElement;
	}
	public void setTotalNumberOfElement(Integer totalNumberOfElement) {
		this.totalNumberOfElement = totalNumberOfElement;
	}

}
