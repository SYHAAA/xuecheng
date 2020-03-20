package com.xuecheng.framework.domain.learning.response;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @description: 获取学习播放视频响应类
 * @author: 沈煜辉
 * @create: 2020-03-14 14:29
 **/
@Data
@ToString
public class GetMediaResult extends ResponseResult {
    @ApiModelProperty(value = "视频播放地址", example = "true", required = true)
    String mediaUrl;

    public GetMediaResult(ResultCode resultCode,String mediaUrl){
        super(resultCode);
        this.mediaUrl = mediaUrl;
    }
}
