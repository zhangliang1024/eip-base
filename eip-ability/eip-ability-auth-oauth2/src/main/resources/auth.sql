
CREATE TABLE `oauth_client_details` (
  `client_id` varchar(128) NOT NULL COMMENT '客户端ID',
  `resource_ids` varchar(256) DEFAULT NULL COMMENT '资源ID集合，多个资源时用英文逗号分隔',
  `client_secret` varchar(256) DEFAULT NULL COMMENT '客户端密匙',
  `scope` varchar(256) DEFAULT NULL COMMENT '客户端申请的权限范围',
  `authorized_grant_types` varchar(256) DEFAULT NULL COMMENT '客户端支持的grant_type',
  `web_server_redirect_uri` varchar(256) DEFAULT NULL COMMENT '重定向URI',
  `authorities` varchar(256) DEFAULT NULL COMMENT '客户端所拥有的SpringSecurity的权限值，多个用英文逗号分隔',
  `access_token_validity` int(11) DEFAULT NULL COMMENT '访问令牌有效时间值(单位秒)',
  `refresh_token_validity` int(11) DEFAULT NULL COMMENT '更新令牌有效时间值(单位秒)',
  `additional_information` varchar(4096) DEFAULT NULL COMMENT '预留字段',
  `autoapprove` varchar(256) DEFAULT NULL COMMENT '用户是否自动Approval操作',
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户端信息';

INSERT INTO `oauth_client_details`(`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`) VALUES ('c1', 'all', '$2a$10$n/wCt3PAUiFRwWmygwphaODTGf4nZku6UJQh1Lyy8YKmi1cfGtrDW', 'ROLE_ADMIN,ROLE_USER,ROLE_API,ALL', 'authorization_code,password,client_credentials,implicit,refresh_token', 'http://www.baidu.com', NULL, NULL, NULL, NULL, 'false');
INSERT INTO `oauth_client_details`(`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`) VALUES ('c2', 'all', '$2a$10$4ITes3sAlqV9B2lUB3nJA.9cd16aGC8cyEP9VNpa6pr8Ag9CqzyJy', 'ROLE_ADMIN,ROLE_USER,ROLE_API,ALL', 'authorization_code,password,client_credentials,implicit,refresh_token', 'http://www.baidu.com', NULL, NULL, NULL, NULL, 'false');

CREATE TABLE `oauth_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `CODE` varchar(256) DEFAULT NULL COMMENT '授权码(未加密)',
  `authentication` blob COMMENT 'AuthorizationRequestHolder.java对象序列化后的二进制数据',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='授权码';



