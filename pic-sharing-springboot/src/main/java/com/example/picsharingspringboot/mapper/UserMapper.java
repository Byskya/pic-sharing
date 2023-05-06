package com.example.picsharingspringboot.mapper;
import com.example.picsharingspringboot.entity.Follow;
import com.example.picsharingspringboot.entity.User;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface UserMapper {
    List<User> getAllUser();

    List<User> searchUser(String username);

    void updateUserInfo(User user);

    void banUser(Integer id);

    User getUserInfo(User user);

    int addUser(User user);

    boolean editUserInfo(User user);

    User getUserInfoById(User user);

    Follow checkFollow(Follow follow);

    boolean followUser(Follow follow);

    boolean deleteFollow(Follow follow);

    List<User> getUserFollowList(Follow follow);

    List<User> getFansList(Follow follow);
}
