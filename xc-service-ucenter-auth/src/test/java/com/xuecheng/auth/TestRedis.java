package com.xuecheng.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description: 测试redis连接
 * @author: 沈煜辉
 * @create: 2020-03-16 20:20
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRedis {

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    /**
     * 测试redis连接,并设置值和过期时间
     */
    @Test
    public void testRedis(){
        String key = "user_token:9734b68f‐cf5e‐456f‐9bd6‐df578c711390";
        Map<String,String> map = new HashMap<>();
        map.put("id","101");
        map.put("username","itcast");
        map.put("password","123");
        String value = JSON.toJSONString(map);
        stringRedisTemplate.boundValueOps(key).set(value,60, TimeUnit.SECONDS);
        Long expire = stringRedisTemplate.getExpire(key);
        System.out.println(expire);
        String s = stringRedisTemplate.opsForValue().get(key);
        System.out.println(s);
    }
}
