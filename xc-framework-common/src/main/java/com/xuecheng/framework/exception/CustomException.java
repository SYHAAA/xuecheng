package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * @description: 用户自定义异常类
 * @author: 沈煜辉
 * @create: 2020-01-31 16:41
 **/
public class CustomException extends RuntimeException{
    private ResultCode resultCode;

    public CustomException(ResultCode resultCode){
        super("错误代码："+resultCode.code()+"错误信息："+resultCode.message());
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return this.resultCode;
    }
}
