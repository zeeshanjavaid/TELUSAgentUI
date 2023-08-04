-- MIGRATION SCRIPT
-- Database Type: DBType{type='mysql', urlScheme='jdbc:', dialect='org.hibernate.dialect.MySQLDialect', driverClass='org.mariadb.jdbc.Driver'}
-- App Name: TELUSAgentUI
-- Database: TELUSAgentUIDB
-- Created At: 04-Aug-2023 06:17:22.UTC
-- Script Version: 14.0
-- -----------------------------------------------------------------
ALTER TABLE `ENTITY_DOCUMENTS` MODIFY COLUMN `documentName` VARCHAR(100) NULL;
