package com.xuecheng.manage.service;

import com.xuecheng.framework.domain.system.SysDictionary;

/**
 * @description: 系统数据业务接口‘
 * @author: 沈煜辉
 * @create: 2020-02-11 17:31
 **/
public interface SystemService {
    /**
     * 通过类型查询数据字典
     * @param dType 数据类型
     * @return
     */
    SysDictionary findSysDictionary(String dType);
}
