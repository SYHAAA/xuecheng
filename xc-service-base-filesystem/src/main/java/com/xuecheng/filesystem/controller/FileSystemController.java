package com.xuecheng.filesystem.controller;

import com.xuecheng.api.filesystem.FileSystemControllerApi;
import com.xuecheng.filesystem.service.FileSystemService;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description: 文件管理实现类
 * @author: 沈煜辉
 * @create: 2020-03-04 14:43
 **/
@RequestMapping("/filesystem")
@RestController
public class FileSystemController implements FileSystemControllerApi {

    @Autowired
    FileSystemService fileSystemService;

    @Override
    @PostMapping("/upload")
    public UploadFileResult fileUpload(MultipartFile file, String businesskey, String filetag, String metadata) {
        return fileSystemService.fileUpload(file,businesskey,filetag,metadata);
    }
}
