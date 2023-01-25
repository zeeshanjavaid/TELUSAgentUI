package com.fico.ps.hermes.config;

import java.math.BigDecimal;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.ListCompareAlgorithm;
import org.javers.core.diff.custom.CustomBigDecimalComparator;
import org.javers.core.metamodel.clazz.EntityDefinition;
import org.springframework.context.annotation.Configuration;

import com.fico.ps.hermes.config.ApplicationConfig.DomainModelSaveConfig;

/**
 * @author MushfikKhan
 *
 */
@Configuration
public class JaversBuilderConfig {

	/**
	 * @return
	 * @throws ClassNotFoundException
	 */
	public Javers getJavers(ApplicationConfig applicationConfig) throws ClassNotFoundException {
		JaversBuilder javersBuilder = JaversBuilder.javers()
				.registerValue(BigDecimal.class, new CustomBigDecimalComparator(2)) //To compare BigDecimal properties with 2 decimal precision.
				.withListCompareAlgorithm(ListCompareAlgorithm.AS_SET);

		for (DomainModelSaveConfig domainModelSaveConfig : applicationConfig.getDomainModelSaveConfig()) {
			javersBuilder
					.registerEntity(new EntityDefinition(Class.forName(domainModelSaveConfig.getDomainModelClassName()),
							domainModelSaveConfig.getIdAttribName() != null
									&& !domainModelSaveConfig.getIdAttribName().trim().equals("")
											? domainModelSaveConfig.getIdAttribName()
											: applicationConfig.getDefaultIdAttribName()));

		}

		return javersBuilder.build();
	}
}
