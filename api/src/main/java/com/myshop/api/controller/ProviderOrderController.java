package com.myshop.api.controller;

import com.myshop.api.annotation.CurrentProvider;
import com.myshop.api.domain.dto.request.CustomPageRequest;
import com.myshop.api.domain.dto.request.OrderRequest;
import com.myshop.api.domain.dto.response.BaseResponse;
import com.myshop.api.domain.dto.response.data.OrderItemData;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.enumeration.OrderStatus;
import com.myshop.api.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"판매자 주문 REST API"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/provider/order")
public class ProviderOrderController {

    private final OrderService orderService;

    @ApiOperation(value = "상태별 주문내역 조회")
    @GetMapping(value = {"/", ""}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<OrderItemData> getStatusOrders(@CurrentProvider Provider provider, CustomPageRequest pageRequest, OrderStatus orderStatus) {
        Page<OrderItemData> pagingOrders = orderService.getOrdersByProvider(provider, pageRequest.of(), orderStatus);

        return pagingOrders;
    }
    
    @ApiOperation(value = "선택한 주문 상태 변경")
    @PutMapping(value = {"/",""}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> changeOrders(@RequestBody OrderRequest.OrderChange orderChangeRequest) {
        orderService.changeOrders(orderChangeRequest);

        return BaseResponse.ok();
    }
}
