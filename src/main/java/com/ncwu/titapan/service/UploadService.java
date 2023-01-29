package com.ncwu.titapan.service;

import com.ncwu.titapan.mapper.FileMapper;
import com.ncwu.titapan.pojo.FileChunk;
import com.ncwu.titapan.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/5 22:54
 */

public interface UploadService {

    int checkFile(User user, FileChunk fileChunk, String userPath);

    boolean checkFileChunk(String md5_val);

    // 用于上传单个完整文件
    void commonUploadFile(User user, String userPath, FileChunk fileChunk);
    // 用于上传某个文件中的某个文件块
    void quickUploadFile(User user, String userPath, String md5_val, String fileName);
    // 用于文件块合并后的相关操作
    void mergeFileChunk(User user, String userPath, FileChunk fileChunk);
    // 文件块上传过程中发现整个文件已经存在
    void mergeFileChunk(User user, String userPath, String md5_val, String fileName);

    void saveChunk(FileChunk fileChunk);

}
