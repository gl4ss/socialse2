-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: authentication
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `comment_date` datetime(6) NOT NULL,
  `content` varchar(255) NOT NULL,
  `user_id` bigint NOT NULL,
  `post_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKh4c7lvsc298whoyd4w9ta25cr` (`post_id`),
  CONSTRAINT `FKh4c7lvsc298whoyd4w9ta25cr` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `follows`
--

DROP TABLE IF EXISTS `follows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `follows` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `follower_id` bigint DEFAULT NULL,
  `following_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `follows`
--

LOCK TABLES `follows` WRITE;
/*!40000 ALTER TABLE `follows` DISABLE KEYS */;
INSERT INTO `follows` VALUES (1,2,1),(2,1,2),(3,1,5),(4,1,7),(5,7,2),(6,5,1),(7,102,2);
/*!40000 ALTER TABLE `follows` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `posts`
--

DROP TABLE IF EXISTS `posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `posts` (
  `post_id` int NOT NULL AUTO_INCREMENT,
  `image_url` varchar(150) DEFAULT NULL,
  `status` varchar(1000) DEFAULT NULL,
  `id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `content` text,
  `create_at` datetime(6) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`post_id`),
  KEY `FK5sco241pb0dja052701tgpanj` (`id`),
  KEY `FK5lidm6cqbc7u4xhqpxm898qme` (`user_id`),
  CONSTRAINT `FK5lidm6cqbc7u4xhqpxm898qme` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK5sco241pb0dja052701tgpanj` FOREIGN KEY (`id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `posts`
--

LOCK TABLES `posts` WRITE;
/*!40000 ALTER TABLE `posts` DISABLE KEYS */;
INSERT INTO `posts` VALUES (2,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRuoAtsQeJvlc943lH4Jeo-xNKoWCee_GnvEA&usqp=CAU','Nothing beats a good book on a rainy day.',NULL,2,NULL,NULL,NULL),(3,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRuoAtsQeJvlc943lH4Jeo-xNKoWCee_GnvEA&usqp=CAU','So grateful for this amazing community!',NULL,5,NULL,NULL,NULL),(4,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRuoAtsQeJvlc943lH4Jeo-xNKoWCee_GnvEA&usqp=CAU','Exploring the city has never been more fun!',NULL,7,NULL,NULL,NULL),(5,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRuoAtsQeJvlc943lH4Jeo-xNKoWCee_GnvEA&usqp=CAU','Can\'t believe how fast my little one is growing up.',NULL,1,NULL,NULL,NULL),(6,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRuoAtsQeJvlc943lH4Jeo-xNKoWCee_GnvEA&usqp=CAU','Throwback to an unforgettable adventure.',NULL,2,NULL,NULL,NULL),(7,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRuoAtsQeJvlc943lH4Jeo-xNKoWCee_GnvEA&usqp=CAU','A day well spent with family.',NULL,7,NULL,NULL,NULL),(8,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRuoAtsQeJvlc943lH4Jeo-xNKoWCee_GnvEA&usqp=CAU','Feeling motivated after a great workout!',NULL,5,NULL,NULL,NULL),(9,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRuoAtsQeJvlc943lH4Jeo-xNKoWCee_GnvEA&usqp=CAU','Delicious homemade meal #Foodie',NULL,1,NULL,NULL,NULL),(10,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRuoAtsQeJvlc943lH4Jeo-xNKoWCee_GnvEA&usqp=CAU','The journey is the destination.',NULL,2,NULL,NULL,NULL),(11,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRuoAtsQeJvlc943lH4Jeo-xNKoWCee_GnvEA&usqp=CAU','Cherishing these quiet moments of reflection.',NULL,1,NULL,NULL,NULL),(12,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRuoAtsQeJvlc943lH4Jeo-xNKoWCee_GnvEA&usqp=CAU','When nature calls, you answer!',NULL,2,NULL,NULL,NULL),(13,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRuoAtsQeJvlc943lH4Jeo-xNKoWCee_GnvEA&usqp=CAU','Dreaming of my next vacation spot.',NULL,102,NULL,NULL,NULL),(14,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRuoAtsQeJvlc943lH4Jeo-xNKoWCee_GnvEA&usqp=CAU','Life is better with friends.',NULL,152,NULL,NULL,NULL),(15,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRuoAtsQeJvlc943lH4Jeo-xNKoWCee_GnvEA&usqp=CAU','Starting the day with a smile :)',NULL,202,NULL,NULL,NULL),(16,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRuoAtsQeJvlc943lH4Jeo-xNKoWCee_GnvEA&usqp=CAU','Capturing the beauty of everyday life.',NULL,252,NULL,NULL,NULL),(17,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRuoAtsQeJvlc943lH4Jeo-xNKoWCee_GnvEA&usqp=CAU','It\'s the little things in life that matter most.',NULL,302,NULL,NULL,NULL),(18,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRuoAtsQeJvlc943lH4Jeo-xNKoWCee_GnvEA&usqp=CAU','Finding peace in the midst of chaos.',NULL,303,NULL,NULL,NULL),(19,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRuoAtsQeJvlc943lH4Jeo-xNKoWCee_GnvEA&usqp=CAU','A night out on the town!',NULL,304,NULL,NULL,NULL),(20,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRuoAtsQeJvlc943lH4Jeo-xNKoWCee_GnvEA&usqp=CAU','Embracing the art of slow living.',NULL,304,NULL,NULL,NULL);
/*!40000 ALTER TABLE `posts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reaction`
--

DROP TABLE IF EXISTS `reaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reaction` (
  `reaction_id` int NOT NULL AUTO_INCREMENT,
  `post_id` int NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`reaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reaction`
--

LOCK TABLES `reaction` WRITE;
/*!40000 ALTER TABLE `reaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `reaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL,
  `dob` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `gender` int DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_no` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'2003-09-21',NULL,'do van viet edit 1',1,NULL,NULL,NULL),(2,'2222-02-12','viet2@gmail.com','viet2',2,'21092003','0359461556',NULL),(5,'2222-02-12','viet3@gmail.com','viet3',2,'21092003','0359461556',NULL),(7,NULL,'viet4@gmail.com','viet4',1,'21092003','21092003','USER'),(102,'2003-02-10','viet5@gmail.com','viet5@gmail.com',1,'$2a$10$MecySBS47qC2aZc4d0IPDeWx9J/NuJAT03WiYuSBEPqXCR.1Xbc8C','0359461556','USER'),(152,'2003-02-10','admin@gmail.com','viet admin',1,'$2a$10$AQCFZGdZSp.kVsRz7m.wNuKNnRsxeBFrakQWhFNvtSqMyQbRJNYu2','0359461556','ADMIN'),(202,NULL,'user@gmail.com','viet user',1,'$2a$10$zMBuhl4.l089q8qPzTmo4.A.un6Wnqoa7qzjQVxM9rayuo9R.BBSW','0359461556','USER'),(252,NULL,'new@gmail.com','vietnew',1,'$2a$10$ozrsAznK4ubsEMwRFtcXm.ZNIiV4JIy98nXTPazBttpQKhElPvNrm','0359461556',NULL),(302,NULL,'v6@gmail.com','v6',2,'$2a$10$aIZdPscrkOeTRsxU5yaM8OjH1L7qD0lyzUwdAoSr6HKzE977WM0c2','0359461556',NULL),(303,NULL,'v7@gmail.com','v7',1,'$2a$10$kBYFo0afjdEAhUPrQ2PiQO8cKjikoWSkqPcDIActW4VGGOfdlca1W','0359461556',NULL),(304,NULL,'v8@gmail.com','v8',1,'$2a$10$njTx5u1IlxUwtYrKbmoiZOlCzcjdUWIuR6jfCxJ1D3iZSif20VGE.','0359461556',NULL),(305,NULL,'v9@gmail.com','v9',1,'$2a$10$XvIXFz5hYH/8Paycxnatbu7bOQYKW2bD7xqPnu4rsgztGjFYVugyq','0359461556',NULL),(306,NULL,'v10@gmail.com','v10',1,'$2a$10$VgkJq7tR5BrXUFtO9.4WB.Cs5k7xfeibpsyI6VfotZNXJXLO9tOr6','0359461556',NULL),(307,NULL,'v11@gmail.com','v11',1,'$2a$10$nUrEutVzg6/UBXCAbnld3uTuLvKiEvqB.TsqQV6kBDKCiHw6qvloK','0359461556',NULL),(308,NULL,'v12@gmail.com','v12',1,'$2a$10$ONQNHQrkk9/7I79Np1iO0.2EULCg2SRPHzx9L2EnDw.6Y5jq0UZkC','0359461556',NULL),(309,NULL,'v13@gmail.com','v13',1,'$2a$10$scr/18oCb1dnux1HEoTIC.DRyG0kLQAB6B2ehBDlenv9HEH5D0nJq','0359461556',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_seq`
--

DROP TABLE IF EXISTS `users_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_seq`
--

LOCK TABLES `users_seq` WRITE;
/*!40000 ALTER TABLE `users_seq` DISABLE KEYS */;
INSERT INTO `users_seq` VALUES (401);
/*!40000 ALTER TABLE `users_seq` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-15 14:38:49
