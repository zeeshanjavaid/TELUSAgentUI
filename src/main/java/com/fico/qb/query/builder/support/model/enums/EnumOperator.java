/*
 * Copyright (c) 2017.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fico.qb.query.builder.support.model.enums;

/**
 * ---------------------------------------------------------------------------
 *
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/10/31 17:28
 * ---------------------------------------------------------------------------
 */
public enum EnumOperator {
    EQUAL("is equal to"),
    NOT_EQUAL("is not equal to"),
    IN("is in"),
    NOT_IN("is not in"),
    LESS("is less than"),
    LESS_OR_EQUAL("is less than or equal to"),
    GREATER("is greater than"),
    GREATER_OR_EQUAL("is greater than or equal to"),
    BETWEEN("between"),
    NOT_BETWEEN("not_between"),
//    BEGINS_WITH("begins_with"),
//    NOT_BEGINS_WITH("not_begins_with"),
    BEGINS_WITH("begins with"),
    NOT_BEGINS_WITH("does not begin with"),
    CONTAINS("contains"),
    NOT_CONTAINS("does not contain"),
    ENDS_WITH("ends with"),
    NOT_ENDS_WITH("does not end with"),
    IS_EMPTY("is empty"),
    IS_NOT_EMPTY("is not empty"),
    IS_NULL("is_null"),
    IS_NOT_NULL("is_not_null"),
    //FICO custom boolean true/false operations
	TRUE("true"),
	FALSE("false");

    private final String value;

    /**
     * 构造函数
     * @param value
     */
    EnumOperator(String value) {
        this.value = value;
    }

    /**
     * Getter method for property <tt>value</tt>.
     * @return property value of value
     * @author hewei
     */
    public String getValue() {
        return value;
    }

    /**
     * Getter method for property <tt>value</tt>.
     * @return property value of value
     * @author hewei
     */
    public String value() {
        return value;
    }

    /**
     * 比较
     * @param value
     * @return
     */
    public boolean equals(String value){
        if (value == null){
            return false;
        }
        return this.value.equals(value);
    }
}
