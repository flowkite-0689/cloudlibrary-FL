package com.cloudlibrary_api.service;

public interface EmailService {
    /**
     * 发送验证码邮件
     * @param to 收件人邮箱
     * @return 生成的验证码
     */
    String sendVerificationCode(String to);

    /**
     * 验证验证码是否正确
     * @param email 邮箱
     * @param code 验证码
     * @return 是否正确
     */
    boolean verifyCode(String email, String code);
} 