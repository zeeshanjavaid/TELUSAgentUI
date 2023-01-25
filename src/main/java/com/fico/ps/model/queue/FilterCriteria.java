/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.ps.model.queue;

public class FilterCriteria {
   String id;
   String field;
   String type;
   String input;
   String operator;
   Object value;

    public FilterCriteria() {
    }

    public FilterCriteria(String id, String field, String type, String input, String operator, Object value) {
        this.id = id;
        this.field = field;
        this.type = type;
        this.input = input;
        this.operator = operator;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return "FilterCriteria{" +
                "id='" + id + '\'' +
                ", field='" + field + '\'' +
                ", type='" + type + '\'' +
                ", input='" + input + '\'' +
                ", operator='" + operator + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}