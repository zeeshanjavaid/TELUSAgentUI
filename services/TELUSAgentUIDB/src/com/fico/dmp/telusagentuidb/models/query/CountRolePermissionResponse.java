/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.telusagentuidb.models.query;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/


import java.io.Serializable;
import java.util.Objects;

import com.wavemaker.runtime.data.annotations.ColumnAlias;

public class CountRolePermissionResponse implements Serializable {


    @ColumnAlias("COUNT(PermissionId)")
    private Long count_permissionId_;

    public Long getCount_permissionId_() {
        return this.count_permissionId_;
    }

    public void setCount_permissionId_(Long count_permissionId_) {
        this.count_permissionId_ = count_permissionId_;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CountRolePermissionResponse)) return false;
        final CountRolePermissionResponse countRolePermissionResponse = (CountRolePermissionResponse) o;
        return Objects.equals(getCount_permissionId_(), countRolePermissionResponse.getCount_permissionId_());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCount_permissionId_());
    }
}