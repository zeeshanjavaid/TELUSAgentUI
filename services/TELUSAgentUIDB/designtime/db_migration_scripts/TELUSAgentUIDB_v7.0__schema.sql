-- MIGRATION SCRIPT
-- Database Type: DBType{type='mysql', urlScheme='jdbc:', dialect='org.hibernate.dialect.MySQLDialect', driverClass='org.mariadb.jdbc.Driver'}
-- App Name: TELUSAgentUI
-- Database: TELUSAgentUIDB
-- Created At: 01-Mar-2023 08:47:44.UTC
-- Script Version: 7.0
-- -----------------------------------------------------------------
ALTER TABLE `USER` ADD COLUMN `managerId` INT NULL;
