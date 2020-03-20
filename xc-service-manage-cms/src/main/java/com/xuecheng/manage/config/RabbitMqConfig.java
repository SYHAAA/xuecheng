package com.xuecheng.manage.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 消息队列配置类
 * @author: 沈煜辉
 * @create: 2020-02-06 11:05
 **/
@Configuration
public class RabbitMqConfig {

    public final static String EX_ROUTING_CMS_POSTPAGE = "ex_routing_cms_postpage";

    /**
     * 声明交换机
     * @return
     */
    @Bean(EX_ROUTING_CMS_POSTPAGE)
    public Exchange EX_ROUTING_CMS_POSTPAGE(){
        return ExchangeBuilder.directExchange(EX_ROUTING_CMS_POSTPAGE).durable(true).build();
    }

}
