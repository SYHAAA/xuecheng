package com.xuecheng.manage.service;

import com.xuecheng.framework.model.response.QueryResponseResult;

/**
 * @description: cms站点业务接口
 * @author: 沈煜辉
 * @create: 2020-01-29 14:30
 **/
public interface CmsSiteService {
    /**
     * 查询所用的站点
     * @return
     */
    QueryResponseResult findAllSite();
}
