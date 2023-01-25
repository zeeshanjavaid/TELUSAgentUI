package com.fico.ps.security;

import com.fico.pscomponent.handlers.RoleManagementHandler;
import com.wavemaker.runtime.dmp.integration.spring.runtime.decorator.DmpAuthDecorator;
import com.wavemaker.runtime.security.core.AppRolesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomRoleService implements AppRolesProvider {

    private static final Logger logger = LoggerFactory.getLogger(CustomRoleService.class);

    @Autowired
    private RoleManagementHandler roleManagementHandler;

    @Override
    public List<String> getCustomRoles(Object authDetails) {
        final List<String> customRoles = new ArrayList<>();
        if (authDetails instanceof DmpAuthDecorator) {
            try {
                final DmpAuthDecorator dmpAuthDecorator = (DmpAuthDecorator) authDetails;
                // user can get username, email and token from dmpAuthDecorator.
                // dmpAuthDecorator.getUserEmailId();
                // dmpAuthDecorator.getUserName();
                // dmpAuthDecorator.getDmpAccessToken();
                customRoles.addAll(roleManagementHandler.getUserRoleByUsername(dmpAuthDecorator.getUserId()));
                logger.info("# ----------------------------");
                logger.info("dmpAuthDecorator.getUserName() {}", dmpAuthDecorator.getUserName());
                logger.info("dmpAuthDecorator.getUserEmailId() {}", dmpAuthDecorator.getUserEmailId());
                logger.info("dmpAuthDecorator.getUserId() {}", dmpAuthDecorator.getUserId());
                final String userRoles = customRoles.stream().collect(Collectors.joining(","));;
                logger.info("# ROLES of {} : {}", dmpAuthDecorator.getUserEmailId(), userRoles);
                logger.info("# ----------------------------");
            } catch (Exception e) {
                logger.error("Error while adding custom role: " + e);
            }
        }
        return customRoles;
    }
}
