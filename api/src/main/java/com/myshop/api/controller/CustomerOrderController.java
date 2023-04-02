package com.myshop.api.controller;

import com.myshop.api.annotation.CurrentCustomer;
import com.myshop.api.domain.dto.pay.kakao.ReadyResponse;
import com.myshop.api.domain.dto.request.CustomPageRequest;
import com.myshop.api.domain.dto.request.OrderRequest;
import com.myshop.api.domain.dto.response.BaseResponse;
import com.myshop.api.domain.dto.response.data.OrderData;
import com.myshop.api.domain.dto.response.data.OrderItemData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(tags = {"구매자 주문 REST API"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/customer/order")
public class CustomerOrderController {

    private final OrderService orderService;

    @Value("${client.url}")
    private String clientUrl;

    @ApiOperation(value = "구매자 주문내역 조회")
    @GetMapping(value = {"/", ""}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<OrderItemData> getOrders(@CurrentCustomer Customer customer, CustomPageRequest pageRequest) {
        Page<OrderItemData> pagingOrders = orderService.getOrdersByCustomer(customer, pageRequest.of());

        return pagingOrders;
    }

    @ApiOperation(value = "주문 번호에 해당하는 주문 상세 조회")
    @PostMapping(value = {"/", ""})
    public ResponseEntity<BaseResponse> getOrdersByOrderId(@CurrentCustomer Customer customer, @RequestBody String orderId) {
        OrderData orderData = orderService.getOrdersByOrderId(customer, orderId);

        return BaseResponse.ok(orderData);
    }

    @ApiOperation(value = "카카오 주문 준비")
    @PostMapping(value = "/kakao/ready")
    public ResponseEntity<BaseResponse> readyToKakaoPay(@CurrentCustomer Customer customer, @RequestBody OrderRequest.Order orderRequest) {
        ReadyResponse readyResponse = orderService.readyToKakaoPay(customer, orderRequest);

        return BaseResponse.ok(readyResponse);
    }

    @ApiOperation(value = "카카오 주문 성공")
    @GetMapping(value = "/kakao/approval")
    public ResponseEntity<BaseResponse> approveToKakaoPay(HttpServletResponse response, @RequestParam("pg_token") String pgToken, @RequestParam("orderId") String orderId) throws IOException {
        orderService.approveToKakaoPay(pgToken, orderId);

        response.sendRedirect(clientUrl + "/order?orderId=" + orderId);

        return BaseResponse.ok("주문 성공");
    }

    @ApiOperation(value = "카카오 주문 취소")
    @GetMapping(value = "/kakao/cancel")
    public ResponseEntity<BaseResponse> cancelToKakaoPay(@RequestParam("orderId") String orderId) {
        orderService.cancelToKakaoPay(orderId);

        return BaseResponse.ok("주문 취소");
    }

    @ApiOperation(value = "카카오 주문 실패")
    @GetMapping(value = "/kakao/fail")
    public ResponseEntity<BaseResponse> failToKakaoPay(@RequestParam("orderId") String orderId) {
        orderService.failToKakaoPay(orderId);

        return BaseResponse.ok("주문 실패");
    }
}
