package com.fico.ps;

import java.util.UUID;

public final class TraceId {

    private String id;

    public TraceId() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "TraceId{" +
                "id='" + id + '\'' +
                '}';
    }
}
