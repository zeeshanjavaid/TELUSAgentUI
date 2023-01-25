package com.fico.ps.cache;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hazelcast.cluster.Member;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;

/**
 * @author chriswayjones
 * A utility to look-up a Hazelcast instance and execute a properties refresh on all members of the cluster.
 */
@Component
public class DistributedRefreshUtil {

	private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(CacheHelper.class.getName());

	@Autowired
	private com.wavemaker.runtime.WMAppObjectMapper wmAppObjectMapper;

	@Autowired
	private CacheHelper cacheHelper;

	public ObjectNode properties() {
		logger.info("Executing properties refresh on all nodes.");

		ObjectNode rootNode = wmAppObjectMapper.createObjectNode();
		rootNode.put("instance", cacheHelper.getInstanceName());
		Boolean result = null;

		HazelcastInstance instance = Hazelcast.getHazelcastInstanceByName(cacheHelper.getInstanceName());

		if (instance != null) {

			logger.info("Invoking refresh on instance: " + cacheHelper.getInstanceName());

			// Invoke refresh on all hazelcast members
			IExecutorService executorService = instance.getExecutorService("default");
			Map<Member, Future<Boolean>> futures = executorService.submitToAllMembers(new DistributedRefresh());

			// Fetch results from each member
			try {
				for (Entry<Member, Future<Boolean>> future : futures.entrySet()) {
					Boolean execResult = future.getValue().get();
					if (result == null)
						result = execResult;
					else
						result = execResult && result;

					logger.info("Distributed properties refresh complete for member " + future.getKey().toString());

				}

			} catch (Exception ex) {
				result = false;
				logger.error("Refresh failed on " + instance.getName(), ex);
			}
		}

		rootNode.put("result", result);

		return rootNode;
	}

}
