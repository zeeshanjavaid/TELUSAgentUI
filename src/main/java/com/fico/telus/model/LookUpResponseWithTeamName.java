package com.fico.telus.model;

public class LookUpResponseWithTeamName {


    private String banId ;

    private String banName ;

    private String billingSystem ;

    private String rcId;

    private String cbucId ;

    private Integer entityId ;

    private String entityName ;

    private String entityType;

    private String entityOwner ;

    private Boolean dntlFlag ;

    private Boolean currentEntityForBan ;

    private String teamName;
     private Integer totalNumberOfElement;

    public LookUpResponseWithTeamName() {
    }

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

    public String getBillingSystem() {
        return billingSystem;
    }

    public void setBillingSystem(String billingSystem) {
        this.billingSystem = billingSystem;
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

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityOwner() {
        return entityOwner;
    }

    public void setEntityOwner(String entityOwner) {
        this.entityOwner = entityOwner;
    }

    public Boolean getDntlFlag() {
        return dntlFlag;
    }

    public void setDntlFlag(Boolean dntlFlag) {
        this.dntlFlag = dntlFlag;
    }

    public Boolean getCurrentEntityForBan() {
        return currentEntityForBan;
    }

    public void setCurrentEntityForBan(Boolean currentEntityForBan) {
        this.currentEntityForBan = currentEntityForBan;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    
     public Integer getTotalNumberOfElement() {
        return totalNumberOfElement;
    }

    public void setTotalNumberOfElement(Integer totalNumberOfElement) {
        this.totalNumberOfElement = totalNumberOfElement;
    }
}
