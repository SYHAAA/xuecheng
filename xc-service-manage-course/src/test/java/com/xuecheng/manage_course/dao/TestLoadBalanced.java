package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_course.ManageCourseApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * @description: 测试负载均衡
 * @author: 沈煜辉
 * @create: 2020-03-05 15:34
 **/
@SpringBootTest(classes = ManageCourseApplication.class)
@RunWith(SpringRunner.class)
public class TestLoadBalanced {

    @Autowired
    RestTemplate restTemplate;

    @Test
    public void test(){
        String serviceId="XC-SERVICE-MANAGE-CMS";
        for (int i = 0; i < 10; i++) {
            ResponseEntity<CmsPage> entity = restTemplate.getForEntity("http://"+serviceId+"/cms/get/5a754adf6abb500ad05688d9", CmsPage.class);
            CmsPage cmsPage = entity.getBody();
            System.out.println(cmsPage);
        }
    }
}
