package com.xuecheng.manage_media;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @description: 测试类
 * @author: 沈煜辉
 * @create: 2020-03-12 13:48
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFile {

    /**
     * 测试文件分块
     */
    @Test
    public void testChuck() throws IOException {
        File sourceFile = new File("H:\\xuecheng\\video\\lucene.mp4");
        String chuckPath = "H:\\xuecheng\\video\\chuck\\";
        File chuckPathFile = new File(chuckPath);
        if (!chuckPathFile.exists()){
            chuckPathFile.mkdirs();
        }
        long chuckFileSize = 1024 * 1024;
        long chuckNum = (long) Math.ceil(sourceFile.length()*1.0/chuckFileSize);
        RandomAccessFile raf_read = new RandomAccessFile(sourceFile,"r");
        byte[] bytes = new byte[1024];
        int len = -1;
        for (long i = 0; i < chuckNum; i++) {
            File chuckFile = new File(chuckPath+i);
            boolean chuckFileNewFile = chuckFile.createNewFile();
            if (chuckFileNewFile){
                RandomAccessFile raf_write = new RandomAccessFile(chuckFile,"rw");
                while ((len=raf_read.read(bytes))!=-1){
                    raf_write.write(bytes,0,len);
                    if (raf_write.length()==chuckFileSize){
                        break;
                    }
                }
                raf_write.close();
            }
        }
        raf_read.close();
    }

    /**
     * 测试文件的合并
     * @throws IOException
     */
    @Test
    public void testMerge() throws IOException {
        File chuckPathFile = new File("H:\\xuecheng\\video\\chuck\\");
        File[] files = chuckPathFile.listFiles();
        File mergeFile = new File("H:\\xuecheng\\video\\luceneMerge.mp4");
        if (!mergeFile.exists()){
            mergeFile.createNewFile();
        }
        RandomAccessFile raf_write = new RandomAccessFile(mergeFile,"rw");
        byte[] bytes = new byte[1024];
        int len = -1;
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
        for (File file : fileList) {
            RandomAccessFile raf_read = new RandomAccessFile(file,"r");
            while ((len=raf_read.read(bytes))!=-1){
                raf_write.write(bytes,0,len);
            }
            raf_read.close();
        }
        raf_write.close();
    }
}
