package com.fico.telus.model;

import java.util.List;

public class OrderMgmtHistoryResponse {

    private String actionId;
    private String status;
     private String createdBy;
    private String assignedTo;
    private String getAssignedTeam;
    private String updatedBy;
    private String updatedOn;
    private String dueDate;
    private List<Integer> banList;
     private String comment;
    private String eventType;

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getGetAssignedTeam() {
        return getAssignedTeam;
    }

    public void setGetAssignedTeam(String getAssignedTeam) {
        this.getAssignedTeam = getAssignedTeam;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public List<Integer> getBanList() {
        return banList;
    }

    public void setBanList(List<Integer> banList) {
        this.banList = banList;
    }
    
     public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    
     public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
