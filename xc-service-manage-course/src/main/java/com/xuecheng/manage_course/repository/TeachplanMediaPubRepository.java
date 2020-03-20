package com.xuecheng.manage_course.repository;

import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator.
 */
public interface TeachplanMediaPubRepository extends JpaRepository<TeachplanMediaPub,String> {

    /**
     * 根据课程id删除媒资信息
     * @param courseId
     * @return
     */
    Long deleteByCourseId(String courseId);
}
