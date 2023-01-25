/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.ps.model.queue;

import java.util.List;

public class FilterGroup {
    String condition;
    List<FilterGroup> filterGroup;
    List<FilterCriteria> filterCriteria;

    public FilterGroup() {
    }

    public FilterGroup(String condition, List<FilterGroup> filterGroup, List<FilterCriteria> filterCriteria) {
        this.condition = condition;
        this.filterGroup = filterGroup;
        this.filterCriteria = filterCriteria;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public List<FilterGroup> getFilterGroup() {
        return filterGroup;
    }

    public void setFilterGroup(List<FilterGroup> filterGroup) {
        this.filterGroup = filterGroup;
    }

    public List<FilterCriteria> getFilterCriteria() {
        return filterCriteria;
    }

    public void setFilterCriteria(List<FilterCriteria> filterCriteria) {
        this.filterCriteria = filterCriteria;
    }

    @Override
    public String toString() {
        return "FilterGroup{" +
                "condition='" + condition + '\'' +
                ", filterGroup=" + filterGroup +
                ", filterCriteria=" + filterCriteria +
                '}';
    }
}