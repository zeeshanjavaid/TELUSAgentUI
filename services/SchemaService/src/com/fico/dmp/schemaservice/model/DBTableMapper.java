/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.schemaservice.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBTableMapper implements RowMapper<DBTableVO> {

    @Override
    public DBTableVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        final DBTableVO vo = new DBTableVO();
        // STRING: tableName, tableType, engine, collation
        // INTEGER: tableRows, avgRowLength, dataLength;
        // Timestamp: createTime, updateTime;
        vo.setTableName(rs.getString("TABLE_NAME"));
        vo.setTableType(rs.getString("TABLE_TYPE"));
        vo.setEngine(rs.getString("ENGINE"));
        vo.setCollation(rs.getString("TABLE_COLLATION"));

        vo.setTableRows(rs.getInt("TABLE_ROWS"));
        vo.setAvgRowLength(rs.getInt("AVG_ROW_LENGTH"));
        vo.setDataLength(rs.getInt("DATA_LENGTH"));

        vo.setCreateTime(rs.getTimestamp("CREATE_TIME"));
        vo.setUpdateTime(rs.getTimestamp("UPDATE_TIME"));
        return vo;
    }
}