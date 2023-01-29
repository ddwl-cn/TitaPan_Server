package com.ncwu.titapan.config;

import com.ncwu.titapan.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * TODO 类描述
 *
 * @author ddwl.
 * @date 2023/1/6 17:30
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Bean
    public AuthenticationInterceptor Authentication(){
        return new AuthenticationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册UserLoginInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(Authentication());
        registration.addPathPatterns("/**"); //所有路径都被拦截
        registration.excludePathPatterns(Constant.excludePath); // 设定放行路径

    }
}
