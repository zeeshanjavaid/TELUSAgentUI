package com.fico.ps.events;

import org.springframework.context.ApplicationEvent;

public class AccessApplicationEvent extends ApplicationEvent {

    /**
     *
     */
    private static final long serialVersionUID = -8429721501879231354L;

    public AccessApplicationEvent(AccessEvent source) {
        super(source);
    }

    public AccessEvent getAccessApplicationEvent() {
        return (AccessEvent) this.source;
    }
}