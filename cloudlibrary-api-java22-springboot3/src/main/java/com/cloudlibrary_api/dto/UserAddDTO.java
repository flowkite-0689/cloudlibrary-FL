package com.cloudlibrary_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserAddDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email=username+"default@qq.com";

    @NotBlank(message = "昵称不能为空")
    private String nickname="default";

    @Pattern(regexp = "^(MALE|FEMALE|UNKNOWN)$", message = "性别只能是 MALE/FEMALE/UNKNOWN")
    private String gender = "UNKNOWN";  // 默认值

    private String role = "USER";       // 默认值

    private String avatar="http://sx8glofs4.hn-bkt.clouddn.com/avatar/1749084470028.jpg";              // 可选

    private Integer status = 0;         // 默认正常状态
} 