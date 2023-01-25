/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.schemaservice.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBProcessMapper implements RowMapper<DBProcessVO> {
    @Override
    public DBProcessVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        final DBProcessVO vo = new DBProcessVO();
        vo.setDb(rs.getString("db"));
        vo.setId(rs.getString("Id"));
        vo.setUser(rs.getString("User"));
        vo.setHost(rs.getString("Host"));
        vo.setCommand(rs.getString("Command"));
        vo.setTime(rs.getString("Time"));
        vo.setState(rs.getString("State"));
        //vo.setProgress(rs.getString("Progress"));
        vo.setProgress("-");
        vo.setInfo(rs.getString("Info"));
        return vo;
    }
}