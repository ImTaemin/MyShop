package com.myshop.api.controller;

import com.myshop.api.annotation.CurrentCustomer;
import com.myshop.api.domain.dto.request.CartRequest;
import com.myshop.api.domain.dto.request.CustomPageRequest;
import com.myshop.api.domain.dto.response.data.CartData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"구매자 장바구니 REST API"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/customer/cart")
public class CartController {

    private final CartService cartService;
    
    @ApiOperation(value = "구매자 장바구니 목록 조회")
    @GetMapping("/")
    public ResponseEntity<PageImpl<CartData>> getCartItemList(@CurrentCustomer Customer customer, CustomPageRequest pageRequest) {
        PageImpl<CartData> pagingCartItemList = cartService.getCartItemList(customer, pageRequest.of());

        return ResponseEntity.ok(pagingCartItemList);
    }

    @ApiOperation(value = "구매자 장바구니 상품 추가")
    @PostMapping("/")
    public ResponseEntity<Void> insertCartItem(@CurrentCustomer Customer customer, @RequestBody CartRequest cartRequest) {
        cartService.insertCartItem(customer, cartRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "구매자 장바구니 상품 수량 수정")
    @PutMapping("/")
    public ResponseEntity<Void> updateCartItemQuantity(@CurrentCustomer Customer customer, @RequestBody CartRequest cartRequest) {
        cartService.updateCartItemQuantity(customer, cartRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "구매자 장바구니 상품 삭제")
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteCartItem(@CurrentCustomer Customer customer, @PathVariable Long itemId) {
        cartService.deleteCateItem(customer, itemId);

        return ResponseEntity.ok().build();
    }


}
