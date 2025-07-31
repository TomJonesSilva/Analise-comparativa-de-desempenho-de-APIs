CREATE DATABASE  IF NOT EXISTS `desempenho_api` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `desempenho_api`;
-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: desempenho_api
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `produtos`
--

DROP TABLE IF EXISTS `produtos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produtos` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `descricao` text,
  `preco` decimal(10,2) NOT NULL,
  `quantidade_em_estoque` int NOT NULL DEFAULT '0',
  `categoria` varchar(50) DEFAULT NULL,
  `data_criacao` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ativo` tinyint(1) NOT NULL DEFAULT '1',
  `fornecedor` varchar(100) DEFAULT NULL,
  `codigo_barras` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `codigo_barras` (`codigo_barras`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produtos`
--

LOCK TABLES `produtos` WRITE;
/*!40000 ALTER TABLE `produtos` DISABLE KEYS */;
INSERT INTO `produtos` VALUES (1,'Camiseta Branca','Camiseta 100% algodão, tamanho M',39.90,120,'Roupas','2025-07-01 14:29:44',1,'Confecções Silva','7891234560011'),(2,'Notebook Dell','Notebook i5 8GB RAM 256GB SSD',3899.00,15,'Eletrônicos','2025-07-01 14:29:44',1,'Dell Brasil','7891234560012'),(3,'Smartphone X','Celular Android com 128GB',2299.00,30,'Eletrônicos','2025-07-01 14:29:44',1,'MobileTech','7891234560013'),(4,'Cafeteira Elétrica','Cafeteira 30 xícaras inox',199.90,42,'Eletrodomésticos','2025-07-01 14:29:44',1,'Eletrosul','7891234560014'),(5,'chapeu','chapeu estiloso',10.50,50,'Roupas','2025-07-01 18:47:54',1,'ket+','12345678912345'),(6,'Produto Teste','Descrição do produto',49.99,10,'Eletrônicos','2025-07-14 15:23:14',1,'Fornecedor A','1234567890');
/*!40000 ALTER TABLE `produtos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `relatorio_vendas_mensais`
--

DROP TABLE IF EXISTS `relatorio_vendas_mensais`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `relatorio_vendas_mensais` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `ano` int NOT NULL,
  `mes` varchar(2) NOT NULL,
  `categoria` varchar(50) NOT NULL,
  `quantidade_total` int NOT NULL,
  `valor_total` decimal(12,2) NOT NULL,
  `projecao_12_meses` decimal(12,2) NOT NULL,
  `data_geracao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `relatorio_vendas_mensais`
--

LOCK TABLES `relatorio_vendas_mensais` WRITE;
/*!40000 ALTER TABLE `relatorio_vendas_mensais` DISABLE KEYS */;
INSERT INTO `relatorio_vendas_mensais` VALUES (1,2020,'07','Roupas',20,210.00,251.08,'2025-07-01 19:08:05'),(2,2020,'08','Roupas',10,399.00,477.05,'2025-07-01 19:08:05'),(3,2020,'09','Eletrônicos',5,19495.00,23308.58,'2025-07-01 19:08:05'),(4,2021,'01','Eletrônicos',14,32186.00,38482.17,'2025-07-01 19:08:05'),(5,2021,'03','Eletrodomésticos',30,5997.00,7170.12,'2025-07-01 19:08:05'),(6,2020,'08','Roupas',10,399.00,477.05,'2025-07-01 19:11:56'),(7,2020,'09','Eletrônicos',5,19495.00,23308.58,'2025-07-01 19:11:56'),(8,2021,'01','Eletrônicos',14,32186.00,38482.17,'2025-07-01 19:11:56'),(9,2021,'04','Eletrônicos',50,114950.00,137436.31,'2025-07-01 19:11:56'),(10,2021,'03','Eletrodomésticos',30,5997.00,7170.12,'2025-07-01 19:11:56'),(11,2020,'07','Roupas',20,210.00,251.08,'2025-07-01 19:11:56'),(12,2020,'08','Roupas',10,399.00,477.05,'2025-07-01 19:14:00'),(13,2022,'12','Roupas',50,1995.00,2385.26,'2025-07-01 19:14:00'),(14,2020,'09','Eletrônicos',5,19495.00,23308.58,'2025-07-01 19:14:00'),(15,2021,'01','Eletrônicos',14,32186.00,38482.17,'2025-07-01 19:14:00'),(16,2021,'04','Eletrônicos',50,114950.00,137436.31,'2025-07-01 19:14:00'),(17,2021,'03','Eletrodomésticos',30,5997.00,7170.12,'2025-07-01 19:14:00'),(18,2020,'07','Roupas',20,210.00,251.08,'2025-07-01 19:14:00'),(19,2020,'08','Roupas',10,399.00,477.05,'2025-07-01 19:16:02'),(20,2022,'12','Roupas',70,2205.00,2636.34,'2025-07-01 19:16:02'),(21,2020,'09','Eletrônicos',5,19495.00,23308.58,'2025-07-01 19:16:02'),(22,2021,'01','Eletrônicos',14,32186.00,38482.17,'2025-07-01 19:16:02'),(23,2021,'04','Eletrônicos',50,114950.00,137436.31,'2025-07-01 19:16:02'),(24,2021,'03','Eletrodomésticos',30,5997.00,7170.12,'2025-07-01 19:16:02'),(25,2020,'07','Roupas',20,210.00,251.08,'2025-07-01 19:16:02'),(26,2020,'08','Roupas',10,399.00,477.05,'2025-07-02 12:18:09'),(27,2022,'12','Roupas',70,2205.00,2636.34,'2025-07-02 12:18:10'),(28,2020,'09','Eletrônicos',5,19495.00,23308.58,'2025-07-02 12:18:10'),(29,2021,'01','Eletrônicos',14,32186.00,38482.17,'2025-07-02 12:18:10'),(30,2021,'04','Eletrônicos',50,114950.00,137436.31,'2025-07-02 12:18:10'),(31,2021,'03','Eletrodomésticos',30,5997.00,7170.12,'2025-07-02 12:18:10'),(32,2020,'07','Roupas',20,210.00,251.08,'2025-07-02 12:18:10'),(33,2020,'08','Roupas',10,399.00,477.05,'2025-07-02 13:02:58'),(34,2022,'12','Roupas',70,2205.00,2636.34,'2025-07-02 13:02:58'),(35,2020,'09','Eletrônicos',5,19495.00,23308.58,'2025-07-02 13:02:58'),(36,2021,'01','Eletrônicos',14,32186.00,38482.17,'2025-07-02 13:02:58'),(37,2021,'04','Eletrônicos',50,114950.00,137436.31,'2025-07-02 13:02:58'),(38,2021,'03','Eletrodomésticos',30,5997.00,7170.12,'2025-07-02 13:02:58'),(39,2020,'07','Roupas',20,210.00,251.08,'2025-07-02 13:02:58'),(40,2020,'08','Roupas',10,399.00,477.05,'2025-07-14 15:31:23'),(41,2022,'12','Roupas',70,2205.00,2636.34,'2025-07-14 15:31:23'),(42,2020,'07','Roupas',20,210.00,251.08,'2025-07-14 15:31:23'),(43,2020,'09','Eletrônicos',5,19495.00,23308.58,'2025-07-14 15:31:23'),(44,2021,'03','Eletrodomésticos',30,5997.00,7170.12,'2025-07-14 15:31:23'),(45,2021,'01','Eletrônicos',14,32186.00,38482.17,'2025-07-14 15:31:23'),(46,2021,'04','Eletrônicos',50,114950.00,137436.31,'2025-07-14 15:31:23'),(47,2025,'07','Eletrônicos',3,149.97,179.31,'2025-07-14 15:31:23');
/*!40000 ALTER TABLE `relatorio_vendas_mensais` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vendas`
--

DROP TABLE IF EXISTS `vendas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vendas` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `produto_id` int unsigned NOT NULL,
  `quantidade` int NOT NULL,
  `preco_unitario` decimal(10,2) NOT NULL,
  `data_venda` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `produto_id` (`produto_id`),
  CONSTRAINT `vendas_ibfk_1` FOREIGN KEY (`produto_id`) REFERENCES `produtos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vendas`
--

LOCK TABLES `vendas` WRITE;
/*!40000 ALTER TABLE `vendas` DISABLE KEYS */;
INSERT INTO `vendas` VALUES (1,5,20,10.50,'2020-07-23'),(2,1,10,39.90,'2020-08-23'),(3,2,5,3899.00,'2020-09-23'),(4,3,14,2299.00,'2021-01-23'),(5,4,30,199.90,'2021-03-23'),(6,3,50,2299.00,'2021-04-23'),(7,1,50,39.90,'2022-12-23'),(8,5,20,10.50,'2022-12-23'),(9,6,3,49.99,'2025-07-11');
/*!40000 ALTER TABLE `vendas` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-31 11:22:28
