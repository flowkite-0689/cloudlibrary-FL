package com.cloudlibrary_api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudlibrary_api.common.utils.Result;
import com.cloudlibrary_api.dto.RecordAddDTO;
import com.cloudlibrary_api.dto.RecordPageQueryDTO;
import com.cloudlibrary_api.dto.RecordUpdateDTO;
import com.cloudlibrary_api.service.RecordService;
import com.cloudlibrary_api.vo.RecordVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/records")
@Tag(name = "借阅记录管理", description = "图书借阅记录相关操作")
public class RecordController {

    private final RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @PostMapping
    @Operation(
            summary = "添加借阅记录",
            description = "根据图书信息和借阅人信息创建新的借阅记录"
    )
    public Result<RecordVO> addRecord(@Valid @RequestBody RecordAddDTO recordAddDTO) {
        RecordVO record = recordService.addRecord(recordAddDTO);
        return Result.success(record);
    }

    @DeleteMapping("/{recordId}")
    @Operation(summary = "删除借阅记录", description = "根据ID删除借阅记录")
    public Result<Boolean> deleteRecord(@PathVariable Long recordId) {
        return Result.success(recordService.deleteRecord(recordId));
    }

    @PutMapping
    public Result<RecordVO> updateRecord(@RequestBody RecordUpdateDTO dto) {
        RecordVO recordVO = recordService.updateRecord(dto);
        return Result.success(recordVO);
    }

    /**
     * 分页查询借阅记录
     *
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    @GetMapping("/page")
    public Result<IPage<RecordVO>> pageQueryRecords(RecordPageQueryDTO queryDTO) {
        System.out.println(queryDTO.toString());
        IPage<RecordVO> page = recordService.pageQueryRecords(queryDTO);
        return Result.success(page);
    }
}