package com.fico.ps.hermes.mapper;

import java.util.Map;

import com.fico.ps.hermes.config.ApplicationConfig;
import com.fico.ps.hermes.core.ChangeHolder;

/**
 * @author MushfikKhan
 *
 */
public interface IReferenceMapper {
	
	public Object mapReferenceToEntity(Object domainRootObject, Object entityObject, ChangeHolder changeHolder,
			Map<String, Map<Object, ChangeHolder>> changeMapByClassName, String domainModelClassName, ApplicationConfig applicationConfig) throws Exception;

}
