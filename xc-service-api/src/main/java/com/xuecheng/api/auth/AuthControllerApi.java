package com.xuecheng.api.auth;

import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @description: 用户认证授权接口
 * @author: 沈煜辉
 * @create: 2020-03-17 12:22
 **/
@Api(value = "用户认证授权接口",tags = "用户认证授权接口")
public interface AuthControllerApi {

    /**
     * 用户登录
     * @param loginRequest 登录所需的参数对象
     * @return
     */
    @ApiOperation(value = "用户登录")
    LoginResult login(LoginRequest loginRequest);

    /**
     * 用户退出
     * @return
     */
    @ApiOperation(value = "用户退出")
    ResponseResult logout();

    /**
     * 查询jwt令牌
     * @return
     */
    @ApiOperation(value = "查询jwt令牌")
    JwtResult getJwt();
}
