package com.fico.ps.hermes.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fico.ps.hermes.config.ApplicationConfig.DomainModelSaveConfig;
import com.fico.ps.hermes.config.ConfigurationProvider;
import com.fico.ps.hermes.core.ChangeHolder;

/**
 * @author MushfikKhan
 *
 */
@Component
public class Mapper implements IMapper{
	
	private static Logger logger = LoggerFactory.getLogger(Mapper.class);
	

	@Override
	public Object mapToEntity(ChangeHolder changeHolder, String domainModelClassName, ConfigurationProvider configurationProvider) throws Exception {
		logger.debug("Starting mapping for '{}' :  {}", domainModelClassName, changeHolder.getChangedDomainObject());
		Map<String, DomainModelSaveConfig> domainModelSaveConfigMap = configurationProvider.getApplicationConfig().getDomainModelSaveConfigMap();
		DomainModelSaveConfig domainModelSaveConfig = domainModelSaveConfigMap.get(domainModelClassName);
		String entityClassName = domainModelSaveConfig.getSaveConfig().getEnityClassName();
		
		Object entityObject = null;
		try {
			Class<?> clazz = Class.forName(entityClassName);
			Constructor<?> cons = clazz.getConstructor();
			entityObject = cons.newInstance();
			
			configurationProvider.getModelMapper().map(changeHolder.getChangedDomainObject(), entityObject);
			
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.error("Error occured while mapping of domain object '{}' to entity object '{}'", domainModelClassName, entityClassName);
			logger.error(e.getMessage(), e);
			throw new Exception(e);
		}
		
		return entityObject;
	}
}
