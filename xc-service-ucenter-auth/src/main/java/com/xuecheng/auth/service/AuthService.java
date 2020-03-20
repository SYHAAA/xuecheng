package com.xuecheng.auth.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.util.StringUtil;
import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.CookieUtil;
import org.bouncycastle.asn1.ua.UAObjectIdentifiers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description: 认证业务类
 * @author: 沈煜辉
 * @create: 2020-03-17 13:14
 **/
@Service
public class AuthService {

    @Autowired
    LoadBalancerClient loadBalancerClient;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Value("${auth.clientId}")
    String clientId;
    @Value("${auth.clientSecret}")
    String clientSecret;
    @Value("${auth.tokenValiditySeconds}")
    int tokenValiditySeconds;
    @Value("${auth.cookieDomain}")
    String cookieDomain;
    @Value("${auth.cookieMaxAge}")
    int cookieMaxAge;

    public LoginResult login(LoginRequest loginRequest) {
        if (loginRequest==null|| StringUtil.isEmpty(loginRequest.getUsername())){
            ExceptionCast.cast(AuthCode.AUTH_USERNAME_NONE);
        }
        if (loginRequest==null||StringUtil.isEmpty(loginRequest.getPassword())){
            ExceptionCast.cast(AuthCode.AUTH_PASSWORD_NONE);
        }
//        获取令牌
        AuthToken authToken = applyToken(loginRequest.getUsername(), loginRequest.getPassword(), clientId, clientSecret);
//        将令牌存入redis
        String jwt_token = authToken.getJwt_token();
        String content = JSON.toJSONString(authToken);
        boolean saveResult = saveToken(jwt_token,content,tokenValiditySeconds);
        if (!saveResult){
            ExceptionCast.cast(AuthCode.AUTH_JWT_SAVEERROR);
        }
        saveCookie(authToken.getJwt_token());
        return new LoginResult(CommonCode.SUCCESS,authToken.getJwt_token());
    }

    /**
     * 将身份临牌保存至cookie中
     * @param jwt_token
     */
    private void saveCookie(String jwt_token) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        CookieUtil.addCookie(response,cookieDomain,"/","uid",jwt_token,cookieMaxAge,false);
    }

    /**
     * 保存令牌至redis中
     * @param accessToken
     * @param content
     * @param tokenValiditySeconds
     * @return
     */
    private boolean saveToken(String accessToken, String content, int tokenValiditySeconds) {
        String  key = "user_token:"+accessToken;
        stringRedisTemplate.boundValueOps(key).set(content,tokenValiditySeconds, TimeUnit.SECONDS);
        Long expire = stringRedisTemplate.getExpire(key);
        return expire>0;
    }

    /**
     * 申请令牌
     * @param username 用户名
     * @param password 密码
     * @param clientId 客户端id
     * @param clientSecret 客户端密码
     * @return
     */
    private AuthToken applyToken(String username,String password,String clientId,String clientSecret){
        ServiceInstance instance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        URI uri = instance.getUri();
        String authUrl = uri+"/auth/oauth/token";
        LinkedMultiValueMap<String,String> headers = new LinkedMultiValueMap<String,String>();
        String basic = httpBasic("XcWebApp", "XcWebApp");
        headers.add("Authorization",basic);
        LinkedMultiValueMap<String,String> body = new LinkedMultiValueMap<String,String>();
        body.add("grant_type","password");
        body.add("username",username);
        body.add("password",password);
        HttpEntity<MultiValueMap<String, String>> multiValueMapHttpEntity = new HttpEntity<>(body, headers);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode()!=400&&response.getRawStatusCode()!=401){
                    super.handleError(response);
                }
            }
        });
        ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, multiValueMapHttpEntity, Map.class);
        Map map = exchange.getBody();
        if (map==null ||
                StringUtil.isEmpty((String) map.get("access_token")) ||
                StringUtil.isEmpty((String) map.get("refresh_token")) ||
                StringUtil.isEmpty((String) map.get("jti"))){
            String description = (String) map.get("error_description");
            if (StringUtil.isNotEmpty(description)){
                if ("坏的凭证".equals(description)){
                    ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
                }else if (description.startsWith("UserDetailsService returned null")){
                    ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);
                }else {
                    ExceptionCast.cast(AuthCode.AUTH_LOGIN_ERROR);
                }
            }else{
                ExceptionCast.cast(AuthCode.AUTH_LOGIN_ERROR);
            }
        }
        AuthToken authToken = new AuthToken();
        authToken.setAccess_token((String) map.get("access_token"));
        authToken.setRefresh_token((String) map.get("refresh_token"));
        authToken.setJwt_token((String) map.get("jti"));
        return authToken;
    }

    /**
     * 对客户端和密码进行base64编码
     * @param clientId
     * @param clientSecret
     * @return
     */
    private String httpBasic(String clientId,String clientSecret){
        String client = clientId+":"+clientSecret;
        byte[] encode = Base64Utils.encode(client.getBytes());
        return "Basic "+new String(encode);
    }

    public JwtResult getJwt() {
        String jwt = getTokenFormCookie();
        String userToken = "user_token:"+jwt;
        String authTokenString = stringRedisTemplate.opsForValue().get(userToken);
        if (StringUtil.isEmpty(authTokenString)){
            return new JwtResult(CommonCode.FAIL,null);
        }
        AuthToken authToken = JSON.parseObject(authTokenString, AuthToken.class);
        return new JwtResult(CommonCode.SUCCESS,authToken.getAccess_token());
    }

    /**
     * 从cookie获取身份令牌
     * @return
     */
    private String getTokenFormCookie(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String> cookie = CookieUtil.readCookie(request, "uid");
        String uid = cookie.get("uid");
        return uid;
    }

    /**
     * 用户退出
     * @return
     */
    public ResponseResult logout() {
        String accessToken = getTokenFormCookie();
        delToken(accessToken);
        delCookie(accessToken);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 从redis中删除身份令牌
     * @param accessToken
     * @return
     */
    private boolean delToken(String accessToken) {
        String  key = "user_token:"+accessToken;
        stringRedisTemplate.delete(key);
        Long expire = stringRedisTemplate.getExpire(key);
        return expire<0;
    }


    /**
     * 从cookie清楚身份令牌
     * @param jwt_token
     */
    private void delCookie(String jwt_token) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        CookieUtil.addCookie(response,cookieDomain,"/","uid",jwt_token,0,false);
    }
}
