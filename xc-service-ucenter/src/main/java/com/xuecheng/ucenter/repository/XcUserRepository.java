package com.xuecheng.ucenter.repository;

import com.xuecheng.framework.domain.ucenter.XcUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description: 用户repository
 * @author: 沈煜辉
 * @create: 2020-03-17 15:43
 **/
public interface XcUserRepository extends JpaRepository<XcUser,String> {

    /**
     * 通过用户名查找用户信息
     * @param username
     * @return
     */
    XcUser findByUsername(String username);
}
