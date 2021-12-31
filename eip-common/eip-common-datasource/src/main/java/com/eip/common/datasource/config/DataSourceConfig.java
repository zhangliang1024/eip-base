package com.eip.common.datasource.config;

import com.eip.common.datasource.enums.DBTypeEnum;
import com.eip.common.datasource.router.MyRoutingDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 数据源配置
 *   主库 masterDataSource
 *   从库 slave01DataSource
 *       slave01DataSource
 */
@Configuration
public class DataSourceConfig {

    @Autowired
    private Environment env;

    @Bean
    @ConfigurationProperties("spring.datasource.master")
    public DataSource masterDataSource(){
        DataSource build = DataSourceBuilder.create().build();
        HikariDataSource hikariDataSource = buildDataSource(build,"master");
        return hikariDataSource;
    }

    @Bean
    @ConfigurationProperties("spring.datasource.slave01")
    public DataSource slave01DataSource(){
        DataSource build = DataSourceBuilder.create().build();
        HikariDataSource hikariDataSource = buildDataSource(build,"slave01");
        return hikariDataSource;

    }

    @Bean
    @ConfigurationProperties("spring.datasource.slave02")
    public DataSource slave02DataSource(){
        DataSource build = DataSourceBuilder.create().build();
        HikariDataSource hikariDataSource = buildDataSource(build,"slave02");
        return hikariDataSource;
    }

    @Bean
    public DataSource myRoutingDataSource(@Qualifier("masterDataSource")DataSource masterDataSource,
                                         @Qualifier("slave01DataSource")DataSource slave01DataSource,
                                         @Qualifier("slave02DataSource")DataSource slave02DataSource){

        Map<Object,Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DBTypeEnum.MASTER,masterDataSource);
        targetDataSources.put(DBTypeEnum.SLAVE01,slave01DataSource());
        targetDataSources.put(DBTypeEnum.SLAVE02,slave02DataSource());

        MyRoutingDataSource myRoutingDataSource = new MyRoutingDataSource();
        myRoutingDataSource.setDefaultTargetDataSource(masterDataSource);
        myRoutingDataSource.setTargetDataSources(targetDataSources);
        return myRoutingDataSource;
    }

    public HikariDataSource buildDataSource(DataSource dataSource, String dataSourcePrefix){
        HikariDataSource hikariDataSource= (HikariDataSource) dataSource;
        hikariDataSource.setDriverClassName(env.getProperty("spring.datasource."+dataSourcePrefix+".driver-class-name"));
        hikariDataSource.setJdbcUrl(env.getProperty("spring.datasource."+dataSourcePrefix+".jdbc-url"));
        hikariDataSource.setUsername(env.getProperty("spring.datasource."+dataSourcePrefix+".username"));
        hikariDataSource.setPassword(env.getProperty("spring.datasource."+dataSourcePrefix+".password"));
        hikariDataSource.setMinimumIdle(Integer.parseInt(env.getProperty("spring.datasource."+dataSourcePrefix+".minIdle")));
        hikariDataSource.setConnectionTimeout(Long.parseLong(env.getProperty("spring.datasource."+dataSourcePrefix+".connectionTimeout")));
        hikariDataSource.setValidationTimeout(Long.parseLong(env.getProperty("spring.datasource."+dataSourcePrefix+".validationTimeout")));
        hikariDataSource.setMaximumPoolSize(Integer.parseInt(env.getProperty("spring.datasource."+dataSourcePrefix+".maxPoolSize")));
        return hikariDataSource;
    }


}
