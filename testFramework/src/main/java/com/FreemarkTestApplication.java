package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @description: 启动类
 * @author: 沈煜辉
 * @create: 2020-02-01 16:43
 **/
@SpringBootApplication
public class FreemarkTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(FreemarkTestApplication.class);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }

}
