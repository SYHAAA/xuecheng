package com.rabbitMq.test;

import com.xuecheng.RabbitMQ.RabbitMqProducerApplication;
import com.xuecheng.RabbitMQ.config.RabbitMqConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description: 测试类
 * @author: 沈煜辉
 * @create: 2020-02-05 15:28
 **/
@SpringBootTest(classes = RabbitMqProducerApplication.class)
@RunWith(SpringRunner.class)
public class RabbitMqProduceTest {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void testProducer(){
        for (int i = 0; i < 5; i++) {
            String msg = "send msg with sms and email to user"+i;
//            发送信息
            rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_TOPICS_INFORM,"inform.sms",msg);
            System.out.println(msg);
        }
    }
}
