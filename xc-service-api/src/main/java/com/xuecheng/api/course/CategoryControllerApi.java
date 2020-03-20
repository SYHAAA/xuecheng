package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @description: 课程分类的接口
 * @author: 沈煜辉
 * @create: 2020-02-11 16:54
 **/
@Api(value = "课程分类管理",tags = "课程分类管理",description = "课程分类管理")
public interface CategoryControllerApi {

    /**
     * 查询课程分类
     * @return
     */
    @ApiOperation("查询课程分类")
    CategoryNode findList();
}
