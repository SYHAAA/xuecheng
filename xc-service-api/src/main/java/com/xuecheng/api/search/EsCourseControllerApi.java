package com.xuecheng.api.search;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

/**
 * @description: 课程搜索接口
 * @author: 沈煜辉
 * @create: 2020-03-09 15:24
 **/
@Api(value = "课程搜索接口",tags = "课程搜索")
public interface EsCourseControllerApi {

    /**
     * 课程搜索
     * @param page
     * @param size
     * @param courseSearchParam
     * @return
     */
    @ApiOperation("课程搜索")
    QueryResponseResult list(Integer page, Integer size, CourseSearchParam courseSearchParam);

    /**
     * 根据id查询课程信息
     * @param id 课程ID
     * @return
     */
    @ApiOperation("根据id查询课程信息")
    Map<String, CoursePub> getAll(String id);

    /**
     * 根据课程计划查询媒资信息
     * @param teachplanId
     * @return
     */
    @ApiOperation("根据课程计划查询媒资信息")
    TeachplanMediaPub getmedia(String teachplanId);
}
