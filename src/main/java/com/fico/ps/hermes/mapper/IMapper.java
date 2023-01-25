package com.fico.ps.hermes.mapper;

import com.fico.ps.hermes.config.ConfigurationProvider;
import com.fico.ps.hermes.core.ChangeHolder;

/**
 * @author MushfikKhan
 *
 */
public interface IMapper {
	
	public Object mapToEntity(ChangeHolder changeHolder, String domainModelClassName, ConfigurationProvider configurationProvider) throws Exception;

}
