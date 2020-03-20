package com.xuecheng.manage_media_process;

import com.xuecheng.framework.domain.media.MediaFileProcess_m3u8;
import com.xuecheng.framework.utils.Mp4VideoUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-12 9:11
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestProcessBuilder {

    /**
     * 测试ProcessBuilder调用第三方应用
     * @throws IOException
     */
    @Test
    public void testProcessBuilder() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("ping","127.0.0.1");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        InputStream inputStream = process.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"gbk");
        char[] chars = new char[1024];
        int len = -1;
        while ((len=inputStreamReader.read(chars))!=-1){
            String string = new String(chars,0,len);
            System.out.println(string);
        }
        inputStreamReader.close();
        inputStream.close();
    }

    //测试使用工具类将avi转成mp4
//    String ffmpeg_path, String video_path, String mp4_name, String mp4folder_path
    @Test
    public void testProcessMp4(){
        String ffmpeg_path = "C:/Program Files/ffmpeg/bin/ffmpeg.exe";
        String video_path = "H:\\xuecheng\\video\\lucene.avi";
        String mp4_name = "lucene.mp4";
        String mp4folder_path = "H:\\xuecheng\\video\\";
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpeg_path,video_path,mp4_name,mp4folder_path);
        String result = mp4VideoUtil.generateMp4();
        System.out.println(result);
    }

}
