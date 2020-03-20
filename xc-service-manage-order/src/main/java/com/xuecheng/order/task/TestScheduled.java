package com.xuecheng.order.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @description: 测试定时器
 * @author: 沈煜辉
 * @create: 2020-03-20 14:02
 **/
public class TestScheduled {

    private final static Logger LOGGER = LoggerFactory.getLogger(TestScheduled.class);

//    @Scheduled(cron = "0/5 * * * * *")
//    @Scheduled(fixedDelay = 5000)
    @Scheduled(fixedRate = 5000)
    public void task1(){
        LOGGER.info("开始执行任务");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("任务执行结束");
    }

    @Scheduled(fixedRate = 5000)
    public void task2(){
        LOGGER.info("======开始执行任务==========");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("======任务执行结束==========");
    }
}
