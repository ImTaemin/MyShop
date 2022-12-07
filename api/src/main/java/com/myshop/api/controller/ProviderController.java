package com.myshop.api.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/provider")
public class ProviderController {

    @ApiOperation(value = "판매자 회원가입")
    @PostMapping("/test")
    public String test(){
        return "test";
    }

}
