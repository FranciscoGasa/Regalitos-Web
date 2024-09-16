-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         11.4.2-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para regalitosdb
CREATE DATABASE IF NOT EXISTS `regalitosdb` /*!40100 DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci */;
USE `regalitosdb`;

-- Volcando estructura para tabla regalitosdb.friend
CREATE TABLE IF NOT EXISTS `friend` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(255) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3uu8s7yyof1qmenthngm24hry` (`user_id`),
  CONSTRAINT `FK3uu8s7yyof1qmenthngm24hry` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=153 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla regalitosdb.friend: ~6 rows (aproximadamente)
INSERT INTO `friend` (`id`, `nickname`, `user_id`) VALUES
	(1, 'pedros', 1),
	(2, 'jefrey', 1),
	(4, 'ramon', 1),
	(52, 'silvia', 2),
	(53, 'mati', 2),
	(102, 'aaron', 1);

-- Volcando estructura para tabla regalitosdb.friend_seq
CREATE TABLE IF NOT EXISTS `friend_seq` (
  `next_not_cached_value` bigint(21) NOT NULL,
  `minimum_value` bigint(21) NOT NULL,
  `maximum_value` bigint(21) NOT NULL,
  `start_value` bigint(21) NOT NULL COMMENT 'start value when sequences is created or value if RESTART is used',
  `increment` bigint(21) NOT NULL COMMENT 'increment value',
  `cache_size` bigint(21) unsigned NOT NULL,
  `cycle_option` tinyint(1) unsigned NOT NULL COMMENT '0 if no cycles are allowed, 1 if the sequence should begin a new cycle when maximum_value is passed',
  `cycle_count` bigint(21) NOT NULL COMMENT 'How many cycles have been done'
) ENGINE=InnoDB SEQUENCE=1;

-- Volcando datos para la tabla regalitosdb.friend_seq: ~1 rows (aproximadamente)
INSERT INTO `friend_seq` (`next_not_cached_value`, `minimum_value`, `maximum_value`, `start_value`, `increment`, `cache_size`, `cycle_option`, `cycle_count`) VALUES
	(251, 1, 9223372036854775806, 1, 50, 0, 0, 0);

-- Volcando estructura para tabla regalitosdb.gift
CREATE TABLE IF NOT EXISTS `gift` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `estado_regalo` tinyint(4) DEFAULT NULL CHECK (`estado_regalo` between 0 and 2),
  `name` varchar(255) NOT NULL,
  `price` double NOT NULL,
  `friend_id` bigint(20) NOT NULL,
  `giftlist_id` bigint(20) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKeslu0hfpryxq4ocw4ge1c6myt` (`friend_id`),
  KEY `FKourxuavus5fod36hayigbw6j3` (`giftlist_id`),
  CONSTRAINT `FKeslu0hfpryxq4ocw4ge1c6myt` FOREIGN KEY (`friend_id`) REFERENCES `friend` (`id`),
  CONSTRAINT `FKourxuavus5fod36hayigbw6j3` FOREIGN KEY (`giftlist_id`) REFERENCES `gift_list` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=203 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla regalitosdb.gift: ~6 rows (aproximadamente)
INSERT INTO `gift` (`id`, `estado_regalo`, `name`, `price`, `friend_id`, `giftlist_id`, `url`) VALUES
	(1, 1, 'Sagrada', 20, 102, 1, 'https://www.amazon.es/Devir-Sagrada-Juego-Mesa-Castellano/dp/B07G4B3TQS'),
	(2, 1, '7wonder', 30, 4, 1, 'https://jugamosuna.es/tienda/8918/comprar-7-wonders-nueva-edicion-barato.html'),
	(3, 0, 'Zombicide', 60, 2, 1, 'https://dungeonmarvels.com/zombicide-segunda-edicion-49844.html'),
	(4, 2, 'Virus', 12, 4, 1, 'https://frikadas.es/virus-juego-de-cartas/'),
	(152, 2, 'Libro', 5, 4, 102, NULL),
	(202, 1, 'lapiz', 3, 102, 102, NULL);

-- Volcando estructura para tabla regalitosdb.gift_list
CREATE TABLE IF NOT EXISTS `gift_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `coste_total` double NOT NULL,
  `estado_lista` tinyint(4) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKr5l5lqouv3agqay743h5w1abv` (`user_id`),
  CONSTRAINT `FKr5l5lqouv3agqay743h5w1abv` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla regalitosdb.gift_list: ~3 rows (aproximadamente)
INSERT INTO `gift_list` (`id`, `coste_total`, `estado_lista`, `name`, `user_id`) VALUES
	(1, 122, 1, 'Reyes 2022', 1),
	(52, 0, NULL, 'Reyes 2026', 1),
	(102, 8, 0, 'Navidad 2012', 1);

-- Volcando estructura para tabla regalitosdb.gift_list_seq
CREATE TABLE IF NOT EXISTS `gift_list_seq` (
  `next_not_cached_value` bigint(21) NOT NULL,
  `minimum_value` bigint(21) NOT NULL,
  `maximum_value` bigint(21) NOT NULL,
  `start_value` bigint(21) NOT NULL COMMENT 'start value when sequences is created or value if RESTART is used',
  `increment` bigint(21) NOT NULL COMMENT 'increment value',
  `cache_size` bigint(21) unsigned NOT NULL,
  `cycle_option` tinyint(1) unsigned NOT NULL COMMENT '0 if no cycles are allowed, 1 if the sequence should begin a new cycle when maximum_value is passed',
  `cycle_count` bigint(21) NOT NULL COMMENT 'How many cycles have been done'
) ENGINE=InnoDB SEQUENCE=1;

-- Volcando datos para la tabla regalitosdb.gift_list_seq: ~1 rows (aproximadamente)
INSERT INTO `gift_list_seq` (`next_not_cached_value`, `minimum_value`, `maximum_value`, `start_value`, `increment`, `cache_size`, `cycle_option`, `cycle_count`) VALUES
	(201, 1, 9223372036854775806, 1, 50, 0, 0, 0);

-- Volcando estructura para tabla regalitosdb.gift_seq
CREATE TABLE IF NOT EXISTS `gift_seq` (
  `next_not_cached_value` bigint(21) NOT NULL,
  `minimum_value` bigint(21) NOT NULL,
  `maximum_value` bigint(21) NOT NULL,
  `start_value` bigint(21) NOT NULL COMMENT 'start value when sequences is created or value if RESTART is used',
  `increment` bigint(21) NOT NULL COMMENT 'increment value',
  `cache_size` bigint(21) unsigned NOT NULL,
  `cycle_option` tinyint(1) unsigned NOT NULL COMMENT '0 if no cycles are allowed, 1 if the sequence should begin a new cycle when maximum_value is passed',
  `cycle_count` bigint(21) NOT NULL COMMENT 'How many cycles have been done'
) ENGINE=InnoDB SEQUENCE=1;

-- Volcando datos para la tabla regalitosdb.gift_seq: ~1 rows (aproximadamente)
INSERT INTO `gift_seq` (`next_not_cached_value`, `minimum_value`, `maximum_value`, `start_value`, `increment`, `cache_size`, `cycle_option`, `cycle_count`) VALUES
	(301, 1, 9223372036854775806, 1, 50, 0, 0, 0);

-- Volcando estructura para tabla regalitosdb.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `firstname` varchar(255) NOT NULL,
  `hashed_password` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `role` tinyint(4) DEFAULT NULL CHECK (`role` between 0 and 1),
  `confirmation_token` varchar(255) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla regalitosdb.user: ~8 rows (aproximadamente)
INSERT INTO `user` (`id`, `email`, `firstname`, `hashed_password`, `lastname`, `username`, `role`, `confirmation_token`, `enabled`) VALUES
	(1, 'fran@fran.es', 'fran', '$2a$10$5US6a1GjrSDFr.sn10pJ0.muN5tvJCouMgoWrBJHACrvVjCNerDgm', 'fran', 'fran', 0, NULL, b'0'),
	(2, 'sol@sol.com', 'sol', '$2a$10$jKR0BslIeDRNIKqUgwloiO1mGepl5LivAeeGdDwD0X2BA9G42LLgC', 'sol', 'sol', 1, NULL, b'0'),
	(3, 'pepe@pepe.es', 'pepe', 'pepe', 'pepe', 'pepe', 0, NULL, b'0'),
	(102, 'juan@juan.es', 'juan', '$2a$10$K5ClNP8MAH8IWlgvVdbS.eqzKhTKRR.lVH76DqTIsIFnil2ytRfOq', 'juan', 'juan', 0, NULL, b'0'),
	(152, 'we@we.es', 'wey', '$2a$10$mnt..p0sWiW1Xv2tor1qn.qPtyl9KkikNmPe1tMWbx5yh1MzW8A.C', 'wey', 'wey', 0, NULL, b'0'),
	(302, 'xxfran322xx@gmail.com', 'cr7', '$2a$10$uWLf54g5eXY1N/bV/ltVZuwk3/VU/j0aRzrRFrTP0Jtq9rfCnyX9K', 'cr7', 'cr7', 0, 'aefdf844-6588-45c5-9c54-b1c80324db61', b'0'),
	(352, 'xfrangsx@gmail.com', 'messi', '$2a$10$pBPw/ds8KeVGAnIT/EQK7OyENuD/aQaly1l8PnUgmxWlcVkxw0vCi', 'messi', 'messi', 0, '6c734750-98ac-437d-9ffb-10f8282ed94a', b'0');

-- Volcando estructura para tabla regalitosdb.user_roles
CREATE TABLE IF NOT EXISTS `user_roles` (
  `user_id` bigint(20) NOT NULL,
  `roles` enum('USER','ADMIN') DEFAULT NULL,
  KEY `FK55itppkw3i07do3h7qoclqd4k` (`user_id`),
  CONSTRAINT `FK55itppkw3i07do3h7qoclqd4k` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla regalitosdb.user_roles: ~0 rows (aproximadamente)
INSERT INTO `user_roles` (`user_id`, `roles`) VALUES
	(1, 'USER');

-- Volcando estructura para tabla regalitosdb.user_seq
CREATE TABLE IF NOT EXISTS `user_seq` (
  `next_not_cached_value` bigint(21) NOT NULL,
  `minimum_value` bigint(21) NOT NULL,
  `maximum_value` bigint(21) NOT NULL,
  `start_value` bigint(21) NOT NULL COMMENT 'start value when sequences is created or value if RESTART is used',
  `increment` bigint(21) NOT NULL COMMENT 'increment value',
  `cache_size` bigint(21) unsigned NOT NULL,
  `cycle_option` tinyint(1) unsigned NOT NULL COMMENT '0 if no cycles are allowed, 1 if the sequence should begin a new cycle when maximum_value is passed',
  `cycle_count` bigint(21) NOT NULL COMMENT 'How many cycles have been done'
) ENGINE=InnoDB SEQUENCE=1;

-- Volcando datos para la tabla regalitosdb.user_seq: ~1 rows (aproximadamente)
INSERT INTO `user_seq` (`next_not_cached_value`, `minimum_value`, `maximum_value`, `start_value`, `increment`, `cache_size`, `cycle_option`, `cycle_count`) VALUES
	(601, 1, 9223372036854775806, 1, 50, 0, 0, 0);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
