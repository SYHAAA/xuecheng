package com.xuecheng.manage_cms_client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_cms_client.repository.CmsPageRepository;
import com.xuecheng.manage_cms_client.repository.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * @description: cmsPage业务类
 * @author: 沈煜辉
 * @create: 2020-02-06 11:30
 **/
@Service
public class PageService {
    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    CmsSiteRepository cmsSiteRepository;

    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    GridFSBucket gridFSBucke;

    public void savePageToServerPath(String pageId){
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALIDPARAM);
        }
        CmsPage cmsPage = optional.get();
        String pagePhysicalPath = cmsPage.getPagePhysicalPath()+cmsPage.getPageName();
        InputStream inputStream = getFileById(cmsPage.getHtmlFileId());
        FileOutputStream fos = null;
        try {
            String s = IOUtils.toString(inputStream, "utf-8");
            File file = new File(pagePhysicalPath);
            fos = new FileOutputStream(file);
            fos.write(s.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private InputStream getFileById(String fileId){
        GridFSFile gridFSFile =
                gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        GridFSDownloadStream gridFSDownloadStream =
                gridFSBucke.openDownloadStream(gridFSFile.getObjectId());
        GridFsResource resource = new GridFsResource(gridFSFile,gridFSDownloadStream);
        try {
            InputStream inputStream = resource.getInputStream();
            return inputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
