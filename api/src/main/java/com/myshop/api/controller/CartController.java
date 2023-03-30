package com.myshop.api.controller;

import com.myshop.api.annotation.CurrentCustomer;
import com.myshop.api.domain.dto.request.CartRequest;
import com.myshop.api.domain.dto.request.CustomPageRequest;
import com.myshop.api.domain.dto.response.BaseResponse;
import com.myshop.api.domain.dto.response.data.CartData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"구매자 장바구니 REST API"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/customer/cart")
public class CartController {

    private final CartService cartService;
    
    @ApiOperation(value = "구매자 장바구니 목록 조회")
    @GetMapping(value = {"/", ""}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse> getCartItemList(@CurrentCustomer Customer customer) {
        List<CartData> cartItemList = cartService.getCartItemList(customer);

        return BaseResponse.ok(cartItemList);
    }

    @ApiOperation(value = "구매자 장바구니 상품 추가")
    @PostMapping(value = {"/", ""}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse> insertCartItem(@CurrentCustomer Customer customer, @RequestBody CartRequest cartRequest) {
        cartService.insertCartItem(customer, cartRequest);

        return BaseResponse.ok();
    }

    @ApiOperation(value = "구매자 장바구니 상품 수량 수정")
    @PutMapping(value = {"/", ""}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse> updateCartItemQuantity(@CurrentCustomer Customer customer, @RequestBody CartRequest cartRequest) {
        cartService.updateCartItemQuantity(customer, cartRequest);

        return BaseResponse.ok();
    }

    @ApiOperation(value = "구매자 장바구니 상품 삭제")
    @DeleteMapping(value = {"/", ""}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse> deleteCartItem(@CurrentCustomer Customer customer, @RequestBody Long itemId) {
        cartService.deleteCartItem(customer, itemId);

        return BaseResponse.ok();
    }


}
