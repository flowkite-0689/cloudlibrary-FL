package com.cloudlibrary_api.interceptor;

import com.cloudlibrary_api.common.exception.BusinessException;
import com.cloudlibrary_api.common.exception.ErrorCode;
import com.cloudlibrary_api.common.utils.JwtUtil;
import com.cloudlibrary_api.common.utils.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JwtAuthInterceptor implements HandlerInterceptor {

    private static final List<String> EXCLUDE_PATHS = Arrays.asList(
            "/api/users/login",
            "/api/users/add",
            "/api/users/register",
            "/api-docs",
            "/swagger-ui/**",
            "/error"
    );


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestPath = request.getRequestURI();

        // 检查是否在排除路径中
        for (String path : EXCLUDE_PATHS) {
            if (requestPath.startsWith(path)) {
                return true; // 直接放行
            }
        }


        // 获取请求头中的token
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        try {
            // 去掉可能存在的Bearer前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // 解析token
            Map<String, Object> claims = JwtUtil.parseToken(token);
            if (claims == null || claims.isEmpty()) {
                throw new BusinessException(ErrorCode.INVALID_TOKEN);
            }

            // 获取用户ID并存入上下文
            Object userIdObj = claims.get("userId");
            if (userIdObj == null) {
                throw new BusinessException(ErrorCode.MISSING_USER_INFO);
            }

            Long userId = Long.valueOf(userIdObj.toString());
            UserContext.setCurrentUserId(userId);

            return true;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.TOKEN_VERIFICATION_FAIL);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 清理ThreadLocal
        UserContext.remove();
    }
} 