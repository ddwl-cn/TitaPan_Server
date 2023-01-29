package com.ncwu.titapan.controller;

import com.ncwu.titapan.constant.Message;
import com.ncwu.titapan.pojo.ResultMessage;
import com.ncwu.titapan.pojo.User;
import com.ncwu.titapan.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/5 21:35
 */
@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;

    @RequestMapping("/login")
    public ResultMessage<String> login(HttpServletRequest request,
                                      HttpServletResponse response,
                                      User userInfo){

        if(accountService.login(request, response, userInfo)) {
            return new ResultMessage<>(Message.SUCCESS, Message.loginSuccess, null);
        }
        return new ResultMessage<>(Message.WARNING, Message.userInfoError, null);
    }

    @RequestMapping("/registry")
    public ResultMessage<String> registry(HttpServletRequest request,
                                          HttpServletResponse response,
                                          User userInfo){

        if(accountService.registry(request, response, userInfo)){
            return new ResultMessage<>(Message.SUCCESS, Message.registrySuccess, null);
        }
        return new ResultMessage<>(Message.SUCCESS, Message.userExist, null);
    }

}
