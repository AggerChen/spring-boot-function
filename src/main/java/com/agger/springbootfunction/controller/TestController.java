package com.agger.springbootfunction.controller;

import com.agger.springbootfunction.vo.Person;
import com.agger.springbootfunction.vo.ResultVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

/**
 * @program: TestController
 * @description: 测试控制类
 * @author: chenhx
 * @create: 2019-11-29 10:13
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/getUser")
    public ResultVO getUser(){
        Person person = new Person(25,"张三");
        return new ResultVO(0,"查询成功",person);
    }

    @GetMapping("/getUserById")
    public ResultVO getUserById(Integer id){
        Person person = new Person(id,"汤姆");
        return new ResultVO(0,"查询成功",person);
    }

    @PostMapping("/postUser")
    public ResultVO postUser(Person person){
        System.out.println(person.toString());
        return new ResultVO(0,"添加成功",person.toString());
    }

    @PostMapping("/postUser2")
    public ResultVO postUser2(@RequestBody Person person){
        System.out.println(person.toString());
        return new ResultVO(0,"添加成功2",person.toString());
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResultVO deleteUser(@PathVariable("id") Integer id){
        System.out.println("删除成功" + id);
        return new ResultVO(0,"删除成功",id);
    }

    @PostMapping("/upload")
    public ResultVO upload(MultipartRequest request) {
        // Spring MVC 使用 MultipartRequest 接受带文件的 HTTP 请求
        MultipartFile file = request.getFile("file");
        String originalFilename = file.getOriginalFilename();
        return new ResultVO(0,"上传成功",originalFilename);
    }

}
