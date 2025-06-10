package com.cloudlibrary_api.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.cloudlibrary_api.common.enums.BookStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("book")  // MyBatis-Plus 表名注解
public class Book {
    @TableId(type = IdType.AUTO)  // MyBatis-Plus 主键策略
    private Integer bookId;

    // 分类关联（需要 Classify 实体类）
    // @TableField("classify_id")
    // private Integer classifyId;
    @TableField
    private String bookName;

    @TableField(value = "book_isbn", condition = SqlCondition.EQUAL)  // 精确匹配
    private String isbn;

    @TableField("book_press")
    private String press;

    @TableField("book_author")
    private String author;
    @TableField("book_pagination")
    private Integer pagination;

    @TableField("book_price")
    private BigDecimal price;  // 金额使用 BigDecimal


    @TableField("book_status")
    private BookStatusEnum status = BookStatusEnum.AVAILABLE;

    // MyBatis-Plus 自动填充（需配置处理器）
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic  // MyBatis-Plus 逻辑删除注解
    @TableField("is_deleted")
    private Integer isDeleted;

}
