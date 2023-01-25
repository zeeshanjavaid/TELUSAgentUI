package com.fico.ps.cache;

import java.io.Serializable;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fico.pscomponent.service.property.FawbPropertySourceRefresher;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;

/**
 * @author chriswayjones
 * A callable implementation intended to perform a properties refresh on each member of a Hazelcast cluster.
 */
public class DistributedRefresh implements Callable<Boolean>, Serializable, HazelcastInstanceAware {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(DistributedRefresh.class.getName());

	private transient HazelcastInstance hazelcastInstance;

	public DistributedRefresh() {
	}

	@Override
	public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
		this.hazelcastInstance = hazelcastInstance;
	}

	@Override
	public Boolean call() {

		logger.info("Distributed properties refresh starting for member "
				+ hazelcastInstance.getCluster().getLocalMember().toString());

		boolean result = false;

		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();

		FawbPropertySourceRefresher fawbPropertySourceRefresher = context.getBean(FawbPropertySourceRefresher.class);
		fawbPropertySourceRefresher.refresh();

		result = true;

		return result;
	}
}
