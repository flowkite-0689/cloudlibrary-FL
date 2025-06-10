package com.cloudlibrary_api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BookPageQueryDTO {
    @Min(value = 1, message = "页码最小为1")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页数量最小为1")
    @Max(value = 100, message = "每页数量最大为100")
    private Integer pageSize = 100;

    private String bookName;
    private String author;
    private String press;
    private String status;

    // 排序字段映射
    private String sortField = "book_id";
    private String sortOrder = "asc";
}