package com.fico.core.util;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class ObjectMapperConfig {

	@Autowired
	com.wavemaker.runtime.WMAppObjectMapper wmAppObjectMapper;

	@Bean(name = "customObjectMapper")
	public ObjectMapper customObjectMapper() {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		JavaTimeModule module = new JavaTimeModule();		
		//DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		//objectMapper.setDateFormat(df);
		module.addDeserializer(LocalDateTime.class, new CustomDateDeserializer());
		objectMapper.registerModule(module);

		return objectMapper;
	}

	@Bean
	public Module dynamoDemoEntityDeserializer() {
		JavaTimeModule module = new JavaTimeModule();
		//module.addDeserializer(Date.class, new DateConverter.Deserialize());
		wmAppObjectMapper.registerReaderModule(module);
		wmAppObjectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
		return module;
	}

}
