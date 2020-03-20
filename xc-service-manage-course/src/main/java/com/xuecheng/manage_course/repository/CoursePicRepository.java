package com.xuecheng.manage_course.repository;

import com.xuecheng.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description: 课程图片
 * @author: 沈煜辉
 * @create: 2020-03-04 20:01
 **/
public interface CoursePicRepository extends JpaRepository<CoursePic,String> {

    /**
     * 通过课程Id删除图片信息
     * @param courseid 课程ID
     * @return 删除数量
     */
    long deleteByCourseid(String courseid);
}
