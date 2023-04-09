package com.example.picsharingspringboot.controller;

import com.example.picsharingspringboot.entity.User;
import com.example.picsharingspringboot.service.UserService;
import com.example.picsharingspringboot.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private UserService userService;
//    获取所有用户数据
    @RequestMapping("/get/allUser")
    public ResponseResult<List<User>> getAllUser(){
        List<User> allUser = userService.getAllUser();
        ResponseResult<List<User>> rr = new ResponseResult<List<User>>();
        rr.setData(allUser);
        rr.setState(200);
        return rr;
    }
//    模糊查询
    @RequestMapping("/search/user/{username}")
    public ResponseResult<List<User>> getUserBycondition(@PathVariable("username") String username){
        ResponseResult<List<User>> rr = new ResponseResult<>();
        List<User> users = userService.searchUser(username);
        rr.setData(users);
        rr.setState(200);
        return rr;
    }
}
