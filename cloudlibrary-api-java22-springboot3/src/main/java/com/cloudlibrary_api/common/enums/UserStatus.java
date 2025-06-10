package com.cloudlibrary_api.common.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    ENABLED(0, "正常"),
    DISABLED(1, "禁用");

    private final int code;
    private final String desc;

    UserStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static UserStatus fromCode(int code) {
        for (UserStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return ENABLED;
    }
}