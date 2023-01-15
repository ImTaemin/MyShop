package com.myshop.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.myshop.api.annotation.mock.WithMockProvider;
import com.myshop.api.domain.dto.request.CustomPageRequest;
import com.myshop.api.domain.dto.request.ItemRequest;
import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.enumeration.GenderType;
import com.myshop.api.enumeration.ItemType;
import com.myshop.api.service.ItemService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @SuppressWarnings("all")
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ItemService itemService;

    ObjectMapper objectMapper = new ObjectMapper();

    // Request
    CustomPageRequest pageRequest = new CustomPageRequest();

    // Response
    ItemData.Item resItem;
    ItemData.ItemSimple resItemSimple;
    PageImpl<ItemData.ItemSimple> pagingItems;

    @Retention(RetentionPolicy.RUNTIME)
    @WithMockUser(username = "테스트_최고관리자", roles = "SUPER")
    public @interface WithUser {
    }

    @BeforeEach
    public void initEach() throws Exception {

        resItem = new ItemData.Item();
        resItem.setId(1L);
        resItem.setCode("ABCD-BK");
        resItem.setName("ABCD 블랙");
        resItem.setBrandName("커버낫");
        resItem.setPrice(30000);
        resItem.setQuantity(1000);
        resItem.setMainImage("");
        resItem.setContent("내용내용내용");
        resItem.setItemType(ItemType.BAG);
        resItem.setGenderType(GenderType.MEN);
        resItem.setImageDetailList(mock(ArrayList.class));

        resItemSimple = new ItemData.ItemSimple();
        resItemSimple.setId(1L);
        resItemSimple.setName("ABCD 블랙");
        resItemSimple.setBrandName("커버낫");
        resItemSimple.setPrice(30000);
        resItemSimple.setMainImage("메인 이미지");

        List<ItemData.ItemSimple> itemSimpleList = new ArrayList<>();
        itemSimpleList.add(resItemSimple);
        itemSimpleList.add(resItemSimple);

        pagingItems = new PageImpl<>(itemSimpleList, pageRequest.of(), 30);
    }

    @Test
    @WithUser
    @DisplayName("상품 단건 조회 테스트")
    public void getItemTest() throws Exception {
        //given
        given(itemService.getItem(anyLong())).willReturn(resItem);

        //when
        mockMvc.perform(
                        get("/item/1")
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.brandName").exists())
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.quantity").exists())
                .andExpect(jsonPath("$.mainImage").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.itemType").exists())
                .andExpect(jsonPath("$.genderType").exists())
                .andExpect(jsonPath("$.imageDetailList").exists())
                .andDo(print());

        //then
        verify(itemService).getItem(anyLong());
    }

    @Test
    @WithUser
    @DisplayName("브랜드명으로 상품 페이징 조회 테스트")
    public void getItemsByBrandNameTest() throws Exception {
        //given
        given(itemService.getItemsByBrandName(anyString(), any(PageRequest.class)))
                .willReturn(pagingItems);

        String content = objectMapper.writeValueAsString(pageRequest);

        //when
        mockMvc.perform(
                        get("/item/brands/커버낫")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[*]").isNotEmpty())
                .andExpect(jsonPath("$.pageable").isNotEmpty())
                .andExpect(jsonPath("$.totalPages", is(pagingItems.getTotalPages())))
                .andExpect(jsonPath("$.totalElements", is(Long.valueOf(pagingItems.getTotalElements()).intValue())))
                .andExpect(jsonPath("$.size", is(pagingItems.getSize())))
                .andExpect(jsonPath("$.number", is(pagingItems.getNumber())))
                .andDo(print());

        //then
        verify(itemService).getItemsByBrandName(anyString(), any(PageRequest.class));
    }

    /**
     * List<MultipartFile>을 가지고 있는 List<ItemRequest.Item>을 테스트 하기 위해선
     * MultipartFile을 DTO화 시킨 후, ItemRequest.Item에 저장해야 하는데,
     * 그러기 위해선 필드를 변경해야함. 상품 전체 수정도 마찬가지
     * 때문에 PostMan에서 통합 테스트로 진행함
     */
    @Test
    @WithUser
    @DisplayName("상품 등록 테스트")
    public void insertItemsTest() throws Exception {
    /*
        //given
        given(itemService.insertItems(any(Provider.class), anyList())).willReturn(true);

        ItemRequest.ItemList requestItemList = new ItemRequest.ItemList();
        ItemRequest.Item requestItem = new ItemRequest.Item();
        requestItem.setCode("ABCD-BK");
        requestItem.setName("ABCD 블랙");
        requestItem.setBrandName("커버낫");
        requestItem.setPrice(30000);
        requestItem.setContent("내용내용내용");
        requestItem.setItemType(ItemType.BAG);
        requestItem.setQuantity(1000);
        requestItem.setGenderType(GenderType.MEN);

        MockMultipartFile file = new MockMultipartFile(
                "itemRequestList",
                "test.png",
                "image/png",
                "itemRequestList".getBytes());
        List<MultipartFile> files = new ArrayList<>();
        files.add(file);

        requestItem.setImageList(files);
        requestItemList.setItemRequestList(List.of(requestItem));

        //when
        mockMvc.perform(
                post("/item/")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(objectMapper.writeValueAsString(requestItemList))
                .with(csrf()))
            .andExpect(status().isOk());

        //then
        verify(itemService).insertItems(any(Provider.class), anyList());
    */
    }

    /**
     * PostMan에서 통합 테스트로 진행함
     */
    @Test
    @WithUser
    @DisplayName("상품 수정 테스트")
    public void modifyItemsTest() throws Exception {
        //given

        //when

        //then

    }

    @Test
    @WithMockProvider
    @DisplayName("상품 가격, 수량 수정 테스트")
    public void modifyPriceAndQuantityItemsTest() throws Exception {
        // given
        given(itemService.modifyPriceAndQuantityItems(any(Provider.class), anyList())).willReturn(1L);

        ItemRequest.PriceAndQuantityList priceAndQuantityList = new ItemRequest.PriceAndQuantityList();
        ItemRequest.PriceAndQuantity priceAndQuantity = new ItemRequest.PriceAndQuantity();
        priceAndQuantity.setId(1L);
        priceAndQuantity.setPrice(27000);
        priceAndQuantity.setQuantity(800);
        priceAndQuantityList.setPriceAndQuantityList(List.of(priceAndQuantity));

        String content = objectMapper.writeValueAsString(priceAndQuantityList);

        //when
        mockMvc.perform(
                        patch("/item/")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .with(csrf()))
                .andExpect(status().isOk());

        //then
        verify(itemService).modifyPriceAndQuantityItems(any(Provider.class), anyList());
    }

    @Test
    @WithMockProvider
    @DisplayName("상품 삭제")
    public void deleteItemsTest() throws Exception {
        //given
        given(itemService.deleteItems(any(Provider.class), anyList())).willReturn(1L);

        // {"itemIdList" : [1,2,...]}
        List<Long> itemIdList = List.of(1L, 2L);
        ObjectNode root = objectMapper.createObjectNode();
        ArrayNode arrayNode = root.putArray("itemIdList");
        itemIdList.forEach(arrayNode::add);

        String content = objectMapper.writeValueAsString(root);

        //when
        mockMvc.perform(
                delete("/item/")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(status().isOk());

        //then
        verify(itemService).deleteItems(any(Provider.class), anyList());
    }
}