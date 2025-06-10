package com.cloudlibrary_api.service.impl;

import com.cloudlibrary_api.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final StringRedisTemplate redisTemplate;
    private static final String CODE_PREFIX = "email:code:";
    private static final long CODE_EXPIRE_MINUTES = 5;

    @Override
    public String sendVerificationCode(String to) {
        // 生成6位随机验证码
        String code = generateVerificationCode();
        
        // 发送邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2634317128@qq.com"); // 需要替换为实际的QQ邮箱
        message.setTo(to);
        message.setSubject("云图书馆 - 验证码");
        message.setText("您的验证码是：" + code + "，有效期5分钟。请勿泄露给他人。");
        
        mailSender.send(message);

        // 将验证码保存到Redis，设置5分钟过期
        redisTemplate.opsForValue().set(CODE_PREFIX + to, code, CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);
        
        return code;
    }

    @Override
    public boolean verifyCode(String email, String code) {
        String savedCode = redisTemplate.opsForValue().get(CODE_PREFIX + email);
        if (savedCode != null && savedCode.equals(code)) {
            // 验证成功后删除验证码
            redisTemplate.delete(CODE_PREFIX + email);
            return true;
        }
        return false;
    }

    private String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
} 