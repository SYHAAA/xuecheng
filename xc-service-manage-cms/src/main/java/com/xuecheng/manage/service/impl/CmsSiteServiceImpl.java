package com.xuecheng.manage.service.impl;

import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResultCode;
import com.xuecheng.manage.repository.CmsPageRepository;
import com.xuecheng.manage.repository.CmsSiteRepository;
import com.xuecheng.manage.service.CmsSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: cms站点业务接口实现类
 * @author: 沈煜辉
 * @create: 2020-01-29 14:31
 **/
@Service
public class CmsSiteServiceImpl implements CmsSiteService {

    @Autowired
    CmsSiteRepository cmsSiteRepository;

    @Override
    public QueryResponseResult findAllSite() {
        List<CmsSite> cmsSites = cmsSiteRepository.findAll();
        QueryResult queryResult = new QueryResult();
        queryResult.setList(cmsSites);
        queryResult.setTotal(cmsSites.size());
        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }
}
