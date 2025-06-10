package com.cloudlibrary_api.dto;

import com.cloudlibrary_api.common.validation.ISBN;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "RecordAddDTO", description = "添加借阅记录请求参数")
public class RecordAddDTO {

    @Schema(
            description = "图书名称",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Java编程思想"
    )
    @NotBlank(message = "图书名称不能为空")
    @Size(max = 50, message = "图书名称长度不能超过50个字符")
    private String bookName;

    @Schema(
            description = "ISBN编号",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "9787111213826"
    )
    @ISBN
    private String isbn;

    @Schema(
            description = "借阅人姓名",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "张三"
    )
    @NotBlank(message = "借阅人姓名不能为空")
    @Size(max = 20, message = "借阅人姓名长度不能超过20个字符")
    private String borrower;

    @Schema(
            description = "借阅时间",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "2023-08-15 10:30:00"
    )
    @NotBlank(message = "借阅时间不能为空")
    private String borrowTime;

    @Schema(
            description = "归还时间",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "2023-09-15 10:30:00"
    )
    private String returnTime;
}