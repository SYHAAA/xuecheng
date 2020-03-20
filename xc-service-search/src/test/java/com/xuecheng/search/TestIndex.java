package com.xuecheng.search;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteAction;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.rest.RestStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 测试类
 * @author: 沈煜辉
 * @create: 2020-03-07 19:00
 **/
@SpringBootTest(classes = SearchApplication.class)
@RunWith(SpringRunner.class)
public class TestIndex {

    @Autowired
    RestHighLevelClient restHighLevelClient;
    @Autowired
    RestClient restClient;


    /**
     * 删除索引
     * @throws IOException
     */
    @Test
    public void testDelIndex() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("xc-course");
        IndicesClient indices = restHighLevelClient.indices();
        DeleteIndexResponse response = indices.delete(deleteIndexRequest);
        boolean acknowledged = response.isAcknowledged();
        System.out.println(acknowledged);
    }

    /**
     * 测试添加索引库
     * @throws IOException
     */
    @Test
    public void testCreateIndex() throws IOException {
        CreateIndexRequest createIndexRequest =
                new CreateIndexRequest("xc-course")
                        .settings(Settings.builder()
                        .put("number_of_shards",1)
                        .put("number_of_replicas",0));
        createIndexRequest.mapping("doc","{\n" +
                "\t\"properties\":{\n" +
                "\t\t\"name\":{\n" +
                "\t\t\t\"type\":\"text\",\n" +
                "\t\t\t\"analyzer\":\"ik_max_word\",\n" +
                "\t\t\t\"search_analyzer\":\"ik_smart\"\n" +
                "\t\t},\n" +
                "\t\t\"description\":{\n" +
                "\t\t\t\"type\":\"text\",\n" +
                "\t\t\t\"analyzer\":\"ik_max_word\",\n" +
                "\t\t\t\"search_analyzer\":\"ik_smart\"\n" +
                "\t\t},\n" +
                "\t\t\"pic\":{\n" +
                "\t\t\t\"type\":\"text\",\n" +
                "\t\t\t\"index\":false\n" +
                "\t\t},\n" +
                "\t\t\"studymodel\":{\n" +
                "\t\t\t\"type\":\"text\"\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}",XContentType.JSON);
        IndicesClient client = restHighLevelClient.indices();
        CreateIndexResponse response = client.create(createIndexRequest);
        boolean acknowledged = response.isShardsAcknowledged();
        System.out.println(acknowledged);
    }

    /**
     * 测试添加文档
     * @throws IOException
     */
    @Test
    public void testAddDoc() throws IOException {
        IndexRequest indexRequest = new IndexRequest("xc-course","doc");
        Map<String,Object> map = new HashMap();
        map.put("name","java开发入门到放弃");
        map.put("description","本课程主要从四个章节进行讲解： 1.微服务架构入门 2.spring cloud  基础入门 3.实战Spring Boot 4.注册中心eureka");
        map.put("pic","group/asdjalkj");
        map.put("studymodel","201001");
        IndexRequest source = indexRequest.source(map);
        IndexResponse response = restHighLevelClient.index(indexRequest);
        DocWriteResponse.Result result = response.getResult();
        System.out.println(result);
    }

    /**
     * 测试查询文档
     * @throws IOException
     */
    @Test
    public void testGetDoc() throws IOException {
        GetRequest getRequest = new GetRequest("xc-course","doc","C1EhuHAB4P_Qi7ilQ83C");
        GetResponse response = restHighLevelClient.get(getRequest);
        Map<String, Object> map = response.getSourceAsMap();
        System.out.println(map);
    }

    /**
     * 测试更新文档
     * @throws IOException
     */
    @Test
    public void testUpdateDoc() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("xc-course","doc","C1EhuHAB4P_Qi7ilQ83C");
        Map<String,Object> map = new HashMap<>();
        map.put("pic","group1/M00/00/00/wKgZhV5gfMmALJSsAA3s6Rw4mEc742.jpg");
        updateRequest.doc(map);
        UpdateResponse response = restHighLevelClient.update(updateRequest);
        RestStatus status = response.status();
        System.out.println(status);
    }

    /**
     * 测试删除文档
     * @throws IOException
     */
    @Test
    public void testDelDoc() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("xc-course","doc","C1EhuHAB4P_Qi7ilQ83C");
        DeleteResponse delete = restHighLevelClient.delete(deleteRequest);
        DocWriteResponse.Result result = delete.getResult();
        System.out.println(result);
    }


}
