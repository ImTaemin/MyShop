package com.myshop.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * 빈의 모든 주입이 완료된 후에 @Value 어노테이션이 작동한다. (빈 초기화 중 값 사용x)
 * PropertySourcesPlaceholderConfigurer를 사용하면 런타임 시 속성 값을 확인할 수 있다.
 */
@Configuration
@PropertySource("classpath:/application-pay.yml")
public class AppConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
