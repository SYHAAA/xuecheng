package com.xuecheng.manage.repository;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @description: 系统数据jpa
 * @author: 沈煜辉
 * @create: 2020-02-11 17:36
 **/
public interface SystemRepository extends MongoRepository<SysDictionary,String> {

    SysDictionary findByDType(String dType);
}
