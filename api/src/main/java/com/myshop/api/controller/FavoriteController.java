package com.myshop.api.controller;

import com.myshop.api.annotation.CurrentCustomer;
import com.myshop.api.domain.dto.request.CustomPageRequest;
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

@Api(tags = {"구매자 찜 REST API"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/customer/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @ApiOperation(value = "구매자 상품 찜 목록")
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageImpl<ItemData.ItemSimple>> getFavoriteItemList(@CurrentCustomer Customer customer, CustomPageRequest pageRequest) {
        PageImpl<ItemData.ItemSimple> pagingFavoriteItemList = favoriteService.getFavoriteItemList(customer, pageRequest.of());

        return ResponseEntity.ok(pagingFavoriteItemList);
    }
    
    @ApiOperation(value = "구매자 상품 찜 추가")
    @PostMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> insertFavoriteItem(@CurrentCustomer Customer customer, @PathVariable Long itemId) {
        favoriteService.insertFavoriteItem(customer, itemId);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "구매자 상품 찜 제거")
    @DeleteMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteFavoriteItem(@CurrentCustomer Customer customer, @PathVariable Long itemId) {
        favoriteService.deleteFavoriteItem(customer, itemId);

        return ResponseEntity.ok().build();
    }

}
