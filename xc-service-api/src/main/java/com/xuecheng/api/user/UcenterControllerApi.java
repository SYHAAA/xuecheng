package com.xuecheng.api.user;

import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @description: 用户中心接口
 * @author: 沈煜辉
 * @create: 2020-03-17 15:35
 **/
@Api(value = "用户中心接口",tags = "用户中心接口")
public interface UcenterControllerApi {

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    @ApiOperation(value = "根据用户名获取用户信息")
    XcUserExt getUserext(String username);
}
