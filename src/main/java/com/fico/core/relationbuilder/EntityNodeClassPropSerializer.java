package com.fico.core.relationbuilder;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * @author AnubhavDas
 * <br><br>
 *
 * @implSpec
 * Serialization class for <b>clazz</b> property of {@link EntityNode}
 */
@SuppressWarnings("rawtypes")
public class EntityNodeClassPropSerializer extends StdSerializer<Class> {
	
	private static final long serialVersionUID = -557752486450303803L;
	
	private EntityNodeClassPropSerializer() {
		this(null);
	}
	
	protected EntityNodeClassPropSerializer(Class<Class> vc) {
		super(vc);
	}
	
	@Override
	public void serialize(Class value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(value.getName());
	}
	
}
