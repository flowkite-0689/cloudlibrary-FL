// common/validation/ISBNValidator.java
package com.cloudlibrary_api.common.validation;

// ISBNValidator.java
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
public class ISBNValidator implements ConstraintValidator<ISBN, String> {

    private static final String ISBN_REGEX =
            "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";

    @Override
    public void initialize(ISBN constraintAnnotation) {
        // 初始化逻辑（可选）
    }

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext context) {
        if (isbn == null) {
            return false;
        }
        return isbn.matches(ISBN_REGEX);
    }
}