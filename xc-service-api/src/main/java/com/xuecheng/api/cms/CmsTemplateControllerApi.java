package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @description: cms页面接口
 * @author: 沈煜辉
 * @create: 2019-12-22 22:04
 **/
@Api(value = "cms模板管理接口")
public interface CmsTemplateControllerApi {
    /**
     * 查询所用的模板
     * @return
     */
    @ApiOperation(value = "查询所用的模板信息")
    QueryResponseResult findAllTemplate();
}
