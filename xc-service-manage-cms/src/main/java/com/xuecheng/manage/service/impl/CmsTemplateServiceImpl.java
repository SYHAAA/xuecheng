package com.xuecheng.manage.service.impl;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage.repository.CmsTemplateRepository;
import com.xuecheng.manage.service.CmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * @description: cms模板业务具体实现类
 * @author: 沈煜辉
 * @create: 2020-01-31 13:03
 **/
@Service
public class CmsTemplateServiceImpl implements CmsTemplateService {

    @Autowired
    CmsTemplateRepository cmsTemplateRepository;

    @Override
    public QueryResponseResult findAllTemplate() {
        List<CmsTemplate> templates = cmsTemplateRepository.findAll();
        if (templates != null){
            QueryResult queryResult = new QueryResult();
            queryResult.setList(templates);
            queryResult.setTotal(templates.size());
            QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
            return queryResponseResult;
        }
        return new QueryResponseResult(CommonCode.FAIL,null);
    }

    @Override
    public CmsTemplate findTemplateById(String templateId) {
        if (StringUtils.isEmpty(templateId)){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_NOTEXISTSPARAM);
        }
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("templateId", ExampleMatcher.GenericPropertyMatchers.exact());
        CmsTemplate cmsTemplate = new CmsTemplate();
        cmsTemplate.setTemplateId(templateId);
        Example<CmsTemplate> example = Example.of(cmsTemplate,exampleMatcher);
        Optional<CmsTemplate> optional = cmsTemplateRepository.findOne(example);
        if(!optional.isPresent()){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        return optional.get();
    }
}
