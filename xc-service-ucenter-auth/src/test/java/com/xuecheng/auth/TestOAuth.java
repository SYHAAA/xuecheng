package com.xuecheng.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 测试oauth认证授权
 * @author: 沈煜辉
 * @create: 2020-03-16 19:35
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestOAuth {

    /**
     * 测试获得令牌
     */
    @Test
    public void testCreateJwt(){
        String key_location = "xc.keystore";
        String keyStore_password = "xuechengkeystore";
        ClassPathResource classPathResource = new ClassPathResource(key_location);
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(classPathResource, keyStore_password.toCharArray());

        String alias = "xckey";
        String key_password = "xuecheng";
        KeyPair keyPair = factory.getKeyPair(alias, key_password.toCharArray());
        RSAPrivateKey key = (RSAPrivateKey) keyPair.getPrivate();
        Map<String,Object> map = new HashMap<>();
        map.put("id","123");
        map.put("name","mrt");
        map.put("roles","r01,r02");
        map.put("ext","1");
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(map), new RsaSigner(key));
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }

    /**
     * 验证令牌和取出原内容
     */
    @Test
    public void testVerify(){
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOiIxIiwidXNlcnBpYyI6bnVsbCwidXNlcl9uYW1lIjoiaXRjYXN0Iiwic2NvcGUiOlsiYXBwIl0sIm5hbWUiOiJ0ZXN0MDIiLCJ1dHlwZSI6IjEwMTAwMiIsImlkIjoiNDkiLCJleHAiOjE1ODQ2NDUyMjQsImF1dGhvcml0aWVzIjpbInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfYmFzZSIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfZGVsIiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZV9saXN0IiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZV9wbGFuIiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZSIsImNvdXJzZV9maW5kX2xpc3QiLCJ4Y190ZWFjaG1hbmFnZXIiLCJ4Y190ZWFjaG1hbmFnZXJfY291cnNlX21hcmtldCIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfcHVibGlzaCIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfYWRkIl0sImp0aSI6IjJjMzNkOTUwLThmNjgtNGNiYi1iOGMzLTdjZWVmZDllNTBmZSIsImNsaWVudF9pZCI6IlhjV2ViQXBwIn0.gQhxchZEv1Mbc6sbHJe5nujVYLCIq1r3v23f5PT7OaQzsnJTaXQX50TVxnA6JQ-IWoEFYzSDWjkEGPZubSMgiNgJWC-756Ptq5JDgDL75jJ1oesDLo6Fn1vEOfgXRy5HgJ0LM0xU1Cr8czclmEri-2L0feuYbxQqGB2iJ1bl6NHETYy5G7pu1qqKaWMlPEjGBkBPnckmqxnXhVU0sQSCWBgaeWQDmlKaQslAGE2pwQ2_gD3E6X2qS342imw8gZ2sY94lnbgxVXEUI_JUXUiYO9REkZ-z4M6WaHC3Bh3_o6w_6ycQraSzVcw3bdUCLdiS_kwdKjWmFWIoUDzFLbaYaA";
        String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnASXh9oSvLRLxk901HANYM6KcYMzX8vFPnH/To2R+SrUVw1O9rEX6m1+rIaMzrEKPm12qPjVq3HMXDbRdUaJEXsB7NgGrAhepYAdJnYMizdltLdGsbfyjITUCOvzZ/QgM1M4INPMD+Ce859xse06jnOkCUzinZmasxrmgNV3Db1GtpyHIiGVUY0lSO1Frr9m5dpemylaT0BV3UwTQWVW9ljm6yR3dBncOdDENumT5tGbaDVyClV0FEB1XdSKd7VjiDCDbUAUbDTG1fm3K9sx7kO1uMGElbXLgMfboJ963HEJcU01km7BmFntqI5liyKheX+HBUCD4zbYNPw236U+7QIDAQAB-----END PUBLIC KEY-----";
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publicKey));
        String claims = jwt.getClaims();
        System.out.println(claims);
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }
}
