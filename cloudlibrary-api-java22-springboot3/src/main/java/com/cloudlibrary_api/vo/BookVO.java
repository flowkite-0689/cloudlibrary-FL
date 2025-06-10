package com.cloudlibrary_api.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookVO {
    private Integer bookId;
    private String bookName;
    private String isbn;
    private String press;
    private String author;
//    private Integer pagination;
    private BigDecimal price;
    private String status; // 状态代码
    private String statusDesc; // 状态描述
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}