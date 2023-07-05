package com.fico.telus.model;

import java.util.List;

public class OrderMgmtHistoryResponse {

    private String actionId;
    private String status;
    private String assignedTo;
    private String getAssignedTeam;
    private String updatedBy;
    private String updatedOn;
    private String dueDate;
    private List<String> banList;

    public OrderMgmtHistoryResponse(String actionId, String status, String assignedTo, String getAssignedTeam, String updatedBy, String updatedOn, String dueDate, List<String> banList) {
        this.actionId = actionId;
        this.status = status;
        this.assignedTo = assignedTo;
        this.getAssignedTeam = getAssignedTeam;
        this.updatedBy = updatedBy;
        this.updatedOn = updatedOn;
        this.dueDate = dueDate;
        this.banList = banList;
    }

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

    public List<String> getBanList() {
        return banList;
    }

    public void setBanList(List<String> banList) {
        this.banList = banList;
    }
}
