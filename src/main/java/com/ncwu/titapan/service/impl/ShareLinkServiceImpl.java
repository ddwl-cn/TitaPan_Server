package com.ncwu.titapan.service.impl;

import com.ncwu.titapan.mapper.ShareLinkMapper;
import com.ncwu.titapan.mapper.UserFileListMapper;
import com.ncwu.titapan.pojo.ShareLink;
import com.ncwu.titapan.pojo.UserFileList;
import com.ncwu.titapan.service.ShareLinkService;
import com.ncwu.titapan.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/20 13:07
 */
@Service
public class ShareLinkServiceImpl implements ShareLinkService {
    @Autowired
    private UserFileListMapper userFileListMapper;
    @Autowired
    private ShareLinkMapper shareLinkMapper;

     @Override
     public boolean share(ShareLink shareLink){
         UserFileList userFileList = userFileListMapper.getUserFileInfo(shareLink.getUid(), shareLink.getF_name(), shareLink.getStorage_path());

         if(userFileList == null){
             return false;
         }
         shareLinkMapper.insertNewShareLink(shareLink);
         return true;
     }
     @Override
     public int extract(int uid,String userPath, String share_link, String share_code){
         ShareLink shareLink = shareLinkMapper.getShareLinkByUUID(share_link);
         if(shareLink == null) return 0; // 链接已被取消或不存在
         UserFileList userFileList = userFileListMapper.getUserFileInfo(shareLink.getUid(), shareLink.getF_name(), shareLink.getStorage_path());

         UserFileList user_file = userFileListMapper.getUserFileInfo(uid, shareLink.getF_name(), userPath);
         if(user_file != null) return 3; // 重名文件

         if(userFileList == null) return 0; // 或文件已被删除
         if(!shareLink.getShare_code().equals(share_code)){
             return 1; // 分享码错误
         }
         if(DateUtil.biggerThan(DateUtil.getFormatDate(), shareLink.getExpire_date())){
             return 2; // 分享过期
         }
         // 如果分享的是文件夹
         if(shareLink.isFolder()){
             UserFileList[] userFileLists =
                     userFileListMapper.getFileUnderFolder(shareLink.getUid(), shareLink.getF_name(), shareLink.getStorage_path());

             for (UserFileList file : userFileLists) {
                 file.setUid(uid);
                 // 处理原先的文件路径为提取者的路径
                 // 替换原先文件的存储路径前缀为提取者的当前路径
                 String path = file.getStorage_path()
                         .replaceFirst(shareLink.getStorage_path(), userPath);
                 file.setStorage_path(path);
                 userFileListMapper.insertFile(file);
             }
         }
         else{
             UserFileList file = userFileListMapper.getUserFileInfo(shareLink.getUid(), shareLink.getF_name(), shareLink.getStorage_path());
             file.setUid(uid);
             String path = file.getStorage_path()
                     .replaceFirst(shareLink.getStorage_path(), userPath);
             file.setStorage_path(path);
             userFileListMapper.insertFile(file);
         }
         return 4;
     }
}
