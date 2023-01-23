package com.myshop.api.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * GenericFilter: 기존 필터에서 가져올 수 없는 스프링의 설정 정보를 가져울 수 있게 확장된 추상 클래스
 * GenericFilterBean을 사용해도 되지만
 * 서블릿은 사용자의 요청을 받으면 서블릿을 생성해 메모리에 저장해두고
 * 동일한 클라이언트의 요청을 받으면 재활용하는 구조여서
 * RequestDispatcher에 의해 다른 서블릿으로 디스패치 되면서 필터가 두 번 실행되는 현상이 발생할 수 있다.
 *
 * 그래서 나온 것이 OncePerRequestFilter
 */

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);
        LOGGER.info("token 값 추출 완료 : {}", token);

        // 토큰 값 유효성 검사
        if(token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}