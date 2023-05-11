package com.ncwu.titapan.service;

import com.ncwu.titapan.pojo.FileChunk;
import com.ncwu.titapan.pojo.User;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/5/10 19:25
 */
public interface PublicFileTaskService {


    int checkFile(User user, FileChunk fileChunk, String userPath);

    boolean saveChunk(FileChunk fileChunk);

}
