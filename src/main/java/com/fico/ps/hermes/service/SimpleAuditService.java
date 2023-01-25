package com.fico.ps.hermes.service;

import com.fico.ps.hermes.audit.*;
import com.fico.ps.hermes.config.ApplicationConfig;
import com.fico.ps.hermes.config.ConfigurationProvider;
import com.fico.ps.hermes.config.ConfigurationProviderMap;
import com.fico.ps.hermes.core.*;
import com.fico.ps.hermes.save.DataModelSave;
import com.fico.ps.hermes.save.Error;
import com.fico.ps.hermes.save.ResponseSave;
import com.fico.ps.hermes.save.ResponseStatus;
import com.fico.ps.hermes.util.JacksonUtility;
import org.javers.core.Javers;
import org.javers.core.diff.Diff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class SimpleAuditService {

    private static Logger logger = LoggerFactory.getLogger(SimpleAuditService.class);
    
	private static final String DEFAULT_MODEL_CONFIG = "defaultModelConfig";

    @Autowired
    private AuditDataProcessor auditDataProcessor;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private JacksonUtility jacksonUtility;

    @Autowired
    private DataModelSave dataModelSave;

    @Autowired
    private PreProcessor preProcessor;

    @Autowired
    private PostProcessor postProcessor;

    @Autowired
    private ChangeProcessor changeProcessor;

    @Autowired
    private SensitivityCheck sensitivityCheck;
    
	//@Autowired
	private ConfigurationProviderMap configurationProviderMap;

    public <T> ResponseSave<T, Error> create(T modified, Class parentClass, Integer parentId) throws Exception{
        return this.save(null, modified, parentClass, parentId);
    }

    public <T> ResponseSave<T, Error> save(T original, T modified, Class parentClass, Integer parentId) throws Exception {

        ResponseSave<T, Error> responseSave = null;
        
        ConfigurationProvider configurationProvider = getConfigurationProvider(null) ;

        Error error = new Error(null, null);

        T processedModified = preProcessor.process(modified, configurationProvider.getApplicationConfig(), error);

        if (error.getDetails() == null || error.getDetails().size() == 0) {
        	Javers javersBuilder = configurationProvider.getJavers();
            Diff diff = javersBuilder.compare(original, processedModified);

            if(logger.isDebugEnabled()) {
                logger.debug("==================== Diff print start ====================");
                logger.debug(diff.prettyPrint());
                logger.debug("==================== Diff print end ====================");
            }

            Map<String, Map<Object, ChangeHolder>> changeMapByClassName = changeProcessor.getChangeHolderMapByClassName(diff, configurationProvider.getApplicationConfig());

            if (changeMapByClassName != null) {

                T postProcessedObj = postProcessor.process(processedModified, changeMapByClassName, configurationProvider.getApplicationConfig());
                responseSave = new ResponseSave<T, Error>(postProcessedObj, ResponseStatus.PASSED);
                List<AuditData> auditDataList = processAuditData(postProcessedObj, changeMapByClassName, configurationProvider.getApplicationConfig(), parentClass, parentId);
            } else {
                logger.info("--------------------------------------------------------------------");
                logger.info("No change detected in application. Database update is not required.");
                logger.info("--------------------------------------------------------------------");
                responseSave = new ResponseSave<T, Error>(modified, ResponseStatus.PASSED);
            }
        } else {
            logger.error("Validation failed.");
            error.getDetails().forEach(e -> logger.error(e.getCode() +" : "+ e.getDesc()));
            responseSave = new ResponseSave<T, Error>(modified, error, ResponseStatus.FAILED);
        }

        return responseSave;
    }

    public List<AuditData> processAuditData(Object savedRootObject,
                                            Map<String, Map<Object, ChangeHolder>> changeMapByClassName, ApplicationConfig applicationConfig,
                                            Class parentClass, Integer parentId) throws Exception {
        List<AuditData> auditDataList = new ArrayList<AuditData>();
        for (Map.Entry<String, Map<Object, ChangeHolder>> entryByClassName : changeMapByClassName
                .entrySet()) {
            if (applicationConfig.getDomainModelSaveConfigMap() != null && applicationConfig
                    .getDomainModelSaveConfigMap().get(entryByClassName.getKey()) != null) {
                ApplicationConfig.DomainModelSaveConfig domainModelSaveConfig = applicationConfig.getDomainModelSaveConfigMap().get(entryByClassName.getKey());
                for (Map.Entry<Object, ChangeHolder> entryById : entryByClassName.getValue()
                        .entrySet()) {
                    ChangeHolder changeHolder = entryById.getValue();
                    if (!changeHolder.isPersisted()) {
                        AuditData auditData = auditDataProcessor.process(changeMapByClassName, changeHolder, domainModelSaveConfig, applicationConfig);
                        auditData.setParentDomainClassName(parentClass.getName());
                        auditData.setParentEntityName(parentClass.getSimpleName());
                        auditData.setParentEntityId(parentId);
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
            saveAuditChangesAsynch(savedRootObject, auditDataList, applicationConfig, parentId);
        }
        return auditDataList;
    }

    private void saveAuditChangesAsynch(Object savedRootObject,
                                        List<AuditData> auditDataList, ApplicationConfig applicationConfig, Integer parentId) {

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
                                ApplicationConfig.DomainModelSaveConfig domainModelSaveConfig = applicationConfig
                                        .getDomainModelSaveConfigMap().get(auditData.getDomainClassName());
                                if (domainModelSaveConfig.getSaveConfig() != null
                                        && !domainModelSaveConfig.getSaveConfig().isExcludeFromAudit()) {
                                    List<ChangeData> changeDataList = auditData.getChangeDataList();
                                    String changeDataJson = jacksonUtility.serialize(changeDataList);
                                    auditData.setChangeData(changeDataJson);
                                    logger.debug("{} - {}", domainModelSaveConfig.getDomainModelClassName(), changeDataJson);

                                    logger.debug("Calling save method for saving '{}' entity's Audit Data : {}", auditData.getEntityName(), auditData);
                                    boolean saved = auditDataSave.save(auditData, parentId);
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
    
    private ConfigurationProvider getConfigurationProvider(Object[] params) {
		ConfigurationProvider configurationProvider = null;
		if (params != null && params.length > 1) {
			logger.info("Inside getConfigurationProvider(), model config name : {}", params[1]);
			if (params[1] != null && params[1] instanceof String) {
				if (configurationProviderMap.getConfigurationProviderMap() != null && configurationProviderMap.getConfigurationProviderMap().get((String) params[1]) != null) {
					configurationProvider = configurationProviderMap.getConfigurationProviderMap().get((String) params[1]);
				} else {
					logger.error("'{}' configuration is not present in application configuration.", (String) params[1]);
				}
			} else {
				logger.error("params[1] is not appropriate. It must be the type of string.");
			}
		} else {
			logger.info("Inside getConfigurationProvider(), default model config name will be used : {}", DEFAULT_MODEL_CONFIG);
			if (configurationProviderMap.getConfigurationProviderMap() != null && configurationProviderMap.getConfigurationProviderMap().get(DEFAULT_MODEL_CONFIG) != null) {
				configurationProvider = configurationProviderMap.getConfigurationProviderMap().get(DEFAULT_MODEL_CONFIG);
			} else {
				logger.error("'defaultModelConfig' configuration is not present in application configuration.");
			}
		}
		return configurationProvider;
	}
}
