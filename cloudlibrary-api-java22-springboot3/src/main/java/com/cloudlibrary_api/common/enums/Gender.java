package com.cloudlibrary_api.common.enums;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("MALE", "男"),
    FEMALE("FEMALE", "女"),
    UNKNOWN("UNKNOWN", "未知");

    private final String code;
    private final String desc;

    Gender(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static Gender fromCode(String code) {
        for (Gender gender : values()) {
            if (gender.code.equals(code)) {
                return gender;
            }
        }
        return UNKNOWN;
    }
}