package com.ncwu.titapan.service.impl;

import com.ncwu.titapan.constant.Constant;
import com.ncwu.titapan.mapper.FileChunkMapper;
import com.ncwu.titapan.mapper.FileMapper;
import com.ncwu.titapan.mapper.PublicFileMapper;
import com.ncwu.titapan.mapper.UserFileListMapper;
import com.ncwu.titapan.pojo.CustomFile;
import com.ncwu.titapan.pojo.FileChunk;
import com.ncwu.titapan.pojo.User;
import com.ncwu.titapan.pojo.UserFileList;
import com.ncwu.titapan.service.PublicFileTaskService;
import com.ncwu.titapan.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/5/10 19:25
 */
@Service
public class PublicFileTaskServiceImpl implements PublicFileTaskService {
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private UserFileListMapper userFileListMapper;
    @Autowired
    private FileChunkMapper fileChunkMapper;
    @Autowired
    private PublicFileMapper publicFileMapper;


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
