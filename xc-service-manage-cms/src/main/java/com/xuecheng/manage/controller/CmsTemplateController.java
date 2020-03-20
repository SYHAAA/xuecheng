package com.xuecheng.manage.controller;

import com.xuecheng.api.cms.CmsTemplateControllerApi;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage.service.CmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: cms模板管理controller
 * @author: 沈煜辉
 * @create: 2020-01-31 13:01
 **/
@RestController
public class CmsTemplateController implements CmsTemplateControllerApi {

    @Autowired
    CmsTemplateService cmsTemplateService;
    /**
     * 查询所有的模板
     * @return
     */
    @Override
    @GetMapping("/cms/findAllTemplate")
    public QueryResponseResult findAllTemplate() {
        return cmsTemplateService.findAllTemplate();
    }
}
