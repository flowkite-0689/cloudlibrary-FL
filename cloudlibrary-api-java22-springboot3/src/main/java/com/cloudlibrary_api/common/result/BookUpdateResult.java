// 文件路径: src/main/java/com/cloudlibrary_api/common/result/BookUpdateResult.java

package com.cloudlibrary_api.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookUpdateResult {
    private Long bookId;    // 图书ID
    private String isbn;    // ISBN编号
}