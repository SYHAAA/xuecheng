package com.xuecheng.manage_course.repository;

import com.xuecheng.framework.domain.course.CourseMarket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @description: 课程营销信息jpa
 * @author: 沈煜辉
 * @create: 2020-02-12 12:34
 **/
public interface CourseMarketRepository extends JpaRepository<CourseMarket,String> {
}
