package com.ncwu.titapan.service.impl;

import com.ncwu.titapan.mapper.UserFileListMapper;
import com.ncwu.titapan.pojo.User;
import com.ncwu.titapan.pojo.UserFileList;
import com.ncwu.titapan.service.UserBehaviorService;
import com.ncwu.titapan.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
