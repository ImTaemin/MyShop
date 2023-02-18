package com.myshop.api.config.security;

import com.myshop.api.enumeration.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://provider.myshop.com", "http://myshop.com", "http://localhost:3000"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("X-AUTH-TOKEN", "Content-Type", "Access-Control-Allow-Headers"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .cors()
                .and()
                .authorizeRequests()
                // TODO 적용 했는데 권한이 없어도 접근이 된다... UI 완료 후 확인
                .antMatchers(Constant.permitAllArray).permitAll() //부분 허용
                .antMatchers(HttpMethod.POST, "/item").hasRole(UserRole.PROVIDER.toString())
                .antMatchers(HttpMethod.GET, "/provider").hasRole(UserRole.PROVIDER.toString())
                .antMatchers(HttpMethod.GET, "/customer").hasRole(UserRole.CUSTOMER.toString())
                .antMatchers(HttpMethod.GET, "/**").permitAll() //GET 모두 허용
                .antMatchers("**exception**").permitAll()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())

                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())

                // 기타 요청은 인증된 권한을 가진 사용자만
//                .anyRequest()
//                .hasAnyRole() // 인증된 권한. 인자에 경로 넣으면 됨

                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);
    }

}