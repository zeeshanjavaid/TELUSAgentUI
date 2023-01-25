package com.fico.ps.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.wavemaker.runtime.hazelcast.FawbAppHazelcastInstance;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;

/**
 * @author chriswayjones
 * This is an extended version of the Hazelcast-Spring cache manager implementation providing default configuration and the instance lookup.
 */
@Component
public class HazelcastCacheManager implements CacheManager, InitializingBean {

	//Autowire in place to make sure hazelcast is available
	@Autowired
	private FawbAppHazelcastInstance fawbAppHazelcastInstance;

	private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(HazelcastCacheManager.class.getName());

	private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<>();

	private HazelcastInstance hazelcastInstance;

	@Autowired
	private CacheHelper cacheHelper;

	@Autowired
	private CacheMapConfig cacheMapConfig;

	private long defaultReadTimeout;

	private Map<String, Long> readTimeoutMap = new HashMap<>();

	public HazelcastCacheManager() {
	}

	public HazelcastCacheManager(HazelcastInstance hazelcastInstance) {
		this.hazelcastInstance = hazelcastInstance;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		hazelcastInstance = Hazelcast.getHazelcastInstanceByName(cacheHelper.getInstanceName());
		logger.info("Configured Spring Cache Manager with Hazelcast instance " + cacheHelper.getInstanceName());

	}

	@Override
	public Cache getCache(String name) {
		Cache cache = this.caches.get(name);
		if (cache == null) {

			this.hazelcastInstance.getConfig().addMapConfig(cacheMapConfig.getDefaultMapConfig(name));

			IMap<Object, Object> map = this.hazelcastInstance.getMap(name);

			cache = new HazelcastCache(map);
			long cacheTimeout = calculateCacheReadTimeout(name);
			((HazelcastCache) cache).setReadTimeout(cacheTimeout);
			Cache currentCache = this.caches.putIfAbsent(name, cache);
			if (currentCache != null)
				cache = currentCache;
		}
		return cache;
	}

	@Override
	public Collection<String> getCacheNames() {
		Set<String> cacheNames = new HashSet<>();
		Collection<DistributedObject> distributedObjects = this.hazelcastInstance.getDistributedObjects();
		for (DistributedObject distributedObject : distributedObjects) {
			if (distributedObject instanceof IMap) {
				IMap<?, ?> map = (IMap<?, ?>) distributedObject;
				cacheNames.add(map.getName());
			}
		}
		return cacheNames;
	}

	private long calculateCacheReadTimeout(String name) {
		Long timeout = getReadTimeoutMap().get(name);
		return (timeout == null) ? this.defaultReadTimeout : timeout.longValue();
	}

	public HazelcastInstance getHazelcastInstance() {
		return this.hazelcastInstance;
	}

	public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
		this.hazelcastInstance = hazelcastInstance;
	}

	public long getDefaultReadTimeout() {
		return this.defaultReadTimeout;
	}

	public void setDefaultReadTimeout(long defaultReadTimeout) {
		this.defaultReadTimeout = defaultReadTimeout;
	}

	public Map<String, Long> getReadTimeoutMap() {
		return this.readTimeoutMap;
	}

}
