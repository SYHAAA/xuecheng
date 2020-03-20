package com.xuecheng.auth.controller;

import com.xuecheng.api.auth.AuthControllerApi;
import com.xuecheng.auth.service.AuthService;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.model.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 认证接口实现类
 * @author: 沈煜辉
 * @create: 2020-03-17 13:12
 **/
@RestController
public class AuthController implements AuthControllerApi {

    @Autowired
    AuthService authService;

    @Override
    @PostMapping("/userlogin")
    public LoginResult login(LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @Override
    @PostMapping("/userlogout")
    public ResponseResult logout() {
        return authService.logout();
    }

    @Override
    @GetMapping("/userjwt")
    public JwtResult getJwt() {
        return authService.getJwt();
    }
}
