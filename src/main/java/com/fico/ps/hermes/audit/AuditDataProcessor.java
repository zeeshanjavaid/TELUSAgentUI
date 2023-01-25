package com.fico.ps.hermes.audit;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fico.dmp.telusagentuidb.User;
import com.fico.pscomponent.handlers.UserManagementHandler;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.javers.core.diff.Change;
import org.javers.core.diff.changetype.InitialValueChange;
import org.javers.core.diff.changetype.ReferenceChange;
import org.javers.core.diff.changetype.TerminalValueChange;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.metamodel.object.GlobalId;
import org.javers.core.metamodel.object.InstanceId;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.fico.ps.hermes.config.ApplicationConfig;
import com.fico.ps.hermes.config.ApplicationConfig.DomainModelSaveConfig;
import com.fico.ps.hermes.core.Action;
import com.fico.ps.hermes.core.ChangeHolder;
import com.fico.ps.hermes.util.Utility;

/**
 * @author MushfikKhan
 *
 */
@Component
public class AuditDataProcessor {
    
    @Autowired
	private UserManagementHandler userManagementHandler;

	private static final Logger logger = LoggerFactory.getLogger(AuditDataProcessor.class);

	/**
	 * @param savedRootObject
	 * @param changeMapByClassName
	 * @param changeHolder
	 * @param domainModelSaveConfig
	 * @param applicationConfig
	 * @return 
	 * @throws Exception 
	 */
	public AuditData process(Map<String, Map<Object, ChangeHolder>> changeMapByClassName,
			ChangeHolder changeHolder, DomainModelSaveConfig domainModelSaveConfig,
			ApplicationConfig applicationConfig) throws Exception {
		AuditData auditData = null;
		final User user = userManagementHandler.getLoggedInUser();
		//logger.info("------------------------");
		//logger.info(ToStringBuilder.reflectionToString(changeHolder)); 
		if (changeHolder != null && changeHolder.getAction() != null
				&& !changeHolder.getAction().equals(Action.NONE)) {

			auditData = new AuditData();
			auditData.setDomainClassName(domainModelSaveConfig.getDomainModelClassName());
			auditData.setEntityName(Utility.getClassNameWithoutPackage(domainModelSaveConfig.getSaveConfig().getEnityClassName()));
			auditData.setEntityId(changeHolder.getEntityId());
			auditData.setAction(changeHolder.getAction());
			if (user != null && user.getId() != null)
			    auditData.setCreatedBy(user.getId()); //TODO set user ID
			auditData.setCreatedOn(Timestamp.from(Instant.now()));
			if (changeHolder.getParentEntityInfo() != null) {
				auditData.setParentDomainClassName(changeHolder.getParentEntityInfo().getDomainClassName());
				auditData.setParentEntityName(Utility.getClassNameWithoutPackage(changeHolder.getParentEntityInfo().getEnityClassName()));
				auditData.setParentEntityId(changeHolder.getParentEntityInfo().getEntityId());
			}

			List<ChangeData> changedataList = getChangeData(changeMapByClassName, changeHolder, Utility.getIdAttributeNameByDomainClass(domainModelSaveConfig.getDomainModelClassName(),
							applicationConfig));
			auditData.setChangeDataList(changedataList);
			//logger.info("------------------------");
		} 
		return auditData; 
	}

	/**
	 * @param changeMapByClassName
	 * @param changeHolder
	 * @param idAttributeName
	 * @return
	 * @throws Exception 
	 */
	private List<ChangeData> getChangeData(Map<String, Map<Object, ChangeHolder>> changeMapByClassName, 
			ChangeHolder changeHolder, String idAttributeName) throws Exception {
		List<ChangeData> changeDataList = new ArrayList<ChangeData>();

		if (changeHolder.getChanges() != null) { // Property Changes
			for (Change change : changeHolder.getChanges()) {
				ChangeData changeData = null;

				if (change instanceof InitialValueChange) {
					InitialValueChange initialValueChange = (InitialValueChange) change;
					String propertyName = initialValueChange.getPropertyName();
					if (propertyName != null && !propertyName.equals(idAttributeName)) {
						changeData = new ChangeData();
						changeData.setPropertyName(propertyName);
						changeData.setOldValue(getAtomicValue(initialValueChange.getLeft()));
						changeData.setNewValue(getAtomicValue(initialValueChange.getRight()));
						changeData.setChangeType("INIT");
					}
				} else if (change instanceof TerminalValueChange) {
					TerminalValueChange terminalValueChange = (TerminalValueChange) change;
					String propertyName = terminalValueChange.getPropertyName();
					if (propertyName != null && !propertyName.equals(idAttributeName)) {
						changeData = new ChangeData();
						changeData.setPropertyName(propertyName);
						changeData.setOldValue(getAtomicValue(terminalValueChange.getLeft()));
						changeData.setNewValue(getAtomicValue(terminalValueChange.getRight()));
						changeData.setChangeType("UNSET");
					}
				} else if (change instanceof ValueChange) {
					ValueChange valueChange = (ValueChange) change;
					String propertyName = valueChange.getPropertyName();
					if (propertyName != null && !propertyName.equals(idAttributeName)) {
						changeData = new ChangeData();
						changeData.setPropertyName(propertyName);
						changeData.setOldValue(getAtomicValue(valueChange.getLeft()));
						changeData.setNewValue(getAtomicValue(valueChange.getRight()));
						changeData.setChangeType("CHANGE");
					}
				}
				if (changeData != null) {
					changeDataList.add(changeData);
				}
			}
		}

		if (changeHolder.getReferenceChanges() != null) { // Reference Changes
			for (ReferenceChange referenceChange : changeHolder.getReferenceChanges()) {
				ChangeData changeData = new ChangeData();
				changeData.setPropertyName(referenceChange.getPropertyName());

				if (referenceChange.getLeft() == null && referenceChange.getRight() != null) {// Reference added
					GlobalId globalIdRight = referenceChange.getRight();
					if (globalIdRight instanceof InstanceId) {
						InstanceId instanceId = (InstanceId) globalIdRight;
						changeData.setChangeType("INIT");
						changeData.setOldValue("");
						String newValue = getReferenceEntityId(instanceId, changeMapByClassName);
						changeData.setNewValue(newValue);
					}
				} else if (referenceChange.getLeft() != null && referenceChange.getRight() != null) {// Reference
																										// updated
					GlobalId globalIdRight = referenceChange.getRight();
					if (globalIdRight instanceof InstanceId) {
						InstanceId instanceId = (InstanceId) globalIdRight;
						String newValue = getReferenceEntityId(instanceId, changeMapByClassName);
						changeData.setChangeType("CHANGE");
						changeData.setNewValue(newValue);
					}
					GlobalId globalIdLeft = referenceChange.getLeft();
					if (globalIdLeft instanceof InstanceId) {
						InstanceId instanceId = (InstanceId) globalIdLeft;
						String oldValue = getReferenceEntityId(instanceId, changeMapByClassName);
						changeData.setOldValue(oldValue);
					}
				} else if (referenceChange.getLeft() != null && referenceChange.getRight() == null) {// Reference
																										// removed
					GlobalId globalIdLeft = referenceChange.getLeft();
					if (globalIdLeft instanceof InstanceId) {
						InstanceId instanceId = (InstanceId) globalIdLeft;
						String oldValue = getReferenceEntityId(instanceId, changeMapByClassName);
						changeData.setOldValue(oldValue);
						changeData.setChangeType("UNSET");
						changeData.setNewValue("");
					}
				}
				changeDataList.add(changeData);
			}
		}

		return changeDataList;
	}

	/**
	 * @param atomicValue
	 * @return
	 */
	private String getAtomicValue(Object atomicValue) {
		if (atomicValue != null && atomicValue.getClass() == Date.class) {
			Date dateValue = (Date) atomicValue;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);
			return formatter.format(dateValue.toInstant());
		} else {
			return atomicValue != null ? atomicValue.toString() : "";
		}
	}

	/**
	 * @param instanceId
	 * @param changeMapByClassName
	 * @return
	 */
	private String getReferenceEntityId(InstanceId instanceId,
			Map<String, Map<Object, ChangeHolder>> changeMapByClassName) {
		String refEntityId = "";
		if (instanceId.getCdoId() != null) {
			if (instanceId.getCdoId().getClass() == Integer.class) {
				Integer domainId = (Integer) instanceId.getCdoId();
				String domainClassName = instanceId.getTypeName();
				if (domainId < 0 && changeMapByClassName.get(domainClassName) != null
						&& changeMapByClassName.get(domainClassName).get(domainId) != null) {
					
					ChangeHolder changeHolder = changeMapByClassName.get(domainClassName).get(domainId);
					refEntityId = changeHolder.getEntityId().toString();
				} else {
					refEntityId = instanceId.getCdoId().toString();
				}
			}
		}
		return refEntityId;
	}
}
