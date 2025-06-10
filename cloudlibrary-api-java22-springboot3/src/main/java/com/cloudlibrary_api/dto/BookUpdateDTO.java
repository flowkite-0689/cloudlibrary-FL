package com.cloudlibrary_api.dto;

import com.cloudlibrary_api.common.enums.BookStatusEnum;
import com.cloudlibrary_api.common.validation.ValidEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class BookUpdateDTO {
    @NotNull(message = "图书ID不能为空")
    private Long bookId;

    @ValidEnum(
            enumClass = BookStatusEnum.class,
            method = "fromDesc",
            message = "状态必须是可借阅/已借出/维护中"
    )
    private String status;  // ✅ 使用String接收前端参数

    @DecimalMin(value = "0.01", message = "价格必须大于0")
    private BigDecimal price;
    @NotBlank(message = "书名不能为空")
    private String bookName;  // 新增字段

    @NotBlank(message = "作者不能为空")
    private String author;    // 新增字段
}