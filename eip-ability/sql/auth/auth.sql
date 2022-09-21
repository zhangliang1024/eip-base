

-- ----------------------------
-- Table structure for sys_oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth_client_details`;
CREATE TABLE `sys_oauth_client_details` (
  `client_id` varchar(32) NOT NULL COMMENT '客户端ID',
  `client_secret` varchar(256) DEFAULT NULL COMMENT '客户端秘钥',
  `status` bit(1) DEFAULT b'1' COMMENT '应用状态',
  `type` tinyint DEFAULT '0' COMMENT '应用类型（0=综合应用,1=服务应用,2=PC网页,3=手机网页,4=小程序）',
  `resource_ids` varchar(255) DEFAULT NULL COMMENT '微服务应用名（暂时不建议用）',
  `client_name` varchar(255) DEFAULT NULL COMMENT '客户端名称',
  `scope` varchar(256) DEFAULT NULL COMMENT '范围',
  `authorized_grant_types` varchar(256) DEFAULT NULL COMMENT '认证类型',
  `web_server_redirect_uri` varchar(256) DEFAULT NULL COMMENT 'web服务站点',
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int DEFAULT '43200' COMMENT 'token 有效期默认12小时',
  `refresh_token_validity` int DEFAULT '604800' COMMENT 'refresh token  有效期默认7天',
  `additional_information` varchar(4096) DEFAULT NULL COMMENT '附加信息',
  `autoapprove` varchar(256) DEFAULT NULL COMMENT '自动审批',
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='终端信息表';

-- ----------------------------
-- Records of sys_oauth_client_details
-- ----------------------------
BEGIN;
INSERT INTO `sys_oauth_client_details` VALUES ('client', 'client', b'1', 0, NULL, '我是客户端', 'server', 'password,client_credentials,authorization_code', NULL, NULL, 86400, 604800, NULL, NULL);
INSERT INTO `sys_oauth_client_details` VALUES ('test', 'test', b'1', 0, NULL, '测试客户端', 'server', 'password,refresh_token,client_credentials,authorization_code', NULL, NULL, 86400, 604800, NULL, NULL);
COMMIT;

