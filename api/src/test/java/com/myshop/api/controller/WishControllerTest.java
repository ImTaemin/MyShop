package com.myshop.api.controller;

import com.myshop.api.annotation.mock.WithMockCustomer;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.service.WishService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = WishController.class)
public class WishControllerTest {

    @SuppressWarnings("all")
    @Autowired
    MockMvc mockMvc;

    @MockBean
    WishService wishService;

    @Test
    @WithMockCustomer
    @DisplayName("구매자 상품 찜 추가")
    public void insertWishItemTest() throws Exception {
        //given

        //when
        mockMvc.perform(
                        post("/customer/wish-item/1")
                                .with(csrf()))
                .andExpect(status().isOk());

        //then
        verify(wishService).insertWishItem(any(Customer.class), anyLong());
    }

    @Test
    @WithMockCustomer
    @DisplayName("구매자 상품 찜 제거")
    public void deleteWishItemTest() throws Exception {
        //given

        //when
        mockMvc.perform(
                        delete("/customer/wish-item/1")
                                .with(csrf()))
                .andExpect(status().isOk());

        //then
        verify(wishService).deleteWishItem(any(Customer.class), anyLong());
    }
}
