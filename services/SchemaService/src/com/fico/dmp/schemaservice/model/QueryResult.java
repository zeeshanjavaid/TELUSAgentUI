/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.schemaservice.model;

import java.io.Serializable;

public class QueryResult implements Serializable {

    private String sql;
    private String response;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "QueryResult{" +
                "sql='" + sql + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}