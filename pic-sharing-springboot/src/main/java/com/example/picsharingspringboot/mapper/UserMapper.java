package com.example.picsharingspringboot.mapper;

import com.example.picsharingspringboot.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface UserMapper {
    List<User> getAllUser();
}