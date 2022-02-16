-- MySQL Workbench Synchronization
-- Generated: 2022-02-15 17:25
-- Model: New Model
-- Version: 1.0
-- Project: Name of the project
-- Author: zhliang1024

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

CREATE SCHEMA IF NOT EXISTS `eip-log` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE IF NOT EXISTS `eip-log`.`log_operate` (
  `log_id` VARCHAR(45) NOT NULL COMMENT '日志全局ID',
  `business_id` VARCHAR(45) NULL COMMENT '业务标识',
  `business_type` VARCHAR(45) NULL COMMENT '业务类型',
  `event_type` VARCHAR(45) NULL COMMENT '事件类型',
  `operate_type` VARCHAR(45) NULL COMMENT '操作类型',
  `log_message` VARCHAR(45) NULL COMMENT '日志内容',
  `event_level` VARCHAR(45) NULL COMMENT '事件级别',
  `operate_module` VARCHAR(45) NULL COMMENT '操作模块',
  `ip` VARCHAR(45) NULL COMMENT 'IP地址',
  `mac_address` VARCHAR(45) NULL COMMENT 'MAC地址',
  `request_uri` VARCHAR(45) NULL COMMENT '请求地址',
  `operate_sub_system` VARCHAR(45) NULL COMMENT '子系统',
  `operator` VARCHAR(45) NULL COMMENT '操作人账号',
  `success` VARCHAR(45) NULL COMMENT '是否成功',
  `result` VARCHAR(45) NULL COMMENT '请求执行结果',
  `exception` VARCHAR(45) NULL COMMENT '请求失败异常',
  `operate_time` TIMESTAMP NULL COMMENT '执行时间',
  `execution_time` INT(11) NULL COMMENT '执行耗时',
  PRIMARY KEY (`log_id`)
  ) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8 COMMENT =  '日志记录';


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
