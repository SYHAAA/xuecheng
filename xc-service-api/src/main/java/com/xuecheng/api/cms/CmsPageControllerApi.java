package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CoursePreviewResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.*;

import java.util.concurrent.CountDownLatch;

/**
 * @description: cms页面接口
 * @author: 沈煜辉
 * @create: 2019-12-22 22:04
 **/
@Api(value = "cms页面管理接口")
public interface CmsPageControllerApi {
    /**
     * 条件分页查询cms
     * @param page
     * @param size
     * @param queryPageRequest
     * @return
     */
    @ApiOperation(value = "分页查询页面列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "page",value = "页码",required = true,
                    paramType = "path",dataType = "int"),
            @ApiImplicitParam(name = "size",value = "每页记录数",required = true,
                    paramType = "path",dataType = "int")
    })
    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);


    /**
     * 新增cms页面
     * @param cmsPage
     * @return
     */
    @ApiOperation(value = "新增cms页面")
    CmsPageResult saveCmsPage(CmsPage cmsPage);

    /**
     * 通过id查找页面
     * @param id 页面id
     * @return
     */
    @ApiOperation(value = "通过id查找页面")
    CmsPage findCmsPageByPageId(String id);

    /**
     * 通过页面ID修改页面
     * @param id 页面ID
     * @param cmsPage 修改页面
     * @return
     */
    @ApiOperation(value = "通过页面ID修改页面")
    CmsPageResult updateCmsPage(String id,CmsPage cmsPage);

    /**
     * 删除页面
     * @param id 页面ID
     * @return
     */
    @ApiOperation(value = "删除页面")
    ResponseResult deleteCmsPage(String id);

    /**
     * 根据页面ID发布页面
     * @param pageId
     * @return
     */
    @ApiOperation(value = "发布页面")
    ResponseResult post(String pageId);

    /**
     * 保存页面信息
     * @param cmsPage 页面
     * @return
     */
    @ApiOperation(value = "保存页面信息")
    CmsPageResult save(CmsPage cmsPage);

    /**
     * 课程发布
     * @param cmsPage 页面基本信息
     * @return
     */
    @ApiOperation(value = "课程发布")
    CoursePreviewResult coursePost(CmsPage cmsPage);
}
