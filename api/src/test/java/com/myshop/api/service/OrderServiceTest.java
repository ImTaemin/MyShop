package com.myshop.api.service;

import com.myshop.api.domain.dto.pay.kakao.ReadyResponse;
import com.myshop.api.domain.dto.request.CustomPageRequest;
import com.myshop.api.domain.dto.request.OrderRequest;
import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.dto.response.data.OrderItemData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Orders;
import com.myshop.api.enumeration.OrderStatus;
import com.myshop.api.enumeration.UserRole;
import com.myshop.api.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderService orderService;

    @Mock
    KakaoPayService kakaoPayService;

    Customer customer;
    Orders order;
    
    List<OrderItemData> orderItemList = new ArrayList<>();
    ItemData.ItemSimple itemSimple = new ItemData.ItemSimple();
    OrderItemData orderItemData = new OrderItemData();
    ReadyResponse readyResponse = new ReadyResponse();

    @BeforeEach
    public void init() {
        customer = Customer.builder()
                .userId("taemin")
                .password("$2a$12$Liq1iPQn58mqSt8Efe.mn.bQt7W4uuVNypg8N2IAHG.cEPqLqyMZ6")
                .phone("010-1234-5678")
                .name("김태민")
                .roles(List.of(UserRole.CUSTOMER.toString()))
                .build();

        order = Orders.builder()
                .id("202301281234560001")
                .tid("tid")
                .totalPayment(3500)
                .orderDate(LocalDateTime.now())
                .createDate(LocalDateTime.now())
                .customer(customer)
                .build();

        itemSimple.setId(1L);
        itemSimple.setName("상품명");
        itemSimple.setBrandName("브랜드명");
        itemSimple.setPrice(3500);
        itemSimple.setMainImage("http~~");

        orderItemData.setOrderNo("202301281234560001");
        orderItemData.setOrderDate(LocalDateTime.now());
        orderItemData.setOrderStatus(OrderStatus.REQUESTED);
        orderItemData.setPayment(3500);
        orderItemData.setQuantity(1);
        orderItemData.setItem(itemSimple);

        orderItemList.add(orderItemData);
        orderItemList.add(orderItemData);

        readyResponse.setPartner_order_id("cid");
        readyResponse.setTid("tid");
        readyResponse.setNext_redirect_pc_url("nextUrl");
        readyResponse.setOrder(order);
    }

    @Test
    @DisplayName("구매자 주문 목록 페이징")
    public void getOrderByCustomerTest() throws Exception {
        //given
        given(orderRepository.selectByCustomer(any(Customer.class), any(Pageable.class)))
                .willReturn(orderItemList);

        //when
        List<OrderItemData> orderItemList = orderRepository.selectByCustomer(customer, new CustomPageRequest().of());

        //then
        Assertions.assertEquals(orderItemList.size(), 2);
        verify(orderRepository).selectByCustomer(any(Customer.class), any(Pageable.class));

    }

    @Test
    @DisplayName("카카오 주문 준비")
    public void readyToKakaoPay() throws Exception {
        //given
        given(kakaoPayService.ready(any(Customer.class), anyString(), any(OrderRequest.Order.class)))
                .willReturn(readyResponse);
        given(orderRepository.save(any(Orders.class))).willReturn(order);

        //when
        kakaoPayService.ready(customer, "202301281234560001", new OrderRequest.Order());
        readyResponse.setOrder(null);

        orderRepository.save(order);

        //then
        verify(orderRepository).save(any(Orders.class));
        verify(kakaoPayService).ready(any(Customer.class), anyString(), any(OrderRequest.Order.class));
        Assertions.assertNull(readyResponse.getOrder());
    }
    
    @Test
    @DisplayName("카카오 주문 취소")
    public void cancelToKakaoPay() throws Exception {
        //given

        //when
        orderRepository.deleteOrderById("1234");

        //then
        verify(orderRepository).deleteOrderById(anyString());
    }

    @Test
    @DisplayName("카카오 주문 실패")
    public void failToKakaoPay() throws Exception {
        //given

        //when
        orderService.cancelToKakaoPay("1234");

        //then
        verify(orderService).cancelToKakaoPay(anyString());
    }
}
