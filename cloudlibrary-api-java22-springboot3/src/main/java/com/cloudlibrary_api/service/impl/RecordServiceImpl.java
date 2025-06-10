package com.cloudlibrary_api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudlibrary_api.common.enums.BookStatusEnum;
import com.cloudlibrary_api.common.exception.ErrorCode;
import com.cloudlibrary_api.common.enums.RecordStatusEnum;
import com.cloudlibrary_api.common.exception.BusinessException;
import com.cloudlibrary_api.dto.RecordAddDTO;
import com.cloudlibrary_api.dto.RecordPageQueryDTO;
import com.cloudlibrary_api.dto.RecordUpdateDTO;
import com.cloudlibrary_api.mapper.BookMapper;
import com.cloudlibrary_api.mapper.RecordMapper;
import com.cloudlibrary_api.mapper.UserMapper;
import com.cloudlibrary_api.pojo.Book;
import com.cloudlibrary_api.pojo.Record;
import com.cloudlibrary_api.pojo.User;
import com.cloudlibrary_api.service.RecordService;
import com.cloudlibrary_api.vo.RecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    private final RecordMapper recordMapper;
    private final BookMapper bookMapper;
    @Autowired
    private final UserMapper userMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public RecordServiceImpl(RecordMapper recordMapper,
                             BookMapper bookMapper,
                             UserMapper userMapper) {
        this.recordMapper = recordMapper;
        this.bookMapper = bookMapper;
        this.userMapper = userMapper;
    }

    // -------------------------------- 新增借阅记录核心逻辑 --------------------------------
    @Override
    @Transactional
    public RecordVO addRecord(RecordAddDTO recordAddDTO) {
        // 1. 验证图书是否存在
        Book book = bookMapper.selectByIsbn(recordAddDTO.getIsbn());
        if (book == null) {
            throw new BusinessException(ErrorCode.BOOK_NOT_FOUND);
        }

        // 确保 ISBN 正确设置
        book.setIsbn(recordAddDTO.getIsbn());

        // 2. 验证借阅人是否存在
        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(User::getUsername,recordAddDTO.getBorrower());
        User user = userMapper.selectOne(userQueryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 3. 验证图书是否可借
        if (book.getStatus() != BookStatusEnum.AVAILABLE) {
            throw new BusinessException(
                    book.getStatus() == BookStatusEnum.BORROWED ?
                            ErrorCode.BOOK_ALREADY_BORROWED :
                            ErrorCode.BOOK_NOT_AVAILABLE
            );
        }

        // 4. 创建借阅记录
        Record record = new Record();
        record.setBookId(book.getBookId());
        record.setUserId(user.getUserId());

        // 解析时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        record.setBorrowTime(LocalDateTime.parse(recordAddDTO.getBorrowTime(), formatter));

        // 处理归还时间
        if (StringUtils.isNotBlank(recordAddDTO.getReturnTime())) {
            record.setReturnTime(LocalDateTime.parse(recordAddDTO.getReturnTime(), formatter));
            record.setStatus(RecordStatusEnum.RETURNED);
        } else {
            record.setStatus(RecordStatusEnum.BORROWED);
        }

        // 5. 保存借阅记录
        int insertResult = recordMapper.insert(record);
        if (insertResult <= 0) {
            throw new BusinessException(ErrorCode.RECORD_CREATE_FAILED);
        }

        // 6. 更新图书状态
        BookStatusEnum newStatus = (record.getStatus() == RecordStatusEnum.RETURNED) ?
                BookStatusEnum.AVAILABLE : BookStatusEnum.BORROWED;

        int updateResult = bookMapper.updateBookStatus(book.getBookId(), newStatus);
        if (updateResult <= 0) {
            throw new BusinessException(ErrorCode.UPDATE_BOOK_STATUS_FAILED);
        }

        // 7. 返回响应对象
        return convertToVO(record, book, user);
    }

    // -------------------------------- 删除借阅记录核心逻辑 --------------------------------
    @Transactional
    @Override
    public boolean deleteRecord(Long recordId) {
        // 使用新添加的 getRecordBy 方法获取记录
        Record record = getRecordBy(recordId);

        // 2. 状态检查：不能删除已归还的记录
        if (record.getStatus() == RecordStatusEnum.RETURNED) {
            throw new BusinessException(ErrorCode.RECORD_ALREADY_RETURNED);
        }

        // 3. 恢复书籍状态
        if (record.getStatus() == RecordStatusEnum.BORROWED ||
                record.getStatus() == RecordStatusEnum.OVERDUE) {

            Book book = bookMapper.selectById(record.getBookId());
            if (book != null) {
                book.setStatus(BookStatusEnum.AVAILABLE);
                int updateResult = bookMapper.updateById(book);
                if (updateResult <= 0) {
                    log.error("书籍状态更新失败: bookId={}", book.getBookId());
                    throw new BusinessException(ErrorCode.UPDATE_BOOK_STATUS_FAILED);
                }
            }
        }

        // 4. 执行逻辑删除
        int deleteResult = recordMapper.deleteById(recordId);
        if (deleteResult <= 0) {
            throw new BusinessException(ErrorCode.RECORD_DELETE_FAILED);
        }

        log.info("借阅记录已删除: recordId={}", recordId);
        return true;
    }


    // -------------------------------- 修改借阅记录核心逻辑 --------------------------------
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RecordVO updateRecord(RecordUpdateDTO dto) {
        // 1. 参数验证并获取相关实体
        UpdateValidationData data = validateAndPrepareData(dto);
        Record existingRecord = data.existingRecord();
        Book originalBook = data.originalBook();
        User originalUser = data.originalUser();

        // 2. 检查新书籍是否存在（如果需要更新书籍）
        Book newBook = null;
        if (StringUtils.isNotBlank(dto.getIsbn()) && !dto.getIsbn().equals(originalBook.getIsbn())) {
            newBook = bookMapper.selectByIsbn(dto.getIsbn());
            if (newBook == null) {
                throw new BusinessException(ErrorCode.BOOK_NOT_FOUND);
            }
        }

        // 3. 检查新用户是否存在（如果需要更新借阅人）
        User newUser = null;
        if (StringUtils.isNotBlank(dto.getBorrower()) && !dto.getBorrower().equals(originalUser.getUsername())) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUsername, dto.getBorrower());
            newUser = userMapper.selectOne(queryWrapper);
            if (newUser == null) {
                throw new BusinessException(ErrorCode.USER_NOT_FOUND);
            }
        }

        // 4. 处理书籍状态变化（如果换了书）
        handleBookStatusChanges(existingRecord, originalBook, newBook);

        // 5. 构建更新后的借阅记录
        Record updatedRecord = buildUpdatedRecord(dto, existingRecord,
                newBook != null ? newBook : originalBook,
                newUser != null ? newUser : originalUser);

        // 6. 执行更新
        updatedRecord.setStatus(RecordStatusEnum.from(dto.getStatusDesc()));
        int updateResult = recordMapper.updateById(updatedRecord);
        if (updateResult <= 0) {
            throw new BusinessException(ErrorCode.RECORD_UPDATE_FAILED);

        }


        // 7. 处理归还操作（如果有）
        handleReturnAction(updatedRecord);

        log.info("借阅记录更新成功: recordId={}", dto.getRecordId());

        // 8. 返回更新后的视图对象
        return convertToVO(updatedRecord,
                newBook != null ? newBook : originalBook,
                newUser != null ? newUser : originalUser);
    }
    // -------------------------------- 分页查询借阅记录 --------------------------------
    @Override
    public IPage<RecordVO> pageQueryRecords(RecordPageQueryDTO queryDTO) {


        // 创建分页对象
        Page<Record> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 执行XML配置的查询
        IPage<Record> recordPage = recordMapper.selectRecordPage(page, queryDTO);
        log.info("查询到 {} 条借阅记录", recordPage.getTotal());

        // 转换为视图对象
        return recordPage.convert(this::convertToVO);
    }

    // -------------------------------- 辅助方法 --------------------------------
    /**
     * 验证参数并准备数据
     */
    private UpdateValidationData validateAndPrepareData(RecordUpdateDTO dto) {
        // 验证记录是否存在
        Record existingRecord = getRecordBy(dto.getRecordId());

        // 获取原始书籍和用户
        Book originalBook = bookMapper.selectById(existingRecord.getBookId());
        if (originalBook == null) {
            throw new BusinessException(ErrorCode.BOOK_NOT_FOUND);
        }

        User originalUser = userMapper.selectById(existingRecord.getUserId());
        if (originalUser == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        return new UpdateValidationData(existingRecord, originalBook, originalUser);
    }



    /**
     * 处理书籍状态变化
     */
    private void handleBookStatusChanges(Record record, Book originalBook, Book newBook) {
        // 如果换了新书
        if (newBook != null) {
            // 将原书籍恢复为可借状态
            if (originalBook.getStatus() == BookStatusEnum.BORROWED) {
                bookMapper.updateBookStatus(originalBook.getBookId(), BookStatusEnum.AVAILABLE);
                log.info("原书籍状态已恢复可借: bookId={}", originalBook.getBookId());
            }

            // 检查新书是否可借
            if (newBook.getStatus() != BookStatusEnum.AVAILABLE) {
                throw new BusinessException(newBook.getStatus() == BookStatusEnum.BORROWED ?
                        ErrorCode.BOOK_ALREADY_BORROWED : ErrorCode.BOOK_NOT_AVAILABLE);
            }

            // 更新新书状态为借出
            bookMapper.updateBookStatus(newBook.getBookId(), BookStatusEnum.BORROWED);
            log.info("新书籍状态已更新为借出: bookId={}", newBook.getBookId());
        }
    }
    /**
     * 构建更新后的借阅记录对象
     */
    private Record buildUpdatedRecord(RecordUpdateDTO dto, Record existingRecord, Book book, User user) {
        Record updatedRecord = new Record();
        updatedRecord.setRecordId(Math.toIntExact(dto.getRecordId()));

        // 设置书籍和用户
        updatedRecord.setBookId(book.getBookId());
        updatedRecord.setUserId(user.getUserId());

        // 设置借阅时间
        if (StringUtils.isNotBlank(dto.getBorrowTime())) {
            updatedRecord.setBorrowTime(LocalDateTime.parse(dto.getBorrowTime(), formatter));
        } else {
            updatedRecord.setBorrowTime(existingRecord.getBorrowTime());
        }

        // 处理归还时间
        if (StringUtils.isNotBlank(dto.getReturnTime())) {
            updatedRecord.setReturnTime(LocalDateTime.parse(dto.getReturnTime(), formatter));
            updatedRecord.setStatus(RecordStatusEnum.RETURNED);
        } else {
            updatedRecord.setReturnTime(existingRecord.getReturnTime());
            updatedRecord.setStatus(existingRecord.getStatus());
        }

        return updatedRecord;
    }

    /**
     * 处理归还操作
     */
    private void handleReturnAction(Record record) {
        if (record.getStatus() == RecordStatusEnum.RETURNED) {
            Book book = bookMapper.selectById(record.getBookId());
            if (book != null) {
                bookMapper.updateBookStatus(book.getBookId(), BookStatusEnum.AVAILABLE);
                log.info("书籍已归还: bookId={}", book.getBookId());
            }
        }
    }



    /**
     * 获取借阅记录详情（含验证）
     */
    private Record getRecordBy(Long recordId) {
        Record record = this.getById(recordId);

        if (record == null || record.getIsDeleted() == 1) {
            throw new BusinessException(ErrorCode.RECORD_NOT_FOUND);
        }
        return record;
    }

    /**
     * 转换为视图对象
     */
    private RecordVO convertToVO(Record record, Book book, User user) {
        RecordVO vo = new RecordVO();
        vo.setRecordId(record.getRecordId());
        vo.setBookName(book.getBookName());
        vo.setBookIsbn(book.getIsbn());
        vo.setBorrowerName(user.getUsername());
        vo.setBorrowTime(record.getBorrowTime());
        vo.setReturnTime(record.getReturnTime());
        vo.setStatus(record.getStatus().name());
        vo.setStatusDesc(record.getStatus().getDesc());
        return vo;
    }
    // 将Record实体转换为VO
    private RecordVO convertToVO(Record record) {
        RecordVO vo = new RecordVO();
        BeanUtils.copyProperties(record, vo);

        // 处理状态显示
       vo.setStatus(record.getStatus().name());
       vo.setStatusDesc(record.getStatus().getDesc());

        return vo;
    }

    // -------------------------------- 内部数据载体 --------------------------------
    private record UpdateValidationData(Record existingRecord,
                                        Book originalBook,
                                        User originalUser) {}
}