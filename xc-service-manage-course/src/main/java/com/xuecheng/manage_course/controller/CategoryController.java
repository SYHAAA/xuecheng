package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CategoryControllerApi;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description: 课程分类controller
 * @author: 沈煜辉
 * @create: 2020-02-11 18:07
 **/
@RestController
public class CategoryController implements CategoryControllerApi {

    @Autowired
    CategoryService categoryService;

    @Override
    @GetMapping("/category/list")
    public CategoryNode findList() {
        return categoryService.findList();
    }
}
