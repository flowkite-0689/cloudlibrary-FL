package com.cloudlibrary_api.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.cloudlibrary_api.dto.RecordPageQueryDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import com.cloudlibrary_api.pojo.Record;

@Mapper
public interface RecordMapper extends BaseMapper<Record> {

    @Insert("INSERT INTO record (book_id, user_id, record_borrowtime, record_returntime, record_status) " +
            "VALUES (#{bookId}, #{userId}, #{borrowTime}, #{remandTime}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "recordId")
    int insertRecord(Record record);

    // XML配置的分页查询方法
    IPage<Record> selectRecordPage(
            IPage<Record> page,
            @Param("query") RecordPageQueryDTO queryDTO
    );
}