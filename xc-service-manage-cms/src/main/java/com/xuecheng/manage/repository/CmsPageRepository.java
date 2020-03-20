package com.xuecheng.manage.repository;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @description: cms的repositry接口
 * @author: 沈煜辉
 * @create: 2019-12-22 22:58
 **/
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {
    /**
     * 根据页面名称、站点id、访问路径查询cmsPage
     * @param pageName 页面名称
     * @param siteId 站点id
     * @param pageWebPath 访问路径
     * @return
     */
    CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName,String siteId,String pageWebPath);
}
