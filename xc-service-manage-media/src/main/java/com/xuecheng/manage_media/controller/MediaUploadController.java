package com.xuecheng.manage_media.controller;

import com.xuecheng.api.media.MediaUploadControllerApi;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.service.MediaUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description: 媒资文件上传实现类
 * @author: 沈煜辉
 * @create: 2020-03-12 14:46
 **/
@RestController
@RequestMapping("/media/upload")
public class MediaUploadController implements MediaUploadControllerApi {

    @Autowired
    MediaUploadService mediaUploadService;

    @PostMapping("/register")
    @Override
    public ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        return mediaUploadService.register(fileMd5,fileName,fileSize,mimetype,fileExt);
    }

    @PostMapping("/checkchunk")
    @Override
    public CheckChunkResult checkchunk(String fileMd5, String chunk, Long chunkSize) {
        return mediaUploadService.checkchunk(fileMd5,chunk,chunkSize);
    }

    @Override
    @PostMapping("/uploadchunk")
    public ResponseResult uploadchunk(MultipartFile file, Integer chunk, String fileMd5) {
        return mediaUploadService.uploadchunk(file,chunk,fileMd5);
    }

    @Override
    @PostMapping("/mergechunks")
    public ResponseResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        return mediaUploadService.mergechunks(fileMd5,fileName,fileSize,mimetype,fileExt);
    }

    @GetMapping("/mediaProcess/{id}")
    public ResponseResult mediaProcess(@PathVariable("id") String id){
        return mediaUploadService.sendProcessVideoMsg(id);
    }
}
