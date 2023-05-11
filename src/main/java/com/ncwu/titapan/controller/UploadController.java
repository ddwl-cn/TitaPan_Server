package com.ncwu.titapan.controller;

import com.ncwu.titapan.constant.Constant;
import com.ncwu.titapan.constant.Message;
import com.ncwu.titapan.mapper.FileChunkMapper;
import com.ncwu.titapan.mapper.FileMapper;
import com.ncwu.titapan.mapper.UserFileListMapper;
import com.ncwu.titapan.pojo.*;
import com.ncwu.titapan.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO 用户上传文件 应该属于UserBehaviorController 但是内容较多单拎出来写了
 *
 * @author ddwl.
 * @date 2023/1/5 22:44
 */
@RestController
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    private UploadService uploadService;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private UserFileListMapper userFileListMapper;
    @Autowired
    private FileChunkMapper fileChunkMapper;

    // 预检验文件是否已经存在 不携带文件本身
    @RequestMapping("/checkFile")
    public ResultMessage<String> checkFile(HttpServletRequest request,
                            HttpServletResponse response,
                            FileChunk fileChunk) {
        // 获得用户实体
        User user = (User) request.getSession().getAttribute(Constant.user);
        // 用户当前路径
        String userPath = (String) request.getSession().getAttribute(Constant.userPath);
        int total = fileChunk.getTotal();
        if(total <= 0 ){
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);
        }
        else {
            // 检验文件是否存在 或用户文件夹下是否有重名
            int state = uploadService.checkFile(user, fileChunk, userPath);
            if (state == 0) {
                if(total == 1)
                    return new ResultMessage<>(Message.SUCCESS, Message.commonUpload, null);
                else{
                    // 块是否存在
                    if(uploadService.checkFileChunk(fileChunk.getMd5_val())){
                        return new ResultMessage<>(Message.SUCCESS, Message.quickUpload, null);
                    }
                    else{
                        return new ResultMessage<>(Message.SUCCESS, Message.commonUpload, null);
                    }
                }
            }
            else if (state == 1)
                return new ResultMessage<>(Message.SUCCESS, Message.quickUpload, null);
            else
                return new ResultMessage<>(Message.WARNING, Message.fileNameRepetitive, null);
        }
    }


    /**
     * TODO 文件普通上传(携带文件实体)
     *
     * @param request   request
     * @param response  response
     * @param fileChunk fileChunk
     * @return lang.String
     * @Author ddwl.
     * @Date 2023/1/7 22:25
     **/
    @RequestMapping("/commonUpload")
    public ResultMessage<String> commonUpload(HttpServletRequest request,
                               HttpServletResponse response,
                               FileChunk fileChunk) {
        // 数据无效返回错误信息
        if (fileChunk == null || fileChunk.getMFile() == null || fileChunk.getMFile().getSize() <= 0 || fileChunk.getTotal() <= 0)
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);
        // 获得用户实体
        User user = (User) request.getSession().getAttribute(Constant.user);
        // 用户当前路径
        String userPath = (String) request.getSession().getAttribute(Constant.userPath);
        UserFileList userFileList = userFileListMapper.getUserFileInfo(user.getUid(), fileChunk.getOriginal_file_name(), userPath);
        if(userFileList != null) return new ResultMessage<>(Message.WARNING, Message.fileNameRepetitive, null);
        // 是一个文件
        if (fileChunk.getTotal() == 1) {
            // 检验文件是否存在
            CustomFile cFile = fileMapper.getFileInfoByMD5(fileChunk.getId());
            if (cFile == null) {
                uploadService.commonUploadFile(user, userPath, fileChunk, false, null);
            } else {
                uploadService.quickUploadFile(user, userPath, fileChunk.getId(), fileChunk.getOriginal_file_name());
            }
        } else {
            // 检验整个文件是否已经存在
            CustomFile cFile = fileMapper.getFileInfoByMD5(fileChunk.getId());
            if (cFile == null) {
                FileChunk fChunk = fileChunkMapper.getFileChunkByMD5(fileChunk.getMd5_val());
                // 分块不存在 暂时保存分块
                if (fChunk == null) {
                    uploadService.saveChunk(fileChunk);
                }
                // 如果数据库中已经有所有的分块
                if (fileChunkMapper.getFileChunkNumber(fileChunk.getId()) == fileChunk.getTotal()) {
                    // 合并分块
                    uploadService.mergeFileChunk(user, userPath, fileChunk, false, null);
                    return new ResultMessage<>(Message.SUCCESS, Message.uploadComplete, null);
                }
                return new ResultMessage<>(Message.SUCCESS, Message.uploadChunkComplete, null);
            }
            // 整个文件已经存在了
            else {
                uploadService.quickUploadFile(user, userPath, fileChunk.getId(), fileChunk.getOriginal_file_name());
            }
        }
        return new ResultMessage<>(Message.SUCCESS, Message.uploadComplete, null);
    }

    @RequestMapping("/quickUpload")
    public ResultMessage<String> quickUpload(HttpServletRequest request,
                              HttpServletResponse response,
                              FileChunk fileChunk) {
        if (fileChunk == null || fileChunk.getMFile() != null || fileChunk.getTotal() <= 0)
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);
        // 获得用户实体
        User user = (User) request.getSession().getAttribute(Constant.user);
        // 用户当前路径
        String userPath = (String) request.getSession().getAttribute(Constant.userPath);

        // 不分块
        if (fileChunk.getTotal() == 1) {
            CustomFile cFile = fileMapper.getFileInfoByMD5(fileChunk.getId());
            // 快传但是数据库中此时却没有文件
            if (cFile == null) return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);
            uploadService.quickUploadFile(user, userPath, fileChunk.getId(), fileChunk.getOriginal_file_name());
        }
        // 分块
        else {
            CustomFile cFile = fileMapper.getFileInfoByMD5(fileChunk.getId());
            // 已有完整文件
            if (cFile != null) {
                UserFileList userFileList =
                        userFileListMapper.getUserFileInfo(user.getUid(),
                                fileChunk.getOriginal_file_name(),
                                userPath);
                // 用户路径下有重名文件
                if (userFileList != null) return new ResultMessage<>(Message.WARNING, Message.fileNameRepetitive, null);

                uploadService.quickUploadFile(user, userPath, fileChunk.getId(), fileChunk.getOriginal_file_name());
                return new ResultMessage<>(Message.SUCCESS, Message.uploadComplete, null);
            } else {
                // 如果数据库中已经有所有的分块

                if (fileChunkMapper.getFileChunkNumber(fileChunk.getId()) == fileChunk.getTotal()) {
                    // 合并分块
                    uploadService.mergeFileChunk(user, userPath, fileChunk, false, null);
                    return new ResultMessage<>(Message.SUCCESS, Message.uploadComplete, null);
                }
                return new ResultMessage<>(Message.SUCCESS, Message.uploadChunkComplete, null);
            }
        }
        return new ResultMessage<>(Message.SUCCESS, Message.uploadComplete, null);
    }

    @RequestMapping("/doUpload")
    public ResultMessage<String> doUpload(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FileChunk fileChunk) {
        if (fileChunk == null || fileChunk.getTotal() <= 0)
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);
        // 获得用户实体
        User user = (User) request.getSession().getAttribute(Constant.user);
        // 用户当前路径
        String userPath = (String) request.getSession().getAttribute(Constant.userPath);
        UserFileList userFileList = userFileListMapper.getUserFileInfo(user.getUid(), fileChunk.getOriginal_file_name(), userPath);
        if(userFileList != null) return new ResultMessage<>(Message.WARNING, Message.fileNameRepetitive, null);

        // 检验文件是否存在
        CustomFile cFile = fileMapper.getFileInfoByMD5(fileChunk.getId());
        if (cFile == null) {
            // 整个文件是一个分块
            if (fileChunk.getTotal() == 1 && fileChunk.getMFile() != null && fileChunk.getMFile().getSize() > 0) {
                uploadService.commonUploadFile(user, userPath, fileChunk, false, null);
            }
            // 多个分块的文件
            else {
                FileChunk fChunk = fileChunkMapper.getFileChunkByMD5(fileChunk.getMd5_val());
                // 分块不存在 暂时保存分块
                if (fChunk == null) {
                    uploadService.saveChunk(fileChunk);
                }
                // 如果数据库中已经有所有的分块
                if (fileChunkMapper.getFileChunkNumber(fileChunk.getId()) == fileChunk.getTotal()) {
                    // 合并分块
                    uploadService.mergeFileChunk(user, userPath, fileChunk, false, null);
                    return new ResultMessage<>(Message.SUCCESS, Message.uploadComplete, null);
                }
                return new ResultMessage<>(Message.SUCCESS, Message.uploadChunkComplete, null);
            }
        }
        // 整个文件已经存在了
        else {
            uploadService.quickUploadFile(user, userPath, fileChunk.getId(), fileChunk.getOriginal_file_name());
        }
        return new ResultMessage<>(Message.SUCCESS, Message.uploadComplete, null);
    }
}