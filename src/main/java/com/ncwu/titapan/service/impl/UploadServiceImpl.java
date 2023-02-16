package com.ncwu.titapan.service.impl;

import com.ncwu.titapan.constant.Constant;
import com.ncwu.titapan.mapper.FileChunkMapper;
import com.ncwu.titapan.mapper.FileMapper;
import com.ncwu.titapan.mapper.UserFileListMapper;
import com.ncwu.titapan.pojo.CustomFile;
import com.ncwu.titapan.pojo.FileChunk;
import com.ncwu.titapan.pojo.User;
import com.ncwu.titapan.pojo.UserFileList;
import com.ncwu.titapan.service.UploadService;
import com.ncwu.titapan.utils.DateUtil;
import com.ncwu.titapan.utils.FileUtil;
import com.ncwu.titapan.utils.PreviewImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/5 22:55
 */
@Service
public class UploadServiceImpl implements UploadService {
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private UserFileListMapper userFileListMapper;
    @Autowired
    private FileChunkMapper fileChunkMapper;

    /**
     * TODO 预检验文件是否存在
     *
     * @param user user
     * @param fileChunk fileChunk
     * @param userPath userPath
     * @return int
     * @Author ddwl.
     * @Date 2023/1/7 21:38
    **/
    @Override
    public int checkFile(User user, FileChunk fileChunk, String userPath) {
        // 判断文件是否已经存储有
        CustomFile file = fileMapper.getFileInfoByMD5(fileChunk.getId());
        UserFileList userFileList =
                userFileListMapper.getUserFileInfo(user.getUid(),
                        fileChunk.getOriginal_file_name(),
                        userPath);
        boolean flag_file = file != null;
        boolean flag_userFileList = userFileList != null;
        // 没文件 且不重名, 可以普通上传
        if(!flag_file && !flag_userFileList) return 0;
        // 有文件 且不重名, 可以进行快传
        else if(flag_file && !flag_userFileList) return 1;
        // 没文件 用户目录下文件重名, 目录下重名不允许上传
        else if(!flag_file && flag_userFileList) return 2;
        // 有文件 但用户目录下文件重名, 目录下重名不允许上传
        else return 3;
    }
    
    @Override
    public boolean checkFileChunk(String md5_val){
        FileChunk fileChunk = fileChunkMapper.getFileChunkByMD5(md5_val);
        return fileChunk != null;
    }

    /**
     * TODO 此方法用于处理完整文件第一次被上传的情况
     *
     * @param user user 进行上传操作的用户实体
     * @param userPath userPath 用户上传到的路径
     * @param fileChunk fileChunk 文件块
     * @return void
     * @Author ddwl.
     * @Date 2023/1/6 8:56
    **/
    @Override
    public boolean commonUploadFile(User user, String userPath, FileChunk fileChunk) {
        try {
            CustomFile cFile = new CustomFile();
            UserFileList ufList = new UserFileList();
            // TODO 插入文件保存并更新数据库
            cFile.setMd5_val(fileChunk.getMd5_val());
            // MD5+后缀作为文件名
            cFile.setF_name(fileChunk.getId() + fileChunk.getSuffix());
            cFile.setStorage_path(Constant.sys_storage_path);
            cFile.setUpload_date(DateUtil.getFormatDate());
            cFile.setF_size(fileChunk.getMFile().getSize());

            // 保存文件到统一的位置
            // String randomName = PreviewImageUtil.createRandomName(64) + fileChunk.getSuffix();
            File file = new File(Constant.sys_storage_path + cFile.getF_name());
            fileChunk.getMFile().transferTo(file);

            String preview_url = null;


            if(user.getType() == 1 && fileChunk.isPublic_file()){
                cFile.setPublic_file(true);
                cFile.setF_description(fileChunk.getF_description());
                cFile.setN_name(fileChunk.getOriginal_file_name());
                if(fileChunk.getMPreview() == null || fileChunk.getMPreview().getSize() > 1024 * 1024) return false;

                File tFile = new File(Constant.sys_preview_path + cFile.getF_name());
                fileChunk.getMPreview().transferTo(tFile);
                preview_url = PreviewImageUtil.get_preview_pic_url(tFile, true);
                tFile.delete();
            }
            else{
                preview_url = PreviewImageUtil.createPreviewURL(cFile.getStorage_path() + cFile.getF_name(), fileChunk.getSuffix());
            }
            System.out.println(preview_url);
            cFile.setPreview_url(preview_url);
            // 插入到file表中
            if(fileChunk.isPublic_file()) fileMapper.insertPublicFile(cFile);
            else fileMapper.insertFile(cFile);
            // 插入后在查询file中的id
            if(user.getType() == 0) {
                cFile = fileMapper.getFileInfoByMD5(cFile.getMd5_val());

                ufList.setUid(user.getUid());
                ufList.setFid(cFile.getFid());
                ufList.setStorage_path(userPath);
                ufList.setUpload_date(DateUtil.getFormatDate());
                ufList.setF_name(fileChunk.getOriginal_file_name());
                ufList.setF_size(cFile.getF_size());
                ufList.setPreview_url(preview_url);
                // 更新user_file_list
                userFileListMapper.insertFile(ufList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * TODO 此方法用于处理上传完整文件时文件在服务端已存在的情况
     *
     * @param user user
     * @param userPath userPath
     * @param md5_val md5_val
     * @param fileName fileName
     * @return void
     * @Author ddwl.
     * @Date 2023/1/6 16:42
    **/
    @Override
    public boolean quickUploadFile(User user, String userPath, String md5_val, String fileName) {
        CustomFile cFile = fileMapper.getFileInfoByMD5(md5_val);
        UserFileList ufList = new UserFileList();
        ufList.setUid(user.getUid());
        ufList.setFid(cFile.getFid());
        ufList.setUpload_date(DateUtil.getFormatDate());
        ufList.setStorage_path(userPath);
        ufList.setF_name(fileName);
        ufList.setF_size(cFile.getF_size());
        ufList.setPreview_url(cFile.getPreview_url());
        // 更新用户文件列表
        userFileListMapper.insertFile(ufList);
        return true;

    }

    /**
     * TODO 此方法用于处理上传分块文件完成后的合并与数据库更新
     *
     * @param user user
     * @param userPath userPath
     * @param fileChunk fileChunk
     * @return void
     * @Author ddwl.
     * @Date 2023/1/6 16:39
    **/
    @Override
    public boolean mergeFileChunk(User user, String userPath, FileChunk fileChunk) {
        try {
            // 查询文件所有的分块
            List<FileChunk> fileChunkList =
                    fileChunkMapper.getFileChunkListById(fileChunk.getId());

            List<String> filePathList = new ArrayList<>();
            for (FileChunk chunk : fileChunkList) {
                filePathList.add(chunk.getStorage_path()
                        + chunk.getTempName());
            }
            System.out.println(filePathList);
            // 文件合并 并返回保存的路径
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            FileUtil.merge(filePathList,
                    Constant.sys_storage_path,
                    fileChunk.getId() + fileChunk.getSuffix());

            stopWatch.stop();
            System.out.println("合并文件 " + fileChunk.getOriginal_file_name() + "用时：" + stopWatch.getTotalTimeSeconds() + "秒.");
            // 合并完成删除临时文件
            FileUtil.deleteTempFile(filePathList);
            // TODO 删除file_chunk表中的数据
            fileChunkMapper.deleteFileChunkById(fileChunk.getId());

            // 修改file表与userfile表
            CustomFile cFile = new CustomFile();
            UserFileList ufList = new UserFileList();
            cFile.setMd5_val(fileChunk.getId());
            cFile.setF_name(fileChunk.getId() + fileChunk.getSuffix());
            cFile.setStorage_path(Constant.sys_storage_path);
            cFile.setUpload_date(DateUtil.getFormatDate());
            cFile.setF_size(fileChunk.getTotalSize());

            String preview_url = null;

            if (user.getType() == 1 && fileChunk.isPublic_file()) {
                cFile.setPublic_file(true);
                cFile.setF_description(fileChunk.getF_description());
                cFile.setN_name(fileChunk.getOriginal_file_name());
                if (fileChunk.getMPreview() == null || fileChunk.getMPreview().getSize() > 1024 * 1024) return false;

                File tFile = new File(Constant.sys_preview_path + cFile.getF_name());
                fileChunk.getMPreview().transferTo(tFile);
                preview_url = PreviewImageUtil.get_preview_pic_url(tFile, true);
                tFile.delete();
            } else {
                preview_url = PreviewImageUtil.createPreviewURL(cFile.getStorage_path() + cFile.getF_name(), fileChunk.getSuffix());
            }

            cFile.setPreview_url(preview_url);


            if(fileChunk.isPublic_file()) fileMapper.insertPublicFile(cFile);
            else fileMapper.insertFile(cFile);

            // 插入后在查询file中的id
            if(user.getType() == 0) {
                cFile = fileMapper.getFileInfoByMD5(cFile.getMd5_val());
                ufList.setUid(user.getUid());
                ufList.setFid(cFile.getFid());
                ufList.setStorage_path(userPath);
                ufList.setUpload_date(DateUtil.getFormatDate());
                ufList.setF_name(fileChunk.getOriginal_file_name());
                ufList.setF_size(cFile.getF_size());
                ufList.setPreview_url(preview_url);

                userFileListMapper.insertFile(ufList);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return true;

    }

//    /**
//     * TODO 此方法用于当上传文件块到一半时但检测到服务端完整文件已经存在的情况 终止上传直接更新user_file_list表
//     * 好像写多余了
//     * @param user user
//     * @param userPath userPath
//     * @param md5_val md5_val
//     * @param fileName fileName
//     * @return void
//     * @Author ddwl.
//     * @Date 2023/1/6 16:38
//    **/
//    @Override
//    public boolean mergeFileChunk(User user, String userPath, String md5_val, String fileName) {
//        // 则删除部分块
//        List<FileChunk> fileChunkList =
//                fileChunkMapper.getFileChunkListById(md5_val);
//        List<String> filePathList = new ArrayList<>();
//        for (FileChunk chunk : fileChunkList) {
//            filePathList.add(chunk.getStorage_path()
//                    + chunk.getTempName());
//        }
//        FileUtil.deleteTempFile(filePathList);
//        fileChunkMapper.deleteFileChunkById(md5_val);
//
//        CustomFile cFile = fileMapper.getFileInfoByMD5(md5_val);
//        UserFileList ufList = new UserFileList();
//        ufList.setUid(user.getUid());
//        ufList.setFid(cFile.getFid());
//        ufList.setUpload_date(DateUtil.getFormatDate());
//        ufList.setStorage_path(userPath);
//        ufList.setF_name(fileName);
//        ufList.setF_size(cFile.getF_size());
//        ufList.setPreview_url(cFile.getPreview_url());
//        // 更新用户文件列表
//        userFileListMapper.insertFile(ufList);
//        return true;
//
//    }

    /**
     * TODO 暂时保存文件分块
     *
     * @param fileChunk fileChunk
     * @return void
     * @Author ddwl.
     * @Date 2023/1/6 11:58
    **/
    @Override
    public boolean saveChunk(FileChunk fileChunk) {
        try {
            MultipartFile mFile = fileChunk.getMFile();
            FileChunk theChunk = new FileChunk();
            // 路径文件夹以整个文件的MD5值命名
            String savePath = Constant.chunk_file_temp_path;
            // 添加到表中
            theChunk.setId(fileChunk.getId());
            theChunk.setMd5_val(fileChunk.getMd5_val());
            theChunk.setChunk_size(fileChunk.getChunk_size());
            theChunk.setNumber(fileChunk.getNumber());
            theChunk.setOriginal_file_name(fileChunk.getOriginal_file_name());
            theChunk.setTotal(fileChunk.getTotal());
            theChunk.setStorage_path(savePath);
            theChunk.setSuffix(fileChunk.getSuffix());
            theChunk.setStd_chunk_size(fileChunk.getStd_chunk_size());
            theChunk.setUpload_date(DateUtil.getFormatDate());
            // 插入块文件
            fileChunkMapper.insertFileChunk(theChunk);
            // 保存文件分块
            mFile.transferTo(new File(savePath + fileChunk.getTempName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
