package com.xuecheng.ucenter.mapper;

import com.xuecheng.framework.domain.ucenter.XcMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description: 权限查询接口
 * @author: 沈煜辉
 * @create: 2020-03-19 14:55
 **/
@Mapper
public interface XcMenuMapper {

    /**
     * 通过用户id查询用户权限
     * @param userId 用户id
     * @return 用户权限集合
     */
    List<XcMenu> findByUserId(String userId);
}
