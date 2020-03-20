package com.xuecheng.search.controller;

import com.xuecheng.api.search.EsCourseControllerApi;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.search.service.EsCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @description: 搜索接口实现类
 * @author: 沈煜辉
 * @create: 2020-03-09 15:29
 **/
@RestController
@RequestMapping("/search/course")
public class EsCourseController implements EsCourseControllerApi {

    @Autowired
    EsCourseService exCourseService;

    @Override
    @GetMapping(value = "/list/{page}/{size}")
    public QueryResponseResult list(@PathVariable("page") Integer page,@PathVariable("size") Integer size, CourseSearchParam courseSearchParam) {
        return exCourseService.list(page,size,courseSearchParam);
    }

    @Override
    @GetMapping(value = "/getall/{id}")
    public Map<String, CoursePub> getAll(@PathVariable("id") String id) {
        return exCourseService.getAll(id);
    }

    @Override
    @GetMapping(value="/getmedia/{teachplanId}")
    public TeachplanMediaPub getmedia(@PathVariable("teachplanId") String teachplanId) {
        String[] teachplanIds = new String[]{teachplanId};
        QueryResult queryResult = exCourseService.getmedia(teachplanIds);
        List<TeachplanMediaPub> list = queryResult.getList();
        if (list!=null&&list.size()!=0){
            return list.get(0);
        }
        return null;
    }
}
