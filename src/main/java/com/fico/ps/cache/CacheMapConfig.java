package com.fico.ps.cache;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.config.NearCacheConfig;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;

/**
 * @author chriswayjones
 * Provides the default configuration for new caches created by spring caching.
 */
@Component
public class CacheMapConfig {
	
	 private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(CacheMapConfig.class.getName());
	
	@Autowired
	private Environment environment;
	
	private static final int DEFAULT_MAX_ENTRIES=1000;
	private static final int DEFAULT_TTL=900;
	private static final int DEFAULT_NEAR_TTL=300;
	private static final boolean DEFAULT_USE_NEAR_CACHE=true;
	private static final int DEFAULT_NEAR_MAX_ENTRIES=1000;


	public MapConfig getDefaultMapConfig(String name) {

		int maxEntries=environment.getProperty("app.environment.ps.cache.max.entries",int.class, DEFAULT_MAX_ENTRIES);
		int ttl=environment.getProperty("app.environment.ps.cache.ttl.seconds", int.class,DEFAULT_TTL);
		boolean useNearCache=environment.getProperty("app.environment.ps.cache.near.cache.enabled", boolean.class,DEFAULT_USE_NEAR_CACHE);
		int nearMaxEntries=environment.getProperty("app.environment.ps.near.cache.max.entries",int.class, DEFAULT_NEAR_MAX_ENTRIES);
		int nearTTL=environment.getProperty("app.environment.ps.near.cache.ttl.seconds", int.class,DEFAULT_NEAR_TTL);
		
		EvictionConfig eviction = new EvictionConfig()
				.setSize(maxEntries)
				.setMaxSizePolicy(MaxSizePolicy.PER_NODE)
				.setEvictionPolicy(EvictionPolicy.LRU);
		
		EvictionConfig nearEviction = new EvictionConfig()
				.setSize(nearMaxEntries)
				.setMaxSizePolicy(MaxSizePolicy.ENTRY_COUNT)
				.setEvictionPolicy(EvictionPolicy.LRU);
               
        
		NearCacheConfig nearCacheConfig = new NearCacheConfig()
				.setInMemoryFormat(InMemoryFormat.BINARY)
				.setTimeToLiveSeconds(nearTTL)
				.setInvalidateOnChange(true)
				.setEvictionConfig(nearEviction);
		
		MapConfig mapConfig = new MapConfig(name)
				.setStatisticsEnabled(true)
				.setInMemoryFormat(InMemoryFormat.BINARY)
				.setTimeToLiveSeconds(ttl)
				.setEvictionConfig(eviction);
			
		logger.info("Configuring new map cache: " + name + " TTL:" + ttl + " Max Entries:" + maxEntries);		
		
		if (useNearCache)	{	
		    mapConfig.setNearCacheConfig(nearCacheConfig);
		    logger.info("Configuring near cache for cache: " + name + " TTL:" + nearTTL +  " Max Entries:" + nearMaxEntries);
		}
		
		

		return mapConfig;
	}

}
