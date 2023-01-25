package com.fico.ps;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chriswayjones
 * This configuration class enables the spring caching and replaces the default key generator as per Hazelcast documentation.
 */
@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	public KeyGenerator keyGenerator() {
		return null;
	}

}
