package com.xuecheng.manage_cms_client.config;

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

    public final static String QUEUE_CMS_POSTPAGE = "queue_cms_postpage";
    public final static String EX_ROUTING_CMS_POSTPAGE = "ex_routing_cms_postpage";
    @Value("${spring.xuecheng.mq.queue}")
    public String queue_cms_postpage_name;
    @Value("${spring.xuecheng.mq.routingKey}")
    public String routingKey;

    /**
     * 声明交换机
     * @return
     */
    @Bean(EX_ROUTING_CMS_POSTPAGE)
    public Exchange EX_ROUTING_CMS_POSTPAGE(){
        return ExchangeBuilder.directExchange(EX_ROUTING_CMS_POSTPAGE).durable(true).build();
    }

    /**
     * 声明队列
     * @return
     */
    @Bean(QUEUE_CMS_POSTPAGE)
    public Queue QUEUE_CMS_POSTPAGE(){
        return new Queue(QUEUE_CMS_POSTPAGE);
    }

    /**
     * 绑定队列和交换机
     * @param exchange 交换机
     * @param queue 队列
     * @return
     */
    @Bean
    public Binding BIND_QUEUE_CMS_POSTPAGE(@Qualifier(EX_ROUTING_CMS_POSTPAGE) Exchange exchange,@Qualifier(QUEUE_CMS_POSTPAGE) Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }
}
