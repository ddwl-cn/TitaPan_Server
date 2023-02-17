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
    // TODO 文件提取身份验证有问题，描述如下：
    //    如果在http://127.0.0.1:8081/#/ 下已经登录localstorage已经存储有用户token的情况下
    //    此时同一浏览器下手动打开新的标签页访问文件提取链接 会出现登录失效的情况。
    //    经排查发现当前标签下localstorage中并没有存储token，请求携带的token为null于是出现请求被拦截的情况。
    //    上述情况可能是同源不同路径localstorage数据不共享导致。
    //    经过测试如果此时继续在当前标签进行登录后，继续手动打开新的标签页访问文件提取链接，却可以正常访问，出乎意料。
    //    推测有可能是同源的每一个不同路径下的localstorage均各自独立
    //    因为localstorage中的数据不共享 所以导致同源网站却需要登录两次........
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // session有用户信息不需要验证
        System.out.println("-----------------------------------------------------");
        System.out.println("debug-1");
        User user = new User();
        // 验证token
        String token = request.getHeader("token");
        // 放行OPTION请求
        if(token == null && HttpMethod.OPTIONS.toString().equals(request.getMethod())){
            System.out.println("debug-2");
            return true;
        }
        else if(token == null || token.isBlank()){
            System.out.println("debug-3");
            response.setHeader("Data", JSONObject.toJSONString(new ResultMessage<String>(Message.WARNING, Message.invalidToken, null)));
            return false;
        }
        DecodedJWT verify;
        // 捕获token无效异常 并返回错误信息
        try {
            verify = TokenUtils.verify(token, Constant.KEY);
            if(verify == null){
                System.out.println("debug-4");
                response.setHeader("Data", JSONObject.toJSONString(new ResultMessage<String>(Message.WARNING, Message.invalidToken, null)));
                return false;
            }

            user = userMapper.getUserInfoByUserId(verify.getClaim("uid").asInt());
            if(user == null){
                System.out.println("debug-5");
                response.setHeader("Data", JSONObject.toJSONString(new ResultMessage<String>(Message.WARNING, Message.invalidToken, null)));
                return false;
            }
        }catch(Exception e){
            System.out.println("debug-6");
            System.out.println("token失效异常: " + e.getLocalizedMessage());
            e.printStackTrace();
            response.setHeader("Data", JSONObject.toJSONString(new ResultMessage<String>(Message.WARNING, Message.invalidToken, null)));
            return false;
        }
        Token userToken = tokenMapper.getUserToken(user.getUid(), token);
        if(userToken == null){
            System.out.println("debug-7");
            response.setHeader("Data", JSONObject.toJSONString(new ResultMessage<String>(Message.WARNING, Message.invalidToken, null)));
            return false;
        }

        // 是否已经建立了会话 如果是直接返回true
        if(request.getSession().getAttribute(Constant.user) != null){
            System.out.println("debug-8");
            return true;
        }
        System.out.println("debug-9");
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
