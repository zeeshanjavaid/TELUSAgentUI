package com.fico.ps.security.events;

import com.wavemaker.runtime.data.model.FawbApplicationSessionContext;

@SuppressWarnings("serial")
public class AuthSessionExpiredEvent extends AbstractAuthSessionEvent {

    /**
     * Create a new {@link SessionCreatedEvent}.
     *
     * @param source  the source of the event
     * @param session the session that was created
     */
    public AuthSessionExpiredEvent(Object source, FawbApplicationSessionContext authSession, String id) {
        super(source, authSession, id);
    }
}