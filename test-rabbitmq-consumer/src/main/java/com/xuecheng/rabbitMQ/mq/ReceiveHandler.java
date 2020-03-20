package com.xuecheng.rabbitMQ.mq;

import com.xuecheng.rabbitMQ.config.RabbitMqConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: 沈煜辉
 * @create: 2020-02-05 16:01
 **/
@Component
public class ReceiveHandler {

    @RabbitListener(queues = {RabbitMqConfig.QUEUE_INFORM_EMAIL})
    public void receive_email(Message message,String msg){
        System.out.println(msg);
    }

    @RabbitListener(queues = {RabbitMqConfig.QUEUE_INFORM_SMS})
    public void receive_sms(Message message,String msg){
        System.err.println(msg);
    }
}
