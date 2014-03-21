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
  `invisible` tinyint(1) NOT NULL,
  `voteCount` bigint(20) NOT NULL,
  `author_id` bigint(20) DEFAULT NULL,
  `information_id` bigint(20) NOT NULL,
  `lastTouchedBy_id` bigint(20) DEFAULT NULL,
  `question_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_3q5s4b88xp78n3c49dtxfs97e` (`author_id`),
  KEY `FK_dshhxt02iww0fxkl2li8l3ao2` (`information_id`),
  KEY `FK_drifk8pp2s7wsh57nvwna9m1g` (`lastTouchedBy_id`),
  KEY `FK_10g8xii7lw9kq0kcsobgmtw72` (`question_id`),
  CONSTRAINT `FK_10g8xii7lw9kq0kcsobgmtw72` FOREIGN KEY (`question_id`) REFERENCES `Question` (`id`),
  CONSTRAINT `FK_3q5s4b88xp78n3c49dtxfs97e` FOREIGN KEY (`author_id`) REFERENCES `Users` (`id`),
  CONSTRAINT `FK_drifk8pp2s7wsh57nvwna9m1g` FOREIGN KEY (`lastTouchedBy_id`) REFERENCES `Users` (`id`),
  CONSTRAINT `FK_dshhxt02iww0fxkl2li8l3ao2` FOREIGN KEY (`information_id`) REFERENCES `AnswerInformation` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AnswerInformation`
--

DROP TABLE IF EXISTS `AnswerInformation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AnswerInformation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment` longtext NOT NULL,
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
  KEY `FK_lecgqmqj00d06wb8nwaq60rpr` (`answer_id`),
  KEY `FK_237rcoro0n05xyxjga1ip7pd8` (`author_id`),
  KEY `FK_dbuximcggdn5k2j9svvpwtxrm` (`moderatedBy_id`),
  CONSTRAINT `FK_237rcoro0n05xyxjga1ip7pd8` FOREIGN KEY (`author_id`) REFERENCES `Users` (`id`),
  CONSTRAINT `FK_dbuximcggdn5k2j9svvpwtxrm` FOREIGN KEY (`moderatedBy_id`) REFERENCES `Users` (`id`),
  CONSTRAINT `FK_lecgqmqj00d06wb8nwaq60rpr` FOREIGN KEY (`answer_id`) REFERENCES `Answer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Answer_Comments`
--

DROP TABLE IF EXISTS `Answer_Comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Answer_Comments` (
  `Answer_id` bigint(20) NOT NULL,
  `comments_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_731ugn0r28nit0o73yytcw5oh` (`comments_id`),
  KEY `FK_731ugn0r28nit0o73yytcw5oh` (`comments_id`),
  KEY `FK_5c40gkw8p92hpuy5nnothdhw5` (`Answer_id`),
  CONSTRAINT `FK_5c40gkw8p92hpuy5nnothdhw5` FOREIGN KEY (`Answer_id`) REFERENCES `Answer` (`id`),
  CONSTRAINT `FK_731ugn0r28nit0o73yytcw5oh` FOREIGN KEY (`comments_id`) REFERENCES `Comment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Answer_Flags`
--

DROP TABLE IF EXISTS `Answer_Flags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Answer_Flags` (
  `Answer_id` bigint(20) NOT NULL,
  `flags_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_9y4wu81bq6tdthk881o8twros` (`flags_id`),
  KEY `FK_9y4wu81bq6tdthk881o8twros` (`flags_id`),
  KEY `FK_6lq2gt46y9974igmh0jxo666` (`Answer_id`),
  CONSTRAINT `FK_6lq2gt46y9974igmh0jxo666` FOREIGN KEY (`Answer_id`) REFERENCES `Answer` (`id`),
  CONSTRAINT `FK_9y4wu81bq6tdthk881o8twros` FOREIGN KEY (`flags_id`) REFERENCES `Flag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Answer_Votes`
--

DROP TABLE IF EXISTS `Answer_Votes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Answer_Votes` (
  `Answer_id` bigint(20) NOT NULL,
  `votes_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_5ya5d072g3h38tvb9brj7bs8o` (`votes_id`),
  KEY `FK_5ya5d072g3h38tvb9brj7bs8o` (`votes_id`),
  KEY `FK_qpawvfihxtc49opw1q5le336l` (`Answer_id`),
  CONSTRAINT `FK_5ya5d072g3h38tvb9brj7bs8o` FOREIGN KEY (`votes_id`) REFERENCES `Vote` (`id`),
  CONSTRAINT `FK_qpawvfihxtc49opw1q5le336l` FOREIGN KEY (`Answer_id`) REFERENCES `Answer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `htmlComment` longtext NOT NULL,
  `lastUpdatedAt` datetime DEFAULT NULL,
  `invisible` tinyint(1) NOT NULL,
  `voteCount` bigint(20) NOT NULL,
  `author_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_j94pith5sd971k29j6ysxuk7` (`author_id`),
  CONSTRAINT `FK_j94pith5sd971k29j6ysxuk7` FOREIGN KEY (`author_id`) REFERENCES `Users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Comment_Flags`
--

DROP TABLE IF EXISTS `Comment_Flags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Comment_Flags` (
  `Comment_id` bigint(20) NOT NULL,
  `flags_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_gkehc29f3h04bua96bjurv4vd` (`flags_id`),
  KEY `FK_gkehc29f3h04bua96bjurv4vd` (`flags_id`),
  KEY `FK_g45y0rm9o8k7uyoih84rrccra` (`Comment_id`),
  CONSTRAINT `FK_g45y0rm9o8k7uyoih84rrccra` FOREIGN KEY (`Comment_id`) REFERENCES `Comment` (`id`),
  CONSTRAINT `FK_gkehc29f3h04bua96bjurv4vd` FOREIGN KEY (`flags_id`) REFERENCES `Flag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Comment_Votes`
--

DROP TABLE IF EXISTS `Comment_Votes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Comment_Votes` (
  `Comment_id` bigint(20) NOT NULL,
  `votes_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_dtgxtqffciorpdsjdshnkbpw6` (`votes_id`),
  KEY `FK_dtgxtqffciorpdsjdshnkbpw6` (`votes_id`),
  KEY `FK_obxdv4j0ph2swt8r81dx8h0yw` (`Comment_id`),
  CONSTRAINT `FK_dtgxtqffciorpdsjdshnkbpw6` FOREIGN KEY (`votes_id`) REFERENCES `Vote` (`id`),
  CONSTRAINT `FK_obxdv4j0ph2swt8r81dx8h0yw` FOREIGN KEY (`Comment_id`) REFERENCES `Comment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  KEY `FK_lumgu8dnorkynxw1l039ovm8q` (`author_id`),
  CONSTRAINT `FK_lumgu8dnorkynxw1l039ovm8q` FOREIGN KEY (`author_id`) REFERENCES `Users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  KEY `FK_l0vhdtw8ymw1rrxq9usrqjh4x` (`user_id`),
  CONSTRAINT `FK_l0vhdtw8ymw1rrxq9usrqjh4x` FOREIGN KEY (`user_id`) REFERENCES `Users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `News`
--

DROP TABLE IF EXISTS `News`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `News` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `approved` tinyint(1) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `lastUpdatedAt` datetime DEFAULT NULL,
  `invisible` tinyint(1) NOT NULL,
  `views` bigint(20) NOT NULL,
  `voteCount` bigint(20) NOT NULL,
  `author_id` bigint(20) DEFAULT NULL,
  `information_id` bigint(20) NOT NULL,
  `lastTouchedBy_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_e3k3kapw96m39ma7uus1r6f7m` (`author_id`),
  KEY `FK_5qrrq79ar6t4p4vf4djvqf63i` (`information_id`),
  KEY `FK_glms254gw9a4kv5qh3ptijiqd` (`lastTouchedBy_id`),
  CONSTRAINT `FK_5qrrq79ar6t4p4vf4djvqf63i` FOREIGN KEY (`information_id`) REFERENCES `NewsInformation` (`id`),
  CONSTRAINT `FK_e3k3kapw96m39ma7uus1r6f7m` FOREIGN KEY (`author_id`) REFERENCES `Users` (`id`),
  CONSTRAINT `FK_glms254gw9a4kv5qh3ptijiqd` FOREIGN KEY (`lastTouchedBy_id`) REFERENCES `Users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `NewsInformation`
--

DROP TABLE IF EXISTS `NewsInformation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `NewsInformation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment` longtext NOT NULL,
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
  `news_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_n3hxv49li2jcopaj6dio6b8b6` (`author_id`),
  KEY `FK_fppkwwg6svwefnnni9ygaefg2` (`moderatedBy_id`),
  KEY `FK_b942i5pshr99wwqdk03d98ofg` (`news_id`),
  CONSTRAINT `FK_b942i5pshr99wwqdk03d98ofg` FOREIGN KEY (`news_id`) REFERENCES `News` (`id`),
  CONSTRAINT `FK_fppkwwg6svwefnnni9ygaefg2` FOREIGN KEY (`moderatedBy_id`) REFERENCES `Users` (`id`),
  CONSTRAINT `FK_n3hxv49li2jcopaj6dio6b8b6` FOREIGN KEY (`author_id`) REFERENCES `Users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `News_Comments`
--

DROP TABLE IF EXISTS `News_Comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `News_Comments` (
  `News_id` bigint(20) NOT NULL,
  `comments_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_5rrh2dvs1cy19pliwyvb8w8u7` (`comments_id`),
  KEY `FK_5rrh2dvs1cy19pliwyvb8w8u7` (`comments_id`),
  KEY `FK_dcofc609g7loixe8pcm3myemd` (`News_id`),
  CONSTRAINT `FK_5rrh2dvs1cy19pliwyvb8w8u7` FOREIGN KEY (`comments_id`) REFERENCES `Comment` (`id`),
  CONSTRAINT `FK_dcofc609g7loixe8pcm3myemd` FOREIGN KEY (`News_id`) REFERENCES `News` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `News_Flags`
--

DROP TABLE IF EXISTS `News_Flags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `News_Flags` (
  `News_id` bigint(20) NOT NULL,
  `flags_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_s0ugdfyknbgui197dcioqqovi` (`flags_id`),
  KEY `FK_s0ugdfyknbgui197dcioqqovi` (`flags_id`),
  KEY `FK_fr0qqkoqj2rai6g9epq0iw4wu` (`News_id`),
  CONSTRAINT `FK_fr0qqkoqj2rai6g9epq0iw4wu` FOREIGN KEY (`News_id`) REFERENCES `News` (`id`),
  CONSTRAINT `FK_s0ugdfyknbgui197dcioqqovi` FOREIGN KEY (`flags_id`) REFERENCES `Flag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `News_Votes`
--

DROP TABLE IF EXISTS `News_Votes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `News_Votes` (
  `News_id` bigint(20) NOT NULL,
  `votes_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_mecjpg8cg90p1ry4sg4rckb09` (`votes_id`),
  KEY `FK_mecjpg8cg90p1ry4sg4rckb09` (`votes_id`),
  KEY `FK_rqhl3hox4wntf7oc9y4af5cgv` (`News_id`),
  CONSTRAINT `FK_mecjpg8cg90p1ry4sg4rckb09` FOREIGN KEY (`votes_id`) REFERENCES `Vote` (`id`),
  CONSTRAINT `FK_rqhl3hox4wntf7oc9y4af5cgv` FOREIGN KEY (`News_id`) REFERENCES `News` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `News_Watchers`
--

DROP TABLE IF EXISTS `News_Watchers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `News_Watchers` (
  `News_id` bigint(20) NOT NULL,
  `watchers_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_m5koj8lugxy5pp42xk656vqln` (`watchers_id`),
  KEY `FK_m5koj8lugxy5pp42xk656vqln` (`watchers_id`),
  KEY `FK_bywcu6iha3jaici2oiljml8ho` (`News_id`),
  CONSTRAINT `FK_bywcu6iha3jaici2oiljml8ho` FOREIGN KEY (`News_id`) REFERENCES `News` (`id`),
  CONSTRAINT `FK_m5koj8lugxy5pp42xk656vqln` FOREIGN KEY (`watchers_id`) REFERENCES `Watcher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Question`
--

DROP TABLE IF EXISTS `Question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Question` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `answerCount` bigint(20) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `lastUpdatedAt` datetime DEFAULT NULL,
  `invisible` tinyint(1) NOT NULL,
  `views` bigint(20) NOT NULL,
  `voteCount` bigint(20) NOT NULL,
  `author_id` bigint(20) DEFAULT NULL,
  `information_id` bigint(20) NOT NULL,
  `lastTouchedBy_id` bigint(20) DEFAULT NULL,
  `solution_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_a3dib35x299yvhfk7pau0kw5w` (`author_id`),
  KEY `FK_i2xt9jcwfauudnswun94neqyg` (`information_id`),
  KEY `FK_9d3cyy648wfruj9t7556wqgjr` (`lastTouchedBy_id`),
  KEY `FK_liw3djybv5je7ra806bsipg68` (`solution_id`),
  CONSTRAINT `FK_9d3cyy648wfruj9t7556wqgjr` FOREIGN KEY (`lastTouchedBy_id`) REFERENCES `Users` (`id`),
  CONSTRAINT `FK_a3dib35x299yvhfk7pau0kw5w` FOREIGN KEY (`author_id`) REFERENCES `Users` (`id`),
  CONSTRAINT `FK_i2xt9jcwfauudnswun94neqyg` FOREIGN KEY (`information_id`) REFERENCES `QuestionInformation` (`id`),
  CONSTRAINT `FK_liw3djybv5je7ra806bsipg68` FOREIGN KEY (`solution_id`) REFERENCES `Answer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=206 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QuestionInformation`
--

DROP TABLE IF EXISTS `QuestionInformation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QuestionInformation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment` longtext NOT NULL,
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
  KEY `FK_9nfk2kchvyn69e7gdh798gf` (`author_id`),
  KEY `FK_t1oox0xh74vlikcvhxd5k2kq1` (`moderatedBy_id`),
  KEY `FK_pl1drgbxhfd4hbmd3smwa3svl` (`question_id`),
  CONSTRAINT `FK_9nfk2kchvyn69e7gdh798gf` FOREIGN KEY (`author_id`) REFERENCES `Users` (`id`),
  CONSTRAINT `FK_pl1drgbxhfd4hbmd3smwa3svl` FOREIGN KEY (`question_id`) REFERENCES `Question` (`id`),
  CONSTRAINT `FK_t1oox0xh74vlikcvhxd5k2kq1` FOREIGN KEY (`moderatedBy_id`) REFERENCES `Users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=207 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QuestionInformation_Tag`
--

DROP TABLE IF EXISTS `QuestionInformation_Tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QuestionInformation_Tag` (
  `QuestionInformation_id` bigint(20) NOT NULL,
  `tags_id` bigint(20) NOT NULL,
  `tag_order` int(11) NOT NULL,
  PRIMARY KEY (`QuestionInformation_id`,`tag_order`),
  KEY `FK_nv1tmcost5jqejnlb6u0wrypo` (`tags_id`),
  KEY `FK_7u4x47xa5gdhmt650curhu3kx` (`QuestionInformation_id`),
  CONSTRAINT `FK_7u4x47xa5gdhmt650curhu3kx` FOREIGN KEY (`QuestionInformation_id`) REFERENCES `QuestionInformation` (`id`),
  CONSTRAINT `FK_nv1tmcost5jqejnlb6u0wrypo` FOREIGN KEY (`tags_id`) REFERENCES `Tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Question_Comments`
--

DROP TABLE IF EXISTS `Question_Comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Question_Comments` (
  `Question_id` bigint(20) NOT NULL,
  `comments_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_6jsfvsef241a3ldcck6pid4vi` (`comments_id`),
  KEY `FK_6jsfvsef241a3ldcck6pid4vi` (`comments_id`),
  KEY `FK_fm57yvrnidsyeuvls0he5c9pk` (`Question_id`),
  CONSTRAINT `FK_6jsfvsef241a3ldcck6pid4vi` FOREIGN KEY (`comments_id`) REFERENCES `Comment` (`id`),
  CONSTRAINT `FK_fm57yvrnidsyeuvls0he5c9pk` FOREIGN KEY (`Question_id`) REFERENCES `Question` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Question_Flags`
--

DROP TABLE IF EXISTS `Question_Flags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Question_Flags` (
  `Question_id` bigint(20) NOT NULL,
  `flags_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_a8brcb8bpevccipiyplquqqjv` (`flags_id`),
  KEY `FK_a8brcb8bpevccipiyplquqqjv` (`flags_id`),
  KEY `FK_ftntexa7hxbaqo2i2yg4i0yr3` (`Question_id`),
  CONSTRAINT `FK_a8brcb8bpevccipiyplquqqjv` FOREIGN KEY (`flags_id`) REFERENCES `Flag` (`id`),
  CONSTRAINT `FK_ftntexa7hxbaqo2i2yg4i0yr3` FOREIGN KEY (`Question_id`) REFERENCES `Question` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Question_Votes`
--

DROP TABLE IF EXISTS `Question_Votes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Question_Votes` (
  `Question_id` bigint(20) NOT NULL,
  `votes_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_p5sgssf0gw0br66mvu9cctlmq` (`votes_id`),
  KEY `FK_p5sgssf0gw0br66mvu9cctlmq` (`votes_id`),
  KEY `FK_24u6uwfjr8s8pfk7oyfw8u4o4` (`Question_id`),
  CONSTRAINT `FK_24u6uwfjr8s8pfk7oyfw8u4o4` FOREIGN KEY (`Question_id`) REFERENCES `Question` (`id`),
  CONSTRAINT `FK_p5sgssf0gw0br66mvu9cctlmq` FOREIGN KEY (`votes_id`) REFERENCES `Vote` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Question_Watchers`
--

DROP TABLE IF EXISTS `Question_Watchers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Question_Watchers` (
  `Question_id` bigint(20) NOT NULL,
  `watchers_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_5twinfe7e6g09gaowkeah0498` (`watchers_id`),
  KEY `FK_5twinfe7e6g09gaowkeah0498` (`watchers_id`),
  KEY `FK_pu72rhjonka0flev96adthdp0` (`Question_id`),
  CONSTRAINT `FK_5twinfe7e6g09gaowkeah0498` FOREIGN KEY (`watchers_id`) REFERENCES `Watcher` (`id`),
  CONSTRAINT `FK_pu72rhjonka0flev96adthdp0` FOREIGN KEY (`Question_id`) REFERENCES `Question` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ReputationEvent`
--

DROP TABLE IF EXISTS `ReputationEvent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ReputationEvent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `context_type` varchar(255) DEFAULT NULL,
  `context_id` bigint(20) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `karmaReward` int(11) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_gbu6jo147pal18b3q3blpr0of` (`user_id`),
  CONSTRAINT `FK_gbu6jo147pal18b3q3blpr0of` FOREIGN KEY (`user_id`) REFERENCES `Users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  UNIQUE KEY `UK_24642shpebunaq3ggshotv9hk` (`name`),
  KEY `FK_9a9b8a968n0ejs6yikpgo563r` (`author_id`),
  CONSTRAINT `FK_9a9b8a968n0ejs6yikpgo563r` FOREIGN KEY (`author_id`) REFERENCES `Users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UserSession`
--

DROP TABLE IF EXISTS `UserSession`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserSession` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdAt` datetime DEFAULT NULL,
  `sessionKey` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_jhpxm4m9w5ujlygolg3nj08m9` (`sessionKey`),
  KEY `session_key` (`sessionKey`),
  KEY `FK_g1vcu7yf9bjb3kj31y3ghw0jg` (`user_id`),
  CONSTRAINT `FK_g1vcu7yf9bjb3kj31y3ghw0jg` FOREIGN KEY (`user_id`) REFERENCES `Users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `confirmedEmail` tinyint(1) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `forgotPasswordToken` varchar(255) DEFAULT NULL,
  `isBanned` tinyint(1) NOT NULL,
  `isSubscribed` tinyint(1) NOT NULL,
  `karma` bigint(20) NOT NULL,
  `location` varchar(100) DEFAULT NULL,
  `markedAbout` varchar(600) DEFAULT NULL,
  `moderator` tinyint(1) NOT NULL,
  `name` varchar(100) NOT NULL,
  `nameLastTouchedAt` datetime DEFAULT NULL,
  `photoUri` varchar(255) DEFAULT NULL,
  `realName` varchar(100) DEFAULT NULL,
  `sluggedName` longtext NOT NULL,
  `website` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  KEY `FK_6nch3y92lphrbsh0o5c7o0jov` (`author_id`),
  CONSTRAINT `FK_6nch3y92lphrbsh0o5c7o0jov` FOREIGN KEY (`author_id`) REFERENCES `Users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Watcher`
--

DROP TABLE IF EXISTS `Watcher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Watcher` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` tinyint(1) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `watcher_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_hobtys3mefri57vry8w6o8xyq` (`watcher_id`),
  CONSTRAINT `FK_hobtys3mefri57vry8w6o8xyq` FOREIGN KEY (`watcher_id`) REFERENCES `Users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `brutalmigration`
--

DROP TABLE IF EXISTS `brutalmigration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `brutalmigration` (
  `number` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-08-07 11:06:45
