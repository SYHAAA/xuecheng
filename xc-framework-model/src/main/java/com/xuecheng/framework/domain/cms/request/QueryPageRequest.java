package com.xuecheng.framework.domain.cms.request;

import com.xuecheng.framework.model.request.RequestData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

/**
 * @description: 要求查询的数据
 * @author: 沈煜辉
 * @create: 2019-12-22 11:04
 **/
@Data
public class QueryPageRequest extends RequestData {
    @Id
    @ApiModelProperty(value = "站点id")
    private String siteId;
    @ApiModelProperty(value = "页面id")
    private String pageId;
    @ApiModelProperty(value = "页面名称")
    private String pageName;
    @ApiModelProperty(value = "页面别名")
    private String pageAliase;
    @ApiModelProperty(value = "模板Id")
    private String templateId;
}
