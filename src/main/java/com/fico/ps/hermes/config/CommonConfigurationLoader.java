package com.fico.ps.hermes.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;


/**
 * @author MushfikKhan
 *
 */
@Configuration
public class CommonConfigurationLoader {
	
//	/**
//	 * @return
//	 */
//	@Bean("applicationConfig")
//	public ApplicationConfig getApplicationConfig() {
//		ApplicationConfig applicationConfig = new ApplicationConfig();
//		
//		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//		
//		Yaml yaml = new Yaml();
//		
//		try {
//			File file = new File(classLoader.getResource("hermes/application.yml").getFile());
//			CommonConfig commonConfig = yaml.loadAs(new FileInputStream(file), CommonConfig.class);
//			Map<String, ApplicationConfig> modelConfigMap = commonConfig.getModelConfigMap();
//			applicationConfig = modelConfigMap.get("defaultModelConfig");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return applicationConfig;
//	}
	
	@Bean("configurationProviderMap")
	public ConfigurationProviderMap getConfigurationProviderMap() throws ClassNotFoundException {
		ConfigurationProviderMap domainModelConfigMap = new ConfigurationProviderMap();
		
		Map<String, ConfigurationProvider> mapOfconfigurationProvider = new HashMap<String, ConfigurationProvider>();
		
		ModelMapperConfig modelMapperConfig = new ModelMapperConfig();
		JaversBuilderConfig javersBuilderConfig = new JaversBuilderConfig();
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		
		Yaml yaml = new Yaml();
		
		try {
			File file = new File(classLoader.getResource("hermes/application.yml").getFile());
			CommonConfig commonConfig = yaml.loadAs(new FileInputStream(file), CommonConfig.class);
			Map<String, ApplicationConfig> modelConfigMap = commonConfig.getModelConfigMap();
			if (modelConfigMap != null) {
				for (Entry<String, ApplicationConfig> entry : modelConfigMap.entrySet()) {
					ConfigurationProvider configurationProvider = new ConfigurationProvider();
					
					ApplicationConfig applicationConfig = entry.getValue();
					applicationConfig.init();
					configurationProvider.setApplicationConfig(applicationConfig);
					
					configurationProvider.setModelMapper(modelMapperConfig.getModelMapper(applicationConfig));
					configurationProvider.setJavers(javersBuilderConfig.getJavers(applicationConfig));
					
					mapOfconfigurationProvider.put(entry.getKey(), configurationProvider);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		domainModelConfigMap.setConfigurationProviderMap(mapOfconfigurationProvider);
		return domainModelConfigMap;
	}
	
	/**
	 * @return
	 */
	@Bean("sensitivityConfig")
	public SensitivityConfig getSensitivityConfig() {
		SensitivityConfig sensitivityConfig = new SensitivityConfig();
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		
		Yaml yaml = new Yaml();
		
		try {
			File file = new File(classLoader.getResource("hermes/sensitivityConfig.yml").getFile());
			CommonConfig commonConfig = yaml.loadAs(new FileInputStream(file), CommonConfig.class);
			sensitivityConfig = commonConfig.getSensitivityConfig();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sensitivityConfig;
	}
	
	/**
	 * @return
	 */
	@Bean("validatorConfig")
	public ValidatorConfig getValidatorConfig() {
		ValidatorConfig validatorConfig = new ValidatorConfig();
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		
		Yaml yaml = new Yaml();
		
		try {
			File file = new File(classLoader.getResource("hermes/validatorConfig.yml").getFile());
			CommonConfig commonConfig = yaml.loadAs(new FileInputStream(file), CommonConfig.class);
			validatorConfig = commonConfig.getValidatorConfig();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return validatorConfig;
	}
}
