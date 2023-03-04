package com.myshop.api.config.security;

public final class Constant {

    // 모두 허용
    public static final String[] permitAllArray = new String[] {
            "/*/sign-up",
            "/*/sign-in",
            "/*/exists/**",
            "/customer/order/kakao/approval",
            "/customer/order/kakao/cancel",
            "/customer/order/kakao/fail"
    };
    
    // 인증, 인가 예외
    public static final String[] ignoreArray = new String[]{
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v2/api-docs"
    };
}
