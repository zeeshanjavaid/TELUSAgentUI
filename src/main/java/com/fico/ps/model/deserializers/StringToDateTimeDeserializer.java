package com.fico.ps.model.deserializers;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class StringToDateTimeDeserializer extends StdDeserializer<LocalDateTime> {
	
	protected StringToDateTimeDeserializer(Class<LocalDateTime> vc) {
		super(vc);
	}
	
	public StringToDateTimeDeserializer() {
		super(LocalDateTime.class);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		LocalDateTime timeToStore = null;

		try {
			if(p.getText() != null) {
				timeToStore = LocalDateTime.parse(p.getText() + "T00:00:00");
			}
		} catch (Exception e) {
			timeToStore = null;
		}
		
		return timeToStore;
	}

}
