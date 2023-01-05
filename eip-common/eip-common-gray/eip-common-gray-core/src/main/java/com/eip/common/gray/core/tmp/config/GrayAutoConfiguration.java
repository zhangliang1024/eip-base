// package com.eip.common.gray.core.tmp.config;
//
// import com.eip.common.gray.interceptor.FeignRequestInterceptor;
// import org.springframework.context.annotation.Bean;
//
// /**
//  * ClassName: GrayRuleConfig
//  * Function: 灰度部署的负载规则配置类
//  * 注意: 这个类一定不要被扫描进IOC容器中，一旦扫描则对全部服务生效
//  * Date: 2023年01月03 16:15:50
//  *
//  * @author 张良 E-mail:zhangliang01@jingyougroup.com
//  * @version V1.0.0
//  */
// public class GrayAutoConfiguration {
//
//     @Bean
//     public GrayRule grayRule(){
//         return new GrayRule();
//     }
//
//     @Bean
//     public FeignRequestInterceptor feignRequestInterceptor(){
//         return new FeignRequestInterceptor();
//     }
// }
