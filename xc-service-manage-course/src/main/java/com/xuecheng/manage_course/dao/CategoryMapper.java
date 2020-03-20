package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description: 分类信息mapper
 * @author: 沈煜辉
 * @create: 2020-02-11 18:22
 **/
@Mapper
public interface CategoryMapper {
    /**
     * 查询所用的分类信息
     * @return
     */
    public CategoryNode findList();
}
