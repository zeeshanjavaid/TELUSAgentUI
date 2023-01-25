/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.schemaservice.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBVariableMapper implements RowMapper<DBVariableVO> {
    @Override
    public DBVariableVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        final DBVariableVO vo = new DBVariableVO();
        vo.setVariableName(rs.getString("Variable_name"));
        vo.setValue(rs.getString("Value"));
        return vo;
    }
}