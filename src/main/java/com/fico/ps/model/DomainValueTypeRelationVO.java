package com.fico.ps.model;

public class DomainValueTypeRelationVO {
	private Boolean isRelationPresent;
	private DomainValueTypeVO parentDV1Type;
	private DomainValueTypeVO parentDV2Type;
	public Boolean isRelationPresent() {
		return isRelationPresent;
	}
	public void setRelationPresent(Boolean isRelationPresent) {
		this.isRelationPresent = isRelationPresent;
	}
	public DomainValueTypeVO getParentDV1Type() {
		return parentDV1Type;
	}
	public void setParentDV1Type(DomainValueTypeVO parentDV1Type) {
		this.parentDV1Type = parentDV1Type;
	}
	public DomainValueTypeVO getParentDV2Type() {
		return parentDV2Type;
	}
	public void setParentDV2Type(DomainValueTypeVO parentDV2Type) {
		this.parentDV2Type = parentDV2Type;
	}
}
