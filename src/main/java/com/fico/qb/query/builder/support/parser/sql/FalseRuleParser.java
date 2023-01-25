package com.fico.qb.query.builder.support.parser.sql;

import com.fico.qb.query.builder.support.model.IRule;
import com.fico.qb.query.builder.support.model.enums.EnumOperator;
import com.fico.qb.query.builder.support.model.sql.Operation;
import com.fico.qb.query.builder.support.parser.AbstractSqlRuleParser;
import com.fico.qb.query.builder.support.parser.JsonRuleParser;

/**
 * @author AnubhavDas
 * Custom boolean equals TRUE rule parser
 */
public class FalseRuleParser extends AbstractSqlRuleParser {

	@Override
	public boolean canParse(IRule rule) {
		return EnumOperator.FALSE.equals(rule.getOperator());
	}

	@Override
	public Operation parse(IRule rule, JsonRuleParser parser) {
		return new Operation(new StringBuffer(rule.getField()).append(" = ?"), false);
	}

}
