package com.fico.core.relationbuilder.config;

import java.io.File;
import java.io.FileInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;

/**
 * @author AnubhavDas
 *
 * @implSpec
 * Configuration class responsible for loading of the base {@link EntityRelationConfig} from the
 * external configuration file
 * 
 * @see
 * EntityRelationConfig
 */
@Configuration
public class EntityRelationConfigLoader {

	private static final Logger logger = LoggerFactory.getLogger(EntityRelationConfigLoader.class);
	
	/**
	 * Creates a {@link Bean} for the loaded base {@link EntityRelationConfig}
	 */
	@Bean(value = "entityRelationCfg")
	public EntityRelationConfig getEntityRelationConfig() {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'getEntityRelationConfig'. Attempting to create a bean of 'EntityRelationConfig'");
		
		EntityRelationConfig relationConfig = null;
		
		Yaml yaml = new Yaml();
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		File relationConfigFile = new File(contextClassLoader.getResource("entityDeletion/deletionConfig.yml").getFile());
		try {
			relationConfig = yaml.loadAs(new FileInputStream(relationConfigFile), EntityRelationConfig.class);
			
			if(relationConfig == null)
				throw new Exception("Loaded relation configuration is null");
		
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Failed to load entity relation base configuration", e);
			throw new RuntimeException(e);
		}
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'getEntityRelationConfig'. Bean of 'EntityRelationConfig' registered successfully");
		return relationConfig;
	}
	
}
