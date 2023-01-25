package com.fico.ps.hermes.save;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Error implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String code;
	private final String desc;
	private final ZonedDateTime timestamp = ZonedDateTime.now();

	private List<Error> details;

	public Error(String code, String desc) {
		this.code = code;
		this.desc = desc;
		this.details = null;
	}

	public Error(String code, String desc, List<Error> details) {
		this.details = details;
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public List<Error> getDetails() {
		return details;
	}
	
	public void addError(Error error) {
		if (this.details == null) {
			this.details = new ArrayList<Error>();
		}
		details.add(error);
	}

	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

}