/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.telusagentuidb.models.query;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/


import java.io.Serializable;
import java.util.Objects;

import com.wavemaker.runtime.data.annotations.ColumnAlias;

public class SearchUsersResponse implements Serializable {


    @ColumnAlias("firstName")
    private String firstName;

    @ColumnAlias("userId")
    private String userId;

    @ColumnAlias("lastName")
    private String lastName;

    @ColumnAlias("email")
    private String email;

    @ColumnAlias("active")
    private Boolean active;

    @ColumnAlias("emplId")
    private String emplId;

    @ColumnAlias("role")
    private String role;

    @ColumnAlias("workCategory")
    private String workCategory;

    @ColumnAlias("teamId")
    private String teamId;

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getEmplId() {
        return this.emplId;
    }

    public void setEmplId(String emplId) {
        this.emplId = emplId;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getWorkCategory() {
        return this.workCategory;
    }

    public void setWorkCategory(String workCategory) {
        this.workCategory = workCategory;
    }

    public String getTeamId() {
        return this.teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchUsersResponse)) return false;
        final SearchUsersResponse searchUsersResponse = (SearchUsersResponse) o;
        return Objects.equals(getFirstName(), searchUsersResponse.getFirstName()) &&
                Objects.equals(getUserId(), searchUsersResponse.getUserId()) &&
                Objects.equals(getLastName(), searchUsersResponse.getLastName()) &&
                Objects.equals(getEmail(), searchUsersResponse.getEmail()) &&
                Objects.equals(getActive(), searchUsersResponse.getActive()) &&
                Objects.equals(getEmplId(), searchUsersResponse.getEmplId()) &&
                Objects.equals(getRole(), searchUsersResponse.getRole()) &&
                Objects.equals(getWorkCategory(), searchUsersResponse.getWorkCategory()) &&
                Objects.equals(getTeamId(), searchUsersResponse.getTeamId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(),
                getUserId(),
                getLastName(),
                getEmail(),
                getActive(),
                getEmplId(),
                getRole(),
                getWorkCategory(),
                getTeamId());
    }
}