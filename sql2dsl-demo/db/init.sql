CREATE DATABASE `sql2dsl`;
USE `sql2dsl`;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username` varchar(20) DEFAULT NULL COMMENT '姓名',
    `account` varchar(20) DEFAULT NULL COMMENT '账号',
    `age` int DEFAULT NULL COMMENT '年龄',
    `sex` int DEFAULT NULL COMMENT '性别',
    `address` varchar(50) DEFAULT NULL COMMENT '地址',
    `create_time` TIMESTAMP DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) COMMENT='用户信息表';