package com.xuecheng.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CoursePreviewResult;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage.config.RabbitMqConfig;
import com.xuecheng.manage.repository.CmsPageRepository;
import com.xuecheng.manage.repository.CmsSiteRepository;
import com.xuecheng.manage.service.CmsPageService;
import com.xuecheng.manage.service.CmsTemplateService;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @description: cms业务接口实现类
 * @author: 沈煜辉
 * @create: 2019-12-22 22:58
 **/
@Service
public class CmsPageServiceImpl implements CmsPageService {

    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    CmsTemplateService cmsTemplateService;
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    GridFSBucket gridFSBucket;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    CmsSiteRepository cmsSiteRepository;

    @Override
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if(queryPageRequest == null){
            queryPageRequest = new QueryPageRequest();
        }
        CmsPage cmsPage = new CmsPage();
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("siteId", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("templateId",ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        if(!StringUtils.isEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if (!StringUtils.isEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        if (!StringUtils.isEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        Example example = Example.of(cmsPage,exampleMatcher);
        if (page <= 0) {
            page=0;
        }else{
            page = page -1;
        }
        if(size<=0){
            size=10;
        }
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> cmsPages = cmsPageRepository.findAll(example,pageable);
        QueryResult queryResult = new QueryResult();
        queryResult.setList(cmsPages.getContent());
        queryResult.setTotal(cmsPages.getTotalPages());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }

    @Override
    public CmsPageResult saveCmsPage(CmsPage cmsPage) {
        CmsPage searchCmsPage = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(
                cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if(searchCmsPage != null){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        cmsPage.setPageId(null);
        CmsPage save = cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS,save);
    }

    @Override
    public CmsPage findById(String id) {
        Optional<CmsPage> optionalCmsPage = cmsPageRepository.findById(id);
        if(optionalCmsPage.isPresent()){
            CmsPage cmsPage = optionalCmsPage.get();
            return cmsPage;
        }
        return null;
    }

    @Override
    public CmsPageResult updateCmsPage(String id, CmsPage cmsPage) {
        CmsPage cmsPagePut = findById(id);
        if(cmsPagePut != null){
            cmsPagePut.setSiteId(cmsPage.getSiteId());
            cmsPagePut.setTemplateId(cmsPage.getTemplateId());
            cmsPagePut.setPageName(cmsPage.getPageName());
            cmsPagePut.setPageAliase(cmsPage.getPageAliase());
            cmsPagePut.setPageWebPath(cmsPage.getPageWebPath());
            cmsPagePut.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            cmsPagePut.setPageType(cmsPage.getPageType());
            cmsPagePut.setPageCreateTime(cmsPage.getPageCreateTime());
            cmsPagePut.setDataUrl(cmsPage.getDataUrl());
            CmsPage save = cmsPageRepository.save(cmsPagePut);
            if (save != null){
                return new CmsPageResult(CommonCode.SUCCESS,save);
            }
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    @Override
    public ResponseResult deleteCmsPage(String id) {
        CmsPage cmsPage = findById(id);
        if (cmsPage != null){
            cmsPageRepository.delete(cmsPage);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    @Override
    public String getPageHtml(String pageId){
        Map map = getModelByDataUrl(pageId);
        if (map == null){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        String template = getTemplateByPageId(pageId);
        if (StringUtils.isEmpty(template)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        String generateHtml = generateHtml(template, map);
        if(StringUtils.isEmpty(generateHtml)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        return generateHtml;
    }

    @Override
    public ResponseResult post(String pageId) {
        String pageHtml = getPageHtml(pageId);
        if (StringUtils.isEmpty(pageHtml)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_SAVEHTMLERROR);
        }
        sendPostPage(pageId);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 保存页面信息，有则更新，无则添加
     * @param cmsPage
     * @return
     */
    @Override
    public CmsPageResult save(CmsPage cmsPage) {
        CmsPage findCmsPage = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if (findCmsPage != null){
            return updateCmsPage(findCmsPage.getPageId(),cmsPage);
        }
        return saveCmsPage(cmsPage);
    }

    @Override
    public CoursePreviewResult coursePost(CmsPage cmsPage) {
        CmsPageResult cmsPageResult = save(cmsPage);
        if (!cmsPageResult.isSuccess()){
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_CDETAILERROR);
        }
        CmsPage page = cmsPageResult.getCmsPage();
        String pageId = page.getPageId();
        ResponseResult responseResult = post(pageId);
        if (!responseResult.isSuccess()){
            ExceptionCast.cast(CourseCode.COURSE_POST_ERROR);
        }
        Optional<CmsSite> siteOptional = cmsSiteRepository.findById(page.getSiteId());
        if (!siteOptional.isPresent()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        CmsSite cmsSite = siteOptional.get();
        String siteDomain = cmsSite.getSiteDomain();
        String sitePort = cmsSite.getSitePort();
        String pageWebPath = page.getPageWebPath();
        String pageName = page.getPageName();
        String pageUrl = siteDomain+":"+sitePort+pageWebPath+pageName;
        return new CoursePreviewResult(CommonCode.SUCCESS,pageUrl);
    }

    /**
     * 发送页面发布消息
     * @param pageId
     */
    public void sendPostPage(String pageId){
        String pageHtml = getPageHtml(pageId);
        CmsPage cmsPage = saveCmsPage(pageId, pageHtml);
        Map<String,String> map = new HashMap<>();
        map.put("pageId", pageId);
        String s = JSON.toJSONString(map);
        rabbitTemplate.convertAndSend(RabbitMqConfig.EX_ROUTING_CMS_POSTPAGE,cmsPage.getSiteId(),s);
    }

    /**
     * 静态化html文件
     * @param pageId 页面ID
     * @param pageHtml 页面的html文件
     * @return
     */
    public CmsPage saveCmsPage(String pageId,String pageHtml){
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_NOTEXISTSNAME);
        }
        CmsPage cmsPage = optional.get();
        String htmlFileId = cmsPage.getHtmlFileId();
        if(!StringUtils.isEmpty(htmlFileId)){
            gridFsTemplate.delete(Query.query(Criteria.where("_id").is(htmlFileId)));
        }
        InputStream stream = null;
        try {
            stream = IOUtils.toInputStream(pageHtml, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectId id = gridFsTemplate.store(stream, cmsPage.getPageName());
        cmsPage.setHtmlFileId(String.valueOf(id));
        cmsPageRepository.save(cmsPage);
        return cmsPage;
    }

    /**
     * 根据页面Id获取数据模型
     * @param pageId 页面ID
     * @return 数据模型
     */
    private Map getModelByDataUrl(String pageId){
        CmsPage cmsPage = findById(pageId);
        if (cmsPage == null){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_NOTEXISTSNAME);
        }
        String dataUrl = cmsPage.getDataUrl();
        if (StringUtils.isEmpty(dataUrl)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return body;
    }

    /**
     * 根据页面ID获取模板
     * @param pageId 页面Id
     * @return 模板
     */
    private String getTemplateByPageId(String pageId) {
        CmsPage cmsPage = findById(pageId);
        if (cmsPage == null){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_NOTEXISTSNAME);
        }
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        CmsTemplate cmsTemplate = cmsTemplateService.findTemplateById(templateId);
        String templateFileId = cmsTemplate.getTemplateFileId();
        if (StringUtils.isEmpty(templateFileId)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        GridFSFile gridFSFile =
                gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
        GridFSDownloadStream gridFSDownloadStream =
                gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
        String s = null;
        try {
            s = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 将模板和数据模型输出，生成静态页面
     * @param template 模板
     * @param map 数据模型
     * @return 静态页面
     * @throws IOException
     * @throws TemplateException
     */
    private String generateHtml(String template,Map map){
        Configuration configuration = new Configuration(Configuration.getVersion());
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",template);
        configuration.setTemplateLoader(stringTemplateLoader);
        String html = null;
        try {
            Template template1 = configuration.getTemplate("template");
            html = FreeMarkerTemplateUtils.processTemplateIntoString(template1,map);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return html;
    }
}
