package com.ncwu.titapan.controller;

import com.ncwu.titapan.constant.Constant;
import com.ncwu.titapan.constant.Message;
import com.ncwu.titapan.mapper.FileMapper;
import com.ncwu.titapan.pojo.*;
import com.ncwu.titapan.utils.DateUtil;
import com.ncwu.titapan.utils.FileUtil;
import com.ncwu.titapan.utils.PreviewImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/2/13 13:46
 */
@RestController
@RequestMapping("/public")
public class PublicFileController {
    @Autowired
    private FileMapper fileMapper;

    @RequestMapping("/getPublicFileList")
    public ResultMessage<Map<String, Object>> getPublicFileList(HttpServletRequest request,
                                                HttpServletResponse response,
                                                int index, int count){
        if(ObjectUtils.isEmpty(index) || ObjectUtils.isEmpty(count))
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);

        CustomFile[] publicFiles = fileMapper.getPublicFileList((index-1)*count, count);

        int totalPages = (int) Math.ceil(((double)fileMapper.getPublicFileCount() / (double)count));

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("publicFileList", publicFiles);
        resultMap.put("totalPages", totalPages);
        resultMap.put("currentPage", index);
        resultMap.put("showNumber", count);

        return new ResultMessage<>(Message.SUCCESS, Message.getPublicFileListSuccess, resultMap);
    }

    // 上传公共文件 暂时不使用分块上传和快传
    // TODO 公共文件上传计划从另一个客户端进行上传，不在此客户端
    @RequestMapping("/uploadPublicFile")
    public ResultMessage<Map<String, Object>> uploadPublicFile(HttpServletRequest request,
                                                                HttpServletResponse response,
                                                                FileChunk fileChunk,
                                                               String f_description){
        // 数据无效返回错误信息
        if (fileChunk == null || fileChunk.getMFile() == null || fileChunk.getMFile().getSize() <= 0 || fileChunk.getTotal() <= 0)
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);
        // 获得用户实体
        User user = (User) request.getSession().getAttribute(Constant.user);
        // 是一个文件
        if (fileChunk.getTotal() == 1) {
            try {
                // 直接把文件保存
                fileChunk.getMFile().transferTo(new File(Constant.sys_public_file_path + fileChunk.getId()+fileChunk.getSuffix()));
                CustomFile cFile = new CustomFile();
                cFile.setF_name(fileChunk.getOriginal_file_name());
                cFile.setMd5_val(fileChunk.getId());
                cFile.setF_size(fileChunk.getTotalSize());
                cFile.setStorage_path(Constant.sys_public_file_path);
                cFile.setUpload_date(DateUtil.getFormatDate());
                cFile.setPublic_file(true);
                cFile.setF_description(f_description);

                String preview_url = null;
                if(FileUtil.isPic(fileChunk.getSuffix())) {
                    // 生成图片预览地址
                    preview_url = PreviewImageUtil.get_preview_pic_url(new File(cFile.getStorage_path() + fileChunk.getId() + fileChunk.getSuffix()));
                }
                else if(FileUtil.isVedio(fileChunk.getSuffix())){
                    // 获取视频第一张图片
                    String framePath = Constant.preview_image_path+PreviewImageUtil.createRandomName(32)+".jpg";

                    PreviewImageUtil.getVideoFirstFrame(new File(cFile.getStorage_path() + fileChunk.getId() + fileChunk.getSuffix()),
                            framePath);
                    // 生成图片预览地址
                    File frameFile = new File(framePath);

                    preview_url = PreviewImageUtil.get_preview_pic_url(frameFile);
                    frameFile.delete();
                }

                cFile.setPreview_url(preview_url);
                fileMapper.insertPublicFile(cFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);
        }
        return new ResultMessage<>(Message.SUCCESS, Message.uploadComplete, null);
    }



}
