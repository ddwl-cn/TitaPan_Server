package com.ncwu.titapan.controller;

import com.ncwu.titapan.constant.Constant;
import com.ncwu.titapan.constant.Message;
import com.ncwu.titapan.mapper.FileMapper;
import com.ncwu.titapan.mapper.ShareLinkMapper;
import com.ncwu.titapan.mapper.UserFileListMapper;
import com.ncwu.titapan.pojo.*;
import com.ncwu.titapan.service.ShareLinkService;
import com.ncwu.titapan.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/20 12:29
 */
@RestController
public class ShareLinkController {
    @Autowired
    private UserFileListMapper userFileListMapper;
    @Autowired
    private ShareLinkService shareLinkService;
    @Autowired
    private ShareLinkMapper shareLinkMapper;
    @Autowired
    private FileMapper fileMapper;

    @RequestMapping("/share")
    public ResultMessage<String> share(HttpServletRequest request,
                                       HttpServletResponse response,
                                       ShareLink shareLink){
        User user = (User) request.getSession().getAttribute(Constant.user);
        // 用户当前路径
        String userPath = (String) request.getSession().getAttribute(Constant.userPath);

        shareLink.setUid(user.getUid());
        Date date = new Date();
        shareLink.setShare_date(DateUtil.formatDate(date));

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        // 把日期往后增加一天,整数  往后推,负数往前移动
        if("1天".equals(shareLink.getValid()))
            calendar.add(Calendar.DATE, 1);
        if("7天".equals(shareLink.getValid()))
            calendar.add(Calendar.DATE, 7);
        if("30天".equals(shareLink.getValid()))
            calendar.add(Calendar.DATE, 30);
        if("永久".equals(shareLink.getValid()))
            calendar.add(Calendar.DATE, 36500);
        // 这个时间就是日期往后推一天的结果
        date = calendar.getTime();
        shareLink.setExpire_date(DateUtil.formatDate(date));
        shareLink.setStorage_path(userPath);



        if(shareLinkService.share(shareLink))
            return new ResultMessage<>(Message.SUCCESS, Message.createShareLinkSuccess, null);
        else
            return new ResultMessage<>(Message.ERROR, Message.unknownError, null);
    }

    @RequestMapping("/getShareList")
    public ResultMessage<ShareLink[]> getShareList(HttpServletRequest request,
                                                 HttpServletResponse response){
        User user = (User) request.getSession().getAttribute(Constant.user);

        ShareLink[] shareLinks = shareLinkMapper.getShareList(user.getUid());
        if(shareLinks == null) return new ResultMessage<>(Message.ERROR, Message.unknownError, null);

        return new ResultMessage<>(Message.SUCCESS, Message.getShareListSuccess, shareLinks);
    }

    @RequestMapping("/extract")
    public ResultMessage<String> extract(HttpServletRequest request,
                                         HttpServletResponse response,
                                         String savePath,
                                         String share_uuid,
                                         String share_code){
        User user = (User) request.getSession().getAttribute(Constant.user);


        int state = shareLinkService.extract(user.getUid(), savePath, share_uuid, share_code);

        if(state == 0){
            return new ResultMessage<>(Message.ERROR, Message.shareLinkDeleted, null);
        }
        else if(state == 1){
            return new ResultMessage<>(Message.ERROR, Message.shareCodeError, null);
        }
        else if(state == 2){
            return new ResultMessage<>(Message.ERROR, Message.shareLinkExpired, null);
        }
        else if(state == 3){
            return new ResultMessage<>(Message.ERROR, Message.fileNameRepetitive, null);

        }
        return new ResultMessage<>(Message.SUCCESS, Message.extractSuccess, null);
    }

    @RequestMapping("/deleteShareLink")
    public ResultMessage<String> deleteShareLink(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 Integer share_id){

        User user = (User) request.getSession().getAttribute(Constant.user);
        // 用户当前路径
        String userPath = (String) request.getSession().getAttribute(Constant.userPath);

        ShareLink shareLink = shareLinkMapper.getShareLinkById(share_id);
        if(shareLink == null) return new ResultMessage<>(Message.ERROR, Message.unknownError, null);

        shareLinkMapper.deleteShareLink(share_id);
        return new ResultMessage<>(Message.SUCCESS, Message.deleteShareLinkSuccess, null);
    }

    @RequestMapping("/getSharedFileInfo")
    public ResultMessage<UserFileList> getSharedFileInfo(HttpServletRequest request,
                                                       HttpServletResponse response,
                                                       String share_uuid){

        ShareLink shareLink = shareLinkMapper.getShareLinkByUUID(share_uuid);
        // 先查出来分享链接 通过其中的uid f_name storage_path查找file信息
        if(shareLink == null)
            return new ResultMessage<>(Message.ERROR, Message.unknownError, null);
        // sharelink中的文件名需要随着用户更改文件名称一起修改
        UserFileList file =
                userFileListMapper.getUserFileInfo(shareLink.getUid(), shareLink.getF_name(), shareLink.getStorage_path());

        if(file == null)
            return new ResultMessage<>(Message.ERROR, Message.unknownError, null);


        return new ResultMessage<>(Message.SUCCESS, Message.getSharedFileInfoSuccess, file);
    }


    @RequestMapping("/getShareLinkInfo")
    public ResultMessage<ShareLink> getShareLinkInfo(HttpServletRequest request,
                                                         HttpServletResponse response,
                                                         String share_uuid){

        ShareLink shareLink = shareLinkMapper.getShareLinkByUUID(share_uuid);
        // 先查出来分享链接 通过其中的uid f_name storage_path查找file信息
        if(shareLink == null)
            return new ResultMessage<>(Message.ERROR, Message.unknownError, null);

        shareLink.setShare_code("");

        return new ResultMessage<>(Message.SUCCESS, Message.getShareLinkInfoSuccess, shareLink);
    }

}
