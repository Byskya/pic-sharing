package com.example.picsharingspringboot.entity;

import lombok.*;

import java.sql.Date;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class User extends BaseEntity{
    private static final long serialVersionUID = -1327935301720951537L;
    private Integer id;
    private String username;
    private String password;
    private char gender;
    private Integer age;
    private String email;
    private String phoneNumber;
    private Date birthday;
    private byte[] avatar;
    private String address;
    private boolean isUser;
    private boolean isAdmin;
    private Integer isDelete;
    private String salt;
}
