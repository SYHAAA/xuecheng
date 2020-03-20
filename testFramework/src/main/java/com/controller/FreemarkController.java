package com.controller;


import com.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @description:
 * @author: 沈煜辉
 * @create: 2020-02-01 16:35
 **/
@Controller
@RequestMapping("/freemarker")
public class FreemarkController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/test1")
    public String freemark(Map<String,Object> map){
        Student student = new Student();
        student.setName("小王");
        student.setAge(20);
        student.setMoney(100.23f);
        student.setBirthday(new Date());
        Student bestFri = new Student("小赵",19,null,1000f,null,null);
        student.setBestFriend(bestFri);
        Student fri1 = new Student("小李",19,null,1000f,null,null);
        Student fri2 = new Student("小张",22,null,999f,null,null);
        Student fri3 = new Student("小沈",19,null,1200f,null,null);
        List<Student> friends = new ArrayList<>();
        friends.add(fri1);
        friends.add(fri2);
        friends.add(fri3);
        student.setFriends(friends);
        map.put("student",student);
        map.put("friends",friends);
        map.put("fri1",fri1);
        Map map1 = new HashMap(8);
        map1.put("one",fri1);
        map1.put("two",fri2);
        map1.put("three",fri3);
        map.put("stuMap",map1);
        return "test";
    }

    @RequestMapping("/indexBanner")
    public String indexBanner(Map<String,Object> map){
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getModel/5a791725dd573c3574ee333f", Map.class);
        Map body = forEntity.getBody();
        map.putAll(body);
        return "index_banner";
    }

    @RequestMapping("/courseView")
    public String getCourse(Map<String,Object> map){
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31200/course/courseView/297e7c7c62b888f00162b8a7dec20000", Map.class);
        Map body = forEntity.getBody();
        map.putAll(body);
        return "course";
    }
}
