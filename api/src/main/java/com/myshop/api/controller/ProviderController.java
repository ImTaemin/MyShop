package com.myshop.api.controller;

import com.myshop.api.dto.SignInResultDto;
import com.myshop.api.dto.SignUpResultDto;
import com.myshop.api.dto.provider.ProviderDto;
import com.myshop.api.dto.provider.ProviderUpdateParam;
import com.myshop.api.service.ProviderService;
import com.myshop.api.service.SignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import java.util.Map;

@Api(tags = {"판매자 REST API"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth/provider")
public class ProviderController {

    private final ProviderService providerService;
    private final SignService signService;

    @ApiOperation(value = "판매처 입점")
    @PostMapping(value = "/sign-up", produces = "application/json;charset=UTF-8")
    public ResponseEntity<SignUpResultDto> signUp(@Valid @RequestBody ProviderDto providerDto) {
        SignUpResultDto signUpResultDto = signService.signUp(providerDto);

        return ResponseEntity.ok(signUpResultDto);
    }

    @ApiOperation(value = "판매처 로그인")
    @PostMapping(value = "/sign-in", produces = "application/json;charset=UTF-8")
    public ResponseEntity<SignInResultDto> signIn(@RequestBody Map<String, String> userInfo) throws AccountNotFoundException {
        SignInResultDto signInResultDto = signService.signIn(userInfo.get("loginId"), userInfo.get("password"));

        return ResponseEntity.ok(signInResultDto);
    }

    @ApiOperation(value = "브랜드 명 중복확인")
    @GetMapping("/{brandName}/exists")
    public ResponseEntity<Boolean> checkBrandName(@PathVariable String brandName) {
        return providerService.checkBrandName(brandName)
                ? ResponseEntity.ok(true)   //사용 가능
                : ResponseEntity.ok(false); //사용 불가
    }

    @ApiOperation("판매처 수정")
    @PutMapping(value = "/", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Boolean> modify(@Valid @RequestBody ProviderUpdateParam updateParam) {
        return providerService.modify(updateParam)
                ? ResponseEntity.ok(true)
                : ResponseEntity.badRequest().body(false);
    }

    @ApiOperation(value = "판매처 퇴점")
    @PostMapping(value = "/delete", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Boolean> withdrawal(@RequestBody Map<String, String> userInfo){
        return providerService.withdrawal(userInfo.get("loginId"), userInfo.get("password"))
                ? ResponseEntity.ok(true)
                : ResponseEntity.badRequest().body(false);
    }
}

