package com.myshop.api.controller;

import com.myshop.api.annotation.CurrentCustomer;
import com.myshop.api.annotation.CurrentProvider;
import com.myshop.api.domain.dto.request.CustomPageRequest;
import com.myshop.api.domain.dto.request.ItemRequest;
import com.myshop.api.domain.dto.response.BaseResponse;
import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.enumeration.ItemType;
import com.myshop.api.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = {"상품 REST API"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    @ApiOperation(value = "상품 상세 조회")
    @GetMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> getItem(@CurrentCustomer Customer customer, @PathVariable Long itemId) {
        ItemData.Item resItem = itemService.getItem(customer, itemId);

        return BaseResponse.ok(resItem);
    }


    @ApiOperation(value = "판매자 상품 목록 조회")
    @GetMapping(value = {"/", ""}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<ItemData.Item> getItems(@CurrentProvider Provider provider, CustomPageRequest pageRequest) {
        return itemService.getItemsByProvider(provider, pageRequest.of());
    }

    @ApiOperation(value = "상품 등록")
    @PostMapping(value = {"/", ""}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BaseResponse> insertItem(@CurrentProvider Provider provider, ItemRequest.Item requestItem) {
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

    // MultipartFile 바인딩이 되지 않아 매개변수로 받아옴.
    @ApiOperation(value = "상품 수정")
    @PutMapping(value = {"/", ""}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse> modifyItem(@CurrentProvider Provider provider, @RequestPart ItemRequest.Item requestItem, @RequestPart("imageList") List<MultipartFile> imageList) {
        requestItem.setImageList(imageList);
        Boolean isModified = itemService.modifyItem(provider, requestItem);

        return isModified
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

    @ApiOperation(value = "브랜드명으로 상품 목록 조회")
    @GetMapping(value = "/brand/{brandName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<ItemData.ItemSimple> getItemsByBrandName(@PathVariable String brandName, CustomPageRequest pageRequest) {
        return itemService.getItemsByBrandName(brandName, pageRequest.of());
    }

    @ApiOperation(value = "상품 타입으로 상품 목록 조회")
    @GetMapping(value = "/category/{category}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<ItemData.ItemSimple> getItemsByCategory(@PathVariable String category, CustomPageRequest pageRequest) {
        return itemService.getItemsByCategory(ItemType.valueOf(category), pageRequest.of());
    }

}