package com.xuecheng.api.filesystem;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description: 文件管理接口
 * @author: 沈煜辉
 * @create: 2020-03-04 14:39
 **/
@Api(value = "文件管理接口")
public interface FileSystemControllerApi {

    /**
     * 文件上传接口
     * @param file 文件
     * @param businesskey 业务key
     * @param filetag 文件标签
     * @param metadata 元信息
     * @return
     */
    UploadFileResult fileUpload(MultipartFile file,String businesskey,String filetag,String metadata);
}
