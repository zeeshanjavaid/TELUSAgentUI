package com.fico.ps.hermes.config;

import org.javers.core.Javers;
import org.modelmapper.ModelMapper;

/**
 * @author MushfikKhan
 *
 */
public class ConfigurationProvider {
	
	private ApplicationConfig applicationConfig;
	
	private ModelMapper modelMapper;
	
	private Javers javers;

	/**
	 * @return the applicationConfig
	 */
	public ApplicationConfig getApplicationConfig() {
		return applicationConfig;
	}

	/**
	 * @param applicationConfig the applicationConfig to set
	 */
	public void setApplicationConfig(ApplicationConfig applicationConfig) {
		this.applicationConfig = applicationConfig;
	}

	/**
	 * @return the modelMapper
	 */
	public ModelMapper getModelMapper() {
		return modelMapper;
	}

	/**
	 * @param modelMapper the modelMapper to set
	 */
	public void setModelMapper(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	/**
	 * @return the javers
	 */
	public Javers getJavers() {
		return javers;
	}

	/**
	 * @param javers the javers to set
	 */
	public void setJavers(Javers javers) {
		this.javers = javers;
	}
	
}
