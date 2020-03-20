package com.xuecheng.auth.client;

import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description: 调用远程用户中心接口
 * @author: 沈煜辉
 * @create: 2020-03-17 16:11
 **/
@FeignClient(value = XcServiceList.XC_SERVICE_UCENTER)
public interface UcenterClient {

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    @GetMapping("/ucenter/getuserext")
    XcUserExt getUserExt(@RequestParam("username") String username);
}
