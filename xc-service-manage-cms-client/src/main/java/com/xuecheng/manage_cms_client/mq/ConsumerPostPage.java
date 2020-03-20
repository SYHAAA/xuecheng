package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.mongodb.MongoClient;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms_client.config.RabbitMqConfig;
import com.xuecheng.manage_cms_client.repository.CmsPageRepository;
import com.xuecheng.manage_cms_client.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * @description: 监听消息队列
 * @author: 沈煜辉
 * @create: 2020-02-06 12:01
 **/
@Component
public class ConsumerPostPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerPostPage.class);

    @Autowired
    PageService pageService;
    @Autowired
    CmsPageRepository cmsPageRepository;

    @RabbitListener(queues = RabbitMqConfig.QUEUE_CMS_POSTPAGE)
    public void postPage(String msg){
        Map map = JSON.parseObject(msg, Map.class);
        String pageId = (String) map.get("pageId");
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if(!optional.isPresent()){
            LOGGER.error("receive cms post page,cmsPage is null:{}",msg.toString());
        }
        pageService.savePageToServerPath(pageId);
    }
}
