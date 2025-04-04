/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.telusagentuidb;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

/**
 * QrtzJobDetails generated by WaveMaker Studio.
 */
@Entity
@Table(name = "`QRTZ_JOB_DETAILS`")
@IdClass(QrtzJobDetailsId.class)
public class QrtzJobDetails implements Serializable {


    private String jobGroup;

    private String schedName;

    private String jobName;

    private String description;

    private String isDurable;

    private String isNonconcurrent;

    private String isUpdateData;

    private String jobClassName;

    @JsonProperty(access = Access.READ_ONLY)
    private byte[] jobData;

    private String requestsRecovery;

    @Id
    @Column(name = "`JOB_GROUP`", nullable = false, length = 200)
    public String getJobGroup() {
        return this.jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    @Id
    @Column(name = "`SCHED_NAME`", nullable = false, length = 120)
    public String getSchedName() {
        return this.schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    @Id
    @Column(name = "`JOB_NAME`", nullable = false, length = 200)
    public String getJobName() {
        return this.jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Column(name = "`DESCRIPTION`", nullable = true, length = 250)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "`IS_DURABLE`", nullable = false, length = 1)
    public String getIsDurable() {
        return this.isDurable;
    }

    public void setIsDurable(String isDurable) {
        this.isDurable = isDurable;
    }

    @Column(name = "`IS_NONCONCURRENT`", nullable = false, length = 1)
    public String getIsNonconcurrent() {
        return this.isNonconcurrent;
    }

    public void setIsNonconcurrent(String isNonconcurrent) {
        this.isNonconcurrent = isNonconcurrent;
    }

    @Column(name = "`IS_UPDATE_DATA`", nullable = false, length = 1)
    public String getIsUpdateData() {
        return this.isUpdateData;
    }

    public void setIsUpdateData(String isUpdateData) {
        this.isUpdateData = isUpdateData;
    }

    @Column(name = "`JOB_CLASS_NAME`", nullable = false, length = 250)
    public String getJobClassName() {
        return this.jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }

    @Lob()
    @Column(name = "`JOB_DATA`", nullable = true)
    public byte[] getJobData() {
        return this.jobData;
    }

    public void setJobData(byte[] jobData) {
        this.jobData = jobData;
    }

    @Column(name = "`REQUESTS_RECOVERY`", nullable = false, length = 1)
    public String getRequestsRecovery() {
        return this.requestsRecovery;
    }

    public void setRequestsRecovery(String requestsRecovery) {
        this.requestsRecovery = requestsRecovery;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QrtzJobDetails)) return false;
        final QrtzJobDetails qrtzJobDetails = (QrtzJobDetails) o;
        return Objects.equals(getJobGroup(), qrtzJobDetails.getJobGroup()) &&
                Objects.equals(getSchedName(), qrtzJobDetails.getSchedName()) &&
                Objects.equals(getJobName(), qrtzJobDetails.getJobName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getJobGroup(),
                getSchedName(),
                getJobName());
    }
}