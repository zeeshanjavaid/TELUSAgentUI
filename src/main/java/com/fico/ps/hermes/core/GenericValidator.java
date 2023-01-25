package com.fico.ps.hermes.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fico.ps.hermes.save.Error;
import com.fico.ps.hermes.config.ValidatorConfig;
import com.fico.ps.hermes.config.ValidatorConfig.EntityValidatorConfig;
import com.fico.ps.hermes.config.ValidatorConfig.EntityValidatorConfig.FieldValidatorConfig;

/**
 * @author MushfikKhan
 *
 */
@Component
public class GenericValidator {

	@Autowired
	private ValidatorConfig validatorConfig;

	/**
	 * @param object
	 * @param fields
	 * @param error
	 * @return
	 */
	public Error validate(Object object, Field[] fields, Error error) {
		if (fields != null) {
			EntityValidatorConfig entityValidatorConfig = validatorConfig.getEntityValidatorConfigMap()
					.get(object.getClass().getName());
			if (entityValidatorConfig != null && entityValidatorConfig.getFieldValidatorConfigs() != null) {
				Map<String, FieldValidatorConfig> fieldValidatorConfigMap = new HashMap<String, FieldValidatorConfig>();
				entityValidatorConfig.getFieldValidatorConfigs()
						.forEach(fieldValidatorConfig -> fieldValidatorConfigMap.put(fieldValidatorConfig.getName(),
								fieldValidatorConfig));
				for (Field field : fields) {
					FieldValidatorConfig fieldValidatorConfig = fieldValidatorConfigMap.get(field.getName());
					if (fieldValidatorConfig != null) {
						try {
							field.setAccessible(true);
							Object fieldValueObj = field.get(object);
							if (fieldValidatorConfig.isMandatoryCheck()) {
								if (fieldValueObj == null) {
									String errorCode = String.format(validatorConfig.getMandatoryErrorCodeFormat(), fieldValidatorConfig.getErrorCode());
									String errorMessage = String.format(validatorConfig.getMandatoryErrorMessageFormat(), fieldValidatorConfig.getName());
									error.addError(new Error(errorCode, errorMessage));
								} 
							}
							if (fieldValueObj != null && field.getType() == String.class) {
								int maxLength = fieldValidatorConfig.getMaxLength();
								if (maxLength > 0 &&  ((String)fieldValueObj).length() > maxLength) {
									String errorCode = String.format(validatorConfig.getMaxLengthErrorCodeFormat(), fieldValidatorConfig.getErrorCode());
									String errorMessage = String.format(validatorConfig.getMaxLengthErrorMessageFormat(), fieldValidatorConfig.getName(), maxLength);
									error.addError(new Error(errorCode, errorMessage));
								}
								String regex = fieldValidatorConfig.getRegularExpression();
								if (regex != null) {
									Pattern pattern = Pattern.compile(regex);
									Matcher matcher = pattern.matcher((String)fieldValueObj);
									if (!matcher.matches()) {
										String errorCode = String.format(validatorConfig.getRegularExpErrorCodeFormat(), fieldValidatorConfig.getErrorCode());
										String errorMessage = String.format(validatorConfig.getRegularExpErrorMessageFormat(), fieldValidatorConfig.getName());
										error.addError(new Error(errorCode, errorMessage));
									}
								}
							}
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		return error;
	}
}
