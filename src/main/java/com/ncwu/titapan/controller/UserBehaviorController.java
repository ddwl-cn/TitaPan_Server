package com.ncwu.titapan.controller;

import com.ncwu.titapan.constant.Constant;
import com.ncwu.titapan.constant.Message;
import com.ncwu.titapan.mapper.*;
import com.ncwu.titapan.pojo.*;
import com.ncwu.titapan.service.UserBehaviorService;
import com.ncwu.titapan.utils.DateUtil;
import com.ncwu.titapan.utils.FileUtil;
import com.ncwu.titapan.utils.PreviewImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/5 21:30
 */
@RestController
@RequestMapping("/user")
public class UserBehaviorController {
    @Autowired
    private UserFileListMapper userFileListMapper;

    @Autowired
    private UserBehaviorService userBehaviorService;
    @Autowired
    private ShareLinkMapper shareLinkMapper;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private StarMapper starMapper;
    @Autowired
    private MarkMapper markMapper;
    @Autowired
    private PublicFileMapper publicFileMapper;


    @RequestMapping("/getUserFileList")
    public ResultMessage<UserFileList[]> getUserFileList(HttpServletRequest request){
        User user = (User)request.getSession().getAttribute(Constant.user);
        String userPath = (String)request.getSession().getAttribute(Constant.userPath);

        UserFileList[] userFiles = userFileListMapper.getUserFileList(user.getUid(), userPath);


        if(userFiles == null) return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);

        return new ResultMessage<>(Message.SUCCESS, Message.getUserFileListSuccess, userFiles);
    }

    @RequestMapping("/createFolder")
    public ResultMessage<Object> createFolder(HttpServletRequest request,
                                              String folderName){

        if(folderName == null || folderName.isBlank() || folderName.isEmpty())
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);

        User user = (User)request.getSession().getAttribute(Constant.user);
        String userPath = (String)request.getSession().getAttribute(Constant.userPath);
        UserFileList userFile = userFileListMapper.getUserFileInfo(user.getUid(), folderName, userPath);

        if(userFile != null) return new ResultMessage<>(Message.WARNING, Message.fileNameRepetitive, null);

        userBehaviorService.createFolder(user, userPath, folderName);

        return new ResultMessage<>(Message.SUCCESS, Message.createFolderSuccess, null);
    }

    @RequestMapping("/updateFileName")
    public ResultMessage<Object> updateFileName(HttpServletRequest request,
                                                String oldName, String newName, boolean isFolder){

        User user = (User)request.getSession().getAttribute(Constant.user);
        String userPath = (String)request.getSession().getAttribute(Constant.userPath);
        UserFileList userFileList = userFileListMapper.getUserFileInfo(user.getUid(), oldName, userPath);

        if (userFileList == null) return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);
        userFileList = userFileListMapper.getUserFileInfo(user.getUid(), newName, userPath);

        if (userFileList != null) return new ResultMessage<>(Message.WARNING, Message.fileNameRepetitive, null);
        if(!isFolder) {
            // 真正开始修改文件名
            shareLinkMapper.updateFileName(user.getUid(), oldName, newName, userPath);
            userFileListMapper.updateFileName(user.getUid(), oldName, newName, DateUtil.getFormatDate(), userPath);
            return new ResultMessage<>(Message.SUCCESS, Message.updateFileNameSuccess, null);
        }
        else{
            // 修改文件夹名称及其子文件路径
            shareLinkMapper.updateFileName(user.getUid(), oldName, newName,userPath);
            userFileListMapper.updateFolderName(user.getUid(), oldName, newName, DateUtil.getFormatDate(), userPath);
            return new ResultMessage<>(Message.SUCCESS, Message.updateFileNameSuccess, null);
        }
    }

    @RequestMapping("/deleteFile")
    public ResultMessage<Object> deleteFile(HttpServletRequest request,
                                                String fileName){

        User user = (User)request.getSession().getAttribute(Constant.user);
        String userPath = (String)request.getSession().getAttribute(Constant.userPath);

        UserFileList userFileList = userFileListMapper.getUserFileInfo(user.getUid(), fileName, userPath);

        if (userFileList == null) return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);

        userFileListMapper.deleteFile(user.getUid(), fileName, userPath);
        // 同时删除本地预览图片
        if(FileUtil.isVedio(fileName) || FileUtil.isVedio(fileName)) {
            String path = Constant.preview_image_path + FileUtil.getFileNameFromPath(userFileList.getPreview_url());
            File file = new File(path);
            file.delete();
        }

        return new ResultMessage<>(Message.SUCCESS, Message.deleteFileSuccess, null);
    }

    @RequestMapping("/deleteFolder")
    public ResultMessage<Object> deleteFolder(HttpServletRequest request,
                                            String fileName){

        User user = (User)request.getSession().getAttribute(Constant.user);
        String userPath = (String)request.getSession().getAttribute(Constant.userPath);

        UserFileList userFileList = userFileListMapper.getUserFileInfo(user.getUid(), fileName, userPath);

        if (userFileList == null) return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);

        userFileListMapper.deleteFolder(user.getUid(), fileName, userPath);

        return new ResultMessage<>(Message.SUCCESS, Message.deleteFileSuccess, null);
    }


    @RequestMapping("/toPath")
    public ResultMessage<String> toPath(HttpServletRequest request,
                                            HttpServletResponse response,
                                            String toPath){// 前端传来需要前往的路径
        User user = (User)request.getSession().getAttribute(Constant.user);

        if(toPath == null || toPath.isBlank() || userFileListMapper.isPathExist(user.getUid(), toPath) == 0)
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);

        request.getSession().setAttribute(Constant.userPath, toPath);

        return new ResultMessage<>(Message.SUCCESS, Message.changePathSuccess, null);
    }

    @RequestMapping("/getUserFolderList")
    public ResultMessage<UserFileList[]> getUserFolderList(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   String savePath){
        User user = (User)request.getSession().getAttribute(Constant.user);

        if(savePath == null || savePath.isBlank() || (userFileListMapper.isPathExist(user.getUid(), savePath) == 0 && !"/".equals(savePath)))
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);

        UserFileList[] userFileLists = userFileListMapper.getUserFolders(user.getUid(), savePath);

        return new ResultMessage<>(Message.SUCCESS, Message.getUserFolderListSuccess, userFileLists);
    }

    @RequestMapping("/toFolder")
    public ResultMessage<String> toFolder(HttpServletRequest request,
                                        HttpServletResponse response,
                                        String savePath){// 前端传来需要前往的路径
        User user = (User)request.getSession().getAttribute(Constant.user);

        if(savePath == null || savePath.isBlank() || userFileListMapper.isPathExist(user.getUid(), savePath) == 0)
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);

        return new ResultMessage<>(Message.SUCCESS, Message.changePathSuccess, null);
    }

    /**
     * TODO 文件预览 使用第三方预览
     *
     * @param request request
     * @param f_name f_name
     * @return ResultMessage<String>
     * @Author ddwl.
     * @Date 2023/2/4 14:05
    **/
    @RequestMapping("/preview")
    public ResultMessage<String> preview(HttpServletRequest request,
                                         HttpServletResponse response,
                                         String f_name){
        User user = (User)request.getSession().getAttribute(Constant.user);
        String userPath = (String)request.getSession().getAttribute(Constant.userPath);
        if(f_name == null || f_name.isBlank())
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);

        // 用户文件
        UserFileList userFileList = userFileListMapper.getUserFileInfo(user.getUid(), f_name, userPath);
        if(userFileList == null)
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);

        CustomFile customFile = fileMapper.getFileInfoByFid(userFileList.getFid());
        if(customFile == null)
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);
        // Files类实现文件复制
        String randomName = null;
        try {
            // 源文件 目标文件 预览文件长度设定为小于64 定期删除
            randomName = PreviewImageUtil.createRandomName(32) + FileUtil.getFileSuffix(customFile.getF_name());
            File src_file = new File(Constant.sys_storage_path + customFile.getF_name());
            File dest_file = new File(Constant.sys_preview_path + randomName);
            if(!dest_file.exists()) {
                // 前端video标签播放的视频要使用h264编码 否则只有声音没有画面
                // TODO： (1)、转换视频编码速度太慢 (2)、暂时只支持mp4视频文件
                // 返回数据流依然没有画面 看来必须得转码？？
//                if (f_name.endsWith(".mp4"))
//                    FileUtil.convertMP4EncodeType(src_file, dest_file);
//                else
                Files.copy(src_file.toPath(), dest_file.toPath());
            }

        } catch (IOException e) {
            e.printStackTrace();
            return new ResultMessage<>(Message.ERROR, Message.unknownError, null);
        }

        return new ResultMessage<>(Message.SUCCESS, "", randomName);
    }



    @RequestMapping("/copy")
    public ResultMessage<String> copy(HttpServletRequest request,
                                      HttpServletResponse response,
                                      String f_name){
        User user = (User)request.getSession().getAttribute(Constant.user);
        String userPath = (String)request.getSession().getAttribute(Constant.userPath);

        UserFileList userFileList = userFileListMapper.getUserFileInfo(user.getUid(), f_name, userPath);
        ClipBoard clipBoard = new ClipBoard(0, userFileList);
        // 文件信息存入粘贴板
        request.getSession().setAttribute(Constant.clipBoard, clipBoard);

        return new ResultMessage<>(Message.SUCCESS, Message.copyFileSuccess, null);
    }


    @RequestMapping("/cut")
    public ResultMessage<String> cut(HttpServletRequest request,
                                      HttpServletResponse response,
                                      String f_name){
        User user = (User)request.getSession().getAttribute(Constant.user);
        String userPath = (String)request.getSession().getAttribute(Constant.userPath);

        UserFileList userFileList = userFileListMapper.getUserFileInfo(user.getUid(), f_name, userPath);
        ClipBoard clipBoard = new ClipBoard(1, userFileList);
        // 文件信息存入粘贴板
        request.getSession().setAttribute(Constant.clipBoard, clipBoard);

        return new ResultMessage<>(Message.SUCCESS, Message.cutFileSuccess, null);
    }

    @RequestMapping("/paste")
    public ResultMessage<String> copyTo(HttpServletRequest request,
                                     HttpServletResponse response){
        User user = (User)request.getSession().getAttribute(Constant.user);
        String userPath = (String)request.getSession().getAttribute(Constant.userPath);

        ClipBoard clipBoard = (ClipBoard)request.getSession().getAttribute(Constant.clipBoard);
        return userBehaviorService.paste(user, userPath, clipBoard);
    }


    @RequestMapping("/resetUserPath")
    public void resetUserPath(HttpServletRequest request){
        request.getSession().setAttribute(Constant.userPath, Constant.user_root_path);
    }

    @RequestMapping("/getUserInfo")
    public ResultMessage<User> getUserInfo(HttpServletRequest request){
        User userInfo = (User)request.getSession().getAttribute(Constant.user);
        if(userInfo == null)
            return new ResultMessage<>(Message.ERROR, Message.unknownError, null);
        userInfo.setU_password(null);

        return new ResultMessage<>(Message.SUCCESS, Message.getUserInfoSuccess, userInfo);
    }


    // 没效果 还是没有画面
    @RequestMapping("/playVideo/{videoName}")
    public ResultMessage<String> playVideo(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @PathVariable("videoName") String videoName){
        //获取视频文件流
        try (OutputStream outputStream = response.getOutputStream(); FileInputStream fileInputStream = new FileInputStream(new File(Constant.sys_preview_path + videoName))) {
            byte[] cache = new byte[1024];
            response.setHeader(HttpHeaders.CONTENT_TYPE, "video/mp4");
            response.setHeader(HttpHeaders.CONTENT_LENGTH, fileInputStream.available() + "");
            int flag;
            while ((flag = fileInputStream.read(cache)) != -1) {
                outputStream.write(cache, 0, flag);
            }
            outputStream.flush();
        } catch (Exception e) {
            throw new RuntimeException("文件传输错误");
        }

        return new ResultMessage<>(Message.SUCCESS, "", null);
    }

    @RequestMapping("/updateUserInfo")
    public ResultMessage<String> updateUserInfo(HttpServletRequest request,
                                                HttpServletResponse response,
                                                User userInfo){
        User user = (User)request.getSession().getAttribute(Constant.user);
        User oldInfo = userMapper.getUserInfoByUserId(user.getUid());
        userInfo.setUid(user.getUid());
        userBehaviorService.updateUserInfo(userInfo, oldInfo);

        // 更新session中的信息
        request.getSession().setAttribute(Constant.user, userMapper.getUserInfoByUserId(user.getUid()));

        return new ResultMessage<>(Message.SUCCESS, Message.updateUserInfoSuccess, null);
    }

    @RequestMapping("/comment")
    public ResultMessage<String> comment(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Comment comment){
        User user = (User)request.getSession().getAttribute(Constant.user);
        comment.setUid(user.getUid());
        comment.setReply_to(-1);
        comment.setBelong_to(-1);
        comment.setReply_nike_name("");
        comment.setNike_name(user.getNike_name());
        comment.setAvatar_url(user.getAvatar_url());
        comment.setComment_date(DateUtil.getFormatDate());

        commentMapper.insertComment(comment);


        return new ResultMessage<>(Message.SUCCESS, Message.commentSuccess, null);
    }

    @RequestMapping("/reply")
    public ResultMessage<String> reply(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Comment comment){
        User user = (User)request.getSession().getAttribute(Constant.user);

        if(comment.getReply_to() > 0) {
            User reply_user = userMapper.getUserInfoByUserId(comment.getReply_to());
            comment.setReply_nike_name(reply_user.getNike_name());
        }
        comment.setUid(user.getUid());
        comment.setNike_name(user.getNike_name());
        comment.setAvatar_url(user.getAvatar_url());
        comment.setComment_date(DateUtil.getFormatDate());

        commentMapper.insertComment(comment);

        commentMapper.updateReplyNumber(comment.getBelong_to());

        return new ResultMessage<>(Message.SUCCESS, Message.replySuccess, null);
    }

    @RequestMapping("/getComment")
    public ResultMessage<List<Comment>> getComment(HttpServletRequest request,
                                       HttpServletResponse response,
                                       int fid){
        User user = (User)request.getSession().getAttribute(Constant.user);
        int uid = user.getUid();


        // 获得当前文件的所有评论
        Comment[] allComments = commentMapper.getComment(fid);

        // 筛选出所有评论
        List<Comment> comments = Arrays.stream(allComments)
                .filter(comment -> comment.getBelong_to() == -1)
                .collect(Collectors.toList());

        // 筛选出所有评论对应的回复
        comments.forEach(comment -> {
            comment.setReplies(Arrays.stream(allComments)
                    .filter(item -> item.getBelong_to() == comment.getCid()).toArray(Comment[]::new));
        });

        List<Star> stars = Arrays.asList(starMapper.getUserStars(uid));
        comments.forEach(comment -> {
            boolean hasStar = stars.stream()
                    .anyMatch(star -> star.getCid() == comment.getCid());
            comment.set_star(hasStar);

            Comment[] replies = comment.getReplies();
            if (replies != null) {
                Arrays.stream(replies).forEach(reply -> {
                    boolean hasStarReply = stars.stream()
                            .anyMatch(star -> star.getCid() == reply.getCid());
                    reply.set_star(hasStarReply);
                });
            }
        });

        return new ResultMessage<>(Message.SUCCESS, Message.getCommentSuccess, comments);
    }

    @RequestMapping("/starComment")
    public ResultMessage<String> starComment(HttpServletRequest request,
                                             HttpServletResponse response,
                                             int cid){
        User user = (User)request.getSession().getAttribute(Constant.user);
        int uid = user.getUid();
        Star star = starMapper.getUserStar(uid, cid);
        if(star == null) {
            star = new Star();
            star.setCid(cid);
            star.setUid(uid);
            // 插入新的点赞和更新点赞数
            commentMapper.starComment(cid);
            starMapper.insertStar(star);
        }
        else{
            // 取消点赞和更新点赞数
            commentMapper.cancelStarComment(cid);
            starMapper.cancelStar(uid, cid);
        }

        return new ResultMessage<>(Message.SUCCESS, Message.starSuccess, null);
    }

    @RequestMapping("/getMark")
    public ResultMessage<Integer> mark(HttpServletRequest request,
                                         HttpServletResponse response,
                                         int fid){
        User user = (User)request.getSession().getAttribute(Constant.user);
        int uid = user.getUid();
        Mark mark = markMapper.getMark(uid, fid);
        int score = mark == null ? 0 : mark.getScore();

        return new ResultMessage<>(Message.SUCCESS, "", score);
    }

    @RequestMapping("/mark")
    public ResultMessage<String> mark(HttpServletRequest request,
                                      HttpServletResponse response,
                                      int fid,
                                      int score){
        User user = (User)request.getSession().getAttribute(Constant.user);
        int uid = user.getUid();
        PublicFile publicFile = publicFileMapper.getPublicFileInfoByFid(fid);

        Mark[] allMark = markMapper.getMarkNumber(fid);

        Mark old_mark = markMapper.getMark(uid, fid);

        if(old_mark == null){
            old_mark = new Mark();
            // 第一次评分 直接添加进去
            old_mark.setFid(fid);
            old_mark.setUid(uid);
            markMapper.insertMark(old_mark);
            old_mark = markMapper.getMark(uid, fid);
            allMark = markMapper.getMarkNumber(fid);
        }

        final Mark t =  old_mark;
        allMark = Arrays.stream(allMark)
                .peek(m -> m.setScore(m.getMid() == t.getMid() ? score : m.getScore()))
                .toArray(Mark[]::new);

        old_mark.setScore(score);

        // 计算得到新的平均分
        float avgScore = (float) Arrays.stream(allMark)
                .mapToDouble(Mark::getScore)
                .average()
                .orElse(0.0);
        // 设置新的评分
        publicFile.setScore(avgScore);

        // 更新文件评分和用户评分
        publicFileMapper.updatePublicFileInfo(publicFile);
        markMapper.updateMark(old_mark);

        return new ResultMessage<>(Message.SUCCESS, "", null);
    }

    @RequestMapping("/deleteComment")
    public ResultMessage<String> deleteComment(HttpServletRequest request,
                                               HttpServletResponse response,
                                               int cid){
        User user = (User)request.getSession().getAttribute(Constant.user);
        int uid = user.getUid();

        Comment comment = commentMapper.getCommentByCid(cid);
        Comment[] comments = commentMapper.getCommentBelongTo(cid);
        if(comment != null && comment.getUid() == uid) {
            starMapper.deleteStar(cid);

            // 删除回复和评论
            for (Comment comment1 : comments) {
                commentMapper.deleteComment(comment1.getCid());
            }
            commentMapper.deleteComment(cid);
            return new ResultMessage<>(Message.SUCCESS, "success", null);
        }
        return new ResultMessage<>(Message.ERROR, "error", null);
    }

}
