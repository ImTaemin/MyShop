package com.myshop.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer{

    // 뷰를 리액트가 담당하기 때문에 디폴트 뷰 리졸버 등록
    @Bean
    public InternalResourceViewResolver defaultViewResolver() {
        return new InternalResourceViewResolver();
    }

//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        WebMvcConfigurer.super.addFormatters(registry);
//        registry.addConverter(new OrderStatusConverter());
//    }
}
