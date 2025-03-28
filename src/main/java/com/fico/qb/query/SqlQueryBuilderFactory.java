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

package com.fico.qb.query;

import java.util.ArrayList;
import java.util.List;

import com.fico.qb.query.builder.config.SqlQueryBuilderConfig;
import com.fico.qb.query.builder.support.builder.SqlBuilder;
import com.fico.qb.query.builder.support.parser.AbstractSqlRuleParser;
import com.fico.qb.query.builder.support.parser.IRuleParser;
import com.fico.qb.query.builder.support.parser.sql.*;

/**
 * ---------------------------------------------------------------------------
 *
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/10/31 17:03
 * ---------------------------------------------------------------------------
 */
public class SqlQueryBuilderFactory extends AbstractQueryBuilderFactory {
    private SqlQueryBuilderConfig config;   // 配置

    /**
     * @param config
     */
    public SqlQueryBuilderFactory(SqlQueryBuilderConfig config) {
        super();
        this.config = config;

        // ------------------------ filter ---------------------------
    //    filters.add(new SqlInjectionAttackFilter(config.getDbType()));    // sql

        // ------------------------- group parser ----------------------
        groupParser = new DefaultGroupParser();

        // ---------------------- rule parser ----------------------------
        ruleParsers.add(new EqualRuleParser());
        ruleParsers.add(new NotEqualRuleParser());
        ruleParsers.add(new INRuleParser());
        ruleParsers.add(new NotInRuleParser());
        ruleParsers.add(new LessRuleParser());
        ruleParsers.add(new LessOrEqualRuleParser());
        ruleParsers.add(new GreaterRuleParser());
        ruleParsers.add(new GreaterOrEqualRuleParser());
        ruleParsers.add(new BetweenRuleParser());
        ruleParsers.add(new NotBetweenRuleParser());
        ruleParsers.add(new BeginsWithRuleParser());
        ruleParsers.add(new NotBeginsWithRuleParser());
        ruleParsers.add(new ContainsRuleParser());
        ruleParsers.add(new NotContainsRuleParser());
        ruleParsers.add(new EndsWithRuleParser());
        ruleParsers.add(new NotEndsWithRuleParser());
        ruleParsers.add(new IsEmptyRuleParser());
        ruleParsers.add(new IsNotEmptyRuleParser());
        ruleParsers.add(new IsNullRuleParser());
        ruleParsers.add(new IsNotNullRuleParser());
        //added for FICO custom boolean true/false operations
        ruleParsers.add(new TrueRuleParser());
        ruleParsers.add(new FalseRuleParser());
    }

    /**
     * 构造函数
     */
    public SqlQueryBuilderFactory() {
        this(new SqlQueryBuilderConfig());
    }

    /**
     * @return
     */
    public SqlBuilder builder() {
        List<IRuleParser> sqlRuleParsers = new ArrayList<>();
        for (IRuleParser parser : ruleParsers) {
            if (parser instanceof AbstractSqlRuleParser) {
                sqlRuleParsers.add(parser);
            }
        }
        return new SqlBuilder(groupParser, sqlRuleParsers, filters);
    }
}
