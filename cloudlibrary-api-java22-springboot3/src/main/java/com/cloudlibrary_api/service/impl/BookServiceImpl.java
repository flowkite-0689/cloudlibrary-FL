package com.cloudlibrary_api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudlibrary_api.common.enums.BookStatusEnum;
import com.cloudlibrary_api.common.exception.BusinessException;
import com.cloudlibrary_api.common.exception.ErrorCode;
import com.cloudlibrary_api.common.result.BookUpdateResult;
import com.cloudlibrary_api.dto.BookPageQueryDTO;
import com.cloudlibrary_api.dto.BookUpdateDTO;
import com.cloudlibrary_api.mapper.BookMapper;
import com.cloudlibrary_api.mapper.UserMapper;
import com.cloudlibrary_api.pojo.Book;
import com.cloudlibrary_api.service.BookService;

import com.cloudlibrary_api.vo.BookVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Autowired
    private final UserMapper userMapper;
    private final BookMapper bookMapper;
    // -------------------------------- 新增图书核心逻辑 --------------------------------
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBook(Book book) throws BusinessException {
        // 1. ISBN唯一性校验
        checkIsbnUnique(book.getIsbn());

        // 2. 设置默认状态
        book.setStatus(BookStatusEnum.getByCode(BookStatusEnum.AVAILABLE.getCode()));

        // 3. 保存到数据库（复用MyBatis-Plus的save方法）
        boolean success = this.save(book);

        if (success) {
            log.info("新增图书成功，ISBN: {}", book.getIsbn());
        }
        return success;
    }
    // -------------------------------- 删除图书核心逻辑 --------------------------------
    @Transactional(rollbackFor = Exception.class)
    public int deleteBook(Long bookId) {
        // 1. 校验图书是否存在
        Book book = this.getById(bookId);
        if (book == null || book.getIsDeleted() == 1) {
            throw new BusinessException(ErrorCode.BOOK_NOT_FOUND);
        }

        // 2. 校验业务规则（已借出的书不能删除）
        if (book.getStatus() == BookStatusEnum.BORROWED) {
            throw new BusinessException(ErrorCode.CANNOT_DELETE_BORROWED_BOOK);
        }

        // 3. 执行逻辑删除（实际是更新操作）
        return bookMapper.deleteById(bookId);
    }
    // -------------------------------- 修改图书核心逻辑 --------------------------------
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BookUpdateResult updateBook(BookUpdateDTO dto) {
        // 参数验证
        validateUpdateParams(dto);
        // 构建更新对象
        Book book = new Book();
        book.setBookId(Math.toIntExact(dto.getBookId()));
        book.setBookName(dto.getBookName());  // 设置新书名
        book.setAuthor(dto.getAuthor());      // 设置新作者

        // 动态更新非空字段
        boolean success = this.updateById(book);
        // 参数转换
        BookStatusEnum status = BookStatusEnum.fromDesc(dto.getStatus());

        // 更新价格
        if (dto.getPrice() != null) {
            this.updatePrice(dto.getBookId(), dto.getPrice());
        }

        // 更新状态
        if (status != null) {
            this.updateStatus(dto.getBookId(), status.getCode());
        }

        // 返回结果
        return new BookUpdateResult(
                dto.getBookId(),
                this.baseMapper.selectById(dto.getBookId()).getIsbn()
        );
    }
    private void updatePrice(Long bookId, BigDecimal price) {
        this.baseMapper.updatePrice(bookId, price);
    }

    @Transactional
    public void updateStatus(Long bookId, Integer newStatus) {
        // 查询旧状态
        Book book = this.getById(bookId);

        // 执行更新
        book.setStatus(BookStatusEnum.from(newStatus));
        this.updateById(book);
    }
    // -------------------------------- 分页查询图书核心逻辑 --------------------------------
    @Override
    public IPage<BookVO> pageQueryBooks(BookPageQueryDTO queryDTO) {
        // 创建分页对象
        Page<Book> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 执行分页查询
        IPage<Book> bookPage = baseMapper.selectBookPage(page, queryDTO);

        // 转换为视图对象
        return bookPage.convert(this::convertToVO);
    }

    private BookVO convertToVO(Book book) {
        BookVO vo = new BookVO();
        BeanUtils.copyProperties(book, vo);
        // 添加状态描述
        vo.setStatusDesc(book.getStatus().getDesc());
        return vo;
    }

    // -------------------------------- 参数验证方法 --------------------------------
    private void validateUpdateParams(BookUpdateDTO dto) {
        if (dto.getPrice() != null && dto.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(ErrorCode.INVALID_PRICE);
        }
        if (dto.getStatus() != null) {
            if (!BookStatusEnum.isValidDesc(dto.getStatus())) {
                throw new BusinessException(ErrorCode.INVALID_STATUS);
            }
        }
        // 新增书名和作者校验
        if (dto.getBookName() != null && dto.getBookName().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_BOOK_NAME);
        }
        if (dto.getAuthor() != null && dto.getAuthor().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_AUTHOR);
        }
    }

    // -------------------------------- 私有方法封装 --------------------------------
    /**
     * ISBN唯一性校验（复用校验逻辑）
     */
    private void checkIsbnUnique(String isbn) throws BusinessException {
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Book::getIsbn, isbn);
        if (this.baseMapper.exists(wrapper)) {
            throw new BusinessException(ErrorCode.ISBN_EXISTS, "ISBN: " + isbn);
        }
    }

}