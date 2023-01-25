package com.fico.ps.hermes.save;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fico.ps.hermes.config.ApplicationConfig;
import com.fico.ps.hermes.config.ApplicationConfig.DomainModelSaveConfig;
import com.fico.ps.hermes.config.ConfigurationProvider;
import com.fico.ps.hermes.core.Action;
import com.fico.ps.hermes.core.ChangeHolder;
import com.fico.ps.hermes.mapper.IMapper;
import com.fico.ps.hermes.mapper.IReferenceMapper;
import com.fico.ps.hermes.util.ReflectionUtility;
import com.fico.ps.hermes.util.Utility;

/**
 * @author MushfikKhan
 *
 */
@Component
public class DataModelSave {
	
	private static Logger logger = LoggerFactory.getLogger(DataModelSave.class);
//	private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(DataModelSave.class.getName());
	
	
	@Autowired
	private IMapper mapper;
	
	@Autowired
	private IReferenceMapper referenceMapper;
	
	@Autowired
	private DatabaseEntityCrudAction databaseEntityCrudAction;
	
	/**
	 * @param domainRootObject
	 * @param changeMapByClassName
	 * @param configurationProvider
	 * @return
	 * @throws Exception 
	 */
	public Map<String, Map<Object, ChangeHolder>>  saveDataModel(Object domainRootObject, Map<String, Map<Object, ChangeHolder>> changeMapByClassName, ConfigurationProvider configurationProvider) throws Exception {
		ApplicationConfig applicationConfig = configurationProvider.getApplicationConfig();
		
		List<DomainModelSaveConfig> domainSaveConfigSortedList = applicationConfig.getSortedDomainModelSaveConfig();
		
		if (changeMapByClassName != null) {
			// Iterate in defined sequence for CREATE and UPDATE entity records.
			for (DomainModelSaveConfig domainModelSaveConfig : domainSaveConfigSortedList) {
				String domainModelClassName = domainModelSaveConfig.getDomainModelClassName();
				Map<Object, ChangeHolder> changeGroupMap = changeMapByClassName.get(domainModelClassName);
				if (changeGroupMap != null) {
					for (Map.Entry<Object, ChangeHolder> entry : changeGroupMap.entrySet()) {
						ChangeHolder changeHolder = entry.getValue();
						Object mappedEntity = null;
						switch (changeHolder.getAction()) {
							case CREATE:
								mappedEntity = mapper.mapToEntity(changeHolder, domainModelClassName, configurationProvider);
								mappedEntity = referenceMapper.mapReferenceToEntity(domainRootObject, mappedEntity, changeHolder, changeMapByClassName, domainModelClassName, applicationConfig);
								Object savedEntity = databaseEntityCrudAction.crud(mappedEntity, domainModelClassName, Action.CREATE, applicationConfig);
								changeMapByClassName.get(domainModelSaveConfig.getDomainModelClassName()).get(entry.getKey()).setChangedEntityObject(savedEntity);
								String idAttributeName = Utility.getIdAttributeNameByDomainClass(domainModelClassName, applicationConfig);
								changeMapByClassName.get(domainModelSaveConfig.getDomainModelClassName()).get(entry.getKey()).setEntityId(ReflectionUtility.getPropertyValue(savedEntity, idAttributeName));
								changeMapByClassName.get(domainModelSaveConfig.getDomainModelClassName()).get(entry.getKey()).setPersisted(true);
								break;
							case DELETE:
								break;
							case UPDATE:
								mappedEntity = mapper.mapToEntity(changeHolder, domainModelClassName, configurationProvider);
								mappedEntity = referenceMapper.mapReferenceToEntity(domainRootObject, mappedEntity, changeHolder, changeMapByClassName, domainModelClassName, applicationConfig);
								changeMapByClassName.get(domainModelSaveConfig.getDomainModelClassName()).get(entry.getKey()).setChangedEntityObject(mappedEntity);
								databaseEntityCrudAction.crud(mappedEntity, domainModelClassName, Action.UPDATE, applicationConfig);
								changeMapByClassName.get(domainModelSaveConfig.getDomainModelClassName()).get(entry.getKey()).setPersisted(true);
								break;
							default: 
								logger.info("No Action found for class {}", domainModelClassName);
						}
//						logger.info(entry.getKey() + "/" + entry.getValue());
					}
				}
			}
			
			ListIterator<DomainModelSaveConfig> listIterator = domainSaveConfigSortedList.listIterator(domainSaveConfigSortedList.size());

			// Iterate in reverse order to DELETE entity records.
			while(listIterator.hasPrevious()) {
				DomainModelSaveConfig domainModelSaveConfig = listIterator.previous();
				String domainModelClassName = domainModelSaveConfig.getDomainModelClassName();
				Map<Object, ChangeHolder> changeGroupMap = changeMapByClassName.get(domainModelClassName);
				if (changeGroupMap != null) {
					for (Map.Entry<Object, ChangeHolder> entry : changeGroupMap.entrySet()) {
						ChangeHolder changeHolder = entry.getValue();
						Object mappedEntity = null;
						switch (changeHolder.getAction()) {
							case CREATE:
								break;
							case DELETE:
								mappedEntity = mapper.mapToEntity(changeHolder, domainModelClassName, configurationProvider);
								databaseEntityCrudAction.crud(mappedEntity, domainModelClassName, Action.DELETE, applicationConfig);
								changeMapByClassName.get(domainModelSaveConfig.getDomainModelClassName()).get(entry.getKey()).setPersisted(true);
								break;
							case UPDATE:
								break;
							default:
								//Do nothing
						}
					}
				}
			}
		} else {
			logger.info("No entity is eligible to save.");
		}
		
		return changeMapByClassName;
	}
}
