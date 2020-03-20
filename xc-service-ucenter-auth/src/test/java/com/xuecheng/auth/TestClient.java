package com.xuecheng.auth;

import com.xuecheng.framework.client.XcServiceList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * @description: 测试客户端登录
 * @author: 沈煜辉
 * @create: 2020-03-17 12:25
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestClient {

    @Autowired
    LoadBalancerClient loadBalancerClient;
    @Autowired
    RestTemplate restTemplate;

    /**
     * 测试通过密码模式获得密码
     */
    @Test
    public void testClient(){
//        请求地址 http://localhost:40400/auth/oauth/token
        ServiceInstance instance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        URI uri = instance.getUri();
        String authUrl = uri+"/auth/oauth/token";
        LinkedMultiValueMap<String,String> headers = new LinkedMultiValueMap<String,String>();
        String basic = httpBasic("XcWebApp", "XcWebApp");
        headers.add("Authorization",basic);
        LinkedMultiValueMap<String,String> body = new LinkedMultiValueMap<String,String>();
        body.add("grant_type","password");
        body.add("username","itcast");
        body.add("password","123");
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
        System.out.println(map);
    }

    private String httpBasic(String clientId,String clientSecret){
        String client = clientId+":"+clientSecret;
        byte[] encode = Base64Utils.encode(client.getBytes());
        return "Basic "+new String(encode);
    }

    @Test
    public void testPasswrodEncoder(){
        String password = "111111";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        for (int i = 0; i < 10; i++) {
            String encode = encoder.encode(password);
            System.out.println(encode);
            boolean matches = encoder.matches(password,encode);
            System.out.println(matches);
        }
    }
}
