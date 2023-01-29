package com.ncwu.titapan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/5 21:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFileList {
    int uid;
    int fid;
    String upload_date;
    String storage_path;
    String f_name;
    long f_size;
    boolean isFolder;
}
