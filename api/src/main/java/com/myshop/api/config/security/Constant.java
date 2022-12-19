package com.myshop.api.config.security;

public final class Constant {

    // 모두 허용
    public static final String[] permitAllArray = new String[] {
            "/auth/provider/sign-up",
            "/auth/provider/sign-in",
    };
    
    // 인증, 인가 예외
    public static final String[] ignoreArray = new String[]{
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v2/api-docs"
    };
}
