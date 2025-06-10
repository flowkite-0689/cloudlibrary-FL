-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: cloudlibrary
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book` (
  `book_id` int NOT NULL AUTO_INCREMENT COMMENT '图书编号',
  `classify_id` int DEFAULT NULL COMMENT '图书类型编号',
  `book_name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '图书名称',
  `book_isbn` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '图书标准ISBN编号',
  `book_press` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '图书出版社',
  `book_author` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '图书作者',
  `book_pagination` int DEFAULT NULL COMMENT '图书页数',
  `book_price` decimal(10,2) DEFAULT NULL,
  `book_status` varchar(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记(0:未删除 1:已删除)',
  PRIMARY KEY (`book_id`) USING BTREE,
  KEY `book_classify` (`classify_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES (1,4,'被讨厌的勇气','9787111495482','机械工业出版社','王宁栋',284,39.99,'1','2025-05-28 21:20:36','2025-05-30 09:02:46',0),(2,2,'我与地坛','9787020084357','人民文学出版社','史铁生\r\n',240,29.00,'0','2025-05-28 21:20:36','2025-05-28 21:20:36',0),(4,4,'命运','9787533969608','浙江文艺出版社','\r\n蔡崇达\r\n',368,25.00,'0','2025-05-28 21:20:36','2025-05-28 21:20:36',0),(5,1,'一句顶一万句','9787536097261','\r\n花城出版社','刘震云',620,68.00,'0','2025-05-28 21:20:36','2025-05-28 21:20:36',0),(6,5,'狼道','9787555711629','成都地图出版社','\r\n金国强',183,19.00,'0','2025-05-28 21:20:36','2025-05-30 19:50:30',0),(7,5,'塔木德','9787513909419','新世界出版社','张艳玲',368,42.00,'0','2025-05-28 21:20:36','2025-05-28 21:20:36',0),(8,1,'\r\n平凡的世界','9787530211267','中国文联出版公司','\r\n路遥',1631,49.00,'0','2025-05-28 21:20:36','2025-05-28 21:20:36',0),(10,1,'围城(精)','9787020024759','人民文学出版社','钱钟书',472,19.00,'0','2025-05-28 21:20:36','2025-05-28 21:20:36',0),(12,1,'\r\n额尔古纳河右岸','9787020139590','人民文学出版社','\r\n迟子建',266,32.00,'0','2025-05-28 21:20:36','2025-05-28 21:20:36',0),(13,2,'一个人的村庄','9787544787765','译林出版社','刘亮程',464,68.00,'0','2025-05-28 21:20:36','2025-06-02 04:50:54',0),(14,3,'解忧杂货店','9787544270878','\r\n南海出版公司','东野圭吾',291,34.00,'0','2025-05-28 21:20:36','2025-05-30 19:50:30',0),(15,2,'朱自清散文','9787020167296','人民文学出版社','朱自清',344,36.00,'0','2025-05-28 21:20:36','2025-05-28 21:20:36',0),(16,2,'冰心散文','\r\n9787020167166','人民文学出版社','冰心',424,42.00,'0','2025-05-28 21:20:36','2025-05-28 21:20:36',0),(17,6,'采桑子','9787530218754','北京出版集团北京十月文艺出版社','叶广芩',456,60.00,'0','2025-05-28 21:20:36','2025-05-28 21:20:36',0),(18,1,'活着','9787530221532','作家出版社','余华',191,45.00,'0','2025-05-28 21:20:36','2025-05-28 21:20:36',0),(19,3,'云边有个小卖部','9787540487645','湖南文艺出版社','张嘉佳',306,42.00,'0','2025-05-28 21:20:36','2025-05-28 21:20:36',0),(20,3,'稻草人','9787540486860','湖南文艺出版社','叶圣陶',281,42.00,'1','2025-05-28 21:20:36','2025-06-02 04:50:54',0),(21,NULL,'Spring Boot实战','978-7-121-35886-3',NULL,NULL,NULL,68.50,'0','2025-05-29 15:13:43','2025-05-29 15:13:43',0),(22,NULL,'Spring实战（第6版）','978-7-115-55864-0',NULL,NULL,NULL,99.80,'0','2025-05-29 15:17:28','2025-05-29 20:56:39',1),(23,NULL,'Spring实战（第7版）','978-7-115-55864-1',NULL,NULL,NULL,99.80,'0','2025-05-29 15:33:19','2025-06-04 00:38:48',1),(24,NULL,'Spring Boot实战','978-7-121-35886-7',NULL,'张三',NULL,68.50,'0','2025-05-29 15:37:22','2025-05-29 15:37:22',0),(26,NULL,'Spring Boot实战（第6版）','978-7-121-35886-9','电子工业出版社','克雷格·沃斯',450,99.80,'0','2025-05-29 15:42:28','2025-05-29 15:42:28',0),(27,NULL,'Spring Boot实战','9787121262027','人民邮电出版社','Craig Walls',NULL,89.00,'0','2025-05-30 08:58:58','2025-05-30 08:58:58',0),(28,NULL,'Spring Boot实战（第6版）','978-7-121-35816-9','电子工业出版社','克雷格·沃斯',450,99.80,'0','2025-05-30 15:52:56','2025-05-30 15:52:56',0),(29,NULL,'Spring Boot实战','9787121262017','人民邮电出版社','Craig Walls',NULL,89.00,'0','2025-06-04 00:56:20','2025-06-04 00:56:20',0);
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `record`
--

DROP TABLE IF EXISTS `record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `record` (
  `record_id` int NOT NULL AUTO_INCREMENT COMMENT '借阅记录id',
  `record_borrowtime` datetime(6) DEFAULT NULL COMMENT '图书借阅时间',
  `record_returntime` datetime(6) DEFAULT NULL COMMENT '图书归还时间',
  `book_id` int DEFAULT NULL COMMENT '图书id',
  `user_id` int DEFAULT NULL COMMENT '用户id',
  `record_status` int NOT NULL DEFAULT '1' COMMENT '借阅状态:1-借阅中,2-已归还,3-已逾期',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标志:0-未删除,1-已删除',
  PRIMARY KEY (`record_id`) USING BTREE,
  KEY `record_book` (`book_id`) USING BTREE,
  KEY `record_user` (`user_id`) USING BTREE,
  CONSTRAINT `record_book` FOREIGN KEY (`book_id`) REFERENCES `book` (`book_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `record_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `record`
--

LOCK TABLES `record` WRITE;
/*!40000 ALTER TABLE `record` DISABLE KEYS */;
INSERT INTO `record` VALUES (12,'2025-05-11 00:00:00.000000','2025-05-12 00:00:00.000000',20,2,1,0),(23,'2025-06-02 14:30:00.000000','2025-06-13 10:00:00.000000',23,2,2,0),(32,'2023-10-01 09:00:00.000000',NULL,13,2,1,1),(33,'2025-06-02 14:30:00.000000',NULL,20,2,1,0);
/*!40000 ALTER TABLE `record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `nickname` varchar(50) NOT NULL COMMENT '用户昵称',
  `email` varchar(100) NOT NULL COMMENT '用户邮箱（登录账号）',
  `gender` varchar(10) DEFAULT NULL COMMENT '用户性别：MALE/FEMALE/UNKNOWN',
  `role` varchar(50) NOT NULL COMMENT '用户角色：ADMIN/USER',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '用户状态：0-正常, 1-禁用',
  `avatar` varchar(255) DEFAULT NULL COMMENT '用户头像URL',
  `username` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `password` varchar(100) NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标志:0-未删除,1-已删除',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `uk_email` (`email`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'新昵称','newemail@example.com','MALE','ADMIN',0,'https://example.com/new-avatar.jpg',NULL,'2025-05-30 16:56:04','2025-06-03 12:09:37','newpassword123',0),(2,'汽水','sujunkang@itcast.cn','男','USER',0,NULL,'苏俊康','2025-05-30 16:56:04','2025-05-30 18:00:20','',0),(3,'杨泽钦','yangzeqin@itcast.cn','男','USER',0,NULL,NULL,'2025-05-30 16:56:04','2025-05-30 16:56:13','',0),(4,'梁景德','liangjingde@itcast.cn','男','USER',0,NULL,NULL,'2025-05-30 16:56:04','2025-05-30 16:56:13','',0),(5,'王平创','wangpingchuang@itcast.cn','男','USER',0,NULL,NULL,'2025-05-30 16:56:04','2025-05-30 16:56:13','',0),(6,'test_user','test@example.com','UNKNOWN','USER',0,NULL,'test_user','2025-06-02 21:53:10','2025-06-02 21:53:10','password123',0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-04  1:17:55
