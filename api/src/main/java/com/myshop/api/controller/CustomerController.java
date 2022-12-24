package com.myshop.api.controller;

import com.myshop.api.domain.account.CustomerAccount;
import com.myshop.api.dto.SignInResultDto;
import com.myshop.api.dto.SignUpResultDto;
import com.myshop.api.dto.UserIdAndPassword;
import com.myshop.api.dto.customer.CustomerDto;
import com.myshop.api.dto.customer.CustomerUpdateParam;
import com.myshop.api.mapper.CustomerMapper;
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
    public ResponseEntity<CustomerDto> info(@AuthenticationPrincipal CustomerAccount customerAccount) {
        CustomerDto customerDto = CustomerMapper.INSTANCE.toDto(customerAccount.getCustomer());
        customerDto.setPassword(null);

        return ResponseEntity.ok(customerDto);
    }

    @ApiOperation(value = "구매자 회원 가입")
    @PostMapping(value = "/sign-up", produces = "application/json;charset=UTF-8")
    public ResponseEntity<SignUpResultDto> signUp(@Valid @RequestBody CustomerDto customerDto) {
        SignUpResultDto signUpResultDto = signService.signUp(customerDto);

        return ResponseEntity.ok(signUpResultDto);
    }

    @ApiOperation(value = "구매자 로그인")
    @PostMapping(value = "/sign-in", produces = "application/json;charset=UTF-8")
    public ResponseEntity<SignInResultDto> signIn(@RequestBody UserIdAndPassword param) throws AccountNotFoundException {
        SignInResultDto signInResultDto = signService.signInCustomer(param.getUserId(), param.getPassword());

        return ResponseEntity.ok(signInResultDto);
    }

    @ApiOperation(value = "구매자 아이디 중복 확인")
    @GetMapping("/exists/id/{userId}")
        public ResponseEntity<Boolean> checkUserId(@PathVariable String userId) {
        return customerService.checkUserId(userId) //TODO
                ? ResponseEntity.ok(true)   //사용 가능
                : ResponseEntity.ok(false); //사용 불가
    }

    @ApiOperation("구매자 정보 수정")
    @PutMapping(value = "/", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Boolean> modify(@Valid @RequestBody CustomerUpdateParam updateParam) {
        return customerService.modify(updateParam)
                ? ResponseEntity.ok(true)
                : ResponseEntity.badRequest().body(false);
    }

    @ApiOperation(value = "구매자 회원 탈퇴")
    @DeleteMapping(value = "/", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Boolean> withdrawal(@RequestBody UserIdAndPassword param){
        return customerService.withdrawal(param.getUserId(), param.getPassword())
                ? ResponseEntity.ok(true)
                : ResponseEntity.badRequest().body(false);
    }
}

