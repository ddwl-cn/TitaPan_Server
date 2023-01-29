package com.ncwu.titapan.config;

import org.springframework.boot.autoconfigure.session.DefaultCookieSerializerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableSpringHttpSession
public class SessionConfig {
    @Bean
    public SessionRepository sessionRepository() {
        return new MapSessionRepository(new ConcurrentHashMap<>());
    }
/*
跨域请求要设置sameSite属性为none 不然set-cookie会被谷歌浏览器阻止
*/

    @Bean
    DefaultCookieSerializerCustomizer cookieSerializerCustomizer() {
        return cookieSerializer -> {
            cookieSerializer.setSameSite("None");
            cookieSerializer.setUseSecureCookie(true); // 此项必须，否则set-cookie会被chrome浏览器阻拦
        };
    }
}
