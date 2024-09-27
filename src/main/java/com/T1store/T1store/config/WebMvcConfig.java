package com.T1store.T1store.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${uploadPath}") //application.properties에 설정한 uploadPath
    String uploadPath;
    //uploadPath = "C:/JavaBackFront_MSA/shop
    //images/item/xxx.jpg


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // '/images'로 시작하는 경우 uploadPath에 설정한 폴더를 기준으로 파일 읽도록 설정
        // 웹 페이지 url에 실제 경로를 보여주지 않을 수 있음
        registry.addResourceHandler("images/**")
                .addResourceLocations(uploadPath); //로컬에서 root 경로를 설정(실제 경로)

    }
}
