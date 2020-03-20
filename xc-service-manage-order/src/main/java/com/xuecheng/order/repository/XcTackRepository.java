package com.xuecheng.order.repository;

import com.xuecheng.framework.domain.task.XcTask;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.xml.crypto.Data;
import java.util.Date;

/**
 * @description: 任务的dao类
 * @author: 沈煜辉
 * @create: 2020-03-20 14:31
 **/
public interface XcTackRepository extends JpaRepository<XcTask,String> {

    /**
     * 查出时间节点前的任务
     * @param date 时间点
     * @param pageable
     * @return
     */
    Page<XcTask> findByUpdateTimeBefore(Date date, Pageable pageable);

    /**
     * 跟新任务时间
     * @param id
     * @param updateTime
     * @return
     */
    @Modifying
    @Query("update XcTask t set t.updateTime = :updateTime where t.id = :id")
    int updateTaskTime(@Param(value = "id") String id, @Param(value = "updateTime") Date updateTime);

    /**
     * 乐观锁取任务
     * @param id
     * @param version
     * @return
     */
    @Modifying
    @Query("update XcTask t set t.version = :version+1 where t.id = :id and t.version = :version")
    int updateTaskVersion(@Param(value = "id") String id, @Param(value = "version") Integer version);
}
