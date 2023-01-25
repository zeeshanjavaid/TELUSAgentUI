package com.fico.ps.http;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

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
