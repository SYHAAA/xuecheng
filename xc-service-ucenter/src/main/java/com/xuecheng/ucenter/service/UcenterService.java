package com.xuecheng.ucenter.service;

import com.github.pagehelper.util.StringUtil;
import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import com.xuecheng.framework.domain.ucenter.XcMenu;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.ucenter.mapper.XcMenuMapper;
import com.xuecheng.ucenter.repository.XcCompanyUserRepository;
import com.xuecheng.ucenter.repository.XcUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 用户中心业务类
 * @author: 沈煜辉
 * @create: 2020-03-17 15:47
 **/
@Service
public class UcenterService {

    @Autowired
    XcUserRepository xcUserRepository;
    @Autowired
    XcCompanyUserRepository xcCompanyUserRepository;
    @Autowired
    XcMenuMapper xcMenuMapper;

    public XcUserExt getUserext(String username) {
        if (StringUtil.isEmpty(username)){
            ExceptionCast.cast(CommonCode.INVALIDPARAM);
        }
        XcUser xcUser = xcUserRepository.findByUsername(username);
        if (xcUser==null){
            return null;
        }
        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(xcUser,xcUserExt);
        String userId = xcUser.getId();
        XcCompanyUser companyUser = xcCompanyUserRepository.findByUserId(userId);
        if (companyUser!=null){
            xcUserExt.setCompanyId(companyUser.getCompanyId());
        }
        List<XcMenu> permissions = xcMenuMapper.findByUserId(userId);
        if (permissions!=null||permissions.size()!=0){
            xcUserExt.setPermissions(permissions);
        }
        return xcUserExt;
    }
}
