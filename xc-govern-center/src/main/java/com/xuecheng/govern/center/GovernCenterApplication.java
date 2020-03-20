package com.xuecheng.govern.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @description: 注册中心启动类
 * @author: 沈煜辉
 * @create: 2020-03-05 14:18
 **/
@EnableEurekaServer
@SpringBootApplication
public class GovernCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(GovernCenterApplication.class,args);
    }
}
