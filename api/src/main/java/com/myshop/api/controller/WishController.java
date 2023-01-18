package com.myshop.api.controller;

import com.myshop.api.annotation.CurrentCustomer;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.service.WishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"구매자 찜 REST API"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/customer/wish-item")
public class WishController {

    private final WishService wishService;

    @ApiOperation(value = "구매자 상품 찜 추가")
    @PostMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> insertWishItem(@CurrentCustomer Customer customer, @PathVariable Long itemId) {
        wishService.insertWishItem(customer, Long.valueOf(itemId));

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "구매자 상품 찜 제거")
    @DeleteMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteWishItem(@CurrentCustomer Customer customer, @PathVariable Long itemId) {
        wishService.deleteWishItem(customer, Long.valueOf(itemId));

        return ResponseEntity.ok().build();
    }

}
