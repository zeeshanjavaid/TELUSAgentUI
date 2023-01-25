package com.fico.ps.cache;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wavemaker.runtime.service.AppRuntimeService;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;

/**
 * @author chriswayjones
 *	Exposes the internal FAWB hazelcast instance name for other classes to access.
 */
@Component
public class CacheHelper implements InitializingBean {

	private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(CacheHelper.class.getName());

	@Autowired
	private AppRuntimeService appRuntimeService;

	private static String instanceName = null;

	@Override
	public void afterPropertiesSet() throws Exception {

		String projectName = this.appRuntimeService.getProjectDisplayName();
		String projectNameInUpperCase = (projectName != null) ? projectName.toUpperCase() : "";
		String componentId = System.getenv("DMP_COMPONENT_UUID");

		if (StringUtils.isNotEmpty(componentId)) {
			String uppercaseComponentId = componentId.toUpperCase();
			CacheHelper.instanceName = getInstanceName(projectNameInUpperCase) + "_" + uppercaseComponentId;
		} else {
			CacheHelper.instanceName = getInstanceName(projectNameInUpperCase);

		}

		logger.info("Hazelcast Instance Name: " + CacheHelper.instanceName);

	}

	private String getInstanceName(String projectName) {
		return "FAWB_APPLICATION_" + projectName + "_" + "INSTANCE";
	}

	public String getInstanceName() {
		return instanceName;
	}

}
