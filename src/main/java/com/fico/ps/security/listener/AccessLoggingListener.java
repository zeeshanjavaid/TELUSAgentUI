package com.fico.ps.security.listener;

import com.fico.dmp.telusagentuidb.AccessLog;
import com.fico.dmp.telusagentuidb.UserSession;
import com.fico.ps.events.AccessApplicationEvent;
import com.fico.ps.events.AccessEvent;
import com.fico.ps.security.events.AuthSessionCreatedEvent;
import com.fico.ps.security.events.AuthSessionEvictedEvent;
import com.fico.ps.security.events.AuthSessionExpiredEvent;
import com.wavemaker.runtime.data.dao.WMGenericDao;
import com.wavemaker.runtime.data.model.FawbApplicationSessionContext;
import com.wavemaker.runtime.security.SecurityService;
import com.wavemaker.runtime.security.WMUserDetails;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.concurrent.Executor;

@Component
public class AccessLoggingListener implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(AccessLoggingListener.class);

    @Autowired
    private Environment environment;

    @Autowired
    private SecurityService securityService;

    @Autowired
    @Qualifier("TELUSAgentUIDBTemplate")
    private HibernateTemplate template;

    @Autowired
    @Qualifier("TELUSAgentUIDBSessionFactory")
    private SessionFactory sessionFactory;

    @Autowired
    @Qualifier("TELUSAgentUIDB.AccessLogDao")
    private WMGenericDao<AccessLog, Integer> wmGenericDao;

    @Autowired
    @Qualifier("TELUSAgentUIDB.UserSessionDao")
    private WMGenericDao<UserSession, Integer> userSessionDao;

    @Value("${app.environment.ps.sessiontracking.enable}")
    private Boolean isTrackingEnabled;

    @Bean(name = "accessLoggingTaskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(10);
        executor.initialize();
        return executor;
    }

    @Async("accessLoggingTaskExecutor")
    @Transactional(value = "TELUSAgentUIDBTransactionManager", propagation = Propagation.REQUIRES_NEW)
    @EventListener
    public void onTicketUpdatedEvent(AccessApplicationEvent accessApplicationEvent) {
        //logger.info("Received AccessApplicationEvent {}", accessApplicationEvent);
        if(isTrackingEnabled) {
            AccessEvent accessEvent = accessApplicationEvent.getAccessApplicationEvent();
            try {
                //logger.warn("Persisting event. ID:{}", accessEvent.getId());
                AccessLog accessLog = new AccessLog();
                // accessLog.setId(accessEvent.getId());
                accessLog.setIpAddress(accessEvent.getIp());
                accessLog.setMethod(accessEvent.getMethod());
                accessLog.setUsername(accessEvent.getUserId());
                accessLog.setResultCode(accessEvent.getResultCode().shortValue());
                accessLog.setPath(accessEvent.getPath());
                accessLog.setTraceId(accessEvent.getTraceId());
                accessLog.setCreatedOn(Timestamp.valueOf(accessEvent.getTimestamp()));
                AccessLog accessLogCreated = this.wmGenericDao.create(accessLog);
            } catch (Exception ex) {
                logger.error("Persisting event failed. ID:{}", accessEvent.getId());
            }
        }
    }

    @Async("accessLoggingTaskExecutor")
    @Transactional(value = "TELUSAgentUIDBTransactionManager", propagation = Propagation.REQUIRES_NEW)
    @EventListener
    public void onLogin(AuthSessionCreatedEvent authSessionCreatedEvent) {
        //logger.info("new Login authSessionCreatedEvent {}", authSessionCreatedEvent);
        if(isTrackingEnabled) {
            try {
                FawbApplicationSessionContext fawbApplicationSessionContext = authSessionCreatedEvent.getAuthSession();
                final String userId = ((WMUserDetails) fawbApplicationSessionContext.getSecurityContext().getAuthentication().getPrincipal()).getUserId();

                final UserSession userSession = new UserSession();
                userSession.setAuthCookie(authSessionCreatedEvent.getAuthSessionId());
                userSession.setStartedOn(Timestamp.from(Instant.now()));
                userSession.setUserName(userId);

                final UserSession userSessionCreated = userSessionDao.create(userSession);

                //logger.warn("Auth Session started event. UserId {} ", userId);
            } catch (Exception ex) {
                logger.warn(ex.getMessage());
            }
        }
    }

    @Async("accessLoggingTaskExecutor")
    @Transactional(value = "TELUSAgentUIDBTransactionManager", propagation = Propagation.REQUIRES_NEW)
    @EventListener
    public void onLogin(AuthSessionExpiredEvent authSessionExpiredEvent) {
        final FawbApplicationSessionContext fawbApplicationSessionContext = authSessionExpiredEvent.getAuthSession();
        final String userId = ((WMUserDetails) fawbApplicationSessionContext.getSecurityContext().getAuthentication().getPrincipal()).getUserId();
        final String sessionId = authSessionExpiredEvent.getAuthSessionId();
//        try{
//
//        }
//        catch (Throwable e){
//
//        }
        logger.warn("Auth Session expired event. UserId {} ", userId);
    }

    @Async("accessLoggingTaskExecutor")
    @Transactional(value = "TELUSAgentUIDBTransactionManager", propagation = Propagation.REQUIRES_NEW)
    @EventListener
    public void onLogin(AuthSessionEvictedEvent authSessionEvictedEvent) {
        final FawbApplicationSessionContext fawbApplicationSessionContext = authSessionEvictedEvent.getAuthSession();
        final String userId = ((WMUserDetails) fawbApplicationSessionContext.getSecurityContext().getAuthentication().getPrincipal()).getUserId();
        final String sessionId = authSessionEvictedEvent.getAuthSessionId();
//        try{
//
//        }
//        catch (Throwable e){
//
//        }
        logger.warn("Auth Session evicted event. UserId {} ", userId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //this.isTrackingEnabled = environment.getProperty("app.environment.ps.sessiontracking.enable", Boolean.class, true);
        logger.warn("Is User Session tracking enabled? {}", isTrackingEnabled);
    }
}
