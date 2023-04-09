package com.example.picsharingspringboot.service;

import com.example.picsharingspringboot.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUser();

    List<User> searchUser(String username);
}
