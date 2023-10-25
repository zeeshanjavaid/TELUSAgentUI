package com.fico.telus.model;

import java.time.LocalDate;

public class TeamsActionViewResponseWIthTotalCount {

    private Integer actionId = null;

    private Integer entityId = null;

    private String entityName = null;

    private String entityOwner = null;

    private String entityType = null;

    private String collectionStatus = null;

    private String actionType = null;

    private String priority = null;

    private LocalDate dueDate = null;

    private String status = null;

    private String assignedTeam = null;

    private String assignedAgent = null;

    private String workCategory = null;

    private Double totalAr = null;

    private Double totalOverDue = null;

    private Integer totalNumberOfElement;

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
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

    public String getEntityOwner() {
        return entityOwner;
    }

    public void setEntityOwner(String entityOwner) {
        this.entityOwner = entityOwner;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getCollectionStatus() {
        return collectionStatus;
    }

    public void setCollectionStatus(String collectionStatus) {
        this.collectionStatus = collectionStatus;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignedTeam() {
        return assignedTeam;
    }

    public void setAssignedTeam(String assignedTeam) {
        this.assignedTeam = assignedTeam;
    }

    public String getAssignedAgent() {
        return assignedAgent;
    }

    public void setAssignedAgent(String assignedAgent) {
        this.assignedAgent = assignedAgent;
    }

    public String getWorkCategory() {
        return workCategory;
    }

    public void setWorkCategory(String workCategory) {
        this.workCategory = workCategory;
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

    public Integer getTotalNumberOfElement() {
        return totalNumberOfElement;
    }

    public void setTotalNumberOfElement(Integer totalNumberOfElement) {
        this.totalNumberOfElement = totalNumberOfElement;
    }
}
