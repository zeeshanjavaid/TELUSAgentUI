package com.fico.ps.hermes.util;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionUtility {
	
	private static Logger logger = LoggerFactory.getLogger(ReflectionUtility.class);
	
	/**
	 * @param object
	 * @param propertyName
	 * @return
	 * @throws Exception 
	 */
	public static Object getPropertyValue (Object object, String propertyName) throws Exception {
		Object fieldValueObj = null;
		try {
			logger.debug("getPropertyValue() - {} : {}",propertyName, object);
			Field entityField = object.getClass().getDeclaredField(propertyName);
			entityField.setAccessible(true);
			fieldValueObj = entityField.get(object);
		} catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException e) {
			logger.error("Error occured while getting property value for the property '{}' of {}", propertyName, object);
			logger.error(e.getMessage(), e);
			throw new Exception(e);
		}
		return fieldValueObj;
	}
	
	/**
	 * @param object
	 * @param value
	 * @param propertyName
	 * @return
	 * @throws Exception 
	 */
	public static Object setPropertyValue (Object object, Object value, String propertyName) throws Exception {
		try {
			logger.debug("setPropertyValue() - {}={} : {}",propertyName, value, object);
			Field entityField = object.getClass().getDeclaredField(propertyName);
			entityField.setAccessible(true);
			entityField.set(object, value);
		} catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException e) {
			logger.error("Error occured while setting property value '{}' for the property '{}' of {}", value, propertyName, object);
			logger.error(e.getMessage(), e);
			throw new Exception(e);
		}
		return object;
	}
	
	/**
	 * @param object
	 * @param field
	 * @param domainIdValue
	 * @return
	 * @throws Exception 
	 */
	public static Object updateIdValueIfEmpty (Object object, Field field, Object domainIdValue) throws Exception {
		try {
			field.setAccessible(true);
			Object idObject = field.get(object);
			if((idObject == null) || (idObject instanceof Integer && (Integer)idObject == 0)) {
				field.set(object, domainIdValue);
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			logger.error("Error occured while updating ID attribute value '{}' of {}", domainIdValue, object);
			logger.error(e.getMessage(), e);
			throw new Exception(e);
		}
		return object;
	}
	
	/**
	 * @param object
	 * @param objectField
	 * @return
	 * @throws Exception 
	 */
	public static Object getPropertyValue (Object object, Field objectField) throws Exception {
		Object fieldValueObj = null;
		try {
			objectField.setAccessible(true);
			fieldValueObj = objectField.get(object);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			logger.error("Error occured while getting propety value for field '{}' of {}", objectField, object);
			logger.error(e.getMessage(), e);
			throw new Exception(e);
		}
		return fieldValueObj;
	}
	
	/**
	 * @param object
	 * @param objectField
	 * @param value
	 * @return
	 * @throws Exception 
	 */
	public static Object setPropertyValue (Object object, Field objectField, Object value) throws Exception {
		try {
			if(!(objectField.getType().isPrimitive() && value == null)) {
				objectField.setAccessible(true);
				objectField.set(object, value);
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			logger.error("Error occured while setting propety value '{}' for field '{}' of {}", value, objectField, object);
			logger.error(e.getMessage(), e);
			throw new Exception(e);
		}
		return object;
	}
}
