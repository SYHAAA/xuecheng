package com.xuecheng.framework.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @description: fegin调用拦截器
 * @author: 沈煜辉
 * @create: 2020-03-19 16:24
 **/
public class FeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()){
            String name = headers.nextElement();
            String value = request.getHeader(name);
            if ("authorization".equals(name)){
                requestTemplate.header(name,value);
            }
        }
    }
}
