package com.ncwu.titapan.service;

import com.ncwu.titapan.pojo.ClipBoard;
import com.ncwu.titapan.pojo.ResultMessage;
import com.ncwu.titapan.pojo.User;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/11 11:33
 */
public interface UserBehaviorService {

    void createFolder(User user, String userPath, String folderName);

    ResultMessage<String> paste(User user, String userPath, ClipBoard clipBoard);

    void updateUserInfo(User userInfo, User oldInfo);

}
