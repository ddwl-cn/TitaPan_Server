package com.ncwu.titapan.config;

import org.springframework.boot.autoconfigure.session.DefaultCookieSerializerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

import java.util.Locale;
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
            cookieSerializer.setSameSite(isLinux() ? "Lax" : "None");// 设置为lax规范或者strict允许非安全连接（http） 保存cookie
            cookieSerializer.setUseHttpOnlyCookie(true);
            cookieSerializer.setUseSecureCookie(!isLinux()); // 此项必须，否则set-cookie会被chrome浏览器阻拦
        };
    }

    private boolean isLinux(){
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }
}
