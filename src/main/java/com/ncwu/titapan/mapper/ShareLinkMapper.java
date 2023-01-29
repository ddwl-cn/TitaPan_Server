package com.ncwu.titapan.mapper;

import com.ncwu.titapan.pojo.ShareLink;
import org.apache.ibatis.annotations.Mapper;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/20 13:11
 */
@Mapper
public interface ShareLinkMapper {

    void insertNewShareLink(ShareLink shareLink);

    ShareLink getShareLinkByUUID(String link);

    ShareLink[] getShareList(int uid);

    void deleteShareLink(int share_id);

    ShareLink getShareLinkById(int share_id);

    void updateFileName(int uid, String old_f_name, String new_f_name, String storage_path);
}
