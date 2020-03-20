package com.xuecheng.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.util.StringUtil;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

/**
 * @description: 文件上传业务类
 * @author: 沈煜辉
 * @create: 2020-03-04 14:43
 **/
@Service
public class FileSystemService {
    @Autowired
    FileSystemRepository fileSystemRepository;
    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
    private int connect_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
    private int network_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.charset}")
    private String charset;
    @Value("${xuecheng.fastdfs.tracker_servers}")
    private String tracker_servers;

    public UploadFileResult fileUpload(MultipartFile file, String businesskey, String filetag, String metadata) {
        if (file == null){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }
        FileSystem system = new FileSystem();
        String fileId = fdfs_upload(file);
        if (StringUtil.isEmpty(fileId)){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }
        system.setFileId(fileId);
        system.setFilePath(fileId);
        if (StringUtil.isEmpty(businesskey)){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_BUSINESSISNULL);
        }
        system.setBusinesskey(businesskey);
        if (StringUtil.isEmpty(metadata)){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_METAISNULL);
        }
        try {
            Map map = JSON.parseObject(metadata, Map.class);
            system.setMetadata(map);
        } catch (Exception e) {
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_METAERROR);
        }
        system.setFileName(file.getOriginalFilename());
        system.setFileSize(file.getSize());
        system.setFileType(file.getContentType());
        system.setFiletag(filetag);
        FileSystem save1 = fileSystemRepository.save(system);
        return new UploadFileResult(CommonCode.SUCCESS,save1);
    }

    /**
     * 文件上传
     * @param file
     * @return
     */
    private String fdfs_upload(MultipartFile file) {
        try {
            init();
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
            StorageClient1 storageClient1 = new StorageClient1(trackerServer,storeStorage);
            byte[] bytes = file.getBytes();
            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
            String fileId = storageClient1.upload_file1(bytes, ext, null);
            return fileId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void init() {
        ClientGlobal.setG_charset(charset);
        ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
        ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
        try {
            ClientGlobal.initByTrackers(tracker_servers);
        } catch (Exception e) {
            ExceptionCast.cast(FileSystemCode.FS_INITFDFSERROR);
        }
    }
}
