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
    @Override
    public List<User> getAllUser() {
        List<User> allUser = userMapper.getAllUser();
        return allUser;
    }
}
