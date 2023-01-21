package com.myshop.api.service;

import com.myshop.api.annotation.mock.WithMockCustomer;
import com.myshop.api.domain.dto.request.CartRequest;
import com.myshop.api.domain.dto.request.CustomPageRequest;
import com.myshop.api.domain.dto.response.data.CartData;
import com.myshop.api.domain.entity.Cart;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Item;
import com.myshop.api.domain.entity.id.CartId;
import com.myshop.api.enumeration.GenderType;
import com.myshop.api.enumeration.ItemType;
import com.myshop.api.enumeration.UserRole;
import com.myshop.api.repository.CartRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    CartService cartService;

    @Mock
    CartRepository cartRepository;

    Customer customer;
    Cart cart;
    Item item;

    @BeforeEach
    public void init() {
        cartService = new CartServiceImpl(cartRepository);

        customer = Customer.builder()
                .userId("taemin")
                .password("$2a$12$Liq1iPQn58mqSt8Efe.mn.bQt7W4uuVNypg8N2IAHG.cEPqLqyMZ6")
                .phone("010-1234-5678")
                .name("김태민")
                .roles(Set.of(UserRole.CUSTOMER.toString()))
                .build();

        item = Item.builder()
                .id(1L)
                .code("AAA_BK")
                .name("aaa")
                .brandName("브랜드명")
                .price(3500)
                .mainImage("메인 이미지 경로")
                .quantity(1000)
                .content("브랜드명 코트")
                .itemType(ItemType.OUTER)
                .genderType(GenderType.MEN)
                .createDate(LocalDate.now())
                .build();

        cart = Cart.builder()
                .customer(customer)
                .item(item)
                .build();
    }

    @WithMockCustomer
    @Test
    @DisplayName("상품 장바구니 목록 페이징")
    public void getCartItemListTest() throws Exception {
        //given
        List<CartData> itemList = new ArrayList<>();
        for(int i=0; i<100; i++) {
            itemList.add(new CartData());
        }

        given(cartRepository.getCartItemList(any(Customer.class), any(Pageable.class)))
                .willReturn(itemList);

        //when
        PageImpl<CartData> resCartItems = cartService.getCartItemList(customer, new CustomPageRequest().of());

        //then
        Assertions.assertNotNull(resCartItems);
        Assertions.assertEquals(resCartItems.getTotalPages(), 4);
        Assertions.assertEquals(resCartItems.getTotalElements(), 100);
        Assertions.assertEquals(resCartItems.getSize(), 30);
        Assertions.assertNotNull(resCartItems.getContent());
        Assertions.assertFalse(resCartItems.hasPrevious());
        Assertions.assertTrue(resCartItems.hasNext());
    }

    @WithMockCustomer
    @Test
    @DisplayName("구매자 장바구니 상품 추가")
    public void insertFavoriteItemTest() throws Exception {
        //given

        //when
        cartRepository.save(cart);

        //then
        verify(cartRepository).save(any(Cart.class));
    }

    @WithMockCustomer
    @Test
    @DisplayName("구매자 장바구니 상품 수량 수정")
    public void updateCartItemQuantityTest() throws Exception {
        //given

        //when
        cartRepository.updateCartItemQuantity(customer, CartRequest.builder().build());
        
        //then
        verify(cartRepository).updateCartItemQuantity(any(Customer.class), any(CartRequest.class));
        
    }
    
    @WithMockCustomer
    @Test
    @DisplayName("구매자 장바구니 상품 제거")
    public void deleteCartItemTest() throws Exception {
        //given
        CartId cartId = CartId.builder()
                .customer(customer.getId())
                .item(item.getId())
                .build();

        //when
        cartRepository.deleteById(cartId);

        //then
        verify(cartRepository).deleteById(any(CartId.class));
    }
}
