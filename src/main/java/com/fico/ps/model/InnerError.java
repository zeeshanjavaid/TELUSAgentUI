package com.fico.ps.model;

import java.util.List;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InnerError implements Serializable {

	
	private final String code;

	public InnerError(String code) {

		this.code=code;
	}


	public String getCode() {
		return code;
	}


}
