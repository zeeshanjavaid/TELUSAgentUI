/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.schemaservice.model;

public class DBStatusVO {

    private String name, engine, version, rowsFormat, rows, avgRowLenght, dataLenght, maxDataLenght, indexLenght,
            dataFree, autoIncrement, createTime, updateTime, checkTime, collation, checksum, createOptions, comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRowsFormat() {
        return rowsFormat;
    }

    public void setRowsFormat(String rowsFormat) {
        this.rowsFormat = rowsFormat;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public String getAvgRowLenght() {
        return avgRowLenght;
    }

    public void setAvgRowLenght(String avgRowLenght) {
        this.avgRowLenght = avgRowLenght;
    }

    public String getDataLenght() {
        return dataLenght;
    }

    public void setDataLenght(String dataLenght) {
        this.dataLenght = dataLenght;
    }

    public String getMaxDataLenght() {
        return maxDataLenght;
    }

    public void setMaxDataLenght(String maxDataLenght) {
        this.maxDataLenght = maxDataLenght;
    }

    public String getIndexLenght() {
        return indexLenght;
    }

    public void setIndexLenght(String indexLenght) {
        this.indexLenght = indexLenght;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getCollation() {
        return collation;
    }

    public void setCollation(String collation) {
        this.collation = collation;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getCreateOptions() {
        return createOptions;
    }

    public void setCreateOptions(String createOptions) {
        this.createOptions = createOptions;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDataFree() {
        return dataFree;
    }

    public void setDataFree(String dataFree) {
        this.dataFree = dataFree;
    }

    public String getAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(String autoIncrement) {
        this.autoIncrement = autoIncrement;
    }
}