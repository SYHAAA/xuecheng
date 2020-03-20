package com.xuecheng.govern.gateway;

import com.xuecheng.framework.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @description: 认证业务
 * @author: 沈煜辉
 * @create: 2020-03-18 13:04
 **/
@Service
public class AuthService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public String getTokenFromCookie(HttpServletRequest request){
        Map<String, String> cookie = CookieUtil.readCookie(request, "uid");
        String uid = cookie.get("uid");
        if (StringUtils.isEmpty(uid)){
            return null;
        }
        return uid;
    }

    public boolean getTokenFromRedis(HttpServletRequest request){
        String token = getTokenFromCookie(request);
        String key = "user_token:"+token;
        Long expire = stringRedisTemplate.getExpire(key);
        return expire>0;
    }
}
