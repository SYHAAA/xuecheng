package com.xuecheng.rabbitMQ;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @description:
 * @author: 沈煜辉
 * @create: 2020-02-05 15:57
 **/
@SpringBootApplication
public class RabbitMqConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RabbitMqConsumerApplication.class,args);
    }
}
