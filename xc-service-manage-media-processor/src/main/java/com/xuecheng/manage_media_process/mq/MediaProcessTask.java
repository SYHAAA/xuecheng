package com.xuecheng.manage_media_process.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.MediaFileProcess_m3u8;
import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.utils.HlsVideoUtil;
import com.xuecheng.framework.utils.Mp4VideoUtil;
import com.xuecheng.manage_media_process.dao.MediaFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @description: 处理视频任务
 * @author: 沈煜辉
 * @create: 2020-03-12 19:31
 **/
@Component
public class MediaProcessTask {

    private final static Logger LOGGER = LoggerFactory.getLogger(MediaProcessTask.class);

    @Value("${xc-service-manage-media.mq.routingkey-media-video}")
    String routingkey_media_video;
    @Value("${xc-service-manage-media.video-location}")
    String video_location;
    @Value("${xc-service-manage-media.ffmpeg-path}")
    String ffmpeg_path;
    @Autowired
    MediaFileRepository mediaFileRepository;

    @RabbitListener(queues = "${xc-service-manage-media.mq.queue-media-video-processor}",containerFactory = "customContainerFactory")
    public void receiveMediaProcessTask(String msg){
        Map<String,String> map = JSON.parseObject(msg, Map.class);
        String mediaId = map.get("mediaId");
        Optional<MediaFile> mediaFileOptional = mediaFileRepository.findById(mediaId);
        if (!mediaFileOptional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALIDPARAM);
        }
        MediaFile mediaFile = mediaFileOptional.get();
        String fileType = mediaFile.getFileType();
        if (!"avi".equals(fileType)){
            mediaFile.setProcessStatus("303004");
            mediaFileRepository.save(mediaFile);
            return;
        }else{
            mediaFile.setProcessStatus("303001");
            mediaFileRepository.save(mediaFile);
        }
//        将视频格式改变为mp4
//        String ffmpeg_path, String video_path, String mp4_name, String mp4folder_path
        String video_path = video_location+mediaFile.getFilePath()+mediaFile.getFileName();
        String mp4_name = mediaId+".mp4";
        String mp4folder_path = video_location+mediaFile.getFilePath();
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpeg_path,video_path,mp4_name,mp4folder_path);
        String result = mp4VideoUtil.generateMp4();
        if (result == null||!"success".equals(result)){
            mediaFile.setProcessStatus("303003");
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            mediaFileProcess_m3u8.setErrormsg(result);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
            mediaFileRepository.save(mediaFile);
        }
//        生成mu38文件
//        String ffmpeg_path, String video_path, String m3u8_name,String m3u8folder_path
        String mp4_filePath = mp4folder_path+mp4_name;
        String m3u8_name = mediaId+".m3u8";
        String m3u8folder_path = video_location+mediaFile.getFilePath()+"hls/";
        HlsVideoUtil hlsVideoUtil = new HlsVideoUtil(ffmpeg_path,mp4_filePath,m3u8_name,m3u8folder_path);
        String generateM3u8 = hlsVideoUtil.generateM3u8();
        if (generateM3u8 == null||!"success".equals(result)){
            mediaFile.setProcessStatus("303003");
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            mediaFileProcess_m3u8.setErrormsg(generateM3u8);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
            mediaFileRepository.save(mediaFile);
        }
        List<String> ts_list = hlsVideoUtil.get_ts_list();
        mediaFile.setProcessStatus("303002");
        MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
        mediaFileProcess_m3u8.setTslist(ts_list);
        mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
        mediaFile.setFileUrl(mediaFile.getFilePath()+"/hls/"+m3u8_name);
        mediaFileRepository.save(mediaFile);
    }
}
