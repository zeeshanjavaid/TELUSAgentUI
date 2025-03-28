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
import javax.persistence.Table;

/**
 * QrtzLocks generated by WaveMaker Studio.
 */
@Entity
@Table(name = "`QRTZ_LOCKS`")
@IdClass(QrtzLocksId.class)
public class QrtzLocks implements Serializable {


    private String lockName;

    private String schedName;

    @Id
    @Column(name = "`LOCK_NAME`", nullable = false, length = 40)
    public String getLockName() {
        return this.lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }

    @Id
    @Column(name = "`SCHED_NAME`", nullable = false, length = 120)
    public String getSchedName() {
        return this.schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }





    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QrtzLocks)) return false;
        final QrtzLocks qrtzLocks = (QrtzLocks) o;
        return Objects.equals(getLockName(), qrtzLocks.getLockName()) &&
                Objects.equals(getSchedName(), qrtzLocks.getSchedName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLockName(),
                getSchedName());
    }
}