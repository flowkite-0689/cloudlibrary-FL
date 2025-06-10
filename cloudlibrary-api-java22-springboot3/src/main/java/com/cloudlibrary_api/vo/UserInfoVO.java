package com.cloudlibrary_api.vo;

import lombok.Data;

@Data
public class UserInfoVO {
    private Integer userId;
    private String username;
    private String nickname;
    private String email;
    private String gender;
    private String role;
    private Integer status;
    private String statusDESC;
    private String avatar;
    private String password;
} 