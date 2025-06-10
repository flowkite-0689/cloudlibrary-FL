package com.cloudlibrary_api.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Getter
public enum RecordStatusEnum implements IEnum<Integer> {
    BORROWED(1, "借阅中"),
    RETURNED(2, "已归还"),
    OVERDUE(3, "已逾期");

    private final int code;
    private final String desc;

    RecordStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    // 实现 IEnum 接口的方法
    @Override
    public Integer getValue() {
        return this.code;
    }

    // JSON序列化
    @JsonValue
    public int getCode() {
        return this.code;
    }

    /**
     * 根据 code 获取枚举实例
     */
    public static RecordStatusEnum fromCode(int code) {
        Optional<RecordStatusEnum> result = Arrays.stream(values())
                .filter(status -> status.code == code)
                .findFirst();

        return result.orElseThrow(() ->
                new IllegalArgumentException("无效的借阅状态编码: " + code));
    }

    /**
     * 根据描述转换（模糊匹配、大小写不敏感）
     */
    public static RecordStatusEnum fromDesc(String desc) {
        if (desc == null || desc.trim().isEmpty()) {
            return null;
        }

        String normalizedDesc = desc.trim().toLowerCase();

        Optional<RecordStatusEnum> result = Arrays.stream(values())
                .filter(status ->
                        status.desc.toLowerCase().contains(normalizedDesc) ||
                                normalizedDesc.contains(status.desc.toLowerCase()))
                .findFirst();

        return result.orElseThrow(() ->
                new IllegalArgumentException("无效状态描述: " + desc));
    }

    /**
     * 通用转换方法（支持多种输入类型）
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static RecordStatusEnum from(Object input) {
        if (input == null) {
            return null;
        }

        try {
            if (input instanceof Integer) {
                return fromCode((Integer) input);
            } else if (input instanceof Number) {
                return fromCode(((Number) input).intValue());
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
        } catch (Exception e) {
            log.error("RecordStatusEnum转换失败: input={}, error={}", input, e.getMessage());
            throw new IllegalArgumentException("无法转换到借阅状态: " + input);
        }

        throw new IllegalArgumentException("不支持的输入类型: " + input.getClass().getSimpleName());
    }

    @Override
    public String toString() {
        return "RecordStatusEnum{code=" + code + ", desc='" + desc + "'}";
    }
}