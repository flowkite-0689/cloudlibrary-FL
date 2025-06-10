package com.cloudlibrary_api.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.cloudlibrary_api.common.enums.RecordStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("record")
public class Record {
    @TableId(type = IdType.AUTO)
    private Integer recordId; // 借阅记录id

    @TableField("record_borrowtime")
    private LocalDateTime borrowTime; // 图书借阅时间

    @TableField("record_returntime")
    private LocalDateTime returnTime; // 图书归还时间（可为空）

    @TableField("book_id")
    private Integer bookId; // 图书id

    @TableField("user_id")
    private Integer userId; // 用户id

    // 关联图书信息（非数据库字段）
    @TableField(exist = false)
    private Book book;

    // 关联用户信息（非数据库字段）
    @TableField(exist = false)
    private User user;

    @TableField("record_status")
    private RecordStatusEnum status; // 借阅状态枚举

    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted; // 逻辑删除标志

    // 添加查询关联字段（非表字段）
    @TableField(exist = false)
    private String bookName;        // 对应查询结果中的book_name

    @TableField(exist = false)
    private String bookIsbn;        // 对应查询结果中的book_isbn

    @TableField(exist = false)
    private String borrowerName;   // 对应查询结果中的borrower_name
}