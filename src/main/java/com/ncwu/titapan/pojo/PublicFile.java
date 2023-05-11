package com.ncwu.titapan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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
    // 文件名（作为用户保存后的默认文件名称）
    String f_name;
    // 文件别名
    String n_name;
    // 文件评分
    float score;
    // 文件热度
    int hot;
    // 文件大小
    long f_size;
    // 前端更新预览图需要
    MultipartFile preview_image;
    // 文件描述
    String f_description;
    // 上传日期
    String upload_date;
    // 封面缩略图
    String preview_url;
    // 用来标记是否是用户的公共文件上传任务 0为正常 1为未审核通过不能被其他用户看见
    boolean state;
    boolean visible;
}
