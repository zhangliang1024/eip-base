DROP TABLE IF EXISTS `common_dictionary`;
CREATE TABLE `common_dictionary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(64) NOT NULL COMMENT '编码\r\n一颗树仅仅有一个统一的编码',
  `name` varchar(64) NOT NULL COMMENT '名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `status` bit(1) DEFAULT b'1' COMMENT '状态',
  `readonly` bit(1) DEFAULT b'0' COMMENT '内置角色',
  `sequence` tinyint(3) DEFAULT NULL COMMENT '排序',
  `created_by` bigint(20) DEFAULT '0' COMMENT '创建人id',
  `created_name` varchar(50) DEFAULT NULL COMMENT '创建人名称',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_by` bigint(20) DEFAULT '0' COMMENT '更新人id',
  `last_modified_name` varchar(50) DEFAULT NULL COMMENT '更新人名称',
  `last_modified_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COMMENT='字典类型';


DROP TABLE IF EXISTS `common_dictionary_item`;
CREATE TABLE `common_dictionary_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dictionary_id` bigint(20) NOT NULL COMMENT '类型ID',
  `dictionary_code` varchar(64) NOT NULL COMMENT '类型',
  `value` varchar(64) NOT NULL DEFAULT '' COMMENT '编码',
  `label` varchar(64) NOT NULL DEFAULT '' COMMENT '名称',
  `status` bit(1) DEFAULT b'1' COMMENT '状态',
  `color` varchar(255) DEFAULT NULL COMMENT '颜色',
  `description` varchar(255) DEFAULT '' COMMENT '描述',
  `sequence` int(11) DEFAULT '1' COMMENT '排序',
  `created_by` bigint(20) DEFAULT '0' COMMENT '创建人id',
  `created_name` varchar(255) DEFAULT NULL,
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_by` bigint(20) DEFAULT '0' COMMENT '更新人id',
  `last_modified_name` varchar(255) DEFAULT NULL,
  `last_modified_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `dict_code_item_code_uniq` (`dictionary_code`,`value`) USING BTREE COMMENT '字典编码与字典项目编码联合唯一'
) ENGINE=InnoDB AUTO_INCREMENT=125 DEFAULT CHARSET=utf8mb4 COMMENT='字典项';


DROP TABLE IF EXISTS `common_login_log`;
CREATE TABLE `common_login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '登录人ID',
  `ip` varchar(50) DEFAULT NULL COMMENT '登录IP',
  `client_id` varchar(50) DEFAULT NULL COMMENT '登录人客户端ID',
  `name` varchar(50) DEFAULT NULL COMMENT '登录人姓名',
  `principal` varchar(30) DEFAULT NULL COMMENT '登录人账号',
  `platform` varchar(255) DEFAULT NULL COMMENT '平台',
  `engine` varchar(255) DEFAULT NULL COMMENT '引擎类型',
  `engine_version` varchar(255) DEFAULT NULL COMMENT '引擎版本',
  `browser` varchar(255) DEFAULT NULL COMMENT '浏览器名称',
  `browser_version` varchar(255) DEFAULT NULL COMMENT '浏览器版本',
  `os` varchar(255) DEFAULT NULL COMMENT '操作系统',
  `location` varchar(50) DEFAULT NULL COMMENT '登录地点',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='登录日志';


DROP TABLE IF EXISTS `common_opt_log`;
CREATE TABLE `common_opt_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ip` varchar(50) DEFAULT NULL COMMENT '操作IP',
  `location` varchar(255) DEFAULT NULL,
  `trace` varchar(255) DEFAULT NULL COMMENT '日志链路追踪id日志标志',
  `type` varchar(5) DEFAULT 'OPT' COMMENT '日志类型\n#LogType{OPT:操作类型;EX:异常类型}',
  `description` varchar(255) DEFAULT NULL COMMENT '操作描述',
  `class_path` varchar(255) DEFAULT NULL COMMENT '类路径',
  `action_method` varchar(50) DEFAULT NULL COMMENT '请求方法',
  `request_uri` varchar(50) DEFAULT NULL COMMENT '请求地址',
  `http_method` varchar(10) DEFAULT 'GET' COMMENT '请求类型\n#HttpMethod{GET:GET请求;POST:POST请求;PUT:PUT请求;DELETE:DELETE请求;PATCH:PATCH请求;TRACE:TRACE请求;HEAD:HEAD请求;OPTIONS:OPTIONS请求;}',
  `params` longtext COMMENT '请求参数',
  `result` longtext COMMENT '返回值',
  `ex_desc` longtext COMMENT '异常详情信息',
  `ex_detail` longtext COMMENT '异常描述',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '开始时间',
  `finish_time` timestamp NULL DEFAULT NULL COMMENT '完成时间',
  `consuming_time` bigint(20) DEFAULT '0' COMMENT '消耗时间',
  `browser` varchar(500) DEFAULT NULL COMMENT '浏览器',
  `os` varchar(500) DEFAULT NULL COMMENT '浏览器',
  `engine` varchar(500) DEFAULT NULL COMMENT '浏览器',
  `engine_version` varchar(500) DEFAULT NULL COMMENT '浏览器',
  `platform` varchar(500) DEFAULT NULL COMMENT '浏览器',
  `browser_version` varchar(500) DEFAULT NULL COMMENT '浏览器',
  `created_by` bigint(20) DEFAULT '0' COMMENT '创建人id',
  `created_name` varchar(50) DEFAULT NULL COMMENT '操作人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_type` (`type`) USING BTREE COMMENT '日志类型'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统日志';


DROP TABLE IF EXISTS `sys_org`;
CREATE TABLE `sys_org` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `label` varchar(255) NOT NULL DEFAULT '' COMMENT '名称',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `alias` varchar(255) DEFAULT '' COMMENT '简称',
  `tel` varchar(255) DEFAULT NULL COMMENT '联系方式',
  `tree_path` varchar(255) DEFAULT ',' COMMENT '所有父级ID',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父ID',
  `sequence` int(11) DEFAULT '1' COMMENT '排序',
  `status` bit(1) DEFAULT b'1' COMMENT '状态',
  `description` varchar(255) DEFAULT '' COMMENT '描述',
  `created_by` bigint(20) DEFAULT '0' COMMENT '创建人id',
  `created_name` varchar(50) DEFAULT NULL COMMENT '创建人名称',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_by` bigint(20) DEFAULT '0' COMMENT '更新人id',
  `last_modified_name` varchar(50) DEFAULT NULL COMMENT '更新人名称',
  `last_modified_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10205 DEFAULT CHARSET=utf8mb4 COMMENT='组织';

DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `label` varchar(100) NOT NULL DEFAULT '' COMMENT '名称',
  `tree_path` varchar(500) DEFAULT ',' COMMENT '该节点的所有父节点',
  `permission` varchar(255) DEFAULT NULL COMMENT '权限',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父级菜单ID',
  `path` varchar(255) DEFAULT '' COMMENT '路径',
  `component` varchar(255) DEFAULT NULL COMMENT '组件',
  `sequence` int(11) DEFAULT '1' COMMENT '排序',
  `icon` varchar(255) DEFAULT '' COMMENT '菜单图标',
  `style` varchar(255) DEFAULT NULL COMMENT '样式',
  `type` tinyint(2) DEFAULT '1' COMMENT '类型（1=菜单;2=按钮）',
  `status` bit(1) DEFAULT b'1' COMMENT '1=启用;0=禁用',
  `readonly` bit(1) DEFAULT b'0' COMMENT '内置菜单（0=否;1=是）',
  `global` bit(1) DEFAULT b'0' COMMENT '公共资源\nTrue是无需分配所有人就可以访问的',
  `display` bit(1) DEFAULT b'1' COMMENT '0=隐藏;1=显示',
  `description` varchar(200) DEFAULT '' COMMENT '描述',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `created_name` varchar(255) DEFAULT NULL COMMENT '创建人名称',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_by` bigint(20) DEFAULT NULL COMMENT '更新人id',
  `last_modified_name` varchar(255) DEFAULT NULL COMMENT '更新人名称',
  `last_modified_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `INX_STATUS` (`global`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1030104 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='菜单';

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tenant_id` int(20) DEFAULT NULL COMMENT '租户编码',
  `code` varchar(30) DEFAULT NULL COMMENT '角色编码',
  `name` varchar(30) NOT NULL DEFAULT '' COMMENT '名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述信息',
  `scope_type` tinyint(2) DEFAULT NULL COMMENT '数据权限范围，值越大，权限越大',
  `locked` tinyint(1) DEFAULT '0' COMMENT '0=正常1=禁用',
  `super` tinyint(1) DEFAULT '0' COMMENT '0=非 1=管理员',
  `readonly` tinyint(1) DEFAULT '0' COMMENT '是否内置角色',
  `created_by` bigint(20) DEFAULT '0' COMMENT '创建人id',
  `created_name` varchar(255) DEFAULT NULL,
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_by` bigint(20) DEFAULT '0' COMMENT '更新人id',
  `last_modified_name` varchar(255) DEFAULT NULL,
  `last_modified_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色';


DROP TABLE IF EXISTS `sys_role_org`;
CREATE TABLE `sys_role_org` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `org_id` bigint(20) NOT NULL COMMENT '组织ID',
  UNIQUE KEY `role_id` (`role_id`,`org_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色表';

-- ----------------------------
-- Table structure for sys_role_res
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_res`;
CREATE TABLE `sys_role_res` (
  `role_id` bigint(8) NOT NULL COMMENT '角色ID',
  `res_id` bigint(8) NOT NULL COMMENT '菜单ID',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  UNIQUE KEY `idx_role_res` (`role_id`,`res_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限表';


DROP TABLE IF EXISTS `sys_station`;
CREATE TABLE `sys_station` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '名称',
  `code` varchar(255) DEFAULT NULL COMMENT '编码',
  `type` tinyint(2) DEFAULT NULL COMMENT '类型',
  `sequence` tinyint(3) DEFAULT NULL COMMENT '排序',
  `org_id` bigint(20) DEFAULT '0' COMMENT '组织ID\n#c_core_org',
  `status` bit(1) DEFAULT b'1' COMMENT '状态',
  `description` varchar(255) DEFAULT '' COMMENT '描述',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_name` varchar(255) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_name` varchar(255) DEFAULT NULL,
  `last_modified_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10129 DEFAULT CHARSET=utf8mb4 COMMENT='岗位';


DROP TABLE IF EXISTS `sys_station_message`;
CREATE TABLE `sys_station_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `level` varchar(64) DEFAULT NULL COMMENT '消息级别',
  `title` varchar(64) DEFAULT NULL COMMENT '名称',
  `content` varchar(1024) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `mark` bit(1) DEFAULT b'0' COMMENT '状态（0=未读 1=已读）',
  `receive_id` bigint(20) DEFAULT NULL COMMENT '接收人ID',
  `created_by` bigint(20) DEFAULT '0' COMMENT '创建人id',
  `created_name` varchar(50) DEFAULT NULL COMMENT '创建人名称',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_by` bigint(20) DEFAULT '0' COMMENT '更新人id',
  `last_modified_name` varchar(50) DEFAULT NULL COMMENT '更新人名称',
  `last_modified_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=utf8mb4 COMMENT='站内消息';

DROP TABLE IF EXISTS `sys_station_message_publish`;
CREATE TABLE `sys_station_message_publish` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `level` varchar(255) DEFAULT NULL COMMENT '消息级别',
  `status` tinyint(1) DEFAULT NULL COMMENT '0=为发布;1=已发布',
  `type` varchar(64) DEFAULT NULL COMMENT '编码',
  `title` varchar(64) DEFAULT NULL COMMENT '名称',
  `receiver` varchar(255) DEFAULT NULL COMMENT '接受者ID',
  `content` varchar(1024) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `created_by` bigint(20) DEFAULT '0' COMMENT '创建人id',
  `created_name` varchar(50) DEFAULT NULL COMMENT '创建人名称',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_by` bigint(20) DEFAULT '0' COMMENT '更新人id',
  `last_modified_name` varchar(50) DEFAULT NULL COMMENT '更新人名称',
  `last_modified_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='站内消息';

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  UNIQUE KEY `role_id` (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色表';


DROP TABLE IF EXISTS `t_file`;
CREATE TABLE `t_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `content_type` varchar(100) DEFAULT NULL COMMENT '文件类型',
  `size` bigint(20) DEFAULT NULL COMMENT '文件大小',
  `location` varchar(50) DEFAULT NULL COMMENT '登录地点',
  `ip` varchar(50) DEFAULT NULL COMMENT 'IP',
  `engine` varchar(255) DEFAULT NULL COMMENT '引擎类型',
  `engine_version` varchar(255) DEFAULT NULL COMMENT '引擎版本',
  `os` varchar(255) DEFAULT NULL COMMENT '操作系统',
  `bucket` varchar(255) DEFAULT NULL COMMENT '操作系统',
  `origin_name` varchar(255) DEFAULT NULL COMMENT '原始名称',
  `target_name` varchar(255) DEFAULT NULL COMMENT '目标名称',
  `mapping_path` varchar(255) DEFAULT NULL COMMENT '映射地址',
  `full_url` varchar(255) DEFAULT NULL COMMENT '完整地址',
  `extend` varchar(255) DEFAULT NULL COMMENT '拓展字段',
  `created_by` bigint(20) DEFAULT '0' COMMENT '创建人id',
  `created_name` varchar(50) DEFAULT NULL COMMENT '创建人名称',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_by` bigint(20) DEFAULT '0' COMMENT '更新人id',
  `last_modified_name` varchar(50) DEFAULT NULL COMMENT '更新人名称',
  `last_modified_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `UN_TARGET_NAME` (`target_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COMMENT='文件';

DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `username` varchar(30) NOT NULL COMMENT '账号',
  `password` varchar(200) DEFAULT NULL COMMENT '密码',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '昵称',
  `org_id` bigint DEFAULT NULL COMMENT '组织ID',
  `station_id` bigint DEFAULT NULL COMMENT '岗位ID',
  `readonly` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否内置',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机',
  `id_card` varchar(50) DEFAULT NULL COMMENT '身份证',
  `sex` tinyint DEFAULT '1' COMMENT '性别（1=男;2=女）',
  `status` bit(1) DEFAULT b'0' COMMENT '状态 1启用 0禁用',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `nation` varchar(20) DEFAULT NULL COMMENT '民族',
  `education` varchar(20) DEFAULT NULL COMMENT '学历',
  `position_status` varchar(20) DEFAULT NULL COMMENT '职位状态',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `created_by` bigint DEFAULT '0' COMMENT '创建人id',
  `created_name` varchar(50) DEFAULT NULL COMMENT '创建人名称',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_by` bigint DEFAULT '0' COMMENT '更新人id',
  `last_modified_name` varchar(50) DEFAULT NULL COMMENT '更新人名称',
  `last_modified_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `UN_ACCOUNT` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='用户';

-- 2022年 新增表
-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_api_perm`;
CREATE TABLE `sys_api_perm` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tenant_id` int(20) DEFAULT NULL COMMENT '租户编码',
  `interface_name` varchar(30) NOT NULL DEFAULT '' COMMENT '接口名称',
  `interface_method` varchar(50) DEFAULT NULL COMMENT '接口方法',
  `interface_path` varchar(50) DEFAULT NULL COMMENT '接口地址',
  `interface_description` varchar(255) DEFAULT NULL COMMENT '接口描述信息',
  `scope_type` tinyint(2) DEFAULT NULL COMMENT '数据权限范围，值越大，权限越大',
  `locked` tinyint(1) DEFAULT '0' COMMENT '0=正常1=禁用',
  `readonly` tinyint(1) DEFAULT '0' COMMENT '是否内置角色',
  `created_by` bigint(20) DEFAULT '0' COMMENT '创建人id',
  `created_name` varchar(255) DEFAULT NULL,
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_by` bigint(20) DEFAULT '0' COMMENT '更新人id',
  `last_modified_name` varchar(255) DEFAULT NULL,
  `last_modified_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='接口地址';

DROP TABLE IF EXISTS `sys_role_api`;
CREATE TABLE `sys_role_api` (
  `api_id` bigint(20) NOT NULL COMMENT '接口ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  UNIQUE KEY `role_id` (`api_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口角色表';
