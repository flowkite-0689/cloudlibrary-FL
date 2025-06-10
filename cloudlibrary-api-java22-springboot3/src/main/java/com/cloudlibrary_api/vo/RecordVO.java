package com.cloudlibrary_api.vo;

import com.cloudlibrary_api.common.enums.RecordStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Schema(description = "借阅记录响应")
@AllArgsConstructor
@NoArgsConstructor
public class RecordVO {
    @Schema(description = "记录ID", example = "1001")
    private Integer recordId;

    @Schema(description = "图书名称", example = "Java编程思想")
    private String bookName;

    @Schema(description = "ISBN编号", example = "9787111213826")
    private String bookIsbn;

    @Schema(description = "借阅人姓名", example = "张三")
    private String borrowerName;

    @Schema(description = "借阅时间", example = "2023-08-15T10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime borrowTime;

    @Schema(description = "归还时间", example = "2023-09-15T10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime returnTime;

    @Schema(description = "借阅状态", example = "BORROWED")
    private String status;

    @Schema(description = "状态描述", example = "借阅中")
    private String statusDesc;

    // 设置状态信息方法
    public void setStatusInfo(RecordStatusEnum statusEnum) {
        this.status = statusEnum.name();
        this.statusDesc = statusEnum.getDesc();}
}