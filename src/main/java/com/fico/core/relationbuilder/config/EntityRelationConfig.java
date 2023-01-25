package com.fico.core.relationbuilder.config;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.fico.core.relationbuilder.EntityRelationBuilder;

/**
 * @author AnubhavDas
 * 
 * @implSpec
 * Base class that is expected to contain configuration for all the required {@link EntityRelationBuilder}
 * instances needed. This will be configured via some external configuration
 */
public class EntityRelationConfig implements ApplicationContextAware, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(EntityRelationConfig.class);
	
	private ApplicationContext applicationContext;
	
	//list of configured entity relation builders
	private List<EntityRelationBuilder> entityRelationBuilders;

	public List<EntityRelationBuilder> getEntityRelationBuilders() {
		return entityRelationBuilders;
	}

	public void setEntityRelationBuilders(List<EntityRelationBuilder> entityRelationBuilders) {
		this.entityRelationBuilders = entityRelationBuilders;
	}
	
	/**
	 * Validates all the configured {@link EntityRelationBuilder}(s)
	 * @throws Exception
	 */
	public void validateForUniqueBuilders() throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'validateForUniqueBuilders'. Validating all configured entity relation builders");
		
		if(this.entityRelationBuilders != null) {
			//checking validity of builders configured
			if(this.entityRelationBuilders.stream().anyMatch(entityRelationBuilder -> 
				entityRelationBuilder.getRootEntityName() == null || entityRelationBuilder.getRootEntityName().isEmpty() ||
				entityRelationBuilder.getPackageName() == null || entityRelationBuilder.getPackageName().isEmpty()
			))
				throw new Exception("Found some EntityRelationBuilder(s) that have no rootEntity/packageName specified");
			
			//checking for duplicate builders configured
			EntityRelationBuilder lastProcessedBuilder = null;
			for(EntityRelationBuilder builder : this.entityRelationBuilders) {
				if(lastProcessedBuilder != null && lastProcessedBuilder.getRootEntityName().equals(builder.getRootEntityName())
						&& lastProcessedBuilder.getPackageName().equals(builder.getPackageName()))
					throw new Exception("Cannot have two EntityRelationBuilder(s) with same rootEntity and packageName");
				
				lastProcessedBuilder = builder;
			}
		}
		else
			throw new Exception("List of configured EntityRelationBuilder(s) is null");
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'validateForUniqueBuilders'. Validation completed for all configured entity relation builders");
	}
	
	/**
	 * Returns the associated instance of the entity relation builder based on the supplied package & root entity name
	 * @param packageName
	 * The fully qualified package name containing the specified root entity
	 * @param rootEntityName
	 * The name of the root entity
	 * @return
	 * Matching instance of the {@link EntityRelationBuilder}
	 * @throws Exception
	 */
	public EntityRelationBuilder getEntityRelationBuilder(String packageName, String rootEntityName) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'getEntityRelationBuilder'");
		
		if(rootEntityName == null || rootEntityName.isEmpty())
			return null;
		else {
			final List<EntityRelationBuilder> matchedBuilder = this.entityRelationBuilders.stream().
					filter(builder -> 
						builder.getPackageName().equals(packageName) && builder.getRootEntityName().equals(rootEntityName)).
					collect(Collectors.toList());
			
			if(matchedBuilder != null && !matchedBuilder.isEmpty()) {
				if(logger.isInfoEnabled())
					logger.info("-----Inside method 'getEntityRelationBuilder'. Found relation builder with supplied root entity ["+ rootEntityName +"]");
				return matchedBuilder.get(0);
			}
			else
				return null;
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.applicationContext = arg0;
	}

	public ApplicationContext getApplicationContext() {
		return this.applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		//run validation of the loaded configuration
		validateForUniqueBuilders();
		
		//setting application context on all the builders
		for(EntityRelationBuilder builder : this.entityRelationBuilders)
			builder.setApplicationContext(this.applicationContext);
	}

}
