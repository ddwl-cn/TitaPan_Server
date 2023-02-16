package com.ncwu.titapan.controller;

import com.ncwu.titapan.constant.Constant;
import com.ncwu.titapan.constant.Message;
import com.ncwu.titapan.mapper.FileChunkMapper;
import com.ncwu.titapan.mapper.FileMapper;
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

    @RequestMapping("/getPublicFileList")
    public ResultMessage<Map<String, Object>> getPublicFileList(HttpServletRequest request,
                                                HttpServletResponse response,
                                                int index, int count){
        if(ObjectUtils.isEmpty(index) || ObjectUtils.isEmpty(count))
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);

        CustomFile[] publicFiles = fileMapper.getPublicFileList((index-1)*count, count);

        int totalPages = (int) Math.ceil(((double)fileMapper.getPublicFileCount() / (double)count));

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("publicFileList", publicFiles);
        resultMap.put("totalPages", totalPages);
        resultMap.put("currentPage", index);
        resultMap.put("showNumber", count);

        return new ResultMessage<>(Message.SUCCESS, Message.getPublicFileListSuccess, resultMap);
    }

    @RequestMapping("/commonUpload")
    public ResultMessage<String> commonUpload(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  FileChunk fileChunk){
        System.out.println(fileChunk);
        User user = (User)request.getSession().getAttribute(Constant.user);
        if(fileChunk.getTotal() == 1){
            uploadService.commonUploadFile(user, null, fileChunk);
        }
        else{
            FileChunk fChunk = fileChunkMapper.getFileChunkByMD5(fileChunk.getMd5_val());
            if(fChunk == null){
                uploadService.saveChunk(fileChunk);
            }
            int total = fileChunkMapper.getFileChunkNumber(fileChunk.getId());
            if(total == fileChunk.getTotal()){
                uploadService.mergeFileChunk(user, null, fileChunk);
                return new ResultMessage<>(Message.SUCCESS, Message.uploadComplete, null);
            }
            return new ResultMessage<>(Message.SUCCESS, Message.uploadChunkComplete, null);
        }

        return new ResultMessage<>(Message.SUCCESS, Message.uploadComplete, null);
    }

    @RequestMapping("/quickUpload")
    public ResultMessage<String> quickUpload(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  FileChunk fileChunk){
        System.out.println(fileChunk);
        try {
            CustomFile cFile = fileMapper.getFileInfoByMD5(fileChunk.getId());
            if(cFile != null) {
                if (!cFile.isPublic_file()) {
                    cFile.setPublic_file(true);
                    cFile.setN_name(fileChunk.getOriginal_file_name());
                    cFile.setF_description(fileChunk.getF_description());
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
