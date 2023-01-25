/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.schemaservice.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SchemaStatisticsMapper implements RowMapper<SchemaStatisticsVO> {
    @Override
    public SchemaStatisticsVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        final SchemaStatisticsVO vo = new SchemaStatisticsVO();
        vo.setTableName(rs.getString("table_name"));
        vo.setTableSchema(rs.getString("table_schema"));
        vo.setRowes(rs.getString("rowes"));
        vo.setDaata(rs.getString("DAATA"));
        vo.setIndexSize(rs.getString("idx"));
        vo.setTotalSize(rs.getString("total_size"));
        vo.setIdxFrac(rs.getString("idxfrac"));
        return vo;
    }
}