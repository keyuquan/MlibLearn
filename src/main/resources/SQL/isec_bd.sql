/*
Navicat MySQL Data Transfer

Source Server         : isecdev
Source Server Version : 50722
Source Host           : isec-hdp01:3306
Source Database       : isec_bd

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2018-05-08 11:39:45
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for clean_rule
-- ----------------------------
DROP TABLE IF EXISTS `clean_rule`;
CREATE TABLE `clean_rule` (
  `uid` varchar(32) NOT NULL,
  `rule_type` varchar(20) DEFAULT NULL,
  `rule_name` varchar(32) DEFAULT NULL,
  `rule_date` varchar(32) DEFAULT NULL,
  `delete_flag` int(11) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of clean_rule
-- ----------------------------

-- ----------------------------
-- Table structure for site_info
-- ----------------------------
DROP TABLE IF EXISTS `site_info`;
CREATE TABLE `site_info` (
  `code` varchar(32) NOT NULL,
  `name` varchar(100) NOT NULL,
  `domain` varchar(32) NOT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT '1',
  `create_date` datetime NOT NULL,
  `end_date` datetime DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of site_info
-- ----------------------------
INSERT INTO `site_info` VALUES ('1', 'baidu', 'www.baidu.com', '百度', '1', '2018-05-08 10:52:42', '2040-10-24 11:11:33');
INSERT INTO `site_info` VALUES ('2', 'guge', 'www.google.com', '谷歌', '1', '2018-05-15 10:55:54', null);
INSERT INTO `site_info` VALUES ('3', 'sina', 'www.sina.com', '新浪', '1', '2018-05-08 10:57:21', null);

-- ----------------------------
-- Table structure for site_tag_relation
-- ----------------------------
DROP TABLE IF EXISTS `site_tag_relation`;
CREATE TABLE `site_tag_relation` (
  `tag_code` varchar(30) NOT NULL,
  `domain_code` varchar(30) NOT NULL,
  `tag_weight` decimal(8,2) DEFAULT NULL,
  `version_no` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of site_tag_relation
-- ----------------------------
INSERT INTO `site_tag_relation` VALUES ('1', '1', '0.80', '1');
INSERT INTO `site_tag_relation` VALUES ('1', '2', '0.60', '1');
INSERT INTO `site_tag_relation` VALUES ('1', '3', '0.20', '1');
INSERT INTO `site_tag_relation` VALUES ('2', '1', '0.60', '1');
INSERT INTO `site_tag_relation` VALUES ('2', '3', '0.40', '1');
INSERT INTO `site_tag_relation` VALUES ('3', '3', '0.80', '1');
INSERT INTO `site_tag_relation` VALUES ('3', '1', '0.70', '1');
INSERT INTO `site_tag_relation` VALUES ('3', '2', '0.60', '1');
INSERT INTO `site_tag_relation` VALUES ('4', '2', '0.40', '1');

-- ----------------------------
-- Table structure for tag_attr
-- ----------------------------
DROP TABLE IF EXISTS `tag_attr`;
CREATE TABLE `tag_attr` (
  `code` varchar(32) NOT NULL,
  `type` varchar(32) NOT NULL,
  `name` varchar(32) NOT NULL,
  `remarks` varchar(200) DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT '1',
  `parent_code` varchar(32) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `order` int(11) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tag_attr
-- ----------------------------
INSERT INTO `tag_attr` VALUES ('1', 'mh', '门户', '门户', '1', null, '1', null, '2018-05-08 10:57:48', null, null);
INSERT INTO `tag_attr` VALUES ('2', 'sp', '视频', '视频', '1', null, null, null, '2018-05-08 10:58:49', null, null);
INSERT INTO `tag_attr` VALUES ('3', 'sex', '色情', '色情', '1', null, null, null, '2018-05-08 10:59:33', null, null);
INSERT INTO `tag_attr` VALUES ('4', 'sport', '体育', '体育', '1', null, null, null, '2018-05-08 10:59:59', null, null);
