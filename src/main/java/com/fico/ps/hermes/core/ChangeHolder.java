package com.fico.ps.hermes.core;

import java.util.ArrayList;
import java.util.List;

import org.javers.core.diff.Change;
import org.javers.core.diff.changetype.NewObject;
import org.javers.core.diff.changetype.ObjectRemoved;
import org.javers.core.diff.changetype.ReferenceChange;
import org.javers.core.diff.changetype.container.ListChange;

/**
 * @author MushfikKhan
 *
 */
public class ChangeHolder {
	
	private Action action;
	
	private Object domainId;
	
	private Object entityId;
	
	private EntityInfo parentEntityInfo;
	
	private boolean persisted;
	
	private Object changedDomainObject;
	
	private Object changedEntityObject;
	
	// Holds ValueChange, InitialValueChange, TerminalValueChange
	private List<Change> changes; 
	
	private List<ListChange> listChanges;
	
	private List<ReferenceChange> referenceChanges;
	
	private NewObject newObject;
	
	private ObjectRemoved objectRemoved;

	/**
	 * @return
	 */
	public Action getAction() {
		if (action == null) {
			setAction(Action.NONE);
		}
		return action;
	}

	/**
	 * @param action
	 */
	public void setAction(Action action) {
		if (this.action == null) {
			this.action = action;
		} else if (action.equals(Action.CREATE) || action.equals(Action.DELETE)) {
			this.action = action;
		}
	}

	/**
	 * @return the domainId
	 */
	public Object getDomainId() {
		return domainId;
	}

	/**
	 * @param domainId the domainId to set
	 */
	public void setDomainId(Object domainId) {
		this.domainId = domainId;
	}	

	/**
	 * @return
	 */
	public Object getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId
	 */
	public void setEntityId(Object entityId) {
		if (entityId != null && entityId instanceof Integer && (Integer)entityId > 0) {
			this.entityId = entityId;
		}
	}

	/**
	 * @return
	 */
	public Object getChangedDomainObject() {
		return changedDomainObject;
	}

	/**
	 * @param changedDomainObject
	 */
	public void setChangedDomainObject(Object changedObject) {
		this.changedDomainObject = changedObject;
	}

	/**
	 * @return the changes
	 */
	public List<Change> getChanges() {
		return changes;
	}

	/**
	 * @param changes the changes to set
	 */
	public void setChanges(List<Change> changes) {
		this.changes = changes;
	}

	/**
	 * @return the listChanges
	 */
	public List<ListChange> getListChanges() {
		return listChanges;
	}

	/**
	 * @param listChanges the listChanges to set
	 */
	public void setListChanges(List<ListChange> listChanges) {
		this.listChanges = listChanges;
	}

	/**
	 * @return the referenceChanges
	 */
	public List<ReferenceChange> getReferenceChanges() {
		return referenceChanges;
	}

	/**
	 * @param referenceChanges the referenceChanges to set
	 */
	public void setReferenceChanges(List<ReferenceChange> referenceChanges) {
		this.referenceChanges = referenceChanges;
	}

	/**
	 * @return the newObject
	 */
	public NewObject getNewObject() {
		return newObject;
	}

	/**
	 * @param newObject the newObject to set
	 */
	public void setNewObject(NewObject newObject) {
		this.newObject = newObject;
	}

	/**
	 * @return the objectRemoved
	 */
	public ObjectRemoved getObjectRemoved() {
		return objectRemoved;
	}

	/**
	 * @param objectRemoved the objectRemoved to set
	 */
	public void setObjectRemoved(ObjectRemoved objectRemoved) {
		this.objectRemoved = objectRemoved;
	}
	
	/**
	 * @param change
	 */
	public void addChange (Change change) {
		if (this.changes == null) {
			this.changes = new ArrayList<Change>();
			this.changes.add(change);
		} else {
			this.changes.add(change);
		}
	}
	
	/**
	 * @param listChange
	 */
	public void addListChange(ListChange listChange) {
		if (this.listChanges == null) {
			this.listChanges = new ArrayList<ListChange>();
			this.listChanges.add(listChange);
		} else {
			this.listChanges.add(listChange);
		}
	}

	public void addReferenceChange(ReferenceChange referenceChange) {
		if (this.referenceChanges == null) {
			this.referenceChanges = new ArrayList<ReferenceChange>();
			this.referenceChanges.add(referenceChange);
		} else {
			this.referenceChanges.add(referenceChange);
		}
	}

	/**
	 * @return the changedEntityObject
	 */
	public Object getChangedEntityObject() {
		return changedEntityObject;
	}

	/**
	 * @param changedEntityObject the changedEntityObject to set
	 */
	public void setChangedEntityObject(Object changedEntityObject) {
		this.changedEntityObject = changedEntityObject;
	}

	/**
	 * @return the parentEntityInfo
	 */
	public EntityInfo getParentEntityInfo() {
		return parentEntityInfo;
	}

	/**
	 * @param parentEntityInfo the parentEntityInfo to set
	 */
	public void setParentEntityInfo(EntityInfo parentEntityInfo) {
		this.parentEntityInfo = parentEntityInfo;
	}

	/**
	 * @return the persisted
	 */
	public boolean isPersisted() {
		return persisted;
	}

	/**
	 * @param persisted the persisted to set
	 */
	public void setPersisted(boolean persisted) {
		this.persisted = persisted;
	}
}
