CREATE DATABASE `test`;

USE `test`;

DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username` varchar(10) DEFAULT NULL COMMENT '用户名称',
    `sex` char(1) DEFAULT NULL COMMENT '性别',
    `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
    PRIMARY KEY (`id`)
) COMMENT='用户表';

INSERT INTO `t_user`(`username`, `sex`, `phone`) VALUES ('张三', '1', '13500000000');
INSERT INTO `t_user`(`username`, `sex`, `phone`) VALUES ( '李四', '0', '18311111111');
INSERT INTO `t_user`(`username`, `sex`, `phone`) VALUES ( '王五', '1', '15522222222');