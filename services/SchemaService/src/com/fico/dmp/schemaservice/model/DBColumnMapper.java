/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.schemaservice.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBColumnMapper implements RowMapper<DBColumnVO> {
    @Override
    public DBColumnVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        final DBColumnVO vo = new DBColumnVO();
        vo.setField(rs.getString("Field"));
        vo.setType(rs.getString("Type"));
        vo.setNullable(rs.getString("Null"));
        vo.setKey(rs.getString("Key"));
        vo.setDefaultVal(rs.getString("Default"));
        vo.setExtra(rs.getString("Extra"));
        return vo;
    }
}