package com.ncwu.titapan.mapper;

import com.ncwu.titapan.pojo.CustomFile;
import org.apache.ibatis.annotations.Mapper;

/**
 * TODO file表相关操作
 *
 * @author ddwl.
 * @date 2023/1/5 22:51
 */
@Mapper
public interface FileMapper {
    CustomFile getFileInfoByMD5(String md5_val);
    CustomFile getFileInfoByFid(int fid);

    void insertFile(CustomFile cFile);



}
