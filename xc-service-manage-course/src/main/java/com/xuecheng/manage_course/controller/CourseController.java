package com.xuecheng.manage_course.controller;

import com.github.pagehelper.PageInfo;
import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.cms.response.CoursePreviewResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.Oauth2Util;
import com.xuecheng.framework.utils.XcOauth2Util;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_course.service.CourseService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rx.plugins.RxJavaCompletableExecutionHook;

import java.util.Map;

/**
 * @description: 课程管理Controller
 * @author: 沈煜辉
 * @create: 2020-02-06 15:46
 **/
@RestController
@RequestMapping("/course")
public class CourseController extends BaseController implements CourseControllerApi {

    @Autowired
    CourseService courseService;

    @PreAuthorize("hasAuthority('course_find_teachplan')")
    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachListById(@PathVariable("courseId") String courseId) {
        return courseService.findTeachplanNodeById(courseId);
    }

    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult saveTeachplan(@RequestBody Teachplan teachplan) {
        return courseService.saveTeachplan(teachplan);
    }

    @PreAuthorize("hasAuthority('course_find_list')")
    @Override
    @GetMapping("/coursebase/list/{pageNum}/{pageSize}")
    public QueryResponseResult findAllCourse(@PathVariable("pageNum") Integer pageNum,@PathVariable("pageSize") Integer pageSize, CourseListRequest courseListRequest) {
        if (courseListRequest == null){
            courseListRequest = new CourseListRequest();
        }
        XcOauth2Util xcOauth2Util = new XcOauth2Util();
        XcOauth2Util.UserJwt userJwt = xcOauth2Util.getUserJwtFromHeader(request);
        if (userJwt == null){
            ExceptionCast.cast(CommonCode.UNAUTHENTICATED);
        }
        String companyId = userJwt.getCompanyId();
        if (companyId!=null){
            courseListRequest.setCompanyId(companyId);
        }
        return courseService.findAllCourse(pageNum,pageSize,courseListRequest);
    }

    @Override
    @PostMapping("/coursebase/add")
    public ResponseResult saveCourseBase(@RequestBody CourseBase courseBase) {
        return courseService.saveCourseBase(courseBase);
    }

    @Override
    @GetMapping("/courseview/{courseId}")
    public CourseBase findCourseView(@PathVariable("courseId") String courseId) {
        return courseService.findCourseView(courseId);
    }

    @Override
    @PutMapping("/updateCourse/{courseId}")
    public ResponseResult updateCourse(@PathVariable("courseId") String courseId,@RequestBody CourseBase courseBase) {
        return courseService.updateCourse(courseId,courseBase);
    }

    @Override
    @GetMapping("/market/{courseId}")
    public CourseMarket findCourseMarket(@PathVariable("courseId") String courseId) {
        return courseService.findCourseMarket(courseId);
    }

    @Override
    @PutMapping("/updateCourseMarket/{courseId}")
    public ResponseResult updateCourseMarket(@PathVariable("courseId") String courseId,@RequestBody CourseMarket courseMarket) {
        return courseService.updateCourseMarket(courseId,courseMarket);
    }

    @Override
    @PostMapping("/coursepic/add")
    public ResponseResult addCoursePic(String courseId, String pic) {
        return courseService.addCoursePic(courseId,pic);
    }

    @Override
    @GetMapping("/coursepic/list/{courseId}")
    public CoursePic findByCourseId(@PathVariable("courseId") String courseId) {
        return courseService.findByCourseId(courseId);
    }

    @Override
    @DeleteMapping("/coursepic/delete")
    public ResponseResult deleteCourse(String courseId) {
        return courseService.deleteCourse(courseId);
    }

    @Override
    @GetMapping("/courseView/{id}")
    public CourseView courseView(@PathVariable("id") String id) {
        return courseService.getCourseView(id);
    }

    @Override
    @PostMapping("/preview/{courseId}")
    public CoursePreviewResult coursePreview(@PathVariable("courseId") String courseId) {
        return courseService.coursePreview(courseId);
    }

    @Override
    @PostMapping("/publish/{id}")
    public CoursePreviewResult coursePost(@PathVariable("id") String courseId) {
        return courseService.coursePost(courseId);
    }

    @Override
    @PostMapping("/savemedia")
    public ResponseResult saveMedia(@RequestBody TeachplanMedia teachplanMedia) {
        return courseService.saveMedia(teachplanMedia);
    }
}
