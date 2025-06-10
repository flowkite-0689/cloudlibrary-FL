
package com.cloudlibrary_api.common.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum BookStatusEnum {
    AVAILABLE(0, "可借阅"),
    BORROWED(1, "已借出"),
    MAINTENANCE(2, "维护中");

    private static final Map<String, BookStatusEnum> DESC_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(BookStatusEnum::getDesc, Function.identity()));

    private static final Map<Integer, BookStatusEnum> CODE_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(BookStatusEnum::getCode, Function.identity()));
    @EnumValue
    private final int code;
    private final String desc;

    BookStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举实例（用于数据库值转换）
     */
    public static BookStatusEnum getByCode(int code) {
        for (BookStatusEnum status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的状态码: " + code);
    }
    // 新增方法：校验状态是否合法
    public static boolean isValid(int code) {
        return code >= 0 && code < values().length;
    }
    // ------------------------- 多场景转换方法 -------------------------
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static BookStatusEnum from(Object input) {
        if (input instanceof Integer) {
            return fromCode((Integer) input);
        } else if (input instanceof String) {
            String str = (String) input;
            try {
                // 尝试按code转换
                return fromCode(Integer.parseInt(str));
            } catch (NumberFormatException e) {
                // 按desc转换
                return fromDesc(str);
            }
        }
        throw new IllegalArgumentException("Unsupported input type: " + input.getClass());
    }

    public static BookStatusEnum fromCode(int code) {
        BookStatusEnum status = CODE_MAP.get(code);
        if (status == null) {
            throw new IllegalArgumentException("无效状态码: " + code);
        }
        return status;
    }

    // 根据描述转换
    public static BookStatusEnum fromDesc(String desc) {
        return Arrays.stream(values())
                .filter(e -> e.getDesc().equals(desc))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("无效状态描述: " + desc));
    }
    // ------------------------- 校验方法 -------------------------
    public static boolean isValidCode(int code) {
        return CODE_MAP.containsKey(code);
    }

    public static boolean isValidDesc(String desc) {
        return DESC_MAP.containsKey(desc);
    }

}