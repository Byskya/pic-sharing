package com.example.picsharingspringboot.service;

import com.example.picsharingspringboot.entity.Follow;
import com.example.picsharingspringboot.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUser();

    List<User> searchUser(String username);

    User updateUserInfo(User user);

    void banUser(Integer id);

    boolean adminLogin(User user);

    User userLogin(User user);

    boolean userRegister(User user);

    User getUserInfo(User user);

    boolean editUserInfo(User user);

    Follow checkFollow(Follow follow);

    boolean followUser(Follow follow);

    boolean deleteFollow(Follow follow);

    List<User> getUserFollowList(Follow follow);
}
