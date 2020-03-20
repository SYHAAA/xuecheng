package com.xuecheng.manage_media.service;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

/**
 * @description: 媒资文件管理业务类
 * @author: 沈煜辉
 * @create: 2020-03-13 13:31
 **/
@Service
public class MediaFileService {

    @Autowired
    MediaFileRepository mediaFileRepository;

    public QueryResponseResult findAll(Integer page, Integer size, QueryMediaFileRequest queryMediaFileRequest) {
        if (queryMediaFileRequest == null){
            queryMediaFileRequest = new QueryMediaFileRequest();
        }
        if (page<=0){
            page=1;
        }
        page = page - 1;
        if (size<=10){
            size = 10;
        }
        MediaFile mediaFile = new MediaFile();
        if (StringUtil.isNotEmpty(queryMediaFileRequest.getTag())){
            mediaFile.setTag(queryMediaFileRequest.getTag());
        }
        if (StringUtil.isNotEmpty(queryMediaFileRequest.getFileOriginalName())){
            mediaFile.setFileOriginalName(queryMediaFileRequest.getFileOriginalName());
        }
        if (StringUtil.isNotEmpty(queryMediaFileRequest.getProcessStatus())){
            mediaFile.setProcessStatus(queryMediaFileRequest.getProcessStatus());
        }
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                                        .withMatcher("tag", ExampleMatcher.GenericPropertyMatchers.contains())
                                        .withMatcher("fileOriginalName",ExampleMatcher.GenericPropertyMatchers.contains());
        Example example = Example.of(mediaFile,exampleMatcher);
        Pageable pageable = PageRequest.of(page,size);
        Page all = mediaFileRepository.findAll(example, pageable);
        QueryResult queryResult = new QueryResult();
        queryResult.setTotal(all.getTotalElements());
        queryResult.setList(all.getContent());
        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }
}
