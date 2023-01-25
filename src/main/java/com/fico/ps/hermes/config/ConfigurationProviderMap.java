package com.fico.ps.hermes.config;

import java.util.Map;

/**
 * @author MushfikKhan
 *
 */
public class ConfigurationProviderMap {
	
	private Map<String, ConfigurationProvider> configurationProviderMap;

	/**
	 * @return the configurationProviderMap
	 */
	public Map<String, ConfigurationProvider> getConfigurationProviderMap() {
		return configurationProviderMap;
	}

	/**
	 * @param configurationProviderMap the configurationProviderMap to set
	 */
	public void setConfigurationProviderMap(Map<String, ConfigurationProvider> configurationProviderMap) {
		this.configurationProviderMap = configurationProviderMap;
	}
	
}
