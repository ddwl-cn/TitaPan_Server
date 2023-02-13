package com.ncwu.titapan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/5 21:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomFile {
    int fid;
    String md5_val;
    String storage_path;
    String f_name;
    String upload_date;
    long f_size;
    // 是否为公共文件
    boolean public_file;
    String preview_url;
    String f_description;
}
