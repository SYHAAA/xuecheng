package com.xuecheng.manage_cms_client.repository;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @description: cms站点实现接口
 * @author: 沈煜辉
 * @create: 2020-01-29 14:32
 **/
public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {
}
