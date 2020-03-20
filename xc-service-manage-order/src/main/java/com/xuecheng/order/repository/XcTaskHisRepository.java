package com.xuecheng.order.repository;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

/**
 * @description: 任务的dao类
 * @author: 沈煜辉
 * @create: 2020-03-20 14:31
 **/
public interface XcTaskHisRepository extends JpaRepository<XcTaskHis,String> {

}
