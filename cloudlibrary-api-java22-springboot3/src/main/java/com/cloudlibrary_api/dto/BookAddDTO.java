package com.cloudlibrary_api.dto;

import com.cloudlibrary_api.common.validation.ISBN;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

// BookAddDTO.java
@Data
public class BookAddDTO {
    @NotBlank(message = "书名不能为空")
    private String bookName;

    @ISBN // 自定义ISBN校验注解
    private String isbn;

    @DecimalMin(value = "0.0", message = "价格不能为负")
    private BigDecimal price;
}