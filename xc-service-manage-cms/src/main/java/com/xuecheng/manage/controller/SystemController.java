package com.xuecheng.manage.controller;

import com.xuecheng.api.system.SystemControllerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 数据字典Controller
 * @author: 沈煜辉
 * @create: 2020-02-11 17:29
 **/
@RestController
@RequestMapping("/sys")
public class SystemController implements SystemControllerApi {

    @Autowired
    SystemService systemService;

    @Override
    @GetMapping("/dictionary/get/{dType}")
    public SysDictionary findSysDictionary(@PathVariable("dType") String dType) {
        return systemService.findSysDictionary(dType);
    }
}
