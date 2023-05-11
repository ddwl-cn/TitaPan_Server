package com.ncwu.titapan.controller;

import com.ncwu.titapan.constant.Message;
import com.ncwu.titapan.pojo.PublicFileTask;
import com.ncwu.titapan.pojo.ResultMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/5/11 11:27
 */
@RestController
public class TestController {
    @RequestMapping("/test")
    public ResultMessage<String> test(PublicFileTask publicFileTask){
        System.out.println(publicFileTask);

        return new ResultMessage<>(Message.SUCCESS, null, null);
    }
}
