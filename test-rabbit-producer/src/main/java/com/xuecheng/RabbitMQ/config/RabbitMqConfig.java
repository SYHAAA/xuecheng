package com.xuecheng.RabbitMQ.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 消息队列配置类
 * @author: 沈煜辉
 * @create: 2020-02-05 15:29
 **/
@Configuration
public class RabbitMqConfig {

    public final static String QUEUE_INFORM_EMAIL = "queue_inform_email";
    public final static String QUEUE_INFORM_SMS = "queue_inform_sms";
    public final static String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";

    /**
     * 声明交换机
     * @return
     */
    @Bean(EXCHANGE_TOPICS_INFORM)
    public Exchange EXCHANGE_TOPICS_INFORM(){
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_INFORM).durable(true).build();
    }

    /**
     * 声明队列
     * @return
     */
    @Bean(QUEUE_INFORM_EMAIL)
    public Queue QUEUE_INFORM_EMAIL(){
        return new Queue(QUEUE_INFORM_EMAIL);
    }

    /**
     * 声明队列
     * @return
     */
    @Bean(QUEUE_INFORM_SMS)
    public Queue QUEUE_INFORM_SMS(){
        return new Queue(QUEUE_INFORM_SMS);
    }

    /**
     * 将交换机与邮箱消息队列绑定
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding BIND_QUEUE_INFORM_EMAIL(@Qualifier(QUEUE_INFORM_EMAIL) Queue queue,@Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("inform.#.email.#").noargs();
    }

    /**
     * 将交换机与短信消息队列绑定
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding BIND_QUEUE_INFORM_SMS(@Qualifier(QUEUE_INFORM_SMS) Queue queue,@Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("inform.#.sms.#").noargs();
    }
}
