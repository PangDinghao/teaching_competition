/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 80036
Source Host           : localhost:3306
Source Database       : yizhi_test

Target Server Type    : MYSQL
Target Server Version : 80036
File Encoding         : 65001

Date: 2024-04-01 17:46:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `rolename` varchar(255) DEFAULT NULL,
  `roledetail` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '教育局管理员', '拥有最最最牛逼的权利');
INSERT INTO `role` VALUES ('2', '评委', '评审专家，求求多给几分吧');
INSERT INTO `role` VALUES ('3', '教师', '参赛选手');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '鐢ㄦ埛涓婚敭',
  `name` varchar(255) DEFAULT NULL,
  `account` varchar(255) DEFAULT NULL,
  `school` varchar(255) DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'e10adc3949ba59abbe56e057f20f883e',
  `create_time` datetime DEFAULT NULL,
  `create_user` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_user` bigint DEFAULT NULL,
  `roleid` bigint DEFAULT '0',
  `groupid` bigint DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '超级管理员', 'admin', '清北大学', 'e10adc3949ba59abbe56e057f20f883e', '2024-03-29 21:59:03', '1', '2024-03-29 21:59:09', '1', '1', '0');
INSERT INTO `user` VALUES ('2', '评审专家1', 'export1', '中东大学', 'e10adc3949ba59abbe56e057f20f883e', '2024-03-30 10:30:25', '1', '2024-03-30 10:30:29', '1', '2', '0');
INSERT INTO `user` VALUES ('3', '参赛教师1', 'teacher1', '中华大学', 'e10adc3949ba59abbe56e057f20f883e', '2024-03-30 10:31:10', '1', '2024-03-30 10:31:14', '1', '3', '1');
INSERT INTO `user` VALUES ('7', '张三', 'zs123', '中北大学', 'e10adc3949ba59abbe56e057f20f883e', '2024-03-30 15:24:39', null, '2024-03-30 15:24:39', null, '2', '1');
INSERT INTO `user` VALUES ('8', '李四', 'ls123', '中北大学', 'e10adc3949ba59abbe56e057f20f883e', '2024-03-30 16:19:50', '1', '2024-03-30 16:19:56', '1', '3', '2');
INSERT INTO `user` VALUES ('30', '张物流', 'ww121', '中东大学', '96e79218965eb72c92a549dd5a330112', '2024-03-30 21:12:40', '1', '2024-03-30 21:43:17', '1', '2', '1');

-- ----------------------------
-- Table structure for `work_file`
-- ----------------------------
DROP TABLE IF EXISTS `work_file`;
CREATE TABLE `work_file` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `work_id` bigint NOT NULL,
  `work_typeid` bigint DEFAULT NULL,
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of work_file
-- ----------------------------
INSERT INTO `work_file` VALUES ('1', '1', '1', 'dc970ca7-d042-4ba1-aa11-8dbd6aa7dc68_测试文件.pdf');
INSERT INTO `work_file` VALUES ('2', '1', '1', 'dc970ca7-d042-4ba1-aa11-8dbd6aa7dc68_测试文件.pdf');
INSERT INTO `work_file` VALUES ('3', '1', '1', 'dc970ca7-d042-4ba1-aa11-8dbd6aa7dc68_测试文件.pdf');
INSERT INTO `work_file` VALUES ('4', '1', '1', '77e6c0e8-b86f-436c-af4f-9a6168089aae_测试文件.mp4');
INSERT INTO `work_file` VALUES ('5', '1', '2', 'dc970ca7-d042-4ba1-aa11-8dbd6aa7dc68_测试文件.pdf');
INSERT INTO `work_file` VALUES ('6', '1', '3', 'dc970ca7-d042-4ba1-aa11-8dbd6aa7dc68_测试文件.pdf');
INSERT INTO `work_file` VALUES ('55', '10', '1', '404423b0-4eb4-44a9-89e1-2b97b4d4c739_测试文件.mp4');
INSERT INTO `work_file` VALUES ('56', '10', '1', '404423b0-4eb4-44a9-89e1-2b97b4d4c739_测试文件.mp4');
INSERT INTO `work_file` VALUES ('57', '10', '1', '404423b0-4eb4-44a9-89e1-2b97b4d4c739_测试文件.mp4');
INSERT INTO `work_file` VALUES ('58', '10', '2', '404423b0-4eb4-44a9-89e1-2b97b4d4c739_测试文件.pdf');
INSERT INTO `work_file` VALUES ('59', '10', '3', '404423b0-4eb4-44a9-89e1-2b97b4d4c739_测试文件.pdf');
INSERT INTO `work_file` VALUES ('60', '10', '4', '404423b0-4eb4-44a9-89e1-2b97b4d4c739_测试文件.pdf');
INSERT INTO `work_file` VALUES ('61', '10', '5', '404423b0-4eb4-44a9-89e1-2b97b4d4c739_测试文件.pdf');
INSERT INTO `work_file` VALUES ('62', '10', '6', '404423b0-4eb4-44a9-89e1-2b97b4d4c739_测试文件！！我是修改之后的.pdf');

-- ----------------------------
-- Table structure for `work_file_typename`
-- ----------------------------
DROP TABLE IF EXISTS `work_file_typename`;
CREATE TABLE `work_file_typename` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `typename` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of work_file_typename
-- ----------------------------
INSERT INTO `work_file_typename` VALUES ('1', '课堂实录');
INSERT INTO `work_file_typename` VALUES ('2', '教案');
INSERT INTO `work_file_typename` VALUES ('3', '教学实施报告');
INSERT INTO `work_file_typename` VALUES ('4', '专业人才培养方案');
INSERT INTO `work_file_typename` VALUES ('5', '课程标准');
INSERT INTO `work_file_typename` VALUES ('6', '教材选用说明');

-- ----------------------------
-- Table structure for `work_group`
-- ----------------------------
DROP TABLE IF EXISTS `work_group`;
CREATE TABLE `work_group` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of work_group
-- ----------------------------
INSERT INTO `work_group` VALUES ('1', '思政课程组');
INSERT INTO `work_group` VALUES ('2', '公共基础课程组');
INSERT INTO `work_group` VALUES ('3', '专业技能课程一组');
INSERT INTO `work_group` VALUES ('4', '专业技能课程二组');

-- ----------------------------
-- Table structure for `work_info`
-- ----------------------------
DROP TABLE IF EXISTS `work_info`;
CREATE TABLE `work_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `work_name` varchar(255) DEFAULT NULL,
  `school` varchar(255) DEFAULT NULL,
  `team_username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `groupid` varchar(255) DEFAULT NULL,
  `contact_info` varchar(255) DEFAULT NULL COMMENT '联系信息',
  `ave_score` double DEFAULT NULL,
  `entry_form` varchar(255) DEFAULT '' COMMENT '参赛报名表文件名',
  `info_form` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '信息公式文件名',
  `create_time` datetime DEFAULT NULL,
  `create_user` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_user` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of work_info
-- ----------------------------
INSERT INTO `work_info` VALUES ('1', '数学人才培养计划', '中北大学', '张三', '2', '13623675486', '82', '180bfef2-05a1-4872-ae5a-8ed79b1eb563_测试文件.doc', '180bfef2-05a1-4872-ae5a-8ed79b1eb563_测试文件.doc', '2024-03-31 18:57:06', '5', '2024-03-31 18:57:12', '1');
INSERT INTO `work_info` VALUES ('2', '英语人才培养计划', '中东大学', '李四，王五，张三', '2', '13213213234', '95', '180bfef2-05a1-4872-ae5a-8ed79b1eb563_测试文件.doc', '180bfef2-05a1-4872-ae5a-8ed79b1eb563_测试文件.doc', '2024-03-31 20:26:25', '5', '2024-03-31 20:26:33', '1');
INSERT INTO `work_info` VALUES ('10', '物理人才培养计划', '中西大学', '牛三', '2', '16453645364', '99', '404423b0-4eb4-44a9-89e1-2b97b4d4c739_测试文件.pdf', '404423b0-4eb4-44a9-89e1-2b97b4d4c739_测试文件.pdf', '2024-03-31 22:09:41', '1', '2024-03-31 22:13:34', '1');
INSERT INTO `work_info` VALUES ('11', '思政人才培养机会', '中南大学', '牛五', '1', '14343454567', null, '404423b0-4eb4-44a9-89e1-2b97b4d4c739_测试文件.pdf', '404423b0-4eb4-44a9-89e1-2b97b4d4c739_测试文件.pdf', '2024-04-01 17:27:46', '1', '2024-04-01 17:27:25', '1');

-- ----------------------------
-- Table structure for `work_review`
-- ----------------------------
DROP TABLE IF EXISTS `work_review`;
CREATE TABLE `work_review` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `workid` bigint NOT NULL,
  `review_detile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `score1` double DEFAULT NULL,
  `score2` double DEFAULT NULL,
  `score3` double DEFAULT NULL,
  `score4` double DEFAULT NULL,
  `score5` double DEFAULT NULL,
  `score6` double DEFAULT NULL,
  `total_score` double DEFAULT NULL,
  `create_user` bigint DEFAULT NULL,
  `create_datetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of work_review
-- ----------------------------
INSERT INTO `work_review` VALUES ('1', '1', '这个作品很好', '20', '20', '40', '8', '8', '4', '100', '2', '2024-04-01 15:13:59');
INSERT INTO `work_review` VALUES ('2', '2', '这个作品更好', '20', '20', '40', '8', '8', '4', '100', '2', '2024-04-01 15:14:49');
INSERT INTO `work_review` VALUES ('10', '10', '这个作品太强了，强的可怕', '20', '20', '40', '8', '8', '3', '99', '7', '2024-04-01 17:22:16');
