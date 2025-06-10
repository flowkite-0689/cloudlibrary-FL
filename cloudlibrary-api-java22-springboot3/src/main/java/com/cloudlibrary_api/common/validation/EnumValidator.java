// 文件路径：src/main/java/com/cloudlibrary_api/common/validation/EnumValidator.java
package com.cloudlibrary_api.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {
    private Class<? extends Enum<?>> enumClass;
    private String methodName;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
        this.methodName = constraintAnnotation.method();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        try {
            // 通过反射调用枚举转换方法
            Method method = enumClass.getMethod(methodName, String.class);
            return method.invoke(null, value) != null;
        } catch (Exception e) {
            return false;
        }
    }
}