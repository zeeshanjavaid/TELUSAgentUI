package com.fico.ps.security.oidc;

import com.fico.dmp.core.security.servlet.DMPOAuthLogoutServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value="/removeSession", name="LogoutServlet")
public class LogoutServlet extends DMPOAuthLogoutServlet {

    private static final Logger LOGGER  =  LogManager.getLogger(LogoutServlet.class);

    @Override
    public synchronized void init(ServletConfig srvConfig) throws ServletException {
        LOGGER.info("LogoutServlet INIT");
        super.init(srvConfig);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOGGER.info("LogoutServlet doPost");
        super.doPost(request, response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOGGER.info("LogoutServlet doGet");
        super.doGet(request, response);
    }
}