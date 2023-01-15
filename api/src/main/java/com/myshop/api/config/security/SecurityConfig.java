package com.myshop.api.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://provider.myshop.com", "http://myshop.com"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.httpBasic(AbstractHttpConfigurer::disable) //UI 시큐리티 설정 비활성화
                /*
                 * 스프링 시큐리티의 csrf()는 CSRF 토큰을 발급해서 클라이언트로부터
                 * 요청을 받을 때마다 토큰을 검증하기 때문에 비활성화해도 큰 문제가 되지 않는다.
                 */
                .csrf(AbstractHttpConfigurer::disable)
                .cors()
                .configurationSource(corsConfigurationSource())

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 상단부터 차례차례 적용됨(순서 중요!)
                .and()
                .authorizeRequests()
                // TODO 적용 했는데 권한이 없어도 접근이 된다... UI 완료 후 확인
                .antMatchers(HttpMethod.POST, "/item").hasRole("PROVIDER")
                .antMatchers(HttpMethod.GET, "/auth/provider").hasRole("PROVIDER")
                .antMatchers(Constant.permitAllArray).permitAll() //부분 허용
                .antMatchers(HttpMethod.GET, "/**").permitAll() //GET 모두 허용
                .antMatchers("**exception**").permitAll()
                // 기타 요청은 인증된 권한을 가진 사용자만
//                .anyRequest()
//                .hasAnyRole() // 인증된 권한. 인자에 경로 넣으면 됨

                .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())

                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())

                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 인증 인가가 적용되지 않는 리소스
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(Constant.ignoreArray); // 스웨거 인증, 인가 무시
    }

}