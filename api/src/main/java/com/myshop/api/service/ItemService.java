package com.myshop.api.service;


import com.myshop.api.domain.dto.request.ItemRequest;
import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.enumeration.ItemType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {

    ItemData.Item getItem(Long itemId);
    List<ItemData.ItemSimple> getItems(List<Long> itemIdList);
    PageImpl<ItemData.ItemSimple> getItemsByBrandName(String brandName, Pageable pageable);
    PageImpl<ItemData.ItemSimple> getItemsByCategory(ItemType itemType, Pageable pageable);
    PageImpl<ItemData.Item> getItemsByProvider(Provider provider, Pageable pageable);
    Boolean insertItem(Provider provider, ItemRequest.Item requestItem);
    Boolean insertItems(Provider provider, List<ItemRequest.Item> itemUploadParam);
    Boolean modifyItem(Provider provider, ItemRequest.Item requestItem);
    Long modifyPriceAndQuantityItems(Provider provider, List<ItemRequest.PriceAndQuantity> priceAndQuantityList);
    Long deleteItems(Provider provider, List<Long> itemIds);
    Boolean checkItemCode(String brandName, String itemCode);

}
