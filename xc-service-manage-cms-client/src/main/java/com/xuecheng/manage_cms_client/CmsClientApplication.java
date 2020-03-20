package com.xuecheng.manage_cms_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @description: 启动类
 * @author: 沈煜辉
 * @create: 2020-02-06 11:06
 **/
@SpringBootApplication
@EntityScan("com.xuecheng.framework.domain.cms")
@ComponentScan(basePackages = {"com.xuecheng.framework"})
@ComponentScan(basePackages = {"com.xuecheng.manage_cms_client"})
public class CmsClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmsClientApplication.class,args);
    }
}
