package com.fico.ps.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hazelcast.cluster.Member;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.map.LocalMapStats;
import com.hazelcast.nearcache.NearCacheStats;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;

/**
 * @author chriswayjones
 * A set of utility methods to obtain information around the cache configuration and current configuration.
 */
@Component
public class CacheUtils {

	private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(CacheHelper.class.getName());

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private CacheHelper cacheHelper;

	@Autowired
	private com.wavemaker.runtime.WMAppObjectMapper wmAppObjectMapper;

	public Map<String, Integer> cacheSizes() {

		Map<String, Integer> cacheSize = new HashMap<>();

		HazelcastInstance hazelcastInstance = Hazelcast.getHazelcastInstanceByName(cacheHelper.getInstanceName());

		for (String cacheName : cacheManager.getCacheNames()) {

			IMap<Object, Object> cacheMap = hazelcastInstance.getMap(cacheName);

			cacheSize.put(cacheName, cacheMap.size());

		}

		return cacheSize;
	}

	public Integer memberCount() {

		HazelcastInstance hazelcastInstance = Hazelcast.getHazelcastInstanceByName(cacheHelper.getInstanceName());

		return hazelcastInstance.getCluster().getMembers().size();
	}

	public Config config() {
		HazelcastInstance hazelcastInstance = Hazelcast.getHazelcastInstanceByName(cacheHelper.getInstanceName());

		return hazelcastInstance.getConfig();

	}

	public Map<String, LocalMapStats> stats() {

		Map<String, LocalMapStats> cacheStats = new HashMap<>();

		HazelcastInstance hazelcastInstance = Hazelcast.getHazelcastInstanceByName(cacheHelper.getInstanceName());

		for (String cacheName : cacheManager.getCacheNames()) {

			IMap<Object, Object> cacheMap = hazelcastInstance.getMap(cacheName);

			LocalMapStats mapStatistics = cacheMap.getLocalMapStats();

			cacheStats.put(cacheName, mapStatistics);
			logger.info("Local Map Stats. CacheName:" + cacheName + " Hits:" + mapStatistics.getHits() + " Owned Entry:"
					+ mapStatistics.getOwnedEntryCount() + " Max Get Latency:" + mapStatistics.getMaxGetLatency());

			if (mapStatistics.getNearCacheStats() != null) {
				NearCacheStats nearCacheStatistics = mapStatistics.getNearCacheStats();
				logger.info("Near Cache Stats. CacheName:" + cacheName + " Hits:" + nearCacheStatistics.getHits()
						+ " Owned Entry:" + nearCacheStatistics.getOwnedEntryCount() + " Hit Ratio:"
						+ nearCacheStatistics.getRatio());
			}

		}

		return cacheStats;
	}

	public ObjectNode clusterMembers() {

		ObjectNode rootNode = wmAppObjectMapper.createObjectNode();

		ArrayNode instanceArray = wmAppObjectMapper.createArrayNode();

		Set<HazelcastInstance> hazelcastInstances = Hazelcast.getAllHazelcastInstances();

		rootNode.put("instanceCount", hazelcastInstances.size());

		for (HazelcastInstance instance : hazelcastInstances) {

			ObjectNode instanceNode = wmAppObjectMapper.createObjectNode();
			instanceNode.put("name", instance.getName());

			Set<Member> members = instance.getCluster().getMembers();
			instanceNode.put("memberCount", members.size());

			ArrayNode memberArray = wmAppObjectMapper.createArrayNode();

			for (Member member : members) {
				ObjectNode memberNode = wmAppObjectMapper.createObjectNode();
				memberNode.put("uuid", member.getUuid().toString());
				memberArray.add(memberNode);
			}
			instanceNode.set("members", memberArray);
			instanceArray.add(instanceNode);
		}

		rootNode.set("instances", instanceArray);

		return rootNode;
	}

}
