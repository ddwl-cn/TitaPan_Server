package com.ncwu.titapan.controller;

import com.ncwu.titapan.constant.Constant;
import com.ncwu.titapan.constant.Message;
import com.ncwu.titapan.mapper.FileChunkMapper;
import com.ncwu.titapan.mapper.FileMapper;
import com.ncwu.titapan.mapper.PublicFileMapper;
import com.ncwu.titapan.pojo.*;
import com.ncwu.titapan.service.UploadService;
import com.ncwu.titapan.utils.DateUtil;
import com.ncwu.titapan.utils.FileUtil;
import com.ncwu.titapan.utils.PreviewImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/2/13 13:46
 */
@RestController
@RequestMapping("/public")
public class PublicFileController {
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private FileChunkMapper fileChunkMapper;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private PublicFileMapper publicFileMapper;

    // 前端请求偏移和页容量 返回对应数据
    @RequestMapping("/getPublicFileList")
    public ResultMessage<Map<String, Object>> getPublicFileList(HttpServletRequest request,
                                                HttpServletResponse response,
                                                int index, int count){
        if(ObjectUtils.isEmpty(index) || ObjectUtils.isEmpty(count))
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);

        PublicFile[] publicFiles = publicFileMapper.getPublicFileList((index-1)*count, count);

        int totalPages = (int) Math.ceil(((double)publicFileMapper.getPublicFileCount() / (double)count));

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("publicFileList", publicFiles);
        resultMap.put("totalPages", totalPages);
        resultMap.put("currentPage", index);
        resultMap.put("showNumber", count);

        return new ResultMessage<>(Message.SUCCESS, Message.getPublicFileListSuccess, resultMap);
    }

    @RequestMapping("/commonUpload")
    public ResultMessage<String> commonUpload(HttpServletRequest request, FileChunk fileChunk) {
        User user = (User) request.getSession().getAttribute(Constant.user);

        try {
            if (fileChunk.getTotal() == 1) {
                uploadService.commonUploadFile(user, null, fileChunk);
                return new ResultMessage<>(Message.SUCCESS, Message.uploadComplete, null);
            }
            FileChunk fChunk = fileChunkMapper.getFileChunkByMD5(fileChunk.getMd5_val());
            if (fChunk == null) {
                uploadService.saveChunk(fileChunk);
            }
            int total = fileChunkMapper.getFileChunkNumber(fileChunk.getId());
            if (total == fileChunk.getTotal()) {
                uploadService.mergeFileChunk(user, null, fileChunk);
                return new ResultMessage<>(Message.SUCCESS, Message.uploadComplete, null);
            }
            return new ResultMessage<>(Message.SUCCESS, Message.uploadChunkComplete, null);
        } catch (Exception e) {
            // 处理可能出现的异常情况
            return new ResultMessage<>(Message.ERROR, e.getMessage(), null);
        }
    }


    @RequestMapping("/quickUpload")
    public ResultMessage<String> quickUpload(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  FileChunk fileChunk){
        System.out.println(fileChunk);
        try {
            CustomFile cFile = fileMapper.getFileInfoByMD5(fileChunk.getId());
            if(cFile != null) {
                // 有这个文件 但是并不是公共文件 需要添加文件为公共文件
                if (!cFile.isPublic_file()) {
                    cFile.setPublic_file(true);
                    PublicFile publicFile = new PublicFile();
                    publicFile.setFid(cFile.getFid());
                    publicFile.setF_name(fileChunk.getOriginal_file_name());
                    publicFile.setN_name(fileChunk.getN_name());
                    publicFile.setF_size(cFile.getF_size());
                    publicFile.setF_description(fileChunk.getF_description());
                    publicFile.setUpload_date(DateUtil.getFormatDate());

                    File tFile = new File(Constant.sys_preview_path + cFile.getF_name());
                    fileChunk.getMPreview().transferTo(tFile);
                    String preview_url = PreviewImageUtil.get_preview_pic_url(tFile, true);
                    tFile.delete();
                    if (cFile.getPreview_url() != null) {
                        // 删除先前的预览图片 由管理员上传的替代
                        String path = Constant.sys_preview_path + FileUtil.getFileNameFromPath(cFile.getPreview_url());
                        new File(path).delete();
                    }
                    cFile.setPreview_url(preview_url);
                    fileMapper.updateFile(cFile);

                    publicFile.setPreview_url(preview_url);
                    publicFileMapper.insertPublicFile(publicFile);
                }
                else{
                    // 有这个文件且是公共文件说明已经上传过相同文件
                    return new ResultMessage<>(Message.SUCCESS, Message.fileNameRepetitive, null);
                }
            }
            else{
                return new ResultMessage<>(Message.SUCCESS, Message.uploadChunkComplete, null);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResultMessage<>(Message.SUCCESS, Message.uploadComplete, null);
    }


    @RequestMapping("/checkFile")
    public ResultMessage<String> checkPublicFile(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  FileChunk fileChunk){
        CustomFile cFile = fileMapper.getFileInfoByMD5(fileChunk.getId());
        if(cFile != null) {
            return new ResultMessage<>(Message.SUCCESS, Message.quickUpload, null);
        }

        FileChunk fChunk = fileChunkMapper.getFileChunkByMD5(fileChunk.getMd5_val());
        if(fChunk != null){
            return new ResultMessage<>(Message.SUCCESS, Message.quickUpload, null);
        }

        return new ResultMessage<>(Message.SUCCESS, Message.commonUpload, null);
    }



}
