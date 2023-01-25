package com.fico.ps.hermes.service;

import java.util.List;
import java.util.Map;

import org.javers.core.Javers;
import org.javers.core.diff.Diff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fico.ps.hermes.audit.AuditData;
import com.fico.ps.hermes.audit.AuditService;
import com.fico.ps.hermes.config.ConfigurationProvider;
import com.fico.ps.hermes.config.ConfigurationProviderMap;
import com.fico.ps.hermes.core.ChangeHolder;
import com.fico.ps.hermes.core.ChangeProcessor;
import com.fico.ps.hermes.core.PostProcessor;
import com.fico.ps.hermes.core.PreProcessor;
import com.fico.ps.hermes.core.SensitivityCheck;
import com.fico.ps.hermes.save.DataModelSave;
import com.fico.ps.hermes.save.Error;
import com.fico.ps.hermes.save.ResponseSave;
import com.fico.ps.hermes.save.ResponseStatus;

/**
 * @author MushfikKhan
 *
 */
// @Service
public class DomainModelSave implements IDomainModelSave {
	
	private static final String DEFAULT_MODEL_CONFIG = "defaultModelConfig";

	private static Logger logger = LoggerFactory.getLogger(DomainModelSave.class);
//	private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(DataModelSave.class.getName());
	
	
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
	
	@Autowired
	private ConfigurationProviderMap configurationProviderMap;
	
	@Autowired
	private AuditService auditService;
	
	/**
	 * @param <T>
	 * @param origal
	 * @param modified
	 * @param params : (boolean) params[0] -> is sensitivity check required? Default value is 'true'
	 * @param params : (String) params[1] -> model config name. Default value is 'defaultModelConfig'
	 * @return
	 * @throws Exception
	 */
	@Override
	public <T> ResponseSave<T, Error> save(T original, T modified, Object... params) throws Exception {
		
		ResponseSave<T, Error> responseSave = null;
		
		Error error = new Error(null, null);
		
		ConfigurationProvider configurationProvider = getConfigurationProvider(params) ;
		  
		T processedModified = preProcessor.process(modified, configurationProvider.getApplicationConfig(), error);
		
		if (error.getDetails() == null || error.getDetails().size() == 0) {
			Javers javersBuilder = configurationProvider.getJavers();
			Diff diff = javersBuilder.compare(original, processedModified);
			
			//logger.info("==================== Diff print start ====================");
			//logger.info(diff.prettyPrint());
			//logger.info("==================== Diff print end ====================");
			
			Map<String, Map<Object, ChangeHolder>> changeMapByClassName = changeProcessor.getChangeHolderMapByClassName(diff, configurationProvider.getApplicationConfig());
			
			if (changeMapByClassName != null) {
				
				dataModelSave.saveDataModel(processedModified, changeMapByClassName, configurationProvider);
				
				T postProcessedObj = postProcessor.process(processedModified, changeMapByClassName, configurationProvider.getApplicationConfig());
				responseSave = new ResponseSave<T, Error>(postProcessedObj, ResponseStatus.PASSED);
				List<AuditData> auditDataList = auditService.processAuditData(postProcessedObj, changeMapByClassName, configurationProvider.getApplicationConfig());
				if (params.length > 0 && (boolean)params[0]) { //Check and set sensitive data changes in response.
					responseSave.setSensitiveDataChangeMap(sensitivityCheck.getSensitiveChanges(auditDataList, configurationProvider.getApplicationConfig()));
				}
			} else {
				//logger.info("--------------------------------------------------------------------");
				//logger.info("No change detected in application. Database update is not required.");
				//logger.info("--------------------------------------------------------------------");
				responseSave = new ResponseSave<T, Error>(modified, ResponseStatus.PASSED);
			}
		} else {
			//logger.info("Validation failed.");
			error.getDetails().forEach(e -> logger.error(e.getCode() +" : "+ e.getDesc()));
			responseSave = new ResponseSave<T, Error>(modified, error, ResponseStatus.FAILED);
		}
		
		return responseSave;
	}

	private ConfigurationProvider getConfigurationProvider(Object[] params) {
		ConfigurationProvider configurationProvider = null;
		if (params != null && params.length > 1) {
			//logger.info("Inside getConfigurationProvider(), model config name : {}", params[1]);
			if (params[1] != null && params[1] instanceof String) {
				if (configurationProviderMap.getConfigurationProviderMap() != null && configurationProviderMap.getConfigurationProviderMap().get((String) params[1]) != null) {
					configurationProvider = configurationProviderMap.getConfigurationProviderMap().get((String) params[1]);
				} else {
					logger.warn("'{}' configuration is not present in application configuration.", (String) params[1]);
				}
			} else {
				logger.warn("params[1] is not appropriate. It must be the type of string.");
			}
		} else {
			//logger.info("Inside getConfigurationProvider(), default model config name will be used : {}", DEFAULT_MODEL_CONFIG);
			if (configurationProviderMap.getConfigurationProviderMap() != null && configurationProviderMap.getConfigurationProviderMap().get(DEFAULT_MODEL_CONFIG) != null) {
				configurationProvider = configurationProviderMap.getConfigurationProviderMap().get(DEFAULT_MODEL_CONFIG);
			} else {
				logger.warn("'defaultModelConfig' configuration is not present in application configuration.");
			}
		}
		return configurationProvider;
	}
}
