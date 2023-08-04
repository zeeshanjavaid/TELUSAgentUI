-- MIGRATION SCRIPT
-- Database Type: DBType{type='mysql', urlScheme='jdbc:', dialect='org.hibernate.dialect.MySQLDialect', driverClass='org.mariadb.jdbc.Driver'}
-- App Name: TELUSAgentUI
-- Database: TELUSAgentUIDB
-- Created At: 04-Aug-2023 06:08:25.UTC
-- Script Version: 13.0
-- -----------------------------------------------------------------
ALTER TABLE `ENTITY_NOTES` ADD COLUMN `createdByEmplId` VARCHAR(100) NULL;
