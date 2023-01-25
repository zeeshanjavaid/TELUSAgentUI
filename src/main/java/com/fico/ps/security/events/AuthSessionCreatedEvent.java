package com.fico.ps.security.events;

import com.wavemaker.runtime.data.model.FawbApplicationSessionContext;

@SuppressWarnings("serial")
public class AuthSessionCreatedEvent extends AbstractAuthSessionEvent {

    /**
     * Create a new {@link SessionCreatedEvent}.
     *
     * @param source  the source of the event
     * @param session the session that was created
     */
    public AuthSessionCreatedEvent(Object source, FawbApplicationSessionContext authSession, String id) {
        super(source, authSession, id);
    }
}