package com.fico.ps.events;

import com.fico.ps.TraceIdHolder;
import com.wavemaker.runtime.security.SecurityService;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component("loggingFilter")
public class AccessLoggingFilter implements Filter, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(AccessLoggingFilter.class);

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private SecurityService securityService;

    @Value("${app.environment.ps.accesslog.enable}")
    private Boolean isEnabled;

    @Value("${app.environment.ps.tracerheader.enable}")
    private Boolean tracerHeaderEnabled;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        final HttpServletResponse res = (HttpServletResponse) response;
        final HttpServletRequest req = (HttpServletRequest) request;

        TraceIdHolder.clean();
        final String xTraceId = TraceIdHolder.getCurrentTraceId().getId();

        if(this.tracerHeaderEnabled) {
            res.addHeader("X-Trace-Id", xTraceId);
        }

        final String userId = securityService.getUserId();
        if (userId == null) {
            logger.info("Access event skipped as user id is null");
        }
        else {
            if (this.isEnabled && shouldLogRequest(req.getMethod(), req.getRequestURI())) {
                final AccessEvent accessEvent = new AccessEvent();
                accessEvent.setTraceId(xTraceId);
                accessEvent.setIp(req.getRemoteAddr());
                accessEvent.setMethod(req.getMethod());
                accessEvent.setResultCode(res.getStatus());
                accessEvent.setUserId(userId);
                accessEvent.setPath(req.getRequestURI());
                accessEvent.setTimestamp(LocalDateTime.now());
                applicationEventPublisher.publishEvent(new AccessApplicationEvent(accessEvent));
            }
        }
        chain.doFilter(request, response);
    }

    private boolean shouldLogRequest(String httpMethod, String requestURI) {
        if((requestURI == null || httpMethod == null)){
            return false;
        }
        else if(httpMethod.equalsIgnoreCase("GET")
                || requestURI.endsWith(".js")
                || requestURI.endsWith("security/info")
                || requestURI.endsWith("services/servicedefs")
                || requestURI.endsWith("UserRole/filter")
                || requestURI.endsWith("queries/test_run")){
            return false;
        }
        return true;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.warn("Init filter bean.");
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.warn("### Access log enabled: {}", isEnabled);
        logger.warn("### Tracer header enabled: {}", tracerHeaderEnabled);
    }
}