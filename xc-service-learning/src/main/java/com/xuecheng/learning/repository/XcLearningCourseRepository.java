package com.xuecheng.learning.repository;

import com.xuecheng.framework.domain.learning.XcLearningCourse;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description:
 * @author: 沈煜辉
 * @create: 2020-03-20 15:23
 **/
public interface XcLearningCourseRepository extends JpaRepository<XcLearningCourse,String> {

    /**
     * 通过用户id和课程id查询课程学习信息
     * @param userId 用户id
     * @param courseId 课程Id
     * @return
     */
    XcLearningCourse findByUserIdAndCourseId(String userId,String courseId);
}
