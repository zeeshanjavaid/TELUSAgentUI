package com.fico.ps.hermes.save;

import com.fico.ps.hermes.core.Action;

/**
 * @author MushfikKhan
 *
 */
public class SensitiveDataChange {

	private String domainClassName;

	private String entityName;

	private Object entityId;

	private Action entityAction;

	private String propertyName;
	
	private String oldValue;
	
	private String newValue;
	
	private String changeType;

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

	/**
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * @param entityName the entityName to set
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
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
	 * @return the entityAction
	 */
	public Action getEntityAction() {
		return entityAction;
	}

	/**
	 * @param entityAction the entityAction to set
	 */
	public void setEntityAction(Action entityAction) {
		this.entityAction = entityAction;
	}

	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * @return the oldValue
	 */
	public String getOldValue() {
		return oldValue;
	}

	/**
	 * @param oldValue the oldValue to set
	 */
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	/**
	 * @return the newValue
	 */
	public String getNewValue() {
		return newValue;
	}

	/**
	 * @param newValue the newValue to set
	 */
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	/**
	 * @return the changeType
	 */
	public String getChangeType() {
		return changeType;
	}

	/**
	 * @param changeType the changeType to set
	 */
	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

}
