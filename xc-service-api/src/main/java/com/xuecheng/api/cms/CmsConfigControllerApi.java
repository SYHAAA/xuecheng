package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @description: cms相关数据配置接口
 * @author: 沈煜辉
 * @create: 2020-02-02 15:53
 **/
@Api(value = "cms配置管理接口，提供数据模型的管理、查询接口")
public interface CmsConfigControllerApi {

    /**
     * 通过ID查询cms数据
     * @param id 数据Id
     * @return
     */
    @ApiOperation(value = "通过ID查询cms数据")
    public CmsConfig getModel(String id);
}
