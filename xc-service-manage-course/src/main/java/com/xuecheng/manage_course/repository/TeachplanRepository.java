package com.xuecheng.manage_course.repository;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Administrator.
 */
public interface TeachplanRepository extends JpaRepository<Teachplan,String> {

    /**
     * 通过课程id和父节点id查询课程计划列表
     * @param courseid 课程id
     * @param parentId 父节点id
     * @return 计划列表
     */
    List<Teachplan> findByCourseidAndParentid(String courseid,String parentId);
}
