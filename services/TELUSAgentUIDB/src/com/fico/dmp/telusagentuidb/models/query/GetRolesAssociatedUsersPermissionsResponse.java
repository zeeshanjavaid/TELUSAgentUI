/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.telusagentuidb.models.query;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/


import java.io.Serializable;
import java.util.Objects;

import com.wavemaker.runtime.data.annotations.ColumnAlias;

public class GetRolesAssociatedUsersPermissionsResponse implements Serializable {


    @ColumnAlias("roleId")
    private Long roleId;

    @ColumnAlias("role")
    private String role;

    @ColumnAlias("description")
    private String description;

    @ColumnAlias("permissionCount")
    private Long permissionCount;

    @ColumnAlias("userCount")
    private Long userCount;

    public Long getRoleId() {
        return this.roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPermissionCount() {
        return this.permissionCount;
    }

    public void setPermissionCount(Long permissionCount) {
        this.permissionCount = permissionCount;
    }

    public Long getUserCount() {
        return this.userCount;
    }

    public void setUserCount(Long userCount) {
        this.userCount = userCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetRolesAssociatedUsersPermissionsResponse)) return false;
        final GetRolesAssociatedUsersPermissionsResponse getRolesAssociatedUsersPermissionsResponse = (GetRolesAssociatedUsersPermissionsResponse) o;
        return Objects.equals(getRoleId(), getRolesAssociatedUsersPermissionsResponse.getRoleId()) &&
                Objects.equals(getRole(), getRolesAssociatedUsersPermissionsResponse.getRole()) &&
                Objects.equals(getDescription(), getRolesAssociatedUsersPermissionsResponse.getDescription()) &&
                Objects.equals(getPermissionCount(), getRolesAssociatedUsersPermissionsResponse.getPermissionCount()) &&
                Objects.equals(getUserCount(), getRolesAssociatedUsersPermissionsResponse.getUserCount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoleId(),
                getRole(),
                getDescription(),
                getPermissionCount(),
                getUserCount());
    }
}