package com.myshop.api.controller;

import com.myshop.api.annotation.CurrentProvider;
import com.myshop.api.domain.dto.request.ProviderRequest;
import com.myshop.api.domain.dto.request.UserIdAndPassword;
import com.myshop.api.domain.dto.request.UserUpdateRequest;
import com.myshop.api.domain.dto.response.BaseResponse;
import com.myshop.api.domain.dto.response.data.ProviderData;
import com.myshop.api.domain.dto.response.data.SignData;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.service.ProviderService;
import com.myshop.api.service.SignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(tags = {"판매자 REST API"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/provider")
public class ProviderController {

    private final ProviderService providerService;
    private final SignService signService;

    @ApiOperation(value = "판매자 정보 확인")
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProviderData.Provider> info(@CurrentProvider Provider provider) {
        ProviderData.Provider providerData = providerService.getInfo(provider);

        return ResponseEntity.ok(providerData);
    }

    @ApiOperation(value = "판매자 입점")
    @PostMapping(value = "/sign-up", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignData.SignUpResponse> signUp(@Valid @RequestBody ProviderRequest providerRequest) {
        SignData.SignUpResponse signUpResponse = signService.signUp(providerRequest);

        return ResponseEntity.ok(signUpResponse);
    }

    @ApiOperation(value = "판매자 로그인")
    @PostMapping(value = "/sign-in", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignData.SignInResponse> signIn(@RequestBody UserIdAndPassword idAndPassword) throws AccountNotFoundException {
        SignData.SignInResponse signInResponse = signService.signInProvider(idAndPassword.getUserId(), idAndPassword.getPassword());

        return ResponseEntity.ok(signInResponse);
    }

    @ApiOperation(value = "판매자 토큰 재발행")
    @PostMapping(value = "/reissue", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignData.SignInResponse> reissue(@RequestHeader(name = "X-AUTH-TOKEN") String refreshToken) {
        SignData.SignInResponse reissueResponse = signService.reissueAccessToken(refreshToken);

        return ResponseEntity.ok(reissueResponse);
    }

    @ApiOperation(value = "판매자 아이디 중복확인")
    @GetMapping(value = "/exists/id/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> checkUserId(@PathVariable String userId) {
        return providerService.checkUserId(userId)
                ? BaseResponse.ok("사용 가능")
                : BaseResponse.fail("사용 불가");
    }

    @ApiOperation(value = "브랜드 명 중복확인")
    @GetMapping(value = "/exists/brand/{brandName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> checkBrandName(@PathVariable String brandName) {
        return providerService.checkBrandName(brandName)
                ? BaseResponse.ok("사용 가능")
                : BaseResponse.fail("사용 불가");
    }

    @ApiOperation("판매자 패스워드 수정")
    @PutMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> modify(@CurrentProvider @Valid @RequestBody UserUpdateRequest updateParam) {
        return providerService.modify(updateParam)
                ? BaseResponse.ok()
                : BaseResponse.fail();
    }

    @ApiOperation(value = "판매자 퇴점")
    @DeleteMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> withdrawal(@CurrentProvider @RequestBody UserIdAndPassword idAndPassword){
        return providerService.withdrawal(idAndPassword.getUserId(), idAndPassword.getPassword())
                ? BaseResponse.ok()
                : BaseResponse.fail();
    }
}

