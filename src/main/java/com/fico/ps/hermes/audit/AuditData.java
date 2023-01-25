package com.fico.ps.hermes.audit;

import java.sql.Timestamp;
import java.util.List;

import com.fico.ps.hermes.core.Action;

/**
 * @author MushfikKhan
 *
 */
public class AuditData {

	    private Integer id;

	    private Integer createdBy;

		private Timestamp createdOn;

	    private String domainClassName;

	    private String entityName;

	    private Object entityId;
	    
	    private String parentDomainClassName;
	    
	    private String parentEntityName;

	    private Object parentEntityId;
	    
	    private Action action;

	    private String changeData;

	    private List<ChangeData> changeDataList;

	    public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public Integer getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(Integer createdBy) {
			this.createdBy = createdBy;
		}

		public Timestamp getCreatedOn() {
			return createdOn;
		}

		public void setCreatedOn(Timestamp createdOn) {
			this.createdOn = createdOn;
		}

		public String getDomainClassName() {
			return domainClassName;
		}

		public void setDomainClassName(String domainClassName) {
			this.domainClassName = domainClassName;
		}

		public String getEntityName() {
			return entityName;
		}

		public void setEntityName(String entityName) {
			this.entityName = entityName;
		}

		public Object getEntityId() {
			return entityId;
		}

		public void setEntityId(Object entityId) {
			this.entityId = entityId;
		}

		/**
		 * @return the parentDomainClassName
		 */
		public String getParentDomainClassName() {
			return parentDomainClassName;
		}

		/**
		 * @param parentDomainClassName the parentDomainClassName to set
		 */
		public void setParentDomainClassName(String parentDomainClassName) {
			this.parentDomainClassName = parentDomainClassName;
		}

		/**
		 * @return the parentEntityName
		 */
		public String getParentEntityName() {
			return parentEntityName;
		}

		/**
		 * @param parentEntityName the parentEntityName to set
		 */
		public void setParentEntityName(String parentEntityName) {
			this.parentEntityName = parentEntityName;
		}

		/**
		 * @return the parentEntityId
		 */
		public Object getParentEntityId() {
			return parentEntityId;
		}

		/**
		 * @param parentEntityId the parentEntityId to set
		 */
		public void setParentEntityId(Object parentEntityId) {
			this.parentEntityId = parentEntityId;
		}

		public String getChangeData() {
			return changeData;
		}

		public void setChangeData(String changeData) {
			this.changeData = changeData;
		}

		public Action getAction() {
			return action;
		}

		public void setAction(Action action) {
			this.action = action;
		}

		/**
		 * @return the changeDataList
		 */
		public List<ChangeData> getChangeDataList() {
			return changeDataList;
		}

		/**
		 * @param changeDataList the changeDataList to set
		 */
		public void setChangeDataList(List<ChangeData> changeDataList) {
			this.changeDataList = changeDataList;
		}
}
