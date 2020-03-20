package com.xuecheng.api.media;

import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description: 媒资文件上传接口
 * @author: 沈煜辉
 * @create: 2020-03-12 14:30
 **/
@Api(value = "媒资文件上传接口",tags = "媒资文件上传接口")
public interface MediaUploadControllerApi {

    /**
     * 上传文件前的注册功能
     * @param fileMd5 文件id
     * @param fileName 文件名称
     * @param fileSize 文件大小
     * @param mimetype 文件类型
     * @param fileExt 文件扩展名
     * @return
     */
     ResponseResult register(String fileMd5,
                             String fileName,
                             Long fileSize,
                             String mimetype,
                             String fileExt);

     /**
     * 检查文件分块信息，实现断点续传
     * @param fileMd5 文件ID
     * @param chunk 当前分块下标
     * @param chunkSize 分块大小
     * @return
     */
     CheckChunkResult checkchunk(String fileMd5,
                                 String chunk,
                                 Long chunkSize);

    /**
     * 上传分块文件
     * @param file 分块文件
     * @param chunk
     * @param fileMd5
     * @return
     */
    ResponseResult uploadchunk(MultipartFile file,
                               Integer chunk,
                               String fileMd5);
    /**
     * 合并分块信息
     * @param fileMd5
     * @param fileName
     * @param fileSize
     * @param mimetype
     * @param fileExt
     * @return
     */
     ResponseResult mergechunks(String fileMd5,
                                String fileName,
                                Long fileSize,
                                String mimetype,
                                String fileExt);
}
