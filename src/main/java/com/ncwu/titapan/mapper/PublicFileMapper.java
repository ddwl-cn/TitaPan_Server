package com.ncwu.titapan.mapper;

import com.ncwu.titapan.pojo.PublicFile;
import org.apache.ibatis.annotations.Mapper;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/2/17 19:32
 */
@Mapper
public interface PublicFileMapper {
    void insertPublicFile(PublicFile publicFile);
    void deletePublicFile(int fid);
    PublicFile[] getPublicFileList(int offset, int count);
    int getPublicFileCount();
}
