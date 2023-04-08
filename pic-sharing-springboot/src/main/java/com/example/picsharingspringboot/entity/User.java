package com.example.picsharingspringboot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@ToString
public class User extends BaseEntity{
    private static final long serialVersionUID = -1327935301720951537L;
    private Integer id;
    private String username;
    private String password;
    private char gender;
    private String email;
    private String phoneNumber;
    private Date birthday;
    private String avatar;
    private String address;
    private boolean isUser;
    private boolean isAdmin;
    public User(Integer id, String username, String password, char gender, String email, String phoneNumber, Date birthday, String avatar, String address, boolean isUser, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.avatar = avatar;
        this.address = address;
        this.isUser = isUser;
        this.isAdmin = isAdmin;
    }
    public User(Timestamp createdAt, Timestamp updatedAt, Integer id, String username, String password, char gender, String email, String phoneNumber, Date birthday, String avatar, String address, boolean isUser, boolean isAdmin) {
        super(createdAt, updatedAt);
        this.id = id;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.avatar = avatar;
        this.address = address;
        this.isUser = isUser;
        this.isAdmin = isAdmin;
    }
}
