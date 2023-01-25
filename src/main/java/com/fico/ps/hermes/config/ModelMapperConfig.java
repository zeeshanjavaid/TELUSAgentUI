package com.fico.ps.hermes.config;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.Mapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fico.ps.hermes.config.ApplicationConfig.DomainModelSaveConfig;
import com.fico.ps.hermes.config.ApplicationConfig.DomainModelSaveConfig.DirectMapping.FieldMapping;

/**
 * @author MushfikKhan
 *
 */
public class ModelMapperConfig {

	private static Logger logger = LoggerFactory.getLogger(ModelMapperConfig.class);
	
	/**
	 * @return modelMapper
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ModelMapper getModelMapper(ApplicationConfig applicationConfig) throws ClassNotFoundException {
		ModelMapper modelMapper = new ModelMapper();
		switch (applicationConfig.getModelMapperConfig().getMatchingStrategy()) {
		case "STRICT":
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			break;
		case "STANDARD":
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
			break;
		case "LOOSE":
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
			break;
		default:
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		}
//		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		logger.info("Initializing Model Mapper bean.");
		for (DomainModelSaveConfig domainModelSaveConfig : applicationConfig.getDomainModelSaveConfig()) {
			logger.debug("====== Mapping for : {} -> {} ======", domainModelSaveConfig.getDomainModelClassName(), domainModelSaveConfig.getSaveConfig().getEnityClassName());
			TypeMap typeMap = modelMapper.typeMap(Class.forName(domainModelSaveConfig.getDomainModelClassName()),
					Class.forName(domainModelSaveConfig.getSaveConfig().getEnityClassName()));
			if (domainModelSaveConfig.getDirectMapping() != null
					&& domainModelSaveConfig.getDirectMapping().getFieldMappings() != null) {
				for (FieldMapping fieldMapping : domainModelSaveConfig.getDirectMapping().getFieldMappings()) {
					typeMap.addMappings(new PropertyMap() {

						@Override
						protected void configure() {
							map(source(fieldMapping.getSourceField()), destination(fieldMapping.getDestField()));
							logger.debug("CustomMapping[{} -> {}]", fieldMapping.getSourceField(),
									fieldMapping.getDestField());
						}
					});
				}
			}
			if (logger.isDebugEnabled()) {
				List<Mapping> mappings = typeMap.getMappings();
				logger.debug("Effective mapping");
				if (mappings != null) {
					for (Mapping mapping : mappings) {
						logger.debug("{}", mapping);
					}
				}
			}
		}
		return modelMapper;
	}
}
