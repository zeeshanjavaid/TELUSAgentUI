package com.fico.ps.model;

public class DomainValueRelationVO {
	private Integer id;
	private Integer parent1DomainValueId;
	private String parent1DomainValueDescription;
	private Integer parent2DomainValueId;
	private String parent2DomainValueDescription;
	private Boolean isDeleted;
	 
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getParent1DomainValueId() {
		return parent1DomainValueId;
	}
	public void setParent1DomainValueId(Integer parent1DomainValueId) {
		this.parent1DomainValueId = parent1DomainValueId;
	}
	public Boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public Integer getParent2DomainValueId() {
		return parent2DomainValueId;
	}
	public void setParent2DomainValueId(Integer parent2DomainValueId) {
		this.parent2DomainValueId = parent2DomainValueId;
	}
	public String getParent1DomainValueDescription() {
		return parent1DomainValueDescription;
	}
	public void setParent1DomainValueDescription(String parent1DomainValueDescription) {
		this.parent1DomainValueDescription = parent1DomainValueDescription;
	}
	public String getParent2DomainValueDescription() {
		return parent2DomainValueDescription;
	}
	public void setParent2DomainValueDescription(String parent2DomainValueDescription) {
		this.parent2DomainValueDescription = parent2DomainValueDescription;
	}
}
