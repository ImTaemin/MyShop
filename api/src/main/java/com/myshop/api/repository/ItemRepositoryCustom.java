package com.myshop.api.repository;

import com.myshop.api.domain.dto.request.ItemRequest;
import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.ItemImage;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.enumeration.ItemType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

// 정의하려는 기능들 정의
public interface ItemRepositoryCustom {
    Page<ItemData.ItemSimple> selectByBrandName(String brandName, Pageable pageable);
    Page<ItemData.ItemSimple> selectByItemType(ItemType itemType, Pageable pageable);
    Page<ItemData.Item> selectByProvider(Provider provider, Pageable pageable);
    void modifyByProviderItems(Provider provider, ItemRequest.Item item, List<ItemImage> itemImageList);
    void modifyByPriceAndQuantity(Provider provider, ItemRequest.PriceAndQuantity priceAndQuantity);
    Long deleteByProviderItems(Long providerNo, List<Long> itemIdList);
    Boolean existsByItemCode(String brandName, String itemCode);
}
