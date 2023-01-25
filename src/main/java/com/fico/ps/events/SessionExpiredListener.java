package com.fico.ps.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Component;

import java.nio.file.attribute.UserPrincipal;

@Component
public class SessionExpiredListener implements ApplicationListener<SessionDestroyedEvent> {
	
	private static final Logger logger = LoggerFactory.getLogger(SessionExpiredListener.class);

    @Override
    public void onApplicationEvent(SessionDestroyedEvent event) {
        for (SecurityContext securityContext : event.getSecurityContexts()) {
            Authentication authentication = securityContext.getAuthentication();
            UserPrincipal user = (UserPrincipal) authentication.getPrincipal(); // UserPrincipal is my custom Principal class
            logger.error("Session expired!" + user.getName());
            // do custom event handling
        }
    }
}
