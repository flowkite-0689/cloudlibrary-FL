package com.cloudlibrary_api.dto;

import com.cloudlibrary_api.common.enums.RecordStatusEnum;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class RecordPageQueryDTO {

    @Min(value = 1, message = "页码最小为1")
    private Integer pageNum = 1;          // 页码，默认为1

    @Min(value = 1, message = "每页数量最小为1")
    @Max(value = 100, message = "每页数量最大为100")
    private Integer pageSize = 100;        // 每页大小，默认为10

    // 查询参数
    private String bookName;               // 书籍名称（模糊查询）
    private String borrowerName;           // 借阅人名称（模糊查询）
    private Integer status;                 // 状态值（整数形式）
    private String isbn;                   // ISBN号（精确查询）

    // 排序参数
    private String sortField = "recordId"; // 排序字段，默认按ID
    private String sortOrder = "DESC";      // 排序方向，默认降序
}