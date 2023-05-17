package com.fico.telus.model;



public class ParrReports {

    private Integer parrId;
    private Long entityId;
    private String entityName;
    private String entityRisk;
    private String parrStatus;
    private String start;
    private String expiry;
    private String evaluation;
    private String parrAmt;
    private String perOfAmtRecieved_Exp;
    private String createdTeam;
    private String createdBy;


    public Integer getParrId() {
        return parrId;
    }

    public void setParrId(Integer parrId) {
        this.parrId = parrId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityRisk() {
        return entityRisk;
    }

    public void setEntityRisk(String entityRisk) {
        this.entityRisk = entityRisk;
    }

    public String getParrStatus() {
        return parrStatus;
    }

    public void setParrStatus(String parrStatus) {
        this.parrStatus = parrStatus;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public String getParrAmt() {
        return parrAmt;
    }

    public void setParrAmt(String parrAmt) {
        this.parrAmt = parrAmt;
    }


    public String getPerOfAmtRecieved_Exp() {
        return perOfAmtRecieved_Exp;
    }

    public void setPerOfAmtRecieved_Exp(String perOfAmtRecieved_Exp) {
        this.perOfAmtRecieved_Exp = perOfAmtRecieved_Exp;
    }

    public String getCreatedTeam() {
        return createdTeam;
    }

    public void setCreatedTeam(String createdTeam) {
        this.createdTeam = createdTeam;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
