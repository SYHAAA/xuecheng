package com.xuecheng.manage.service;

import com.xuecheng.framework.domain.cms.CmsConfig;

/**
 * @description: cms配置管理业务接口
 * @author: 沈煜辉
 * @create: 2020-02-02 16:08
 **/
public interface CmsConfigService {
    /**
     * 业务层获取数据模型
     * @param id 数据模型ID
     * @return
     */
    CmsConfig getModel(String id);
}
