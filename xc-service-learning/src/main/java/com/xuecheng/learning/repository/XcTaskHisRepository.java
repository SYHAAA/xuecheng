package com.xuecheng.learning.repository;

import com.xuecheng.framework.domain.learning.XcLearningCourse;
import com.xuecheng.framework.domain.task.XcTaskHis;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description:
 * @author: 沈煜辉
 * @create: 2020-03-20 15:23
 **/
public interface XcTaskHisRepository extends JpaRepository<XcTaskHis,String> {
}
