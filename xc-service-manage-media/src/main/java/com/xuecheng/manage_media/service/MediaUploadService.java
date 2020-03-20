package com.xuecheng.manage_media.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.util.StringUtil;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.config.RabbitMQConfig;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.*;

/**
 * @description: 媒资文件上传业务类
 * @author: 沈煜辉
 * @create: 2020-03-12 14:47
 **/
@Service
public class MediaUploadService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MediaUploadService.class);

    @Value("${xc-service-manage-media.upload-location}")
    String upload_location;

    @Autowired
    MediaFileRepository mediaFileRepository;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Value("${xc-service-manage-media.mq.routingkey-media-video}")
    String routingkey_media_video;
    @Value("${xc-service-manage-media.mq.queue-media-video-processor}")
    String queue_media_video_processor;

    public ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
//        得到所要上传的文件
        String filePath = getFilePath(fileMd5,fileExt);
        File file = new File(filePath);
//        从数据库中查询是否已有记录
        Optional<MediaFile> mediaFileOptional = mediaFileRepository.findById(fileMd5);
        if (file.exists()&&mediaFileOptional.isPresent()){
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }
        boolean fileFold = createFileFold(fileMd5);
        if (!fileFold){
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_FAIL);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 创建上传文件目录
     * @param fileMd5
     * @return
     */
    private boolean createFileFold(String fileMd5) {
        String fileFolderPath = getFileFolderPath(fileMd5);
        File file = new File(fileFolderPath);
        if (!file.exists()){
            boolean mkdirs = file.mkdirs();
            return mkdirs;
        }
        return true;
    }

    /**
     * 得到文件相对路径名称
     * @param fileMd5
     * @return
     */
    private String getFileFolderRelativePath(String fileMd5){
        String fileFolderRelativePath = fileMd5.substring(0,1)+"/"+fileMd5.substring(1,2)+"/"+fileMd5+"/";
        return fileFolderRelativePath;
    }

    /**
     * 得到文件的全路径
     * @param fileMd5
     * @param fileExt
     * @return
     */
    private String getFilePath(String fileMd5, String fileExt) {
        String fileFolderPath = getFileFolderPath(fileMd5);
        String filePath = fileFolderPath+fileMd5+"."+fileExt;
        return filePath;
    }

    /**
     * 得到文件的上传目录
     * @param fileMd5
     * @return
     */
    private String getFileFolderPath(String fileMd5) {
        String fileFolderPath = upload_location+fileMd5.substring(0,1)+"/"+fileMd5.substring(1,2)+"/"+fileMd5+"/";
        return fileFolderPath;
    }


    public CheckChunkResult checkchunk(String fileMd5, String chunk, Long chunkSize) {
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File file = new File(chunkFileFolderPath+chunk);
        if (file.exists()){
            return new CheckChunkResult(CommonCode.SUCCESS,true);
        }
        return new CheckChunkResult(CommonCode.SUCCESS,false);
    }

    private String getChunkFileFolderPath(String fileMd5) {
        String fileFolderPath = getFileFolderPath(fileMd5)+ "/chunks/";
        return fileFolderPath;
    }

    public ResponseResult uploadchunk(MultipartFile file, Integer chunk, String fileMd5) {
        if (file==null){
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_ISNULL);
        }
        boolean chunkFileFolder =  createChunkFileFolder(fileMd5);
        File chunkFile = new File(getChunkFileFolderPath(fileMd5)+chunk);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        if (chunkFileFolder){
            try {
                inputStream = file.getInputStream();
                if (!chunkFile.exists()){
                    chunkFile.createNewFile();
                }
                outputStream = new FileOutputStream(chunkFile);
                IOUtils.copy(inputStream,outputStream);
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error("upload chunk file fail:{}",e.getMessage());
                ExceptionCast.cast(MediaCode.UPLOAD_FILE_FAIL);
            }finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    private boolean createChunkFileFolder(String fileMd5) {
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File chunkFileFolder = new File(chunkFileFolderPath);
        if (!chunkFileFolder.exists()){
            boolean mkdirs = chunkFileFolder.mkdirs();
            return mkdirs;
        }
        return true;
    }

    public ResponseResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        String filePath = getFilePath(fileMd5, fileExt);
        File file = new File(filePath);
        if (file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            LOGGER.error("create file error",e.getMessage());
            e.printStackTrace();
            ExceptionCast.cast(MediaCode.MERGE_FILE_CREATEERROR);
        }
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File chunkFileFolder = new File(chunkFileFolderPath);
        File[] files = chunkFileFolder.listFiles();
//        对文件列表进行排序
        List<File> fileList = getChunkFiles(files);
//        合并文件
        file = mergeFile(file,fileList);
        if (file == null){
            ExceptionCast.cast(MediaCode.MERGE_FILE_FAIL);
        }
        boolean checkResult =  checkFileMd5(file,fileMd5);
        if (!checkResult){
            ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
        }
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMd5);
        mediaFile.setFileName(fileMd5+"."+fileExt);
        mediaFile.setFileOriginalName(fileName);
        mediaFile.setFilePath(getFileFolderRelativePath(fileMd5));
        mediaFile.setFileType(fileExt);
        mediaFile.setFileSize(fileSize);
        mediaFile.setUploadTime(new Date());
        mediaFile.setMimeType(mimetype);
        mediaFile.setFileStatus("301002");
        mediaFile.setProcessStatus("303001");
        mediaFileRepository.save(mediaFile);
        sendProcessVideoMsg(fileMd5);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public ResponseResult sendProcessVideoMsg(String mediaId){
        Optional<MediaFile> mediaFileOptional = mediaFileRepository.findById(mediaId);
        if (!mediaFileOptional.isPresent()){
            return new ResponseResult(CommonCode.FAIL);
        }
        Map<String,String> map = new HashMap<>();
        map.put("mediaId",mediaId);
        String msg = JSON.toJSONString(map);
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EX_MEDIA_PROCESSTASK,routingkey_media_video,msg);
        } catch (AmqpException e) {
            e.printStackTrace();
            LOGGER.error("send msg to queue error:{}",e.getMessage());
            return new ResponseResult(CommonCode.FAIL);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 校验文件
     * @param file
     * @param fileMd5
     * @return
     */
    private boolean checkFileMd5(File file, String fileMd5) {
        if (file == null|| StringUtil.isEmpty(fileMd5)){
            return false;
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            String md5Hex = DigestUtils.md5Hex(fileInputStream);
            if (fileMd5.equals(md5Hex)){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("merge file fail:",e.getMessage());
            ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
        }
        return false;
    }

    /**
     * 合并文件
     * @param file
     * @param fileList
     * @return
     */
    private File mergeFile(File file, List<File> fileList) {
        try {
            RandomAccessFile raf_write = new RandomAccessFile(file,"rw");
            byte[] bytes = new byte[1024];
            int len = -1;
            for (File file1 : fileList) {
                RandomAccessFile raf_read = new RandomAccessFile(file1,"r");
                while ((len=raf_read.read(bytes))!=-1){
                    raf_write.write(bytes,0,len);
                }
                raf_read.close();
            }
            raf_write.close();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("merge file fail:",e.getMessage());
            ExceptionCast.cast(MediaCode.MERGE_FILE_FAIL);
        }
        return file;
    }

    /**
     * 对文件数组进行排序，返回有序的文件集合
     * @param files
     * @return
     */
    private List getChunkFiles(File[] files) {
        List<File> fileList = Arrays.asList(files);
        fileList.sort(new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (Integer.parseInt(o1.getName())>Integer.parseInt(o2.getName())){
                    return 1;
                }
                return -1;
            }
        });
        return fileList;
    }
}
