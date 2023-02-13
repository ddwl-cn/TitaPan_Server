package com.ncwu.titapan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/2/13 14:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicFile {
    int fid;
    String f_name;
    String f_description;
    String preview_url;
    long f_size;
    String md5_val;
}
