package com.cloudlibrary_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.qq.com");
        mailSender.setPort(587);
        mailSender.setUsername("2634317128@qq.com");
        mailSender.setPassword("knaikuxuysahdjbh");
        mailSender.setDefaultEncoding("UTF-8");  // 添加编码设置

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // 明确协议版本
        props.put("mail.debug", "true"); // 开启调试

        return mailSender;
    }
}