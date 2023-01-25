package com.fico.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Utility class for parsing Queue's <b>Sort</b> information stored as JSON format in
 * the database
 */
@JsonInclude(value = Include.NON_NULL)
public class QueueSortMetaModel {

	private String fieldId;
	private String direction;
	private int rankOrder;

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public int getRankOrder() {
		return rankOrder;
	}

	public void setRankOrder(int rankOrder) {
		this.rankOrder = rankOrder;
	}
}
