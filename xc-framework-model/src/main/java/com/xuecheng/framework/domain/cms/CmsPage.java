package com.xuecheng.framework.domain.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @Author: mrt.
 * @Description:
 * @Date:Created in 2018/1/24 10:04.
 * @Modified By:
 */
@Data
@ToString
@Document(collection = "cms_page")
public class CmsPage {
    /**
     * 页面名称、别名、访问地址、类型（静态/动态）、页面模版、状态
     */
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @Id
    @ApiModelProperty(value = "页面ID")
    private String pageId;
    @ApiModelProperty(value = "页面名称")
    private String pageName;
    @ApiModelProperty(value = "别名")
    private String pageAliase;
    @ApiModelProperty(value = "访问地址")
    private String pageWebPath;
    //参数
    private String pageParameter;
    @ApiModelProperty(value = "物理路径")
    private String pagePhysicalPath;
    @ApiModelProperty(value = "类型（静态/动态）")
    private String pageType;
    //页面模版
    private String pageTemplate;
    //页面静态化内容
    private String pageHtml;
    @ApiModelProperty(value = "状态")
    private String pageStatus;
    //创建时间
    private Date pageCreateTime;
    @ApiModelProperty(value = "模版id")
    private String templateId;
    //参数列表
    private List<CmsPageParam> pageParams;
    //模版文件Id
//    private String templateFileId;
    //静态文件Id
    private String htmlFileId;
    @ApiModelProperty(value = "数据Url")
    private String dataUrl;
}
