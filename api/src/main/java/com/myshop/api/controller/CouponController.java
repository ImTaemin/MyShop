package com.myshop.api.controller;

import com.myshop.api.annotation.CurrentCustomer;
import com.myshop.api.annotation.CurrentProvider;
import com.myshop.api.domain.dto.request.CouponRequest;
import com.myshop.api.domain.dto.response.BaseResponse;
import com.myshop.api.domain.dto.response.data.CouponData;
import com.myshop.api.domain.entity.Coupon;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.service.CouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"쿠폰 REST API"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/coupon")
public class CouponController {

    private final CouponService couponService;

    @ApiOperation(value = "쿠폰 목록 조회 - 판매자")
    @GetMapping(value = {"/", ""})
    public ResponseEntity<BaseResponse> getCouponList(@CurrentProvider Provider provider) {
        List<CouponData> couponData = couponService.getCouponList(provider);

        return BaseResponse.ok(couponData);
    }

    @ApiOperation(value = "쿠폰 등록")
    @PostMapping(value = {"/", ""}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> insertCoupon(@CurrentProvider Provider provider, @RequestBody CouponRequest couponRequest) {
        couponService.insertCoupon(provider, couponRequest);

        return BaseResponse.ok();
    }

    @ApiOperation(value = "쿠폰 코드 중복 확인")
    @GetMapping(value = {"/exists/code/{couponCode}", "/exists/code/", "/exists/code"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> checkCouponCode(@CurrentProvider Provider provider, @PathVariable String couponCode) {
        return couponService.checkCouponCode(provider.getId(), couponCode)
                ? BaseResponse.ok("사용 가능")
                : BaseResponse.fail("사용 불가");
    }

    @ApiOperation(value = "쿠폰 수정")
    @PutMapping(value = {"/", ""})
    public ResponseEntity<BaseResponse> updateCoupon(@CurrentProvider Provider provider, @RequestBody CouponRequest couponRequest) {
        couponService.updateCoupon(provider, couponRequest);

        return BaseResponse.ok();
    }

    @ApiOperation(value = "쿠폰 삭제")
    @DeleteMapping("/{couponCode}")
    public ResponseEntity<BaseResponse> deleteCoupon(@CurrentProvider Provider provider, @PathVariable String couponCode) {
        couponService.deleteCoupon(provider, couponCode);

        return BaseResponse.ok();
    }
    
    @ApiOperation(value = "쿠폰 검색")
    @GetMapping("/search")
    public ResponseEntity<BaseResponse> searchCouponByCode(@CurrentCustomer Customer customer, @RequestParam String couponCode, @RequestParam Long itemId) {
        CouponData findCoupon =  couponService.searchCoupon(customer, couponCode, itemId);

        return findCoupon != null
                ? BaseResponse.ok(findCoupon)
                : BaseResponse.fail();

    }

}
