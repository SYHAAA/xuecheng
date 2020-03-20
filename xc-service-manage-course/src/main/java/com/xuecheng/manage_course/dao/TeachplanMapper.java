package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description: 教学计划dao接口
 * @author: 沈煜辉
 * @create: 2020-02-06 18:37
 **/
@Mapper
public interface TeachplanMapper {

    /**
     * 通过课程id查询教学计划
     * @param courseId
     * @return
     */
    TeachplanNode findTeachplanNodeByCourseId(String courseId);
}
