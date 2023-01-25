package com.fico.pscomponent.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class PropertiesUtil {

	@Autowired
	private Environment environment;

	public String getPropertyValue(String key) {
		return environment.getProperty(key);
	}
}
