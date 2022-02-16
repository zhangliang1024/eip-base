package com.eip.sample.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * ClassName: EipLogerApplication
 * Function:
 * Date: 2022年02月11 16:49:57
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */

@SpringBootApplication
public class LogSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogSampleApplication.class,args);
    }

}
