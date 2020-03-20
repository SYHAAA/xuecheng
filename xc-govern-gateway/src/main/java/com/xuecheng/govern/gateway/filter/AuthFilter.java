package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.govern.gateway.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: 认证过滤器
 * @author: 沈煜辉
 * @create: 2020-03-18 13:02
 **/
@Component
public class AuthFilter extends ZuulFilter {

    @Autowired
    AuthService authService;

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
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String token = authService.getTokenFromCookie(request);
//        如果从cookie中取不到令牌，拒接访问
        if (StringUtils.isEmpty(token)){
            access_denied();
            return null;
        }
        boolean fromRedis = authService.getTokenFromRedis(request);
        if (!fromRedis){
            access_denied();
            return null;
        }
        String header = request.getHeader("Authorization");
        if (StringUtils.isEmpty(header)){
            access_denied();
            return null;
        }
        return null;
    }

    private void access_denied(){
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletResponse response = requestContext.getResponse();
        requestContext.setResponseStatusCode(200);
        requestContext.setSendZuulResponse(false);
        ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHENTICATED);
        String string = JSON.toJSONString(responseResult);
        requestContext.setResponseBody(string);
        response.setContentType("application/json;charset=utf-8");
    }
}
