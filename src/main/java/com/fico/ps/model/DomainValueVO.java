package com.fico.ps.model;

import java.util.Date;
import java.util.List;
import com.wavemaker.runtime.security.xss.XssDisable;

@XssDisable
public class DomainValueVO {
	
	private Integer id;
	private String code;
	private Integer rankOrder;
	private Boolean isActive;
	private Boolean isDefault;
	private String createdBy;
	private Date createdOn;
	private String updatedBy;
	private Date updatedOn;
	private DomainValueTypeVO domainValueType;
	private DomainValueTypeRelationVO domainValueTypeRelation;
	private List<DomainValueRelationVO> domainValueRelation;
	private List<DomainValueDescriptionVO> domainValueDescription;
	private DomainValueDescriptionVO defaultLocale;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getRankOrder() {
		return rankOrder;
	}
	public void setRankOrder(Integer rankOrder) {
		this.rankOrder = rankOrder;
	}
	public Boolean isDefault() {
		return isDefault;
	}
	public void setDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	public Boolean isActive() {
		return isActive;
	}
	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	public DomainValueTypeVO getDomainValueType() {
		return domainValueType;
	}
	public void setDomainValueType(DomainValueTypeVO domainValueType) {
		this.domainValueType = domainValueType;
	}
	public DomainValueTypeRelationVO getDomainValueTypeRelation() {
		return domainValueTypeRelation;
	}
	public void setDomainValueTypeRelation(DomainValueTypeRelationVO domainValueTypeRelation) {
		this.domainValueTypeRelation = domainValueTypeRelation;
	}
	public List<DomainValueRelationVO> getDomainValueRelation() {
		return domainValueRelation;
	}
	public void setDomainValueRelation(List<DomainValueRelationVO> domainValueRelation) {
		this.domainValueRelation = domainValueRelation;
	}
	public List<DomainValueDescriptionVO> getDomainValueDescription() {
		return domainValueDescription;
	}
	public void setDomainValueDescription(List<DomainValueDescriptionVO> domainValueDescription) {
		this.domainValueDescription = domainValueDescription;
	}
	public DomainValueDescriptionVO getDefaultLocale() {
		return defaultLocale;
	}
	public void setDefaultLocale(DomainValueDescriptionVO defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	
}
