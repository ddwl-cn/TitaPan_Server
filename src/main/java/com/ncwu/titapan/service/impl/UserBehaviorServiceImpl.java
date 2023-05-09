package com.ncwu.titapan.service.impl;

import com.ncwu.titapan.constant.Constant;
import com.ncwu.titapan.constant.Message;
import com.ncwu.titapan.mapper.CommentMapper;
import com.ncwu.titapan.mapper.UserFileListMapper;
import com.ncwu.titapan.mapper.UserMapper;
import com.ncwu.titapan.pojo.ClipBoard;
import com.ncwu.titapan.pojo.ResultMessage;
import com.ncwu.titapan.pojo.User;
import com.ncwu.titapan.pojo.UserFileList;
import com.ncwu.titapan.service.UserBehaviorService;
import com.ncwu.titapan.utils.DateUtil;
import com.ncwu.titapan.utils.FileUtil;
import com.ncwu.titapan.utils.PreviewImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/11 11:37
 */
@Service
public class UserBehaviorServiceImpl implements UserBehaviorService {
    @Autowired
    private UserFileListMapper userFileListMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;

    /**
     * TODO 在用户当前路径下创建文件夹
     *
     * @param user user
     * @param userPath userPath
     * @param folderName folderName
     * @return void
     * @Author ddwl.
     * @Date 2023/1/11 11:39
    **/
    @Override
    public void createFolder(User user, String userPath, String folderName) {
        UserFileList userFileList = new UserFileList();
        userFileList.setUid(user.getUid());
        // 文件夹无 fid
        userFileList.setFid(-1);
        // 文件夹大小为0
        userFileList.setF_size(0L);
        userFileList.setStorage_path(userPath);
        userFileList.setUpload_date(DateUtil.getFormatDate());
        userFileList.setF_name(folderName);
        userFileList.setFolder(true);

        userFileListMapper.insertFile(userFileList);
    }

    @Override
    public ResultMessage<String> paste(User user, String userPath, ClipBoard clipBoard) {
        if(clipBoard != null){
            UserFileList userFileList = clipBoard.getUserFileList();
            UserFileList[] userFiles = null;
            UserFileList[] userFolders = null;
            // 粘贴
            // 如果是文件夹 需要把其中的文件都移动过去
            if(userFileList.isFolder()) {
                userFiles = userFileListMapper.getAllFileUnderFolder(user.getUid(), userFileList.getStorage_path(), userFileList.getF_name());
                userFolders = userFileListMapper.getAllFolderUnderFolder(user.getUid(), userFileList.getStorage_path(), userFileList.getF_name());
                // 剪切的位置不能是该文件夹的子文件夹(即不能递归剪切)
                if(clipBoard.getType() == 1 && userPath.startsWith(userFileList.getStorage_path()) && userFileList.isFolder()){
                    return new ResultMessage<>(Message.WARNING, Message.pasteRecursive, null);
                }
            }
            if(userFileListMapper.getUserFileInfo(user.getUid(), userFileList.getF_name(), userPath) != null){
                return new ResultMessage<>(Message.WARNING, Message.fileNameRepetitive, null);
            }
            // 剪切需要额外进行删除操作

            if(clipBoard.getType() == 1){
                userFileListMapper.deleteFile(user.getUid(), userFileList.getF_name(), userFileList.getStorage_path());
                if(userFileList.isFolder()){
                    for (UserFileList userFile : userFiles) {
                        userFileListMapper.deleteFile(user.getUid(), userFile.getF_name(), userFile.getStorage_path());
                    }
                    for (UserFileList userFolder : userFolders) {
                        userFileListMapper.deleteFile(user.getUid(), userFolder.getF_name(), userFolder.getStorage_path());
                    }
                }
            }

            if(userFileList.isFolder()){
                // 把文件存储路径修改
                for (UserFileList userFile : userFiles) {
                    userFile.setStorage_path(userFile.getStorage_path().replaceFirst(userFileList.getStorage_path(), userPath));
                    userFileListMapper.insertFile(userFile);
                }
                // 把文件存储路径修改
                for (UserFileList userFolder : userFolders) {
                    userFolder.setStorage_path(userFolder.getStorage_path().replaceFirst(userFileList.getStorage_path(), userPath));
                    userFileListMapper.insertFile(userFolder);
                }
            }

            userFileList.setStorage_path(userPath);
            userFileListMapper.insertFile(userFileList);

            return new ResultMessage<>(Message.SUCCESS, Message.pasteFileSuccess, null);
        }
        return new ResultMessage<>(Message.ERROR, Message.unknownError, null);
    }

    @Override
    public void updateUserInfo(User userInfo, User oldInfo){
        MultipartFile multipartFile = userInfo.getAvatar();
        try {
            String old_url = oldInfo.getAvatar_url();
            String avatar_url = old_url;

            if (multipartFile != null) {
                File newAvatar = FileUtil.multipartFileToFile(multipartFile);
                avatar_url = PreviewImageUtil.get_preview_pic_url(newAvatar, true);
                userInfo.setAvatar_url(avatar_url);
            }

            userMapper.updateUserInfo(userInfo);

            if (!avatar_url.equals(old_url)) {
                commentMapper.updateCommentAvatarURL(userInfo.getUid(), avatar_url);

                if (old_url != null) {
                    String fileName = PreviewImageUtil.getFileNameFromUrl(old_url);
                    File oldAvatar = new File(Constant.preview_image_path + fileName);
                    oldAvatar.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
