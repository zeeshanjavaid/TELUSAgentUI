package com.fico.ps;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TraceIdHolder {

    private static final Logger logger = LoggerFactory.getLogger(TraceIdHolder.class);

    private static ThreadLocal<TraceId> threadLocal = new ThreadLocal<TraceId>();

    public static TraceId getCurrentTraceId(){
        TraceId revInfo = threadLocal.get();
        if(revInfo == null){
            revInfo = new TraceId();
            threadLocal.set(revInfo);
        }
        return revInfo;
    }

    public static void clean(){
        try {
            TraceId revInfo = threadLocal.get();
            if(revInfo != null){
                threadLocal.remove();
            }
        }
        catch (Throwable t){
            logger.error("Not cleaned the ThreadLocal for Revinfo: {}", t.getMessage());
        }
    }
}