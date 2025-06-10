package com.cloudlibrary_api.service;

import com.cloudlibrary_api.dto.*;
import com.cloudlibrary_api.pojo.User;
import com.cloudlibrary_api.vo.UserLoginVO;
import com.cloudlibrary_api.vo.UserInfoVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author steven
* @description 针对表【user】的数据库操作Service
* @createDate 2025-06-02 17:38:01
*/
public interface UserService extends IService<User> {

    /**
     * 新增用户（带用户名唯一性校验）
     */
    boolean saveUser(UserAddDTO userAddDTO);

    /**
     * 用户登录
     * @param loginDTO 登录信息
     * @return 登录成功后的用户信息
     */
    UserLoginVO login(UserLoginDTO loginDTO);

    /**
     * 更新用户信息
     * @param updateDTO 更新的用户信息
     * @return 更新后的用户信息
     */
    UserLoginVO updateUser(UserUpdateDTO updateDTO);

    /**
     * 获取当前登录用户的详细信息
     * @return 用户详细信息
     */
    UserInfoVO getCurrentUserInfo();

    /**
     * 更新用户头像
     * @param avatarUrl 新的头像URL
     */
    void updateUserAvatar(String avatarUrl);

    /**
     * 发送注册验证码
     * @param email 邮箱
     */
    void sendRegisterCode(String email);

    /**
     * 用户注册
     * @param registerDTO 注册信息
     * @return 注册成功的用户信息
     */
    UserLoginVO register(UserRegisterDTO registerDTO);

    /**
     * 修改用户密码
     * @param passwordUpdateDTO 密码修改信息
     */
    void updatePassword(PasswordUpdateDTO passwordUpdateDTO);

    /**
     * 上传用户头像
     * @param file 头像文件
     * @return 头像URL
     */
    String uploadAvatar(MultipartFile file);

    // 分页查询用户
    IPage<UserInfoVO> pageQueryUsers(UserPageQueryDTO queryDTO);

    /**
     * 发送登录验证码
     * @param email 邮箱
     */
    void sendLoginCode(EmailDTO email);

    /**
     * 邮箱验证码登录
     * @param emailLoginDTO 邮箱登录参数
     * @return 登录结果
     */
    UserLoginVO loginByEmail(EmailLoginDTO emailLoginDTO);
}
