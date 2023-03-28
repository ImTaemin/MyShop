package com.myshop.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myshop.api.annotation.mock.WithMockCustomer;
import com.myshop.api.domain.dto.pay.kakao.ReadyResponse;
import com.myshop.api.domain.dto.request.CustomPageRequest;
import com.myshop.api.domain.dto.request.OrderRequest;
import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.dto.response.data.OrderItemData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.enumeration.OrderStatus;
import com.myshop.api.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CustomerOrderController.class)
class OrderControllerTest {

    @SuppressWarnings("all")
    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    ObjectMapper objectMapper = new ObjectMapper();

    OrderRequest.Order orderRequest = new OrderRequest.Order();
    CustomPageRequest pageRequest = new CustomPageRequest();
    PageImpl<OrderItemData> pagingOrderItem;
    OrderRequest.OrderItem orderItem = new OrderRequest.OrderItem();

    OrderItemData orderItemData = new OrderItemData();
    ReadyResponse readyResponse = new ReadyResponse();
    ItemData.ItemSimple itemSimple = new ItemData.ItemSimple();
    List<OrderItemData> orderItemList = new ArrayList<>();

    @BeforeEach
    public void initEach() throws Exception {
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

        pagingOrderItem = new PageImpl<>(orderItemList, pageRequest.of(), orderItemList.size());

        orderItem.setItemId(1L);
        orderItem.setQuantity(3);

        orderRequest.setRoadAddress("도로명");
        orderRequest.setDetailAddress("상세주소");
        orderRequest.setOrderItemList(List.of(orderItem, orderItem));

        readyResponse.setPartner_order_id("cid");
        readyResponse.setTid("tid");
        readyResponse.setNext_redirect_pc_url("nextUrl");
    }

    @Test
    @WithMockCustomer
    @DisplayName("구매자 주문 목록 페이징")
    public void getOrdersTest() throws Exception {
        //given
        given(orderService.getOrdersByCustomer(any(Customer.class), any(PageRequest.class)))
                .willReturn(pagingOrderItem);

        String content = objectMapper.writeValueAsString(pageRequest);

        //when
        mockMvc.perform(
                        get("/order/")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[*]").isNotEmpty())
                .andExpect(jsonPath("$.content[0].orderNo").isNotEmpty())
                .andExpect(jsonPath("$.content[0].quantity").isNotEmpty())
                .andExpect(jsonPath("$.content[0].payment").isNotEmpty())
                .andExpect(jsonPath("$.content[0].orderStatus", is(OrderStatus.REQUESTED.toString())))
                .andExpect(jsonPath("$.content[0].orderDate").isNotEmpty())
                .andExpect(jsonPath("$.content[0].item.name").isNotEmpty())
                .andExpect(jsonPath("$.content[0].item.brandName").isNotEmpty())
                .andExpect(jsonPath("$.content[0].item.mainImage").isNotEmpty())
                .andExpect(jsonPath("$.pageable").isNotEmpty())
                .andExpect(jsonPath("$.totalPages", is(pagingOrderItem.getTotalPages())))
                .andExpect(jsonPath("$.totalElements", is(Long.valueOf(pagingOrderItem.getTotalElements()).intValue())))
                .andExpect(jsonPath("$.size", is(pagingOrderItem.getSize())))
                .andExpect(jsonPath("$.number", is(pagingOrderItem.getNumber())))
                .andDo(print());

        //then
        verify(orderService).getOrdersByCustomer(any(Customer.class), any(PageRequest.class));

    }

    @Test
    @WithMockCustomer
    @DisplayName("카카오 주문 준비")
    void readyToKakaoPay() throws Exception {
        //given
        given(orderService.readyToKakaoPay(any(Customer.class), any(OrderRequest.Order.class)))
                .willReturn(readyResponse);

        String content = objectMapper.writeValueAsString(orderRequest);

        //when
        mockMvc.perform(
                        post("/order/kakao/ready/")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.next_redirect_pc_url").isNotEmpty())
                .andExpect(jsonPath("$.data.order").isEmpty())
                .andDo(print());

        //then
        verify(orderService).readyToKakaoPay(any(Customer.class), any(OrderRequest.Order.class));

    }

    @Test
    @WithMockCustomer
    @DisplayName("카카오 결제 성공")
    void approveToKakaoPay() throws Exception {
        //given

        //when
        mockMvc.perform(
                        get("/order/kakao/approval?pg_token=aaaa&orderId=20230128")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg", is("주문 성공")))
                .andDo(print());

        //then
        verify(orderService).approveToKakaoPay(anyString(), anyString());
    }

    @Test
    @WithMockCustomer
    @DisplayName("카카오 결제 취소")
    void cancelToKakaoPay() throws Exception {
        //given

        //when
        mockMvc.perform(
                        get("/order/kakao/cancel?orderId=20230128")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg", is("주문 취소")))
                .andDo(print());

        //then
        verify(orderService).cancelToKakaoPay(anyString());
    }

    @Test
    @WithMockCustomer
    @DisplayName("카카오 결제 실패 ")
    void failToKakaoPay() throws Exception {
        //given

        //when
        mockMvc.perform(
                        get("/order/kakao/fail?orderId=20230128")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg", is("주문 실패")))
                .andDo(print());

        //then
        verify(orderService).failToKakaoPay(anyString());
    }
}