package com.fico.ps.hermes.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author MushfikKhan
 *
 */
public class ValidatorConfig {
	
	private boolean enabled;
	
	private boolean customValidationEnabled;
	
	private String customValidationBean;
	
	private String mandatoryErrorCodeFormat;
	
	private String regularExpErrorCodeFormat;
	
	private String maxLengthErrorCodeFormat;
	
	private String mandatoryErrorMessageFormat;
	
	private String regularExpErrorMessageFormat;
	
	private String maxLengthErrorMessageFormat;
	
	private List<EntityValidatorConfig> entityValidatorConfigs;

	public static class EntityValidatorConfig {
		
		private String domainModelFQClassName;

		private List<FieldValidatorConfig> fieldValidatorConfigs;

		public static class FieldValidatorConfig {
			private String name;
			private boolean mandatoryCheck;
			private String regularExpression;
			private int maxLength;
			private String errorCode;
			private String errorMessage;
			/**
			 * @return the name
			 */
			public String getName() {
				return name;
			}
			/**
			 * @param name the name to set
			 */
			public void setName(String name) {
				this.name = name;
			}
			/**
			 * @return the mandatoryCheck
			 */
			public boolean isMandatoryCheck() {
				return mandatoryCheck;
			}
			/**
			 * @param mandatoryCheck the mandatoryCheck to set
			 */
			public void setMandatoryCheck(boolean mandatoryCheck) {
				this.mandatoryCheck = mandatoryCheck;
			}
			/**
			 * @return the regularExpression
			 */
			public String getRegularExpression() {
				return regularExpression;
			}
			/**
			 * @param regularExpression the regularExpression to set
			 */
			public void setRegularExpression(String regularExpression) {
				this.regularExpression = regularExpression;
			}
			/**
			 * @return the maxLength
			 */
			public int getMaxLength() {
				return maxLength;
			}
			/**
			 * @param maxLength the maxLength to set
			 */
			public void setMaxLength(int maxLength) {
				this.maxLength = maxLength;
			}
			/**
			 * @return the errorCode
			 */
			public String getErrorCode() {
				return errorCode;
			}
			/**
			 * @param errorCode the errorCode to set
			 */
			public void setErrorCode(String errorCode) {
				this.errorCode = errorCode;
			}
			/**
			 * @return the errorMessage
			 */
			public String getErrorMessage() {
				return errorMessage;
			}
			/**
			 * @param errorMessage the errorMessage to set
			 */
			public void setErrorMessage(String errorMessage) {
				this.errorMessage = errorMessage;
			}
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

		/**
		 * @return the fieldValidatorConfigs
		 */
		public List<FieldValidatorConfig> getFieldValidatorConfigs() {
			return fieldValidatorConfigs;
		}

		/**
		 * @param fieldValidatorConfigs the fieldValidatorConfigs to set
		 */
		public void setFieldValidatorConfigs(List<FieldValidatorConfig> fieldValidatorConfigs) {
			this.fieldValidatorConfigs = fieldValidatorConfigs;
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
	 * @return the customValidationEnabled
	 */
	public boolean isCustomValidationEnabled() {
		return customValidationEnabled;
	}

	/**
	 * @param customValidationEnabled the customValidationEnabled to set
	 */
	public void setCustomValidationEnabled(boolean customValidationEnabled) {
		this.customValidationEnabled = customValidationEnabled;
	}

	/**
	 * @return the mandatoryErrorCodeFormat
	 */
	public String getMandatoryErrorCodeFormat() {
		return mandatoryErrorCodeFormat;
	}

	/**
	 * @param mandatoryErrorCodeFormat the mandatoryErrorCodeFormat to set
	 */
	public void setMandatoryErrorCodeFormat(String mandatoryErrorCodeFormat) {
		this.mandatoryErrorCodeFormat = mandatoryErrorCodeFormat;
	}

	/**
	 * @return the regularExpErrorCodeFormat
	 */
	public String getRegularExpErrorCodeFormat() {
		return regularExpErrorCodeFormat;
	}

	/**
	 * @param regularExpErrorCodeFormat the regularExpErrorCodeFormat to set
	 */
	public void setRegularExpErrorCodeFormat(String regularExpErrorCodeFormat) {
		this.regularExpErrorCodeFormat = regularExpErrorCodeFormat;
	}

	/**
	 * @return the maxLengthErrorCodeFormat
	 */
	public String getMaxLengthErrorCodeFormat() {
		return maxLengthErrorCodeFormat;
	}

	/**
	 * @param maxLengthErrorCodeFormat the maxLengthErrorCodeFormat to set
	 */
	public void setMaxLengthErrorCodeFormat(String maxLengthErrorCodeFormat) {
		this.maxLengthErrorCodeFormat = maxLengthErrorCodeFormat;
	}

	/**
	 * @return the mandatoryErrorMessageFormat
	 */
	public String getMandatoryErrorMessageFormat() {
		return mandatoryErrorMessageFormat;
	}

	/**
	 * @param mandatoryErrorMessageFormat the mandatoryErrorMessageFormat to set
	 */
	public void setMandatoryErrorMessageFormat(String mandatoryErrorMessageFormat) {
		this.mandatoryErrorMessageFormat = mandatoryErrorMessageFormat;
	}

	/**
	 * @return the regularExpErrorMessageFormat
	 */
	public String getRegularExpErrorMessageFormat() {
		return regularExpErrorMessageFormat;
	}

	/**
	 * @param regularExpErrorMessageFormat the regularExpErrorMessageFormat to set
	 */
	public void setRegularExpErrorMessageFormat(String regularExpErrorMessageFormat) {
		this.regularExpErrorMessageFormat = regularExpErrorMessageFormat;
	}

	/**
	 * @return the maxLengthErrorMessageFormat
	 */
	public String getMaxLengthErrorMessageFormat() {
		return maxLengthErrorMessageFormat;
	}

	/**
	 * @param maxLengthErrorMessageFormat the maxLengthErrorMessageFormat to set
	 */
	public void setMaxLengthErrorMessageFormat(String maxLengthErrorMessageFormat) {
		this.maxLengthErrorMessageFormat = maxLengthErrorMessageFormat;
	}

	/**
	 * @return the entityValidatorConfigs
	 */
	public List<EntityValidatorConfig> getEntityValidatorConfigs() {
		return entityValidatorConfigs;
	}

	/**
	 * @param entityValidatorConfigs the entityValidatorConfigs to set
	 */
	public void setEntityValidatorConfigs(List<EntityValidatorConfig> entityValidatorConfigs) {
		this.entityValidatorConfigs = entityValidatorConfigs;
	}
	
	@JsonIgnore
	Map<String, EntityValidatorConfig> entityValidatorConfigMap;
	
	@PostConstruct
	public void init() {
		this.entityValidatorConfigMap = new HashMap<String, EntityValidatorConfig>();
		if (this.entityValidatorConfigs != null) {
			this.entityValidatorConfigs.forEach(entityValidatorConfig -> {
				this.entityValidatorConfigMap.put(entityValidatorConfig.getDomainModelFQClassName(), entityValidatorConfig);
			});
		}
	}

	/**
	 * @return the entityValidatorConfigMap
	 */
	public Map<String, EntityValidatorConfig> getEntityValidatorConfigMap() {
		return entityValidatorConfigMap;
	}

	/**
	 * @return the customValidationBean
	 */
	public String getCustomValidationBean() {
		return customValidationBean;
	}

	/**
	 * @param customValidationBean the customValidationBean to set
	 */
	public void setCustomValidationBean(String customValidationBean) {
		this.customValidationBean = customValidationBean;
	}
}
