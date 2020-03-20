package com.xuecheng.manage_course.repository;

import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePub;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description: 课程营销信息jpa
 * @author: 沈煜辉
 * @create: 2020-02-12 12:34
 **/
public interface CoursePubRepository extends JpaRepository<CoursePub,String> {
}
