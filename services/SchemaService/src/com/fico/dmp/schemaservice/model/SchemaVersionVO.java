/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.schemaservice.model;

import java.io.Serializable;
import java.sql.Timestamp;

/*

MariaDB [Core]> describe schema_version;
+----------------+---------------+------+-----+---------------------+-------+
| Field          | Type          | Null | Key | Default             | Extra |
+----------------+---------------+------+-----+---------------------+-------+
| version_rank   | int(11)       | NO   | MUL | NULL                |       |
| installed_rank | int(11)       | NO   | MUL | NULL                |       |
| version        | varchar(50)   | NO   | PRI | NULL                |       |
| description    | varchar(200)  | NO   |     | NULL                |       |
| type           | varchar(20)   | NO   |     | NULL                |       |
| script         | varchar(1000) | NO   |     | NULL                |       |
| checksum       | int(11)       | YES  |     | NULL                |       |
| installed_by   | varchar(100)  | NO   |     | NULL                |       |
| installed_on   | timestamp     | NO   |     | current_timestamp() |       |
| execution_time | int(11)       | NO   |     | NULL                |       |
| success        | tinyint(1)    | NO   | MUL | NULL                |       |

 */

public class SchemaVersionVO implements Serializable {

    private Integer versionRank;
    private Integer installedRank;
    private String version;
    private String description;
    private String type;
    private String script;
    private Integer checksum;
    private String installedBy;
    private Timestamp installedOn;
    private Integer executionTime;
    private Short success;

    public Integer getVersionRank() {
        return versionRank;
    }

    public void setVersionRank(Integer versionRank) {
        this.versionRank = versionRank;
    }

    public Integer getInstalledRank() {
        return installedRank;
    }

    public void setInstalledRank(Integer installedRank) {
        this.installedRank = installedRank;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Integer getChecksum() {
        return checksum;
    }

    public void setChecksum(Integer checksum) {
        this.checksum = checksum;
    }

    public String getInstalledBy() {
        return installedBy;
    }

    public void setInstalledBy(String installedBy) {
        this.installedBy = installedBy;
    }

    public Timestamp getInstalledOn() {
        return installedOn;
    }

    public void setInstalledOn(Timestamp installedOn) {
        this.installedOn = installedOn;
    }

    public Integer getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Integer executionTime) {
        this.executionTime = executionTime;
    }

    public Short getSuccess() {
        return success;
    }

    public void setSuccess(Short success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "SchemaVersionVO{" +
                "versionRank=" + versionRank +
                ", installedRank=" + installedRank +
                ", version='" + version + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", script='" + script + '\'' +
                ", checksum=" + checksum +
                ", installedBy='" + installedBy + '\'' +
                ", installedOn=" + installedOn +
                ", executionTime=" + executionTime +
                ", success=" + success +
                '}';
    }
}