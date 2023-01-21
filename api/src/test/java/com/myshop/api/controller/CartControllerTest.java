package com.myshop.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myshop.api.annotation.mock.WithMockCustomer;
import com.myshop.api.domain.dto.request.CartRequest;
import com.myshop.api.domain.dto.request.CustomPageRequest;
import com.myshop.api.domain.dto.response.data.CartData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.service.CartService;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CartController.class)
public class CartControllerTest {
    @SuppressWarnings("all")
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CartService cartService;

    ObjectMapper objectMapper = new ObjectMapper();

    PageImpl<CartData> pagingCart;

    CartData cartData;
    CustomPageRequest pageRequest = new CustomPageRequest();
    CartRequest cartRequest;

    @BeforeEach
    public void initEach() throws Exception {
        cartRequest = CartRequest.builder()
                .itemId(1L)
                .quantity(3)
                .build();

        cartData = new CartData();
        cartData.setId(1L);
        cartData.setName("ABCD 블랙");
        cartData.setBrandName("브랜드명");
        cartData.setPrice(30000);
        cartData.setMainImage("메인 이미지");
        cartData.setQuantity(5);

        List<CartData> cartList = new ArrayList<>();
        cartList.add(cartData);
        cartList.add(cartData);

        pagingCart = new PageImpl<>(cartList, pageRequest.of(), cartList.size());
    }

    @Test
    @WithMockCustomer
    @DisplayName("구매자 장바구니 목록 페이징")
    public void getCartItemList() throws Exception {
        //given
        given(cartService.getCartItemList(any(Customer.class), any(PageRequest.class)))
                .willReturn(pagingCart);

        String content = objectMapper.writeValueAsString(pageRequest);

        //when
        mockMvc.perform(
                        get("/customer/cart/")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[*]").isNotEmpty())
                .andExpect(jsonPath("$.pageable").isNotEmpty())
                .andExpect(jsonPath("$.totalPages", is(pagingCart.getTotalPages())))
                .andExpect(jsonPath("$.totalElements", is(Long.valueOf(pagingCart.getTotalElements()).intValue())))
                .andExpect(jsonPath("$.size", is(pagingCart.getSize())))
                .andExpect(jsonPath("$.number", is(pagingCart.getNumber())))
                .andDo(print());

        //then
        verify(cartService).getCartItemList(any(Customer.class), any(PageRequest.class));
    }

    @Test
    @WithMockCustomer
    @DisplayName("구매자 장바구니 상품 추가")
    public void insertCartItemTest() throws Exception {
        //given
        String content = objectMapper.writeValueAsString(cartRequest);

        //when
        mockMvc.perform(
                        post("/customer/cart/")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk());

        //then
        verify(cartService).insertCartItem(any(Customer.class), any(CartRequest.class));
    }

    @Test
    @WithMockCustomer
    @DisplayName("구매자 장바구니 상품 수량 수정")
    public void updateCartItemQuantityTest() throws Exception {
        //given
        cartRequest.setQuantity(5);
        String content = objectMapper.writeValueAsString(cartRequest);

        //when
        mockMvc.perform(
                        put("/customer/cart/")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk());

        //then
        verify(cartService).updateCartItemQuantity(any(Customer.class), any(CartRequest.class));
    }

    @Test
    @WithMockCustomer
    @DisplayName("구매자 장바구니 상품 삭제")
    public void deleteCartItemTest() throws Exception {
        //given

        //when
        mockMvc.perform(
                        delete("/customer/cart/1")
                                .with(csrf()))
                .andExpect(status().isOk());

        //then
        verify(cartService).deleteCateItem(any(Customer.class), anyLong());
    }
}
