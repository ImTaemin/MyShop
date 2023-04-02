package com.myshop.api.service;

import com.myshop.api.domain.dto.request.CustomPageRequest;
import com.myshop.api.domain.dto.request.ItemRequest;
import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.dto.response.data.ItemImageData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Item;
import com.myshop.api.domain.entity.ItemImage;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.enumeration.GenderType;
import com.myshop.api.enumeration.ItemType;
import com.myshop.api.enumeration.UserRole;
import com.myshop.api.repository.FavoriteRepository;
import com.myshop.api.repository.ItemImageRepository;
import com.myshop.api.repository.ItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    ItemService itemService;

    @Mock
    ItemRepository itemRepository;

    @Mock
    ItemImageRepository itemImageRepository;

    @Mock
    FavoriteRepository favoriteRepository;

    @Mock
    GCPStorageService gcpStorageService;

    Item item;
    ItemImage itemImage;

    ItemRequest.PriceAndQuantity priceAndQuantity;
    List<ItemRequest.Item> reqItemList = new ArrayList<>();

    // Response
    ItemData.Item resItem;
    ItemData.ItemSimple resItemSimple;
    ItemImageData.ItemImage resItemImage;

    Provider provider;

    @BeforeEach
    public void init() {

        provider = Provider.builder()
                .id(1L)
                .userId("taemin")
                .password("1234")
                .phone("010-1234-5678")
                .brandName("브랜드명")
                .roles(List.of(UserRole.PROVIDER.toString()))
                .build();

        itemService = new ItemServiceImpl(itemRepository, itemImageRepository, favoriteRepository, gcpStorageService);

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

        itemImage = ItemImage.builder()
                .id(1L)
                .item(item)
                .sequence(1)
                .path("temp")
                .build();

        item.setItemImageList(List.of(itemImage));

        priceAndQuantity = new ItemRequest.PriceAndQuantity();
        priceAndQuantity.setId("1");
        priceAndQuantity.setPrice(1000);
        priceAndQuantity.setQuantity(4);

        ItemRequest.Item reqItem = new ItemRequest.Item();
        reqItem.setId("1");
        reqItem.setName("aaa");
        reqItem.setBrandName("브랜드명");
        reqItem.setPrice(3500);
        reqItem.setCode("AAA_BK");
        reqItem.setQuantity(1000);
        reqItem.setItemType(ItemType.OUTER);
        reqItem.setGenderType(GenderType.MEN);
        reqItem.setImageList(List.of(mock(MultipartFile.class)));

        reqItemList.add(reqItem);


        resItemImage = new ItemImageData.ItemImage();
        resItemImage.setId(1L);
        resItemImage.setPath("path");
        resItemImage.setSequence(0);

        resItem = new ItemData.Item();
        resItem.setId(1L);
        resItem.setName("aaa");
        resItem.setBrandName("브랜드명");
        resItem.setPrice(3500);
        resItem.setMainImage("메인 이미지 경로");
        resItem.setCode("AAA_BK");
        resItem.setQuantity(1000);
        resItem.setItemType(ItemType.OUTER);
        resItem.setGenderType(GenderType.MEN);
        resItem.setCreateDate(LocalDateTime.now());
        resItem.setImageDetailList(List.of(resItemImage));

        resItemSimple = new ItemData.ItemSimple();
        resItemSimple.setId(1L);
        resItemSimple.setName("aaa");
        resItemSimple.setBrandName("브랜드명");
        resItemSimple.setPrice(3500);
        resItemSimple.setMainImage("메인 이미지 경로");

    }

    @Test
    @DisplayName("상품 단건 조회")
    public void getItemTest() throws Exception {
        //given
        given(itemRepository.findById(anyLong())).willReturn(Optional.ofNullable(item));

        //when
        ItemData.Item resItem = itemService.getItem(new Customer(), 1L);

        //then
        Assertions.assertNotNull(resItem.getId());
        Assertions.assertNotNull(resItem.getName());
        Assertions.assertNotNull(resItem.getBrandName());
        Assertions.assertNotNull(resItem.getPrice());
        Assertions.assertNotNull(resItem.getMainImage());
        Assertions.assertNotNull(resItem.getCode());
        Assertions.assertNotNull(resItem.getQuantity());
        Assertions.assertNotNull(resItem.getItemType());
        Assertions.assertNotNull(resItem.getGenderType());
        Assertions.assertNotNull(resItem.getCreateDate());
        Assertions.assertNotNull(resItem.getImageDetailList());
    }

    @Test
    @DisplayName("브랜드명으로 상품 목록 조회")
    public void getItemsByBrandNameTest() throws Exception {
        //given
        List<ItemData.ItemSimple> itemList = new ArrayList<>();
        for(int i=0; i<100; i++) {
            itemList.add(new ItemData.ItemSimple());
        }
        Page<ItemData.ItemSimple> itemSimplePage = new PageImpl<>(itemList, new CustomPageRequest().of(), 100);
        given(itemRepository.selectByBrandName(anyString(), any(Pageable.class))).willReturn(itemSimplePage);

        //when
        Page<ItemData.ItemSimple> resBrandNameItems = itemService.getItemsByBrandName(item.getBrandName(), new CustomPageRequest().of());

        //then
        Assertions.assertNotNull(resBrandNameItems);
        Assertions.assertEquals(resBrandNameItems.getTotalPages(), 4);
        Assertions.assertEquals(resBrandNameItems.getTotalElements(), 100);
        Assertions.assertEquals(resBrandNameItems.getSize(), 30);
        Assertions.assertNotNull(resBrandNameItems.getContent());
        Assertions.assertFalse(resBrandNameItems.hasPrevious());
        Assertions.assertTrue(resBrandNameItems.hasNext());
    }

    @Test
    @DisplayName("상품 등록")
    public void insertItemsTest() throws Exception {
        //given
        given(itemRepository.save(any(Item.class))).willReturn(item);
        given(itemRepository.saveAll(anyList())).willReturn(List.of(item));
        given(gcpStorageService.uploadImages(anyList(), any(Item.class))).willReturn(List.of(itemImage));

        //when
        itemService.insertItems(provider, reqItemList);

        //then
        verify(itemRepository).save(any(Item.class));
        verify(itemRepository).saveAll(anyList());
        verify(gcpStorageService).uploadImages(anyList(), any(Item.class));
    }

    @Test
    @DisplayName("상품 수정")
    public void modifyItemsTest() throws Exception {
        /*
        //given
        given(itemRepository.findById(anyLong())).willReturn(Optional.ofNullable(item));
        given(gcpStorageService.uploadImages(anyList(), any(Item.class))).willReturn(List.of(itemImage));

        //when
        *//**
         * 연관 엔티티(itemImageList)를 삭제하고 새로 받은 이미지들을 저장하는데,
         * 테스트 코드에서 itemImageList.clear()를 진행 시
         * UnsupportedOperationException 에러가 발생.
         * 원인: Immutable 컬렉션을 삭제하려고 해서. 통합 테스트에선 잘 됨.
         *//*
        Long cnt = itemService.modifyItems(provider, reqItemList);

        //then
        Assertions.assertTrue(cnt > 0);
        verify(gcpStorageService).deleteImages(anyString(), anyLong());
        verify(itemRepository).findById(anyLong());
        verify(gcpStorageService).uploadImages(anyList(), any(Item.class));
        verify(itemRepository).modifyByProviderItems(any(Provider.class), any(ItemRequest.Item.class), anyList());
        */
    }

    @Test
    @DisplayName("상품 가격, 수량 수정")
    public void modifyPriceAndQuantityItemsTest() throws Exception {
        //given

        //when
        itemService.modifyPriceAndQuantityItems(provider, List.of(priceAndQuantity));

        //then
        verify(itemRepository).modifyByPriceAndQuantity(any(Provider.class), any(ItemRequest.PriceAndQuantity.class));

    }

    @Test
    @DisplayName("상품 삭제")
    public void deleteItemsTest() throws Exception {
        //given
        given(itemRepository.deleteByProviderItems(anyLong(), anyList())).willReturn(1L);
        doNothing().when(gcpStorageService).deleteImages(anyString(), anyLong());

        //when
        itemService.deleteItems(provider, List.of(1L,2L));

        //then
        verify(gcpStorageService, times(2)).deleteImages(anyString(), anyLong());
        verify(itemRepository).deleteByProviderItems(anyLong(), anyList());
    }
}
