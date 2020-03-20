package com.xuecheng.filesystem.dao;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @description:
 * @author: 沈煜辉
 * @create: 2020-03-04 14:44
 **/
public interface FileSystemRepository extends MongoRepository<FileSystem,String> {
}
