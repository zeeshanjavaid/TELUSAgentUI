package com.fico.ps.hermes.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fico.ps.hermes.config.SensitivityConfig.DomainModelClassConfigs.DomainModelClassConfig;

/**
 * @author MushfikKhan
 *
 */
public class SensitivityConfig {
	
	private boolean enabled;
	
	private Map<String, DomainModelClassConfigs> sensitivityConfigMap;
	
	public static class DomainModelClassConfigs {

		private List<DomainModelClassConfig> domainModelClassConfigs;
		
		@JsonIgnore
		private Map<String, DomainModelClassConfig> domainModelClassConfigsMap;

		public static class DomainModelClassConfig {
		
		private String domainModelFQClassName;

		private List<String> attributes;

		/**
		 * @return the attributes
		 */
		public List<String> getAttributes() {
			return attributes;
		}

		/**
		 * @param attributes the attributes to set
		 */
		public void setAttributes(List<String> attributes) {
			this.attributes = attributes;
		}

		/**
		 * @return the domainModelFQClassName
		 */
		public String getDomainModelFQClassName() {
			return domainModelFQClassName;
		}

		/**
		 * @param domainModelFQClassName the domainModelFQClassName to set
		 */
		public void setDomainModelFQClassName(String domainModelFQClassName) {
			this.domainModelFQClassName = domainModelFQClassName;
		}
	}

	/**
		 * @return the domainModelClassConfigs
	 */
		public List<DomainModelClassConfig> getDomainModelClassConfigs() {
			return domainModelClassConfigs;
	}

	/**
		 * @param domainModelClassConfigs the domainModelClassConfigs to set
	 */
		public void setDomainModelClassConfigs(List<DomainModelClassConfig> domainModelClassConfigs) {
			this.domainModelClassConfigs = domainModelClassConfigs;
	}
	
		/**
		 * @return the domainModelClassConfigsMap
		 */
		public Map<String, DomainModelClassConfig> getDomainModelClassConfigsMap() {
			return domainModelClassConfigsMap;
		}
	
		/**
		 * @return the domainModelClassConfigsMap
		 */
		public void setDomainModelClassConfigsMap(Map<String, DomainModelClassConfig> domainModelClassConfigsMap) {
			 this.domainModelClassConfigsMap = domainModelClassConfigsMap;;
		}
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the sensitivityConfigMap
	 */
	public Map<String, DomainModelClassConfigs> getSensitivityConfigMap() {
		return sensitivityConfigMap;
	}

	/**
	 * @param sensitivityConfigMap the sensitivityConfigMap to set
	 */
	public void setSensitivityConfigMap(Map<String, DomainModelClassConfigs> sensitivityConfigMap) {
		this.sensitivityConfigMap = sensitivityConfigMap;
	}

	@PostConstruct
	public void init() {
		if (this.sensitivityConfigMap != null) {
			for (Entry<String, DomainModelClassConfigs> entry : this.sensitivityConfigMap.entrySet()) {
				if (entry.getValue().getDomainModelClassConfigs() != null) {
					Map<String, DomainModelClassConfig> domainModelClassConfigMap  = new HashMap<String, DomainModelClassConfig>();
					entry.getValue().getDomainModelClassConfigs().forEach(domainModelClassConfig -> {
						domainModelClassConfigMap.put(domainModelClassConfig.getDomainModelFQClassName(), domainModelClassConfig);
					});
					entry.getValue().setDomainModelClassConfigsMap(domainModelClassConfigMap);
				}
			}
		}
	}
}
