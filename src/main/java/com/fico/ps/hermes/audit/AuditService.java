package com.fico.ps.hermes.audit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.fico.ps.hermes.config.ApplicationConfig;
import com.fico.ps.hermes.config.ApplicationConfig.DomainModelSaveConfig;
import com.fico.ps.hermes.core.ChangeHolder;
import com.fico.ps.hermes.util.JacksonUtility;

/**
 * @author MushfikKhan
 *
 */
@Service
public class AuditService {

	private static Logger logger = LoggerFactory.getLogger(AuditService.class);
//	private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(DataModelSave.class.getName());

	@Autowired
	private AuditDataProcessor auditDataProcessor;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private JacksonUtility jacksonUtility;
	
	/**
	 * @param savedRootObject
	 * @param changeMapByClassName
	 * @param applicationConfig
	 * @return
	 * @throws Exception
	 */
	public List<AuditData> processAuditData(Object savedRootObject,
			Map<String, Map<Object, ChangeHolder>> changeMapByClassName, ApplicationConfig applicationConfig) throws Exception {
		List<AuditData> auditDataList = new ArrayList<AuditData>();
		for (Entry<String, Map<Object, ChangeHolder>> entryByClassName : changeMapByClassName
				.entrySet()) {
			if (applicationConfig.getDomainModelSaveConfigMap() != null && applicationConfig
					.getDomainModelSaveConfigMap().get(entryByClassName.getKey()) != null) {
				DomainModelSaveConfig domainModelSaveConfig = applicationConfig.getDomainModelSaveConfigMap().get(entryByClassName.getKey());
				for (Entry<Object, ChangeHolder> entryById : entryByClassName.getValue()
						.entrySet()) {
					ChangeHolder changeHolder = entryById.getValue();
					if (changeHolder.isPersisted()) {
						AuditData auditData = auditDataProcessor.process(changeMapByClassName, changeHolder, domainModelSaveConfig, applicationConfig);
						if (auditData != null) {
							if (auditDataList == null) {
								auditDataList = new ArrayList<AuditData>();
								auditDataList.add(auditData);
							} else {
								auditDataList.add(auditData);
							}
						}
					}
				}
			}
		} 
		if (auditDataList != null) {
			saveAuditChangesAsynch(savedRootObject, auditDataList, applicationConfig);
		}
		return auditDataList;
	}

	/**
	 * @param savedRootObject
	 * @param changeMapByClassName
	 * @param applicationConfig
	 */
	private void saveAuditChangesAsynch(Object savedRootObject,
			List<AuditData> auditDataList, ApplicationConfig applicationConfig) {

		if (applicationConfig.isDataAuditEnabled()) {
			CompletableFuture<Void> runAsyncFuture = CompletableFuture.runAsync(new Runnable() {
				public void run() {
					IAuditDataSave auditDataSave = null;
					try{
						auditDataSave = (IAuditDataSave) applicationContext.getBean(applicationConfig.getDataAuditSaveBean());
					} catch (NoSuchBeanDefinitionException e) {
						logger.warn("Data audit save bean '{}' specified in config file could not be found in application context.", applicationConfig.getDataAuditSaveBean());
						logger.warn("Data audit save will be skipped.");
						logger.warn(e.getMessage(), e);
					} catch (ClassCastException e) {
						logger.warn("Data audit save bean '{}' specified in config file must implement 'com.fico.ps.hermes.audit.IAuditDataSave'.", applicationConfig.getDataAuditSaveBean());
						logger.warn("Data audit save will be skipped.");
						logger.warn(e.getMessage(), e);
					}
					try {
						if (auditDataSave != null) {
							for (AuditData auditData : auditDataList) {
									DomainModelSaveConfig domainModelSaveConfig = applicationConfig
										.getDomainModelSaveConfigMap().get(auditData.getDomainClassName());
									if (domainModelSaveConfig.getSaveConfig() != null
											&& !domainModelSaveConfig.getSaveConfig().isExcludeFromAudit()) {
									List<ChangeData> changeDataList = auditData.getChangeDataList();
									String changeDataJson = jacksonUtility.serialize(changeDataList);
									auditData.setChangeData(changeDataJson);
									logger.debug("{} - {}", domainModelSaveConfig.getDomainModelClassName(), changeDataJson);

									logger.debug("Calling save method for saving '{}' entity's Audit Data : {}", auditData.getEntityName(), auditData);
									boolean saved = auditDataSave.save(savedRootObject, auditData);
									logger.debug("Audit Data saved successfully '{}'", saved);
									} else {
										logger.debug("Entity '{}' is excluded from Data Audit.",
												domainModelSaveConfig.getSaveConfig().getEnityClassName());
									}
								}
						}
					} catch (Exception e) {
						logger.error("Error occured while saving audit data change.");
						logger.error(e.getMessage(), e);
					}
				}
			});
			if (runAsyncFuture.isDone()) {
				logger.info("Asynch save of audit thread has been completed");
			}
		} else {
			logger.warn("Data Audit is disabled.");

		}
	}
}
