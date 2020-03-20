package com.xuecheng.manage_course.repository;

import com.xuecheng.framework.domain.course.CourseBase;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator.
 */
public interface CourseBaseRepository extends JpaRepository<CourseBase,String> {

    /**
     * 通过查询课程基础信息
     * @param name 课程名称
     * @return
     */
    CourseBase findByName(String name);
}
