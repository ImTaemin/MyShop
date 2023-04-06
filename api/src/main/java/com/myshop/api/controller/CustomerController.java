package com.myshop.api.controller;

import com.myshop.api.annotation.CurrentCustomer;
import com.myshop.api.annotation.CurrentProvider;
import com.myshop.api.domain.dto.request.CustomerRequest;
import com.myshop.api.domain.dto.request.UserIdAndPassword;
import com.myshop.api.domain.dto.request.UserUpdateRequest;
import com.myshop.api.domain.dto.response.BaseResponse;
import com.myshop.api.domain.dto.response.data.CustomerData;
import com.myshop.api.domain.dto.response.data.SignData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.service.CustomerService;
import com.myshop.api.service.SignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;

@Api(tags = {"구매자 REST API"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final SignService signService;

    @ApiOperation(value = "구매자 정보 확인")
    @GetMapping({"/", ""})
    public ResponseEntity<CustomerData.Customer> info(@CurrentCustomer Customer customer) {
        CustomerData.Customer customerData = customerService.getInfo(customer);

        return ResponseEntity.ok(customerData);
    }

    @ApiOperation(value = "구매자 회원 가입")
    @PostMapping(value = "/sign-up", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignData.SignUpResponse> signUp(@Valid @RequestBody CustomerRequest customerRequest) {
        SignData.SignUpResponse signUpResponse = signService.signUp(customerRequest);

        return ResponseEntity.ok(signUpResponse);
    }

    @ApiOperation(value = "구매자 로그인")
    @PostMapping(value = "/sign-in", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignData.SignInResponse> signIn(@RequestBody UserIdAndPassword idAndPassword) throws AccountNotFoundException {
        SignData.SignInResponse signInResponse = signService.signInCustomer(idAndPassword.getUserId(), idAndPassword.getPassword());

        return ResponseEntity.ok(signInResponse);
    }

    @ApiOperation(value = "구매자 토큰 재발행")
    @PostMapping(value = "/reissue", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignData.SignInResponse> reissue(@RequestHeader(value="X-AUTH-TOKEN") String refreshToken) {
        SignData.SignInResponse reissueResponse = signService.reissueAccessToken(refreshToken);

        return ResponseEntity.ok(reissueResponse);
    }
    
    @ApiOperation(value = "구매자 아이디 중복 확인")
    @GetMapping("/exists/id/{userId}")
    public ResponseEntity<BaseResponse> checkUserId(@PathVariable String userId) {
        return customerService.checkUserId(userId)
                ? BaseResponse.ok("사용 가능")
                : BaseResponse.fail("사용 불가");
    }

    @ApiOperation("구매자 정보(pw, hp) 수정")
    @PutMapping(value = {"/", ""}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> modify(@CurrentProvider @Valid @RequestBody UserUpdateRequest updateParam) {
        return customerService.modify(updateParam)
                ? BaseResponse.ok()
                : BaseResponse.fail();
    }


    @ApiOperation(value = "구매자 회원 탈퇴")
    @DeleteMapping(value = {"/", ""}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> withdrawal(@CurrentCustomer @RequestBody UserIdAndPassword idAndPassword){
        return customerService.withdrawal(idAndPassword.getUserId(), idAndPassword.getPassword())
                ? BaseResponse.ok()
                : BaseResponse.fail();
    }

}