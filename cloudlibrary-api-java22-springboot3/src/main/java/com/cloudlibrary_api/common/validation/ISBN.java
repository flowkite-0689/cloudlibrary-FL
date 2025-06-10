// common/validation/ISBN.java
package com.cloudlibrary_api.common.validation;

// ISBNValidator.java
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ISBNValidator.class) // 关联校验逻辑实现类
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ISBN {
    String message() default "ISBN格式无效";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}