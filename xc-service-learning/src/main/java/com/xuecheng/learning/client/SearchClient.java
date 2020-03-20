package com.xuecheng.learning.client;

import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @description: 调用搜索服务端接口
 * @author: 沈煜辉
 * @create: 2020-03-14 14:41
 **/
@FeignClient(value = XcServiceList.XC_SERVICE_SEARCH)
public interface SearchClient {

    @GetMapping("/search/course/getmedia/{teachplanId}")
    TeachplanMediaPub getMeida(@PathVariable("teachplanId") String teachplanId);
}
