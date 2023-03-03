/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.telusagentuidb.models.query;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/


import java.io.Serializable;
import java.util.Objects;

import com.wavemaker.runtime.data.annotations.ColumnAlias;

public class GetManagerBasedOnTeamIdResponse implements Serializable {


    @ColumnAlias("managerName")
    private String managerName;

    public String getManagerName() {
        return this.managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetManagerBasedOnTeamIdResponse)) return false;
        final GetManagerBasedOnTeamIdResponse getManagerBasedOnTeamIdResponse = (GetManagerBasedOnTeamIdResponse) o;
        return Objects.equals(getManagerName(), getManagerBasedOnTeamIdResponse.getManagerName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getManagerName());
    }
}