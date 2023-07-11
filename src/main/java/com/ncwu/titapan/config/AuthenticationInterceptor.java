package com.ncwu.titapan.config;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ncwu.titapan.constant.Constant;
import com.ncwu.titapan.constant.Message;
import com.ncwu.titapan.mapper.TokenMapper;
import com.ncwu.titapan.mapper.UserMapper;
import com.ncwu.titapan.pojo.ResultMessage;
import com.ncwu.titapan.pojo.Token;
import com.ncwu.titapan.pojo.User;
import com.ncwu.titapan.utils.CookieUtil;
import com.ncwu.titapan.utils.DateUtil;
import com.ncwu.titapan.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/6 17:32
 */
@Component
public class AuthenticationInterceptor  implements HandlerInterceptor {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TokenMapper tokenMapper;
    // 调用接口前：前端请求需要认证
    // TODO '冷'知识：localhost与127.0.0.1不同源，localhost相当于域名，之前一直没了解过......
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // session有用户信息不需要验证
        User user;
        // 验证token
        String token = request.getHeader("token");
        // 放行OPTION请求
        if(token == null && HttpMethod.OPTIONS.toString().equals(request.getMethod())){
            return true;
        }
        else if(token == null || token.isBlank()){
            // response.setHeader("Data", JSONObject.toJSONString(new ResultMessage<String>(Message.WARNING, Message.invalidToken, null)));
            setReturn(request, response);
            return false;
        }
        DecodedJWT verify;
        // 捕获token无效异常 并返回错误信息
        try {
            verify = TokenUtils.verify(token, Constant.KEY);

            user = userMapper.getUserInfoByUserId(verify.getClaim("uid").asInt());
            if(user == null){
                // response.setHeader("Data", JSONObject.toJSONString(new ResultMessage<String>(Message.WARNING, Message.invalidToken, null)));
                setReturn(request, response);
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            // response.setHeader("Data", JSONObject.toJSONString(new ResultMessage<String>(Message.WARNING, Message.invalidToken, null)));
            setReturn(request, response);
            return false;
        }
        Token userToken = tokenMapper.getUserToken(user.getUid(), token);
        if(userToken == null){
            response.setHeader("Data", JSONObject.toJSONString(new ResultMessage<String>(Message.WARNING, Message.invalidToken, null)));
            return false;
        }

        // 是否已经建立了会话 如果是直接返回true
        if(request.getSession().getAttribute(Constant.user) != null){
            return true;
        }
        // 否则
        // 将user加入session(即用户第一次登录)
        request.getSession().setAttribute(Constant.user, user);
        // 设置用户默认路径
        request.getSession().setAttribute(Constant.userPath, Constant.user_root_path);
        // System.out.println("preHandle:" + new Date().getTime());

        return true;
    }

    // 调用接口后，返回数据前
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // System.out.println("postHandle:" + new Date().getTime());
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);

    }

    // 返回数据后
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // System.out.println("afterCompletion:" + new Date().getTime());
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }


    //返回错误信息
    private static void setReturn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        //UTF-8编码
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        String json = JSONObject.toJSONString(new ResultMessage<>(Message.WARNING, Message.invalidToken, null));
        response.getWriter().print(json);
    }
}
