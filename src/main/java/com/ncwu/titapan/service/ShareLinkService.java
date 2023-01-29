package com.ncwu.titapan.service;

import com.ncwu.titapan.pojo.ShareLink;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/20 13:06
 */
public interface ShareLinkService {

    boolean share(ShareLink shareLink);

    int extract(int uid, String userPath, String share_link, String share_code);
}
