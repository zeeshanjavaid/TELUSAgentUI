package com.fico.ps.events;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fico.dmp.core.*;
import com.fico.dmp.telusagentuidb.AccessLog;
import com.fico.dmp.telusagentuidb.Audit;
import com.fico.dmp.telusagentuidb.AuditDataChange;
import com.fico.dmp.telusagentuidb.AuditSchema;
import com.fico.dmp.telusagentuidb.UserSession;
import com.fico.ps.TraceIdHolder;
import com.wavemaker.runtime.security.SecurityService;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.SessionFactory;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

@Component
public class EntityListener implements PreUpdateEventListener, PreInsertEventListener, PostUpdateEventListener, PostInsertEventListener, PostDeleteEventListener, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(EntityListener.class);

    private static ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);

    @Autowired
    private Environment environment;

    @Autowired
    private SecurityService securityService;

    @Autowired
    @Qualifier("TELUSAgentUIDBSessionFactory")
    private SessionFactory psComponentSessionFactory;

    @Value("${app.environment.ps.audit.enable}")
    private Boolean isEnabled;

    private static final ModelMapper modelMapper = new ModelMapper();
    {
        //to avoid circular reference
        modelMapper.getConfiguration()
                .setPreferNestedProperties(false)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setDeepCopyEnabled(false);

        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //this.isEnabled = environment.getProperty("app.environment.ps.audit.enable", Boolean.class, false);
        logger.warn("### Auditing enabled: {}", isEnabled);
        logger.warn("### Active Profiles: {}", String.join(",", environment.getActiveProfiles()));
        logger.warn("### Default Profiles: {}", String.join(",", environment.getDefaultProfiles()));
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }

    @Transactional(value = "TELUSAgentUIDBTransactionManager", propagation= Propagation.REQUIRES_NEW)
    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        performAudit(1, event.getEntity());
    }

    @Transactional(value = "TELUSAgentUIDBTransactionManager", propagation= Propagation.REQUIRES_NEW)
    @Override
    public void onPostDelete(PostDeleteEvent event) {
        performAudit(2, event.getEntity());
    }

    @Transactional(value = "TELUSAgentUIDBTransactionManager", propagation= Propagation.REQUIRES_NEW)
    @Override
    public void onPostInsert(PostInsertEvent event) {
        performAudit(0, event.getEntity());
    }

    private void performAudit(Integer RevisionType, Object entity) {
        if (isEnabled && !isNotAuditableEntity(entity)) {
            //Audit to Single table
            final String xTraceId = TraceIdHolder.getCurrentTraceId().getId();
            try {
                Audit audit = new Audit();
                //audit.setId(UUID.randomUUID().toString());
                audit.setClassName(entity.getClass().getName());
                audit.setRevisionType(RevisionType);
                audit.setJson(objectMapper.writeValueAsString(entity));
                audit.setTraceId(xTraceId);
                audit.setCreatedOn(Timestamp.from(Instant.now()));
                audit.setUsername(securityService.getUserId());
                psComponentSessionFactory.getCurrentSession().save(audit);
            } catch (Exception ex) {
                logger.error("Failed to save Audit {} - {}", xTraceId, ex);
            }
        }
    }

    public Boolean doesObjectContainField(Object object, String fieldName) {
        return Arrays.stream(object.getClass().getFields())
                .anyMatch(f -> f.getName().equals(fieldName));
    }

    Field getEntityRevisionInfoField(Class cls) {
        final Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            //System.out.println("field " + field);
            if (field.getName().equalsIgnoreCase("REV")) {
                return field;
            }
        }
        return null;
    }

    boolean isNotAuditableEntity(Object entity) {
        return (entity == null
                || entity instanceof Audit
                || entity instanceof UserSession
                || entity instanceof AccessLog
                || entity instanceof AuditDataChange
                || entity instanceof AuditSchema);
    }

    @Override
    public boolean onPreInsert(PreInsertEvent event) {

        if(!isNotAuditableEntity(event.getEntity())) {

            String[] propertyNames = event.getPersister().getEntityMetamodel().getPropertyNames();
            Object[] state = event.getState();

            // Integer userId =getUserId();
            Timestamp timestamp = Timestamp.from(Instant.now());

            //logger.warn("Injecting insert audit information for entity {}", event.getEntity());

            //setValue(state, propertyNames, "up", currentUser, entity);
            setField(event.getEntity(), "createdOn", timestamp);
            setValue(event.getState(), propertyNames, "createdOn", timestamp, event.getEntity());

            setField(event.getEntity(), "updatedOn", timestamp);
            setValue(event.getState(), propertyNames, "updatedOn", timestamp, event.getEntity());

            setField(event.getEntity(), "version", 0);
            setValue(event.getState(), propertyNames, "version", 0, event.getEntity());


            //  setField(event.getEntity(),"createdBy",userId);
            //  setValue(event.getState(), propertyNames, "createdBy", userId, event.getEntity());

            //   setField(event.getEntity(),"updatedBy",userId);
            //   setValue(event.getState(), propertyNames, "updatedBy", userId, event.getEntity());
        }

        return false;
    }

    @Override
    public boolean onPreUpdate(PreUpdateEvent event) {
        // TODO Auto-generated method stub

        if(!isNotAuditableEntity(event.getEntity())) {

            String[] propertyNames = event.getPersister().getEntityMetamodel().getPropertyNames();
            Object[] state = event.getState();


            // Integer userId =getUserId();
            Timestamp timestamp = Timestamp.from(Instant.now());

            //logger.warn("Injecting update audit information for entity {}", event.getEntity());

            setField(event.getEntity(), "updatedOn", timestamp);
            setValue(event.getState(), propertyNames, "updatedOn", timestamp, event.getEntity());


            //   setField(event.getEntity(),"updatedBy",userId);
            //    setValue(event.getState(), propertyNames, "updatedBy", userId, event.getEntity());

        }
        return false;
    }


    private void setField(Object object, String fieldName, Object value) {

        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Boolean hasMethod = Arrays.stream(object.getClass().getMethods())
                .anyMatch(f -> f.getName().equals(methodName));

        if (!hasMethod) {
            return;
        }

        final Class<?> objectClass = object.getClass();
        try {
            Method setMethod = objectClass.getDeclaredMethod(methodName, value.getClass());
            setMethod.invoke(object, value);
        } catch (Exception ex) {
        }
    }


    void setValue(Object[] currentState, String[] propertyNames, String propertyToSet, Object value, Object entity) {
        int index = ArrayUtils.indexOf(propertyNames, propertyToSet);
        if (index >= 0) {
            currentState[index] = value;
            //previousState[index] = value;
        }
    }
}
