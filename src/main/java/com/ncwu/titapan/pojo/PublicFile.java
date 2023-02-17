package com.ncwu.titapan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/2/17 19:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicFile {
    int fid;
    // 文件名
    String f_name;
    // 文件别名
    String n_name;
    // 文件评分
    float score;
    // 文件热度
    int hot;
    // 文件大小
    long f_size;
    // 文件描述
    String f_description;
    // 上传日期
    String upload_date;
    // 封面缩略图
    String preview_url;
}
