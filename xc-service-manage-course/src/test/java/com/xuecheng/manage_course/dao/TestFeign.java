package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_course.ManageCourseApplication;
import com.xuecheng.manage_course.client.CmsPageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description: 测试feign接口的调用
 * @author: 沈煜辉
 * @create: 2020-03-05 18:59
 **/
@SpringBootTest(classes = ManageCourseApplication.class)
@RunWith(SpringRunner.class)
public class TestFeign {

    @Autowired
    CmsPageClient cmsPageClient;

    @Test
    public void test1(){
        CmsPage cmsPage = cmsPageClient.findById("5a754adf6abb500ad05688d9");
        System.out.println(cmsPage);
    }
}
