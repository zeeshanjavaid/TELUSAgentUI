package com.fico.ps.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionCreationEvent;
import org.springframework.stereotype.Component;

@Component
public class SessionCreateListener implements ApplicationListener<SessionCreationEvent>, InitializingBean {
	
	private static final Logger logger = LoggerFactory.getLogger(SessionCreateListener.class);

    @Override
    public void onApplicationEvent(SessionCreationEvent event) {
    	//logger.warn("Session started!" );
    }

	@Override
	public void afterPropertiesSet() throws Exception {
		//logger.warn("Starting event watcher!" );
	}
}
