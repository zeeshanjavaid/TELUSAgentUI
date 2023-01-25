package com.fico.ps.model.serializers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class DateTimeToStringSerializer extends StdSerializer<LocalDateTime> {

	protected DateTimeToStringSerializer(Class<LocalDateTime> t) {
		super(t);
		// TODO Auto-generated constructor stub
	}
	
	public DateTimeToStringSerializer() {
        super(LocalDateTime.class);
    }
    
    
	private static final long serialVersionUID = 1L;

	@Override
	public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		String convertedDateString = null;
		
		if(value != null) {
			 String cvtValue = value.format(DateTimeFormatter.ISO_DATE);
			
			if(cvtValue.length() > 10)
				convertedDateString = cvtValue.substring(0, 10);
			else
				convertedDateString = cvtValue;
		}
		
		gen.writeString(convertedDateString);
	}

}
