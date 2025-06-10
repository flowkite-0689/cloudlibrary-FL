package com.cloudlibrary_api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudlibrary_api.common.enums.UserStatus;
import com.cloudlibrary_api.common.exception.BusinessException;
import com.cloudlibrary_api.common.exception.ErrorCode;
import com.cloudlibrary_api.common.utils.JwtUtil;
import com.cloudlibrary_api.common.utils.QiniuUtil;
import com.cloudlibrary_api.common.utils.UserContext;
import com.cloudlibrary_api.dto.*;
import com.cloudlibrary_api.mapper.UserMapper;
import com.cloudlibrary_api.pojo.User;
import com.cloudlibrary_api.service.EmailService;
import com.cloudlibrary_api.service.UserService;
import com.cloudlibrary_api.vo.UserLoginVO;
import com.cloudlibrary_api.vo.UserInfoVO;
import com.qiniu.storage.model.DefaultPutRet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
* @author steven
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-06-02 17:38:01
*/
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    private final EmailService emailService;
    private final QiniuUtil qiniuUtil;
    private static final String DEFAULT_AVATAR = "http://sx8glofs4.hn-bkt.clouddn.com/avatar/1749084470028.jpg"; // 替换为实际的默认头像URL

    public UserServiceImpl(EmailService emailService, QiniuUtil qiniuUtil) {
        this.emailService = emailService;
        this.qiniuUtil = qiniuUtil;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUser(UserAddDTO userAddDTO) {
        // 1. 用户名唯一性校验
        checkUsernameUnique(userAddDTO.getUsername());
        // 2. 邮箱唯一性校验
        checkEmailUnique(userAddDTO.getEmail());

        // 3. 创建用户实体并复制属性
        User user = new User();
        BeanUtils.copyProperties(userAddDTO, user);

        // 4. 保存到数据库
        boolean success = this.save(user);

        if (success) {
            log.info("新增用户成功，username: {}, email: {}", user.getUsername(), user.getEmail());
        }
        return success;
    }

    @Override
    public void sendRegisterCode(String email) {
        // 1. 检查邮箱是否已被注册
        if (StringUtils.isNotBlank(email)) {
            checkEmailUnique(email);
        }
        
        // 2. 发送验证码
        emailService.sendVerificationCode(email);
        log.info("发送注册验证码到邮箱：{}", email);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserLoginVO register(UserRegisterDTO registerDTO) {
        // 1. 验证验证码
        if (!emailService.verifyCode(registerDTO.getEmail(), registerDTO.getVerificationCode())) {
            throw new BusinessException(ErrorCode.INVALID_CODE);
        }

        // 2. 用户名唯一性校验
        checkUsernameUnique(registerDTO.getUsername());
        
        // 3. 邮箱唯一性校验
        checkEmailUnique(registerDTO.getEmail());

        // 4. 创建用户实体
        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);
        user.setStatus(0); // 默认状态为正常
        user.setAvatar(DEFAULT_AVATAR); // 设置默认头像
        user.setNickname(registerDTO.getUsername());
        user.setRole("user");

        // 5. 保存到数据库
        boolean success = this.save(user);
        if (!success) {
            throw new BusinessException(ErrorCode.OPERATION_FAILED);
        }

        // 6. 生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole());
        String token = JwtUtil.genToken(claims);

        // 7. 转换为VO并返回
        UserLoginVO loginVO = new UserLoginVO();
        BeanUtils.copyProperties(user, loginVO);
        loginVO.setToken(token);  // 设置JWT令牌
        
        log.info("用户注册成功，username: {}, email: {}", user.getUsername(), user.getEmail());
        return loginVO;
    }

    @Override
    public UserLoginVO login(UserLoginDTO loginDTO) {
        // 1. 构建查询条件：邮箱或用户名匹配
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(loginDTO.getEmail()), User::getEmail, loginDTO.getEmail())
              .or()
              .eq(StringUtils.isNotBlank(loginDTO.getUsername()), User::getUsername, loginDTO.getUsername());
        
        User user = this.getOne(wrapper);
        
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 2. 校验用户状态
        if (user.getStatus() == 1) {
            throw new BusinessException(ErrorCode.USER_DISABLED);
        }

        // 3. 校验密码
        if (!user.getPassword().equals(loginDTO.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }

        // 4. 生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole());
        String token = JwtUtil.genToken(claims);

        // 5. 转换为VO对象
        UserLoginVO loginVO = new UserLoginVO();
        BeanUtils.copyProperties(user, loginVO);
        loginVO.setToken(token);  // 设置JWT令牌

        log.info("用户登录成功，username: {}, email: {}", user.getUsername(), user.getEmail());
        return loginVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserLoginVO updateUser(UserUpdateDTO updateDTO) {
        // 1. 查询用户是否存在
        User user = this.getById(updateDTO.getUserId());
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 2. 如果要更新邮箱，检查新邮箱是否已被使用
        if (StringUtils.isNotBlank(updateDTO.getEmail()) && !updateDTO.getEmail().equals(user.getEmail())) {
            checkEmailUnique(updateDTO.getEmail());
        }

        // 3. 如果要更新用户名，检查新用户名是否已被使用
        if (StringUtils.isNotBlank(updateDTO.getUsername()) && !updateDTO.getUsername().equals(user.getUsername())) {
            checkUsernameUnique(updateDTO.getUsername());
        }

        // 4. 更新用户信息
        User updateUser = new User();
        BeanUtils.copyProperties(updateDTO, updateUser);

        // 5. 执行更新
        boolean success = this.updateById(updateUser);
        if (!success) {
            throw new BusinessException(ErrorCode.UPDATE_FAILED);
        }

        // 6. 查询更新后的完整用户信息
        User updatedUser = this.getById(updateDTO.getUserId());
        UserLoginVO userVO = new UserLoginVO();
        BeanUtils.copyProperties(updatedUser, userVO);

        log.info("用户信息更新成功，userId: {}", updatedUser.getUserId());
        return userVO;
    }

    @Override
    public UserInfoVO getCurrentUserInfo() {
        // 1. 获取当前登录用户ID
        Long userId = UserContext.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        // 2. 查询用户信息
        User user = this.getById(userId.intValue());
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }


        // 3. 转换为VO对象
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);
        userInfoVO.setPassword(user.getPassword());

        return userInfoVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserAvatar(String avatarUrl) {
        // 1. 获取当前登录用户ID
        Long userId = UserContext.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        // 2. 更新用户头像
        User updateUser = new User();
        updateUser.setUserId(userId.intValue());
        updateUser.setAvatar(avatarUrl);

        boolean success = this.updateById(updateUser);
        if (!success) {
            throw new BusinessException(ErrorCode.UPDATE_FAILED);
        }

        log.info("用户头像更新成功，userId: {}", userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(PasswordUpdateDTO passwordUpdateDTO) {
        // 1. 获取当前用户
        Long userId = UserContext.getCurrentUserId();
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 2. 验证原密码
        if (!user.getPassword().equals(passwordUpdateDTO.getOldPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }

        // 3. 验证新密码与确认密码是否一致
        if (!passwordUpdateDTO.getNewPassword().equals(passwordUpdateDTO.getConfirmPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "新密码与确认密码不一致");
        }

        // 4. 更新密码
        User updateUser = new User();
        updateUser.setUserId(userId.intValue());
        updateUser.setPassword(passwordUpdateDTO.getNewPassword());

        boolean success = this.updateById(updateUser);
        if (!success) {
            throw new BusinessException(ErrorCode.UPDATE_FAILED);
        }

        log.info("用户密码更新成功，userId: {}", userId);
    }

    @Override
    public String uploadAvatar(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请选择要上传的文件");
        }

        try {
            // 1. 生成文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = "avatar/" + System.currentTimeMillis() + fileExtension;

            // 2. 上传文件到七牛云
            DefaultPutRet putRet = qiniuUtil.upload(file.getInputStream(), fileName);
            if (putRet == null) {
                throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
            }

            // 3. 获取文件访问URL
            String avatarUrl = qiniuUtil.getFile(putRet.key);
            if (!StringUtils.isNotBlank(avatarUrl)) {
                throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
            }

//            // 4. 更新用户头像URL
//            Long userId = UserContext.getCurrentUserId();
//            User updateUser = new User();
//            updateUser.setUserId(userId.intValue());
//            updateUser.setAvatar(avatarUrl);
//
//            boolean success = this.updateById(updateUser);
//            if (!success) {
//                throw new BusinessException(ErrorCode.UPDATE_FAILED);
//            }
//
//            log.info("用户头像更新成功，userId: {}, avatarUrl: {}", userId, avatarUrl);
            return avatarUrl;
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    /**
     * 用户名唯一性校验
     */
    private void checkUsernameUnique(String username) {
        if (StringUtils.isNotBlank(username)) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getUsername, username);
            if (this.baseMapper.exists(wrapper)) {
                throw new BusinessException(ErrorCode.USERNAME_EXISTS);
            }
        }
    }

    /**
     * 邮箱唯一性校验
     */
    private void checkEmailUnique(String email) {
        if (StringUtils.isNotBlank(email)) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getEmail, email);
            if (this.baseMapper.exists(wrapper)) {
                throw new BusinessException(ErrorCode.EMAIL_EXISTS);
            }
        }
    }
//------------------------------------用户分页----------------------------------------
    @Override
    public IPage<UserInfoVO> pageQueryUsers(UserPageQueryDTO queryDTO) {
        // 创建分页对象
        Page<User> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 构建查询条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(queryDTO.getUsername()), User::getUsername, queryDTO.getUsername())
                .like(StringUtils.isNotBlank(queryDTO.getEmail()), User::getEmail, queryDTO.getEmail())
                .like(StringUtils.isNotBlank(queryDTO.getPhone()), User::getPhone, queryDTO.getPhone())
                .eq(queryDTO.getStatus() != null, User::getStatus, queryDTO.getStatus())
                .orderBy(true, "asc".equalsIgnoreCase(queryDTO.getSortOrder()),
                        "user_id".equals(queryDTO.getSortField()) ? User::getUserId :
                        "username".equals(queryDTO.getSortField()) ? User::getUsername :
                        "create_time".equals(queryDTO.getSortField()) ? User::getCreateTime :
                        User::getUserId);

        // 执行分页查询
        IPage<User> userPage = this.page(page, wrapper);

        // 转换为视图对象
        return userPage.convert(this::convertToVO);
    }

    private UserInfoVO convertToVO(User user) {
        UserInfoVO vo = new UserInfoVO();
        BeanUtils.copyProperties(user, vo);
        // 设置状态描述
        String StatusDESC=UserStatus.fromCode(user.getStatus()).getDesc();
        vo.setStatusDESC(StatusDESC);
        return vo;
    }

    @Override
    public void sendLoginCode(EmailDTO emaildto) {
        String email = emaildto.getEmail();
        // 1. 检查邮箱是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);
        User user = this.getOne(wrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 2. 检查用户状态
        if (user.getStatus() == 1) {
            throw new BusinessException(ErrorCode.USER_DISABLED);
        }

        // 3. 发送验证码
        emailService.sendVerificationCode(email);
        log.info("发送登录验证码到邮箱：{}", email);
    }

    @Override
    public UserLoginVO loginByEmail(EmailLoginDTO emailLoginDTO) {
        // 1. 验证验证码
        if (!emailService.verifyCode(emailLoginDTO.getEmail(), emailLoginDTO.getVerificationCode())) {
            throw new BusinessException(ErrorCode.INVALID_CODE);
        }

        // 2. 查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, emailLoginDTO.getEmail());
        User user = this.getOne(wrapper);
        
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 3. 校验用户状态
        if (user.getStatus() == 1) {
            throw new BusinessException(ErrorCode.USER_DISABLED);
        }

        // 4. 生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole());
        String token = JwtUtil.genToken(claims);

        // 5. 转换为VO对象
        UserLoginVO loginVO = new UserLoginVO();
        BeanUtils.copyProperties(user, loginVO);
        loginVO.setToken(token);

        log.info("用户邮箱验证码登录成功，email: {}", user.getEmail());
        return loginVO;
    }
}




