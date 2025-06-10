package com.cloudlibrary_api.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static final String KEY = "itheima";
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000; // 24小时过期

    // 生成token
    public static String genToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        return genToken(claims);
    }

    // 生成token（使用Map参数）
    public static String genToken(Map<String, Object> claims) {
        return JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .sign(Algorithm.HMAC256(KEY));
    }

    // 解析token
    public static Map<String, Object> parseToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(KEY))
                    .build()
                    .verify(token)
                    .getClaim("claims")
                    .asMap();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    // 从token中获取用户ID
    public static Long getUserId(String token) {
        Map<String, Object> claims = parseToken(token);
        if (claims != null && claims.get("userId") != null) {
            return Long.valueOf(claims.get("userId").toString());
        }
        return null;
    }

    // 从token中获取用户名
    public static String getUsername(String token) {
        Map<String, Object> claims = parseToken(token);
        if (claims != null && claims.get("username") != null) {
            return claims.get("username").toString();
        }
        return null;
    }

    // 验证token是否有效
    public static boolean validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(KEY))
                    .build()
                    .verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}
