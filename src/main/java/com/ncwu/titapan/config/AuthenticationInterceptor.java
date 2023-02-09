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
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // session有用户信息不需要验证

        User user = new User();
        // 验证token
        String token = request.getHeader("token");
        // 放行OPTION请求
        if(token == null && HttpMethod.OPTIONS.toString().equals(request.getMethod())){
            return true;
        }
        else if(token == null || token.isBlank()){
            response.setHeader("Data", JSONObject.toJSONString(new ResultMessage<String>(Message.WARNING, Message.invalidToken, null)));
            return false;
        }
        DecodedJWT verify;
        // 捕获token无效异常 并返回错误信息
        try {
            verify = TokenUtils.verify(token, Constant.KEY);
            if(verify == null){
                response.setHeader("Data", JSONObject.toJSONString(new ResultMessage<String>(Message.WARNING, Message.invalidToken, null)));
                return false;
            }

            user = userMapper.getUserInfoByUserId(verify.getClaim("uid").asInt());
            if(user == null){
                response.setHeader("Data", JSONObject.toJSONString(new ResultMessage<String>(Message.WARNING, Message.invalidToken, null)));
                return false;
            }
        }catch(Exception e){
            System.out.println("token失效异常: " + e.getLocalizedMessage());
            e.printStackTrace();
            response.setHeader("Data", JSONObject.toJSONString(new ResultMessage<String>(Message.WARNING, Message.invalidToken, null)));
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
        // 将user加入session
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
}
