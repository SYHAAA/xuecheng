package com.xuecheng.manage_media.controller;

import com.xuecheng.api.media.MediaFileControllerApi;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_media.service.MediaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 媒资文件接口实现类
 * @author: 沈煜辉
 * @create: 2020-03-13 13:28
 **/
@RestController
@RequestMapping("/media/file/")
public class MediaFileController implements MediaFileControllerApi {

    @Autowired
    MediaFileService mediaFileService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findAll(@PathVariable("page") Integer page,@PathVariable("size") Integer size, QueryMediaFileRequest queryMediaFileRequest) {
        return mediaFileService.findAll(page,size,queryMediaFileRequest);
    }
}
