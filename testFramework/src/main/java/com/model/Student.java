package com.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @description: 学生类
 * @author: 沈煜辉
 * @create: 2020-02-01 16:29
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private String name;
    private int age;
    private Date birthday;
    private Float money;
    private List<Student> friends;
    private Student bestFriend;
}
