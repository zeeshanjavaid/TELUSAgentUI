package com.fico.telus.model;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AgentInfo {
    @JsonProperty("firstName")
    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    String firstName;

    @JsonProperty("lastName")
    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    String lastName;

    @JsonProperty("userId")
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String userId;

    @JsonProperty("email")
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String email;

    @JsonProperty("status")
    public Object getStatus() {
        return this.status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    Object status;

    @JsonProperty("role")
    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    String role;

    @JsonProperty("workCategory")
    public ArrayList<String> getWorkCategory() {
        return this.workCategory;
    }

    public void setWorkCategory(ArrayList<String> workCategory) {
        this.workCategory = workCategory;
    }

    ArrayList<String> workCategory;

    @JsonProperty("emplId")
    public Object getEmplId() {
        return this.emplId;
    }

    public void setEmplId(Object emplId) {
        this.emplId = emplId;
    }

    Object emplId;

    @JsonProperty("teamId")
    public Object getTeamId() {
        return this.teamId;
    }

    public void setTeamId(Object teamId) {
        this.teamId = teamId;
    }

    Object teamId;

    @JsonProperty("teamManagerId")
    public Object getTeamManagerId() {
        return this.teamManagerId;
    }

    public void setTeamManagerId(Object teamManagerId) {
        this.teamManagerId = teamManagerId;
    }

    Object teamManagerId;

    @JsonProperty("active")
    public boolean getActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    boolean active;
}