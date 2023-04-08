package com.example.picsharingspringboot.controller;

import com.example.picsharingspringboot.entity.User;
import com.example.picsharingspringboot.service.UserService;
import com.example.picsharingspringboot.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;
@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping("/get/allUser")
    public ResponseResult<List<User>> getAllUser(){
        System.out.println("发送查询所有员工请求");
        List<User> allUser = userService.getAllUser();
        ResponseResult<List<User>> rr = new ResponseResult<List<User>>();
        rr.setData(allUser);
        rr.setState(200);
        return rr;
    }
}
