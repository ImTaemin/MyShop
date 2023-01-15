package com.myshop.api.service;


import com.myshop.api.domain.dto.request.ItemRequest;
import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.entity.Provider;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {

    ItemData.Item getItem(Long itemCode);
    PageImpl<ItemData.ItemSimple> getItemsByBrandName(String brandName, Pageable pageable);
    Boolean insertItems(Provider provider, List<ItemRequest.Item> itemUploadParam);
    Long modifyItems(Provider provider, List<ItemRequest.Item> itemRequest);
    Long modifyPriceAndQuantityItems(Provider provider, List<ItemRequest.PriceAndQuantity> priceAndQuantityList);
    Long deleteItems(Provider provider, List<Long> itemIds);

}
