package com.myshop.api.controller;

import com.myshop.api.domain.account.ProviderAccount;
import com.myshop.api.dto.SignInResultDto;
import com.myshop.api.dto.SignUpResultDto;
import com.myshop.api.dto.UserIdAndPassword;
import com.myshop.api.dto.provider.ProviderDto;
import com.myshop.api.dto.provider.ProviderUpdateParam;
import com.myshop.api.mapper.ProviderMapper;
import com.myshop.api.service.ProviderService;
import com.myshop.api.service.SignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;

@Api(tags = {"판매자 REST API"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth/provider")
public class ProviderController {

    private final ProviderService providerService;
    private final SignService signService;

    @ApiOperation(value = "판매자 정보 확인")
    @GetMapping("/")
    public ResponseEntity<ProviderDto> info(@AuthenticationPrincipal ProviderAccount providerAccount) {
        ProviderDto providerDto = ProviderMapper.INSTANCE.toDto(providerAccount.getProvider());
        providerDto.setPassword(null);

        return ResponseEntity.ok(providerDto);
    }

    @ApiOperation(value = "판매자 입점")
    @PostMapping(value = "/sign-up", produces = "application/json;charset=UTF-8")
    public ResponseEntity<SignUpResultDto> signUp(@Valid @RequestBody ProviderDto providerDto) {
        SignUpResultDto signUpResultDto = signService.signUp(providerDto);

        return ResponseEntity.ok(signUpResultDto);
    }

    @ApiOperation(value = "판매자 로그인")
    @PostMapping(value = "/sign-in", produces = "application/json;charset=UTF-8")
    public ResponseEntity<SignInResultDto> signIn(@RequestBody UserIdAndPassword param) throws AccountNotFoundException {
        SignInResultDto signInResultDto = signService.signInProvider(param.getUserId(), param.getPassword());

        return ResponseEntity.ok(signInResultDto);
    }

    @ApiOperation(value = "판매자 아이디 중복확인")
    @GetMapping("/exists/id/{userId}")
    public ResponseEntity<Boolean> checkUserId(@PathVariable String userId) {
        return providerService.checkUserId(userId)
                ? ResponseEntity.ok(true)   //사용 가능
                : ResponseEntity.ok(false); //사용 불가
    }

    @ApiOperation(value = "브랜드 명 중복확인")
    @GetMapping("/exists/brand/{brandName}")
    public ResponseEntity<Boolean> checkBrandName(@PathVariable String brandName) {
        return providerService.checkBrandName(brandName)
                ? ResponseEntity.ok(true)   //사용 가능
                : ResponseEntity.ok(false); //사용 불가
    }

    @ApiOperation("판매자 정보 수정")
    @PutMapping(value = "/", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Boolean> modify(@Valid @RequestBody ProviderUpdateParam updateParam) {
        return providerService.modify(updateParam)
                ? ResponseEntity.ok(true)
                : ResponseEntity.badRequest().body(false);
    }

    @ApiOperation(value = "판매자 퇴점")
    @DeleteMapping(value = "/", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Boolean> withdrawal(@RequestBody UserIdAndPassword param){
        return providerService.withdrawal(param.getUserId(), param.getPassword())
                ? ResponseEntity.ok(true)
                : ResponseEntity.badRequest().body(false);
    }
}

