package com.xuecheng.search.service;

import com.github.pagehelper.util.StringUtil;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import org.apache.lucene.search.highlight.Highlighter;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @description: 课程搜索业务
 * @author: 沈煜辉
 * @create: 2020-03-09 15:29
 **/
@Service
public class EsCourseService {

    @Autowired
    RestHighLevelClient restHighLevelClient;
    @Autowired
    RestClient restClient;
    @Value("${xuecheng.elasticsearch.course.index}")
    private String index;
    @Value("${xuecheng.elasticsearch.course.type}")
    private String type;
    @Value("${xuecheng.elasticsearch.course.source_field}")
    private String source_field;

    @Value("${xuecheng.elasticsearch.media.index}")
    private String media_index;
    @Value("${xuecheng.elasticsearch.media.type}")
    private String media_type;
    @Value("${xuecheng.elasticsearch.media.source_field}")
    private String media_source_field;

    private final static Logger LOGGER = LoggerFactory.getLogger(EsCourseService.class);

    public QueryResponseResult list(Integer page, Integer size, CourseSearchParam courseSearchParam) {
        /*if (page<=0||page==null){
            page = 1;
        }
        if (size<=20||size==null){
            size = 20;
        }*/
        if (courseSearchParam == null){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//        进行关键字搜素
        if (StringUtil.isNotEmpty(courseSearchParam.getKeyword())){
            MultiMatchQueryBuilder multiMatchQueryBuilder =
                    QueryBuilders.multiMatchQuery(courseSearchParam.getKeyword(),"name","description","teachplan");
            multiMatchQueryBuilder.field("name",10);
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }
//        按分类信息搜索
        if (StringUtil.isNotEmpty(courseSearchParam.getMt())){
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("mt",courseSearchParam.getMt());
            boolQueryBuilder.filter(termQueryBuilder);
        }
        if (StringUtil.isNotEmpty(courseSearchParam.getSt())){
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("st",courseSearchParam.getSt());
            boolQueryBuilder.filter(termQueryBuilder);
        }
        if (StringUtil.isNotEmpty(courseSearchParam.getGrade())){
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("grade",courseSearchParam.getGrade());
            boolQueryBuilder.filter(termQueryBuilder);
        }
//        设置分页参数
        searchSourceBuilder.from((page-1)*size);
        searchSourceBuilder.size(size);
//        设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        searchSourceBuilder.highlighter(highlightBuilder);
        searchSourceBuilder.query(boolQueryBuilder);
        String[] source_id = source_field.split(",");
        searchSourceBuilder.fetchSource(source_id,new String[]{});
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = null;
        try {
            search = restHighLevelClient.search(searchRequest);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("xuecheng search error..{}",e.getMessage());
            return new QueryResponseResult(CommonCode.SUCCESS,new QueryResult());
        }
        QueryResult queryResult = new QueryResult();
        SearchHits hits = search.getHits();
        long totalHits = hits.getTotalHits();
        queryResult.setTotal(totalHits);
        SearchHit[] searchHits = hits.getHits();
        List list = new ArrayList();
        String name = null;
        for (SearchHit searchHit : searchHits) {
            CoursePub coursePub = new CoursePub();
            Map<String, Object> map = searchHit.getSourceAsMap();
            name = (String) map.get("name");
//            取高亮字段
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            if (highlightFields != null || highlightFields.size()!=0){
                HighlightField field = highlightFields.get("name");
                if (field != null){
                    Text[] fragments = field.getFragments();
                    StringBuffer stringBuffer = new StringBuffer();
                    for (Text text : fragments) {
                        stringBuffer.append(text);
                    }
                    name = stringBuffer.toString();
                }
            }
            coursePub.setName(name);
            coursePub.setId((String) map.get("id"));
            coursePub.setPic((String) map.get("pic"));
            coursePub.setPrice((Double) map.get("price"));
            coursePub.setPrice_old((Double) map.get("price_old"));
            list.add(coursePub);
        }
        queryResult.setList(list);
        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }

    public Map<String, CoursePub> getAll(String id) {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder termQuery = QueryBuilders.termQuery("id", id);
        searchSourceBuilder.query(termQuery);
        searchSourceBuilder.fetchSource(new String[]{"id","name","grade","charge","pic","teachplan","description"},new String[]{});
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = null;
        try {
            search = restHighLevelClient.search(searchRequest);
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionCast.cast(CommonCode.FAIL);
        }
        SearchHits hits = search.getHits();
        SearchHit[] searchHits = hits.getHits();
        Map<String,CoursePub> getMap = new HashMap<>();
        for (SearchHit searchHit : searchHits) {
            CoursePub coursePub = new CoursePub();
            Map<String,Object> map = searchHit.getSourceAsMap();
            coursePub.setId((String) map.get("id"));
            coursePub.setName((String) map.get("name"));
            coursePub.setGrade((String) map.get("grade"));
            coursePub.setCharge((String) map.get("charge"));
            coursePub.setPic((String) map.get("pic"));
            coursePub.setTeachplan((String) map.get("teachplan"));
            coursePub.setDescription((String) map.get("description"));
            getMap.put(searchHit.getId(),coursePub);
        }
        return getMap;
    }

    public QueryResult getmedia(String[] teachplanIds) {
        SearchRequest searchRequest = new SearchRequest(media_index);
        searchRequest.types(media_type);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermsQueryBuilder termsQuery = QueryBuilders.termsQuery("teachplan_id", teachplanIds);
        String[] split = media_source_field.split(",");
        searchSourceBuilder.fetchSource(split,new String[]{});
        searchSourceBuilder.query(termsQuery);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = null;
        try {
            search = restHighLevelClient.search(searchRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (search==null){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        SearchHits hits = search.getHits();
        SearchHit[] searchHits = hits.getHits();
        List<TeachplanMediaPub> list = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
            teachplanMediaPub.setTeachplanId((String) sourceAsMap.get("teachplan_id"));
            teachplanMediaPub.setCourseId((String) sourceAsMap.get("courseid"));
            teachplanMediaPub.setMediaId((String) sourceAsMap.get("media_id"));
            teachplanMediaPub.setMediaFileOriginalName((String) sourceAsMap.get("media_fileoriginalname"));
            teachplanMediaPub.setMediaUrl((String) sourceAsMap.get("media_url"));
            list.add(teachplanMediaPub);
        }
        QueryResult queryResult = new QueryResult();
        queryResult.setTotal(hits.getTotalHits());
        queryResult.setList(list);
        return queryResult;
    }
}
