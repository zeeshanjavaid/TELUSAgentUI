/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.schemaservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fico.dmp.telusagentuidb.AuditSchema;
import com.fico.dmp.telusagentuidb.service.AuditSchemaService;
import com.fico.dmp.schemaservice.model.*;
import com.fico.pscomponent.util.SQLCommandUtil;
import com.wavemaker.runtime.security.SecurityService;
import com.wavemaker.runtime.service.annotations.ExposeToClient;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.fico.dmp.schemaservice.model.*;

/**
 * This is a singleton class with all its public methods exposed as REST APIs via generated controller class.
 * To avoid exposing an API for a particular public method, annotate it with @HideFromClient.
 *
 * Method names will play a major role in defining the Http Method for the generated APIs. For example, a method name
 * that starts with delete/remove, will make the API exposed as Http Method "DELETE".
 *
 * Method Parameters of type primitives (including java.lang.String) will be exposed as Query Parameters &
 * Complex Types/Objects will become part of the Request body in the generated API.
 */
@ExposeToClient
public class SchemaService {

    private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(SchemaService.class.getName());

    @Value("${TELUSAgentUIDB.url}")
    private String jdbcURL;

    @Autowired
    private SecurityService securityService;

    @Autowired
    @Qualifier("TELUSAgentUIDBWMManagedDataSource")
    private HikariDataSource coreWMManagedDataSource;

    @Autowired
    private Environment environment;

    @Autowired
    private ListableBeanFactory listableBeanFactory;

    @Autowired
    private AuditSchemaService auditSchemaService;

    private String schema = null;

    @PostConstruct
    public void setUp(){
        this.schema = getParsedSchema();
    }

    public List<QueryResult> getDownloadEndpoints(){
        final Map<String, Object> controllers= listableBeanFactory.getBeansWithAnnotation(RestController.class);
        final List<QueryResult> urls = new ArrayList<>();
        controllers.forEach((s, o) -> {
            QueryResult qs = new QueryResult();
            qs.setSql(s);
            qs.setResponse(o.getClass().getName());
            urls.add(qs);
        });
        return urls;
    }

    public List<DBStatusVO> getDBStatus(String db){
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource(db));
        return jdbcTemplate.query("SHOW TABLE STATUS LIKE '%'", new DBStatusMapper());
    }

    public List<DBVariableVO> getDBVariables(String db){
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource(db));
        return jdbcTemplate.query("show status", new DBVariableMapper());
    }

    public List<SchemaStatisticsVO> getStatistics(String db){
        try {
            final JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource(db));
            final String query = "SELECT table_schema, table_name, CONCAT(ROUND(table_rows / 1000000, 2), 'M') rowes, " +
                    "CONCAT(ROUND(data_length / ( 1024 * 1024 * 1024 ), 2), 'G') DAATA, " +
                    "CONCAT(ROUND(index_length / ( 1024 * 1024 * 1024 ), 2), 'G') idx, " +
                    "CONCAT(ROUND(( data_length + index_length ) / ( 1024 * 1024 * 1024 ), 2), 'G') total_size, " +
                    "ROUND(index_length / data_length, 2) idxfrac FROM information_schema.TABLES " +
                    "where table_schema in (?) ORDER  BY data_length, index_length DESC";
            return jdbcTemplate.query(query, ps -> {
                //String schema = "%";
//                try{
//                    schema = getParsedSchema();
//                }
//                catch (Throwable e){
//                    logger.error("Can`t get schema");
//                }
                ps.setString(1, schema);
            }, new SchemaStatisticsMapper());
        } catch (Throwable t){
        	if(logger.isErrorEnabled())
        		logger.error("_cant get the statistics", t);
            return Collections.emptyList();
        }
    }

    public List<SchemaVersionVO> getMigrations(String db){
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource(db));
        return jdbcTemplate.query("select * from schema_version order by version_rank", new SchemaVersionMapper());
    }

    public List<DBProcessVO> getProcessList(String db){
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource(db));
        return jdbcTemplate.query("show full processlist", new DBProcessMapper());
    }

    public void setSuccess(String db, Integer rankVersion){
//        logger.info("Setting success on {} for {} by {}", db, rankVersion, securityService.getUserId());
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource(db));
        final AuditSchema auditSchema = new AuditSchema();
        auditSchema.setCommand("setting rankVersion: " + rankVersion + " on " + db + " to success");
        auditSchema.setCreatedBy(securityService.getUserId());
        auditSchema.setCreatedOn(new Timestamp(System.currentTimeMillis()));
        auditSchemaService.create(auditSchema);
        jdbcTemplate.update("update schema_version set success = 1 where version_rank = ?", rankVersion);
    }

    public void setFailure(String db, Integer rankVersion){
//        logger.info("Setting failure on {} for {} by {}", db, rankVersion, securityService.getUserId());
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource(db));
        final AuditSchema auditSchema = new AuditSchema();
        auditSchema.setCommand("setting rankVersion: " + rankVersion + " on " + db + " to failure");
        auditSchema.setCreatedBy(securityService.getUserId());
        auditSchema.setCreatedOn(new Timestamp(System.currentTimeMillis()));
        auditSchemaService.create(auditSchema);
        jdbcTemplate.update("update schema_version set success = 0 where version_rank = ?", rankVersion);
    }

    public void deleteEntry(String db, Integer rankVersion){
//        logger.info("Deleting on {} for {} by {}", db, rankVersion, securityService.getUserId());
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource(db));
        final AuditSchema auditSchema = new AuditSchema();
        auditSchema.setCommand("deleting rankVersion: " + rankVersion + " on " + db);
        auditSchema.setCreatedBy(securityService.getUserId());
        auditSchema.setCreatedOn(new Timestamp(System.currentTimeMillis()));
        auditSchemaService.create(auditSchema);
        jdbcTemplate.update("delete from schema_version where version_rank = ? and success = 0", rankVersion);
    }

    public void cleanDB(String db){
//        logger.info("Executing cleanDB for " + db);
//        final Flyway flyway = new Flyway();
//        flyway.setDataSource(getDataSource(db));
//        flyway.clean();
    }

    public List<DBTableVO> listTables(String db){
        final String query = "select * from information_schema.tables " +
                "where table_type = 'BASE TABLE' and table_schema = ? order by table_name;";
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource(db));
//        final String schema = getParsedSchema();
        return jdbcTemplate.query(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, schema);
            }
        }, new DBTableMapper());
    }

    public List<DBIndexVO> getTableIndexes(String db, String tableName){
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource(db));
        final String query = "SHOW INDEX FROM " + schema + "." + tableName;
        return jdbcTemplate.query(query, new DBIndexMapper());
    }

    public DBTableDDL getDDL(String db, String tableName){
    	if(logger.isInfoEnabled())
    		logger.info("Getting DDL for {}", tableName);
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource(db));
        final String query = "show create table " + schema + "." + tableName;
        return jdbcTemplate.queryForObject(query, new DBTableDDLMapper());
    }

    public List<DBColumnVO> descTable(String db, String tableName){
    	if(logger.isInfoEnabled())
    		logger.info("Executing describe for {}", tableName);
        final String query = "SHOW COLUMNS FROM " + schema + "." + tableName;
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource(db));
        return jdbcTemplate.query(query, new DBColumnMapper());
    }

    public List<QueryResult> executeSQLs(String db, String dbCommands){
        final List<QueryResult> executions = new ArrayList<>();
        final SQLCommandUtil util = new SQLCommandUtil();
        final List<String> fixedCommands = util.getCommands(dbCommands);
        if(fixedCommands.size() == 1){
            final String command = fixedCommands.get(0);
            if(command.toLowerCase().startsWith("select") || command.toLowerCase().startsWith("describe")){
                final ObjectMapper objectMapper = new ObjectMapper();
                executeSelect(db, command).stream().forEach(stringObjectMap -> {
                    final QueryResult queryResult = new QueryResult();
                    try {
                        queryResult.setResponse(objectMapper.writeValueAsString(stringObjectMap));
                        executions.add(queryResult);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
                return executions;
            }
        }
        for (String sql : fixedCommands) {
            final QueryResult exec = new QueryResult();
            try {
                exec.setSql(sql);
                executeSQL(db, sql);
                exec.setResponse("success");
            } catch (RuntimeException e) {
                exec.setResponse(e.getMessage());
            }
            executions.add(exec);
        }
        return executions;
    }

    private List<Map<String, Object>> executeSelect(String db, String sql) {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource(db));
//        logger.info("executeSelect on " + db + " - " + sql + " by " + securityService.getUserId());
        final List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        try {
            final AuditSchema auditSchema = new AuditSchema();
            auditSchema.setCommand("executeSelect: " + sql + " on " + db);
            auditSchema.setCreatedBy(securityService.getUserId());
            auditSchema.setCreatedOn(new Timestamp(System.currentTimeMillis()));
            auditSchema.setResult("size: " + result.size());
            auditSchemaService.create(auditSchema);
        }
        catch (Throwable e){
        	if(logger.isErrorEnabled())
        		logger.error(e.getMessage(), e);
        }
        return result;
    }

    private void executeSQL(String db, String sql) {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource(db));
//        logger.info("executeSQL on " + db + " - " + sql + " by " + securityService.getUserId());
        final AuditSchema auditSchema = new AuditSchema();
        auditSchema.setCommand("SQL: " + sql + " on " + db);
        auditSchema.setCreatedBy(securityService.getUserId());
        auditSchema.setCreatedOn(new Timestamp(System.currentTimeMillis()));
        if(!isAuditTampering(sql)) {
            jdbcTemplate.execute(sql);
            auditSchema.setResult("Success");
        }
        else{
        	if(logger.isInfoEnabled())
        		logger.info("Attempted tampering of XYZSchema Audit, ignoring command");
            auditSchema.setResult("DENIED");
        }
        auditSchemaService.create(auditSchema);
    }

    private boolean isAuditTampering(String sql) {
        final String cleanSQL = StringUtils.trim(sql);
        return ((StringUtils.startsWithIgnoreCase(cleanSQL, "insert")
                || StringUtils.startsWithIgnoreCase(cleanSQL,  "delete")
                || StringUtils.startsWithIgnoreCase(cleanSQL, "update")
                || StringUtils.startsWithIgnoreCase(cleanSQL, "truncate"))
                && StringUtils.containsIgnoreCase(cleanSQL, "AuditSchema"));
    }

    public void repairDB(String db){
   /*    logger.info("Executing repairDB for " + db + " by " + securityService.getUserId());
        String projectName = environment.getProperty("microServiceName"); //got from user-web.xml
       logger.info("Project name {}", projectName);
        if(StringUtils.isEmpty(projectName)){
            projectName = "TD_MBNA_FAWB_PROJECT"; //TODO: get from somewhere else
        }
        Map<String, String> migrationLocations = new HashMap<>();
        migrationLocations.put("TELUSAgentUIDB", "filesystem:/root/WaveMaker//WaveMaker-Studio/workspace/default/projects/"+projectName+"/services/TELUSAgentUIDB/designtime/db_migration_scripts/");
        final String location = migrationLocations.get(db);

        final AuditSchema auditSchema = new AuditSchema();
        auditSchema.setCommand("executed repair on " + db);
        auditSchema.setCreatedBy(securityService.getUserId());
        auditSchema.setCreatedOn(new Timestamp(System.currentTimeMillis()));
        auditSchemaService.create(auditSchema);

        final Flyway flyway = new Flyway();
        flyway.setDataSource(getDataSource(db));
        flyway.setSqlMigrationPrefix(db + "_v");
        flyway.setLocations(location);
        flyway.repair();
        flyway.migrate();
        if(logger.isWarnEnabled())
        	logger.warn("UI Flyway Migrate complete on - " + db);

        flyway.validate();
        if(logger.isWarnEnabled())
        	logger.warn("UI Flyway Validation complete on - " + db);  */
    }

    public String info(){
        try{
            return getParsedSchema();
        }
        catch (Throwable t){
        	if(logger.isInfoEnabled())
        		logger.info("can`t get schema", t);
        }
        return this.schema;
    }

    String getParsedSchema(){
        if(this.schema == null) {
            final String regEx = "\\:3306\\/(.*)\\?";
            final Pattern simpleUrlPattern = Pattern.compile(regEx);
            final Matcher urlMatcher = simpleUrlPattern.matcher(this.jdbcURL);
            if(urlMatcher.find()) {
                this.schema = urlMatcher.group(1);
            }
            else{
                this.schema = "%";
            }
        }
        return this.schema;
    }

    private HikariDataSource getDataSource(String db){
        if("TELUSAgentUIDB".equalsIgnoreCase(db)){
            return coreWMManagedDataSource;
        }
        return null;
    }
}