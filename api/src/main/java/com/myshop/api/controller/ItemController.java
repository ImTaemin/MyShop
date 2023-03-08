package com.myshop.api.controller;

import com.myshop.api.annotation.CurrentProvider;
import com.myshop.api.domain.dto.request.CustomPageRequest;
import com.myshop.api.domain.dto.request.ItemRequest;
import com.myshop.api.domain.dto.response.BaseResponse;
import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    
    @ApiOperation(value = "구매자 브랜드명으로 상품 목록 조회")
    @GetMapping(value = "/brand/{brandName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageImpl<ItemData.ItemSimple>> getItemsByBrandName(@PathVariable String brandName, CustomPageRequest pageRequest) {
        PageImpl<ItemData.ItemSimple> pagingItems = itemService.getItemsByBrandName(brandName, pageRequest.of());

        return ResponseEntity.ok(pagingItems);
    }

    @ApiOperation(value = "판매자 상품 목록 조회")
    @GetMapping(value = {"/", ""}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageImpl<ItemData.Item>> getItems(@CurrentProvider Provider provider, CustomPageRequest pageRequest) {
        PageImpl<ItemData.Item> pagingItems = itemService.getItemsByProvider(provider, pageRequest.of());

        return ResponseEntity.ok(pagingItems);
    }

    @ApiOperation(value = "상품 등록")
    @PostMapping(value = {"/", ""}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BaseResponse> insertItems(@CurrentProvider Provider provider, ItemRequest.Item requestItem) {
        Boolean isUploaded = itemService.insertItem(provider, requestItem);

        return isUploaded
                ? BaseResponse.ok()
                : BaseResponse.fail();
    }

    // 다건 등록 바인딩이 되지 않는다... 단건 등록으로 변경..
/*
    @ApiOperation(value = "상품 등록")
    @PostMapping(value = {"/", ""}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BaseResponse> insertItems(@CurrentProvider Provider provider, ItemRequest.ItemList itemList) {
        Boolean isUploaded = itemService.insertItems(provider, itemList.getItemList());

        return isUploaded
                ? BaseResponse.ok()
                : BaseResponse.fail();
    }
    */
    @ApiOperation(value = "상품 수정")
    @PutMapping(value = {"/", ""}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse> modifyItems(@CurrentProvider Provider provider, ItemRequest.ItemList itemList) {
        Long modifiedCnt = itemService.modifyItems(provider, itemList.getItemList());

        return modifiedCnt > 0
                ? BaseResponse.ok()
                : BaseResponse.fail();
    }
    
    @ApiOperation(value = "상품 가격, 수량 수정")
    @PatchMapping(value = {"/", ""}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> modifyPriceAndQuantityItems(@CurrentProvider Provider provider, @RequestBody ItemRequest.PriceAndQuantityList priceAndQuantityList) {

        Long modifiedCnt = itemService.modifyPriceAndQuantityItems(provider, priceAndQuantityList.getPriceAndQuantityList());

        return modifiedCnt > 0
                ? BaseResponse.ok()
                : BaseResponse.fail();
    }
    
    @ApiOperation(value = "상품 삭제")
    @PostMapping(value = {"/delete", ""}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> deleteItems(@CurrentProvider Provider provider, @RequestBody ItemRequest.ItemIdList itemIdList) {
        Long deletedCnt = itemService.deleteItems(provider, itemIdList.getItemIdList());

        return deletedCnt > 0
                ? BaseResponse.ok()
                : BaseResponse.fail();
    }

    @ApiOperation(value = "상품 코드 중복 확인")
    @GetMapping(value = "/exists/code/{itemCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> checkUserId(@CurrentProvider Provider provider, @PathVariable String itemCode) {
        return itemService.checkItemCode(provider.getBrandName(), itemCode.toUpperCase())
                ? BaseResponse.ok("사용 가능")
                : BaseResponse.fail("사용 불가");

    }

}
