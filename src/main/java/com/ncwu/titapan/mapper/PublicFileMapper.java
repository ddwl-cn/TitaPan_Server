package com.ncwu.titapan.mapper;

import com.ncwu.titapan.pojo.PublicFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/2/13 14:08
 */
@Mapper
public interface PublicFileMapper {

    void insertPublicFile(PublicFile publicFile);

    PublicFile[] getPublicFileList(@Param("offset") int index,
                                   @Param("count") int count);

    int getPublicFileCount();

}
