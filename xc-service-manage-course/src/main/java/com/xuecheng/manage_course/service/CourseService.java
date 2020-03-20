package com.xuecheng.manage_course.service;

import com.github.pagehelper.PageInfo;
import com.xuecheng.framework.domain.cms.response.CoursePreviewResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;

/**
 * @description: 课程管理业务层接口
 * @author: 沈煜辉
 * @create: 2020-02-06 18:47
 **/
public interface CourseService {
    /**
     * 业务层通过课程ID查询教学计划
     * @param courseId 课程ID
     * @return
     */
    TeachplanNode findTeachplanNodeById(String courseId);

    /**
     * 业务层添加课程计划
     * @param teachplan 课程计划
     * @return
     */
    @ApiOperation(value = "添加课程计划")
    ResponseResult saveTeachplan(Teachplan teachplan);

    /**
     * 业务成查询所用的课程
     * @param pageNum 起始页
     * @param pageSize 每页大小
     * @param courseListRequest 查询参数对象
     * @return
     */
    QueryResponseResult findAllCourse(Integer pageNum, Integer pageSize, CourseListRequest courseListRequest);

    /**
     * 保存课程信息
     * @param courseBase
     * @return
     */
    ResponseResult saveCourseBase(CourseBase courseBase);

    /**
     * 根据课程Id查询课程基础信息
     * @param courseId 课程ID
     * @return
     */
    CourseBase findCourseView(String courseId);

    /**
     * 更新课程信息业务
     * @param courseId 课程ID
     * @param courseBase 课程基础信息
     * @return
     */
    ResponseResult updateCourse(String courseId,CourseBase courseBase);

    /**
     * 获取课程营销信息
     * @param courseId
     * @return
     */
    CourseMarket findCourseMarket(String courseId);

    /**
     * 跟新课程信息
     * @param courseId
     * @param courseMarket
     * @return
     */
    ResponseResult updateCourseMarket(String courseId, CourseMarket courseMarket);

    /**
     * 业务层接口添加课程图片信息
     * @param courseId
     * @param pic
     * @return
     */
    ResponseResult addCoursePic(String courseId, String pic);

    /**
     * 通过课程Id查询课程图片信息
     * @param courseId
     * @return
     */
    CoursePic findByCourseId(String courseId);

    /**
     * 业务通过课程id删除图片信息
     * @param courseId
     * @return
     */
    ResponseResult deleteCourse(String courseId);

    /**
     * 通过课程Id查询课程数据
     * @param courseId
     * @return
     */
    CourseView getCourseView(String courseId);

    /**
     * 通过课程Id预览课程信息
     * @param courseId
     * @return
     */
    CoursePreviewResult coursePreview(String courseId);

    /**
     * 课程发布接口
     * @param courseId
     * @return
     */
    CoursePreviewResult coursePost(String courseId);

    /**
     * 业务保存课程媒资信息
     * @param teachplanMedia
     * @return
     */
    ResponseResult saveMedia(TeachplanMedia teachplanMedia);
}
