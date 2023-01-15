package com.myshop.api.controller;

import com.myshop.api.domain.dto.account.CustomerAccount;
import com.myshop.api.domain.dto.request.CustomerRequest;
import com.myshop.api.domain.dto.request.ProviderRequest;
import com.myshop.api.domain.dto.request.UserUpdateRequest;
import com.myshop.api.domain.dto.response.data.CustomerData;
import com.myshop.api.domain.dto.response.data.SignData;
import com.myshop.api.service.CustomerService;
import com.myshop.api.service.SignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;

@Api(tags = {"구매자 REST API"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final SignService signService;

    @ApiOperation(value = "구매자 정보 확인")
    @GetMapping("/")
    public ResponseEntity<CustomerData.Customer> info(@AuthenticationPrincipal CustomerAccount customerAccount) {
        CustomerData.Customer customerData = customerService.getInfo(customerAccount);

        return ResponseEntity.ok(customerData);
    }

    @ApiOperation(value = "구매자 회원 가입")
    @PostMapping(value = "/sign-up", produces = "application/json")
    public ResponseEntity<SignData.SignUpResponse> signUp(@Valid @RequestBody CustomerRequest customerRequest) {
        SignData.SignUpResponse signUpResponse = signService.signUp(customerRequest);

        return ResponseEntity.ok(signUpResponse);
    }

    @ApiOperation(value = "구매자 로그인")
    @PostMapping(value = "/sign-in", produces = "application/json")
    public ResponseEntity<SignData.SignInResponse> signIn(@RequestBody ProviderRequest param) throws AccountNotFoundException {
        SignData.SignInResponse signInResultDto = signService.signInCustomer(param.getUserId(), param.getPassword());

        return ResponseEntity.ok(signInResultDto);
    }

    @ApiOperation(value = "구매자 아이디 중복 확인")
    @GetMapping("/exists/id/{userId}")
        public ResponseEntity<Void> checkUserId(@PathVariable String userId) {
        return customerService.checkUserId(userId)
                ? ResponseEntity.ok().build()   //사용 가능
                : ResponseEntity.notFound().build(); //사용 불가
    }

    @ApiOperation("구매자 정보 수정")
    @PutMapping(value = "/", produces = "application/json")
    public ResponseEntity<Void> modify(@Valid @RequestBody UserUpdateRequest updateParam) {
        return customerService.modify(updateParam)
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }

    @ApiOperation(value = "구매자 회원 탈퇴")
    @DeleteMapping(value = "/", produces = "application/json")
    public ResponseEntity<Void> withdrawal(@RequestBody CustomerRequest param){
        return customerService.withdrawal(param.getUserId(), param.getPassword())
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }
}

