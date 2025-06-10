package com.cloudlibrary_api.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserVO {
    private Long userId;
    private String username;
    private String email;
    private String phone;
    private Integer status;
    private String statusDesc;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 