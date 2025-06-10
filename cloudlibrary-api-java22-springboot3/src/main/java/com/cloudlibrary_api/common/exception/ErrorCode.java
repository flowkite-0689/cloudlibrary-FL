package com.cloudlibrary_api.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // ================= 成功状态码 =================
    SUCCESS(200, "成功", "操作已完成"),

    // ================= 客户端错误 (4xxxx) =================
    // 请求参数问题
    PARAMS_ERROR(40000, "请求参数错误", "请检查必填项或参数格式"),
    NULL_ERROR(40001, "请求数据为空", "数据库未查询到相关记录"),

    // 权限问题

    FORBIDDEN(40300, "无权限", "当前用户无此操作权限"),

    // 资源问题
    NOT_FOUND(40400, "资源不存在", "请求的URL或数据未找到"),
    BOOK_NOT_FOUND(40401, "图书不存在", "无法找到指定ISBN对应的图书"),
    USER_NOT_FOUND(40402, "用户不存在", "未找到该用户信息"),
    USER_DISABLED(40403, "用户已禁用", "该用户账号已被禁用"),
    PASSWORD_ERROR(40404, "密码错误", "登录密码不正确"),
    RECORD_NOT_FOUND(40403, "借阅记录不存在" , "未找到该借阅记录信息" ),

    // 文件操作相关错误码
    FILE_NULL_ERROR(40601, "文件为空", "上传的文件不能为空"),
    FILE_TYPE_ERROR(40602, "文件类型错误", "不支持的文件类型"),
    FILE_SIZE_ERROR(40603, "文件大小超限", "文件大小超过限制"),
    FILE_UPLOAD_ERROR(40604, "文件上传失败", "文件上传过程中发生错误"),
    FILE_DELETE_ERROR(40605, "文件删除失败", "文件删除过程中发生错误"),
    FILE_NOT_FOUND(40606, "文件不存在", "要操作的文件不存在"),
    FILE_DOWNLOAD_ERROR(40607, "文件下载失败", "文件下载过程中发生错误"),

    // 业务规则问题
    INVALID_PRICE(40002, "价格参数非法", "价格格式不正确或超出合理范围"),
    INVALID_STATUS(40003, "状态参数非法", "状态值不符合系统要求"),
    INVALID_BOOK_NAME(40004, "书名参数非法", "书名长度或格式不符合要求"),
    INVALID_AUTHOR(40005, "作者名参数非法", "作者名长度或格式不符合要求"),
    BOOK_ALREADY_BORROWED(40006, "图书已借出", "该图书已被他人借阅"),
    BOOK_NOT_AVAILABLE(40007, "图书不可借", "图书当前状态不支持借阅"),
    RECORD_ALREADY_RETURNED(40008, "已归还记录", "该借阅记录已完成归还"),
    UNRETURNED_RECORDS(40009, "有未归还记录", "用户存在未归还的图书"),
    ISBN_EXISTS(40010, "ISBN已存在", "系统中已存在相同的ISBN编号"),
    CANNOT_DELETE_BORROWED_BOOK(40011, "已借出的图书不可删除", "请先处理借阅记录后再删除图书"),
    RECORD_DELETE_FAILED(40412, "记录删除失败", "删除记录时发生错误"),
    USERNAME_EXISTS(40405, "用户名已存在", "该用户名已被其他用户使用"),
    EMAIL_EXISTS(40406, "邮箱已存在", "该邮箱已被其他用户使用"),

    // ========== 认证相关错误 41xxx ==========
    UNAUTHORIZED(40100, "未认证", "请提供有效的认证信息"),
    INVALID_TOKEN(40101, "无效令牌", "身份验证令牌无效或已过期"),
    MISSING_USER_INFO(40102, "缺失用户信息", "令牌中未包含必要的用户信息"),
    TOKEN_VERIFICATION_FAIL(40103, "令牌验证失败", "令牌验证过程中发生错误"),
    NOT_LOGIN(40104, "未登录", "用户尚未登录或会话已过期"),

    // ================= 服务端错误 (5xxxx) =================
    // 系统问题
    SYSTEM_ERROR(50000, "系统异常", "服务器内部错误，请联系管理员"),
    DATABASE_ERROR(50001, "数据库操作失败", "SQL执行异常，请检查日志"),

    // 第三方服务问题
    THIRD_PARTY_ERROR(50002, "第三方服务异常", "外部API调用失败"),

    // 业务操作问题
    UPDATE_FAILED(50003, "更新失败", "数据库更新操作未生效"),
    BORROW_ERROR(50004, "借阅失败", "图书借阅操作未完成"),
    RETURN_ERROR(50005, "归还失败", "图书归还操作未完成"),
    RECORD_CREATE_FAILED(50007, "借阅记录创建失败", "创建借阅记录时发生错误"),
    UPDATE_BOOK_STATUS_FAILED(50006, "图书状态更新失败" , "数据库更新操作未生效" ),
    RECORD_UPDATE_FAILED(50008,"借阅记录更新失败" , "更新借阅记录时发生错误"),

    // ================= 新增错误码 =================
    INVALID_CODE(1007, "验证码无效或已过期", "验证码无效或已过期"),
    OPERATION_FAILED(1008, "操作失败", "操作失败");
    private final int code;
    private final String message;
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
        this.description = "";
    }

    /**
     * 根据状态码获取枚举实例
     */
    public static ErrorCode fromCode(int code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.getCode() == code) {
                return errorCode;
            }
        }
        return SYSTEM_ERROR; // 默认返回系统错误
    }
}