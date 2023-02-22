package com.myshop.api.config.security.filter;

import com.myshop.api.config.security.JwtTokenProvider;
import io.jsonwebtoken.JwtException;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JwtRefreshTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final Logger LOGGER = LoggerFactory.getLogger(JwtRefreshTokenFilter.class);

    public JwtRefreshTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = jwtTokenProvider.resolveToken(request);
            LOGGER.info("token 값 추출 완료 : {}", token);

            List<String> allowUrl = new ArrayList<>();
            allowUrl.add("/provider/reissue");
            allowUrl.add("/customer/reissue");

            if(!allowUrl.contains(request.getRequestURI())) {
                filterChain.doFilter(request, response);
                return;
            }

            // 유효성 검사. 리프레시 토큰은 "/provider/reissue" or "/customer/reissue" 에서만 사용 가능
            if(token != null && jwtTokenProvider.validateRefreshToken(token)) {
                LOGGER.info("refresh token 검증 완료");
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtException | IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 유효하지 않습니다.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
