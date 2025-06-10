package com.cloudlibrary_api.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//统一响应结果
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result<T> {
    private Integer code;//业务状态码
    private String message;//提示信息
    private T data;//响应数据

    //快速返回操作成功响应结果(带响应数据)
    public static <E> Result<E> success(E data) {
        return new Result<>(200, "操作成功", data);
    }

    //快速返回操作成功响应结果
    public static Result success() {
        return new Result(200, "操作成功", null);
    }

    //返回错误响应结果（带错误信息）
    public static Result error(String message) {
        return new Result(500, message, null);
    }

    //返回错误响应结果（带错误码和错误信息）
    public static Result error(int code, String message) {
        return new Result(code, message, null);
    }
}
