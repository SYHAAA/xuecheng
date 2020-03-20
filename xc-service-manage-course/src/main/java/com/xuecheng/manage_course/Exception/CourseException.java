package com.xuecheng.manage_course.Exception;

import com.xuecheng.framework.exception.ExceptionCatch;
import com.xuecheng.framework.model.response.CommonCode;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @description: 课程自定义异常类
 * @author: 沈煜辉
 * @create: 2020-03-19 14:40
 **/
@ControllerAdvice
public class CourseException extends ExceptionCatch {

    static {
        builder.put(AccessDeniedException.class, CommonCode.UNAUTHORISE);
    }

}
