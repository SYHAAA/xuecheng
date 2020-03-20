package com.xuecheng.manage.repository;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @description: cms配置管理reposotory接口
 * @author: 沈煜辉
 * @create: 2020-02-02 16:09
 **/
public interface CmsConfigRepository extends MongoRepository<CmsConfig,String> {
}
