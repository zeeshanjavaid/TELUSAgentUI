package com.fico.ps.hermes.config;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author MushfikKhan
 *
 */
public class ApplicationConfig {
	
	private String defaultIdAttribName;
	
	private String databaseServicePrefix;
	
	private String databaseServiceSuffix;
	
	private boolean dataAuditEnabled;
	
	private String dataAuditSaveBean;
	
	private ModelMapperConfig modelMapperConfig;

	private List<DomainModelSaveConfig> domainModelSaveConfig;
	
	public static class ModelMapperConfig {
		private String matchingStrategy;

		public String getMatchingStrategy() {
			return matchingStrategy;
		}

		public void setMatchingStrategy(String matchingStrategy) {
			this.matchingStrategy = matchingStrategy;
		}
	}

	public static class DomainModelSaveConfig {

		private String domainModelClassName;

		private String idAttribName;

		private int sequence;

		private SaveConfig saveConfig;

		private DirectMapping directMapping;

		private List<EntityReferenceMapping> entityReferenceMappings;

		public static class SaveConfig {

			private String enityClassName;
			
			private boolean excludeFromAudit;

			/**
			 * @return the enityClassName
			 */
			public String getEnityClassName() {
				return enityClassName;
			}

			/**
			 * @param enityClassName the enityClassName to set
			 */
			public void setEnityClassName(String enityClassName) {
				this.enityClassName = enityClassName;
			}

			public boolean isExcludeFromAudit() {
				return excludeFromAudit;
			}

			public void setExcludeFromAudit(boolean excludeFromAudit) {
				this.excludeFromAudit = excludeFromAudit;
			}

		}

		public static class DirectMapping {

			private List<FieldMapping> fieldMappings;

			public static class FieldMapping {
				private String sourceField;
				private String destField;

				/**
				 * @return the sourceField
				 */
				public String getSourceField() {
					return sourceField;
				}

				/**
				 * @param sourceField the sourceField to set
				 */
				public void setSourceField(String sourceField) {
					this.sourceField = sourceField;
				}

				/**
				 * @return the destField
				 */
				public String getDestField() {
					return destField;
				}

				/**
				 * @param destfield the destField to set
				 */
				public void setDestField(String destField) {
					this.destField = destField;
				}
			}

			/**
			 * @return the fieldMappings
			 */
			public List<FieldMapping> getFieldMappings() {
				return fieldMappings;
			}

			/**
			 * @param fieldMappings the fieldMappings to set
			 */
			public void setFieldMappings(List<FieldMapping> fieldMappings) {
				this.fieldMappings = fieldMappings;
			}

		}

		public static class EntityReferenceMapping {
			private String entityFieldName;
			private String referencePathInDomainModel;
			private String parentDomainClass;

			/**
			 * @return the entityFieldName
			 */
			public String getEntityFieldName() {
				return entityFieldName;
			}

			/**
			 * @param entityFieldName the entityFieldName to set
			 */
			public void setEntityFieldName(String entityFieldName) {
				this.entityFieldName = entityFieldName;
			}

			public String getReferencePathInDomainModel() {
				return referencePathInDomainModel;
			}

			public void setReferencePathInDomainModel(String referencePathInDomainModel) {
				this.referencePathInDomainModel = referencePathInDomainModel;
			}

			public String getParentDomainClass() {
				return parentDomainClass;
			}

			public void setParentDomainClass(String parentDomainClass) {
				this.parentDomainClass = parentDomainClass;
			}
		}

		/**
		 * @return the domainModelClassName
		 */
		public String getDomainModelClassName() {
			return domainModelClassName;
		}

		/**
		 * @param domainModelClassName the domainModelClassName to set
		 */
		public void setDomainModelClassName(String domainModelClassName) {
			this.domainModelClassName = domainModelClassName;
		}

		/**
		 * @return the sequence
		 */
		public int getSequence() {
			return sequence;
		}

		/**
		 * @param sequence the sequence to set
		 */
		public void setSequence(int sequence) {
			this.sequence = sequence;
		}

		/**
		 * @return the saveConfig
		 */
		public SaveConfig getSaveConfig() {
			return saveConfig;
		}

		/**
		 * @param saveConfig the saveConfig to set
		 */
		public void setSaveConfig(SaveConfig saveConfig) {
			this.saveConfig = saveConfig;
		}

		/**
		 * @return the idAttribName
		 */
		public String getIdAttribName() {
			return idAttribName;
		}

		/**
		 * @param idAttribName the idAttribName to set
		 */
		public void setIdAttribName(String idAttribName) {
			this.idAttribName = idAttribName;
		}

		public DirectMapping getDirectMapping() {
			return directMapping;
		}

		public void setDirectMapping(DirectMapping directMapping) {
			this.directMapping = directMapping;
		}

		public List<EntityReferenceMapping> getEntityReferenceMappings() {
			return entityReferenceMappings;
		}

		public void setEntityReferenceMappings(List<EntityReferenceMapping> entityReferenceMappings) {
			this.entityReferenceMappings = entityReferenceMappings;
		}
	}

	/**
	 * @return the domainModelSaveConfig
	 */
	public List<DomainModelSaveConfig> getDomainModelSaveConfig() {
		return domainModelSaveConfig;
	}

	/**
	 * @param domainModelSaveConfig the domainModelSaveConfig to set
	 */
	public void setDomainModelSaveConfig(List<DomainModelSaveConfig> domainModelSaveConfig) {
		this.domainModelSaveConfig = domainModelSaveConfig;
	}

	/**
	 * @return the defaultIdAttribName
	 */
	public String getDefaultIdAttribName() {
		return defaultIdAttribName;
	}

	/**
	 * @param defaultIdAttribName the defaultIdAttribName to set
	 */
	public void setDefaultIdAttribName(String defaultIdAttribName) {
		this.defaultIdAttribName = defaultIdAttribName;
	}
	
	@JsonIgnore
	private List<DomainModelSaveConfig> sortedDomainModelSaveConfig;
	
	@JsonIgnore
	private Map<String, DomainModelSaveConfig> domainModelSaveConfigMap;
	
	@PostConstruct
	public void init() {
		this.domainModelSaveConfigMap = new HashMap<String, DomainModelSaveConfig>();
		if(this.domainModelSaveConfig != null) {
			this.sortedDomainModelSaveConfig =
					domainModelSaveConfig.stream()
	                  .sorted(Comparator.comparing(DomainModelSaveConfig::getSequence))
	                  .collect(Collectors.toList());
			
			domainModelSaveConfig.forEach(domainModelSaveConfig -> {
				this.domainModelSaveConfigMap.put(domainModelSaveConfig.getDomainModelClassName(), domainModelSaveConfig);
			});
		}
	}
	
	/**
	 * @return the sortedDomainModelSaveConfig
	 */
	public List<DomainModelSaveConfig> getSortedDomainModelSaveConfig() {
		return this.sortedDomainModelSaveConfig;
	}

	/**
	 * @return the domainModelSaveConfigMap
	 */
	public Map<String, DomainModelSaveConfig> getDomainModelSaveConfigMap() {
		return this.domainModelSaveConfigMap;
	}

	/**
	 * @param sortedDomainModelSaveConfig the sortedDomainModelSaveConfig to set
	 */
	public void setSortedDomainModelSaveConfig(List<DomainModelSaveConfig> sortedDomainModelSaveConfig) {
		this.sortedDomainModelSaveConfig = sortedDomainModelSaveConfig;
	}

	/**
	 * @param domainModelSaveConfigMap the domainModelSaveConfigMap to set
	 */
	public void setDomainModelSaveConfigMap(Map<String, DomainModelSaveConfig> domainModelSaveConfigMap) {
		this.domainModelSaveConfigMap = domainModelSaveConfigMap;
	}

	/**
	 * @return the databaseServicePrefix
	 */
	public String getDatabaseServicePrefix() {
		return databaseServicePrefix;
	}

	/**
	 * @param databaseServicePrefix the databaseServicePrefix to set
	 */
	public void setDatabaseServicePrefix(String databaseServicePrefix) {
		this.databaseServicePrefix = databaseServicePrefix;
	}

	/**
	 * @return the databaseServiceSuffix
	 */	
	public String getDatabaseServiceSuffix() {
		return databaseServiceSuffix;
	}

	/**
	 * @param databaseServiceSuffix the databaseServiceSuffix to set
	 */
	public void setDatabaseServiceSuffix(String databaseServiceSuffix) {
		this.databaseServiceSuffix = databaseServiceSuffix;
	}

	public ModelMapperConfig getModelMapperConfig() {
		return modelMapperConfig;
	}

	public void setModelMapperConfig(ModelMapperConfig modelMapperConfig) {
		this.modelMapperConfig = modelMapperConfig;
	}

	/**
	 * @return the dataAuditEnabled
	 */
	public boolean isDataAuditEnabled() {
		return dataAuditEnabled;
	}

	/**
	 * @param dataAuditEnabled the dataAuditEnabled to set
	 */
	public void setDataAuditEnabled(boolean dataAuditEnabled) {
		this.dataAuditEnabled = dataAuditEnabled;
	}

	/**
	 * @return the dataAuditSaveBean
	 */
	public String getDataAuditSaveBean() {
		return dataAuditSaveBean;
	}

	/**
	 * @param dataAuditSaveBean the dataAuditSaveBean to set
	 */
	public void setDataAuditSaveBean(String dataAuditSaveBean) {
		this.dataAuditSaveBean = dataAuditSaveBean;
	}
	
}
