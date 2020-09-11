DROP TABLE IF EXISTS `justauth`.`t_ja_user`;
CREATE TABLE  `justauth`.`t_ja_user` (
  `uuid` varchar(64) NOT NULL COMMENT '用户第三方系统的唯一id',
  `username` varchar(100) DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(100) DEFAULT NULL COMMENT '用户昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '用户头像',
  `blog` varchar(255) DEFAULT NULL COMMENT '用户网址',
  `company` varchar(50) DEFAULT NULL COMMENT '所在公司',
  `location` varchar(255) DEFAULT NULL COMMENT '位置',
  `email` varchar(50) DEFAULT NULL COMMENT '用户邮箱',
  `gender` varchar(10) DEFAULT NULL COMMENT '性别',
  `remark` varchar(500) DEFAULT NULL COMMENT '用户备注（各平台中的用户个人介绍）',
  `source` varchar(20) DEFAULT NULL COMMENT '用户来源',
  PRIMARY KEY (`uuid`)
);