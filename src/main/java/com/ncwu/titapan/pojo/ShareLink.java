package com.ncwu.titapan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/20 12:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShareLink {

    int share_id;
    int uid;
    int fid;
    String share_uuid;
    String share_code;
    String share_date;
    String expire_date;
    String valid;
    boolean isFolder;
    String storage_path;
    String f_name;

}
