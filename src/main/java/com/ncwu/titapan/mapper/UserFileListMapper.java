package com.ncwu.titapan.mapper;

import com.ncwu.titapan.pojo.User;
import com.ncwu.titapan.pojo.UserFileList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/6 9:36
 */
@Mapper
public interface UserFileListMapper {

    void insertFile(UserFileList userFileList);

    UserFileList getUserFileInfo(@Param("uid") int uid,
                                 @Param("f_name") String f_name,
                                 @Param("storage_path") String storage_path);

    UserFileList[] getUserFileList(@Param("uid") int uid,
                                   @Param("storage_path") String storage_path);


    int isPathExist(@Param("uid") int uid,
                             @Param("storage_path") String storage_path);

    void updateFileName(@Param("uid") int uid,
                        @Param("oldName") String oldName,
                        @Param("newName") String newName,
                        @Param("update_date") String update_date,
                        @Param("storage_path") String storage_path);

    void deleteFile(@Param("uid") int uid,
                    @Param("fileName") String fileName,
                    @Param("storage_path") String storage_path);

    void deleteFolder(@Param("uid") int uid,
                    @Param("fileName") String fileName,
                    @Param("storage_path") String storage_path);

    void updateFolderName(@Param("uid") int uid,
                          @Param("oldName") String oldName,
                          @Param("newName") String newName,
                          @Param("update_date") String update_date,
                          @Param("storage_path") String storage_path);

    UserFileList[] getFileUnderFolder(@Param("uid") int uid,
                            @Param("f_name") String f_name,
                            @Param("storage_path") String storage_path);

    UserFileList[] getUserFolders(@Param("uid") int uid,
                                  @Param("storage_path") String storage_path);


    // 获得目标文件夹下的所有文件夹列表
    UserFileList[] getAllFolderUnderFolder(@Param("uid") int uid,
                                           @Param("folderPath") String folderPath,
                                           @Param("folderName") String folderName);

    // 获得目标文件夹下的所有文件列表
    UserFileList[] getAllFileUnderFolder(@Param("uid") int uid,
                                         @Param("folderPath") String folderPath,
                                         @Param("folderName") String folderName);
}
