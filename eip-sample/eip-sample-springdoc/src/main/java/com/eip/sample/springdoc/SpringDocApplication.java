package com.eip.sample.springdoc;

import com.eip.common.apidoc.annotation.EnableSpringDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ClassName: SpringDocApplication
 * Function:
 * Date: 2022年09月23 13:22:41
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@EnableSpringDoc
@SpringBootApplication
public class SpringDocApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDocApplication.class, args);
    }

}
