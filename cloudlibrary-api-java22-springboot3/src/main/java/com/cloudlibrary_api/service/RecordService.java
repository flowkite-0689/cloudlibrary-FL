package com.cloudlibrary_api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudlibrary_api.dto.RecordAddDTO;
import com.cloudlibrary_api.dto.RecordPageQueryDTO;
import com.cloudlibrary_api.dto.RecordUpdateDTO;
import com.cloudlibrary_api.vo.RecordVO;
import org.springframework.transaction.annotation.Transactional;


public interface RecordService {
    //新增记录
    RecordVO addRecord(RecordAddDTO recordAddDTO);


    // -------------------------------- 删除借阅记录--------------------------------
    @Transactional
    boolean deleteRecord(Long recordId);

//    修改记录
    @Transactional
    RecordVO updateRecord(RecordUpdateDTO record);
    /**
     * 分页查询借阅记录
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<RecordVO> pageQueryRecords(RecordPageQueryDTO queryDTO);
}