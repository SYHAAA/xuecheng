package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @description: 课程数据模型
 * @author: 沈煜辉
 * @create: 2020-03-06 12:21
 **/
@Data
@NoArgsConstructor
@ToString
public class CourseView implements Serializable {
    CourseBase courseBase;
    CoursePic coursePic;
    CourseMarket courseMarket;
    TeachplanNode teachplanNode;
}
