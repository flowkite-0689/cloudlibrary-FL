package com.cloudlibrary_api.interceptor;

import com.cloudlibrary_api.common.exception.BusinessException;
import com.cloudlibrary_api.common.exception.ErrorCode;
import com.cloudlibrary_api.common.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class UserAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 从session中获取用户ID
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        
        // 如果用户未登录，抛出异常
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        
        // 将用户ID存储到ThreadLocal中
        ThreadLocalUtil.set("userId", userId);
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求结束后，清除ThreadLocal中的数据，防止内存泄漏
        ThreadLocalUtil.remove();
    }
} 