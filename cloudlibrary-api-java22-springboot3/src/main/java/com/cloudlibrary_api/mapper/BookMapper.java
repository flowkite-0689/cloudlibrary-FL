package com.cloudlibrary_api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudlibrary_api.common.enums.BookStatusEnum;
import com.cloudlibrary_api.dto.BookPageQueryDTO;
import com.cloudlibrary_api.pojo.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;


@Mapper
public interface BookMapper extends BaseMapper<Book> {
    void updatePrice(Long bookId, BigDecimal price);

    // 继承BaseMapper后自动拥有insert方法

    // 自定义分页查询方法
    IPage<Book> selectBookPage(
            IPage<Book> page,
            @Param("query") BookPageQueryDTO queryDTO
    );

    //按isbn找书
    @Select("SELECT * FROM book WHERE book_isbn = #{isbn} AND is_deleted = 0")
    Book selectByIsbn(@Param("isbn") String isbn);

    //更新书本状态
    @Update("UPDATE book SET book_status = #{status} WHERE book_id = #{bookId}")
    int updateBookStatus(@Param("bookId") Integer bookId, @Param("status") BookStatusEnum status);

}