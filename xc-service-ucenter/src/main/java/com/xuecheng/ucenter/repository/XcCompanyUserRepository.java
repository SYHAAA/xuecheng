package com.xuecheng.ucenter.repository;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description: 用户repository
 * @author: 沈煜辉
 * @create: 2020-03-17 15:43
 **/
public interface XcCompanyUserRepository extends JpaRepository<XcCompanyUser,String> {

    /**
     * 通过用户id查询公司id
     * @param userId
     * @return
     */
    XcCompanyUser findByUserId(String userId);
}
