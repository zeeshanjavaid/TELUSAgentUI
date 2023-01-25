/**
 * Copyright © 2019 - 2020 Fair Isaac Corporation.
 *
 * All rights reserved. FICO, the FICO Logo and the FICO product and
 * service names referenced herein are trademarks or registered trademarks
 * of Fair Isaac Corporation in the United States and in other countries.
 * Other product and company names herein may be trademarks of their respective owners.
 */
package com.fico.fawb.dmp.integration.configuration;
import com.fico.dmp.context.DMPContext;
import com.fico.dmp.core.DMPException;
import com.fico.dmp.manager.model.ServiceInstance;
import com.fico.fawb.dmp.integration.utils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.util.*;
import java.util.function.Consumer;

@Configuration
public class AppSpringBeanInitializerCustom {

    private static final String USER_KEY = "dbUserName";
    private static final String PASS_KEY = "dbPassword";
    private static final String URL_KEY = "jdbcUrl";
    private static final Logger LOGGER = LoggerFactory.getLogger(AppSpringBeanInitializerCustom.class);
    private static final String defaultReadTimeout = "90000";
    private static final String socketTimeoutPropSuffix = ".flywayReadTimeout";

    /**
     * Get the DB configuration
     *
     * @param databaseServiceName
     * @return
     */
    private JdbcConfig getJDBCConfigFromDMP(String databaseServiceName) {
        try {
            JdbcConfig conf = new JdbcConfig();

            Map<?, ?> dmpConf = getDbServiceConfig(databaseServiceName);
            if (dmpConf != null) {
                conf.username = getStringEntry(dmpConf, USER_KEY);
                conf.password = getStringEntry(dmpConf, PASS_KEY);
                conf.url = getStringEntry(dmpConf, URL_KEY);
                LOGGER.debug("Loading JDBC config {} ", conf.toString());
                return conf;
            } else
                LOGGER.warn("No DmpConfig found for DBService name: {} , this may result in fail to access db service .", databaseServiceName);
            return null;
        } catch (Exception e) {
            LOGGER.error("Failed to fetch Database Configuration properties - {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * Get DB properties from DMP
     *
     * @return
     */
    @Order(-15)
    @Bean(name = "dBPropertiesCustom")
    @Profile("dmp")
    public Properties getDBProperties() {
        List<String> dataSourceNames = getDataSourceName();
        Properties properties = new Properties();

        dataSourceNames.forEach(new Consumer<String>() {

            @Override
            public void accept(String databaseServiceName) {
                JdbcConfig conf = getJDBCConfigFromDMP(databaseServiceName);

                if (conf != null) {

                    String url = includeAuroraWithUrl(conf.url);
                    url = appendReadTimeoutWithUrl(url, databaseServiceName);
                    LOGGER.info("database url: {}", url);
                    properties.setProperty(databaseServiceName + ".url", url);
                    properties.setProperty(databaseServiceName + ".username", conf.username);
                    properties.setProperty(databaseServiceName + ".password", conf.password);
                }
            }
        });

        return properties;
    }

    /**
     * To add Read Timeout with the url
     *
     * @param url
     * @param databaseName
     * @return
     */
    private String appendReadTimeoutWithUrl(String url, String databaseName) {

        StringBuffer sb = new StringBuffer();
        sb.append(url);
        if (!url.endsWith("?")) {
            sb.append("&");
        }
        sb.append("socketTimeout=").append(getSocketTimeout(databaseName));
        return sb.toString();
    }

    /**
     * Checking if transactionTimeout exists in db properties, if not we use default value of 40s
     *
     * @param databaseName
     * @return
     */
    private String getSocketTimeout(String databaseName) {
        String flywayReadTimeout = getTimeoutFromPropertyFile(databaseName);
        if (isNumeric(flywayReadTimeout)) {
            int timeout = Integer.parseInt(flywayReadTimeout);
            //if timeout value is less than 1000, we consider it in seconds and convert it to milliseconds
            if(timeout < 1000){
                return flywayReadTimeout + "000";
            }
            return flywayReadTimeout;
        }
        return defaultReadTimeout;
    }

    private boolean isNumeric(String str) {
        return str != null && str.matches("[0-9.]+");
    }

    /**
     * To fetch the transaction timeout value from property file
     *
     * @param databaseName
     * @return
     */
    private String getTimeoutFromPropertyFile(String databaseName) {
        String propFilePath = databaseName + ".properties";

        PropertyUtils propertyUtils =new PropertyUtils();
        String timeoutValue = String.valueOf(propertyUtils.readPropertyFromClassPathResource(propFilePath, databaseName+socketTimeoutPropSuffix));

        LOGGER.info("Value of timeout in file :" + timeoutValue);

        return timeoutValue;
    }


    /**
     * To add aurora: to the jdbc url
     *
     * @param url
     * @return
     */
    private String includeAuroraWithUrl(String url) {
        LOGGER.debug("dmp database service url: {}", url);
        if (url.startsWith("jdbc:mysql://") || url.startsWith("jdbc:mariadb://")) {
            int hostIndex = url.indexOf("//");
            StringBuffer sb = new StringBuffer();
            sb.append(url.substring(0, hostIndex));
            sb.append("aurora:");
            sb.append(url.substring(hostIndex));
            return sb.toString();
        }
        return url;
    }

    /**
     * String Util to get the key value
     *
     * @param m
     * @param key
     * @return
     */
    private String getStringEntry(Map<?, ?> m, String key) {
        Object value = m.get(key);
        return String.valueOf(value);
    }

    /**
     * Get the Db service configuration
     *
     * @param databaseServiceName
     * @return
     */
    private Map<?, ?> getDbServiceConfig(String databaseServiceName) {
        Collection<ServiceInstance> serviceInstances = null;
        ServiceInstance serviceInstance = null;
        int retryCount = 0;

        while (true) {

            if (retryCount >= 3) {
                LOGGER.warn("Retry count : {}", retryCount);
                break;
            } else {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    LOGGER.error("Error startup {}", e.getMessage(), e);
                }
            }
            try {
                serviceInstances = DMPContext.getContext().getComponentContext().listServices();
                break;
            } catch (DMPException e) {
                retryCount++;
                LOGGER.error("Failed to get service instances: {}", e.getMessage(), e);
            }
        }
        if (serviceInstances != null) {
            Iterator<ServiceInstance> iteratorObj = serviceInstances.iterator();
            while (iteratorObj.hasNext()) {
                ServiceInstance nServiceInstance = iteratorObj.next();
                if (nServiceInstance.getServiceId().equals(databaseServiceName)) {
                    serviceInstance = nServiceInstance;
                    break;
                }
            }
        }

        if (serviceInstance == null) {
            LOGGER.debug(" No DB Services Found for: {}", databaseServiceName);
            return null;
        } else {
            Map<?, ?> config = serviceInstance.getOptions();
            if (config == null) {
                LOGGER.debug(" No DB Services Found for: {}", databaseServiceName);
                return null;
            }
            return config;
        }
    }

    /**
     * get the FAWB  configured database source names
     *
     * @return
     */
    private List<String> getDataSourceName() {
        PropertyUtils propertyUtils = new PropertyUtils();
        List<String> configuredDataSources = propertyUtils.getAppConfiguredDbNames("dataSourceType");
        if (configuredDataSources != null && configuredDataSources.size() > 0) {
            return configuredDataSources;
        }
        return new ArrayList<>();
    }

    private static class JdbcConfig {
        private String url, username, password;
    }

    @Order(-15)
    @Bean(name = "dBPropertiesCustom")
    public Properties getDBPropertiesForDesign() {
        Properties properties = new Properties();
        return properties;
    }

}
