/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.schemaservice.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBIndexMapper implements RowMapper<DBIndexVO> {

    @Override
    public DBIndexVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        final DBIndexVO vo = new DBIndexVO();
        vo.setTableName(rs.getString("Table"));
        vo.setNonUnique(rs.getString("Non_unique"));
        vo.setIndexName(rs.getString("Key_name"));
        vo.setColumnName(rs.getString("Column_name"));
        vo.setNullable(rs.getString("Null"));
        vo.setCollation(rs.getString("Collation"));
        vo.setComment(rs.getString("Comment"));
        vo.setType(rs.getString("Index_type"));
        vo.setSeqInIndex(rs.getInt("Seq_in_index"));
        vo.setCardinality(rs.getInt("Cardinality"));
        return vo;
    }
}