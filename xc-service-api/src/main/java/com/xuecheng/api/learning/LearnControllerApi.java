package com.xuecheng.api.learning;

import com.xuecheng.framework.domain.learning.response.GetMediaResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @description: 学习模块接口
 * @author: 沈煜辉
 * @create: 2020-03-14 14:27
 **/
@Api(value = "学习模块接口",tags = "学习模块接口")
public interface LearnControllerApi {

    @ApiOperation(value = "获取视频播放地址")
    GetMediaResult getmedia(String courseId,String teachplanId);
}
