package com.example.picsharingspringboot.service.impl;

import com.example.picsharingspringboot.entity.Follow;
import com.example.picsharingspringboot.entity.User;
import com.example.picsharingspringboot.mapper.UserMapper;
import com.example.picsharingspringboot.service.UserService;
import com.example.picsharingspringboot.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
//    获取所有数据库中所有的用户信息
    @Override
    public List<User> getAllUser() {
        List<User> allUser = userMapper.getAllUser();
        return allUser;
    }
//    根据用户名查找用户
    @Override
    public List<User> searchUser(String username) {
        List<User> users = userMapper.searchUser(username);
        return users;
    }
//    修改用户信息
    @Override
    public User updateUserInfo(User user) {
        userMapper.updateUserInfo(user);
        return null;
    }
//    拉黑用户
    @Override
    public void banUser(Integer id) {
        userMapper.banUser(id);
    }
//    管理员登录
    @Override
    public boolean adminLogin(User user) {
        User userReal = userMapper.getUserInfo(user);
        if (userReal!=null){
            if (userReal.isAdmin()){
                if (user.getPassword().equals(userReal.getPassword()) && userReal.getIsDelete()!=1){
                    return true;
                }
                else{
                    return false;
                }
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }
//    普通用户登录
    @Override
    public User userLogin(User user) {
        User userReal = userMapper.getUserInfo(user);
//        md5加密
        String salt = userReal.getSalt();
        String md5Password = MD5Util.getSaltMD5(user.getPassword(), salt);
        user.setPassword(md5Password);
        if (userReal!=null){
            if (userReal.getPassword().equals(md5Password) && userReal.getIsDelete() != 1){
                return userReal;
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

    @Override
    public boolean userRegister(User user) {
//        密码通过md5加密
        String salt = MD5Util.generateSalt();
        user.setSalt(salt);
        // 加盐后的MD5值
        String md5Password = MD5Util.getSaltMD5(user.getPassword(), salt);
        user.setPassword(md5Password);
        int judge = userMapper.addUser(user);
        if (judge!=0){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public User getUserInfo(User user) {
        User userInfo = userMapper.getUserInfoById(user);
        return userInfo;
    }

    @Override
    public boolean editUserInfo(User user) {
        boolean judge = userMapper.editUserInfo(user);
        return judge;
    }

    @Override
    public Follow checkFollow(Follow follow) {
        return userMapper.checkFollow(follow);
    }

    @Override
    public boolean followUser(Follow follow) {
        return userMapper.followUser(follow);
    }

    @Override
    public boolean deleteFollow(Follow follow) {
        return userMapper.deleteFollow(follow);
    }

    @Override
    public List<User> getUserFollowList(Follow follow) {
        return userMapper.getUserFollowList(follow);
    }

    @Override
    public List<User> getUserFansList(Follow follow) {
        return userMapper.getFansList(follow);
    }
}
