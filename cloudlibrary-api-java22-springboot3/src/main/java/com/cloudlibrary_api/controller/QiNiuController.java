package com.cloudlibrary_api.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.cloudlibrary_api.common.exception.ErrorCode;
import com.cloudlibrary_api.common.utils.Result;
import com.cloudlibrary_api.common.utils.QiniuUtil;
import com.qiniu.storage.model.DefaultPutRet;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * 七牛云工具类
 * @author linfen
 * @since 2020-04-8
 */
@Slf4j
@RestController
@RequestMapping("/qiniu")
public class QiNiuController {

    @Autowired
    private QiniuUtil qiniuUtil;

    /**
     * 七牛云图片文件上传
     *
     * @param file 文件
     * @return Result<Map<String, Object>>
     */
    @ResponseBody
    @PostMapping("/upload/images")
    public Result<Map<String, Object>> uploadPic(MultipartFile[] file) {
        if (file == null || file.length <= 0) {
            return new Result<>(ErrorCode.FILE_NULL_ERROR.getCode(), ErrorCode.FILE_NULL_ERROR.getMessage(), null);
        }
        if (file[0].isEmpty()) {
            return new Result<>(ErrorCode.FILE_NULL_ERROR.getCode(), ErrorCode.FILE_NULL_ERROR.getMessage(), null);
        }

        try {
            BufferedInputStream fileInputStream = new BufferedInputStream(file[0].getInputStream());
            String originalFilename = file[0].getOriginalFilename();
            String fileExtend = originalFilename.substring(originalFilename.lastIndexOf("."));
            String yyyyMMddHHmmss = DateUtil.format(new Date(), "yyyyMMddHHmmss");
            String fileKey = UUID.randomUUID().toString().replace("-", "") + "-" + yyyyMMddHHmmss + fileExtend;

            Map<String, Object> map = new HashMap<>();
            DefaultPutRet uploadInfo = qiniuUtil.upload(fileInputStream, "images/" + fileKey);
            
            map.put("url", qiniuUtil.getFileDomain() + "/" + uploadInfo.key);
            map.put("fileName", uploadInfo.key);
            map.put("originName", originalFilename);
            map.put("size", file[0].getSize());
            log.info("文件上传成功：{}", JSON.toJSONString(map));
            
            return new Result<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), map);
        } catch (Exception e) {
            log.error("文件上传失败：", e);
            return new Result<>(ErrorCode.FILE_UPLOAD_ERROR.getCode(), ErrorCode.FILE_UPLOAD_ERROR.getMessage(), null);
        }
    }

    /**
     * 七牛云视频文件上传
     *
     * @param file 文件
     * @return Result<Map<String, Object>>
     */
    @ResponseBody
    @PostMapping("/upload/video")
    public Result<Map<String, Object>> uploadVideo(MultipartFile[] file) {
        if (file == null || file.length <= 0) {
            return new Result<>(ErrorCode.FILE_NULL_ERROR.getCode(), ErrorCode.FILE_NULL_ERROR.getMessage(), null);
        }
        if (file[0].isEmpty()) {
            return new Result<>(ErrorCode.FILE_NULL_ERROR.getCode(), ErrorCode.FILE_NULL_ERROR.getMessage(), null);
        }

        try {
            BufferedInputStream fileInputStream = new BufferedInputStream(file[0].getInputStream());
            String originalFilename = file[0].getOriginalFilename();
            String fileExtend = originalFilename.substring(originalFilename.lastIndexOf("."));
            String yyyyMMddHHmmss = DateUtil.format(new Date(), "yyyyMMddHHmmss");
            String fileKey = UUID.randomUUID().toString().replace("-", "") + "-" + yyyyMMddHHmmss + fileExtend;

            Map<String, Object> map = new HashMap<>();
            DefaultPutRet uploadInfo = qiniuUtil.upload(fileInputStream, "video/" + fileKey);
            
            map.put("url", qiniuUtil.getFileDomain() + "/" + uploadInfo.key);
            map.put("fileName", uploadInfo.key);
            map.put("originName", originalFilename);
            map.put("size", file[0].getSize());
            log.info("视频上传成功：{}", JSON.toJSONString(map));
            
            return new Result<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), map);
        } catch (Exception e) {
            log.error("视频上传失败：", e);
            return new Result<>(ErrorCode.FILE_UPLOAD_ERROR.getCode(), ErrorCode.FILE_UPLOAD_ERROR.getMessage(), null);
        }
    }

    /**
     * 七牛云文件下载
     *
     * @param filename 文件名
     */
    @GetMapping("/file/{filename}")
    public void download(@PathVariable("filename") String filename, HttpServletResponse response) {
        if (filename == null || filename.isEmpty()) {
            return;
        }
        try {
            String privateFile = qiniuUtil.getFile(filename);
            log.info("文件下载地址：{}", privateFile);
            response.sendRedirect(privateFile);
        } catch (Exception e) {
            log.error("文件下载失败：", e);
        }
    }

    /**
     * 七牛云图片删除文件
     *
     * @param filename 文件名
     * @return Result<Integer>
     */
    @DeleteMapping("/delete/file/images/{filename}")
    public Result<Integer> deletePicFileAttraction(@PathVariable("filename") String filename) {
        if (filename == null || filename.isEmpty()) {
            return new Result<>(ErrorCode.FILE_NOT_FOUND.getCode(), ErrorCode.FILE_NOT_FOUND.getMessage(), null);
        }

        try {
            boolean result = qiniuUtil.delete("images/" + filename);
            if (result) {
                return new Result<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), 1);
            } else {
                return new Result<>(ErrorCode.FILE_DELETE_ERROR.getCode(), ErrorCode.FILE_DELETE_ERROR.getMessage(), null);
            }
        } catch (Exception e) {
            log.error("图片删除失败：", e);
            return new Result<>(ErrorCode.FILE_DELETE_ERROR.getCode(), ErrorCode.FILE_DELETE_ERROR.getMessage(), null);
        }
    }

    /**
     * 七牛云删除视频文件
     *
     * @param filename 文件名
     * @return Result<Integer>
     */
    @DeleteMapping("/delete/file/video/{filename}")
    public Result<Integer> deleteVideoFileAttraction(@PathVariable("filename") String filename) {
        if (filename == null || filename.isEmpty()) {
            return new Result<>(ErrorCode.FILE_NOT_FOUND.getCode(), ErrorCode.FILE_NOT_FOUND.getMessage(), null);
        }

        try {
            boolean result = qiniuUtil.delete("video/" + filename);
            if (result) {
                return new Result<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), 1);
            } else {
                return new Result<>(ErrorCode.FILE_DELETE_ERROR.getCode(), ErrorCode.FILE_DELETE_ERROR.getMessage(), null);
            }
        } catch (Exception e) {
            log.error("视频删除失败：", e);
            return new Result<>(ErrorCode.FILE_DELETE_ERROR.getCode(), ErrorCode.FILE_DELETE_ERROR.getMessage(), null);
        }
    }
    
}