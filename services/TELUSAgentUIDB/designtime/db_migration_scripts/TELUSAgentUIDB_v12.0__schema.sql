-- MIGRATION SCRIPT
-- Database Type: DBType{type='mysql', urlScheme='jdbc:', dialect='org.hibernate.dialect.MySQLDialect', driverClass='org.mariadb.jdbc.Driver'}
-- App Name: TELUSAgentUI
-- Database: TELUSAgentUIDB
-- Created At: 04-Aug-2023 06:06:45.UTC
-- Script Version: 12.0
-- -----------------------------------------------------------------
ALTER TABLE `ENTITY_DOCUMENTS` DROP COLUMN `createdByEmplId`;
