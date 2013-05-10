-- MySQL dump 10.13  Distrib 5.1.56, for slackware-linux-gnu (x86_64)
--
-- Host: localhost    Database: brutal_development
-- ------------------------------------------------------
-- Server version	5.1.56

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Answer`
--

DROP TABLE IF EXISTS `Answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Answer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdAt` datetime DEFAULT NULL,
  `lastUpdatedAt` datetime DEFAULT NULL,
  `voteCount` bigint(20) NOT NULL,
  `author_id` bigint(20) DEFAULT NULL,
  `information_id` bigint(20) NOT NULL,
  `lastTouchedBy_id` bigint(20) DEFAULT NULL,
  `question_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK752F2BDE171098B9` (`lastTouchedBy_id`),
  KEY `FK752F2BDEE5155ACD` (`author_id`),
  KEY `FK752F2BDE23B043C9` (`information_id`),
  KEY `FK752F2BDED6CA1D2D` (`question_id`),
  CONSTRAINT `FK752F2BDED6CA1D2D` FOREIGN KEY (`question_id`) REFERENCES `Question` (`id`),
  CONSTRAINT `FK752F2BDE171098B9` FOREIGN KEY (`lastTouchedBy_id`) REFERENCES `Users` (`id`),
  CONSTRAINT `FK752F2BDE23B043C9` FOREIGN KEY (`information_id`) REFERENCES `AnswerInformation` (`id`),
  CONSTRAINT `FK752F2BDEE5155ACD` FOREIGN KEY (`author_id`) REFERENCES `Users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Answer`
--

LOCK TABLES `Answer` WRITE;
/*!40000 ALTER TABLE `Answer` DISABLE KEYS */;
/*!40000 ALTER TABLE `Answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AnswerInformation`
--

DROP TABLE IF EXISTS `AnswerInformation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AnswerInformation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment` longtext,
  `createdAt` datetime DEFAULT NULL,
  `description` longtext NOT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `markedDescription` longtext,
  `moderatedAt` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `answer_id` bigint(20) DEFAULT NULL,
  `author_id` bigint(20) NOT NULL,
  `moderatedBy_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK23057A6ED3D1EED` (`answer_id`),
  KEY `FK23057A6E7AC17960` (`moderatedBy_id`),
  KEY `FK23057A6EE5155ACD` (`author_id`),
  CONSTRAINT `FK23057A6EE5155ACD` FOREIGN KEY (`author_id`) REFERENCES `Users` (`id`),
  CONSTRAINT `FK23057A6E7AC17960` FOREIGN KEY (`moderatedBy_id`) REFERENCES `Users` (`id`),
  CONSTRAINT `FK23057A6ED3D1EED` FOREIGN KEY (`answer_id`) REFERENCES `Answer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AnswerInformation`
--

LOCK TABLES `AnswerInformation` WRITE;
/*!40000 ALTER TABLE `AnswerInformation` DISABLE KEYS */;
/*!40000 ALTER TABLE `AnswerInformation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Answer_Comments`
--

DROP TABLE IF EXISTS `Answer_Comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Answer_Comments` (
  `Answer_id` bigint(20) NOT NULL,
  `comments_id` bigint(20) NOT NULL,
  UNIQUE KEY `comments_id` (`comments_id`),
  KEY `FK2B8C155D3D1EED` (`Answer_id`),
  KEY `FK2B8C15549C95E12` (`comments_id`),
  CONSTRAINT `FK2B8C15549C95E12` FOREIGN KEY (`comments_id`) REFERENCES `Comment` (`id`),
  CONSTRAINT `FK2B8C155D3D1EED` FOREIGN KEY (`Answer_id`) REFERENCES `Answer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Answer_Comments`
--

LOCK TABLES `Answer_Comments` WRITE;
/*!40000 ALTER TABLE `Answer_Comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `Answer_Comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Answer_Votes`
--

DROP TABLE IF EXISTS `Answer_Votes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Answer_Votes` (
  `Answer_id` bigint(20) NOT NULL,
  `votes_id` bigint(20) NOT NULL,
  UNIQUE KEY `votes_id` (`votes_id`),
  KEY `FKC896CB08D3D1EED` (`Answer_id`),
  KEY `FKC896CB082A7767EE` (`votes_id`),
  CONSTRAINT `FKC896CB082A7767EE` FOREIGN KEY (`votes_id`) REFERENCES `Vote` (`id`),
  CONSTRAINT `FKC896CB08D3D1EED` FOREIGN KEY (`Answer_id`) REFERENCES `Answer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Answer_Votes`
--

LOCK TABLES `Answer_Votes` WRITE;
/*!40000 ALTER TABLE `Answer_Votes` DISABLE KEYS */;
/*!40000 ALTER TABLE `Answer_Votes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Comment`
--

DROP TABLE IF EXISTS `Comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment` longtext NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `htmlComment` longtext,
  `lastUpdatedAt` datetime DEFAULT NULL,
  `voteCount` bigint(20) NOT NULL,
  `author_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9BDE863FE5155ACD` (`author_id`),
  CONSTRAINT `FK9BDE863FE5155ACD` FOREIGN KEY (`author_id`) REFERENCES `Users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Comment`
--

LOCK TABLES `Comment` WRITE;
/*!40000 ALTER TABLE `Comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `Comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Comment_Flags`
--

DROP TABLE IF EXISTS `Comment_Flags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Comment_Flags` (
  `Comment_id` bigint(20) NOT NULL,
  `flags_id` bigint(20) NOT NULL,
  UNIQUE KEY `flags_id` (`flags_id`),
  KEY `FKEB33FC671F1F1C7` (`Comment_id`),
  KEY `FKEB33FC67ED5AAFF2` (`flags_id`),
  CONSTRAINT `FKEB33FC67ED5AAFF2` FOREIGN KEY (`flags_id`) REFERENCES `Flag` (`id`),
  CONSTRAINT `FKEB33FC671F1F1C7` FOREIGN KEY (`Comment_id`) REFERENCES `Comment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Comment_Flags`
--

LOCK TABLES `Comment_Flags` WRITE;
/*!40000 ALTER TABLE `Comment_Flags` DISABLE KEYS */;
/*!40000 ALTER TABLE `Comment_Flags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Comment_Votes`
--

DROP TABLE IF EXISTS `Comment_Votes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Comment_Votes` (
  `Comment_id` bigint(20) NOT NULL,
  `votes_id` bigint(20) NOT NULL,
  UNIQUE KEY `votes_id` (`votes_id`),
  KEY `FKEC1718A92A7767EE` (`votes_id`),
  KEY `FKEC1718A91F1F1C7` (`Comment_id`),
  CONSTRAINT `FKEC1718A91F1F1C7` FOREIGN KEY (`Comment_id`) REFERENCES `Comment` (`id`),
  CONSTRAINT `FKEC1718A92A7767EE` FOREIGN KEY (`votes_id`) REFERENCES `Vote` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Comment_Votes`
--

LOCK TABLES `Comment_Votes` WRITE;
/*!40000 ALTER TABLE `Comment_Votes` DISABLE KEYS */;
/*!40000 ALTER TABLE `Comment_Votes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Flag`
--

DROP TABLE IF EXISTS `Flag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Flag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reason` longtext,
  `type` varchar(255) DEFAULT NULL,
  `author_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK21738CE5155ACD` (`author_id`),
  CONSTRAINT `FK21738CE5155ACD` FOREIGN KEY (`author_id`) REFERENCES `Users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Flag`
--

LOCK TABLES `Flag` WRITE;
/*!40000 ALTER TABLE `Flag` DISABLE KEYS */;
/*!40000 ALTER TABLE `Flag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LoginMethod`
--

DROP TABLE IF EXISTS `LoginMethod`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LoginMethod` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `serviceEmail` varchar(100) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `serviceEmail` (`serviceEmail`),
  KEY `FKA6B9618A845C688D` (`user_id`),
  CONSTRAINT `FKA6B9618A845C688D` FOREIGN KEY (`user_id`) REFERENCES `Users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LoginMethod`
--

LOCK TABLES `LoginMethod` WRITE;
/*!40000 ALTER TABLE `LoginMethod` DISABLE KEYS */;
/*!40000 ALTER TABLE `LoginMethod` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Question`
--

DROP TABLE IF EXISTS `Question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Question` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdAt` datetime DEFAULT NULL,
  `lastUpdatedAt` datetime DEFAULT NULL,
  `views` bigint(20) NOT NULL,
  `voteCount` bigint(20) NOT NULL,
  `author_id` bigint(20) DEFAULT NULL,
  `information_id` bigint(20) NOT NULL,
  `lastTouchedBy_id` bigint(20) DEFAULT NULL,
  `solution_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKBE5CA006171098B9` (`lastTouchedBy_id`),
  KEY `FKBE5CA006E5155ACD` (`author_id`),
  KEY `FKBE5CA0062F77F352` (`solution_id`),
  KEY `FKBE5CA006CFCA4761` (`information_id`),
  CONSTRAINT `FKBE5CA006CFCA4761` FOREIGN KEY (`information_id`) REFERENCES `QuestionInformation` (`id`),
  CONSTRAINT `FKBE5CA006171098B9` FOREIGN KEY (`lastTouchedBy_id`) REFERENCES `Users` (`id`),
  CONSTRAINT `FKBE5CA0062F77F352` FOREIGN KEY (`solution_id`) REFERENCES `Answer` (`id`),
  CONSTRAINT `FKBE5CA006E5155ACD` FOREIGN KEY (`author_id`) REFERENCES `Users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Question`
--

LOCK TABLES `Question` WRITE;
/*!40000 ALTER TABLE `Question` DISABLE KEYS */;
/*!40000 ALTER TABLE `Question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QuestionInformation`
--

DROP TABLE IF EXISTS `QuestionInformation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QuestionInformation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment` longtext,
  `createdAt` datetime DEFAULT NULL,
  `description` longtext NOT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `markedDescription` longtext,
  `moderatedAt` datetime DEFAULT NULL,
  `sluggedTitle` longtext NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `title` longtext NOT NULL,
  `author_id` bigint(20) NOT NULL,
  `moderatedBy_id` bigint(20) DEFAULT NULL,
  `question_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKFE695D467AC17960` (`moderatedBy_id`),
  KEY `FKFE695D46E5155ACD` (`author_id`),
  KEY `FKFE695D46D6CA1D2D` (`question_id`),
  CONSTRAINT `FKFE695D46D6CA1D2D` FOREIGN KEY (`question_id`) REFERENCES `Question` (`id`),
  CONSTRAINT `FKFE695D467AC17960` FOREIGN KEY (`moderatedBy_id`) REFERENCES `Users` (`id`),
  CONSTRAINT `FKFE695D46E5155ACD` FOREIGN KEY (`author_id`) REFERENCES `Users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QuestionInformation`
--

LOCK TABLES `QuestionInformation` WRITE;
/*!40000 ALTER TABLE `QuestionInformation` DISABLE KEYS */;
/*!40000 ALTER TABLE `QuestionInformation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QuestionInformation_Tag`
--

DROP TABLE IF EXISTS `QuestionInformation_Tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QuestionInformation_Tag` (
  `QuestionInformation_id` bigint(20) NOT NULL,
  `tags_id` bigint(20) NOT NULL,
  KEY `FKC479C10176BB2988` (`tags_id`),
  KEY `FKC479C1014B6AB8C7` (`QuestionInformation_id`),
  CONSTRAINT `FKC479C1014B6AB8C7` FOREIGN KEY (`QuestionInformation_id`) REFERENCES `QuestionInformation` (`id`),
  CONSTRAINT `FKC479C10176BB2988` FOREIGN KEY (`tags_id`) REFERENCES `Tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QuestionInformation_Tag`
--

LOCK TABLES `QuestionInformation_Tag` WRITE;
/*!40000 ALTER TABLE `QuestionInformation_Tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `QuestionInformation_Tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Question_Comments`
--

DROP TABLE IF EXISTS `Question_Comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Question_Comments` (
  `Question_id` bigint(20) NOT NULL,
  `comments_id` bigint(20) NOT NULL,
  UNIQUE KEY `comments_id` (`comments_id`),
  KEY `FK2EE77A2D49C95E12` (`comments_id`),
  KEY `FK2EE77A2DD6CA1D2D` (`Question_id`),
  CONSTRAINT `FK2EE77A2DD6CA1D2D` FOREIGN KEY (`Question_id`) REFERENCES `Question` (`id`),
  CONSTRAINT `FK2EE77A2D49C95E12` FOREIGN KEY (`comments_id`) REFERENCES `Comment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Question_Comments`
--

LOCK TABLES `Question_Comments` WRITE;
/*!40000 ALTER TABLE `Question_Comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `Question_Comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Question_Votes`
--

DROP TABLE IF EXISTS `Question_Votes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Question_Votes` (
  `Question_id` bigint(20) NOT NULL,
  `votes_id` bigint(20) NOT NULL,
  UNIQUE KEY `votes_id` (`votes_id`),
  KEY `FK52D681302A7767EE` (`votes_id`),
  KEY `FK52D68130D6CA1D2D` (`Question_id`),
  CONSTRAINT `FK52D68130D6CA1D2D` FOREIGN KEY (`Question_id`) REFERENCES `Question` (`id`),
  CONSTRAINT `FK52D681302A7767EE` FOREIGN KEY (`votes_id`) REFERENCES `Vote` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Question_Votes`
--

LOCK TABLES `Question_Votes` WRITE;
/*!40000 ALTER TABLE `Question_Votes` DISABLE KEYS */;
/*!40000 ALTER TABLE `Question_Votes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Tag`
--

DROP TABLE IF EXISTS `Tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdAt` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `usageCount` bigint(20) DEFAULT NULL,
  `author_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `FK1477AE5155ACD` (`author_id`),
  CONSTRAINT `FK1477AE5155ACD` FOREIGN KEY (`author_id`) REFERENCES `Users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Tag`
--

LOCK TABLES `Tag` WRITE;
/*!40000 ALTER TABLE `Tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `Tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Users`
--

DROP TABLE IF EXISTS `Users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `about` varchar(500) DEFAULT NULL,
  `birthDate` datetime DEFAULT NULL,
  `confirmedEmail` bit(1) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `forgotPasswordToken` varchar(255) DEFAULT NULL,
  `karma` bigint(20) NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  `markedAbout` varchar(600) DEFAULT NULL,
  `moderator` bit(1) NOT NULL,
  `name` varchar(100) NOT NULL,
  `nameLastTouchedAt` datetime DEFAULT NULL,
  `photoUri` varchar(255) DEFAULT NULL,
  `realName` varchar(100) DEFAULT NULL,
  `sessionKey` varchar(255) DEFAULT NULL,
  `sluggedName` longtext NOT NULL,
  `website` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sessionKey` (`sessionKey`),
  KEY `session_key` (`sessionKey`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Users`
--

LOCK TABLES `Users` WRITE;
/*!40000 ALTER TABLE `Users` DISABLE KEYS */;
/*!40000 ALTER TABLE `Users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Vote`
--

DROP TABLE IF EXISTS `Vote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Vote` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdAt` datetime DEFAULT NULL,
  `lastUpdatedAt` datetime DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `author_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK28C70AE5155ACD` (`author_id`),
  CONSTRAINT `FK28C70AE5155ACD` FOREIGN KEY (`author_id`) REFERENCES `Users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;


CREATE TABLE `Question_Flags` (
  `Question_id` bigint(20) NOT NULL,
  `flags_id` bigint(20) NOT NULL,
  UNIQUE KEY `flags_id` (`flags_id`),
  KEY `FK51F364EEED5AAFF2` (`flags_id`),
  KEY `FK51F364EED6CA1D2D` (`Question_id`),
  CONSTRAINT `FK51F364EED6CA1D2D` FOREIGN KEY (`Question_id`) REFERENCES `Question` (`id`),
  CONSTRAINT `FK51F364EEED5AAFF2` FOREIGN KEY (`flags_id`) REFERENCES `Flag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Answer_Flags` (
  `Answer_id` bigint(20) NOT NULL,
  `flags_id` bigint(20) NOT NULL,
  UNIQUE KEY `flags_id` (`flags_id`),
  KEY `FKC7B3AEC6D3D1EED` (`Answer_id`),
  KEY `FKC7B3AEC6ED5AAFF2` (`flags_id`),
  CONSTRAINT `FKC7B3AEC6D3D1EED` FOREIGN KEY (`Answer_id`) REFERENCES `Answer` (`id`),
  CONSTRAINT `FKC7B3AEC6ED5AAFF2` FOREIGN KEY (`flags_id`) REFERENCES `Flag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Vote`
--

LOCK TABLES `Vote` WRITE;
/*!40000 ALTER TABLE `Vote` DISABLE KEYS */;
/*!40000 ALTER TABLE `Vote` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-03-07 16:22:47
