package com.ncwu.titapan.utils;

import com.ncwu.titapan.constant.Constant;
import com.ncwu.titapan.pojo.User;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/6 17:36
 */
public class TokenUtils {
    /**
     * 获取token
     * @param user user
     * @return token
     */
    public static String getToken(User user) {
        Calendar instance = Calendar.getInstance();
        //默认令牌过期时间7天
        instance.add(Calendar.DATE, 7);

        JWTCreator.Builder builder = JWT.create();
        // 存放自定义信息
        builder.withClaim("uid", user.getUid())
                .withClaim("u_name", user.getU_name())
                .withClaim(Constant.userPath, Constant.user_root_path);

        return builder.withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(Constant.KEY));// (签名后返回token)
    }

    /**
     * 验证token合法性 成功返回token
     */
    public static DecodedJWT verify(String token, String password) {
        JWTVerifier build = JWT.require(Algorithm.HMAC256(password)).build();
        return build.verify(token);
    }

    public static void main(String[] args) {
        User user = new User(1, "tom", "123456",0);
        String token = getToken(user);
        System.out.println(token);
        DecodedJWT verify = verify(token, Constant.KEY);
        System.out.println(verify.getClaim("u_name").asString());
    }

}
