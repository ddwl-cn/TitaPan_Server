package com.ncwu.titapan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


/**
 * TODO 文件块实体描述
 *
 * @author ddwl.
 * @date 2023/1/6 8:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileChunk {
    // 文件块id唯一标识整个文件 以md5值作为id
    String id;
    // 当前文件块 md5值
    String md5_val;
    // 文件实体
    MultipartFile mFile;
    // 文件块大小
    long chunk_size;
    // 文件块编号
    int number;
    // 总共的块数
    int total;
    // 原始文件名
    String original_file_name;
    // 存储路径
    String storage_path;
    // 文件块标准大小
    long std_chunk_size;
    // 文件后缀
    String suffix;
    // 分块块上传时间
    String upload_date;
    // 生成临时存储名称 后缀自定义为temp
    public String getTempName(){
        return md5_val + suffix;
    }
    // 获得文件的整体大小
    public long getTotalSize(){
        return (total-1)*std_chunk_size + chunk_size;
    }
}
