package com.fico.ps.hermes.util;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author MushfikKhan
 *
 */
@Component
public class JacksonUtility {
	
	private static Logger logger = LoggerFactory.getLogger(JacksonUtility.class);

	/** The object mapper. */
	private ObjectMapper objectMapper;
	
	/**
	 * Initialize.
	 */
	@PostConstruct
	public void initialize() {
	    this.objectMapper = new ObjectMapper();
	}
	
	/**
	 * Serialize.
	 *
	 * @param object the object
	 * @return the string
	 * @throws Exception 
	 */
	public String serialize(Object object) throws Exception {
		try {
			return this.objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			logger.error("Fatal Error occured in serializing as JSON", e);
			throw new Exception(e);
		}
	}
	
	/**
	 * De serialize.
	 *
	 * @param objectAsStr the object as str
	 * @param clazz the clazz
	 * @return the object
	 * @throws Exception 
	 */
	public Object deSerialize(String objectAsStr, Class<?> clazz) throws Exception {
		try {
			return this.objectMapper.readValue(objectAsStr, clazz);
		} catch (IOException e) {
			logger.error("Fatal error occured in deSerializing from JSON", e);
			throw new Exception(e);
		}
	}
}
