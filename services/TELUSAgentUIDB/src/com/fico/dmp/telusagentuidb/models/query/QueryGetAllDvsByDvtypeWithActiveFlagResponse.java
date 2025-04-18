/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.telusagentuidb.models.query;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/


import java.io.Serializable;
import java.util.Objects;

import com.wavemaker.runtime.data.annotations.ColumnAlias;

public class QueryGetAllDvsByDvtypeWithActiveFlagResponse implements Serializable {


    @ColumnAlias("Id")
    private Long id;

    @ColumnAlias("Code")
    private String code;

    @ColumnAlias("Description")
    private String description;

    @ColumnAlias("DVTypeCode")
    private String dvtypeCode;

    @ColumnAlias("IsDefault")
    private Boolean isDefault;

    @ColumnAlias("IsActive")
    private Boolean isActive;

    @ColumnAlias("RankOrder")
    private Long rankOrder;

    @ColumnAlias("IsSelected")
    private Long isSelected;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDvtypeCode() {
        return this.dvtypeCode;
    }

    public void setDvtypeCode(String dvtypeCode) {
        this.dvtypeCode = dvtypeCode;
    }

    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getRankOrder() {
        return this.rankOrder;
    }

    public void setRankOrder(Long rankOrder) {
        this.rankOrder = rankOrder;
    }

    public Long getIsSelected() {
        return this.isSelected;
    }

    public void setIsSelected(Long isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QueryGetAllDvsByDvtypeWithActiveFlagResponse)) return false;
        final QueryGetAllDvsByDvtypeWithActiveFlagResponse queryGetAllDvsByDvtypeWithActiveFlagResponse = (QueryGetAllDvsByDvtypeWithActiveFlagResponse) o;
        return Objects.equals(getId(), queryGetAllDvsByDvtypeWithActiveFlagResponse.getId()) &&
                Objects.equals(getCode(), queryGetAllDvsByDvtypeWithActiveFlagResponse.getCode()) &&
                Objects.equals(getDescription(), queryGetAllDvsByDvtypeWithActiveFlagResponse.getDescription()) &&
                Objects.equals(getDvtypeCode(), queryGetAllDvsByDvtypeWithActiveFlagResponse.getDvtypeCode()) &&
                Objects.equals(getIsDefault(), queryGetAllDvsByDvtypeWithActiveFlagResponse.getIsDefault()) &&
                Objects.equals(getIsActive(), queryGetAllDvsByDvtypeWithActiveFlagResponse.getIsActive()) &&
                Objects.equals(getRankOrder(), queryGetAllDvsByDvtypeWithActiveFlagResponse.getRankOrder()) &&
                Objects.equals(getIsSelected(), queryGetAllDvsByDvtypeWithActiveFlagResponse.getIsSelected());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),
                getCode(),
                getDescription(),
                getDvtypeCode(),
                getIsDefault(),
                getIsActive(),
                getRankOrder(),
                getIsSelected());
    }
}