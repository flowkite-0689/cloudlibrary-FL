package com.cloudlibrary_api.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

// BorrowRecord.java（借阅记录实体）
@Data
@TableName("record")
public class BorrowRecord {
    @TableId(value = "record_id", type = IdType.AUTO)
    private Integer recordId;

    @TableField("record_bookname")
    private String bookName;

    @TableField("record_bookisbn")
    private String bookIsbn;

    @TableField("record_borrower")
    private String borrower;

    @TableField("record_borrowtime")
    private LocalDateTime borrowTime;

    @TableField("record_remandtime")
    private LocalDateTime remandTime;

    @TableField("book_id")
    private Integer bookId;

    @TableField("user_id")
    private Integer userId;
}