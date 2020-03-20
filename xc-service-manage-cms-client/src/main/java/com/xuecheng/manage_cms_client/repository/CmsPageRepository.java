package com.xuecheng.manage_cms_client.repository;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @description: cms的repositry接口
 * @author: 沈煜辉
 * @create: 2019-12-22 22:58
 **/
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {

}
