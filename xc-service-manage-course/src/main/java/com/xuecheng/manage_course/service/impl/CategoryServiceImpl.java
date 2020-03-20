package com.xuecheng.manage_course.service.impl;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.dao.CategoryMapper;
import com.xuecheng.manage_course.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 课程分类管理业务实现类
 * @author: 沈煜辉
 * @create: 2020-02-11 18:10
 **/
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public CategoryNode findList() {
        return categoryMapper.findList();
    }
}
