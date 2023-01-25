/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.schemaservice.model;

public class SchemaStatisticsVO {

    private String tableName, tableSchema, rowes, daata, indexSize, totalSize, idxFrac;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getRowes() {
        return rowes;
    }

    public void setRowes(String rowes) {
        this.rowes = rowes;
    }

    public String getDaata() {
        return daata;
    }

    public void setDaata(String daata) {
        this.daata = daata;
    }

    public String getIndexSize() {
        return indexSize;
    }

    public void setIndexSize(String indexSize) {
        this.indexSize = indexSize;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }

    public String getIdxFrac() {
        return idxFrac;
    }

    public void setIdxFrac(String idxFrac) {
        this.idxFrac = idxFrac;
    }
}