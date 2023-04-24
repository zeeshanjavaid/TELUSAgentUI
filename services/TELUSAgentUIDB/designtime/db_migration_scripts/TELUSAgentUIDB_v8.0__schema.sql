-- MIGRATION SCRIPT
-- Database Type: DBType{type='mysql', urlScheme='jdbc:', dialect='org.hibernate.dialect.MySQLDialect', driverClass='org.mariadb.jdbc.Driver'}
-- App Name: TELUSAgentUI
-- Database: TELUSAgentUIDB
-- Created At: 21-Apr-2023 10:39:56.UTC
-- Script Version: 8.0
-- -----------------------------------------------------------------
CREATE TABLE `ENTITY_NOTES` (`ID` INT NOT NULL AUTO_INCREMENT,
`entityId` VARCHAR(100) NULL,
`banId` INT NULL,
`notes` VARCHAR(255) NULL,
`docId` INT NULL,
`createdOn` TIMESTAMP(3) NULL,
`createdBy` INT NULL,
PRIMARY KEY(`ID`));
CREATE TABLE `ENTITY_DOCUMENTS` (`ID` INT NOT NULL AUTO_INCREMENT,
`document` LONGBLOB NULL,
`createdOn` TIMESTAMP(3) NULL,
`createdBy` INT NULL,
PRIMARY KEY(`ID`));
