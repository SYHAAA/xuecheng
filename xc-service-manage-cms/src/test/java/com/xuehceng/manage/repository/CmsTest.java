package com.xuehceng.manage.repository;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.manage.CmsService;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.Map;
import java.util.Set;


/**
 * @description: cmsRepository接口测试类
 * @author: 沈煜辉
 * @create: 2019-12-22 23:06
 **/
@SpringBootTest(classes = CmsService.class)
@RunWith(SpringRunner.class)
public class CmsTest {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    GridFSBucket gridFSBucket;

    @Test
    public void testTemplate(){
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getModel/5a791725dd573c3574ee333f", Map.class);
        Map body = forEntity.getBody();
        System.out.println(body);
    }

    /**
     * gridFs存储文件
     * @throws FileNotFoundException
     */
    @Test
    public void testStore() throws IOException {
        File file = new File("H:/course.ftl");
        InputStream is = new FileInputStream(file);
        ObjectId objectId = gridFsTemplate.store(is, "课程详情页面模板", "");
        System.out.println(objectId.toString());
        is.close();
    }

    /**
     * 读取文件
     */
    @Test
    public void testRead() throws IOException {
        String fileId = "5e37c5d8ea5b2b2f702fded6";
//        查找文件
        GridFSFile gridFSFile =
                gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
//        创建下载流
        GridFSDownloadStream gridFSDownloadStream =
                gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
//        创建gridFsResource，用于获取流对象
        GridFsResource gridFsResource =
                new GridFsResource(gridFSFile,gridFSDownloadStream);
        String s = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
        System.out.println(s);
    }

    @Test
    public void testDel(){
        String fileId = "5e37c5d8ea5b2b2f702fded6";
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is(fileId)));
    }
}
