package com.myshop.api.service;

import com.myshop.api.annotation.mock.WithMockCustomer;
import com.myshop.api.domain.dto.request.CustomPageRequest;
import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Favorite;
import com.myshop.api.domain.entity.Item;
import com.myshop.api.domain.entity.id.FavoriteId;
import com.myshop.api.enumeration.GenderType;
import com.myshop.api.enumeration.ItemType;
import com.myshop.api.enumeration.UserRole;
import com.myshop.api.repository.FavoriteRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    FavoriteService favoriteService;

    @Mock
    FavoriteRepository favoriteRepository;

    Customer customer;
    Favorite favorite;
    Item item;

    @BeforeEach
    public void init() {
        favoriteService = new FavoriteServiceImpl(favoriteRepository);

        customer = Customer.builder()
                .userId("taemin")
                .password("$2a$12$Liq1iPQn58mqSt8Efe.mn.bQt7W4uuVNypg8N2IAHG.cEPqLqyMZ6")
                .phone("010-1234-5678")
                .name("김태민")
                .roles(List.of(UserRole.CUSTOMER.toString()))
                .build();

        item = Item.builder()
                .id(1L)
                .code("AAA_BK")
                .name("aaa")
                .brandName("브랜드명")
                .price(3500)
                .mainImage("메인 이미지 경로")
                .quantity(1000)
                .itemType(ItemType.OUTER)
                .genderType(GenderType.MEN)
                .createDate(LocalDateTime.now())
                .build();

        favorite = Favorite.builder()
                .customer(customer)
                .item(item)
                .build();

    }

    @WithMockCustomer
    @Test
    @DisplayName("상품 찜 목록 페이징")
    public void getFavoriteItemListTest() throws Exception {
        //given
        List<ItemData.ItemSimple> itemList = new ArrayList<>();
        for(int i=0; i<100; i++) {
            itemList.add(new ItemData.ItemSimple());
        }

        given(favoriteRepository.getFavoriteItemList(any(Customer.class)))
                .willReturn(itemList);

        //when
        List<ItemData.ItemSimple> resFavoriteItems = favoriteService.getFavoriteItemList(customer);

        //then
        Assertions.assertNotNull(resFavoriteItems);
    }

    @WithMockCustomer
    @Test
    @DisplayName("상품 찜 추가 테스트")
    public void insertFavoriteItemTest() throws Exception {
        //given

        //when
        favoriteRepository.save(favorite);

        //then
        verify(favoriteRepository).save(any(Favorite.class));
    }

    @WithMockCustomer
    @Test
    @DisplayName("상품 찜 제거 테스트")
    public void deleteFavoriteItemTest() throws Exception {
        //given
        FavoriteId favoriteId = FavoriteId.builder()
                .customer(customer.getId())
                .item(item.getId())
                .build();

        //when
        favoriteRepository.deleteById(favoriteId);

        //then
        verify(favoriteRepository).deleteById(any(FavoriteId.class));
    }
}
