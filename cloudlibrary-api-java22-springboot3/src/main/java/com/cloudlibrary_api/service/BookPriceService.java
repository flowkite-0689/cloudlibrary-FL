package com.cloudlibrary_api.service;

import com.cloudlibrary_api.common.exception.BusinessException;
import com.cloudlibrary_api.common.exception.ErrorCode;
import com.cloudlibrary_api.mapper.BookMapper;
import com.cloudlibrary_api.pojo.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BookPriceService {
    private final BookMapper bookMapper;

    @Transactional
    public void updatePrice(Long bookId, BigDecimal newPrice) {
        Book book = bookMapper.selectById(bookId);
        if (book == null) throw new BusinessException(ErrorCode.BOOK_NOT_FOUND);
        book.setPrice(newPrice);
        bookMapper.updateById(book);
    }
}
