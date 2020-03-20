package com.xuecheng.manage.service.impl;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage.repository.SystemRepository;
import com.xuecheng.manage.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @description: 系统数据业务实现类
 * @author: 沈煜辉
 * @create: 2020-02-11 17:32
 **/
@Service
public class SystemServiceImpl implements SystemService {

    @Autowired
    SystemRepository systemRepository;

    @Override
    public SysDictionary findSysDictionary(String dType) {
        if (StringUtils.isEmpty(dType)){
            return null;
        }
        return systemRepository.findByDType(dType);
    }
}
