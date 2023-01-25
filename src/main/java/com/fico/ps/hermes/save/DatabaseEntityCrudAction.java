package com.fico.ps.hermes.save;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.fico.ps.hermes.config.ApplicationConfig;
import com.fico.ps.hermes.core.Action;

/**
 * @author MushfikKhan
 *
 */
@Service
public class DatabaseEntityCrudAction {
	
	private static Logger logger = LoggerFactory.getLogger(DatabaseEntityCrudAction.class);
//	private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(DataModelSave.class.getName());
	
	@Autowired
	private ApplicationContext appContext;
	
	
	public Object crud(Object entityObject, String domainClassName, Action action, ApplicationConfig applicationConfig) throws Exception {
		
		String entityClassName = applicationConfig.getDomainModelSaveConfigMap().get(domainClassName).getSaveConfig().getEnityClassName();
		//logger.info("'{}' entity "+entityClassName, action);
		//logger.info("{}", entityObject);
		Object entityService = appContext.getBean(getDatabaseEntityServiceName(entityClassName, applicationConfig));
		try {
			if (action.equals(Action.CREATE)) {
				Method createMethod = entityService.getClass().getDeclaredMethod("create", Class.forName(entityClassName));
				entityObject = createMethod.invoke(entityService, entityObject);
			} else if (action.equals(Action.UPDATE)) {
				Method updateMethod = entityService.getClass().getDeclaredMethod("update", Class.forName(entityClassName));
				entityObject = updateMethod.invoke(entityService, entityObject);
			} else if (action.equals(Action.DELETE)) {
				Method deleteMethod = entityService.getClass().getDeclaredMethod("delete", Class.forName(entityClassName));
				deleteMethod.invoke(entityService, entityObject);
			}
//			logger.info("{} entity {} \n{}", action, entityClassName, entityObject);
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.error("Error occured during '{}' entity "+entityClassName+": {}", action, entityObject);
			logger.error(e.getMessage(), e);
			throw new Exception(e);
		} 
		return entityObject;
	}
	
	/**
	 * @param entityFQClassName
	 * @param applicationConfig 
	 * @return
	 */
	private String getDatabaseEntityServiceName(String entityFQClassName, ApplicationConfig applicationConfig) {
		StringBuilder sb = new StringBuilder(applicationConfig.getDatabaseServicePrefix());
		
		String[] strArray = entityFQClassName.split("\\.");
		
		sb.append(strArray[strArray.length-1])
			.append(applicationConfig.getDatabaseServiceSuffix());
		
		return sb.toString();
	}
}
