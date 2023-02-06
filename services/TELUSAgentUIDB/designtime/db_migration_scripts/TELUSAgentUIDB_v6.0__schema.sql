-- MIGRATION SCRIPT
-- Database Type: DBType{type='mysql', urlScheme='jdbc:', dialect='org.hibernate.dialect.MySQLDialect', driverClass='org.mariadb.jdbc.Driver'}
-- App Name: TELUSAgentUI
-- Database: TELUSAgentUIDB
-- Created At: 06-Feb-2023 06:22:50.UTC
-- Script Version: 6.0
-- -----------------------------------------------------------------
CREATE TABLE `WORKCATEGORY_USER` (`ID` INT NOT NULL AUTO_INCREMENT,
`userId` INT NOT NULL,
`work_category` VARCHAR(64) NOT NULL,
PRIMARY KEY(`ID`));
ALTER TABLE `WORKCATEGORY_USER` ADD CONSTRAINT `FK_WORKCATEGORY_USER_TO_srqq6` FOREIGN KEY (`userId`) REFERENCES `USER`(`id`);
