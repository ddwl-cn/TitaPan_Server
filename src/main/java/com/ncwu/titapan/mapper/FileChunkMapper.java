package com.ncwu.titapan.mapper;

import com.ncwu.titapan.pojo.FileChunk;
import org.apache.ibatis.annotations.Mapper;

import java.io.File;
import java.util.List;

/**
 * TODO 文件块相关操作
 *
 * @author ddwl.
 * @date 2023/1/6 11:49
 */
@Mapper
public interface FileChunkMapper {

    // 获得文件块
    FileChunk getFileChunkByMD5(String md5_val);

    // 插入新的文件块
    void insertFileChunk(FileChunk fileChunk);

    // 寻找同一文件的所有分块
    List<FileChunk> getFileChunkListById(String id);

    // 删除表中无效的块文件信息
    void deleteFileChunkById(String id);
    void deleteFileChunkByMD5(String md5_val);

    // 获得文件分块已上传的数量
    int getFileChunkNumber(String id);

    FileChunk[] getRubbishChunks(String nowDate);
}
