-- MIGRATION SCRIPT
-- Database Type: DBType{type='mysql', urlScheme='jdbc:', dialect='org.hibernate.dialect.MySQLDialect', driverClass='org.mariadb.jdbc.Driver'}
-- App Name: TELUSAgentUI
-- Database: TELUSAgentUIDB
-- Created At: 23-Jan-2023 05:20:02.UTC
-- Script Version: 2.0
-- -----------------------------------------------------------------
-- MySQL dump 10.19  Distrib 10.2.41-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: TELUSAgentUIDB
-- ------------------------------------------------------
-- Server version	10.2.41-MariaDB
--DROP TABLE IF EXISTS `ACCESS_LOG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `ACCESS_LOG` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ipAddress` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `method` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `path` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `resultCode` smallint(6) DEFAULT NULL,
  `createdOn` timestamp(3) NULL DEFAULT NULL,
  `traceId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `username` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `INDEX_ACCESS_LOG_createdOn` (`createdOn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `APPLICATION`
--

--DROP TABLE IF EXISTS `APPLICATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `APPLICATION` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `applicationNumber` varchar(50) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6xs5ccyjremyiobp4n7bxqd4x` (`applicationNumber`),
  KEY `IDX6xs5ccyjremyiobp4n7bxqd4x` (`applicationNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `USER`
--

--DROP TABLE IF EXISTS `USER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `USER` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `userId` varchar(255) DEFAULT NULL,
  `active` bit(1) NOT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `createdOn` timestamp(3) NULL DEFAULT NULL,
  `updatedOn` timestamp(3) NULL DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(20) DEFAULT NULL,
  `timeZone` varchar(255) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `createdBy` int(11) DEFAULT NULL,
  `updatedBy` int(11) DEFAULT NULL,
  `lendingLimit` double(10,2) DEFAULT NULL,
  `acf2ID` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_oso07pudw19e66bs4yp8hwpux` (`email`),
  UNIQUE KEY `UK_exa4yyy95hmwcp5tan8dgw9uv` (`userId`),
  KEY `FK6cupwxorr5rb6bx06ppvim5n0` (`createdBy`),
  KEY `FKi8inswykfga8w2neygvcmh6nh` (`updatedBy`),
  CONSTRAINT `FK6cupwxorr5rb6bx06ppvim5n0` FOREIGN KEY (`createdBy`) REFERENCES `USER` (`id`),
  CONSTRAINT `FKi8inswykfga8w2neygvcmh6nh` FOREIGN KEY (`updatedBy`) REFERENCES `USER` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `DomainValueType`
--

--DROP TABLE IF EXISTS `DomainValueType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `DomainValueType` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Code` varchar(50) DEFAULT NULL,
  `Description` varchar(100) DEFAULT NULL,
  `CreatedBy` int(11) DEFAULT NULL,
  `UpdatedBy` int(11) DEFAULT NULL,
  `CreatedOn` timestamp(3) NULL DEFAULT NULL,
  `UpdatedOn` timestamp(3) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_DomainValueType_TO_USfrgRW` (`CreatedBy`),
  KEY `FK_DomainValueType_TO_USuJLBd` (`UpdatedBy`),
  CONSTRAINT `FK_DomainValueType_TO_USfrgRW` FOREIGN KEY (`CreatedBy`) REFERENCES `USER` (`id`),
  CONSTRAINT `FK_DomainValueType_TO_USuJLBd` FOREIGN KEY (`UpdatedBy`) REFERENCES `USER` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DomainValue`
--

--DROP TABLE IF EXISTS `DomainValue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `DomainValue` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Code` varchar(50) DEFAULT NULL,
  `DomainValueType` int(11) DEFAULT NULL,
  `IsActive` bit(1) DEFAULT NULL,
  `IsDefault` bit(1) DEFAULT NULL,
  `RankOrder` int(11) DEFAULT NULL,
  `CreatedBy` int(11) DEFAULT NULL,
  `CreatedOn` timestamp(3) NULL DEFAULT NULL,
  `UpdatedBy` int(11) DEFAULT NULL,
  `UpdatedOn` timestamp(3) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_DomainValue_TO_DomainpAvGp` (`DomainValueType`),
  KEY `FK_DomainValue_TO_USER_Cy1BDb` (`CreatedBy`),
  KEY `FK_DomainValue_TO_USER_UyHoBW` (`UpdatedBy`),
  CONSTRAINT `FK_DomainValue_TO_DomainpAvGp` FOREIGN KEY (`DomainValueType`) REFERENCES `DomainValueType` (`ID`),
  CONSTRAINT `FK_DomainValue_TO_USER_Cy1BDb` FOREIGN KEY (`CreatedBy`) REFERENCES `USER` (`id`),
  CONSTRAINT `FK_DomainValue_TO_USER_UyHoBW` FOREIGN KEY (`UpdatedBy`) REFERENCES `USER` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ACTIVITY`
--

--DROP TABLE IF EXISTS `ACTIVITY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `ACTIVITY` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `source` int(11) DEFAULT NULL,
  `applicationId` int(11) DEFAULT NULL,
  `applicationStatus` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `startTime` timestamp(3) NULL DEFAULT NULL,
  `endTime` timestamp(3) NULL DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `isError` bit(1) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `INDEX_ACTIVITY_applicationId` (`applicationId`),
  KEY `INDEX_ACTIVITY_applicatiFZrX7` (`applicationStatus`),
  KEY `INDEX_ACTIVITY_description` (`description`),
  KEY `INDEX_ACTIVITY_name` (`name`),
  KEY `INDEX_ACTIVITY_type` (`type`),
  KEY `FK_ACTIVITY_TO_DomainVal6OQPf` (`source`),
  CONSTRAINT `FK_ACTIVITY_TO_APPLICATIGU2uG` FOREIGN KEY (`applicationId`) REFERENCES `APPLICATION` (`id`),
  CONSTRAINT `FK_ACTIVITY_TO_DomainVal6OQPf` FOREIGN KEY (`source`) REFERENCES `DomainValue` (`ID`),
  CONSTRAINT `FK_ACTIVITY_TO_DomainValayBFN` FOREIGN KEY (`applicationStatus`) REFERENCES `DomainValue` (`ID`),
  CONSTRAINT `FK_ACTIVITY_TO_DomainValqeU7B` FOREIGN KEY (`type`) REFERENCES `DomainValue` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;




--
-- Table structure for table `ACTIVITY_PAYLOAD`
--

--DROP TABLE IF EXISTS `ACTIVITY_PAYLOAD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `ACTIVITY_PAYLOAD` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `activityId` int(11) DEFAULT NULL,
  `requestId` varchar(255) DEFAULT NULL,
  `responseId` varchar(255) DEFAULT NULL,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `dataPayload` longblob DEFAULT NULL,
  `isRequestPayload` bit(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_ACTIVITY_PAYLOAD_TO_AG9Ygt` (`activityId`),
  CONSTRAINT `FK_ACTIVITY_PAYLOAD_TO_AG9Ygt` FOREIGN KEY (`activityId`) REFERENCES `ACTIVITY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `AUDIT`
--

--DROP TABLE IF EXISTS `AUDIT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `AUDIT` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `className` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `json` longtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `createdOn` timestamp(3) NULL DEFAULT NULL,
  `username` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `revisionType` int(11) DEFAULT NULL,
  `traceId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `INDEX_AUDIT_createdOn` (`createdOn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AuditDataChange`
--

--DROP TABLE IF EXISTS `AuditDataChange`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `AuditDataChange` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `applicationId` int(11) DEFAULT NULL,
  `action` varchar(255) DEFAULT NULL,
  `domainClassName` varchar(255) DEFAULT NULL,
  `entityName` varchar(255) DEFAULT NULL,
  `entityId` int(11) DEFAULT NULL,
  `parentDomainClassName` varchar(255) DEFAULT NULL,
  `parentEntityName` varchar(255) DEFAULT NULL,
  `parentEntityId` int(11) DEFAULT NULL,
  `changeData` longtext DEFAULT NULL,
  `createdBy` int(11) DEFAULT NULL,
  `createdOn` timestamp(3) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_AuditDataChange_TO_AP6sJqn` (`applicationId`),
  KEY `FK_AuditDataChange_TO_UStqOQc` (`createdBy`),
  CONSTRAINT `FK_AuditDataChange_TO_AP6sJqn` FOREIGN KEY (`applicationId`) REFERENCES `APPLICATION` (`id`),
  CONSTRAINT `FK_AuditDataChange_TO_UStqOQc` FOREIGN KEY (`createdBy`) REFERENCES `USER` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AuditSchema`
--

--DROP TABLE IF EXISTS `AuditSchema`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `AuditSchema` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `createdOn` timestamp(3) NOT NULL DEFAULT current_timestamp(3) ON UPDATE current_timestamp(3),
  `createdBy` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `command` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `result` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PARTY`
--

--DROP TABLE IF EXISTS `PARTY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `PARTY` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdOn` timestamp(3) NULL DEFAULT NULL,
  `updatedOn` timestamp(3) NULL DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `ApplicationId` int(11) NOT NULL,
  `createdBy` int(11) DEFAULT NULL,
  `updatedBy` int(11) DEFAULT NULL,
  `applicationRole` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKco9t3f6g66opwm70sbn8mdr3r` (`ApplicationId`),
  KEY `FKl2i1vjbwn6mbfagrn8c1ox66x` (`createdBy`),
  KEY `FK3ge8qx045mlshses9gi4yslsl` (`updatedBy`),
  KEY `FK_PARTY_TO_DomainValue_jBf97` (`applicationRole`),
  CONSTRAINT `FK3ge8qx045mlshses9gi4yslsl` FOREIGN KEY (`updatedBy`) REFERENCES `USER` (`id`),
  CONSTRAINT `FK_PARTY_TO_DomainValue_jBf97` FOREIGN KEY (`applicationRole`) REFERENCES `DomainValue` (`ID`),
  CONSTRAINT `FKco9t3f6g66opwm70sbn8mdr3r` FOREIGN KEY (`ApplicationId`) REFERENCES `APPLICATION` (`id`),
  CONSTRAINT `FKl2i1vjbwn6mbfagrn8c1ox66x` FOREIGN KEY (`createdBy`) REFERENCES `USER` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DOCUMENT`
--

--DROP TABLE IF EXISTS `DOCUMENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `DOCUMENT` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `applicationId` int(11) DEFAULT NULL,
  `partyId` int(11) DEFAULT NULL,
  `updatedBy` int(11) DEFAULT NULL,
  `updatedOn` timestamp(3) NULL DEFAULT NULL,
  `createdBy` int(11) DEFAULT NULL,
  `createdOn` timestamp(3) NULL DEFAULT NULL,
  `documentLabel` int(11) DEFAULT NULL,
  `externalId` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_DOCUMENT_TO_APPLICATIGwesA` (`applicationId`),
  KEY `FK_DOCUMENT_TO_PARTY_par1bs57` (`partyId`),
  KEY `FK_DOCUMENT_TO_DomainValLmV6F` (`type`),
  KEY `FK_DOCUMENT_TO_DomainVal5lrkI` (`documentLabel`),
  KEY `FK_DOCUMENT_TO_USER_crealc0nH` (`createdBy`),
  KEY `FK_DOCUMENT_TO_USER_upda2H9vu` (`updatedBy`),
  CONSTRAINT `FK_DOCUMENT_TO_APPLICATIGwesA` FOREIGN KEY (`applicationId`) REFERENCES `APPLICATION` (`id`),
  CONSTRAINT `FK_DOCUMENT_TO_DomainVal5lrkI` FOREIGN KEY (`documentLabel`) REFERENCES `DomainValue` (`ID`),
  CONSTRAINT `FK_DOCUMENT_TO_DomainValLmV6F` FOREIGN KEY (`type`) REFERENCES `DomainValue` (`ID`),
  CONSTRAINT `FK_DOCUMENT_TO_PARTY_par1bs57` FOREIGN KEY (`partyId`) REFERENCES `PARTY` (`id`),
  CONSTRAINT `FK_DOCUMENT_TO_USER_crealc0nH` FOREIGN KEY (`createdBy`) REFERENCES `USER` (`id`),
  CONSTRAINT `FK_DOCUMENT_TO_USER_upda2H9vu` FOREIGN KEY (`updatedBy`) REFERENCES `USER` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `DomainValueDescription`
--

--DROP TABLE IF EXISTS `DomainValueDescription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `DomainValueDescription` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DomainValueId` int(11) DEFAULT NULL,
  `Locale` varchar(10) DEFAULT NULL,
  `Description` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_DomainValueDescriptioCNODF` (`DomainValueId`),
  CONSTRAINT `FK_DomainValueDescriptioCNODF` FOREIGN KEY (`DomainValueId`) REFERENCES `DomainValue` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DomainValueRelation`
--

--DROP TABLE IF EXISTS `DomainValueRelation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `DomainValueRelation` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DomainValueId` int(11) DEFAULT NULL,
  `ParentDomainValueId1` int(11) DEFAULT NULL,
  `ParentDomainValueId2` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_DomainValueRelation_ThHkUG` (`DomainValueId`),
  KEY `FK_DomainValueRelation_TIUJOq` (`ParentDomainValueId1`),
  KEY `FK_DomainValueRelation_TIKNtp` (`ParentDomainValueId2`),
  CONSTRAINT `FK_DomainValueRelation_TIKNtp` FOREIGN KEY (`ParentDomainValueId2`) REFERENCES `DomainValue` (`ID`),
  CONSTRAINT `FK_DomainValueRelation_TIUJOq` FOREIGN KEY (`ParentDomainValueId1`) REFERENCES `DomainValue` (`ID`),
  CONSTRAINT `FK_DomainValueRelation_ThHkUG` FOREIGN KEY (`DomainValueId`) REFERENCES `DomainValue` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `DomainValueTypeRelationship`
--

--DROP TABLE IF EXISTS `DomainValueTypeRelationship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `DomainValueTypeRelationship` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DomainValueTypeId` int(11) DEFAULT NULL,
  `ParentDomainValueTypeId1` int(11) DEFAULT NULL,
  `ParentDomainValueTypeId2` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_DomainValueTypeRelaticKovn` (`DomainValueTypeId`),
  KEY `FK_DomainValueTypeRelatixUieD` (`ParentDomainValueTypeId1`),
  KEY `FK_DomainValueTypeRelatiY1y2v` (`ParentDomainValueTypeId2`),
  CONSTRAINT `FK_DomainValueTypeRelatiY1y2v` FOREIGN KEY (`ParentDomainValueTypeId2`) REFERENCES `DomainValueType` (`ID`),
  CONSTRAINT `FK_DomainValueTypeRelaticKovn` FOREIGN KEY (`DomainValueTypeId`) REFERENCES `DomainValueType` (`ID`),
  CONSTRAINT `FK_DomainValueTypeRelatixUieD` FOREIGN KEY (`ParentDomainValueTypeId1`) REFERENCES `DomainValueType` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Error`
--

--DROP TABLE IF EXISTS `Error`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `Error` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Code` varchar(255) DEFAULT NULL,
  `description` longtext DEFAULT NULL,
  `applicationId` int(11) DEFAULT NULL,
  `errorDate` timestamp(3) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_Error_TO_APPLICATION_co9w9` (`applicationId`),
  CONSTRAINT `FK_Error_TO_APPLICATION_co9w9` FOREIGN KEY (`applicationId`) REFERENCES `APPLICATION` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FAWB_PROPERTY_SOURCE`
--

--DROP TABLE IF EXISTS `FAWB_PROPERTY_SOURCE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `FAWB_PROPERTY_SOURCE` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdOn` timestamp(3) NULL DEFAULT NULL,
  `updatedOn` timestamp(3) NULL DEFAULT NULL,
  `PROPERTY_KEY` varchar(255) DEFAULT NULL,
  `PROPERTY_VALUE` varchar(255) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `createdBy` int(11) DEFAULT NULL,
  `updatedBy` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_flmg3cn1l5ka2dlpdxhpqb13l` (`PROPERTY_KEY`),
  KEY `FKa3vbebyh5nytrol79hhqclyxn` (`createdBy`),
  KEY `FKrt1k8e756kwjocp22p2uremkh` (`updatedBy`),
  CONSTRAINT `FKa3vbebyh5nytrol79hhqclyxn` FOREIGN KEY (`createdBy`) REFERENCES `USER` (`id`),
  CONSTRAINT `FKrt1k8e756kwjocp22p2uremkh` FOREIGN KEY (`updatedBy`) REFERENCES `USER` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `GROUP`
--

--DROP TABLE IF EXISTS `GROUP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `GROUP` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `createdOn` timestamp(3) NULL DEFAULT NULL,
  `updatedOn` timestamp(3) NULL DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `createdBy` int(11) DEFAULT NULL,
  `updatedBy` int(11) DEFAULT NULL,
  `lendingLimit` double(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Name` (`name`),
  KEY `FKdjb3a2n2o3hii7n95iyo0l4mo` (`createdBy`),
  KEY `FKkrcc46r4ivf9abl25cimayvcr` (`updatedBy`),
  CONSTRAINT `FKdjb3a2n2o3hii7n95iyo0l4mo` FOREIGN KEY (`createdBy`) REFERENCES `USER` (`id`),
  CONSTRAINT `FKkrcc46r4ivf9abl25cimayvcr` FOREIGN KEY (`updatedBy`) REFERENCES `USER` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ROLE`
--

--DROP TABLE IF EXISTS `ROLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `ROLE` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `createdOn` timestamp(3) NULL DEFAULT NULL,
  `updatedOn` timestamp(3) NULL DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `createdBy` int(11) DEFAULT NULL,
  `updatedBy` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKi803kih7ryhvjuok84k8w9oqh` (`role`),
  KEY `FKnnewcklsu1l8bn0caamaki2qv` (`createdBy`),
  KEY `FKm4pj2umb9ji3spjnqf3krw2c8` (`updatedBy`),
  CONSTRAINT `FKm4pj2umb9ji3spjnqf3krw2c8` FOREIGN KEY (`updatedBy`) REFERENCES `USER` (`id`),
  CONSTRAINT `FKnnewcklsu1l8bn0caamaki2qv` FOREIGN KEY (`createdBy`) REFERENCES `USER` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `GROUP_ROLE`
--

--DROP TABLE IF EXISTS `GROUP_ROLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `GROUP_ROLE` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdOn` timestamp(3) NULL DEFAULT NULL,
  `updatedOn` timestamp(3) NULL DEFAULT NULL,
  `createdBy` int(11) DEFAULT NULL,
  `updatedBy` int(11) DEFAULT NULL,
  `groupId` int(11) DEFAULT NULL,
  `roleId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6jvvmvyi9rpj5sxb5pmh94e8e` (`groupId`,`roleId`),
  KEY `FKr0amyiuf17khi22crlb7gw46t` (`createdBy`),
  KEY `FKj45tlys3wnwdlrlmsdvn1vkrv` (`updatedBy`),
  KEY `FKtocovgflgmna18x31hy7m9qd4` (`roleId`),
  CONSTRAINT `FKj45tlys3wnwdlrlmsdvn1vkrv` FOREIGN KEY (`updatedBy`) REFERENCES `USER` (`id`),
  CONSTRAINT `FKl8yieyhorolxg20px1ydejkri` FOREIGN KEY (`groupId`) REFERENCES `GROUP` (`id`),
  CONSTRAINT `FKr0amyiuf17khi22crlb7gw46t` FOREIGN KEY (`createdBy`) REFERENCES `USER` (`id`),
  CONSTRAINT `FKtocovgflgmna18x31hy7m9qd4` FOREIGN KEY (`roleId`) REFERENCES `ROLE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=191 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `LOCALE`
--

--DROP TABLE IF EXISTS `LOCALE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `LOCALE` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(10) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `isDefault` bit(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `NOTE`
--

--DROP TABLE IF EXISTS `NOTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `NOTE` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `content` longtext DEFAULT NULL,
  `noteCategory` int(11) DEFAULT NULL,
  `createdBy` int(11) DEFAULT NULL,
  `createdOn` timestamp(3) NULL DEFAULT NULL,
  `updatedBy` int(11) DEFAULT NULL,
  `updatedOn` timestamp(3) NULL DEFAULT NULL,
  `applicationId` int(11) DEFAULT NULL,
  `partyId` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_NOTE_TO_DomainValue_nvLcfG` (`noteCategory`),
  KEY `FK_NOTE_TO_APPLICATION_aQ4lsP` (`applicationId`),
  KEY `FK_NOTE_TO_PARTY_partyId_id` (`partyId`),
  CONSTRAINT `FK_NOTE_TO_APPLICATION_aQ4lsP` FOREIGN KEY (`applicationId`) REFERENCES `APPLICATION` (`id`),
  CONSTRAINT `FK_NOTE_TO_DomainValue_nvLcfG` FOREIGN KEY (`noteCategory`) REFERENCES `DomainValue` (`ID`),
  CONSTRAINT `FK_NOTE_TO_PARTY_partyId_id` FOREIGN KEY (`partyId`) REFERENCES `PARTY` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `PERMISSION`
--

--DROP TABLE IF EXISTS `PERMISSION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `PERMISSION` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) DEFAULT NULL,
  `Description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=939 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_BLOB_TRIGGERS`
--

--DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `QRTZ_BLOB_TRIGGERS` (
  `SCHED_NAME` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `BLOB_DATA` longblob DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_CALENDARS`
--

--DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `QRTZ_CALENDARS` (
  `CALENDAR_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `SCHED_NAME` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `CALENDAR` longblob NOT NULL,
  PRIMARY KEY (`CALENDAR_NAME`,`SCHED_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_CRON_TRIGGERS`
--

--DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `QRTZ_CRON_TRIGGERS` (
  `SCHED_NAME` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `CRON_EXPRESSION` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `TIME_ZONE_ID` varchar(80) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_FIRED_TRIGGERS`
--

--DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `QRTZ_FIRED_TRIGGERS` (
  `ENTRY_ID` varchar(256) COLLATE utf8_unicode_ci NOT NULL,
  `SCHED_NAME` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `FIRED_TIME` bigint(20) NOT NULL,
  `INSTANCE_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `IS_NONCONCURRENT` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `JOB_GROUP` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `JOB_NAME` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PRIORITY` int(11) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SCHED_TIME` bigint(20) NOT NULL,
  `STATE` varchar(16) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`ENTRY_ID`,`SCHED_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_JOB_DETAILS`
--

--DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `QRTZ_JOB_DETAILS` (
  `JOB_GROUP` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `JOB_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `SCHED_NAME` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `DESCRIPTION` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `IS_DURABLE` varchar(1) COLLATE utf8_unicode_ci NOT NULL,
  `IS_NONCONCURRENT` varchar(1) COLLATE utf8_unicode_ci NOT NULL,
  `IS_UPDATE_DATA` varchar(1) COLLATE utf8_unicode_ci NOT NULL,
  `JOB_CLASS_NAME` varchar(250) COLLATE utf8_unicode_ci NOT NULL,
  `JOB_DATA` longblob DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`JOB_GROUP`,`JOB_NAME`,`SCHED_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_LOCKS`
--

--DROP TABLE IF EXISTS `QRTZ_LOCKS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `QRTZ_LOCKS` (
  `LOCK_NAME` varchar(40) COLLATE utf8_unicode_ci NOT NULL,
  `SCHED_NAME` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`LOCK_NAME`,`SCHED_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_PAUSED_TRIGGER_GRPS`
--

--DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `QRTZ_PAUSED_TRIGGER_GRPS` (
  `SCHED_NAME` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_SCHEDULER_STATE`
--

--DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `QRTZ_SCHEDULER_STATE` (
  `INSTANCE_NAME` varchar(256) COLLATE utf8_unicode_ci NOT NULL,
  `SCHED_NAME` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `CHECKIN_INTERVAL` bigint(20) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(20) NOT NULL,
  PRIMARY KEY (`INSTANCE_NAME`,`SCHED_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_SIMPLE_TRIGGERS`
--

--DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `QRTZ_SIMPLE_TRIGGERS` (
  `SCHED_NAME` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `REPEAT_COUNT` bigint(20) NOT NULL,
  `REPEAT_INTERVAL` bigint(20) NOT NULL,
  `TIMES_TRIGGERED` bigint(20) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_SIMPROP_TRIGGERS`
--

--DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `QRTZ_SIMPROP_TRIGGERS` (
  `SCHED_NAME` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `BOOL_PROP_1` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DEC_PROP_1` double DEFAULT NULL,
  `DEC_PROP_2` double DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `STR_PROP_1` varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL,
  `STR_PROP_2` varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL,
  `STR_PROP_3` varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_TRIGGERS`
--

--DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `QRTZ_TRIGGERS` (
  `SCHED_NAME` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `CALENDAR_NAME` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DESCRIPTION` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `END_TIME` bigint(20) DEFAULT NULL,
  `JOB_GROUP` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `JOB_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `MISFIRE_INSTR` smallint(6) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(20) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(20) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `START_TIME` bigint(20) NOT NULL,
  `TRIGGER_STATE` varchar(16) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_TYPE` varchar(8) COLLATE utf8_unicode_ci NOT NULL,
  `JOB_DATA` longblob DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_NAME`),
  KEY `QRTZ_TRIGGERS_ibfk_1` (`JOB_GROUP`,`JOB_NAME`,`SCHED_NAME`),
  CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`JOB_GROUP`, `JOB_NAME`, `SCHED_NAME`) REFERENCES `QRTZ_JOB_DETAILS` (`JOB_GROUP`, `JOB_NAME`, `SCHED_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QUEUE`
--

--DROP TABLE IF EXISTS `QUEUE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `QUEUE` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Category` int(11) DEFAULT NULL,
  `Rank` int(11) DEFAULT NULL,
  `Name` varchar(255) DEFAULT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `CreatedOn` timestamp(3) NULL DEFAULT NULL,
  `CreatedBy` int(11) DEFAULT NULL,
  `UpdatedBy` int(11) DEFAULT NULL,
  `JsonFilterString` longtext DEFAULT NULL,
  `SortField` longtext DEFAULT NULL,
  `SortOrder` varchar(255) DEFAULT NULL,
  `IsActive` bit(1) DEFAULT NULL,
  `FilterCriteriaQuery` longtext DEFAULT NULL,
  `IsLocked` bit(1) DEFAULT NULL,
  `LockedBy` int(11) DEFAULT NULL,
  `IsPersonalQueue` bit(1) DEFAULT NULL,
  `PersonalQueueField` int(11) DEFAULT NULL,
  `QueueResultPage` int(11) DEFAULT NULL,
  `UpdatedOn` timestamp(3) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_QUEUE_TO_USER_CreatedBy_id` (`CreatedBy`),
  KEY `FK_QUEUE_TO_USER_LockedBy_id` (`LockedBy`),
  KEY `FK_QUEUE_TO_DomainValue_vL7bw` (`QueueResultPage`),
  KEY `FK_QUEUE_TO_DomainValue_ZOr0f` (`PersonalQueueField`),
  KEY `FK_QUEUE_TO_USER_UpdatedBy_id` (`UpdatedBy`),
  CONSTRAINT `FK_QUEUE_TO_DomainValue_ZOr0f` FOREIGN KEY (`PersonalQueueField`) REFERENCES `DomainValue` (`ID`),
  CONSTRAINT `FK_QUEUE_TO_DomainValue_vL7bw` FOREIGN KEY (`QueueResultPage`) REFERENCES `DomainValue` (`ID`),
  CONSTRAINT `FK_QUEUE_TO_USER_CreatedBy_id` FOREIGN KEY (`CreatedBy`) REFERENCES `USER` (`id`),
  CONSTRAINT `FK_QUEUE_TO_USER_LockedBy_id` FOREIGN KEY (`LockedBy`) REFERENCES `USER` (`id`),
  CONSTRAINT `FK_QUEUE_TO_USER_UpdatedBy_id` FOREIGN KEY (`UpdatedBy`) REFERENCES `USER` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QUEUE_GROUP`
--

--DROP TABLE IF EXISTS `QUEUE_GROUP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `QUEUE_GROUP` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `QueueId` int(11) DEFAULT NULL,
  `GroupId` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_QUEUE_GROUP_TO_QUEUE_dbyLa` (`QueueId`),
  KEY `FK_QUEUE_GROUP_TO_GROUP_JjVRw` (`GroupId`),
  CONSTRAINT `FK_QUEUE_GROUP_TO_GROUP_JjVRw` FOREIGN KEY (`GroupId`) REFERENCES `GROUP` (`id`),
  CONSTRAINT `FK_QUEUE_GROUP_TO_QUEUE_dbyLa` FOREIGN KEY (`QueueId`) REFERENCES `QUEUE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `ROLE_PERMISSION`
--

--DROP TABLE IF EXISTS `ROLE_PERMISSION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `ROLE_PERMISSION` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `RoleId` int(11) DEFAULT NULL,
  `PermissionId` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_ROLE_PERMISSION_TO_ROH20mB` (`RoleId`),
  KEY `FK_ROLE_PERMISSION_TO_PEsLD72` (`PermissionId`),
  CONSTRAINT `FK_ROLE_PERMISSION_TO_PEsLD72` FOREIGN KEY (`PermissionId`) REFERENCES `PERMISSION` (`ID`),
  CONSTRAINT `FK_ROLE_PERMISSION_TO_ROH20mB` FOREIGN KEY (`RoleId`) REFERENCES `ROLE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=611 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `USER_GROUP`
--

--DROP TABLE IF EXISTS `USER_GROUP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `USER_GROUP` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdOn` timestamp(3) NULL DEFAULT NULL,
  `updatedOn` timestamp(3) NULL DEFAULT NULL,
  `createdBy` int(11) DEFAULT NULL,
  `updatedBy` int(11) DEFAULT NULL,
  `groupId` int(11) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK9oi8jgbv70sgfmq2odyw9ekj` (`userId`,`groupId`),
  KEY `FKf0m5kxg6yilupsilw35i8dmnt` (`createdBy`),
  KEY `FKqwtwube0wer96mu8cqmb7rur7` (`updatedBy`),
  KEY `FKesfn3dt9bkqcd4fl90c4c94wb` (`groupId`),
  CONSTRAINT `FKcv4isf8wxxg8dc6fnbo4t546x` FOREIGN KEY (`userId`) REFERENCES `USER` (`id`),
  CONSTRAINT `FKesfn3dt9bkqcd4fl90c4c94wb` FOREIGN KEY (`groupId`) REFERENCES `GROUP` (`id`),
  CONSTRAINT `FKf0m5kxg6yilupsilw35i8dmnt` FOREIGN KEY (`createdBy`) REFERENCES `USER` (`id`),
  CONSTRAINT `FKqwtwube0wer96mu8cqmb7rur7` FOREIGN KEY (`updatedBy`) REFERENCES `USER` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `USER_SESSION`
--

--DROP TABLE IF EXISTS `USER_SESSION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS  `USER_SESSION` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `authCookie` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `endType` smallint(6) DEFAULT NULL,
  `endedOn` timestamp(3) NULL DEFAULT NULL,
  `startedOn` timestamp(3) NULL DEFAULT NULL,
  `userName` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `INDEX_USER_SESSION_authCvEwFf` (`authCookie`,`userName`),
  KEY `INDEX_USER_SESSION_startedOn` (`startedOn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `schema_version`
--

--DROP TABLE IF EXISTS `schema_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
--CREATE TABLE IF NOT EXISTS  `schema_version` (
--  `version_rank` int(11) NOT NULL,
--  `installed_rank` int(11) NOT NULL,
--  `version` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
--  `description` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
--  `type` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
--  `script` varchar(1000) COLLATE utf8_unicode_ci NOT NULL,
--  `checksum` int(11) DEFAULT NULL,
--  `installed_by` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
--  `installed_on` timestamp NOT NULL DEFAULT current_timestamp(),
--  `execution_time` int(11) NOT NULL,
--  `success` tinyint(1) NOT NULL,
--  PRIMARY KEY (`version`),
--  KEY `schema_version_vr_idx` (`version_rank`),
--  KEY `schema_version_ir_idx` (`installed_rank`),
--  KEY `schema_version_s_idx` (`success`)
--) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


