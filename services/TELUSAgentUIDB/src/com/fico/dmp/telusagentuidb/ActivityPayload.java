/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.telusagentuidb;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

/**
 * ActivityPayload generated by WaveMaker Studio.
 */
@Entity
@Table(name = "`ACTIVITY_PAYLOAD`", indexes = {
            @Index(name = "`FK_ACTIVITY_PAYLOAD_TO_AG9Ygt`", columnList = "`activityId`")})
public class ActivityPayload implements Serializable {


    private Integer id;

    private Integer activityId;

    private String requestId;

    private String responseId;

    private String createdBy;

    private LocalDateTime createdTime;

    @JsonProperty(access = Access.READ_ONLY)
    private byte[] dataPayload;

    private Boolean isRequestPayload;

    private Activity activity;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`ID`", nullable = false, scale = 0, precision = 10)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "`activityId`", nullable = true, scale = 0, precision = 10)
    public Integer getActivityId() {
        return this.activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    @Column(name = "`requestId`", nullable = true, length = 255)
    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Column(name = "`responseId`", nullable = true, length = 255)
    public String getResponseId() {
        return this.responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    @Column(name = "`createdBy`", nullable = true, length = 255)
    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "`createdTime`", nullable = true, columnDefinition="DATETIME")
    public LocalDateTime getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    @Lob()
    @Column(name = "`dataPayload`", nullable = true)
    public byte[] getDataPayload() {
        return this.dataPayload;
    }

    public void setDataPayload(byte[] dataPayload) {
        this.dataPayload = dataPayload;
    }

    @Column(name = "`isRequestPayload`", nullable = true)
    public Boolean getIsRequestPayload() {
        return this.isRequestPayload;
    }

    public void setIsRequestPayload(Boolean isRequestPayload) {
        this.isRequestPayload = isRequestPayload;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "`activityId`", referencedColumnName = "`ID`", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "`FK_ACTIVITY_PAYLOAD_TO_AG9Ygt`"))
    @Fetch(FetchMode.JOIN)
    public Activity getActivity() {
        return this.activity;
    }

    public void setActivity(Activity activity) {
        if(activity != null) {
            this.activityId = activity.getId();
        }

        this.activity = activity;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActivityPayload)) return false;
        final ActivityPayload activityPayload = (ActivityPayload) o;
        return Objects.equals(getId(), activityPayload.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}