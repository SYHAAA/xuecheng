package com.xuecheng.manage.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CoursePreviewResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description: cms的controller
 * @author: 沈煜辉
 * @create: 2019-12-22 22:46
 **/
@RestController
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    CmsPageService cmsPageService;

    @Override
    @GetMapping("/cms/{page}/{size}")
    public QueryResponseResult findList(@PathVariable(value = "page") int page, @PathVariable(value = "size") int size, QueryPageRequest queryPageRequest) {
        return cmsPageService.findList(page,size,queryPageRequest);
    }

    @Override
    @PostMapping("/cms/savePage")
    public CmsPageResult saveCmsPage(@RequestBody CmsPage cmsPage) {
        return cmsPageService.saveCmsPage(cmsPage);
    }

    @Override
    @GetMapping("/cms/get/{id}")
    public CmsPage findCmsPageByPageId(@PathVariable String id){
        return cmsPageService.findById(id);
    }

    @Override
    @PutMapping("/cms/edit/{id}")
    public CmsPageResult updateCmsPage(@PathVariable String id,@RequestBody CmsPage cmsPage) {
        return cmsPageService.updateCmsPage(id,cmsPage);
    }

    @Override
    @DeleteMapping("/cms/del/{id}")
    public ResponseResult deleteCmsPage(@PathVariable String id) {
        return cmsPageService.deleteCmsPage(id);
    }

    @Override
    @PostMapping("/cms/page/postPage/{pageId}")
    public ResponseResult post(@PathVariable("pageId") String pageId) {
        return cmsPageService.post(pageId);
    }

    @Override
    @PostMapping("/cms/save")
    public CmsPageResult save(@RequestBody CmsPage cmsPage) {
        return cmsPageService.save(cmsPage);
    }

    @Override
    @PostMapping("/postPageQuick")
    public CoursePreviewResult coursePost(@RequestBody CmsPage cmsPage) {
        return cmsPageService.coursePost(cmsPage);
    }
}
