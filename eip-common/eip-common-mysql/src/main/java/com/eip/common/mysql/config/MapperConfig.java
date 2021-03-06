package com.eip.common.mysql.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@MapperScan(basePackages = "com.eip.**.mapper")
@EnableTransactionManagement
public class MapperConfig {

}
