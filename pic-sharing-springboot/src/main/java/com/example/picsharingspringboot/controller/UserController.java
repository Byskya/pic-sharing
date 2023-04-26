package com.example.picsharingspringboot.controller;

import com.example.picsharingspringboot.entity.Favorite;
import com.example.picsharingspringboot.entity.Follow;
import com.example.picsharingspringboot.entity.User;
import com.example.picsharingspringboot.service.UserService;
import com.example.picsharingspringboot.util.ResponseResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//设置接收前端发送请求中携带的cookie
@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081"}, allowCredentials = "true")
@RestController
public class UserController {
    @Autowired
    private UserService userService;
//    获取用户的关注列表
    @GetMapping("/user/followList/{pageNum}/{pageSize}")
    public ResponseResult<PageInfo<User>> getUserFollowList(@PathVariable("pageNum")Integer pageNum,@PathVariable("pageSize")Integer pageSize,HttpSession session){
        PageHelper.startPage(pageNum,pageSize);
        ResponseResult<PageInfo<User>> rr= new ResponseResult<>();
        User user = (User)session.getAttribute("user");
        Follow follow = new Follow();
        follow.setFollowerId(user.getId());
        List<User> list = userService.getUserFollowList(follow);
        if (!list.isEmpty()){
            PageInfo<User> pageInfo = new PageInfo<>(list);
            rr.setState(200);
            rr.setMessage("获取用户关注列表成功");
            rr.setData(pageInfo);
        }
        else {
            rr.setData(null);
            rr.setMessage("获取用户关注列表失败");
            rr.setState(500);
        }
        return rr;
    }
//    取消关注
    @PostMapping("/delete/follow/{followingId}")
    @Transactional
    public ResponseResult<Follow> deleteFollow(@PathVariable("followingId") Integer followingId,HttpSession session){
        ResponseResult<Follow> rr = new ResponseResult<>();
        Follow follow = new Follow();
        follow.setFollowingId(followingId);
        User user = (User)session.getAttribute("user");
        follow.setFollowerId(user.getId());
        boolean judge = userService.deleteFollow(follow);
        if (judge){
            rr.setMessage("取消关注成功");
            rr.setState(200);
            rr.setData(follow);
        }
        else {
            rr.setData(follow);
            rr.setState(500);
            rr.setMessage("取消关注失败");
        }
        return rr;
    }
//    关注指定用户
    @PostMapping("/follow/{followingId}")
    public ResponseResult<Follow> followUser(@PathVariable("followingId") Integer followingId,HttpSession session){
        ResponseResult<Follow> rr = new ResponseResult<>();
        Follow follow = new Follow();
        follow.setFollowingId(followingId);
        User user = (User)session.getAttribute("user");
        follow.setFollowerId(user.getId());
        boolean judge = userService.followUser(follow);
        if (judge){
            rr.setMessage("关注成功");
            rr.setState(200);
            rr.setData(follow);
        }
        else {
            rr.setData(follow);
            rr.setState(500);
            rr.setMessage("关注失败");
        }
        return rr;
    }
//    判断用户之间是否存在关注关系
    @GetMapping("/check/isFollow/{followingId}")
    public ResponseResult<Follow> checkFollow(@PathVariable("followingId") Integer followingId, HttpSession session){
        ResponseResult<Follow> rr = new ResponseResult<>();
        Follow follow = new Follow();
        follow.setFollowingId(followingId);
        User user = (User)session.getAttribute("user");
        follow.setFollowerId(user.getId());
        Follow realHas = userService.checkFollow(follow);
        if (realHas!=null){
            rr.setMessage("用户已关注");
            rr.setState(200);
            rr.setData(realHas);
        }
        else {
            rr.setState(500);
            rr.setMessage("用户未关注");
            rr.setData(null);
        }
        return rr;
    }
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
    public ResponseResult<PageInfo<User>> getAllUserByPage(
            @PathVariable("pageNum") Integer pageNum,
            @PathVariable("pageSize") Integer pageSize
    ){
        PageHelper.startPage(pageNum, pageSize);
        ResponseResult<PageInfo<User>> rr = new ResponseResult<PageInfo<User>>();
        List<User> allUser = userService.getAllUser();
        PageInfo<User> userPageInfo = new PageInfo<>(allUser);
        rr.setData(userPageInfo);
        rr.setState(200);
        return rr;
    }
//    管理员修改用户信息
    @PutMapping("/user/update")
    public ResponseResult<User> userUpdate(@RequestBody User user){
        ResponseResult<User> rr = new ResponseResult<>();
        userService.updateUserInfo(user);
        rr.setData(user);
        rr.setState(200);
        rr.setMessage("用户信息修改成功");
        return rr;
    }
//    用户自己修改个人信息
    @PutMapping("/user/infoEdit")
    public ResponseResult<User> userInfoEdit(
            @RequestPart("avatar") MultipartFile avatar,
            @RequestPart("username") String username,
            @RequestPart("gender") String gender,
            @RequestPart("birthday") String birthday,
            @RequestPart("address") String address,
            @RequestPart("id") String id
    ) throws IOException, ParseException {

        ResponseResult<User> rr = new ResponseResult<>();
        User user = new User();
        user.setAvatar(avatar.getBytes());
        user.setId(Integer.valueOf(id));
        System.out.println(Arrays.toString(avatar.getBytes()));
        user.setUsername(username);
        if (gender.equals("男")){
            user.setGender('男');
        }
        else{
            user.setGender('女');
        }
        user.setAddress(address);
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z", Locale.US);
        Date date = formatter.parse(birthday);
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        user.setBirthday(sqlDate);
        boolean judge = userService.editUserInfo(user);
        if (judge){
            System.out.println("================修改成功");
            rr.setData(user);
            rr.setState(200);
            rr.setMessage("用户信息修改成功");
        }
        else {
            System.out.println("==================修改失败");
            rr.setData(null);
            rr.setState(200);
            rr.setMessage("用户信息修改失败");
        }
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
    public ResponseResult<User> userLogin(@RequestBody User user, HttpSession session){
        ResponseResult<User> rr = new ResponseResult<>();
        User userReal = userService.userLogin(user);
        //把用户登录信息保存到session中
        User userSession = new User();
        userSession.setId(userReal.getId());
        userSession.setUsername(userReal.getUsername());
        userSession.setEmail(userReal.getEmail());
        if (userReal!=null){
            session.setAttribute("user",userSession);
            rr.setData(userSession);
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
    //获取当前登录用户的信息
    @GetMapping("/user/get/info")
    public ResponseResult<User> userInfo(HttpSession session) throws IOException {
        ResponseResult<User> rr = new ResponseResult<>();
        User user = (User)session.getAttribute("user");
        User userInfo =  userService.getUserInfo(user);

        if (userInfo!=null){
            rr.setData(userInfo);
            rr.setState(200);
            rr.setMessage("获取用户信息成功");
        }
        else {
            rr.setData(null);
            rr.setState(500);
            rr.setMessage("获取用户信息失败");
        }
        return rr;
    }
    //    根据用户id查找用户
    @GetMapping("/user/info/{userId}")
    public ResponseResult<User> getUserInfoById(@PathVariable("userId")Integer userId) throws IOException {
        ResponseResult<User> rr = new ResponseResult<>();
        User user = new User();
        user.setId(userId);
        User userInfo =  userService.getUserInfo(user);
        if (userInfo!=null){
            rr.setData(userInfo);
            rr.setState(200);
            rr.setMessage("作者信息加载成功");
        }
        else {
            rr.setData(null);
            rr.setState(500);
            rr.setMessage("作者信息加载失败");
        }
        return rr;
    }
}
