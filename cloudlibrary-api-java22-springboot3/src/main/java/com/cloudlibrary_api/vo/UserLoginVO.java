package com.cloudlibrary_api.vo;

import lombok.Data;

@Data
public class UserLoginVO {
    private Integer userId;
    private String username;
    private String nickname;
    private String email;
    private String gender;
    private String role;
    private Integer status;
    private String avatar;
    private String token;  // JWT token
} 