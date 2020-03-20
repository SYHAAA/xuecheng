package com.xuecheng.learning.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.learning.XcLearningCourse;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.learning.config.RabbitMQConfig;
import com.xuecheng.learning.repository.XcLearningCourseRepository;
import com.xuecheng.learning.repository.XcTaskHisRepository;
import com.xuecheng.learning.service.LearnService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description: 选课任务类
 * @author: 沈煜辉
 * @create: 2020-03-20 15:21
 **/
@Component
public class ChooseCourseTask {

    @Autowired
    LearnService learnService;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.XC_LEARNING_ADDCHOOSECOURSE)
    public void receiveChooseCourseTask(XcTask xcTask){
        String body = xcTask.getRequestBody();
        Map map = JSON.parseObject(body, Map.class);
        String userId = (String) map.get("userId");
        String courseId = (String) map.get("courseId");
        ResponseResult result = learnService.addCourse(userId, courseId, xcTask);
        if (result.isSuccess()){
            rabbitTemplate.convertAndSend(RabbitMQConfig.EX_LEARNING_ADDCHOOSECOURSE,RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE_KEY,xcTask);
        }
    }
}
