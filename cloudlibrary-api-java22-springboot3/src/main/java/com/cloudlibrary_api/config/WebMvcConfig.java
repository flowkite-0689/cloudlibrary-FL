package com.cloudlibrary_api.config;

import com.cloudlibrary_api.interceptor.JwtAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册JWT认证拦截器
        registry.addInterceptor(new JwtAuthInterceptor())
                .addPathPatterns("/**")  // 拦截所有请求
                .excludePathPatterns(    // 排除不需要拦截的路径
                        "/api/user/login",
                        "/api/user/register",
                        "/error",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                );
    }
}
