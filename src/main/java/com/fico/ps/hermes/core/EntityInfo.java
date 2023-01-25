package com.fico.ps.hermes.core;

/**
 * @author MushfikKhan
 *
 */
public class EntityInfo {

	private String enityClassName;
	
	private Object entityId;
	
	private String domainClassName;
	
	/**
	 * @return the enityClassName
	 */
	public String getEnityClassName() {
		return enityClassName;
	}

	/**
	 * @param enityClassName the enityClassName to set
	 */
	public void setEnityClassName(String enityClassName) {
		this.enityClassName = enityClassName;
	}

	/**
	 * @return the entityId
	 */
	public Object getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(Object entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return the domainClassName
	 */
	public String getDomainClassName() {
		return domainClassName;
	}

	/**
	 * @param domainClassName the domainClassName to set
	 */
	public void setDomainClassName(String domainClassName) {
		this.domainClassName = domainClassName;
	}
}
