package com.myshop.api.config.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    // 접근이 막혔을 경우 경로 리다이렉트
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if(request.getRequestURI().contains("provider")) {
            response.sendRedirect("http://provider.myshop.com/auth/provider/auth/exception");
        }

        response.sendRedirect("http://myshop.com/auth/customer/sign-up");
    }
}
