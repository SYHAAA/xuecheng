package com.xuecheng.learning.service;

import com.github.pagehelper.util.StringUtil;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.learning.XcLearningCourse;
import com.xuecheng.framework.domain.learning.response.GetMediaResult;
import com.xuecheng.framework.domain.learning.response.LearnCode;
import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.learning.client.SearchClient;
import com.xuecheng.learning.repository.XcLearningCourseRepository;
import com.xuecheng.learning.repository.XcTaskHisRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @description: 学习模块业务类
 * @author: 沈煜辉
 * @create: 2020-03-14 14:36
 **/
@Service
public class LearnService {

    @Autowired
    SearchClient searchClient;
    @Autowired
    XcLearningCourseRepository xcLearningCourseRepository;
    @Autowired
    XcTaskHisRepository xcTaskHisRepository;

    public GetMediaResult getmedia(String courseId, String teachplanId) {
        TeachplanMediaPub teachplanMediaPub = searchClient.getMeida(teachplanId);
        if (teachplanMediaPub==null|| StringUtil.isEmpty(teachplanMediaPub.getMediaUrl())){
            ExceptionCast.cast(LearnCode.LEARNING_MEDIAURL_ERROR);
        }
        String mediaUrl = teachplanMediaPub.getMediaUrl();
        return new GetMediaResult(CommonCode.SUCCESS,mediaUrl);
    }

    @Transactional(rollbackOn = Exception.class)
    public ResponseResult addCourse(String userId, String courseId, XcTask xcTask){
        if (StringUtil.isEmpty(userId)){
            ExceptionCast.cast(LearnCode.LEARNING_ADDCOURSE_USERIDISNULL);
        }
        if (StringUtil.isEmpty(courseId)){
            ExceptionCast.cast(LearnCode.LEARNING_ADDCOURSE_COURSEIDISNULL);
        }
        if (xcTask ==null||xcTask.getId()==null){
            ExceptionCast.cast(CommonCode.INVALIDPARAM);
        }
        Optional<XcTaskHis> optionalXcTaskHis = xcTaskHisRepository.findById(xcTask.getId());
        if (optionalXcTaskHis.isPresent()){
            return new ResponseResult(CommonCode.SUCCESS);
        }
        XcLearningCourse xcLearningCourse = xcLearningCourseRepository.findByUserIdAndCourseId(userId, courseId);
        if (xcLearningCourse == null){
            xcLearningCourse = new XcLearningCourse();
            xcLearningCourse.setUserId(userId);
            xcLearningCourse.setCourseId(courseId);
            xcLearningCourse.setStatus("501001");
            xcLearningCourseRepository.save(xcLearningCourse);
        }else{
            xcLearningCourse.setStatus("501001");
            xcLearningCourseRepository.save(xcLearningCourse);
        }
        XcTaskHis xcTaskHis = new XcTaskHis();
        BeanUtils.copyProperties(xcTask,xcTaskHis);
        xcTaskHisRepository.save(xcTaskHis);
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
