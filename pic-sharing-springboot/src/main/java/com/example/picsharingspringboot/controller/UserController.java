package com.example.picsharingspringboot.controller;

import com.example.picsharingspringboot.entity.User;
import com.example.picsharingspringboot.service.UserService;
import com.example.picsharingspringboot.util.ResponseResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
@RestController
public class UserController {
    @Autowired
    private UserService userService;
////    获取所有用户数据
//    @RequestMapping("/get/allUser")
//    public ResponseResult<List<User>> getAllUser(){
//        List<User> allUser = userService.getAllUser();
//        ResponseResult<List<User>> rr = new ResponseResult<List<User>>();
//        rr.setData(allUser);
//        rr.setState(200);
//        return rr;
//    }
//    模糊查询
    @RequestMapping("/search/user/{username}/{pageNum}/{pageSize}")
    public ResponseResult<PageInfo<User>> getUserBycondition(@PathVariable("username") String username,@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        ResponseResult<PageInfo<User>> rr = new ResponseResult<>();
        List<User> users = userService.searchUser(username);
        PageInfo<User> userPageInfo = new PageInfo<>(users);
        rr.setData(userPageInfo);
        rr.setState(200);
        return rr;
    }
//    分页查询
    @RequestMapping("/get/allUser/{pageNum}/{pageSize}")
    public ResponseResult<PageInfo<User>> getAllUserByPage(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize){
        PageHelper.startPage(pageNum, pageSize);
        ResponseResult<PageInfo<User>> rr = new ResponseResult<PageInfo<User>>();
        List<User> allUser = userService.getAllUser();
        PageInfo<User> userPageInfo = new PageInfo<>(allUser);
        rr.setData(userPageInfo);
        rr.setState(200);
        return rr;
    }
//    修改用户信息
    @PutMapping("/user/update")
    public ResponseResult<User> userUpdate(@RequestBody User user){
        ResponseResult<User> rr = new ResponseResult<>();
        userService.updateUserInfo(user);
        rr.setData(user);
        rr.setState(200);
        rr.setMessage("用户信息修改成功");
        return rr;
    }
//    拉黑用户
    @PutMapping("/user/ban/{id}")
    public ResponseResult<User> userBan(@PathVariable("id") Integer id){
        ResponseResult<User> rr = new ResponseResult<>();
        userService.banUser(id);
        rr.setState(200);
        rr.setMessage("用户拉黑成功");
        return rr;
    }
//    管理员登录
    @PostMapping("/admin/login")
    public ResponseResult<User> adminLogin(@RequestBody User user){
        ResponseResult<User> rr = new ResponseResult<>();
        boolean judge = userService.adminLogin(user);
        if (judge){
            rr.setData(user);
            rr.setState(200);
            rr.setMessage("用户登录成功");
        }
        else {
            rr.setData(null);
            rr.setState(500);
            rr.setMessage("用户密码错误或者被拉黑");
        }
        return rr;
    }
    //    普通用户登录
    @PostMapping("/user/login")
    public ResponseResult<User> userLogin(@RequestBody User user){
        ResponseResult<User> rr = new ResponseResult<>();
        boolean judge2 = userService.userLogin(user);
        if (judge2){
            rr.setData(user);
            rr.setState(200);
            rr.setMessage("用户登录成功");
        }
        else {
            rr.setData(null);
            rr.setState(500);
            rr.setMessage("用户密码或账号输入错误或者被拉黑");
        }
        return rr;
    }
    //普通用户注册
    @PostMapping("/user/register")
    public ResponseResult<User> userRegister(@RequestBody User user){
        System.out.println("用户注册============================");
        ResponseResult<User> rr = new ResponseResult<>();
        boolean judge = userService.userRegister(user);
        if (judge){
            rr.setData(user);
            rr.setState(200);
            rr.setMessage("用户注册成功");
        }
        else {
            rr.setData(null);
            rr.setState(500);
            rr.setMessage("用户已存在或其他错误，请重试");
        }
        return rr;
    }
}
