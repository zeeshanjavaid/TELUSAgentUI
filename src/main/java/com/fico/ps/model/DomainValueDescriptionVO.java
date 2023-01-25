package com.fico.ps.model;
import com.wavemaker.runtime.security.xss.XssDisable;

@XssDisable
public class DomainValueDescriptionVO {
    private Integer id;
	private String locale;
	private String description;
	private Boolean isDeleted;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
}
