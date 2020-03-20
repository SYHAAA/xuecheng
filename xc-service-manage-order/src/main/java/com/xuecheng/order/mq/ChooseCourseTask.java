package com.xuecheng.order.mq;

import com.github.pagehelper.util.StringUtil;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.order.config.RabbitMQConfig;
import com.xuecheng.order.service.TaskService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @description: 发送消息任务类
 * @author: 沈煜辉
 * @create: 2020-03-20 14:45
 **/
@Component
public class ChooseCourseTask {
    @Autowired
    TaskService taskService;


    @Scheduled(fixedRate = 5000)
    public void sendChooseCourseTask(){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(GregorianCalendar.MINUTE,1);
        Date date = calendar.getTime();
        List<XcTask> taskList = taskService.findTaskList(date, 5);
        if (taskList.size()>0){
            for (XcTask xcTask : taskList) {
                String id = xcTask.getId();
                Integer version = xcTask.getVersion();
                if (taskService.getTask(id,version)>0){
                    taskService.publicChooseCourse(xcTask, RabbitMQConfig.EX_LEARNING_ADDCHOOSECOURSE,RabbitMQConfig.XC_LEARNING_ADDCHOOSECOURSE_KEY);
                }
            }
        }
    }

    @RabbitListener(queues = RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE)
    public void finishTask(XcTask xcTask){
        if (xcTask!=null&& StringUtil.isNotEmpty(xcTask.getId())){
            String id = xcTask.getId();
            taskService.finishTask(id);
        }
    }
}
