package com.xuecheng.manage.repository;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @description: cms模板repository
 * @author: 沈煜辉
 * @create: 2020-01-31 13:04
 **/
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate,String> {
}
