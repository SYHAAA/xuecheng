package com.xuecheng.api.media;

import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @description: 媒资文件管理接口
 * @author: 沈煜辉
 * @create: 2020-03-13 13:23
 **/
@Api(value = "媒资文件管理接口",tags = "媒资文件管理接口")
public interface MediaFileControllerApi {

    /**
     * 分页查询所有的媒资文件
     * @param page 起始页
     * @param size 页面大小
     * @param queryMediaFileRequest 页面查询条件
     * @return
     */
    @ApiOperation(value = "分页查询所用的媒资文件")
    QueryResponseResult findAll(Integer page, Integer size, QueryMediaFileRequest queryMediaFileRequest);
}
