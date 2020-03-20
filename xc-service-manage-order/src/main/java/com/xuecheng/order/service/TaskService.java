package com.xuecheng.order.service;

import com.github.pagehelper.util.StringUtil;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.order.repository.XcTackRepository;
import com.xuecheng.order.repository.XcTaskHisRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @description: 任务业务类
 * @author: 沈煜辉
 * @create: 2020-03-20 14:36
 **/
@Service
public class TaskService {

    @Autowired
    XcTackRepository xcTackRepository;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    XcTaskHisRepository xcTaskHisRepository;


    public List<XcTask> findTaskList(Date date,Integer size){
        Pageable pageable = PageRequest.of(0,size);
        Page<XcTask> tasks = xcTackRepository.findByUpdateTimeBefore(date, pageable);
        return tasks.getContent();
    }

    @Transactional(rollbackOn = Exception.class)
    public void publicChooseCourse(XcTask xcTask,String exchange,String routeKey){
        if (xcTask!=null|| StringUtil.isNotEmpty(xcTask.getId())){
            String taskId = xcTask.getId();
            Optional<XcTask> op = xcTackRepository.findById(taskId);
            if (op.isPresent()){
                rabbitTemplate.convertAndSend(exchange,routeKey,xcTask);
                xcTackRepository.updateTaskTime(taskId,new Date());
            }
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public int getTask(String id,Integer version){
        return xcTackRepository.updateTaskVersion(id,version);
    }


    @Transactional(rollbackOn = Exception.class)
    public void finishTask(String xcTaskId){
        if (StringUtil.isNotEmpty(xcTaskId)){
            Optional<XcTask> optionalXcTask = xcTackRepository.findById(xcTaskId);
            if (optionalXcTask.isPresent()){
                XcTask xcTask = optionalXcTask.get();
                XcTaskHis xcTaskHis = new XcTaskHis();
                xcTackRepository.delete(optionalXcTask.get());
                BeanUtils.copyProperties(xcTask,xcTaskHis);
                xcTaskHisRepository.save(xcTaskHis);
            }
        }
    }
}
