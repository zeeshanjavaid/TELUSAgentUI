/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.telusagentuidb;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import java.io.Serializable;
import java.util.Objects;

public class QrtzSimpleTriggersId implements Serializable {

    private String schedName;
    private String triggerName;
    private String triggerGroup;

    public String getSchedName() {
        return this.schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getTriggerName() {
        return this.triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroup() {
        return this.triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QrtzSimpleTriggers)) return false;
        final QrtzSimpleTriggers qrtzSimpleTriggers = (QrtzSimpleTriggers) o;
        return Objects.equals(getSchedName(), qrtzSimpleTriggers.getSchedName()) &&
                Objects.equals(getTriggerName(), qrtzSimpleTriggers.getTriggerName()) &&
                Objects.equals(getTriggerGroup(), qrtzSimpleTriggers.getTriggerGroup());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSchedName(),
                getTriggerName(),
                getTriggerGroup());
    }
}