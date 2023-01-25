/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.schemaservice.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBStatusMapper implements RowMapper<DBStatusVO> {
    @Override
    public DBStatusVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        final DBStatusVO vo = new DBStatusVO();
        vo.setName(rs.getString("Name"));
        vo.setEngine(rs.getString("Engine"));
        vo.setVersion(rs.getString("Version"));
        vo.setRowsFormat(rs.getString("Row_format"));
        vo.setRows(rs.getString("Rows"));
        vo.setAvgRowLenght(rs.getString("Avg_row_length"));
        vo.setDataLenght(rs.getString("Data_length"));
        vo.setMaxDataLenght(rs.getString("Max_data_length"));
        vo.setIndexLenght(rs.getString("Index_length"));
        vo.setDataFree(rs.getString("Data_free"));
        vo.setAutoIncrement(rs.getString("Auto_increment"));
        vo.setCreateTime(rs.getString("Create_time"));
        vo.setUpdateTime(rs.getString("Update_time"));
        vo.setCheckTime(rs.getString("Check_time"));
        vo.setCollation(rs.getString("Collation"));
        vo.setChecksum(rs.getString("Checksum"));
        vo.setCreateOptions(rs.getString("Create_options"));
        vo.setComment(rs.getString("Comment"));
        return vo;
    }
}