/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.telusagentuidb.models.query;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/


import java.io.Serializable;
import java.util.Objects;

import com.wavemaker.runtime.data.annotations.ColumnAlias;

public class QueryGetDomainValueByIdResponse implements Serializable {


    @ColumnAlias("Id")
    private Long id;

    @ColumnAlias("Code")
    private String code;

    @ColumnAlias("Description")
    private String description;

    @ColumnAlias("IsActive")
    private Boolean isActive;

    @ColumnAlias("IsDefault")
    private Boolean isDefault;

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

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
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
        if (!(o instanceof QueryGetDomainValueByIdResponse)) return false;
        final QueryGetDomainValueByIdResponse queryGetDomainValueByIdResponse = (QueryGetDomainValueByIdResponse) o;
        return Objects.equals(getId(), queryGetDomainValueByIdResponse.getId()) &&
                Objects.equals(getCode(), queryGetDomainValueByIdResponse.getCode()) &&
                Objects.equals(getDescription(), queryGetDomainValueByIdResponse.getDescription()) &&
                Objects.equals(getIsActive(), queryGetDomainValueByIdResponse.getIsActive()) &&
                Objects.equals(getIsDefault(), queryGetDomainValueByIdResponse.getIsDefault()) &&
                Objects.equals(getRankOrder(), queryGetDomainValueByIdResponse.getRankOrder()) &&
                Objects.equals(getIsSelected(), queryGetDomainValueByIdResponse.getIsSelected());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),
                getCode(),
                getDescription(),
                getIsActive(),
                getIsDefault(),
                getRankOrder(),
                getIsSelected());
    }
}