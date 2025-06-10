package com.cloudlibrary_api.config.interceptor;

import com.cloudlibrary_api.common.utils.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class UserContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 从请求头或会话中获取用户ID
        String userIdStr = request.getHeader("X-User-Id");
        if (userIdStr != null) {
            try {
                Long userId = Long.parseLong(userIdStr);
                UserContext.setCurrentUserId(userId);
            } catch (NumberFormatException e) {
                // 忽略非法的用户ID
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 清理ThreadLocal
        UserContext.remove();
    }
} 