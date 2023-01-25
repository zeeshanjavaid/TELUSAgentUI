package com.fico.ps.hermes.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fico.ps.hermes.audit.AuditData;
import com.fico.ps.hermes.audit.ChangeData;
import com.fico.ps.hermes.config.ApplicationConfig;
import com.fico.ps.hermes.config.SensitivityConfig;
import com.fico.ps.hermes.config.SensitivityConfig.DomainModelClassConfigs;
import com.fico.ps.hermes.config.SensitivityConfig.DomainModelClassConfigs.DomainModelClassConfig;
import com.fico.ps.hermes.save.SensitiveDataChange;

/**
 * @author MushfikKhan
 *
 */
@Component
public class SensitivityCheck {
	private static Logger logger = LoggerFactory.getLogger(SensitivityCheck.class);
//	private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(ChangeProcessor.class.getName());

	@Autowired
	SensitivityConfig sensitivityConfig;
	
	/**
	 * @param changeGroupByClassName
	 * @return
	 */
	public Map<String, List<SensitiveDataChange>> getSensitiveChanges(List<AuditData> auditDataList, ApplicationConfig applicationConfig) {
		Map<String, List<SensitiveDataChange>> sensitiveDataChangeMap = null;

		if (sensitivityConfig.isEnabled() && sensitivityConfig.getSensitivityConfigMap() != null) {
			for (AuditData auditData: auditDataList) {
				for (Entry<String, DomainModelClassConfigs> sensitivityConfig : sensitivityConfig.getSensitivityConfigMap().entrySet()) {
					String key = sensitivityConfig.getKey();
					DomainModelClassConfigs domainModelClassConfigs = sensitivityConfig.getValue();
					if (domainModelClassConfigs.getDomainModelClassConfigsMap() != null && domainModelClassConfigs.getDomainModelClassConfigsMap().get(auditData.getDomainClassName()) != null) {
						DomainModelClassConfig domainModelClassConfig = domainModelClassConfigs.getDomainModelClassConfigsMap().get(auditData.getDomainClassName());
						for (String attribute : domainModelClassConfig.getAttributes()) {
							ChangeData changeData = getChangedDataByAttribute(attribute, auditData.getChangeDataList());
							if (changeData != null) {
								SensitiveDataChange sensitiveDataChange = new SensitiveDataChange();
								sensitiveDataChange.setDomainClassName(auditData.getDomainClassName());
								sensitiveDataChange.setEntityName(auditData.getEntityName());
								sensitiveDataChange.setEntityId(auditData.getEntityId());
								sensitiveDataChange.setEntityAction(auditData.getAction());
								sensitiveDataChange.setPropertyName(changeData.getPropertyName());
								sensitiveDataChange.setOldValue(changeData.getOldValue());
								sensitiveDataChange.setNewValue(changeData.getNewValue());
								sensitiveDataChange.setChangeType(changeData.getChangeType());
								
								if (sensitiveDataChangeMap == null) {
									sensitiveDataChangeMap = new HashMap<String, List<SensitiveDataChange>>();
								}
								
								if (sensitiveDataChangeMap.get(key) == null) {
									sensitiveDataChangeMap.put(key, new ArrayList<SensitiveDataChange>());
								}
								
								sensitiveDataChangeMap.get(key).add(sensitiveDataChange);
								logger.debug(key+ " field '{}' of '"+auditData.getDomainClassName()+"' changed : '{}' -> '{}'", changeData.getPropertyName(), sensitiveDataChange.getOldValue(), sensitiveDataChange.getNewValue());
							}
						}
					}
				}
						}
		} else {
			logger.warn("Sensitivity Config is disabled or does not have any configuration.");
					}
		
		return sensitiveDataChangeMap;
				}
	

	/**
	 * @param attribute
	 * @param changeDataList
	 * @return
	 */
	private ChangeData getChangedDataByAttribute(String attribute, List<ChangeData> changeDataList) {
		ChangeData returnChangeData = null;
		if (attribute != null && changeDataList != null) {
			for (ChangeData changeData : changeDataList) {
				if (attribute.trim().equals(changeData.getPropertyName())) {
					returnChangeData = changeData;
					break;
				}
			}
		}
		return returnChangeData;
	}
}
