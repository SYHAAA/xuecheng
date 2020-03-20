package com.xuecheng.manage_course.client;

import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CoursePreviewResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @description: 调用cms客户端接口
 * @author: 沈煜辉
 * @create: 2020-03-05 18:54
 **/
@FeignClient(value = XcServiceList.XC_SERVICE_MANAGE_CMS)
public interface CmsPageClient {

    /**
     * 调用cms微服务的接口，根据id查询cms
     * @param id
     * @return
     */
    @GetMapping("/cms/get/{id}")
    CmsPage findById(@PathVariable("id") String id);


    /**
     * 调用cms保存页面信息
     * @param cmsPage
     * @return
     */
    @PostMapping("/cms/save")
    CmsPageResult save(@RequestBody CmsPage cmsPage);

    /**
     * 调用cms的课程一键发布接口
     * @param cmsPage
     * @return
     */
    @PostMapping("/postPageQuick")
    CoursePreviewResult coursePost(@RequestBody CmsPage cmsPage);
}
