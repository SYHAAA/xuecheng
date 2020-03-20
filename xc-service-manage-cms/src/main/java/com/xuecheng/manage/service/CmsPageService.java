package com.xuecheng.manage.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CoursePreviewResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;

/**
 * @description: cms业务接口
 * @author: 沈煜辉
 * @create: 2019-12-22 22:56
 **/
public interface CmsPageService {
    /**
     * 带有条件的cmsPage分页查询
     * @param page 起始页
     * @param size 页面大小
     * @param queryPageRequest 查询参数
     * @return
     */
    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    /**
     * 业务层cms增加页面
     * @param cmsPage
     * @return
     */
    CmsPageResult saveCmsPage(CmsPage cmsPage);

    /**
     * 通过页面ID寻找页面
     * @param id 页面ID
     * @return
     */
    CmsPage findById(String id);

    /**
     * service 更新页面
     * @param id 页面ID
     * @param cmsPage 所要更新的页面
     * @return
     */
    CmsPageResult updateCmsPage(String id, CmsPage cmsPage);

    /**
     * 删除页面
     * @return
     * @param id 页面ID
     */
    ResponseResult deleteCmsPage(String id);

    /**
     * 根据页面Id生成静态界面
     * @param pageId 页面Id
     * @return 静态界面
     */
    String getPageHtml(String pageId);

    /**
     * 业务层根据页面Id发布页面
     * @param pageId
     * @return
     */
    ResponseResult post(String pageId);

    /**
     * 保存界面
     * @param cmsPage 页面信息
     * @return
     */
    CmsPageResult save(CmsPage cmsPage);

    /**
     * 课程一键发布接口
     * @param cmsPage
     * @return
     */
    CoursePreviewResult coursePost(CmsPage cmsPage);
}
