package com.cloudlibrary_api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudlibrary_api.common.utils.Result;
import com.cloudlibrary_api.dto.*;
import com.cloudlibrary_api.service.UserService;
import com.cloudlibrary_api.vo.UserLoginVO;
import com.cloudlibrary_api.vo.UserInfoVO;
import com.cloudlibrary_api.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户相关操作")
public class UserController {
    private final UserService userService;

    @PostMapping("/register/code")
    @Operation(summary = "发送注册验证码", description = "向指定邮箱发送注册验证码")
    public Result<Void> sendRegisterCode(@RequestParam @Email(message = "邮箱格式不正确") String email) {
        userService.sendRegisterCode(email);
        return Result.success();
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "使用邮箱验证码注册新用户")
    public Result<UserLoginVO> register(@RequestBody @Valid UserRegisterDTO registerDTO) {
        UserLoginVO userVO = userService.register(registerDTO);
        return Result.success(userVO);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "使用邮箱/用户名和密码登录")
    public Result<UserLoginVO> login(@RequestBody @Valid UserLoginDTO loginDTO) {
        UserLoginVO loginVO = userService.login(loginDTO);
        return Result.success(loginVO);
    }

    @PostMapping("/login/code/send")
    @Operation(summary = "发送登录验证码", description = "发送邮箱验证码用于登录")
    public Result<Void> sendLoginCode(@RequestBody @Valid EmailDTO emaildto) {
        userService.sendLoginCode(emaildto);
        return Result.success();
    }

    @PostMapping("/login/email")
    @Operation(summary = "邮箱验证码登录", description = "使用邮箱和验证码进行登录")
    public Result<UserLoginVO> loginByEmail(@RequestBody @Valid EmailLoginDTO emailLoginDTO) {
        UserLoginVO loginVO = userService.loginByEmail(emailLoginDTO);
        return Result.success(loginVO);
    }

    @PutMapping("/password")
    @Operation(summary = "修改密码", description = "修改用户密码")
    public Result<Void> updatePassword(@RequestBody @Valid PasswordUpdateDTO passwordUpdateDTO) {
        userService.updatePassword(passwordUpdateDTO);
        return Result.success();
    }

    @PostMapping("/avatar")
    @Operation(summary = "上传头像", description = "上传并更新用户头像")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        String avatarUrl = userService.uploadAvatar(file);
        return Result.success(avatarUrl);
    }

    @PostMapping("/add")
    @Operation(summary = "添加用户", description = "添加新用户")
    public Result<Void> addUser(@RequestBody @Valid UserAddDTO userAddDTO) {
        userService.saveUser(userAddDTO);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "更新用户信息", description = "更新用户基本信息")
    public Result<UserLoginVO> updateUser(@RequestBody @Valid UserUpdateDTO updateDTO) {
        UserLoginVO userVO = userService.updateUser(updateDTO);
        return Result.success(userVO);
    }

    @GetMapping("/userInfo")
    @Operation(summary = "获取用户信息", description = "获取当前登录用户的详细信息")
    public Result<UserInfoVO> getUserInfo() {
        UserInfoVO userInfo = userService.getCurrentUserInfo();
        return Result.success(userInfo);
    }

    @PatchMapping("/updateAvatar")
    @Operation(summary = "更新用户头像", description = "更新当前登录用户的头像")
    public Result<String> updateAvatar(@RequestParam String avatarUrl) {
        userService.updateUserAvatar(avatarUrl);
        return Result.success(avatarUrl);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询用户", description = "支持按用户名、邮箱、手机号、状态筛选")
    public Result<IPage<UserInfoVO>> pageUsers(@Validated UserPageQueryDTO queryDTO) {
        log.info("分页查询用户，参数：{}", queryDTO);
        IPage<UserInfoVO> page = userService.pageQueryUsers(queryDTO);
        return Result.success(page);
    }
} 