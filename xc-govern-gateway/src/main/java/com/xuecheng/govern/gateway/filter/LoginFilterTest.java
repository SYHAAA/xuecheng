package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: 登录过滤器
 * @author: 沈煜辉
 * @create: 2020-03-18 12:32
 **/
public class LoginFilterTest extends ZuulFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginFilterTest.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        HttpServletResponse response = requestContext.getResponse();
        String header = request.getHeader("Authorization");
        if (StringUtils.isEmpty(header)){
            requestContext.setResponseStatusCode(200);
            requestContext.setSendZuulResponse(false);
            ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHENTICATED);
            String string = JSON.toJSONString(responseResult);
            requestContext.setResponseBody(string);
            response.setContentType("application/json;charset=utf-8");
            return null;
        }
        return null;
    }
}
