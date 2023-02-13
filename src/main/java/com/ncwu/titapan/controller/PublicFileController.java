package com.ncwu.titapan.controller;

import com.ncwu.titapan.constant.Message;
import com.ncwu.titapan.mapper.PublicFileMapper;
import com.ncwu.titapan.pojo.PublicFile;
import com.ncwu.titapan.pojo.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private PublicFileMapper publicFileMapper;

    @RequestMapping("/getPublicFileList")
    public ResultMessage<Map<String, Object>> getPublicFileList(HttpServletRequest request,
                                                HttpServletResponse response,
                                                int index, int count){
        if(ObjectUtils.isEmpty(index) || ObjectUtils.isEmpty(count))
            return new ResultMessage<>(Message.ERROR, Message.dataFormatError, null);

        PublicFile[] publicFiles = publicFileMapper.getPublicFileList((index-1)*count, count);

        int totalPages = (int) Math.ceil(((double)publicFileMapper.getPublicFileCount() / (double)count));
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("publicFileList", publicFiles);
        resultMap.put("totalPages", totalPages);
        resultMap.put("currentPage", index);
        resultMap.put("showNumber", count);
        return new ResultMessage<>(Message.SUCCESS, Message.getPublicFileListSuccess, resultMap);
    }




}
