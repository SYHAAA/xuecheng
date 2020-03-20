package com.xuecheng.api.course;

import com.github.pagehelper.PageInfo;
import com.xuecheng.framework.domain.cms.response.CoursePreviewResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;

/**
 * @description: 课程管理接口
 * @author: 沈煜辉
 * @create: 2020-02-06 15:43
 **/
@Api(value = "课程管理接口，负责课程的增、删、改、查操作")
public interface CourseControllerApi {
    /**
     * 课程计划查询
     * @param courseId 课程
     * @return
     */
    @ApiOperation(value = "课程计划查询")
    TeachplanNode findTeachListById(String courseId);

    /**
     * 添加课程计划
     * @param teachplan 课程计划
     * @return
     */
    @ApiOperation(value = "添加课程计划")
    ResponseResult saveTeachplan(Teachplan teachplan);

    /**
     * 分页查询所有的课程
     * @param pageNum
     * @param pageSize
     * @param courseListRequest
     * @return
     */
    @ApiOperation(value = "查询所有的课程")
    QueryResponseResult findAllCourse(Integer pageNum, Integer pageSize, CourseListRequest courseListRequest);

    /**
     * 添加课程基础信息
     * @param courseBase 课程基础对象
     * @return
     */
    @ApiOperation(value = "添加课程基础信息")
    ResponseResult saveCourseBase(CourseBase courseBase);

    /**
     * 根据课程Id查询课程信息
     * @param courseId 课程ID
     * @return 课程基础信息
     */
    @ApiOperation("根据课程Id查询课程信息")
    CourseBase findCourseView(String courseId);

    /**
     * 更新课程信息
     * @param courseId 课程ID
     * @param courseBase 课程基础信息
     * @return
     */
    @ApiOperation("更新课程信息")
    ResponseResult updateCourse(String courseId,CourseBase courseBase);

    /**
     * 获取课程营销信息
     * @param courseId 课程ID
     * @return 课程营销对象
     */
    @ApiOperation("查询课程营销信息")
    CourseMarket findCourseMarket(String courseId);

    /**
     * 更新课程营销信息
     * @param courseId
     * @param courseMarket
     * @return
     */
    @ApiOperation("更新课程营销信息")
    ResponseResult updateCourseMarket(String courseId,CourseMarket courseMarket);

    /**
     * 保存课程图片信息
     * @param courseId 课程ID
     * @param pic 课程图片ID
     * @return
     */
    @ApiOperation("保存课程图片信息")
    ResponseResult addCoursePic(String courseId,String pic);

    /**
     * 通过课程id查询课程图片信息
     * @param courseId 课程ID
     * @return
     */
    @ApiOperation("通过课程id查询课程图片信息")
    CoursePic findByCourseId(String courseId);

    /**
     * 通过课程id删除课程图片信息
     * @param courseId 课程ID
     * @return
     */
    @ApiOperation("通过课程id删除课程图片信息")
    ResponseResult deleteCourse(String courseId);

    /**
     * 通过课程Id查询课程数据
     * @param courseId
     * @return
     */
    @ApiOperation("通过课程Id查询课程数据")
    CourseView courseView(String courseId);

    /**
     * 通过课程Id
     * @param courseId
     * @return
     */
    @ApiOperation("通过课程ID预览课程信息")
    CoursePreviewResult coursePreview(String courseId);

    /**
     * 课程发布接口
     * @param courseId
     * @return
     */
    @ApiOperation("通过课程ID发布课程信息")
    CoursePreviewResult coursePost(String courseId);

    /**
     * 保存课程计划和媒资信息
     * @param teachplanMedia
     * @return
     */
    @ApiOperation(value = "保存课程计划和媒资信息")
    ResponseResult saveMedia(TeachplanMedia teachplanMedia);
}
