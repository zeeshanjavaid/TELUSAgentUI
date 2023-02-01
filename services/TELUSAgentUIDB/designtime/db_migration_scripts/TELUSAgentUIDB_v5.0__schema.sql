-- MIGRATION SCRIPT
-- Database Type: DBType{type='mysql', urlScheme='jdbc:', dialect='org.hibernate.dialect.MySQLDialect', driverClass='org.mariadb.jdbc.Driver'}
-- App Name: TELUSAgentUI
-- Database: TELUSAgentUIDB
-- Created At: 01-Feb-2023 09:48:02.UTC
-- Script Version: 5.0
-- -----------------------------------------------------------------
ALTER TABLE `USER` ADD COLUMN `emplId` VARCHAR(16) NULL;
