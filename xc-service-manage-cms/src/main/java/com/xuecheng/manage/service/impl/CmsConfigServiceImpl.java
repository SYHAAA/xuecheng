package com.xuecheng.manage.service.impl;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.ResultCode;
import com.xuecheng.manage.repository.CmsConfigRepository;
import com.xuecheng.manage.service.CmsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * @description: cms配置管理业务实现类
 * @author: 沈煜辉
 * @create: 2020-02-02 16:09
 **/
@Service
public class CmsConfigServiceImpl implements CmsConfigService {

    @Autowired
    CmsConfigRepository cmsConfigRepository;

    @Override
    public CmsConfig getModel(String id) {
        if(StringUtils.isEmpty(id)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        Optional<CmsConfig> optional = cmsConfigRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }
}
