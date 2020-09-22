SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_ja_user
-- ----------------------------
DROP TABLE IF EXISTS `t_ja_user`;
CREATE TABLE `t_ja_user`  (
  `uuid` varchar(64) NOT NULL COMMENT '用户第三方系统的唯一id',
  `username` varchar(100) NULL DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(100) NULL DEFAULT NULL COMMENT '用户昵称',
  `avatar` varchar(255) NULL DEFAULT NULL COMMENT '用户头像',
  `blog` varchar(255) NULL DEFAULT NULL COMMENT '用户网址',
  `company` varchar(50) NULL DEFAULT NULL COMMENT '所在公司',
  `location` varchar(255) NULL DEFAULT NULL COMMENT '位置',
  `email` varchar(50) NULL DEFAULT NULL COMMENT '用户邮箱',
  `gender` varchar(10) NULL DEFAULT NULL COMMENT '性别',
  `remark` varchar(500) NULL DEFAULT NULL COMMENT '用户备注（各平台中的用户个人介绍）',
  `source` varchar(20) NULL DEFAULT NULL COMMENT '用户来源',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '系统用户ID',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB COMMENT = '授权用户';

-- ----------------------------
-- Records of t_ja_user
-- ----------------------------

-- ----------------------------
-- Table structure for t_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user`;
CREATE TABLE `t_sys_user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(50) NULL DEFAULT NULL COMMENT '用户名称',
  `nickname` varchar(100) NULL DEFAULT NULL COMMENT '用户昵称',
  `password` varchar(255) NULL DEFAULT NULL COMMENT '用户密码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 COMMENT = '系统用户';

-- ----------------------------
-- Records of t_sys_user
-- ----------------------------
INSERT INTO `t_sys_user` VALUES (1, 'user', 'C3Stones', '$2a$10$WXEPqxjMwY6d6A0hkeBtGu.acRRWUOJmX7oLUuYMHF1VWWUm4EqOC');
INSERT INTO `t_sys_user` VALUES (2, 'system', '管理员', '$2a$10$dmO7Uk9/lo1D5d1SvCGgWuB050a0E2uuBDNITEpWFiIfCg.3UbA8y');

SET FOREIGN_KEY_CHECKS = 1;