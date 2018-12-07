CREATE DATABASE  IF NOT EXISTS `deepThoughtTest` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `deepThoughtTest`;
-- MySQL dump 10.13  Distrib 5.7.24, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: deepThoughtDB
-- ------------------------------------------------------
-- Server version	5.7.24-0ubuntu0.18.04.1

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
  `answer_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `htmlContent` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `question_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `createDate` datetime NOT NULL,
  `rating` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`answer_id`),
  UNIQUE KEY `Answer_answer_id_uindex` (`answer_id`),
  KEY `Answer_question_content_id_pk` (`question_id`),
  KEY `answer_user_fk` (`user_id`),
  CONSTRAINT `answer_user_fk` FOREIGN KEY (`user_id`) REFERENCES `UserInfo` (`user_id`),
  CONSTRAINT `question_answer_fk` FOREIGN KEY (`question_id`) REFERENCES `Questions` (`question_id`),
  CONSTRAINT `question_content_answer_fk` FOREIGN KEY (`question_id`) REFERENCES `QuestionContent` (`question_content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ArticleComments`
--

DROP TABLE IF EXISTS `ArticleComments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ArticleComments` (
  `article_comment_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `createDate` datetime(6) NOT NULL,
  `article_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `rating` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`article_comment_id`),
  UNIQUE KEY `postsComments_post_comment_id_uindex` (`article_comment_id`),
  KEY `FKevk58i1vrqkq57kxk4q01g52y` (`article_id`),
  KEY `FKfpevda4yxs23ars6m71e5buhy` (`user_id`),
  CONSTRAINT `FK6tcytb6j305mml7vmwh5m13ov` FOREIGN KEY (`article_id`) REFERENCES `ArticleInfo` (`article_id`),
  CONSTRAINT `FKd3ungegx3154vqhql35nky94g` FOREIGN KEY (`user_id`) REFERENCES `UserInfo` (`user_id`),
  CONSTRAINT `FKevk58i1vrqkq57kxk4q01g52y` FOREIGN KEY (`article_id`) REFERENCES `ArticleInfo` (`article_id`),
  CONSTRAINT `FKfpevda4yxs23ars6m71e5buhy` FOREIGN KEY (`user_id`) REFERENCES `UserInfo` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ArticleContent`
--

DROP TABLE IF EXISTS `ArticleContent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ArticleContent` (
  `article_content_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `htmlContent` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `subtitle` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `article_id` bigint(20) NOT NULL,
  PRIMARY KEY (`article_content_id`),
  UNIQUE KEY `postContent_post_content_id_uindex` (`article_content_id`),
  UNIQUE KEY `ArticleContent_article_id_uindex` (`article_id`),
  CONSTRAINT `article_content_fk` FOREIGN KEY (`article_id`) REFERENCES `ArticleInfo` (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ArticleInfo`
--

DROP TABLE IF EXISTS `ArticleInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ArticleInfo` (
  `article_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createDate` datetime NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `views` bigint(20) NOT NULL DEFAULT '0',
  `user_id` bigint(20) NOT NULL DEFAULT '0',
  `activated` bit(1) NOT NULL,
  `largeImagePath` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL,
  `middleImagePath` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL,
  `rating` bigint(20) NOT NULL DEFAULT '0',
  `smallImagePath` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`article_id`),
  UNIQUE KEY `postsInfo_post_id_uindex` (`article_id`),
  KEY `postsInfo_createDate_index` (`createDate`),
  KEY `postsInfo_title_index` (`title`),
  KEY `FKl3c8q43i4oc64q0ly8pxosp1r` (`user_id`),
  KEY `ArticleInfo_rating_index` (`rating`),
  CONSTRAINT `FKl3c8q43i4oc64q0ly8pxosp1r` FOREIGN KEY (`user_id`) REFERENCES `UserInfo` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ArticlesTags`
--

DROP TABLE IF EXISTS `ArticlesTags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ArticlesTags` (
  `article_id` bigint(20) NOT NULL,
  `tag_id` bigint(20) NOT NULL,
  `articles_tags_id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`articles_tags_id`),
  UNIQUE KEY `PostsTags_posts_tags_id_uindex` (`articles_tags_id`),
  KEY `FK3phn4mlnonhxgd2r2lcwghb23` (`article_id`),
  KEY `FKdjl5akfreu5kwob3kmud9q18i` (`tag_id`),
  CONSTRAINT `FK3phn4mlnonhxgd2r2lcwghb23` FOREIGN KEY (`article_id`) REFERENCES `ArticleInfo` (`article_id`),
  CONSTRAINT `FKd4ssjq4mqjlexqg5jsmi0oj3w` FOREIGN KEY (`tag_id`) REFERENCES `Tags` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Facts`
--

DROP TABLE IF EXISTS `Facts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Facts` (
  `fact_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `text` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `activated` bit(1) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `createDate` datetime NOT NULL,
  `rating` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`fact_id`),
  UNIQUE KEY `Facts_fact_id_uindex` (`fact_id`),
  KEY `Facts_user_id_index` (`user_id`),
  KEY `Facts_createDate_index` (`createDate`),
  KEY `Facts_rating_index` (`rating`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FactsTags`
--

DROP TABLE IF EXISTS `FactsTags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FactsTags` (
  `fact_id` bigint(20) NOT NULL,
  `tag_id` bigint(20) NOT NULL,
  `facts_tags_id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`facts_tags_id`),
  UNIQUE KEY `FactsTags_facts_tags_id_uindex` (`facts_tags_id`),
  KEY `FKnw2tmnxvxtytqk6bcbkxe5rwy` (`tag_id`),
  KEY `FKqt7q0rrpdebq1xrse231na2sv` (`fact_id`),
  CONSTRAINT `FKnw2tmnxvxtytqk6bcbkxe5rwy` FOREIGN KEY (`tag_id`) REFERENCES `Tags` (`tag_id`),
  CONSTRAINT `FKqt7q0rrpdebq1xrse231na2sv` FOREIGN KEY (`fact_id`) REFERENCES `Facts` (`fact_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Problem`
--

DROP TABLE IF EXISTS `Problem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Problem` (
  `problem_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `activated` bit(1) NOT NULL DEFAULT b'1',
  `rating` bigint(20) NOT NULL,
  `createDate` datetime NOT NULL,
  `difficult` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `views` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`problem_id`),
  UNIQUE KEY `Problem_problem_id_uindex` (`problem_id`),
  KEY `Problem_createDate_index` (`createDate`),
  KEY `Problem_user_id_index` (`user_id`),
  KEY `Problem_difficult_index` (`difficult`),
  KEY `Problem_rating_index` (`rating`),
  KEY `Problem_title_index` (`title`),
  CONSTRAINT `FKfrpk0nrxigyk47or8yhcm6u4l` FOREIGN KEY (`user_id`) REFERENCES `UserInfo` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ProblemComment`
--

DROP TABLE IF EXISTS `ProblemComment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ProblemComment` (
  `problem_comment_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `rating` bigint(20) NOT NULL,
  `createDate` datetime NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `problem_id` bigint(20) NOT NULL,
  PRIMARY KEY (`problem_comment_id`),
  UNIQUE KEY `ProblemComment_problem_comment_id_uindex` (`problem_comment_id`),
  KEY `ProblemComment_problem_solution_id_index` (`problem_id`),
  KEY `user_problem_comment_fk` (`user_id`),
  CONSTRAINT `problem_comments_fk` FOREIGN KEY (`problem_id`) REFERENCES `Problem` (`problem_id`),
  CONSTRAINT `user_problem_comment_fk` FOREIGN KEY (`user_id`) REFERENCES `UserInfo` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ProblemContent`
--

DROP TABLE IF EXISTS `ProblemContent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ProblemContent` (
  `problem_content_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `htmlContent` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `problem_id` bigint(20) NOT NULL,
  PRIMARY KEY (`problem_content_id`),
  UNIQUE KEY `ProblemContent_problem_content_id_uindex` (`problem_content_id`),
  UNIQUE KEY `ProblemContent_problem_id_uindex` (`problem_id`),
  CONSTRAINT `problem_content_fk` FOREIGN KEY (`problem_id`) REFERENCES `Problem` (`problem_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ProblemSolution`
--

DROP TABLE IF EXISTS `ProblemSolution`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ProblemSolution` (
  `problem_solution_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `solution` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `answer` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL,
  `problem_id` bigint(20) NOT NULL,
  PRIMARY KEY (`problem_solution_id`),
  UNIQUE KEY `ProblemSolution_problem_solution_id_uindex` (`problem_solution_id`),
  UNIQUE KEY `ProblemSolution_problem_id_uindex` (`problem_id`),
  CONSTRAINT `problem_solution_fk` FOREIGN KEY (`problem_id`) REFERENCES `Problem` (`problem_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ProblemsSolvedUsers`
--

DROP TABLE IF EXISTS `ProblemsSolvedUsers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ProblemsSolvedUsers` (
  `problems_solved_users_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `problem_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`problems_solved_users_id`),
  UNIQUE KEY `ProblemsSolvedUsers_problems_solved_users_id_uindex` (`problems_solved_users_id`),
  KEY `ProblemsSolvedUsers_problem_id_user_id_index` (`problem_id`,`user_id`),
  KEY `ProblemsSolvedUsers_user_id_index` (`user_id`),
  CONSTRAINT `FKaq479lembdi9ujfef856c2utr` FOREIGN KEY (`problem_id`) REFERENCES `Problem` (`problem_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ProblemsTags`
--

DROP TABLE IF EXISTS `ProblemsTags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ProblemsTags` (
  `problems_tags_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `problem_id` bigint(20) NOT NULL,
  `tag_id` bigint(20) NOT NULL,
  PRIMARY KEY (`problems_tags_id`),
  UNIQUE KEY `ProblemsTags_problems_tags_id_uindex` (`problems_tags_id`),
  KEY `ProblemsTags_tag_id_index` (`tag_id`),
  KEY `ProblemsTags_problem_id_index` (`problem_id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QuestionContent`
--

DROP TABLE IF EXISTS `QuestionContent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QuestionContent` (
  `htmlContent` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `question_content_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `question_id` bigint(20) NOT NULL,
  PRIMARY KEY (`question_content_id`),
  UNIQUE KEY `QuestionContent_question_content_id_uindex` (`question_content_id`),
  UNIQUE KEY `QuestionContent_question_id_uindex` (`question_id`),
  CONSTRAINT `question_content_fk` FOREIGN KEY (`question_id`) REFERENCES `Questions` (`question_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Questions`
--

DROP TABLE IF EXISTS `Questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Questions` (
  `question_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createDate` datetime NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `activated` bit(1) NOT NULL,
  `rating` bigint(20) DEFAULT NULL,
  `right_id` bigint(20) DEFAULT '0',
  `views` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`question_id`),
  UNIQUE KEY `Questions_question_id_uindex` (`question_id`),
  KEY `FK3j5i6cc15q91yb3i7m8jp46r8` (`user_id`),
  KEY `Questions_createDate_index` (`createDate`),
  KEY `Questions_title_index` (`title`),
  KEY `Questions_rating_index` (`rating`),
  CONSTRAINT `FK3j5i6cc15q91yb3i7m8jp46r8` FOREIGN KEY (`user_id`) REFERENCES `UserInfo` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QuestionsTags`
--

DROP TABLE IF EXISTS `QuestionsTags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QuestionsTags` (
  `question_id` bigint(20) NOT NULL,
  `tag_id` bigint(20) NOT NULL,
  `questions_tags_id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`questions_tags_id`),
  UNIQUE KEY `QuestionsTags_questions_tags_id_uindex` (`questions_tags_id`),
  KEY `FKd98ukxubf51gue72m55sya7pr` (`tag_id`),
  KEY `FKp95l7s0j9fy26o06kly94s0rg` (`question_id`),
  CONSTRAINT `FKd98ukxubf51gue72m55sya7pr` FOREIGN KEY (`tag_id`) REFERENCES `Tags` (`tag_id`),
  CONSTRAINT `FKp95l7s0j9fy26o06kly94s0rg` FOREIGN KEY (`question_id`) REFERENCES `Questions` (`question_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TagCounter`
--

DROP TABLE IF EXISTS `TagCounter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TagCounter` (
  `tag_counter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `facts` bigint(20) NOT NULL DEFAULT '0',
  `questions` bigint(20) NOT NULL DEFAULT '0',
  `problems` bigint(20) NOT NULL DEFAULT '0',
  `users` bigint(20) NOT NULL DEFAULT '0',
  `articles` bigint(20) NOT NULL DEFAULT '0',
  `uses` bigint(20) NOT NULL DEFAULT '0',
  `tag_id` bigint(20) NOT NULL,
  PRIMARY KEY (`tag_counter_id`),
  UNIQUE KEY `TagCounter_tag_counter_id_uindex` (`tag_counter_id`),
  UNIQUE KEY `TagCounter_tag_id_uindex` (`tag_id`),
  KEY `TagCounter_uses_index` (`uses`),
  CONSTRAINT `tag_counter_fk` FOREIGN KEY (`tag_id`) REFERENCES `Tags` (`tag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Tags`
--

DROP TABLE IF EXISTS `Tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Tags` (
  `tag_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tag_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `activated` bit(1) NOT NULL,
  `color` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '#000000',
  `user_id` bigint(20) NOT NULL,
  `createDate` datetime NOT NULL,
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `Tags_tag_id_uindex` (`tag_id`),
  UNIQUE KEY `Tags_name_uindex` (`tag_name`),
  KEY `Tags_user_id_index` (`user_id`),
  KEY `Tags_createDate_index` (`createDate`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UserCounter`
--

DROP TABLE IF EXISTS `UserCounter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserCounter` (
  `user_counter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `articles` bigint(20) NOT NULL DEFAULT '0',
  `questions` bigint(20) NOT NULL DEFAULT '0',
  `answers` bigint(20) NOT NULL DEFAULT '0',
  `problems` bigint(20) NOT NULL DEFAULT '0',
  `decided` bigint(20) NOT NULL DEFAULT '0',
  `tags` bigint(20) NOT NULL DEFAULT '0',
  `facts` bigint(20) NOT NULL DEFAULT '0',
  `comments` bigint(20) NOT NULL DEFAULT '0',
  `notices` bigint(20) NOT NULL DEFAULT '0',
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_counter_id`),
  UNIQUE KEY `UserCounter_user_counter_id_uindex` (`user_counter_id`),
  KEY `user_counter_fk` (`user_id`),
  CONSTRAINT `user_counter_fk` FOREIGN KEY (`user_id`) REFERENCES `UserInfo` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UserExtended`
--

DROP TABLE IF EXISTS `UserExtended`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserExtended` (
  `user_extended_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `birthDate` date NOT NULL,
  `createDate` datetime DEFAULT NULL,
  `lastName` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `firstName` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `bgImage` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL,
  `about` mediumtext COLLATE utf8mb4_unicode_ci,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_extended_id`),
  UNIQUE KEY `userExtended_user_extended_id_uindex` (`user_extended_id`),
  KEY `userExtended_birthDate_index` (`birthDate`),
  KEY `user_extended_fk` (`user_id`),
  KEY `UserExtended_createDate_index` (`createDate`),
  CONSTRAINT `user_extended_fk` FOREIGN KEY (`user_id`) REFERENCES `UserInfo` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UserInbox`
--

DROP TABLE IF EXISTS `UserInbox`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserInbox` (
  `user_inbox_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `message` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `noticeType` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `createDate` datetime NOT NULL,
  `active` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`user_inbox_id`),
  UNIQUE KEY `userInbox_user_inbox_id_uindex` (`user_inbox_id`),
  KEY `inbox_user_fk` (`user_id`),
  KEY `UserInbox_createDate_index` (`createDate`),
  CONSTRAINT `inbox_user_fk` FOREIGN KEY (`user_id`) REFERENCES `UserInfo` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UserInfo`
--

DROP TABLE IF EXISTS `UserInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserInfo` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rating` bigint(20) DEFAULT '0',
  `login` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `activated` bit(1) NOT NULL DEFAULT b'0',
  `largeImagePath` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL,
  `middleImagePath` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL,
  `smallImagePath` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `userInfo_user_id_uindex` (`user_id`),
  UNIQUE KEY `UserInfo_login_uindex` (`login`),
  KEY `userInfo_rating_index` (`rating`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UsersTags`
--

DROP TABLE IF EXISTS `UsersTags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UsersTags` (
  `users_tags_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `tag_id` bigint(20) NOT NULL,
  PRIMARY KEY (`users_tags_id`),
  UNIQUE KEY `UsersTags_users_tags_id_uindex` (`users_tags_id`),
  KEY `UsersTags_tag_id_index` (`tag_id`),
  KEY `UsersTags_user_id_index` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `activate_tokens`
--

DROP TABLE IF EXISTS `activate_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activate_tokens` (
  `activate_token_Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `log_id` bigint(20) NOT NULL,
  `token` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`activate_token_Id`),
  UNIQUE KEY `activate_tokens_activate_token_Id_uindex` (`activate_token_Id`),
  UNIQUE KEY `activate_tokens_log_id_uindex` (`log_id`),
  KEY `activate_tokens_token_index` (`token`),
  CONSTRAINT `activate_tokens_fk` FOREIGN KEY (`log_id`) REFERENCES `logInfo` (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `answer_voters`
--

DROP TABLE IF EXISTS `answer_voters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `answer_voters` (
  `answer_voters_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `answer_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`answer_voters_id`),
  UNIQUE KEY `answer_voters_answer_voters_id_uindex` (`answer_voters_id`),
  KEY `answer_voters_fk` (`answer_id`),
  KEY `user_answer_voters_fk` (`user_id`),
  CONSTRAINT `answer_voters_fk` FOREIGN KEY (`answer_id`) REFERENCES `Answer` (`answer_id`),
  CONSTRAINT `user_answer_voters_fk` FOREIGN KEY (`user_id`) REFERENCES `UserInfo` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `article_comment_voters`
--

DROP TABLE IF EXISTS `article_comment_voters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article_comment_voters` (
  `article_comment_voters_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `article_comment_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`article_comment_voters_id`),
  UNIQUE KEY `article_comment_voters_article_comment_voters_id_uindex` (`article_comment_voters_id`),
  KEY `article_comment_voters_fk` (`article_comment_id`),
  KEY `user_article_comment_voters_fk` (`user_id`),
  CONSTRAINT `article_comment_voters_fk` FOREIGN KEY (`article_comment_id`) REFERENCES `ArticleComments` (`article_comment_id`),
  CONSTRAINT `user_article_comment_voters_fk` FOREIGN KEY (`user_id`) REFERENCES `UserInfo` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `article_voters`
--

DROP TABLE IF EXISTS `article_voters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article_voters` (
  `article_voters_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `article_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`article_voters_id`),
  UNIQUE KEY `article_voters_article_voters_id_uindex` (`article_voters_id`),
  KEY `article_voters_fk` (`article_id`),
  KEY `user_article_voters_fk` (`user_id`),
  CONSTRAINT `article_voters_fk` FOREIGN KEY (`article_id`) REFERENCES `ArticleInfo` (`article_id`),
  CONSTRAINT `user_article_voters_fk` FOREIGN KEY (`user_id`) REFERENCES `UserInfo` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `logInfo`
--

DROP TABLE IF EXISTS `logInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `logInfo` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pass_h` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL,
  `enable` bit(1) DEFAULT b'1',
  `login` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `email` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`log_id`),
  UNIQUE KEY `logInfo_user_id_uindex` (`log_id`),
  UNIQUE KEY `logInfo_user_id_uindex_2` (`user_id`),
  UNIQUE KEY `logInfo_email_uindex` (`email`),
  UNIQUE KEY `logInfo_login_uindex` (`login`),
  KEY `logInfo_pass_h_index` (`pass_h`),
  CONSTRAINT `log_info_fk` FOREIGN KEY (`user_id`) REFERENCES `UserInfo` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `persistent_logins`
--

DROP TABLE IF EXISTS `persistent_logins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `persistent_logins` (
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `series` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_used` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`series`),
  KEY `persistent_logins_username_index` (`username`),
  KEY `persistent_logins_token_index` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `problem_comment_voters`
--

DROP TABLE IF EXISTS `problem_comment_voters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `problem_comment_voters` (
  `problem_comment_voters_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `problem_comment_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`problem_comment_voters_id`),
  UNIQUE KEY `problem_comment_voters_problem_comment_voters_id_uindex` (`problem_comment_voters_id`),
  KEY `problem_comment_voters_fk` (`problem_comment_id`),
  KEY `user_problem_comment_voters_fk` (`user_id`),
  CONSTRAINT `problem_comment_voters_fk` FOREIGN KEY (`problem_comment_id`) REFERENCES `ProblemComment` (`problem_comment_id`),
  CONSTRAINT `user_problem_comment_voters_fk` FOREIGN KEY (`user_id`) REFERENCES `UserInfo` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `problem_counter`
--

DROP TABLE IF EXISTS `problem_counter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `problem_counter` (
  `problem_counter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `solved` bigint(20) NOT NULL DEFAULT '0',
  `attempts` bigint(20) NOT NULL DEFAULT '0',
  `problem_id` bigint(20) NOT NULL,
  PRIMARY KEY (`problem_counter_id`),
  UNIQUE KEY `problem_counter_problem_counter_id_uindex` (`problem_counter_id`),
  UNIQUE KEY `problem_counter_problem_id_uindex` (`problem_id`),
  CONSTRAINT `problem_counter_fk` FOREIGN KEY (`problem_id`) REFERENCES `Problem` (`problem_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `problem_feedback`
--

DROP TABLE IF EXISTS `problem_feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `problem_feedback` (
  `problem_feedback_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `text` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `createDate` date DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `problem_id` bigint(20) NOT NULL,
  PRIMARY KEY (`problem_feedback_id`),
  UNIQUE KEY `problem_feedback_problem_feedback_id_uindex` (`problem_feedback_id`),
  KEY `problem_feedback_fk` (`problem_id`),
  KEY `user_problem_feedback_fk` (`user_id`),
  CONSTRAINT `problem_feedback_fk` FOREIGN KEY (`problem_id`) REFERENCES `Problem` (`problem_id`),
  CONSTRAINT `user_problem_feedback_fk` FOREIGN KEY (`user_id`) REFERENCES `UserInfo` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `problem_voters`
--

DROP TABLE IF EXISTS `problem_voters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `problem_voters` (
  `problem_voters_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `problem_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`problem_voters_id`),
  UNIQUE KEY `problem_voters_problem_voters_id_uindex` (`problem_voters_id`),
  KEY `problem_voters_fk` (`problem_id`),
  KEY `user_problem_voters_fk` (`user_id`),
  CONSTRAINT `problem_voters_fk` FOREIGN KEY (`problem_id`) REFERENCES `Problem` (`problem_id`),
  CONSTRAINT `user_problem_voters_fk` FOREIGN KEY (`user_id`) REFERENCES `UserInfo` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `question_voters`
--

DROP TABLE IF EXISTS `question_voters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question_voters` (
  `question_voters_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `question_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`question_voters_id`),
  UNIQUE KEY `question_voters_question_voters_id_uindex` (`question_voters_id`),
  KEY `question_voters_fk` (`question_id`),
  KEY `user_question_voters_fk` (`user_id`),
  CONSTRAINT `question_voters_fk` FOREIGN KEY (`question_id`) REFERENCES `Questions` (`question_id`),
  CONSTRAINT `user_question_voters_fk` FOREIGN KEY (`user_id`) REFERENCES `UserInfo` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `secured_token`
--

DROP TABLE IF EXISTS `secured_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `secured_token` (
  `token_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `salt` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`token_id`),
  UNIQUE KEY `secure_tokens_token_id_uindex` (`token_id`),
  KEY `secured_token_fk` (`user_id`),
  CONSTRAINT `secured_token_fk` FOREIGN KEY (`user_id`) REFERENCES `UserInfo` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_forum_answers`
--

DROP TABLE IF EXISTS `user_forum_answers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_forum_answers` (
  `user_forum_answer_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `text` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `createDate` datetime DEFAULT NULL,
  `user_question_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_forum_answer_id`),
  UNIQUE KEY `user_forum_answers_user_forum_answer_id_uindex` (`user_forum_answer_id`),
  UNIQUE KEY `user_forum_answers_user_question_id_uindex` (`user_question_id`),
  CONSTRAINT `user_forum_question_fk` FOREIGN KEY (`user_question_id`) REFERENCES `user_questions` (`user_question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_questions`
--

DROP TABLE IF EXISTS `user_questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_questions` (
  `user_question_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `createDate` datetime NOT NULL,
  `text` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`user_question_id`),
  UNIQUE KEY `user_questions_user_question_id_uindex` (`user_question_id`),
  KEY `user_questions_createDate_index` (`createDate`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_roles` (
  `user_role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `log_id` bigint(20) NOT NULL,
  `role` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`user_role_id`),
  UNIQUE KEY `user_roles_user_role_id_uindex` (`user_role_id`),
  KEY `user_roles_user_id_index` (`log_id`),
  CONSTRAINT `user_roles_fk` FOREIGN KEY (`log_id`) REFERENCES `logInfo` (`log_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-12-04 21:09:10
