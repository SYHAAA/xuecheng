package com.xuecheng.api.cms;

import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @description: cms页面站点接口
 * @author: 沈煜辉
 * @create: 2020-01-29 14:25
 **/
@Api(value = "cms站点管理接口")
public interface CmsSiteControllerApi {
    /**
     * 查询所用的站点
     * @return
     */
    @ApiOperation(value = "查询站点列表")
    QueryResponseResult findAllSite();
}
