package com.xuecheng.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @description: springBoot启动类
 * @author: 沈煜辉
 * @create: 2019-12-22 22:32
 **/
@EnableEurekaClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EntityScan("com.xuecheng.framework.domain.cms")
@ComponentScan(basePackages={"com.xuecheng.api"})
@ComponentScan(basePackages={"com.xuecheng.manage"})
@ComponentScan(basePackages = "com.xuecheng.framework.exception")
public class CmsService {
    public static void main(String[] args) {
        SpringApplication.run(CmsService.class,args);
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
