package com.myshop.api.config.security;

import com.myshop.api.config.security.filter.JwtAuthenticationFilter;
import com.myshop.api.config.security.filter.JwtRefreshTokenFilter;
import com.myshop.api.enumeration.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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
        corsConfiguration.setAllowedOrigins(List.of("http://provider.myshop.r-e.kr", "http://myshop.r-e.kr", "https://localhost:3000", "https://localhost:3001"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("X-AUTH-TOKEN", "Content-Type", "Access-Control-Allow-Headers", "Access-Control-Allow-Origin"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }

    /**
     * 권장되지 않는 방법이라고 해서 configure 메서드에 추가함.
     */
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring()
//                .antMatchers(Constant.permitAllArray)
//                .antMatchers(Constant.ignoreArray);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .cors()

                .and()
                .authorizeRequests()
                .antMatchers(Constant.permitAllArray).permitAll()
                .antMatchers(Constant.ignoreArray).permitAll()
                .antMatchers(HttpMethod.POST, "/item/**").hasAuthority(UserRole.PROVIDER.toString())
                .antMatchers(HttpMethod.GET, "/provider/**").hasAuthority(UserRole.PROVIDER.toString())
                .antMatchers(HttpMethod.GET, "/customer/**").hasAuthority(UserRole.CUSTOMER.toString())
                .antMatchers(HttpMethod.GET).permitAll() //GET 모두 허용

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
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtRefreshTokenFilter(jwtTokenProvider),
                        JwtAuthenticationFilter.class);
    }

}