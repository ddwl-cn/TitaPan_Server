package com.ncwu.titapan.service.impl;

import com.ncwu.titapan.constant.Constant;
import com.ncwu.titapan.mapper.TokenMapper;
import com.ncwu.titapan.mapper.UserMapper;
import com.ncwu.titapan.pojo.User;
import com.ncwu.titapan.service.AccountService;
import com.ncwu.titapan.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/5 22:17
 */
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TokenMapper tokenMapper;

    @Override
    public boolean login(HttpServletRequest request,
                         HttpServletResponse response,
                         User userInfo) {
        User user = userMapper.getUserInfoByUserName(userInfo.getU_name());
        System.out.println(user);
        if(user != null && user.getU_password().equals(userInfo.getU_password()) && user.getU_state() == 0 && userInfo.getType() == 0){
            // 登录成功 将用户实体放入session中
            request.getSession().setAttribute(Constant.user, user);
            // 添加用户所处路径
            request.getSession().setAttribute(Constant.userPath, Constant.user_root_path);
            // TODO 更改用户状态、添加token验证
            String token = TokenUtils.getToken(user);
            response.setHeader("token", token);
            // 存token到表中
            tokenMapper.insertNewToken(user.getUid(), token);
            return true;
        }
        return false;
    }

    @Override
    public boolean adminLogin(HttpServletRequest request,
                         HttpServletResponse response,
                         User userInfo) {
        User user = userMapper.getUserInfoByUserName(userInfo.getU_name());
        if(user != null && user.getU_password().equals(userInfo.getU_password()) && user.getU_state() == 0 && userInfo.getType() == 1){
            // 登录成功 将用户实体放入session中
            request.getSession().setAttribute(Constant.user, user);
            // 添加用户所处路径
            request.getSession().setAttribute(Constant.userPath, Constant.user_root_path);
            // TODO 更改用户状态、添加token验证
            String token = TokenUtils.getToken(user);
            response.setHeader("token", token);
            // 存token到表中
            tokenMapper.insertNewToken(user.getUid(), token);
            return true;
        }
        return false;
    }


    @Override
    public boolean registry(HttpServletRequest request, HttpServletResponse response, User userInfo) {
        User user = userMapper.getUserInfoByUserName(userInfo.getU_name());
        if(user == null){
            userMapper.insertNewUser(userInfo);
            return true;
        }
        return false;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        User user = (User)request.getSession().getAttribute(Constant.user);
        String token = request.getHeader("token");
        // 删除用户token
        tokenMapper.deleteToken(user.getUid(), token);

        request.getSession().removeAttribute(Constant.user);
        request.getSession().removeAttribute(Constant.userPath);
        request.getSession().invalidate();
    }
}
