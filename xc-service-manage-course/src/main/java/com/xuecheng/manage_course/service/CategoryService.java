package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.ext.CategoryNode;

import java.util.List;

/**
 * @description: 课程分类业务层接口
 * @author: 沈煜辉
 * @create: 2020-02-11 18:09
 **/
public interface CategoryService {
    /**
     *查询所用课程分类信息业务接口
     * @return
     */
    CategoryNode findList();
}
