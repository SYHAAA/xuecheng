package com.xuecheng.manage.controller;

import com.xuecheng.api.cms.CmsConfigControllerApi;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage.service.CmsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: cms配置管理Controller
 * @author: 沈煜辉
 * @create: 2020-02-02 15:59
 **/
@RestController
@RequestMapping("/cms/config")
public class CmsConfigController implements CmsConfigControllerApi {

    @Autowired
    CmsConfigService cmsConfigService;

    @Override
    @GetMapping("/getModel/{id}")
    public CmsConfig getModel(@PathVariable String id) {
        return cmsConfigService.getModel(id);
    }
}
