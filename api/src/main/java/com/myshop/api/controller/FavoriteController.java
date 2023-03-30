package com.myshop.api.controller;

import com.myshop.api.annotation.CurrentCustomer;
import com.myshop.api.domain.dto.request.CustomPageRequest;
import com.myshop.api.domain.dto.response.BaseResponse;
import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.service.FavoriteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"구매자 좋아요 REST API"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/customer/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @ApiOperation(value = "구매자 상품 좋아요 목록")
    @GetMapping(value = {"/", ""}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> getFavoriteItemList(@CurrentCustomer Customer customer) {
        List<ItemData.ItemSimple> favoriteItemList = favoriteService.getFavoriteItemList(customer);

        return BaseResponse.ok(favoriteItemList);
    }
    
    @ApiOperation(value = "구매자 상품 좋아요 추가 및 제거")
    @PostMapping(value = {"/", ""}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> insertFavoriteItem(@CurrentCustomer Customer customer, @RequestBody Long itemId) {
        favoriteService.updateFavoriteItem(customer, itemId);

        return ResponseEntity.ok().build();
    }

}
