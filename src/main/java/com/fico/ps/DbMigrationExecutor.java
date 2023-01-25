package com.fico.ps;

import com.fico.fawb.dmp.integration.configuration.DmpIntegrationException;
import com.fico.fawb.dmp.integration.utils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.Flyway;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Profile({"wm_preview"})
public class DbMigrationExecutor implements InitializingBean {

    private static final String FAWB_DATASOURCE_BEAN_SUFFIX = "WMManagedDataSource";

    private static final Logger LOGGER = LoggerFactory.getLogger(DbMigrationExecutor.class);

    @Autowired
    private Scheduler schedulerFactoryBean;

    @Autowired
    private Environment environment;

    @Value("${app.environment.ps.preview.flyway.enable}")
    private Boolean isEnabled;

    @Value("${app.environment.ps.preview.flyway.cleardb}")
    private Boolean isCleanEnabled;

    private final Map<String, String> migrationLocations = new HashMap<>();

//    public DbMigrationExecutor() {
//        final String projectName = "ProjectShell_v3_chris"; //TODO: get from some env var or property
//        migrationLocations.put("Core", "filesystem:/root/WaveMaker//WaveMaker-Studio/workspace/default/projects/"+projectName+"/services/Core/designtime/db_migration_scripts/");
//        migrationLocations.put("PSComponent", "filesystem:/root/WaveMaker//WaveMaker-Studio/workspace/default/projects/"+projectName+"/services/PSComponent/designtime/db_migration_scripts/");
//        migrationLocations.put("PSComponentTimer", "filesystem:/root/WaveMaker//WaveMaker-Studio/workspace/default/projects/"+projectName+"/services/PSComponentTimer/designtime/db_migration_scripts/");
//    }

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        final ApplicationContext applicationContext = event.getApplicationContext();
        try {
            LOGGER.warn("Going to run migration");
            runMigration(applicationContext);
        } catch (DmpIntegrationException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void runMigration(ApplicationContext applicationContext) throws DmpIntegrationException {
        final List<String> datasourceNames = getDataSourcesNames();
        LOGGER.warn("Fetched the datasources to run migration on. Datasources = " + datasourceNames);
        for (String dataSoruceName : datasourceNames) {
            migrateDb(dataSoruceName, getDataSourceByBeanName(applicationContext, dataSoruceName));
        }
    }

    private DataSource getDataSourceByBeanName(ApplicationContext applicationContext, String dataSoruceName) throws DmpIntegrationException {
        final DataSource dataSource = (DataSource) applicationContext.getBean(dataSoruceName + "WMManagedDataSource");
        if (dataSource == null) {
            throw new DmpIntegrationException("Datasource Bean doesnot exist : " + dataSoruceName);
        }
        return dataSource;
    }

    private void migrateDb(String dataSourceName, DataSource dataSource) {
        //final Boolean isEnabled = environment.getProperty("app.environment.ps.preview.flyway.enable", Boolean.class, false);
        //final Boolean isCleanEnabled = environment.getProperty("app.environment.ps.preview.flyway.cleardb", Boolean.class, false);

        if (!isEnabled) {
            LOGGER.warn("Flyway preview migration is disabled.");
            return;
        }

        try {
            final Flyway flyway = new Flyway();
            final String location = this.migrationLocations.get(dataSourceName);
            LOGGER.info("Location of migrations: " + location);
            flyway.setLocations(location);
            flyway.setDataSource(dataSource);
            flyway.setSqlMigrationPrefix(dataSourceName + "_v");
            flyway.setBaselineOnMigrate(true);

            //TODO: it seems that there´s some concurrency between quartz and flyway that blocks the preview when the DB is cleanedUp
            if (isCleanEnabled) {
                //Even with shutdown there´s issues with the Quartz DB instance
                if("PSComponentTimer".equalsIgnoreCase(dataSourceName)) {
                    //shutdownQuartz();
                    LOGGER.warn("Flyway clearing/cleaning requested for Datasource: {} - but ignored, please execute manually", dataSourceName);
                }
                else{
                    flyway.clean();
                    LOGGER.warn("Flyway clearing/cleaning complete. Datasource: {}", dataSourceName);
                }
            }

            flyway.repair();
            LOGGER.warn("Flyway Repair complete. Datasource: {}", dataSourceName);

            flyway.migrate();
            LOGGER.warn("Flyway Migrate complete. Datasource: {}", dataSourceName);

            flyway.validate();
            LOGGER.warn("Flyway Validation complete. Datasource: {}", dataSourceName);
            LOGGER.warn("Completed migration for Database service {} ", dataSourceName);
        } catch (Exception ex) {
            LOGGER.error("Failed migration for Database service {} ", dataSourceName, ex);
        }
    }

    private void shutdownQuartz() {
        try {
            this.schedulerFactoryBean.shutdown(false);
        }
        catch (Throwable throwable){
            LOGGER.warn("Exception while shutingdown quartz ", throwable);
        }
    }

    private List<String> getDataSourcesNames() {
        PropertyUtils propertyUtils = new PropertyUtils();
        return propertyUtils.getAppConfiguredDbNames("dataSourceType");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String projectName = environment.getProperty("microServiceName"); //got from user-web.xml
        if(StringUtils.isEmpty(projectName)){
            projectName = "ProjectShell_v3"; //TODO: get from somewhere else
        }
        migrationLocations.put("TELUSAgentUIDB", "filesystem:/root/WaveMaker//WaveMaker-Studio/workspace/default/projects/"+projectName+"/services/TELUSAgentUIDB/designtime/db_migration_scripts/");
        /*migrationLocations.put("PSComponent", "filesystem:/root/WaveMaker//WaveMaker-Studio/workspace/default/projects/"+projectName+"/services/PSComponent/designtime/db_migration_scripts/");
        migrationLocations.put("PSComponentTimer", "filesystem:/root/WaveMaker//WaveMaker-Studio/workspace/default/projects/"+projectName+"/services/PSComponentTimer/designtime/db_migration_scripts/");*/

        LOGGER.warn("************************ PROJECT NAME: {}", projectName);

        final String[] keys ={
                "app.environment.ps.preview.flyway.enable",
                "app.environment.ps.preview.flyway.cleardb",
                "app.environment.ps.sessiontracking.enable",
                "app.environment.ps.audit.enable",
                "app.environment.ps.accesslog.enable",
                "app.environment.ps.tracerheader.enable",
                "microServiceName"
        };

        LOGGER.warn("************************************ START DEBUG FLAGS *****");
        for(String k : keys){
            LOGGER.warn("--> {} : {}", k, environment.getProperty(k));
        }
        LOGGER.warn("************************************ END DEBUG FLAGS *******");
    }
}
