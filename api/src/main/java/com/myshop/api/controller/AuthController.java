package com.myshop.api.controller;

import com.myshop.api.domain.dto.response.BaseResponse;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"공통 REST API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/exception")
    public ResponseEntity<BaseResponse> authException() {
        return BaseResponse.error("접근이 금지되었습니다");
    }

}
