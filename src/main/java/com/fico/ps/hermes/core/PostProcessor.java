package com.fico.ps.hermes.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fico.ps.hermes.config.ApplicationConfig;
import com.fico.ps.hermes.util.ReflectionUtility;
import com.fico.ps.hermes.util.Utility;

/**
 * @author MushfikKhan
 *
 */
@Component
public class PostProcessor {
	
	private static Logger logger = LoggerFactory.getLogger(PostProcessor.class);
	
	
	/**
	 * @param <T>
	 * @param obj
	 * @param changeMapByClassName
	 * @param applicationConfig
	 * @return
	 * @throws Exception
	 */
	public <T> T process(T obj, Map<String, Map<Object, ChangeHolder>> changeMapByClassName, ApplicationConfig applicationConfig) throws Exception {
		Map<String, String> classIdAttribNameMap = new HashMap<String, String>();
		applicationConfig.getDomainModelSaveConfig().forEach(domainModelSaveConfig -> {
			classIdAttribNameMap.put(domainModelSaveConfig.getDomainModelClassName(),
					domainModelSaveConfig.getIdAttribName() != null ? domainModelSaveConfig.getIdAttribName()
							: Utility.getDefaultIdAttributeName(applicationConfig));
		});
		
		updateObjectId(obj, Utility.getDefaultIdAttributeName(applicationConfig), classIdAttribNameMap, changeMapByClassName);
		
		return obj;
	}

	/**
	 * @param obj
	 * @param classIdAttribNameMap
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	private void updateObjectId(Object obj, String defaultIdAttributeName, Map<String, String> classIdAttribNameMap, Map<String, Map<Object, ChangeHolder>> changeMapByClassName) throws Exception { 
		String fqClassName = obj.getClass().getName();
		String idAttributeName = classIdAttribNameMap.get(fqClassName);
		if (idAttributeName == null || "".equals(idAttributeName)) {
			idAttributeName = defaultIdAttributeName;
		}
		logger.debug("obj : {}, idAttributeName : {}", obj, idAttributeName);
		Field[] fields = obj.getClass().getDeclaredFields();
		if (fields != null && idAttributeName != null) {
			for (Field field : fields) {
				Class<?> type = field.getType();
				if (Utility.isPrimitiveOrPrimitiveWrapperOrString(type)) {
					if (field.getName().equals(idAttributeName)) {
						Object idObject = ReflectionUtility.getPropertyValue(obj, field);
						Integer domainId = (idObject != null) && (idObject instanceof Integer) ? (Integer)idObject : 0;
						if(domainId < 0) {
							Object entityId = getEntityId(changeMapByClassName, idObject, fqClassName);
							ReflectionUtility.setPropertyValue(obj, field, entityId);
						}
					}
				} else {
					try {
						if (type.isArray() && !type.getComponentType().isPrimitive()) {
						//if (type.isArray()) {
							field.setAccessible(true);
							Object[] objArray = (Object[]) field.get(obj);
							if (objArray != null) {
								for (Object o : objArray) {
									updateObjectId(o, defaultIdAttributeName, classIdAttribNameMap, changeMapByClassName);
								}
							}
						} else if (type == List.class) {
							field.setAccessible(true);
							List objList = (List) field.get(obj);
							if (objList != null) {
								for (Object o : objList) {
									updateObjectId(o, defaultIdAttributeName, classIdAttribNameMap, changeMapByClassName);
								}
							}
						} else {
							field.setAccessible(true);
							Object o = field.get(obj);
							if (o != null) {
								updateObjectId(o, defaultIdAttributeName, classIdAttribNameMap, changeMapByClassName);
							}
						}
					} catch (IllegalArgumentException|IllegalAccessException e) {
						logger.error("Error occured while updating valueof ID attribute '{}' of domain class '{}'", idAttributeName, fqClassName);
						logger.error(e.getMessage(), e);
						throw new Exception(e);
					} 
				}
			}
		}
	}
	
	/**
	 * @param changeMapByClassName
	 * @param domainId
	 * @param fqClassName
	 * @return
	 */
	private Object getEntityId(Map<String, Map<Object, ChangeHolder>> changeMapByClassName, Object domainId,
			String fqClassName) {
		Object entityId = null;
		Map<Object, ChangeHolder> changeMapByID = changeMapByClassName.get(fqClassName);
		if (changeMapByID != null && changeMapByID.get(domainId) != null) {
			entityId = changeMapByID.get(domainId).getEntityId();
		}
		return entityId;
	}
}
