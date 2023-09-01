-- MIGRATION SCRIPT
-- Database Type: DBType{type='mysql', urlScheme='jdbc:', dialect='org.hibernate.dialect.MySQLDialect', driverClass='org.mariadb.jdbc.Driver'}
-- App Name: TELUSAgentUI
-- Database: TELUSAgentUIDB
-- Created At: 01-Sep-2023 07:11:17.UTC
-- Script Version: 15.0
-- -----------------------------------------------------------------
CREATE TABLE `TEAM_MANAGER` (`ID` INT NOT NULL AUTO_INCREMENT,
`teamId` INT NULL,
`managerUserId` INT NULL,
`createdOn` TIMESTAMP(3) NULL,
`createdBy` INT NULL,
`updatedOn` TIMESTAMP(3) NULL,
`updatedBy` INT NULL,
PRIMARY KEY(`ID`));
ALTER TABLE `TEAM_MANAGER` ADD CONSTRAINT `FK_TEAM_MANAGER_TO_TEAM_sHqNK` FOREIGN KEY (`teamId`) REFERENCES `TEAM`(`ID`);
