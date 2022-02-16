package com.eip.ability.log;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ClassName: EipLogerApplication
 * Function:
 * Date: 2022年02月11 16:49:57
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@SpringBootApplication
@MapperScan("com.eip.ability.log")
public class LogWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogWebApplication.class,args);
    }

}
