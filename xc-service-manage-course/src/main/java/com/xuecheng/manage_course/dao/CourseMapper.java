package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Administrator.
 */
@Mapper
public interface CourseMapper {
   CourseBase findCourseBaseById(String id);

   /**
    * 带有条件对象分页查询所用的课程
    * @param courseListRequest
    * @return
    */
   List<CourseInfo> findAllCourse(CourseListRequest courseListRequest);
}
