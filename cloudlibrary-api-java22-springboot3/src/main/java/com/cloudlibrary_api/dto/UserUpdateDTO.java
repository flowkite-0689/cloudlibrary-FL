package com.cloudlibrary_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserUpdateDTO {
    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    private String nickname;

    @Email(message = "邮箱格式不正确")
    private String email;

    @Pattern(regexp = "^(MALE|FEMALE|UNKNOWN)$", message = "性别只能是 MALE/FEMALE/UNKNOWN")
    private String gender;

    private String avatar;

    private String username;

    private String password;
} 