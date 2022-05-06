package com.eip.business.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * ClassName: TestProperteis
 * Function:
 * Date: 2022年01月12 17:29:43
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "eip.test")
public class TestProperteis {

    private String name;

    private String age;


}
