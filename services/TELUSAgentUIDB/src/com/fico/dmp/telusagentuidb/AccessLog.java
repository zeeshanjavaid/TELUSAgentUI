/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.telusagentuidb;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * AccessLog generated by WaveMaker Studio.
 */
@Entity
@Table(name = "`ACCESS_LOG`", indexes = {
            @Index(name = "`INDEX_ACCESS_LOG_createdOn`", columnList = "`createdOn`")})
public class AccessLog implements Serializable {


    private Integer id;

    private String ipAddress;

    private String method;

    private String path;

    private Short resultCode;

    private Timestamp createdOn;

    private String traceId;

    private String username;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`ID`", nullable = false, scale = 0, precision = 10)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "`ipAddress`", nullable = true, length = 255)
    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Column(name = "`method`", nullable = true, length = 255)
    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Column(name = "`path`", nullable = true, length = 255)
    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Column(name = "`resultCode`", nullable = true, scale = 0, precision = 5)
    public Short getResultCode() {
        return this.resultCode;
    }

    public void setResultCode(Short resultCode) {
        this.resultCode = resultCode;
    }

    @Column(name = "`createdOn`", nullable = true)
    public Timestamp getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "`traceId`", nullable = true, length = 255)
    public String getTraceId() {
        return this.traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @Column(name = "`username`", nullable = true, length = 255)
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccessLog)) return false;
        final AccessLog accessLog = (AccessLog) o;
        return Objects.equals(getId(), accessLog.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}