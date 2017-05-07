/*
Navicat MySQL Data Transfer

Source Server         : demo
Source Server Version : 50521
Source Host           : localhost:3306
Source Database       : jdbctest

Target Server Type    : MYSQL
Target Server Version : 50521
File Encoding         : 65001

Date: 2015-11-01 15:00:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `username` varchar(20) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  `email` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'chenxun', 'xl', 'xl', 'qq@qq.cn');
INSERT INTO `user` VALUES ('2', 'chenxun', 'xl', 'xl', 'qq@qq.cn');
INSERT INTO `user` VALUES ('3', 'chenxun', 'xl', 'xl', 'qq@qq.cn');
INSERT INTO `user` VALUES ('4', 'chenxun', 'zs', 'zs', 'qq@qq.cn');
