-- MIGRATION SCRIPT
-- Database Type: DBType{type='mysql', urlScheme='jdbc:', dialect='org.hibernate.dialect.MySQLDialect', driverClass='org.mariadb.jdbc.Driver'}
-- App Name: TELUSAgentUI
-- Database: TELUSAgentUIDB
-- Created At: 25-Jan-2023 11:05:37.UTC
-- Script Version: 4.0
-- -----------------------------------------------------------------
CREATE TABLE `TEAM_USER` (`ID` INT NOT NULL AUTO_INCREMENT,
`teamId` INT NULL,
`userId` INT NULL,
`createdOn` TIMESTAMP(3) NULL,
`updatedOn` TIMESTAMP(3) NULL,
`createdBy` INT NULL,
`updatedBy` INT NULL,
PRIMARY KEY(`ID`));
CREATE TABLE `TEAM` (`ID` INT NOT NULL AUTO_INCREMENT,
`TeamID` VARCHAR(255) NOT NULL,
`TeamName` VARCHAR(255) NOT NULL,
`Description` VARCHAR(255) NULL,
`createdOn` TIMESTAMP(3) NULL,
`updatedOn` TIMESTAMP(3) NULL,
`createdBy` INT NULL,
`updatedBy` INT NULL,
PRIMARY KEY(`ID`));
ALTER TABLE `TEAM_USER` ADD CONSTRAINT `FK_TEAM_TO_TEAM_USER_ID_p9oeO` FOREIGN KEY (`teamId`) REFERENCES `TEAM`(`ID`);
ALTER TABLE `TEAM_USER` ADD CONSTRAINT `FK_USER_TO_TEAM_USER_id_QG7Mv` FOREIGN KEY (`userId`) REFERENCES `USER`(`id`);
