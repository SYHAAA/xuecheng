package com.xuecheng.ucenter.controller;

import com.xuecheng.api.user.UcenterControllerApi;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.service.UcenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 用户中心接口实现类
 * @author: 沈煜辉
 * @create: 2020-03-17 15:45
 **/
@RestController
@RequestMapping("/ucenter")
public class UcenterController implements UcenterControllerApi {

    @Autowired
    UcenterService ucenterService;

    @Override
    @GetMapping("/getuserext")
    public XcUserExt getUserext(@RequestParam("username") String username) {
        return ucenterService.getUserext(username);
    }


}
