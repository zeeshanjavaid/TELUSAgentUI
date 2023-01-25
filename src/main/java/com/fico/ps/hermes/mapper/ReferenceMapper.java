package com.fico.ps.hermes.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.javers.core.diff.changetype.ReferenceChange;
import org.javers.core.diff.changetype.container.ContainerElementChange;
import org.javers.core.diff.changetype.container.ListChange;
import org.javers.core.diff.changetype.container.ValueAdded;
import org.javers.core.metamodel.object.GlobalId;
import org.javers.core.metamodel.object.InstanceId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fico.ps.hermes.config.ApplicationConfig;
import com.fico.ps.hermes.config.ApplicationConfig.DomainModelSaveConfig;
import com.fico.ps.hermes.config.ApplicationConfig.DomainModelSaveConfig.DirectMapping.FieldMapping;
import com.fico.ps.hermes.config.ApplicationConfig.DomainModelSaveConfig.EntityReferenceMapping;
import com.fico.ps.hermes.core.ChangeHolder;
import com.fico.ps.hermes.core.EntityInfo;
import com.fico.ps.hermes.util.ReflectionUtility;
import com.fico.ps.hermes.util.Utility;

/**
 * @author MushfikKhan
 *
 */
@Component
public class ReferenceMapper implements IReferenceMapper {
	
	
	private static Logger logger = LoggerFactory.getLogger(ReferenceMapper.class);
	
	@Override
	public Object mapReferenceToEntity(Object domainRootObject, Object entityObject, ChangeHolder changeHolder,
			Map<String, Map<Object, ChangeHolder>> changeMapByClassName,
			String domainModelClassName, ApplicationConfig applicationConfig) throws Exception {
		logger.debug("Starting reference mapping for '{}' :  {}", domainModelClassName, changeHolder.getChangedDomainObject());
		Map<String, DomainModelSaveConfig> domainModelSaveConfigMap = applicationConfig.getDomainModelSaveConfigMap();
		DomainModelSaveConfig domainModelSaveConfig = domainModelSaveConfigMap.get(domainModelClassName);
		List<ReferenceChange> referenceChanges = changeHolder.getReferenceChanges();
		entityObject = updateDirectReferenceToEntity(entityObject, changeMapByClassName, domainModelSaveConfig, referenceChanges);
		
		entityObject = updateEntityReferenceMapping(domainRootObject, entityObject, changeHolder, changeMapByClassName, domainModelSaveConfig, domainModelSaveConfigMap, applicationConfig);
		
		return entityObject;
	}

	/**
	 * @param entityObject
	 * @param changeMapByClassName
	 * @param domainModelSaveConfigMap
	 * @param domainModelClassName
	 * @param referenceChanges
	 * @return
	 * @throws Exception 
	 */
	private Object updateDirectReferenceToEntity(Object entityObject,
			Map<String, Map<Object, ChangeHolder>> changeMapByClassName,
			DomainModelSaveConfig domainModelSaveConfig,
			List<ReferenceChange> referenceChanges) throws Exception {
		logger.debug("updateDirectReferenceToEntity() - entityObject: {}", entityObject);
		if (referenceChanges != null && referenceChanges.size() > 0) {
			for (ReferenceChange referenceChange : referenceChanges) {
				String domainPropertyName = referenceChange.getPropertyName();
				logger.debug("updateDirectReferenceToEntity() - domainPropertyName for reference change : {}", domainPropertyName);
				if ((referenceChange.getLeft() == null && referenceChange.getRight() != null) || 
						(referenceChange.getLeft() != null && referenceChange.getRight() != null)) {//Reference added or updated
					GlobalId globalIdRight = referenceChange.getRight();
					if (globalIdRight instanceof InstanceId) {
						InstanceId instanceId = (InstanceId)globalIdRight;
						String domainClassName = instanceId.getTypeName();
						Object domainId = instanceId.getCdoId();
						logger.debug("updateDirectReferenceToEntity() - added/updated reference of {} in {}", domainClassName, domainPropertyName);
						Map<Object, ChangeHolder> changeByClassName = changeMapByClassName.get(domainClassName);
						if (changeByClassName != null && changeByClassName.get(domainId) != null) {
							Object entityId = changeByClassName.get(domainId).getEntityId();
							String entityPropertyName = getEntityPropertyName(domainPropertyName, domainModelSaveConfig);
							if (entityPropertyName != null && !"".equals(entityPropertyName.trim())) {
								entityObject = ReflectionUtility.setPropertyValue(entityObject, entityId, entityPropertyName.trim());
							}
						}
					}
				} else if (referenceChange.getLeft() != null && referenceChange.getRight() == null) {//Reference removed
					logger.debug("Reference id has been removed for : " + referenceChange.getPropertyName());
					//No need to update removed ID as null, as it will not be there after dozer mapper, since reference object is also removed.
					//entityObject = updatePropertyValue(entityObject, null, getEntityPropertyName(domainPropertyName, domainModelSaveConfigMap.get(domainModelClassName)));
				}
				
			}
		}
		return entityObject;
	}
	

	/**
	 * @param entityObject
	 * @param changeGroupByClassNameMap
	 * @param domainModelSaveConfigMap 
	 * @param domainModelSaveConfigMap
	 * @param applicationConfig 
	 * @param domainModelClassName
	 * @param listReferenceMappings
	 * @return
	 * @throws Exception 
	 */
	private Object updateEntityReferenceMapping(Object domainRootObject, Object entityObject, ChangeHolder changeHolder,
			Map<String, Map<Object, ChangeHolder>> changeGroupByClassNameMap,
			DomainModelSaveConfig domainModelSaveConfig, Map<String, DomainModelSaveConfig> domainModelSaveConfigMap, ApplicationConfig applicationConfig) throws Exception {
		
		Object domainId = changeHolder.getDomainId();
//		EntityInfo entityInfo = null;
		logger.debug("updateEntityReferenceMapping() - entityObject: {}", entityObject);
		if (domainModelSaveConfig != null && domainModelSaveConfig.getEntityReferenceMappings() != null) {
			for (EntityReferenceMapping entityReferenceMapping : domainModelSaveConfig.getEntityReferenceMappings()) {
				EntityInfo entityInfo = getParentEntityIdWithReferenceChange(changeGroupByClassNameMap, domainId, null, entityReferenceMapping);
				logger.debug("updateEntityReferenceMapping() - updating field '{}' of class '{}' for reference '{}'", entityReferenceMapping.getEntityFieldName(), entityReferenceMapping.getParentDomainClass(), entityReferenceMapping.getReferencePathInDomainModel());
				if (entityInfo == null) {
					entityInfo = getParentEntityIdWithoutReferenceChange(domainRootObject, domainId, entityInfo,
							entityReferenceMapping, applicationConfig);
				}
				if (entityInfo != null) {
					entityObject = ReflectionUtility.setPropertyValue(entityObject, entityInfo.getEntityId(), entityReferenceMapping.getEntityFieldName());
					entityInfo.setEnityClassName(getEntityClassNameByDomainClass(entityInfo.getDomainClassName(), domainModelSaveConfigMap));
					changeHolder.setParentEntityInfo(entityInfo);
				}
			}
		}
		return entityObject;
	}

	/**
	 * @param domainRootObject
	 * @param domainId
	 * @param entityInfo
	 * @param entityReferenceMapping
	 * @param applicationConfig 
	 * @return
	 * @throws Exception 
	 */
	private EntityInfo getParentEntityIdWithoutReferenceChange(Object domainRootObject, Object domainId, EntityInfo entityInfo,
			EntityReferenceMapping entityReferenceMapping, ApplicationConfig applicationConfig) throws Exception {
		String referencePathInDomainModel = entityReferenceMapping.getReferencePathInDomainModel();
		String[] strArray = referencePathInDomainModel.split("\\.");
		if (strArray != null && strArray.length > 1) {
			entityInfo = getParentDomainId(1, strArray, domainRootObject, domainId, null, null, applicationConfig);
		}
		logger.debug("getParentEntityIdWithoutReferenceChange() - entityId:  '{}'", entityInfo != null ? entityInfo.getEntityId() : "");
		return entityInfo;
	}

	/**
	 * @param changeGroupByClassNameMap
	 * @param domainId
	 * @param entityInfo
	 * @param entityReferenceMapping
	 * @return
	 */
	private EntityInfo getParentEntityIdWithReferenceChange(Map<String, Map<Object, ChangeHolder>> changeGroupByClassNameMap, Object domainId,
			EntityInfo entityInfo, EntityReferenceMapping entityReferenceMapping) {
		Map<Object, ChangeHolder> parentClassMap = changeGroupByClassNameMap.get(entityReferenceMapping.getParentDomainClass());
		if (parentClassMap != null) {
			for (Entry<Object, ChangeHolder> entry : parentClassMap.entrySet()) {
				ChangeHolder parentChangeHolder = entry.getValue();
				List<ListChange> lsitChanges = parentChangeHolder.getListChanges();
				if (lsitChanges != null) {
					for (ListChange listChange : lsitChanges) {
						String listChangePropertyName = listChange.getPropertyName();
						String fieldNameInDomainClass = getConfiguredFieldName(entityReferenceMapping.getReferencePathInDomainModel());
						if (listChangePropertyName != null && fieldNameInDomainClass != null && listChangePropertyName.equals(fieldNameInDomainClass)) {
							for (ContainerElementChange containerElementChange: listChange.getChanges()) {
								if (containerElementChange instanceof ValueAdded) {
									ValueAdded valueAdded = (ValueAdded) containerElementChange;
									Object obj = valueAdded.getValue();
									if (obj != null && obj instanceof InstanceId) {
										InstanceId instanceId = (InstanceId) obj;
										if (instanceId != null && instanceId.getCdoId().equals(domainId)) {
											entityInfo = new EntityInfo();
											entityInfo.setEntityId(parentChangeHolder.getEntityId());
											entityInfo.setDomainClassName(entityReferenceMapping.getParentDomainClass());
										}
									}
								}
							}
						}
					}
				}
				List<ReferenceChange> lsitReferenceChanges = parentChangeHolder.getReferenceChanges();
				if (lsitReferenceChanges != null) {
					for (ReferenceChange referenceChange : lsitReferenceChanges) {//TODO
						String listChangePropertyName = referenceChange.getPropertyName();
						String fieldNameInDomainClass = getConfiguredFieldName(entityReferenceMapping.getReferencePathInDomainModel());
						if (listChangePropertyName != null && fieldNameInDomainClass != null && listChangePropertyName.equals(fieldNameInDomainClass)) {
							Object obj = referenceChange.getRight();
							if (obj != null && obj instanceof InstanceId) {
								InstanceId instanceId = (InstanceId) obj;
								if (instanceId != null && instanceId.getCdoId().equals(domainId)) {
									entityInfo = new EntityInfo();
									entityInfo.setEntityId(parentChangeHolder.getEntityId());
									entityInfo.setDomainClassName(entityReferenceMapping.getParentDomainClass());
								}
							}
						}
					}
				}
			}
		}
		logger.debug("getParentEntityIdWithReferenceChange() - entityId:  '{}'", entityInfo != null ? entityInfo.getEntityId() : "");
		return entityInfo;
	}
	
	/**
	 * @param index
	 * @param strArray
	 * @param domainObject
	 * @param domainId
	 * @param parentDomainId
	 * @return
	 * @throws Exception 
	 */
	private EntityInfo getParentDomainId(int index, String[] strArray, Object domainObject, Object domainId, Object parentDomainId, String parentDomainClassName, ApplicationConfig applicationConfig) throws Exception {
		EntityInfo parentEntityInfo = null;
		if (index > strArray.length || domainObject == null) {
			return parentEntityInfo;
		}
		try {
			String pIdAttributeName = Utility.getIdAttributeNameByDomainClass(domainObject.getClass().getName(), applicationConfig);
			if (index == strArray.length) {
				Field field = domainObject.getClass().getDeclaredField(pIdAttributeName);
				field.setAccessible(true);
				
				Object obj = field.get(domainObject);
				if (obj != null && obj.equals(domainId)) {
					parentEntityInfo = new EntityInfo();
					parentEntityInfo.setEntityId(parentDomainId);
					parentEntityInfo.setDomainClassName(parentDomainClassName);
				}
				return parentEntityInfo;
			} else {
				Object pDomainId = ReflectionUtility.getPropertyValue(domainObject, pIdAttributeName);
				String pDomainClassName = domainObject.getClass().getName();
				Field field = domainObject.getClass().getDeclaredField(strArray[index]);
				field.setAccessible(true);
				if (field.getType().equals(List.class)) {//TODO Add else if array type 
					@SuppressWarnings("rawtypes")
					List list = (List) field.get(domainObject);
					if (list != null) {
						for (Object obj : list) {
							parentEntityInfo = getParentDomainId(index+1, strArray, obj, domainId, pDomainId, pDomainClassName, applicationConfig);
							if (parentEntityInfo != null) {
								break;
							}
						}
					}
				} else {
					Object obj = field.get(domainObject);
					if (obj != null) {
						parentEntityInfo = getParentDomainId(index+1, strArray, obj, domainId, pDomainId, pDomainClassName, applicationConfig);
					}
				}
			}
		} catch (NullPointerException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)  {
			logger.error("Error occured while getting parent domain ID for '{}'", domainObject);
			logger.error("{}[{}] domainId : {}, parentDomainId : {}, parentDomainClassName : {}", strArray, index, domainId, parentDomainId, parentDomainClassName);
			logger.error(e.getMessage(), e);
			throw new Exception(e);
		}
			
		return parentEntityInfo;
	}

	/**
	 * @param referencePathInDomainModel
	 * @return
	 */
	private String getConfiguredFieldName(String referencePathInDomainModel) {
		String configuredFieldName = null;
		if (referencePathInDomainModel != null) {
			String[] strArray = referencePathInDomainModel.split("\\.");
			if (strArray != null && strArray.length > 0) {
				configuredFieldName = strArray[strArray.length-1];
			}
		}
		return configuredFieldName;
	}

	/**
	 * @param domainPropertyName
	 * @param domainModelSaveConfig
	 * @return
	 */
	private String getEntityPropertyName(String domainPropertyName, DomainModelSaveConfig domainModelSaveConfig) {
		String entityPropertyName = null;
		if (domainModelSaveConfig != null && domainModelSaveConfig.getDirectMapping() != null && domainModelSaveConfig.getDirectMapping().getFieldMappings() != null) {
			for (FieldMapping fieldMapping : domainModelSaveConfig.getDirectMapping().getFieldMappings()) {
				if (fieldMapping.getSourceField() != null) {
					String[] sourceFieldSplitted = fieldMapping.getSourceField().split("\\.");
					if (sourceFieldSplitted != null && sourceFieldSplitted.length > 0) {
						if (domainPropertyName.equals(sourceFieldSplitted[0])) {
							entityPropertyName = fieldMapping.getDestField();
						}
					}
				}
			}
		}
		return entityPropertyName;
	}
	

	/**
	 * @param domainClassName
	 * @param domainModelSaveConfigMap
	 * @return
	 */
	private String getEntityClassNameByDomainClass(String domainClassName,
			Map<String, DomainModelSaveConfig> domainModelSaveConfigMap) {
		DomainModelSaveConfig domainModelSaveConfig = domainModelSaveConfigMap.get(domainClassName);
		return domainModelSaveConfig != null && domainModelSaveConfig.getSaveConfig() != null ? domainModelSaveConfig.getSaveConfig().getEnityClassName() : "";
	}

}
