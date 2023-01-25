package com.fico.ps.hermes.config;

import java.util.Map;

/**
 * @author MushfikKhan
 *
 */
public class CommonConfig {
	
	private Map<String, ApplicationConfig> modelConfigMap;
	
	private SensitivityConfig sensitivityConfig;
	
	private ValidatorConfig validatorConfig;

	/**
	 * @return the sensitivityConfig
	 */
	public SensitivityConfig getSensitivityConfig() {
		return sensitivityConfig;
	}

	/**
	 * @param sensitivityConfig the sensitivityConfig to set
	 */
	public void setSensitivityConfig(SensitivityConfig sensitivityConfig) {
		this.sensitivityConfig = sensitivityConfig;
	}

	/**
	 * @return the validatorConfig
	 */
	public ValidatorConfig getValidatorConfig() {
		return validatorConfig;
	}

	/**
	 * @param validatorConfig the validatorConfig to set
	 */
	public void setValidatorConfig(ValidatorConfig validatorConfig) {
		this.validatorConfig = validatorConfig;
	}

	/**
	 * @return the modelConfigMap
	 */
	public Map<String, ApplicationConfig> getModelConfigMap() {
		return modelConfigMap;
	}

	/**
	 * @param modelConfigMap the modelConfigMap to set
	 */
	public void setModelConfigMap(Map<String, ApplicationConfig> modelConfigMap) {
		this.modelConfigMap = modelConfigMap;
	}
}
