package com.cloudlibrary_api.common.utils;

/**
 * 用户上下文工具类，用于存储和获取当前登录用户信息
 */
public class UserContext {
    private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();

    public static void setCurrentUserId(Long userId) {
        userIdHolder.set(userId);
    }

    public static Long getCurrentUserId() {
        return userIdHolder.get();
    }

    public static void remove() {
        userIdHolder.remove();
    }
} 