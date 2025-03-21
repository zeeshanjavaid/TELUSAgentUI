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
 * DomainValue generated by WaveMaker Studio.
 */
@Entity
@Table(name = "`DomainValue`", indexes = {
            @Index(name = "`FK_DomainValue_TO_DomainpAvGp`", columnList = "`DomainValueType`"),
            @Index(name = "`FK_DomainValue_TO_USER_Cy1BDb`", columnList = "`CreatedBy`"),
            @Index(name = "`FK_DomainValue_TO_USER_UyHoBW`", columnList = "`UpdatedBy`")})
public class DomainValue implements Serializable {


    private Integer id;

    private String code;

    private Integer domainValueType;

    private Boolean isActive;

    private Boolean isDefault;

    private Integer rankOrder;

    private Integer createdBy;

    private Timestamp createdOn;

    private Integer updatedBy;

    private Timestamp updatedOn;

    private User userByCreatedBy;

    private DomainValueType domainValueTypeByDomainValueType;

    private User userByUpdatedBy;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`ID`", nullable = false, scale = 0, precision = 10)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "`Code`", nullable = true, length = 50)
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "`DomainValueType`", nullable = true, scale = 0, precision = 10)
    public Integer getDomainValueType() {
        return this.domainValueType;
    }

    public void setDomainValueType(Integer domainValueType) {
        this.domainValueType = domainValueType;
    }

    @Column(name = "`IsActive`", nullable = true)
    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Column(name = "`IsDefault`", nullable = true)
    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Column(name = "`RankOrder`", nullable = true, scale = 0, precision = 10)
    public Integer getRankOrder() {
        return this.rankOrder;
    }

    public void setRankOrder(Integer rankOrder) {
        this.rankOrder = rankOrder;
    }

    @Column(name = "`CreatedBy`", nullable = true, scale = 0, precision = 10)
    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "`CreatedOn`", nullable = true)
    public Timestamp getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "`UpdatedBy`", nullable = true, scale = 0, precision = 10)
    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Column(name = "`UpdatedOn`", nullable = true)
    public Timestamp getUpdatedOn() {
        return this.updatedOn;
    }

    public void setUpdatedOn(Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "`CreatedBy`", referencedColumnName = "`id`", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "`FK_DomainValue_TO_USER_Cy1BDb`"))
    @Fetch(FetchMode.JOIN)
    public User getUserByCreatedBy() {
        return this.userByCreatedBy;
    }

    public void setUserByCreatedBy(User userByCreatedBy) {
        if(userByCreatedBy != null) {
            this.createdBy = userByCreatedBy.getId();
        }

        this.userByCreatedBy = userByCreatedBy;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "`DomainValueType`", referencedColumnName = "`ID`", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "`FK_DomainValue_TO_DomainpAvGp`"))
    @Fetch(FetchMode.JOIN)
    public DomainValueType getDomainValueTypeByDomainValueType() {
        return this.domainValueTypeByDomainValueType;
    }

    public void setDomainValueTypeByDomainValueType(DomainValueType domainValueTypeByDomainValueType) {
        if(domainValueTypeByDomainValueType != null) {
            this.domainValueType = domainValueTypeByDomainValueType.getId();
        }

        this.domainValueTypeByDomainValueType = domainValueTypeByDomainValueType;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "`UpdatedBy`", referencedColumnName = "`id`", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "`FK_DomainValue_TO_USER_UyHoBW`"))
    @Fetch(FetchMode.JOIN)
    public User getUserByUpdatedBy() {
        return this.userByUpdatedBy;
    }

    public void setUserByUpdatedBy(User userByUpdatedBy) {
        if(userByUpdatedBy != null) {
            this.updatedBy = userByUpdatedBy.getId();
        }

        this.userByUpdatedBy = userByUpdatedBy;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainValue)) return false;
        final DomainValue domainValue = (DomainValue) o;
        return Objects.equals(getId(), domainValue.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}