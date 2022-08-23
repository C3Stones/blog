CREATE DATABASE `mpj`;
USE `mpj`;

CREATE TABLE IF NOT EXISTS `t_user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(10) COMMENT '用户名称',
  `sex` char(1) COMMENT '性别',
  `phone` varchar(11) COMMENT '手机号',
  PRIMARY KEY (`id`)
) COMMENT = '用户信息表';

INSERT INTO `t_user`(`id`, `username`, `sex`, `phone`) VALUES (1, '张三', '1', '13500000000');
INSERT INTO `t_user`(`id`, `username`, `sex`, `phone`) VALUES (2, '李四', '0', '18311111111');

CREATE TABLE IF NOT EXISTS `t_shipping_address`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int COMMENT '用户ID',
  `address` varchar(255) COMMENT '地址',
  `is_default` char(1) DEFAULT 'f' COMMENT '是否默认',
  PRIMARY KEY (`id`),
  INDEX `idx_user_id`(`user_id`) USING BTREE
) COMMENT = '收货地址表';

INSERT INTO `t_shipping_address`(`id`, `user_id`, `address`, `is_default`) VALUES (1, 1, '陕西省西安市雁塔区', 't');
INSERT INTO `t_shipping_address`(`id`, `user_id`, `address`, `is_default`) VALUES (2, 1, '陕西省西安市未央区', 'f');
INSERT INTO `t_shipping_address`(`id`, `user_id`, `address`, `is_default`) VALUES (3, 2, '陕西省西安市莲湖区', 't');