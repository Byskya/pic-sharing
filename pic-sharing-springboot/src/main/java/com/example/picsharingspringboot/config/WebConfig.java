package com.example.picsharingspringboot.config;

import com.example.picsharingspringboot.intercepter.WebSocketInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
    public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(new WebSocketInterceptor()).addPathPatterns("/**").excludePathPatterns("/UserChat/**");
    }
}
