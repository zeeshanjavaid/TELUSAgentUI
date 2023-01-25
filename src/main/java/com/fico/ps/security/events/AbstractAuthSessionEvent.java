/*
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fico.ps.security.events;

import com.wavemaker.runtime.data.model.FawbApplicationSessionContext;
import org.springframework.context.ApplicationEvent;

/**
 * For {@link SessionRepository} implementations that support it, this event is fired when
 * a {@link Session} is updated.
 *
 * @author Rob Winch
 * @since 1.1
 */
@SuppressWarnings("serial")
public abstract class AbstractAuthSessionEvent extends ApplicationEvent {

    private final String authSessionId;

    private final FawbApplicationSessionContext authSession;

    AbstractAuthSessionEvent(Object source, FawbApplicationSessionContext authSession, String id) {
        super(source);
        this.authSession = authSession;
        this.authSessionId = id;
    }

    /**
     * Gets the {@link Session} that was destroyed. For some {@link SessionRepository}
     * implementations it may not be possible to get the original session in which case
     * this may be null.
     *
     * @param <S> the type of Session
     * @return the expired {@link Session} or null if the data store does not support
     * obtaining it
     */
    @SuppressWarnings("unchecked")
    public <S extends FawbApplicationSessionContext> S getAuthSession() {
        return (S) this.authSession;
    }

    public String getAuthSessionId() {
        return this.authSessionId;
    }
}