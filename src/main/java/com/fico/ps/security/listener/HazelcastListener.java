package com.fico.ps.security.listener;

import com.fico.ps.security.events.AuthSessionCreatedEvent;
import com.fico.ps.security.events.AuthSessionEvictedEvent;
import com.fico.ps.security.events.AuthSessionExpiredEvent;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryEvictedListener;
import com.hazelcast.map.listener.EntryExpiredListener;
import com.wavemaker.runtime.data.model.FawbApplicationSessionContext;
import com.wavemaker.runtime.hazelcast.FawbAppHazelcastInstance;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class HazelcastListener implements InitializingBean, EntryAddedListener<String, FawbApplicationSessionContext>, EntryExpiredListener<String, FawbApplicationSessionContext>, EntryEvictedListener<String, FawbApplicationSessionContext> {

    @Autowired
    private FawbAppHazelcastInstance fawbAppHazelcastInstance;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public void afterPropertiesSet() throws Exception {
        fawbAppHazelcastInstance.getSessionContextMap().addEntryListener(this, true);
    }

    @Override
    public void entryAdded(EntryEvent<String, FawbApplicationSessionContext> event) {
        this.eventPublisher.publishEvent(new AuthSessionCreatedEvent(this, event.getValue(), event.getKey()));
    }

    @Override
    public void entryEvicted(EntryEvent<String, FawbApplicationSessionContext> event) {
        this.eventPublisher.publishEvent(new AuthSessionEvictedEvent(this, event.getOldValue(), event.getKey()));
    }

    @Override
    public void entryExpired(EntryEvent<String, FawbApplicationSessionContext> event) {
        this.eventPublisher.publishEvent(new AuthSessionExpiredEvent(this, event.getOldValue(), event.getKey()));
    }
}