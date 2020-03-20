package com.xuecheng.manage.service;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.model.response.QueryResponseResult;

/**
 * @description: cms模板业务接口
 * @author: 沈煜辉
 * @create: 2020-01-31 13:03
 **/
public interface CmsTemplateService {
    /**
     * service 查询所用的模板
     * @return
     */
    QueryResponseResult findAllTemplate();

    /**
     * 通过模板Id查找模板
     * @param templateId 模板Id
     * @return 模板
     */
    CmsTemplate findTemplateById(String templateId);
}
