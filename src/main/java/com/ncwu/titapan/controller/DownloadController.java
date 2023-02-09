package com.ncwu.titapan.controller;

import com.ncwu.titapan.constant.Constant;
import com.ncwu.titapan.constant.Message;
import com.ncwu.titapan.mapper.FileMapper;
import com.ncwu.titapan.mapper.UserFileListMapper;
import com.ncwu.titapan.pojo.CustomFile;
import com.ncwu.titapan.pojo.ResultMessage;
import com.ncwu.titapan.pojo.User;
import com.ncwu.titapan.pojo.UserFileList;
import com.ncwu.titapan.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO 文件下载请求接口
 *
 * @author ddwl.
 * @date 2023/1/8 15:33
 */
@RestController
@RequestMapping("/download")
public class DownloadController {

    @Autowired
    private UserFileListMapper userFileListMapper;
    @Autowired
    FileMapper fileMapper;
    @Autowired
    private DownloadService downloadService;


    // 单文件下载
    @RequestMapping("/single")
    public ResultMessage<String> downloadSingle(HttpServletRequest request,
                                 HttpServletResponse response,
                                 String f_name){

        // 获得用户实体
        User user = (User) request.getSession().getAttribute(Constant.user);
        // 用户当前路径
        String userPath = (String) request.getSession().getAttribute(Constant.userPath);
        // 查询用户要下载的文件是否存在
        UserFileList userFileList = userFileListMapper.getUserFileInfo(user.getUid(), f_name, userPath);
        if(userFileList == null) return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null); // 无效请求
        // 是普通文件
        if(!userFileList.isFolder()) {
            CustomFile cFile = fileMapper.getFileInfoByFid(userFileList.getFid());

            if (downloadService.downloadSingle(response, userFileList.getF_name(), cFile.getF_name())) {
                // 出错返回错误信息 但是正常下载返回void 使用null等效
                return null;
            }
        }
        else{
            // 下载文件
            if(downloadService.downloadFolder(response, user.getUid(), userPath, f_name))
                return null;
        }
        return new ResultMessage<>(Message.ERROR, Message.unknownError, null);
    }

}
