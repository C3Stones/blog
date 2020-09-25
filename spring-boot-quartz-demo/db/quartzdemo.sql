SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `t_qrtz_blob_triggers`;
CREATE TABLE `t_qrtz_blob_triggers`  (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(190) NOT NULL,
  `trigger_group` varchar(190) NOT NULL,
  `blob_data` blob NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  INDEX `sched_name`(`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `t_qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `t_qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- ----------------------------
-- Records of t_qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for t_qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `t_qrtz_calendars`;
CREATE TABLE `t_qrtz_calendars`  (
  `sched_name` varchar(120) NOT NULL,
  `calendar_name` varchar(190) NOT NULL,
  `calendar` blob NOT NULL,
  PRIMARY KEY (`sched_name`, `calendar_name`) USING BTREE
);

-- ----------------------------
-- Records of t_qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for t_qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `t_qrtz_cron_triggers`;
CREATE TABLE `t_qrtz_cron_triggers`  (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(190) NOT NULL,
  `trigger_group` varchar(190) NOT NULL,
  `cron_expression` varchar(120) NOT NULL,
  `time_zone_id` varchar(80) NULL DEFAULT NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `t_qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `t_qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- ----------------------------
-- Records of t_qrtz_cron_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for t_qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `t_qrtz_fired_triggers`;
CREATE TABLE `t_qrtz_fired_triggers`  (
  `sched_name` varchar(120) NOT NULL,
  `entry_id` varchar(95) NOT NULL,
  `trigger_name` varchar(190) NOT NULL,
  `trigger_group` varchar(190) NOT NULL,
  `instance_name` varchar(190) NOT NULL,
  `fired_time` bigint(0) NOT NULL,
  `sched_time` bigint(0) NOT NULL,
  `priority` int(0) NOT NULL,
  `state` varchar(16) NOT NULL,
  `job_name` varchar(190) NULL DEFAULT NULL,
  `job_group` varchar(190) NULL DEFAULT NULL,
  `is_nonconcurrent` varchar(1) NULL DEFAULT NULL,
  `requests_recovery` varchar(1) NULL DEFAULT NULL,
  PRIMARY KEY (`sched_name`, `entry_id`) USING BTREE,
  INDEX `idx_qrtz_ft_trig_inst_name`(`sched_name`, `instance_name`) USING BTREE,
  INDEX `idx_qrtz_ft_inst_job_req_rcvry`(`sched_name`, `instance_name`, `requests_recovery`) USING BTREE,
  INDEX `idx_qrtz_ft_j_g`(`sched_name`, `job_name`, `job_group`) USING BTREE,
  INDEX `idx_qrtz_ft_jg`(`sched_name`, `job_group`) USING BTREE,
  INDEX `idx_qrtz_ft_t_g`(`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  INDEX `idx_qrtz_ft_tg`(`sched_name`, `trigger_group`) USING BTREE
);

-- ----------------------------
-- Records of t_qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for t_qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `t_qrtz_job_details`;
CREATE TABLE `t_qrtz_job_details`  (
  `sched_name` varchar(120) NOT NULL,
  `job_name` varchar(190) NOT NULL,
  `job_group` varchar(190) NOT NULL,
  `description` varchar(250) NULL DEFAULT NULL,
  `job_class_name` varchar(250) NOT NULL,
  `is_durable` varchar(1) NOT NULL,
  `is_nonconcurrent` varchar(1) NOT NULL,
  `is_update_data` varchar(1) NOT NULL,
  `requests_recovery` varchar(1) NOT NULL,
  `job_data` blob NULL,
  PRIMARY KEY (`sched_name`, `job_name`, `job_group`) USING BTREE,
  INDEX `idx_qrtz_j_req_recovery`(`sched_name`, `requests_recovery`) USING BTREE,
  INDEX `idx_qrtz_j_grp`(`sched_name`, `job_group`) USING BTREE
);

-- ----------------------------
-- Records of t_qrtz_job_details
-- ----------------------------

-- ----------------------------
-- Table structure for t_qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `t_qrtz_locks`;
CREATE TABLE `t_qrtz_locks`  (
  `sched_name` varchar(120) NOT NULL,
  `lock_name` varchar(40) NOT NULL,
  PRIMARY KEY (`sched_name`, `lock_name`) USING BTREE
);

-- ----------------------------
-- Records of t_qrtz_locks
-- ----------------------------
INSERT INTO `t_qrtz_locks` VALUES ('clusteredScheduler', 'STATE_ACCESS');
INSERT INTO `t_qrtz_locks` VALUES ('clusteredScheduler', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for t_qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `t_qrtz_paused_trigger_grps`;
CREATE TABLE `t_qrtz_paused_trigger_grps`  (
  `sched_name` varchar(120) NOT NULL,
  `trigger_group` varchar(190) NOT NULL,
  PRIMARY KEY (`sched_name`, `trigger_group`) USING BTREE
);

-- ----------------------------
-- Records of t_qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for t_qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `t_qrtz_scheduler_state`;
CREATE TABLE `t_qrtz_scheduler_state`  (
  `sched_name` varchar(120) NOT NULL,
  `instance_name` varchar(190) NOT NULL,
  `last_checkin_time` bigint(0) NOT NULL,
  `checkin_interval` bigint(0) NOT NULL,
  PRIMARY KEY (`sched_name`, `instance_name`) USING BTREE
);

-- ----------------------------
-- Records of t_qrtz_scheduler_state
-- ----------------------------
INSERT INTO `t_qrtz_scheduler_state` VALUES ('clusteredScheduler', 'C3Stones-PC', 1600918524362, 10000);

-- ----------------------------
-- Table structure for t_qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `t_qrtz_simple_triggers`;
CREATE TABLE `t_qrtz_simple_triggers`  (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(190) NOT NULL,
  `trigger_group` varchar(190) NOT NULL,
  `repeat_count` bigint(0) NOT NULL,
  `repeat_interval` bigint(0) NOT NULL,
  `times_triggered` bigint(0) NOT NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `t_qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `t_qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- ----------------------------
-- Records of t_qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for t_qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `t_qrtz_simprop_triggers`;
CREATE TABLE `t_qrtz_simprop_triggers`  (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(190) NOT NULL,
  `trigger_group` varchar(190) NOT NULL,
  `str_prop_1` varchar(512) NULL DEFAULT NULL,
  `str_prop_2` varchar(512) NULL DEFAULT NULL,
  `str_prop_3` varchar(512) NULL DEFAULT NULL,
  `int_prop_1` int(0) NULL DEFAULT NULL,
  `int_prop_2` int(0) NULL DEFAULT NULL,
  `long_prop_1` bigint(0) NULL DEFAULT NULL,
  `long_prop_2` bigint(0) NULL DEFAULT NULL,
  `dec_prop_1` decimal(13, 4) NULL DEFAULT NULL,
  `dec_prop_2` decimal(13, 4) NULL DEFAULT NULL,
  `bool_prop_1` varchar(1) NULL DEFAULT NULL,
  `bool_prop_2` varchar(1) NULL DEFAULT NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `t_qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `t_qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- ----------------------------
-- Records of t_qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for t_qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `t_qrtz_triggers`;
CREATE TABLE `t_qrtz_triggers`  (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(190) NOT NULL,
  `trigger_group` varchar(190) NOT NULL,
  `job_name` varchar(190) NOT NULL,
  `job_group` varchar(190) NOT NULL,
  `description` varchar(250) NULL DEFAULT NULL,
  `next_fire_time` bigint(0) NULL DEFAULT NULL,
  `prev_fire_time` bigint(0) NULL DEFAULT NULL,
  `priority` int(0) NULL DEFAULT NULL,
  `trigger_state` varchar(16) NOT NULL,
  `trigger_type` varchar(8) NOT NULL,
  `start_time` bigint(0) NOT NULL,
  `end_time` bigint(0) NULL DEFAULT NULL,
  `calendar_name` varchar(190) NULL DEFAULT NULL,
  `misfire_instr` smallint(0) NULL DEFAULT NULL,
  `job_data` blob NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  INDEX `idx_qrtz_t_j`(`sched_name`, `job_name`, `job_group`) USING BTREE,
  INDEX `idx_qrtz_t_jg`(`sched_name`, `job_group`) USING BTREE,
  INDEX `idx_qrtz_t_c`(`sched_name`, `calendar_name`) USING BTREE,
  INDEX `idx_qrtz_t_g`(`sched_name`, `trigger_group`) USING BTREE,
  INDEX `idx_qrtz_t_state`(`sched_name`, `trigger_state`) USING BTREE,
  INDEX `idx_qrtz_t_n_state`(`sched_name`, `trigger_name`, `trigger_group`, `trigger_state`) USING BTREE,
  INDEX `idx_qrtz_t_n_g_state`(`sched_name`, `trigger_group`, `trigger_state`) USING BTREE,
  INDEX `idx_qrtz_t_next_fire_time`(`sched_name`, `next_fire_time`) USING BTREE,
  INDEX `idx_qrtz_t_nft_st`(`sched_name`, `trigger_state`, `next_fire_time`) USING BTREE,
  INDEX `idx_qrtz_t_nft_misfire`(`sched_name`, `misfire_instr`, `next_fire_time`) USING BTREE,
  INDEX `idx_qrtz_t_nft_st_misfire`(`sched_name`, `misfire_instr`, `next_fire_time`, `trigger_state`) USING BTREE,
  INDEX `idx_qrtz_t_nft_st_misfire_grp`(`sched_name`, `misfire_instr`, `next_fire_time`, `trigger_group`, `trigger_state`) USING BTREE,
  CONSTRAINT `t_qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `t_qrtz_job_details` (`sched_name`, `job_name`, `job_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- ----------------------------
-- Records of t_qrtz_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for t_sys_job
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_job`;
CREATE TABLE `t_sys_job`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `job_name` varchar(100) NULL DEFAULT NULL COMMENT '任务名称',
  `cron_expression` varchar(255) NULL DEFAULT NULL COMMENT 'cron表达式',
  `bean_class` varchar(255) NULL DEFAULT NULL COMMENT '任务执行类（包名+类名）',
  `status` varchar(10) NULL DEFAULT NULL COMMENT '任务状态',
  `job_group` varchar(50) NULL DEFAULT NULL COMMENT '任务分组',
  `job_data_map` varchar(1000) NULL DEFAULT NULL COMMENT '参数',
  `create_user_id` int(0) NULL DEFAULT NULL COMMENT '创建人ID',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user_id` int(0) NULL DEFAULT NULL COMMENT '更新人ID',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) AUTO_INCREMENT = 3 COMMENT = '定时任务';

-- ----------------------------
-- Records of t_sys_job
-- ----------------------------
INSERT INTO `t_sys_job` VALUES (1, 'TestJob', '0/5 * * * * ?', 'com.c3stones.job.biz.TestJob', 'NONE', 'default', '{\"username\":\"zhangsan\", \"age\":18}', 1, '2020-09-25 15:22:32', 1, '2020-09-25 15:22:32', '测试定时任务1');
INSERT INTO `t_sys_job` VALUES (2, 'Test2Job', '0 * * * * ?', 'com.c3stones.job.biz.Test2Job', 'NONE', 'default', '{\"username\":\"lisi\", \"age\":20}', 1, '2020-09-25 15:22:54', 1, '2020-09-25 15:22:54', '测试定时任务2');

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
)AUTO_INCREMENT = 3 COMMENT = '系统用户';

-- ----------------------------
-- Records of t_sys_user
-- ----------------------------
INSERT INTO `t_sys_user` VALUES (1, 'user', 'C3Stones', '$2a$10$WXEPqxjMwY6d6A0hkeBtGu.acRRWUOJmX7oLUuYMHF1VWWUm4EqOC');
INSERT INTO `t_sys_user` VALUES (2, 'system', '管理员', '$2a$10$dmO7Uk9/lo1D5d1SvCGgWuB050a0E2uuBDNITEpWFiIfCg.3UbA8y');

SET FOREIGN_KEY_CHECKS = 1;