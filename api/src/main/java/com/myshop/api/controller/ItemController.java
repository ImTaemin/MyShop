package com.myshop.api.controller;

import com.myshop.api.annotation.CurrentProvider;
import com.myshop.api.domain.dto.request.CustomPageRequest;
import com.myshop.api.domain.dto.request.ItemRequest;
import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.exception.ItemNotFoundException;
import com.myshop.api.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(tags = {"상품 REST API"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    @ApiOperation(value = "상품 상세 조회")
    @GetMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemData.Item> getItem(@PathVariable Long itemId) {
        ItemData.Item resItem = itemService.getItem(itemId);

        return ResponseEntity.ok(resItem);
    }
    
    @ApiOperation(value = "브랜드명으로 상품 목록 조회")
    @GetMapping(value = "/brands/{brandName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageImpl<ItemData.ItemSimple>> getItemsByBrandName(@PathVariable String brandName, CustomPageRequest pageRequest) {
        PageImpl<ItemData.ItemSimple> pagingItems = itemService.getItemsByBrandName(brandName, pageRequest.of());

        return ResponseEntity.ok(pagingItems);
    }

    @ApiOperation(value = "상품 등록")
    @PostMapping(value = "/", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> insertItems(@CurrentProvider Provider provider, ItemRequest.ItemList itemRequestList) {
        Boolean isUploaded = itemService.insertItems(provider, itemRequestList.getItemRequestList());

        return isUploaded
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }
    
    @ApiOperation(value = "상품 수정")
    @PutMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> modifyItems(@CurrentProvider Provider provider, ItemRequest.ItemList itemRequestList) {
        Long modifiedCnt = itemService.modifyItems(provider, itemRequestList.getItemRequestList());

        return modifiedCnt > 0
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }
    
    @ApiOperation(value = "상품 가격, 수량 수정")
    @PatchMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> modifyPriceAndQuantityItems(@CurrentProvider Provider provider, @RequestBody ItemRequest.PriceAndQuantityList priceAndQuantityList) {

        Long modifiedCnt = itemService.modifyPriceAndQuantityItems(provider, priceAndQuantityList.getPriceAndQuantityList());

        return modifiedCnt > 0
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }
    
    @ApiOperation(value = "상품 삭제")
    @DeleteMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteItems(@CurrentProvider Provider provider, @RequestBody ItemRequest.ItemIdList itemIdList) {
        Long deletedCnt = itemService.deleteItems(provider, itemIdList.getItemIdList());

        return deletedCnt > 0
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(value = ItemNotFoundException.class)
    public ResponseEntity<Map<String, String>> itemNotFoundHandler(ItemNotFoundException e) {

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        Map<String, String> map = new HashMap<>();
        map.put("msg", e.getMessage());

        return new ResponseEntity<>(map, responseHeaders, HttpStatus.BAD_REQUEST);
    }


}
