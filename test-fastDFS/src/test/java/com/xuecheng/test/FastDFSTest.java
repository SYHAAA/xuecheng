package com.xuecheng.test;

import com.xuecheng.fastDFS.FastDFSApplication;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @description: fastDFS测试类
 * @author: 沈煜辉
 * @create: 2020-03-04 13:12
 **/
@SpringBootTest(classes = FastDFSApplication.class)
@RunWith(SpringRunner.class)
public class FastDFSTest {

    /**
     * 测试上传
     */
    @Test
    public void testUpload(){
        try {
            ClientGlobal.init("config/fastdfs-client.properties");
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
            StorageClient1 storageClient1 =  new StorageClient1(trackerServer,storeStorage);
            String filePath = "C:\\Users\\26253\\Pictures\\Screenshots\\tra.png";
            String ext = filePath.substring(filePath.lastIndexOf(".") + 1);
            System.out.println(ext);
            String fileID = storageClient1.upload_file1(filePath, ext, null);
            System.out.println(fileID);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试文件下载
     */
    @Test
    public void testDownload(){
        try {
            ClientGlobal.init("config/fastdfs-client.properties");
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            StorageClient1 storageClient1 = new StorageClient1(trackerServer,storageServer);
            byte[] bytes = storageClient1.download_file1("group1/M00/00/00/wKgZhV5fPSeAbdRWAAKM9VkKVig262.png");
            FileOutputStream fos = new FileOutputStream(new File("F:/copy.jpg"));
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }
}
