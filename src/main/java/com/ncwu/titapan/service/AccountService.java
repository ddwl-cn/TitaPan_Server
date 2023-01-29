package com.ncwu.titapan.service;

import com.ncwu.titapan.pojo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/5 22:16
 */
public interface AccountService {
    boolean login(HttpServletRequest request, HttpServletResponse response, User user);

    boolean registry(HttpServletRequest request, HttpServletResponse response, User user);
}
