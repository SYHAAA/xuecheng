import com.FreemarkTestApplication;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 测试文件
 * @author: 沈煜辉
 * @create: 2020-02-01 18:11
 **/
@SpringBootTest(classes = FreemarkTestApplication.class)
@RunWith(SpringRunner.class)
public class FreemarkTest {

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    RestTemplate restTemplate;

    /**
     * 读取模板文件进行静态化
     * @throws IOException
     * @throws TemplateException
     */
    @Test
    public void testGenerateHtml() throws IOException, TemplateException {

//        获取文件所在目录
        Configuration configuration = new Configuration(Configuration.getVersion());
        String path = this.getClass().getResource("/").getPath();
        configuration.setDirectoryForTemplateLoading(new File(path+"/templates"));
        configuration.setDefaultEncoding("utf-8");
//        获取模板文件
        Template template = configuration.getTemplate("test1.ftl");
//        设置数据模型
        Map<String,Object> map = new HashMap<>();
        map.put("name","沈煜辉");
//        进行页面静态化
        String s = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
    }

    /**
     * 读取字符串进行模板静态化
     * @throws IOException
     * @throws TemplateException
     */
    @Test
    public void testGenerateText() throws IOException, TemplateException {
//        制作模板
        Configuration configuration = new Configuration(Configuration.getVersion());
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>测试模板</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h2>页面静态化</h2>\n" +
                "作者：${name}\n" +
                "</body>\n" +
                "</html>";
        stringTemplateLoader.putTemplate("template",html);
        configuration.setTemplateLoader(stringTemplateLoader);
        Template template = configuration.getTemplate("template");
//        设置数据模型
        Map<String,Object> map = new HashMap<>();
        map.put("name","沈煜辉");
//        进行静态化
        String s = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        System.out.println(s);
    }

    /**
     * 将文件存储
     * @throws IOException
     * @throws TemplateException
     */
    @Test
    public void toFile() throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.getVersion());
        String path = this.getClass().getResource("/").getPath();
        configuration.setDirectoryForTemplateLoading(new File(path + "/templates"));
        configuration.setDefaultEncoding("utf-8");
        Template template = configuration.getTemplate("index_banner.ftl");
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getModel/5a791725dd573c3574ee333f", Map.class);
        Map body = forEntity.getBody();
        Map<String,Object> map = new HashMap<>();
        map.putAll(body);
        String s = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        File file = new File("H:/index_banner.html");
        if(!file.createNewFile()){
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(s.getBytes());
        fos.close();
    }


}
