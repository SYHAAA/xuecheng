package com.xuecheng.api.system;

/**
 * @description: 系统数据管理接口
 * @author: 沈煜辉
 * @create: 2020-02-11 17:26
 **/

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.framework.domain.system.SysDictionaryValue;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "课程分类管理接口，负责课程分类的增、删、改、查操")
public interface SystemControllerApi {
    /**
     * 通过类型查询数据字典
     * @param dType 数据类型
     * @return
     */
    @ApiOperation("通过类型查询数据字典")
    SysDictionary findSysDictionary(String dType);
}
