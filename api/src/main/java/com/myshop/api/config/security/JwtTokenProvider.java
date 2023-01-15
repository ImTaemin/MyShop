package com.myshop.api.config.security;

import com.myshop.api.service.CustomerService;
import com.myshop.api.service.ProviderService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final ProviderService providerService;
    private final CustomerService customerService;

    private final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("spring.jwt.secretkey")
    private String secretKey;
    private final long tokenValidMillisecond = 1000L * 60 * 60; // 1시간

    // 빈 객체로 주입된 이후 수행되는 메서드
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // 토큰 생성
    public String createToken(String userId, Set<String> roles) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("roles", roles);

        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return token;
    }

    // 인증이 성공했을 때 SecurityContextHolder에 저장할 Authentication을 생성하는 역할
    // 토큰 인증 정보 조회
    public Authentication getAuthentication(String token) {
        // 판매자 구매자 구분
        char role = getRole(token);
        String userId = getUserId(token);

        UserDetails userDetails;
        if(role == 'P') {
            // 판매자
            userDetails = providerService.getProviderByUserId(userId);
        } else {
            // 구매자
            userDetails = customerService.getCustomerByUserId(userId);
        }

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

    }

    // 토큰 기반 회원 구별 정보 조회
    private String getUserId(String token) {
        String userId = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        LOGGER.info("로그인 아이디 : " + userId);

        return userId;
    }

    private char getRole(String token) {
        String roles = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("roles")
                .toString();

        LOGGER.info("권한 정보 : " + roles);

        // [PROVIDER, CUSTOMER] 이런 형식 중 앞 글자만
        return roles.charAt(1);
    }

    // HTTP 헤더에서 토큰 값 조회
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    // 토큰 유효기간 체크
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
