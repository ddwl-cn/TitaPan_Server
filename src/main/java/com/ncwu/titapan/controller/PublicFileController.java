package com.ncwu.titapan.controller;

import com.ncwu.titapan.constant.Constant;
import com.ncwu.titapan.constant.Message;
import com.ncwu.titapan.mapper.*;
import com.ncwu.titapan.pojo.*;
import com.ncwu.titapan.service.UploadService;
import com.ncwu.titapan.utils.DateUtil;
import com.ncwu.titapan.utils.FileUtil;
import com.ncwu.titapan.utils.PreviewImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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
    @Autowired
    private UserFileListMapper userFileListMapper;

    // 前端请求偏移和页容量 返回对应数据
    @RequestMapping("/getPublicFileList")
    public ResultMessage<Map<String, Object>> getPublicFileList(HttpServletRequest request,
                                                                HttpServletResponse response,
                                                                int index, int count, String search, int orderBy, String col){
        if(ObjectUtils.isEmpty(index) || ObjectUtils.isEmpty(count))
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);

        if(ObjectUtils.isEmpty(search)) search = "";

        // 0 默认
        User user = (User) request.getSession().getAttribute(Constant.user);

        PublicFile[] publicFiles = publicFileMapper.getPublicFileList((index-1)*count, count, search, user.getType(), orderBy, col);

        int totalPages = (int) Math.ceil(((double)publicFileMapper.getPublicFileCount(search) / (double)count));

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
                if(!uploadService.commonUploadFile(user, null, fileChunk, false, null)){
                    return new ResultMessage<>(Message.ERROR, Message.unknownError, null);
                }
                return new ResultMessage<>(Message.SUCCESS, Message.uploadComplete, null);
            }
            FileChunk fChunk = fileChunkMapper.getFileChunkByMD5(fileChunk.getMd5_val());
            if (fChunk == null) {
                uploadService.saveChunk(fileChunk);
            }
            int total = fileChunkMapper.getFileChunkNumber(fileChunk.getId());
            if (total == fileChunk.getTotal()) {
                uploadService.mergeFileChunk(user, null, fileChunk, false, null);
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


    @RequestMapping("/updatePublicFileInfo")
    public ResultMessage<String> updatePublicFileInfo(HttpServletRequest request,
                                                      HttpServletResponse response,
                                                      PublicFile rowData){
        try {
            if (rowData.getPreview_image()!=null) {
                File tFile = new File(Constant.sys_preview_path + PreviewImageUtil.createRandomName(32) + ".jpg");
                rowData.getPreview_image().transferTo(tFile);
                String preview_url = PreviewImageUtil.get_preview_pic_url(tFile, true);
                tFile.delete();
                rowData.setPreview_url(preview_url);
            }
            rowData.setUpload_date(DateUtil.getFormatDate());
            publicFileMapper.updatePublicFileInfo(rowData);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResultMessage<>(Message.SUCCESS, Message.updatePublicFileInfoSuccess, null);
    }

    @RequestMapping("/deletePublicFile")
    public ResultMessage<String> deletePublicFile(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  int fid){
        publicFileMapper.deletePublicFile(fid);
        return new ResultMessage<>(Message.SUCCESS, Message.deletePublicFileSuccess, null);
    }

    @RequestMapping("/savePublicFile")
    public ResultMessage<String> savePublicFile(HttpServletRequest request,
                                                HttpServletResponse response,
                                                String savePath,
                                                int fid){

        if(savePath == null || savePath.isBlank() || savePath.isEmpty())
            return new ResultMessage<String>(Message.ERROR, Message.dataFormatError, null);

        PublicFile publicFileInfo = publicFileMapper.getPublicFileInfoByFid(fid);
        User user = (User)request.getSession().getAttribute(Constant.user);
        UserFileList userFileList = userFileListMapper.getUserFileInfo(user.getUid(), publicFileInfo.getF_name(), savePath);
        if(userFileList!=null)
            return new ResultMessage<>(Message.WARNING, Message.fileNameRepetitive, null);

        CustomFile cFile = fileMapper.getFileInfoByFid(publicFileInfo.getFid());
        if(cFile == null)
            return new ResultMessage<>(Message.ERROR, null, null);

        userFileList = new UserFileList();
        userFileList.setUid(user.getUid());
        userFileList.setStorage_path(savePath);
        userFileList.setFolder(false);
        userFileList.setFid(publicFileInfo.getFid());
        userFileList.setF_name(publicFileInfo.getF_name());
        userFileList.setUpload_date(publicFileInfo.getUpload_date());
        userFileList.setF_size(publicFileInfo.getF_size());
        userFileList.setPreview_url(publicFileInfo.getPreview_url());

        userFileListMapper.insertFile(userFileList);

        // 下载次数加一
        publicFileInfo.setHot(publicFileInfo.getHot()+1);
        publicFileMapper.updatePublicFileInfo(publicFileInfo);

        return new ResultMessage<>(Message.SUCCESS, null, null);
    }

    @RequestMapping("/getPublicFileInfo")
    public ResultMessage<PublicFile> getPublicFileInfo(HttpServletRequest request,
                                                       HttpServletResponse response,
                                                       int fid){
        PublicFile publicFile = publicFileMapper.getPublicFileInfoByFid(fid);
        if(publicFile == null) return new ResultMessage<>(Message.ERROR, Message.unknownError, null);
        return new ResultMessage<>(Message.SUCCESS,Message.getPublicFileSuccess, publicFile);
    }


}
