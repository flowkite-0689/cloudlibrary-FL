package com.cloudlibrary_api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UserPageQueryDTO {
    @Min(value = 1, message = "页码最小为1")
    private Integer pageNum = 1;          // 页码，默认为1

    @Min(value = 1, message = "每页数量最小为1")
    @Max(value = 100, message = "每页数量最大为100")
    private Integer pageSize = 100;

    private String username;
    private String email;
    private String phone;
    private Integer status;

    // 排序参数
    private String sortField = "user_id"; // 排序字段，默认按ID
    private String sortOrder = "asc";
}
