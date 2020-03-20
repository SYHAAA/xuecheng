package com.xuecheng.manage.controller;

import com.xuecheng.api.cms.CmsSiteControllerApi;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage.service.CmsSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: cms站点Controller
 * @author: 沈煜辉
 * @create: 2020-01-29 14:29
 **/
@RestController
public class CmsSiteController implements CmsSiteControllerApi {

    @Autowired
    CmsSiteService cmsSiteService;

    @Override
    @GetMapping("/cms/findAllSite")
    public QueryResponseResult findAllSite() {
        return cmsSiteService.findAllSite();
    }
}
