package com.xuecheng.manage_course.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CoursePreviewResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.repository.*;
import com.xuecheng.manage_course.dao.CourseMapper;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @description: 业务层课程管理接口实现类
 * @author: 沈煜辉
 * @create: 2020-02-06 18:49
 **/
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    TeachplanMapper teachplanMapper;
    @Autowired
    TeachplanRepository teachplanRepository;
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    CourseMarketRepository courseMarketRepository;
    @Autowired
    CoursePicRepository coursePicRepository;
    @Autowired
    CmsPageClient cmsPageClient;
    @Autowired
    CoursePubRepository coursePubRepository;
    @Autowired
    TeachplanMediaRepository teachplanMediaRepository;
    @Autowired
    TeachplanMediaPubRepository teachplanMediaPubRepository;

    @Value("${course‐publish.siteId}")
    private String siteId;
    @Value("${course‐publish.templateId}")
    private String templateId;
    @Value("${course‐publish.previewUrl}")
    private String previewUrl;
    @Value("${course‐publish.pageWebPath}")
    private String pageWebPath;
    @Value("${course‐publish.pagePhysicalPath}")
    private String pagePhysicalPath;
    @Value("${course‐publish.dataUrlPre}")
    private String dataUrlPre;
    @Override
    public TeachplanNode findTeachplanNodeById(String courseId) {
        return teachplanMapper.findTeachplanNodeByCourseId(courseId);
    }

    @Override
    public ResponseResult saveTeachplan(Teachplan teachplan) {
        if (teachplan == null||
                StringUtils.isEmpty(teachplan.getCourseid())||
                StringUtils.isEmpty(teachplan.getPname())){
            ExceptionCast.cast(CommonCode.INVALIDPARAM);
        }
        String courseid = teachplan.getCourseid();
        String parentid = teachplan.getParentid();
        if (StringUtils.isEmpty(parentid)){
            parentid = getTeachplanRoot(courseid);
        }
        Optional<Teachplan> optional = teachplanRepository.findById(parentid);
        if (!optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALIDPARAM);
        }
        Teachplan teachplanParent = optional.get();
        String parentGrade = teachplanParent.getGrade();
        if (parentGrade.equals("1")){
            teachplan.setGrade("2");
        }
        if (parentGrade.equals("2")){
            teachplan.setGrade("3");
        }
        teachplan.setParentid(parentid);
        teachplan.setCourseid(teachplanParent.getParentid());
        teachplan.setStatus("0");
        teachplanRepository.save(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    public QueryResponseResult findAllCourse(Integer pageNum, Integer pageSize, CourseListRequest courseListRequest) {
        if (pageNum<=1){
            pageNum = 1;
        }
        if (pageSize <= 10){
            pageSize = 10;
        }
        PageHelper .startPage(pageNum,pageSize);
        if (courseListRequest==null){
            courseListRequest = new CourseListRequest();
        }
        List<CourseInfo> courseInfoList = courseMapper.findAllCourse(courseListRequest);
        PageInfo<CourseInfo> pageInfo = new PageInfo<>(courseInfoList);
        QueryResult<CourseInfo> queryResult = new QueryResult<>();
        queryResult.setList(courseInfoList);
        queryResult.setTotal(pageInfo.getTotal());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }

    @Override
    public ResponseResult saveCourseBase(CourseBase courseBase) {
        if (courseBase == null||StringUtils.isEmpty(courseBase.getName())){
            ExceptionCast.cast(CommonCode.INVALIDPARAM);
        }
        String name = courseBase.getName();
        CourseBase base = courseBaseRepository.findByName(name);
        if (base != null){
            ExceptionCast.cast(CourseCode.COURSE_SAVE_NAMEEXIST);
        }
        base.setStatus("202001");
        CourseBase save = courseBaseRepository.save(courseBase);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    public CourseBase findCourseView(String courseId) {
        if (StringUtils.isEmpty(courseId)){
            ExceptionCast.cast(CommonCode.INVALIDPARAM);
        }
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    @Override
    public ResponseResult updateCourse(String courseId, CourseBase courseBase) {
        CourseBase courseView = findCourseView(courseId);
        if (courseView == null){
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        if (courseBase == null){
            ExceptionCast.cast(CommonCode.INVALIDPARAM);
        }
        courseView.setName(courseBase.getName());
        courseView.setUsers(courseBase.getUsers());
        courseView.setGrade(courseBase.getGrade());
        courseView.setMt(courseBase.getMt());
        courseView.setSt(courseBase.getSt());
        courseView.setDescription(courseBase.getDescription());
        courseView.setStudymodel(courseBase.getStudymodel());
        courseBaseRepository.save(courseView);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    public CourseMarket findCourseMarket(String courseId) {
        if (courseId == null){
            ExceptionCast.cast(CommonCode.INVALIDPARAM);
        }
        Optional<CourseMarket> optional = courseMarketRepository.findById(courseId);
        if (optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    @Override
    public ResponseResult updateCourseMarket(String courseId, CourseMarket courseMarket) {
        CourseMarket market = findCourseMarket(courseId);
        if (market == null){
            CourseMarket saveCourseMarket = saveCourseMarket(courseId, courseMarket);
            if (saveCourseMarket != null){
                return new ResponseResult(CommonCode.SUCCESS);
            }
        }
        market.setCharge(courseMarket.getCharge());
        market.setPrice_old(market.getPrice());
        market.setPrice(courseMarket.getPrice());
        market.setValid(courseMarket.getValid());
        market.setStartTime(courseMarket.getStartTime());
        market.setEndTime(courseMarket.getEndTime());
        market.setQq(courseMarket.getQq());
        courseMarketRepository.save(market);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    public ResponseResult addCoursePic(String courseId, String pic) {
        Optional<CoursePic> coursePicFind = coursePicRepository.findById(courseId);
        CoursePic coursePic = null;
        if (coursePicFind.isPresent()){
            coursePic = coursePicFind.get();
        }else {
            coursePic = new CoursePic();
        }
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    public CoursePic findByCourseId(String courseId) {
        return coursePicRepository.findById(courseId).get();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseResult deleteCourse(@RequestParam("courseId") String courseId) {
        long num = coursePicRepository.deleteByCourseid(courseId);
        if (num>0){
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    @Override
    public CourseView getCourseView(String courseId) {
        if (StringUtil.isEmpty(courseId)){
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        CourseView courseView = new CourseView();
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(courseId);
        if (!courseBaseOptional.isPresent()){
            ExceptionCast.cast(CourseCode.COURSE_FIND_NOTEXIST);
        }
        CourseBase courseBase = courseBaseOptional.get();
        courseView.setCourseBase(courseBase);
        Optional<CoursePic> coursePicOptional = coursePicRepository.findById(courseId);
        if (!coursePicOptional.isPresent()){
            ExceptionCast.cast(CourseCode.COURSE_FIND_NOTEXIST);
        }
        CoursePic coursePic = coursePicOptional.get();
        courseView.setCoursePic(coursePic);
        Optional<CourseMarket> marketOptional = courseMarketRepository.findById(courseId);
        if (!marketOptional.isPresent()){
            ExceptionCast.cast(CourseCode.COURSE_FIND_NOTEXIST);
        }
        CourseMarket courseMarket = marketOptional.get();
        courseView.setCourseMarket(courseMarket);
        TeachplanNode teachplanNode = findTeachplanNodeById(courseId);
        if (teachplanNode==null){
            ExceptionCast.cast(CourseCode.COURSE_FIND_NOTEXIST);
        }
        courseView.setTeachplanNode(teachplanNode);
        return courseView;
    }

    @Override
    public CoursePreviewResult coursePreview(String courseId) {
        CourseBase courseBase = findCourseView(courseId);
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(siteId);
        cmsPage.setPageName(courseId+".html");
        cmsPage.setPageAliase(courseBase.getName());
        cmsPage.setPageWebPath(pageWebPath);
        cmsPage.setPagePhysicalPath(pagePhysicalPath);
        cmsPage.setTemplateId(templateId);
        cmsPage.setDataUrl(dataUrlPre+courseId);
        CmsPageResult cmsPageResult = cmsPageClient.save(cmsPage);
        if (!cmsPageResult.isSuccess()){
            return new CoursePreviewResult(CommonCode.FAIL,null);
        }
        String pageId = cmsPageResult.getCmsPage().getPageId();
        String pageUrl = previewUrl+pageId;


        return new CoursePreviewResult(CommonCode.SUCCESS,pageUrl);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public CoursePreviewResult coursePost(String courseId) {
//        发布课程操作
        CourseBase courseBase = findCourseView(courseId);
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(siteId);
        cmsPage.setPageName(courseId+".html");
        cmsPage.setPageAliase(courseBase.getName());
        cmsPage.setPageWebPath(pageWebPath);
        cmsPage.setPagePhysicalPath(pagePhysicalPath);
        cmsPage.setTemplateId(templateId);
        cmsPage.setDataUrl(dataUrlPre+courseId);
        CoursePreviewResult result = cmsPageClient.coursePost(cmsPage);
        if (!result.isSuccess()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        CourseBase pubState = saveCoursePubState(courseId);

//        添加课程到es索引库中:1.创建课程信息合计2.保存至mysql数据库
        CoursePub coursePub = getCoursePub(courseId);
        CoursePub saveCoursePub = saveCoursePub(courseId,coursePub);
        if (saveCoursePub == null){
            ExceptionCast.cast(CourseCode.COURSE_INDEX_ERROR);
        }
        saveTeachplanMediaPub(courseId);
        return result;
    }

    private void saveTeachplanMediaPub(String courseId){
        teachplanMediaPubRepository.deleteByCourseId(courseId);
        List<TeachplanMedia> teachplanMediaList = teachplanMediaRepository.findByCourseId(courseId);
        List<TeachplanMediaPub> list = new ArrayList<>();
        for (TeachplanMedia media : teachplanMediaList) {
            TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
            BeanUtils.copyProperties(media,teachplanMediaPub);
            teachplanMediaPub.setTimestamp(new Date());
            list.add(teachplanMediaPub);
        }
        teachplanMediaPubRepository.saveAll(list);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseResult saveMedia(TeachplanMedia teachplanMedia) {
        if (teachplanMedia == null){
            ExceptionCast.cast(CommonCode.INVALIDPARAM);
        }
        String teachplanId = teachplanMedia.getTeachplanId();
        Optional<Teachplan> optional = teachplanRepository.findById(teachplanId);
        if (!optional.isPresent()){
            ExceptionCast.cast(CourseCode.COURSE_TEACHPLAN_NOTEXIST);
        }
        Teachplan teachplan = optional.get();
        if (!"3".equals(teachplan.getGrade())){
            ExceptionCast.cast(CourseCode.COURSE_TEACHPLAN_NOTCHECKMEDIA);
        }
        TeachplanMedia teachplanMediaSave = null;
        Optional<TeachplanMedia> teachplanMediaOptional = teachplanMediaRepository.findById(teachplanId);
        if (teachplanMediaOptional.isPresent()){
            teachplanMediaSave = teachplanMediaOptional.get();
        }else{
            teachplanMediaSave = new TeachplanMedia();
        }
        teachplanMediaSave.setTeachplanId(teachplanId);
        teachplanMediaSave.setMediaFileOriginalName(teachplanMedia.getMediaFileOriginalName());
        teachplanMediaSave.setMediaId(teachplanMedia.getMediaId());
        teachplanMediaSave.setMediaUrl(teachplanMedia.getMediaUrl());
        teachplanMediaSave.setCourseId(teachplanMedia.getCourseId());
        teachplanMediaRepository.save(teachplanMediaSave);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    private CoursePub saveCoursePub(String courseId,CoursePub coursePub) {
        if (StringUtil.isEmpty(courseId)){
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        CoursePub coursePubNew = null;
        Optional<CoursePub> coursePubOptional = coursePubRepository.findById(courseId);
        if (coursePubOptional.isPresent()){
            coursePubNew = coursePubOptional.get();
        }else{
            coursePubNew = new CoursePub();
        }
        BeanUtils.copyProperties(coursePub,coursePubNew);
        coursePubNew.setId(courseId);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = simpleDateFormat.format(new Date());
        coursePubNew.setTimestamp(new Date());
        coursePubNew.setPubTime(s);
        CoursePub save = coursePubRepository.save(coursePubNew);
        return save;
    }

    private CoursePub getCoursePub(String courseId){
        CoursePub coursePub = new CoursePub();
        coursePub.setId(courseId);
//        查询课程基础信息
        Optional<CourseBase> baseOptional = courseBaseRepository.findById(courseId);
        if (baseOptional.isPresent()){
            CourseBase courseBase = baseOptional.get();
            BeanUtils.copyProperties(courseBase,coursePub);
        }
//        查询课程图片信息
        Optional<CoursePic> picOptional = coursePicRepository.findById(courseId);
        if (picOptional.isPresent()){
            CoursePic CoursePic = picOptional.get();
            BeanUtils.copyProperties(CoursePic,coursePub);
        }
        Optional<CourseMarket> marketOptional = courseMarketRepository.findById(courseId);
        if (marketOptional.isPresent()){
            CourseMarket courseMarket = marketOptional.get();
            BeanUtils.copyProperties(courseMarket,coursePub);
        }
        TeachplanNode teachplanNode = findTeachplanNodeById(courseId);
        String s = JSON.toJSONString(teachplanNode);
        coursePub.setTeachplan(s);
        return coursePub;
    }

    private CourseBase saveCoursePubState(String courseId){
        CourseBase courseBase = findCourseView(courseId);
        courseBase.setStatus("202002");
        CourseBase base = courseBaseRepository.save(courseBase);
        return base;
    }

    private CourseMarket saveCourseMarket(String id,CourseMarket courseMarket){
        if (courseMarket == null){
            ExceptionCast.cast(CommonCode.INVALIDPARAM);
        }
        courseMarket.setId(id);
        CourseMarket save = courseMarketRepository.save(courseMarket);
        return save;
    }

    /**
     * 根据课程ID获取根节点ID
     * @param courseId  课程ID
     * @return 根节点ID
     */
    private String getTeachplanRoot(String courseId){
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()){
            return null;
        }
        CourseBase courseBase = optional.get();
        List<Teachplan> teachplans = teachplanRepository.findByCourseidAndParentid(courseId, "0");
        if (teachplans==null||teachplans.size()==0){
            Teachplan teachplan = new Teachplan();
            teachplan.setParentid("0");
            teachplan.setCourseid(courseId);
            teachplan.setGrade("1");
            teachplan.setPname(courseBase.getName());
            teachplan.setStatus("0");
            Teachplan teachplan1 = teachplanRepository.save(teachplan);
            return teachplan1.getId();
        }
        Teachplan teachplan = teachplans.get(0);
        return teachplan.getId();
    }
}
