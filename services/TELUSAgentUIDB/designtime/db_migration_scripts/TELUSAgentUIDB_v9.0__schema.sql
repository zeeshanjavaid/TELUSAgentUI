-- MIGRATION SCRIPT
-- Database Type: DBType{type='mysql', urlScheme='jdbc:', dialect='org.hibernate.dialect.MySQLDialect', driverClass='org.mariadb.jdbc.Driver'}
-- App Name: TELUSAgentUI
-- Database: TELUSAgentUIDB
-- Created At: 24-Apr-2023 14:23:04.UTC
-- Script Version: 9.0
-- -----------------------------------------------------------------
ALTER TABLE `ENTITY_DOCUMENTS` ADD COLUMN `documentName` VARCHAR(50) NULL;
