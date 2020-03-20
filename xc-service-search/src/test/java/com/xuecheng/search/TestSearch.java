package com.xuecheng.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @description: 测试搜索
 * @author: 沈煜辉
 * @create: 2020-03-08 12:57
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSearch {

    @Autowired
    RestHighLevelClient restHighLevelClient;
    @Autowired
    RestClient restClient;

    /**
     * 测试搜索全部记录
     * @throws IOException
     */
    @Test
    public void testMatchAll() throws IOException {
//        创建搜索对象
        SearchRequest searchRequest = new SearchRequest("xc-course");
//        设置搜索类型
        searchRequest.types("doc");
//        创建搜素参数对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
//        searchSourceBuilder.fetchSource(new String[]{"name","studymodel"},new String[]{});

//        进行排序
        searchSourceBuilder.sort(new FieldSortBuilder("studymodel").order(SortOrder.DESC));
        searchSourceBuilder.sort(new FieldSortBuilder("price").order(SortOrder.ASC));
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest);
        SearchHit[] hits = search.getHits().getHits();
        System.out.println("搜索结果个数："+hits.length);
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            for (Map.Entry<String, Object> stringObjectEntry : sourceAsMap.entrySet()) {
                System.out.println(stringObjectEntry.getKey()+":"+stringObjectEntry.getValue());
            }
            System.out.println();
        }
    }

    /**
     * 测试分页搜索
     * @throws IOException
     */
    @Test
    public void testMatchAllWithPage() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc-course");
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel"},new String[]{});
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(1);
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest);
        SearchHit[] searchHits = response.getHits().getHits();
        System.out.println(searchHits.length);
        for (SearchHit searchHit : searchHits) {
            Map<String, Object> map = searchHit.getSourceAsMap();
            for (String s : map.keySet()) {
                System.out.println(map.get(s));
            }
        }
    }

    /**
     * 测试精确查询
     * @throws IOException
     */
    @Test
    public void testTermQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc-course");
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("name","spring"));
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest);
        SearchHit[] hits = search.getHits().getHits();
        System.out.println("搜索结果个数："+hits.length);
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            for (Map.Entry<String, Object> stringObjectEntry : sourceAsMap.entrySet()) {
                System.out.println(stringObjectEntry.getKey()+":"+stringObjectEntry.getValue());
            }
        }
    }

    /**
     * 通过id查询
     * @throws IOException
     */
    @Test
    public void testQueryId() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc-course");
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        String[] ids = new String[]{"1","3"};
        searchSourceBuilder.query(QueryBuilders.termsQuery("_id",ids));
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest);
        SearchHit[] hits = search.getHits().getHits();
        System.out.println("搜索结果个数："+hits.length);
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            for (Map.Entry<String, Object> stringObjectEntry : sourceAsMap.entrySet()) {
                System.out.println(stringObjectEntry.getKey()+":"+stringObjectEntry.getValue());
            }
            System.out.println();
        }
    }

    /**
     * 测试matchQuery运算符查询
     * @throws IOException
     */
    @Test
    public void testMatchQueryOperator() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc-course");
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(
                QueryBuilders.matchQuery("description","spring java")
                        .operator(Operator.AND));
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest);
        SearchHit[] hits = search.getHits().getHits();
        System.out.println("搜索结果个数："+hits.length);
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            for (Map.Entry<String, Object> stringObjectEntry : sourceAsMap.entrySet()) {
                System.out.println(stringObjectEntry.getKey()+":"+stringObjectEntry.getValue());
            }
            System.out.println();
        }
    }

    /**
     * 测试matchQuery比率查询
     * @throws IOException
     */
    @Test
    public void testMatchQueryMin() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc-course");
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(
                QueryBuilders.matchQuery("description","spring java 开发")
                        .minimumShouldMatch("80%"));
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest);
        SearchHit[] hits = search.getHits().getHits();
        System.out.println("搜索结果个数："+hits.length);
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            for (Map.Entry<String, Object> stringObjectEntry : sourceAsMap.entrySet()) {
                System.out.println(stringObjectEntry.getKey()+":"+stringObjectEntry.getValue());
            }
            System.out.println();
        }
    }

    /**
     * 测试MulQuery，在多个字段中查询内容
     * @throws IOException
     */
    @Test
    public void testMulQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc-course");
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder multiMatchQueryBuilder =
                QueryBuilders.multiMatchQuery("spring css","name","description")
                        .minimumShouldMatch("50%");
//        增加名字包含内容的比重，能够在先显示
        multiMatchQueryBuilder.field("name",10);
        searchSourceBuilder.query(multiMatchQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest);
        SearchHit[] hits = search.getHits().getHits();
        System.out.println("搜索结果个数："+hits.length);
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            for (Map.Entry<String, Object> stringObjectEntry : sourceAsMap.entrySet()) {
                System.out.println(stringObjectEntry.getKey()+":"+stringObjectEntry.getValue());
            }
            System.out.println();
        }
    }

    /**
     * 测试bool查询（多个查询条件进行查询）
     * @throws IOException
     */
    @Test
    public void testBoolQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc-course");
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring 框架", "name", "description");
        multiMatchQueryBuilder.field("name",10);
        multiMatchQueryBuilder.minimumShouldMatch("50%");
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("studymodel","201001");
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(multiMatchQueryBuilder);
        boolQueryBuilder.must(termQueryBuilder);
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.fetchSource(new String[]{"name","pic","studymodel","description"},new String[]{});
        searchRequest.source(searchSourceBuilder);

        SearchResponse search = restHighLevelClient.search(searchRequest);
        SearchHit[] hits = search.getHits().getHits();
        System.out.println("搜索结果个数："+hits.length);
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            for (Map.Entry<String, Object> stringObjectEntry : sourceAsMap.entrySet()) {
                System.out.println(stringObjectEntry.getKey()+":"+stringObjectEntry.getValue());
            }
            System.out.println();
        }
    }

    /**
     * 测试过滤器
     * @throws IOException
     */
    @Test
    public void testFilter() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc-course");
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        设置multi查询条件
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring 框架", "name", "description");
        multiMatchQueryBuilder.field("name",10);
        multiMatchQueryBuilder.minimumShouldMatch("50%");
//        创建bool查询对象
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//        绑定要满足的条件
        boolQueryBuilder.must(multiMatchQueryBuilder);
//        bool查询进行过滤
        boolQueryBuilder.filter(QueryBuilders.termQuery("studymodel","201001"));
        searchSourceBuilder.query(boolQueryBuilder);
//        设置显示的字段，不显示什么字段
        searchSourceBuilder.fetchSource(new String[]{"name","pic","studymodel","description"},new String[]{});

        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest);
        SearchHit[] hits = search.getHits().getHits();
        System.out.println("搜索结果个数："+hits.length);
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            for (Map.Entry<String, Object> stringObjectEntry : sourceAsMap.entrySet()) {
                System.out.println(stringObjectEntry.getKey()+":"+stringObjectEntry.getValue());
            }
            System.out.println();
        }
    }

    @Test
    public void test() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc-course");
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring java", "name", "description");
        multiMatchQueryBuilder.field("name",10);
//        searchSourceBuilder.fetchSource(new String[]{"name","description"},new String[]{});
        searchSourceBuilder.query(multiMatchQueryBuilder);
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<tag>");
        highlightBuilder.postTags("</tag>");
//        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        highlightBuilder.fields().add(new HighlightBuilder.Field("description"));
        searchSourceBuilder.highlighter(highlightBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest);
        SearchHit[] hits = search.getHits().getHits();
        System.out.println("搜索结果个数："+hits.length);
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            /*for (Map.Entry<String, Object> stringObjectEntry : sourceAsMap.entrySet()) {
                System.out.println(stringObjectEntry.getKey()+":"+stringObjectEntry.getValue());
            }*/
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlightField = highlightFields.get("description");
            Text[] fragments = highlightField.getFragments();
            StringBuffer stringBuffer = new StringBuffer();
            for (Text fragment : fragments) {
                stringBuffer.append(fragment.string());
            }
            String description = stringBuffer.toString();
            System.out.println(description);
        }
    }
}
