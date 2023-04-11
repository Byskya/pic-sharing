package com.example.picsharingspringboot.service.impl;

import com.example.picsharingspringboot.entity.User;
import com.example.picsharingspringboot.mapper.UserMapper;
import com.example.picsharingspringboot.service.UserService;
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
//    用户登录
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
}
