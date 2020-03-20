package com.xuecheng.manage.controller;

import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

/**
 * @description: 页面预览Controller
 * @author: 沈煜辉
 * @create: 2020-02-03 14:34
 **/
@Controller
public class CmsPagePreviewController extends BaseController {

    @Autowired
    CmsPageService cmsPageService;

    @GetMapping("/cms/preview/{pageId}")
    public void preview(@PathVariable String pageId){
        String pageHtml = cmsPageService.getPageHtml(pageId);
        if (!StringUtils.isEmpty(pageHtml)){
            try {
                ServletOutputStream servletOutputStream = response.getOutputStream();
                response.setHeader("Content-type","text/html;charset=utf-8");
                servletOutputStream.write(pageHtml.getBytes("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
