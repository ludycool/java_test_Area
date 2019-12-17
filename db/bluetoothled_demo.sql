/*
Navicat MySQL Data Transfer

Source Server         : mv100
Source Server Version : 50728
Source Host           : 192.168.182.100:3306
Source Database       : bluetoothled_demo

Target Server Type    : MYSQL
Target Server Version : 50728
File Encoding         : 65001

Date: 2019-12-17 10:48:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for d_building
-- ----------------------------
DROP TABLE IF EXISTS `d_building`;
CREATE TABLE `d_building` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `name` varchar(200) DEFAULT NULL COMMENT '楼栋名称',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '父Id，当前记录项目主键',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人ID.',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人ID.',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='楼栋表';

-- ----------------------------
-- Table structure for s_sys_log
-- ----------------------------
DROP TABLE IF EXISTS `s_sys_log`;
CREATE TABLE `s_sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `oname` varchar(50) DEFAULT NULL COMMENT '操作名称',
  `ocontent` varchar(2000) DEFAULT NULL COMMENT '操作内容',
  `url` varchar(255) DEFAULT NULL COMMENT '操作地址',
  `permission` varchar(50) DEFAULT NULL COMMENT '操作权限',
  `ip` varchar(30) DEFAULT NULL COMMENT 'ip地址',
  `user_id` varchar(38) DEFAULT NULL COMMENT '操作人id',
  `user_name` varchar(50) DEFAULT NULL COMMENT '操作人名称',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2327 DEFAULT CHARSET=utf8mb4 COMMENT='操作日志';
