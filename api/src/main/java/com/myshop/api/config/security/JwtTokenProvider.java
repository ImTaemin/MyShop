package com.myshop.api.config.security;

import com.myshop.api.enumeration.RoleType;
import com.myshop.api.enumeration.TokenType;
import com.myshop.api.exception.UserNotFoundException;
import com.myshop.api.service.CustomerService;
import com.myshop.api.service.ProviderService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final ProviderService providerService;
    private final CustomerService customerService;

    private final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("myshop.jwt.secretkey")
    private String secretKey;
    private final long ACCESS_TOKEN_VALID_MS = 1000 * 60 * 60; // 1시간
    private final int REFRESH_TOKEN_VALID_DAY = 30; // 30일

    // 빈 객체로 주입된 이후 수행되는 메서드
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // 토큰 생성
    public String createAccessToken(UserDetails userDetails) {
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("roles", userDetails.getAuthorities());
        claims.put("type", TokenType.ACCESS.toString());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + ACCESS_TOKEN_VALID_MS);

        return "Bearer " + Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken(UserDetails userDetails) {
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("roles", userDetails.getAuthorities());
        claims.put("type", TokenType.REFRESH.toString());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plusDays(REFRESH_TOKEN_VALID_DAY);

        return "Bearer " + Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(expiryDate.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    // 인증이 성공했을 때 SecurityContextHolder에 저장할 Authentication을 생성하는 역할
    // 토큰 인증 정보 조회
    public Authentication getAuthentication(String token) {
        String role = getRole(token);
        String userId = getUserId(token);

        UserDetails userDetails = null;
        if(role.equals(RoleType.PROVIDER.toString())) {
            // 판매자
            userDetails = providerService.getProviderByUserId(userId);
        } else if(role.equals(RoleType.CUSTOMER.toString())) {
            // 구매자
            userDetails = customerService.getCustomerByUserId(userId);
        }

        if (userDetails == null) {
            throw new UserNotFoundException();
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

    private String getRole(String token) {
        ArrayList<LinkedHashMap<String, String>> roles = (ArrayList<LinkedHashMap<String, String>>) Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("roles");

        LOGGER.info("권한 정보 : " + roles);

        return roles.get(0).get("authority");
    }

    private TokenType getTokenType(String token) {
        TokenType tokenType = TokenType.valueOf(((String) Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("type")));

        LOGGER.info("토큰 타입 : {}", tokenType);

        return tokenType;
    }

    // HTTP 헤더에서 토큰 값 조회
    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("X-AUTH-TOKEN");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }

        return null;
    }

    // 엑세스 토큰 유효성 검사
    public boolean validateAccessToken(String accessToken) {
        try {
            // 만료된 토큰이면 여기서 에러발생
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken);
            
            if(getTokenType(accessToken) == TokenType.ACCESS) {
                return true;
            }
        } catch (JwtException | IllegalArgumentException e) {
            throw e;
        }

        return false;
    }

    // 리프레시 토큰 유효성 검사
    public boolean validateRefreshToken(String refreshToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(refreshToken);
            if(getTokenType(refreshToken) == TokenType.REFRESH) {
                return true;
            }
        } catch (JwtException | IllegalArgumentException e) {
            throw e;
        }

        return false;
    }

}
