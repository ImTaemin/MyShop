package com.myshop.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myshop.api.annotation.mock.WithMockCustomer;
import com.myshop.api.domain.dto.request.CustomPageRequest;
import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.service.FavoriteService;
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
@WebMvcTest(controllers = FavoriteController.class)
public class FavoriteControllerTest {

    @SuppressWarnings("all")
    @Autowired
    MockMvc mockMvc;

    @MockBean
    FavoriteService favoriteService;

    ObjectMapper objectMapper = new ObjectMapper();

    List<ItemData.ItemSimple> favoriteSimpleList = null;

    ItemData.ItemSimple resFavoriteSimple;
    CustomPageRequest pageRequest = new CustomPageRequest();

    @BeforeEach
    public void initEach() throws Exception {
        resFavoriteSimple = new ItemData.ItemSimple();
        resFavoriteSimple.setId(1L);
        resFavoriteSimple.setName("ABCD 블랙");
        resFavoriteSimple.setBrandName("브랜드명");
        resFavoriteSimple.setPrice(30000);
        resFavoriteSimple.setMainImage("메인 이미지");

        favoriteSimpleList = new ArrayList<>();
        favoriteSimpleList.add(resFavoriteSimple);
        favoriteSimpleList.add(resFavoriteSimple);
    }

    @Test
    @WithMockCustomer
    @DisplayName("구매자 상품 찜 목록 페이징")
    public void getFavoriteItems() throws Exception {
        //given
        given(favoriteService.getFavoriteItemList(any(Customer.class)))
                .willReturn(favoriteSimpleList);

        String content = objectMapper.writeValueAsString(pageRequest);

        //when
        mockMvc.perform(
                get("/customer/favorite/")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        verify(favoriteService).getFavoriteItemList(any(Customer.class));

    }

    @Test
    @WithMockCustomer
    @DisplayName("구매자 상품 찜 추가")
    public void insertFavoriteItemTest() throws Exception {
        //given

        //when
        mockMvc.perform(
                        post("/customer/favorite/1")
                                .with(csrf()))
                .andExpect(status().isOk());

        //then
        verify(favoriteService).updateFavoriteItem(any(Customer.class), anyLong());
    }

}
