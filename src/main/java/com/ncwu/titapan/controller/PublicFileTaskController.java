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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/5/10 19:12
 */

@RestController
@RequestMapping("/task")
public class PublicFileTaskController {
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
    @Autowired
    private PublicFileTaskMapper publicFileTaskMapper;


    @RequestMapping("/commonUpload")
    public ResultMessage<String> commonUpload(HttpServletRequest request,
                                              PublicFileTask publicFileTask) {

        FileChunk fileChunk = publicFileTask.getFileChunk();

        User user = (User) request.getSession().getAttribute(Constant.user);

        try {
            if (fileChunk.getTotal() == 1) {
                // 但是文件合并时应把publifile表的状态设置为1 代表尚未通过审核不能被查询
                if(!uploadService.commonUploadFile(user, null, fileChunk, true, publicFileTask)){
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
                // 但是文件合并时应把publifile表的状态设置为1 代表尚未通过审核不能被查询
                uploadService.mergeFileChunk(user, null, fileChunk, true, publicFileTask);

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
                                             PublicFileTask publicFileTask){
        FileChunk fileChunk = publicFileTask.getFileChunk();
        User user = (User) request.getSession().getAttribute(Constant.user);
        try {
            CustomFile cFile = fileMapper.getFileInfoByMD5(fileChunk.getId());
            if(cFile != null) {
                // 有这个文件 但是并不是公共文件 需要添加任务
                if (!cFile.isPublic_file()) {
                    cFile.setPublic_file(true);
                    // publicfile 中的状态标记为1 为审核状态
                    PublicFile publicFile = new PublicFile();
                    publicFile.setFid(cFile.getFid());
                    publicFile.setF_name(fileChunk.getOriginal_file_name());
                    publicFile.setN_name(fileChunk.getN_name());
                    publicFile.setF_size(cFile.getF_size());
                    publicFile.setF_description(fileChunk.getF_description());
                    publicFile.setUpload_date(DateUtil.getFormatDate());
                    publicFile.setState(true);

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

                    // 上传完成后生成 任务
                    publicFileTask.setUid(user.getUid());
                    publicFileTask.setState(0);
                    publicFileTask.setFid(cFile.getFid());
                    publicFileTaskMapper.insertTask(publicFileTask);
                }
                else{
                    // 有这个文件且是公共文件说明已经上传过相同文件 不在需要上传
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
                                                 PublicFileTask publicFileTask){
        FileChunk fileChunk = publicFileTask.getFileChunk();

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

    @RequestMapping("/getPublicFileTask")
    public ResultMessage<PublicFileTask[]> getPublicFileTask(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute(Constant.user);

        PublicFileTask[] publicFileTasks = null;
        // 管理员查看所有任务 用户只能查看自己的
        if(user.getType() == 0){
            publicFileTasks = publicFileTaskMapper.getTaskByUid(user.getUid());
        }
        else publicFileTasks = publicFileTaskMapper.getAllTasks();

        for (int i = 0; i < publicFileTasks.length; i++) {
            publicFileTasks[i].setPublicFile(publicFileMapper.getPublicFileInfoByFid(publicFileTasks[i].getFid()));
        }

        return new ResultMessage<>(Message.SUCCESS, null, publicFileTasks);
    }

    // 删除任务接口 用户和管理员都可以调用 只是效果不同
    @RequestMapping("/deleteTask")
    public ResultMessage<String> getPublicFileTask(HttpServletRequest request,
                                                   int tid){
        User user = (User)request.getSession().getAttribute(Constant.user);
        int uid = user.getUid();
        // 查询任务
        PublicFileTask publicFileTask = publicFileTaskMapper.getTaskById(tid);
        // 查询公共文件
        PublicFile publicFile = publicFileMapper.getPublicFileInfoByFid(publicFileTask.getFid());
        // 如果上传的公共文件还没有审核通过就删除 可以将表中数据和文件实体都删除
        if(publicFile.isState()) {
            // 获得文件实体
            CustomFile customFile = fileMapper.getFileInfoByFid(publicFileTask.getFid());
            // 删除文件实体
            File file = new File(customFile.getStorage_path() + customFile.getF_name());
            file.delete();

            // 删除文件表数据
            fileMapper.deleteFileByFid(publicFileTask.getFid());
            // 删除公共文件表数据
            publicFileMapper.deletePublicFile(publicFileTask.getFid());
        }
        // 用户需要uid 管理员不需要
        if(user.getType() == 0)
            publicFileTaskMapper.deleteTask(tid, uid);
        else
            publicFileTaskMapper.deleteTaskByTid(tid);

        return new ResultMessage<>(Message.SUCCESS, null, null);
    }

    @RequestMapping("/updateTask")
    public ResultMessage<String> updateTask(HttpServletRequest request,
                                            PublicFileTask publicFileTask){
        User user = (User)request.getSession().getAttribute(Constant.user);
        if(user.getType() == 0) return new ResultMessage<>(Message.ERROR, null, null);
        // 查询公共文件表
        PublicFile publicFile = publicFileTask.getPublicFile();
        // 更新文件任务表
        if(publicFileTask.getState() == 1) {
            // 更新任务表state为对应状态
            publicFileTaskMapper.updateTask(publicFileTask);
            // 更新公共文件表状态
            publicFile.setState(false);
            publicFileMapper.updatePublicFileInfo(publicFile);

        }
        else if(publicFileTask.getState() == 2){
            // 更新任务表state为对应状态
            publicFileTaskMapper.updateTask(publicFileTask);
            // 未通过审核不修改文件表的state值
            publicFile.setState(true);
            publicFileMapper.updatePublicFileInfo(publicFile);
        }
        else{
            return new ResultMessage<>(Message.ERROR, null, null);
        }

        return new ResultMessage<>(Message.SUCCESS, null, null);
    }

}
