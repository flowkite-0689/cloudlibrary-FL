package com.cloudlibrary_api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudlibrary_api.common.exception.BusinessException;
import com.cloudlibrary_api.common.result.BookUpdateResult;
import com.cloudlibrary_api.dto.BookPageQueryDTO;
import com.cloudlibrary_api.dto.BookUpdateDTO;
import com.cloudlibrary_api.pojo.Book;
import com.cloudlibrary_api.vo.BookVO;

public interface BookService extends IService<Book> {
    /**
     * 新增图书（带ISBN校验）
     */
    boolean saveBook(Book book) throws BusinessException;
    //修改图书信息
    BookUpdateResult updateBook(BookUpdateDTO dto);

    int deleteBook(Long bookId);

    // -------------------------------- 分页查询图书 --------------------------------
    IPage<BookVO> pageQueryBooks(BookPageQueryDTO queryDTO);
}