package com.fico.ps.hermes.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.InitialValueChange;
import org.javers.core.diff.changetype.NewObject;
import org.javers.core.diff.changetype.ObjectRemoved;
import org.javers.core.diff.changetype.PropertyChange;
import org.javers.core.diff.changetype.ReferenceChange;
import org.javers.core.diff.changetype.TerminalValueChange;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.diff.changetype.container.ArrayChange;
import org.javers.core.diff.changetype.container.CollectionChange;
import org.javers.core.diff.changetype.container.ListChange;
import org.springframework.stereotype.Component;

import com.fico.ps.hermes.config.ApplicationConfig;
import com.fico.ps.hermes.config.ApplicationConfig.DomainModelSaveConfig;
import com.fico.ps.hermes.config.ApplicationConfig.DomainModelSaveConfig.DirectMapping.FieldMapping;

/**
 * @author MushfikKhan
 *
 */
@Component
public class ChangeProcessor {
	
	
//	private static Logger logger = LoggerFactory.getLogger(ChangeProcessor.class);
//	private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(ChangeProcessor.class.getName());


	/**
	 * @param diff
	 * @param applicationConfig
	 * @return
	 */
	public Map<String, Map<Object, ChangeHolder>> getChangeHolderMapByClassName(Diff diff, ApplicationConfig applicationConfig) {
		Map<String, Map<Object, ChangeHolder>> changeGroupByClassName = null;
		for (DomainModelSaveConfig domainModelSaveConfig : applicationConfig.getDomainModelSaveConfig()) {
			List<Change> changes = diff.getChanges((Change c) -> c.getAffectedGlobalId().getTypeName()
					.equals(domainModelSaveConfig.getDomainModelClassName()));
			if (changes != null && changes.size() > 0) {
				if (changeGroupByClassName == null) {
					changeGroupByClassName = new HashMap<String, Map<Object, ChangeHolder>>();
				}
				Map<Object, ChangeHolder> changeByIdMap = new HashMap<Object, ChangeHolder>();
				for (Change change : changes) {
					Object id = change.getAffectedLocalId();
					ChangeHolder changeHolder = null;
					if (changeByIdMap.get(id) == null) {
						changeHolder = new ChangeHolder();
						changeHolder.setDomainId(id);
						changeHolder.setEntityId(id);
						changeHolder.setChangedDomainObject(change.getAffectedObject().get());
					} else {
						changeHolder = changeByIdMap.get(id);
					}

					if (change instanceof InitialValueChange || change instanceof TerminalValueChange
							|| change instanceof ValueChange) {
						changeHolder.setAction(Action.UPDATE);
						changeHolder.addChange(change);
					} else if (change instanceof ListChange) {
						changeHolder.addListChange((ListChange) change);
					} else if (change instanceof ReferenceChange) {
						ReferenceChange referenceChange = (ReferenceChange) change;
						if (isUpdateApplicable(referenceChange, domainModelSaveConfig)) {
							changeHolder.setAction(Action.UPDATE);
						}
						changeHolder.addReferenceChange(referenceChange);
					} else if (change instanceof NewObject) {
						changeHolder.setAction(Action.CREATE);
						changeHolder.setNewObject((NewObject) change);
					} else if (change instanceof ObjectRemoved) {
						changeHolder.setAction(Action.DELETE);
						changeHolder.setObjectRemoved((ObjectRemoved) change);
					} else if (change instanceof CollectionChange) {
						// TODO
					} else if (change instanceof ArrayChange) {
						// TODO
					} else if (change instanceof PropertyChange) { // Keep it in the end as this is the super class of
																	// some the the classes ex. ReferenceChange
						changeHolder.setAction(Action.UPDATE);
						changeHolder.addChange(change);
					}
					changeByIdMap.put(id, changeHolder);
				}
				changeGroupByClassName.put(domainModelSaveConfig.getDomainModelClassName(), changeByIdMap);
			}
		}
		return changeGroupByClassName;
	}
	

	/**
	 * @param referenceChange
	 * @param domainModelSaveConfig
	 * @return
	 */
	private boolean isUpdateApplicable(ReferenceChange referenceChange,
			DomainModelSaveConfig domainModelSaveConfig) {
		boolean referenceChangeApplicable = false;
		String propetyName = referenceChange.getPropertyName();
		if (propetyName != null && !"".equals(propetyName.trim())) {
			if (domainModelSaveConfig.getDirectMapping() != null && domainModelSaveConfig.getDirectMapping().getFieldMappings() != null) {
				for (FieldMapping fieldMapping: domainModelSaveConfig.getDirectMapping().getFieldMappings()) {
					if (fieldMapping.getSourceField() != null) {
						String[] arr = fieldMapping.getSourceField().split("\\.");
						if (arr.length > 1 && propetyName.equals(arr[0])) {
							referenceChangeApplicable = true;
						}
					}
				}
			}
		}
		return referenceChangeApplicable;
	}
}
