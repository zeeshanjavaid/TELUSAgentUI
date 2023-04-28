-- MIGRATION SCRIPT
-- Database Type: DBType{type='mysql', urlScheme='jdbc:', dialect='org.hibernate.dialect.MySQLDialect', driverClass='org.mariadb.jdbc.Driver'}
-- App Name: TELUSAgentUI
-- Database: TELUSAgentUIDB
-- Created At: 28-Apr-2023 13:06:09.UTC
-- Script Version: 10.0
-- -----------------------------------------------------------------
ALTER TABLE `ENTITY_NOTES` MODIFY COLUMN `notes` VARCHAR(256) NULL;
