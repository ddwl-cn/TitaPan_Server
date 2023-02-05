package com.ncwu.titapan.controller;

import com.ncwu.titapan.constant.Constant;
import com.ncwu.titapan.constant.Message;
import com.ncwu.titapan.mapper.FileMapper;
import com.ncwu.titapan.mapper.ShareLinkMapper;
import com.ncwu.titapan.mapper.UserFileListMapper;
import com.ncwu.titapan.pojo.CustomFile;
import com.ncwu.titapan.pojo.ResultMessage;
import com.ncwu.titapan.pojo.User;
import com.ncwu.titapan.pojo.UserFileList;
import com.ncwu.titapan.service.UserBehaviorService;
import com.ncwu.titapan.utils.DateUtil;
import com.ncwu.titapan.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/5 21:30
 */
@RestController
@RequestMapping("/user")
public class UserBehaviorController {
    @Autowired
    private UserFileListMapper userFileListMapper;

    @Autowired
    private UserBehaviorService userBehaviorService;
    @Autowired
    private ShareLinkMapper shareLinkMapper;
    @Autowired
    private FileMapper fileMapper;


    @RequestMapping("/getUserFileList")
    public ResultMessage<UserFileList[]> getUserFileList(HttpServletRequest request){
        User user = (User)request.getSession().getAttribute(Constant.user);
        String userPath = (String)request.getSession().getAttribute(Constant.userPath);
        UserFileList[] userFiles = userFileListMapper.getUserFileList(user.getUid(), userPath);

        if(userFiles == null) new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);

        return new ResultMessage<>(Message.SUCCESS, Message.getUserFileListSuccess, userFiles);
    }

    @RequestMapping("/createFolder")
    public ResultMessage<Object> createFolder(HttpServletRequest request,
                                              String folderName){

        if(folderName == null || folderName.isBlank() || folderName.isEmpty())
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);

        User user = (User)request.getSession().getAttribute(Constant.user);
        String userPath = (String)request.getSession().getAttribute(Constant.userPath);
        UserFileList userFile = userFileListMapper.getUserFileInfo(user.getUid(), folderName, userPath);

        if(userFile != null) return new ResultMessage<>(Message.WARNING, Message.fileNameRepetitive, null);

        userBehaviorService.createFolder(user, userPath, folderName);

        return new ResultMessage<>(Message.SUCCESS, Message.createFolderSuccess, null);
    }

    @RequestMapping("/updateFileName")
    public ResultMessage<Object> updateFileName(HttpServletRequest request,
                                                String oldName, String newName, boolean isFolder){

        User user = (User)request.getSession().getAttribute(Constant.user);
        String userPath = (String)request.getSession().getAttribute(Constant.userPath);
        UserFileList userFileList = userFileListMapper.getUserFileInfo(user.getUid(), oldName, userPath);

        if (userFileList == null) return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);
        userFileList = userFileListMapper.getUserFileInfo(user.getUid(), newName, userPath);

        if (userFileList != null) return new ResultMessage<>(Message.WARNING, Message.fileNameRepetitive, null);
        if(!isFolder) {
            // 真正开始修改文件名
            shareLinkMapper.updateFileName(user.getUid(), oldName, newName, userPath);
            userFileListMapper.updateFileName(user.getUid(), oldName, newName, DateUtil.getFormatDate(), userPath);
            return new ResultMessage<>(Message.SUCCESS, Message.updateFileNameSuccess, null);
        }
        else{
            // 修改文件夹名称及其子文件路径
            shareLinkMapper.updateFileName(user.getUid(), oldName, newName,userPath);
            userFileListMapper.updateFolderName(user.getUid(), oldName, newName, DateUtil.getFormatDate(), userPath);
            return new ResultMessage<>(Message.SUCCESS, Message.updateFileNameSuccess, null);
        }
    }

    @RequestMapping("/deleteFile")
    public ResultMessage<Object> deleteFile(HttpServletRequest request,
                                                String fileName){

        User user = (User)request.getSession().getAttribute(Constant.user);
        String userPath = (String)request.getSession().getAttribute(Constant.userPath);

        UserFileList userFileList = userFileListMapper.getUserFileInfo(user.getUid(), fileName, userPath);

        if (userFileList == null) return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);

        userFileListMapper.deleteFile(user.getUid(), fileName, userPath);

        return new ResultMessage<>(Message.SUCCESS, Message.deleteFileSuccess, null);
    }

    @RequestMapping("/deleteFolder")
    public ResultMessage<Object> deleteFolder(HttpServletRequest request,
                                            String fileName){

        User user = (User)request.getSession().getAttribute(Constant.user);
        String userPath = (String)request.getSession().getAttribute(Constant.userPath);

        UserFileList userFileList = userFileListMapper.getUserFileInfo(user.getUid(), fileName, userPath);

        if (userFileList == null) return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);

        userFileListMapper.deleteFolder(user.getUid(), fileName, userPath);

        return new ResultMessage<>(Message.SUCCESS, Message.deleteFileSuccess, null);
    }


    @RequestMapping("/toPath")
    public ResultMessage<String> toPath(HttpServletRequest request,
                                            HttpServletResponse response,
                                            String toPath){// 前端传来需要前往的路径
        User user = (User)request.getSession().getAttribute(Constant.user);

        if(toPath == null || toPath.isBlank() || userFileListMapper.isPathExist(user.getUid(), toPath) == 0)
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);


        request.getSession().setAttribute("userPath", toPath);
        String userPath = (String)request.getSession().getAttribute("userPath");

        return new ResultMessage<>(Message.SUCCESS, Message.changePathSuccess, null);
    }

    @RequestMapping("/getUserFolderList")
    public ResultMessage<UserFileList[]> getUserFolderList(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   String savePath){
        User user = (User)request.getSession().getAttribute(Constant.user);

        if(savePath == null || savePath.isBlank() || userFileListMapper.isPathExist(user.getUid(), savePath) == 0)
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);

        UserFileList[] userFileLists = userFileListMapper.getUserFolders(user.getUid(), savePath);

        return new ResultMessage<>(Message.SUCCESS, Message.getUserFolderListSuccess, userFileLists);
    }

    @RequestMapping("/toFolder")
    public ResultMessage<String> toFolder(HttpServletRequest request,
                                        HttpServletResponse response,
                                        String savePath){// 前端传来需要前往的路径
        User user = (User)request.getSession().getAttribute(Constant.user);

        if(savePath == null || savePath.isBlank() || userFileListMapper.isPathExist(user.getUid(), savePath) == 0)
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);

        return new ResultMessage<>(Message.SUCCESS, Message.changePathSuccess, null);
    }

    /**
     * TODO 文件预览 使用第三方预览
     *
     * @param request request
     * @param f_name f_name
     * @return ResultMessage<String>
     * @Author ddwl.
     * @Date 2023/2/4 14:05
    **/
    @RequestMapping("/preview")
    public ResultMessage<String> preview(HttpServletRequest request,
                                         String f_name){
        User user = (User)request.getSession().getAttribute(Constant.user);
        String userPath = (String)request.getSession().getAttribute(Constant.userPath);
        if(f_name == null || f_name.isBlank())
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);

        // 用户文件
        UserFileList userFileList = userFileListMapper.getUserFileInfo(user.getUid(), f_name, userPath);
        if(userFileList == null)
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);

        CustomFile customFile = fileMapper.getFileInfoByFid(userFileList.getFid());
        if(customFile == null)
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);
        // Files类实现文件复制

        try {

            File src_file = new File(Constant.sys_storage_path + customFile.getF_name());
            File dest_file = new File(Constant.sys_preview_path + customFile.getF_name());

            if(!dest_file.exists()) {
                if(f_name.endsWith(".mp4")) FileUtil.convertMP4EncodeType(src_file, dest_file);
                else Files.copy(src_file.toPath(), dest_file.toPath());
            }

        } catch (IOException e) {
            e.printStackTrace();
            return new ResultMessage<>(Message.ERROR, Message.unknownError, null);
        }

        return new ResultMessage<>(Message.SUCCESS, "", customFile.getF_name());
    }


}
