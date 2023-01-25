package com.fico.ps.hermes.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.fico.ps.hermes.config.ApplicationConfig;
import com.fico.ps.hermes.config.ValidatorConfig;
import com.fico.ps.hermes.hook.ICustomValidator;
import com.fico.ps.hermes.save.Error;
import com.fico.ps.hermes.util.ReflectionUtility;
import com.fico.ps.hermes.util.Utility;

/**
 * @author MushfikKhan
 *
 */
@Component
public class PreProcessor {
	
	private static Logger logger = LoggerFactory.getLogger(PreProcessor.class);
	
	
	@Autowired
	private GenericValidator genericValidator;
	
	@Autowired
	private ValidatorConfig validatorConfig;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private int tempId = -1000;
	
	/**
	 * @param obj
	 * @param error
	 * @return
	 * @throws Exception 
	 */
	public <T> T process(T obj, ApplicationConfig applicationConfig, com.fico.ps.hermes.save.Error error) throws Exception {
		Map<String, String> classIdAttribNameMap = new HashMap<String, String>();
		applicationConfig.getDomainModelSaveConfig().forEach(domainModelSaveConfig -> {
			classIdAttribNameMap.put(domainModelSaveConfig.getDomainModelClassName(),
					domainModelSaveConfig.getIdAttribName() != null ? domainModelSaveConfig.getIdAttribName()
							: Utility.getDefaultIdAttributeName(applicationConfig));
		});
		
		updateObjectId(obj, Utility.getDefaultIdAttributeName(applicationConfig), classIdAttribNameMap, error);
		
		customValidator(obj, error);

		return obj;
	}



	/**
	 * @param obj
	 * @param classIdAttribNameMap
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	private Object updateObjectId(Object obj, String defaultIdAttributeName, Map<String, String> classIdAttribNameMap, Error error) throws Exception {
		String fqClassName = obj.getClass().getName();
		String idAttributeName = classIdAttribNameMap.get(fqClassName);
		if (idAttributeName == null || "".equals(idAttributeName)) {
			idAttributeName = defaultIdAttributeName;
		}
		//logger.info("obj : {}, idAttributeName : {}", obj, idAttributeName);
		Field[] fields = obj.getClass().getDeclaredFields();
		if (validatorConfig.isEnabled()) {
			error = genericValidator.validate(obj, fields, error);
		}
		if (fields != null && idAttributeName != null) {
			for (Field field : fields) {
				Class<?> type = field.getType();
				if (Utility.isPrimitiveOrPrimitiveWrapperOrString(type)) {
					if (field.getName().equals(idAttributeName)) {
						ReflectionUtility.updateIdValueIfEmpty(obj, field, getTempId());
					}
				} else {
					try {
						//logger.info("field: '{}' type: '{}' and object '{}' content '{}'", field.getName(), type.getCanonicalName(), obj, ToStringBuilder.reflectionToString(obj));
					if (type.isArray() && !type.getComponentType().isPrimitive()) {
					//	if (type.isArray()) {
							field.setAccessible(true);
							Object[] objArray = (Object[]) field.get(obj);
							if (objArray != null) {
								for (Object o : objArray) {
									updateObjectId(o, defaultIdAttributeName, classIdAttribNameMap, error);
								}
							}
						} else if (type == List.class) {
							field.setAccessible(true);
							List objList = (List) field.get(obj);
							if (objList != null) {
								for (Object o : objList) {
									updateObjectId(o, defaultIdAttributeName, classIdAttribNameMap, error);
								}
							}
						} else {
							field.setAccessible(true);
							Object o = field.get(obj);
							if (o != null) {
								updateObjectId(o, defaultIdAttributeName, classIdAttribNameMap, error);
							}
						}
					} catch (Throwable e) {
						logger.error("Error occured while updating valueof ID attribute '{}' of domain class '{}'", idAttributeName, fqClassName);
						logger.error(e.getMessage(), e);
						throw new Exception(e);
					}
				}
			}
		}
		
		return obj;
	}
	
	/**
	 * @param obj
	 * @param error
	 */
	private void customValidator(Object obj, com.fico.ps.hermes.save.Error error) {
		if (validatorConfig.isCustomValidationEnabled()) {
			try {
				ICustomValidator customValidator = (ICustomValidator) applicationContext.getBean(validatorConfig.getCustomValidationBean());
				if (customValidator != null) {
					Error customError = customValidator.validate(obj);
					if (customError != null && customError.getDetails() != null) {
						customError.getDetails().forEach(e -> error.addError(e));
					}
				}
			} catch (NoSuchBeanDefinitionException e) {
				logger.warn("Custom Validation bean '{}' specified in validation config file could not be found in application context.", validatorConfig.getCustomValidationBean());
				logger.warn("Custom Validation will be skipped.");
				logger.warn(e.getMessage(), e);
			} catch (ClassCastException e) {
				logger.warn("Custom Validation bean '{}' specified in validation config file must implement 'com.fico.ps.hermes.hook.ICustomValidator'.", validatorConfig.getCustomValidationBean());
				logger.warn("Custom Validation will be skipped.");
				logger.warn(e.getMessage(), e);
			}
		} else {
			logger.info("Custom validation is not enabled.");
		}
	}
	
	/**
	 * @return
	 */
	private Integer getTempId() {
		if (this.tempId < -10000) {
			this.tempId = -1000;
		}
		return this.tempId--;
	}
}
