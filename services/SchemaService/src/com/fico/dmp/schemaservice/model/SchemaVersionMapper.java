/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.schemaservice.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SchemaVersionMapper implements RowMapper<SchemaVersionVO>{

    @Override
    public SchemaVersionVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        final SchemaVersionVO schemaVersionVO = new SchemaVersionVO();
        schemaVersionVO.setVersionRank(rs.getInt("version_rank"));
        schemaVersionVO.setInstalledRank(rs.getInt("installed_rank"));
        schemaVersionVO.setVersion(rs.getString("version"));
        schemaVersionVO.setDescription(rs.getString("description"));
        schemaVersionVO.setType(rs.getString("type"));
        schemaVersionVO.setScript(rs.getString("script"));
        schemaVersionVO.setChecksum(rs.getInt("checksum"));
        schemaVersionVO.setInstalledBy(rs.getString("installed_by"));
        schemaVersionVO.setInstalledOn(rs.getTimestamp("installed_on"));
        schemaVersionVO.setExecutionTime(rs.getInt("execution_time"));
        schemaVersionVO.setSuccess(rs.getShort("success"));
        return schemaVersionVO;
    }
}