package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description: 异常捕获类
 * @author: 沈煜辉
 * @create: 2020-01-31 16:47
 **/
@ControllerAdvice
public class ExceptionCatch {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);

    //使用EXCEPTIONS存放异常类型和错误代码的映射，ImmutableMap的特点的一旦创建不可改变，并且线程安全
    private static ImmutableMap<Class<? extends Throwable>,ResultCode> EXCEPTIONS;
    //使用builder来构建一个异常类型和错误代码的异常
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder = ImmutableMap.builder();
    /**
     * 预知异常响应
     * @param c 自定义异常
     * @return
     */
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customException(CustomException c){
        LOGGER.error("catch exception:{}]\r\nexception:",c.getResultCode().message());
        ResultCode resultCode = c.getResultCode();
        return new ResponseResult(resultCode);
    }

    /**
     *
     * 非预知异常使用的是ImmutableMap存储，里面放置的是异常类型和错误代码，两者一一对应，知道了异常类型，就可以从中取出错位代码，从而响应出去
     * 非预知异常响应
     * @param e 系统异常
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult Exception(Exception e){
        LOGGER.error("catch exception:{}]exception:",e.getMessage());
        if (EXCEPTIONS == null){
            EXCEPTIONS = builder.build();
        }
        System.out.println(e.getClass());
        final ResultCode resultCode = EXCEPTIONS.get(e.getClass());
        final ResponseResult responseResult;
        if (resultCode != null){
            responseResult = new ResponseResult(resultCode);
        }else{
            responseResult = new ResponseResult(CommonCode.SERVER_ERROR);
        }
        return responseResult;
    }

    static {
        builder.put(HttpMessageNotReadableException.class,CommonCode.INVALIDPARAM);
    }
}
