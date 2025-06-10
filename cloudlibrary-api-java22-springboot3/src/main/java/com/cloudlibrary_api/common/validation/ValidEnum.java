// 文件路径：src/main/java/com/cloudlibrary_api/common/validation/ValidEnum.java
package com.cloudlibrary_api.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
public @interface ValidEnum {
    // 枚举类 (必须指定)
    Class<? extends Enum<?>> enumClass();

    // 转换方法名称（默认使用 valueOf）
    String method() default "valueOf";

    // 错误提示信息
    String message() default "无效的枚举值";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}