#创建数据库db0
CREATE DATABASE IF NOT EXISTS `db0` DEFAULT CHARACTER SET utf8;

USE `db0`;

DROP TABLE IF EXISTS `t_user_0`;
CREATE TABLE `t_user_0` (
  `id` int(11) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `org_code` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_user_1`;
CREATE TABLE `t_user_1` (
  `id` int(11) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `org_code` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_user_2`;
CREATE TABLE `t_user_2` (
  `id` int(11) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `org_code` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#创建数据库db1
CREATE DATABASE IF NOT EXISTS `db1` DEFAULT CHARACTER SET utf8 ;

USE `db1`;

DROP TABLE IF EXISTS `t_user_0`;
CREATE TABLE `t_user_0` (
  `id` int(11) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `org_code` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_user_1`;
CREATE TABLE `t_user_1` (
  `id` int(11) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `org_code` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_user_2`;
CREATE TABLE `t_user_2` (
  `id` int(11) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `org_code` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#创建数据库db2
CREATE DATABASE IF NOT EXISTS `db2` DEFAULT CHARACTER SET utf8;

USE `db2`;

DROP TABLE IF EXISTS `t_user_0`;
CREATE TABLE `t_user_0` (
  `id` int(11) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `org_code` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_user_1`;
CREATE TABLE `t_user_1` (
  `id` int(11) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `org_code` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_user_2`;
CREATE TABLE `t_user_2` (
  `id` int(11) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `org_code` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;