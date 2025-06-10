package com.cloudlibrary_api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudlibrary_api.common.result.BookUpdateResult;
import com.cloudlibrary_api.common.utils.Result;
import com.cloudlibrary_api.dto.BookPageQueryDTO;
import com.cloudlibrary_api.dto.BookUpdateDTO;
import com.cloudlibrary_api.pojo.Book;
import com.cloudlibrary_api.service.BookService;
import com.cloudlibrary_api.vo.BookVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping
    @Operation(summary = "添加图书", description = "添加新图书")
    public Result<Void> addBook(@RequestBody @Valid Book book) {
        bookService.saveBook(book);
        return Result.success();
    }
    @PutMapping("/{bookId}")
    public Result<BookUpdateResult> updateBook(
            @PathVariable Long bookId,
            @Valid @RequestBody BookUpdateDTO dto
    ) {
        log.info("接收路径参数 bookId={}", bookId);
        dto.setBookId(bookId);
        log.info("DTO设置后 bookId={}", dto.getBookId());
        return Result.success(bookService.updateBook(dto));
    }
    @DeleteMapping("/{bookId}")
    @Operation(summary = "删除图书", description = "根据ID删除图书")
    public Result<Integer> deleteBook(@PathVariable Long bookId) {
        return Result.success(bookService.deleteBook(bookId));
    }
    @GetMapping("/page")
    public Result<IPage<BookVO>> pageBooks(@Validated BookPageQueryDTO queryDTO) {
        IPage<BookVO> page = bookService.pageQueryBooks(queryDTO);
        return Result.success(page);
    }
}