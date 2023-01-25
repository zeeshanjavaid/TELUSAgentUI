-- MIGRATION SCRIPT
-- Database Type: DBType{type='mysql', urlScheme='jdbc:', dialect='org.hibernate.dialect.MySQLDialect', driverClass='org.mariadb.jdbc.Driver'}
-- App Name: TELUSAgentUI
-- Database: TELUSAgentUIDB
-- Created At: 25-Jan-2023 10:52:33.UTC
-- Script Version: 3.0
-- -----------------------------------------------------------------
DROP TABLE IF EXISTS `TABLE1`;
CREATE TABLE `USER_ROLE` (`ID` INT NOT NULL AUTO_INCREMENT,
`createdOn` TIMESTAMP(3) NULL,
`updatedOn` TIMESTAMP(3) NULL,
`createdBy` INT NULL,
`updatedBy` INT NULL,
`userId` INT NULL,
`roleId` INT NULL,
PRIMARY KEY(`ID`));
ALTER TABLE `USER_ROLE` ADD CONSTRAINT `FK_USER_ROLE_TO_USER_useUDeiZ` FOREIGN KEY (`userId`) REFERENCES `USER`(`id`);
ALTER TABLE `USER_ROLE` ADD CONSTRAINT `FK_ROLE_TO_USER_ROLE_id_OGLV5` FOREIGN KEY (`roleId`) REFERENCES `ROLE`(`id`);
