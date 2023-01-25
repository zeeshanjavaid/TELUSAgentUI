/**
 * Copyright © 2019 - 2020 Fair Isaac Corporation.
 *
 * All rights reserved. FICO, the FICO Logo and the FICO product and
 * service names referenced herein are trademarks or registered trademarks
 * of Fair Isaac Corporation in the United States and in other countries.
 * Other product and company names herein may be trademarks of their respective owners.
 */
package com.fico.fawb;

import com.wavemaker.runtime.property.FawbPropertySource;
import com.wavemaker.runtime.property.config.DatabaseSourceConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public final class DbBasedPropertySource implements FawbPropertySource {
    private DatabaseSourceConfiguration databaseSourceConfiguration;
    private final Properties properties = new Properties();
    private Connection connection = null;
    private String GET_PROPERTY_SOURCE_QUERY = "SELECT PROPERTY_KEY, PROPERTY_VALUE FROM FAWB_PROPERTY_SOURCE";
    private final String KEY = "PROPERTY_KEY";
    private final String VALUE = "PROPERTY_VALUE";

    private static final Logger LOGGER = LoggerFactory.getLogger(DbBasedPropertySource.class);

    public void init() {
        connection = databaseSourceConfiguration.getConnection();
        if (connection != null){
            loadProperties();
        }
    }

    private void loadProperties() {
        Statement statement = null;
        ResultSet resultSet;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(GET_PROPERTY_SOURCE_QUERY);
            while (resultSet.next()) {
                if (resultSet.getString(KEY) != null && resultSet.getString(VALUE) != null)
                    properties.put(resultSet.getString(KEY), resultSet.getString(VALUE));
            }
        } catch (SQLException e) {
            LOGGER.error("Unable to populate from DB based property source", e);
        } finally {
            if (connection != null) {
                try {
                    if(statement != null) {
                        statement.close();
                    }
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.error("Unable to close the db connection", e);
                }
            }
        }
    }

    @Override
    public Properties getProperties() {
        return properties;
    }

    public DatabaseSourceConfiguration getDatabaseSourceConfiguration() {
        return databaseSourceConfiguration;
    }

    public void setDatabaseSourceConfiguration(DatabaseSourceConfiguration databaseSourceConfiguration) {
        this.databaseSourceConfiguration = databaseSourceConfiguration;
    }
}
