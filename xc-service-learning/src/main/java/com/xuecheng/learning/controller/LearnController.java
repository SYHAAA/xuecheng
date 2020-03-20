package com.xuecheng.learning.controller;

import com.xuecheng.api.learning.LearnControllerApi;
import com.xuecheng.framework.domain.learning.response.GetMediaResult;
import com.xuecheng.learning.service.LearnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 学习接口实现类
 * @author: 沈煜辉
 * @create: 2020-03-14 14:34
 **/
@RestController
@RequestMapping("/learning")
public class LearnController implements LearnControllerApi {

    @Autowired
    LearnService learnService;

    @Override
    @GetMapping("/getmedia/{courseId}/{teachplanId}")
    public GetMediaResult getmedia(@PathVariable("courseId") String courseId,@PathVariable("teachplanId") String teachplanId) {
        return learnService.getmedia(courseId,teachplanId);
    }
}
