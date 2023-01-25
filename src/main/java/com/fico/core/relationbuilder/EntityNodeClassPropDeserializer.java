package com.fico.core.relationbuilder;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * @author AnubhavDas
 * <br><br>
 *
 * @implSpec
 * De-serialization class for <b>clazz</b> property of {@link EntityNode}
 */
@SuppressWarnings("rawtypes")
public class EntityNodeClassPropDeserializer extends StdDeserializer<Class> {

	private static final long serialVersionUID = -1890511680396873558L;
	
	private EntityNodeClassPropDeserializer() {
		this(null);
	}
	
	protected EntityNodeClassPropDeserializer(Class<Class> vc) {
		super(vc);
	}
	
	@Override
	public Class deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		try {
			return Class.forName(p.getText());
		} catch (ClassNotFoundException|IOException e) {
			throw new RuntimeException(e);
		}
	}

}
