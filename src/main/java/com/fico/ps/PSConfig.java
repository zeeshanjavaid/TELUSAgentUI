package com.fico.ps;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fico.ps.events.EntityListener;
import org.hibernate.SessionFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.joda.time.DateTime;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NameTokenizers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import javax.sql.DataSource;


@Configuration
@EnableAsync
@EnableScheduling
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackages = "com.fico.ps")
public class PSConfig implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(PSConfig.class);

    @Autowired
    private  com.wavemaker.runtime.WMAppObjectMapper wmAppObjectMapper;

    @Autowired
    private EntityListener entityListener;

    @Autowired
    @Qualifier("TELUSAgentUIDBSessionFactory")
    private SessionFactory CoreSessionFactory;
    
    @Autowired
	@Qualifier("TELUSAgentUIDBWMManagedDataSource")
	private DataSource coreWMdataSource;

/*    @Autowired
    @Qualifier("PSComponentSessionFactory")
    private SessionFactory psComponentSessionFactory;*/

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("Adding FICO PS Audit Interceptor");

        final EventListenerRegistry coreRegistry = ((SessionFactoryImpl) CoreSessionFactory).getServiceRegistry()
                .getService(EventListenerRegistry.class);

        coreRegistry.getEventListenerGroup(EventType.POST_INSERT).appendListener(entityListener);
        coreRegistry.getEventListenerGroup(EventType.POST_UPDATE).appendListener(entityListener);
        coreRegistry.getEventListenerGroup(EventType.POST_DELETE).appendListener(entityListener);
        coreRegistry.getEventListenerGroup(EventType.PRE_INSERT).prependListeners(entityListener);
        coreRegistry.getEventListenerGroup(EventType.PRE_UPDATE).prependListeners(entityListener);

        /*final EventListenerRegistry psComponentRegistry = ((SessionFactoryImpl) CoreSessionFactory).getServiceRegistry()
                .getService(EventListenerRegistry.class);

        psComponentRegistry.getEventListenerGroup(EventType.POST_INSERT).appendListener(entityListener);
        psComponentRegistry.getEventListenerGroup(EventType.POST_UPDATE).appendListener(entityListener);
        psComponentRegistry.getEventListenerGroup(EventType.POST_DELETE).appendListener(entityListener);
        psComponentRegistry.getEventListenerGroup(EventType.PRE_INSERT).prependListeners(entityListener);
        psComponentRegistry.getEventListenerGroup(EventType.PRE_UPDATE).prependListeners(entityListener);*/
    }

    @Bean(value = "coreJDBCTemplate")
    public JdbcTemplate coreJDBCTemplate() {
    	return new JdbcTemplate(this.coreWMdataSource);
    }

    @Bean
    public ObjectMapper buildObjectMapperWithTimezone(){
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        //mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        //mapper.enable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));
        mapper.setTimeZone(TimeZone.getTimeZone("UTC"));
        mapper.registerModule(jsonMapperJava8DateTimeModule());
        return mapper;
    }

    @Bean
    public Module jsonMapperJava8DateTimeModule() {
        SimpleModule bean = new SimpleModule();

        bean.addDeserializer (ZonedDateTime.class, new JsonDeserializer<ZonedDateTime>() {
            @Override
            public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
                return ZonedDateTime.parse(jsonParser.getValueAsString(), DateTimeFormatter.ISO_ZONED_DATE_TIME);
            }
        });

        bean.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
                return LocalDateTime.parse(jsonParser.getValueAsString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            }
        });

        bean.addSerializer(ZonedDateTime.class, new JsonSerializer<ZonedDateTime>() {
            @Override
            public void serialize(
                    ZonedDateTime zonedDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                    throws IOException {
                jsonGenerator.writeString(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(zonedDateTime));
            }
        });

        bean.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(
                    LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                    throws IOException {
                jsonGenerator.writeString(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(localDateTime));
            }
        });

        return bean;
    }


    @Bean
    public Module dynamoDemoEntityDeserializer() {
        logger.warn("Adding FICO PS ZonedDateTime handler");
        final SimpleModule module = new SimpleModule();
        module.addDeserializer(ZonedDateTime.class, new CustomDateSerializer());
        wmAppObjectMapper.registerReaderModule(module);
        return module;
    }

    @Bean
    public ModelMapper modelMapper() {
        logger.warn("Adding FICO PS Model Mapper");

        final  Converter<LocalDateTime, ZonedDateTime> LocaltoZoned = new AbstractConverter<LocalDateTime, ZonedDateTime>() {
            protected ZonedDateTime convert(LocalDateTime source) {
                if (source == null) {
                    return null;
                }
                final ZoneId zoneId = ZoneId.of("UTC");  //Zone information
                return source.atZone(ZoneOffset.UTC).withNano(0);
            }
        };

        Converter<ZonedDateTime, LocalDateTime> ZonedtoLocal = new AbstractConverter<ZonedDateTime, LocalDateTime>() {
            protected LocalDateTime convert(ZonedDateTime source) {
                if (source == null) {
                    return null;
                }
                return LocalDateTime.ofInstant(source.toInstant(), ZoneOffset.UTC).withNano(0);

            }
        };
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD)
                .setSourceNameTokenizer(NameTokenizers.UNDERSCORE)
                .setDestinationNameTokenizer(NameTokenizers.UNDERSCORE);
        modelMapper.addConverter(LocaltoZoned);
        modelMapper.addConverter(ZonedtoLocal);

        return modelMapper;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        //Map<String, String> errors = new HashMap<>();
        // ex.getBindingResult().getAllErrors().forEach((error) -> {
        //     String fieldName = ((FieldError) error).getField();
        //      String errorMessage = error.getDefaultMessage();
        //     errors.put(fieldName, errorMessage);
        //  });
        String test = "Errror with validation";

        return test;
    }
}