package com.myshop.api.service;

import com.myshop.api.domain.dto.request.CustomPageRequest;
import com.myshop.api.domain.dto.request.ItemRequest;
import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Item;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.enumeration.ItemType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {

    ItemData.Item getItem(Customer customer, Long itemId);
    Page<ItemData.ItemSimple> getItemsByBrandName(String brandName, Pageable pageable);
    Page<ItemData.ItemSimple> getItemsByCategory(ItemType itemType, Pageable pageable);
    Page<ItemData.Item> getItemsByProvider(Provider provider, Pageable pageable);
    Boolean insertItem(Provider provider, ItemRequest.Item requestItem);
    Boolean insertItems(Provider provider, List<ItemRequest.Item> itemUploadParam);
    Boolean modifyItem(Provider provider, ItemRequest.Item requestItem);
    Long modifyPriceAndQuantityItems(Provider provider, List<ItemRequest.PriceAndQuantity> priceAndQuantityList);
    Long deleteItems(Provider provider, List<Long> itemIds);
    Boolean checkItemCode(String brandName, String itemCode);

}
