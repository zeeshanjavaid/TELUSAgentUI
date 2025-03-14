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
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Activity generated by WaveMaker Studio.
 */
@Entity
@Table(name = "`ACTIVITY`", indexes = {
            @Index(name = "`INDEX_ACTIVITY_type`", columnList = "`type`"),
            @Index(name = "`INDEX_ACTIVITY_applicatiFZrX7`", columnList = "`applicationStatus`"),
            @Index(name = "`INDEX_ACTIVITY_description`", columnList = "`description`"),
            @Index(name = "`INDEX_ACTIVITY_applicationId`", columnList = "`applicationId`"),
            @Index(name = "`INDEX_ACTIVITY_name`", columnList = "`name`"),
            @Index(name = "`FK_ACTIVITY_TO_DomainVal6OQPf`", columnList = "`source`")})
public class Activity implements Serializable {


    private Integer id;

    private Integer source;

    private Integer applicationId;

    private Integer applicationStatus;

    private String description;

    private String name;

    private Integer type;

    private Timestamp startTime;

    private Timestamp endTime;

    private String status;

    private Integer duration;

    private Boolean isError;

    private String username;

    private DomainValue domainValueByApplicationStatus;

    private Application application;

    private DomainValue domainValueBySource;

    private DomainValue domainValueByType;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`ID`", nullable = false, scale = 0, precision = 10)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "`source`", nullable = true, scale = 0, precision = 10)
    public Integer getSource() {
        return this.source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    @Column(name = "`applicationId`", nullable = true, scale = 0, precision = 10)
    public Integer getApplicationId() {
        return this.applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    @Column(name = "`applicationStatus`", nullable = true, scale = 0, precision = 10)
    public Integer getApplicationStatus() {
        return this.applicationStatus;
    }

    public void setApplicationStatus(Integer applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    @Column(name = "`description`", nullable = true, length = 255)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "`name`", nullable = true, length = 255)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "`type`", nullable = true, scale = 0, precision = 10)
    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "`startTime`", nullable = true)
    public Timestamp getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Column(name = "`endTime`", nullable = true)
    public Timestamp getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Column(name = "`status`", nullable = true, length = 255)
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "`duration`", nullable = true, scale = 0, precision = 10)
    public Integer getDuration() {
        return this.duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Column(name = "`isError`", nullable = true)
    public Boolean getIsError() {
        return this.isError;
    }

    public void setIsError(Boolean isError) {
        this.isError = isError;
    }

    @Column(name = "`username`", nullable = true, length = 255)
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "`applicationStatus`", referencedColumnName = "`ID`", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "`FK_ACTIVITY_TO_DomainValayBFN`"))
    @Fetch(FetchMode.JOIN)
    public DomainValue getDomainValueByApplicationStatus() {
        return this.domainValueByApplicationStatus;
    }

    public void setDomainValueByApplicationStatus(DomainValue domainValueByApplicationStatus) {
        if(domainValueByApplicationStatus != null) {
            this.applicationStatus = domainValueByApplicationStatus.getId();
        }

        this.domainValueByApplicationStatus = domainValueByApplicationStatus;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "`applicationId`", referencedColumnName = "`id`", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "`FK_ACTIVITY_TO_APPLICATIGU2uG`"))
    @Fetch(FetchMode.JOIN)
    public Application getApplication() {
        return this.application;
    }

    public void setApplication(Application application) {
        if(application != null) {
            this.applicationId = application.getId();
        }

        this.application = application;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "`source`", referencedColumnName = "`ID`", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "`FK_ACTIVITY_TO_DomainVal6OQPf`"))
    @Fetch(FetchMode.JOIN)
    public DomainValue getDomainValueBySource() {
        return this.domainValueBySource;
    }

    public void setDomainValueBySource(DomainValue domainValueBySource) {
        if(domainValueBySource != null) {
            this.source = domainValueBySource.getId();
        }

        this.domainValueBySource = domainValueBySource;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "`type`", referencedColumnName = "`ID`", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "`FK_ACTIVITY_TO_DomainValqeU7B`"))
    @Fetch(FetchMode.JOIN)
    public DomainValue getDomainValueByType() {
        return this.domainValueByType;
    }

    public void setDomainValueByType(DomainValue domainValueByType) {
        if(domainValueByType != null) {
            this.type = domainValueByType.getId();
        }

        this.domainValueByType = domainValueByType;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activity)) return false;
        final Activity activity = (Activity) o;
        return Objects.equals(getId(), activity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}