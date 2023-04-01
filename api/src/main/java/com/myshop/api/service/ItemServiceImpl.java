package com.myshop.api.service;

import com.myshop.api.domain.dto.request.ItemRequest;
import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.entity.*;
import com.myshop.api.enumeration.ItemType;
import com.myshop.api.exception.ItemNotFoundException;
import com.myshop.api.repository.FavoriteRepository;
import com.myshop.api.repository.ItemImageRepository;
import com.myshop.api.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final Logger LOGGER = LoggerFactory.getLogger(ItemServiceImpl.class);

    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;
    private final FavoriteRepository favoriteRepository;
    private final GCPStorageService gcpStorageService;

    @Transactional
    @Override
    public ItemData.Item getItem(Customer customer, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        LOGGER.info("상품 조회 완료");

        ItemData.Item resItem = new ItemData.Item(item);

        if(customer != null) {
            Favorite favorite = favoriteRepository.findFavoriteByCustomerAndItemId(customer, itemId);
            resItem.setIsFavorite(favorite != null);
        }

        return resItem;
    }

    @Transactional
    @Override
    public PageImpl<ItemData.ItemSimple> getItemsByBrandName(String brandName, Pageable pageable) {
        List<ItemData.ItemSimple> simpleItemList = itemRepository.selectByBrandName(brandName, pageable);

        LOGGER.info("브랜드 상품 페이징 조회 완료");

        return new PageImpl<>(simpleItemList, pageable, simpleItemList.size());
    }

    public PageImpl<ItemData.Item> getItemsByProvider(Provider provider, Pageable pageable) {
        List<ItemData.Item> providerItemList = itemRepository.selectByProvider(provider, pageable);

        LOGGER.info("판매자 상품 페이징 조회 완료");

        return new PageImpl<>(providerItemList, pageable, providerItemList.size());
    }

    @Transactional
    @Override
    public PageImpl<ItemData.ItemSimple> getItemsByCategory(ItemType itemType, Pageable pageable) {
        List<ItemData.ItemSimple> categoryItemList = itemRepository.selectByItemType(itemType, pageable);

        return new PageImpl<>(categoryItemList, pageable, categoryItemList.size());
    }

    @Transactional
    @Override
    public Boolean insertItem(Provider provider, ItemRequest.Item requestItem) {
        try {
            Item item = Item.builder()
                    .code(requestItem.getCode())
                    .name(requestItem.getName())
                    .brandName(provider.getBrandName())
                    .price(requestItem.getPrice())
                    .quantity(requestItem.getQuantity())
                    .itemType(requestItem.getItemType())
                    .genderType(requestItem.getGenderType())
                    .provider(provider)
                    .itemImageList(new ArrayList<>())
                    .build();

            // save() 후에 이미지에 사용할 id를 알 수 있음
            itemRepository.save(item);

            List<ItemImage> itemImageList = gcpStorageService.uploadImages(requestItem.getImageList(), item);

            itemImageRepository.saveAll(itemImageList);
            itemRepository.save(item);
            LOGGER.info("상품 등록 완료");

            return true;
        } catch (Exception e) {
            LOGGER.warn("상품 등록 실패 {}", e);
            return false;
        }
    }


    @Transactional
    @Override
    public Boolean insertItems(Provider provider, List<ItemRequest.Item> itemUploadParam) {

        List<Item> itemList = new ArrayList<>();

        // 클라우드에 이미지 업로드 병렬처리
        itemUploadParam.parallelStream().forEach(reqItem -> {
            Item item = Item.builder()
                    .id(Long.parseLong(reqItem.getId()))
                    .code(reqItem.getCode().toUpperCase())
                    .name(reqItem.getName())
                    .brandName(reqItem.getBrandName())
                    .price(reqItem.getPrice())
                    .quantity(reqItem.getQuantity())
                    .itemType(reqItem.getItemType())
                    .genderType(reqItem.getGenderType())
                    .provider(provider)
                    .itemImageList(new ArrayList<>())
                    .build();

            // save() 후에 이미지에 사용할 id를 알 수 있음
            itemRepository.save(item);

            List<ItemImage> itemImageList = gcpStorageService.uploadImages(reqItem.getImageList(), item);

            itemImageRepository.saveAll(itemImageList);
            itemList.add(item);
        });

        itemRepository.saveAll(itemList);
        LOGGER.info("상품 등록 완료");

        return itemList.size() > 0;
    }

    @Transactional
    @Override
    public Boolean modifyItem(Provider provider, ItemRequest.Item requestItem) {

        try {
            // GCP 이미지 삭제
            Long itemId = Long.parseLong(requestItem.getId());
            gcpStorageService.deleteImages(provider.getBrandName(), itemId);

            LOGGER.info("DB 이미지 삭제");

            // DB 이미지 정보 삭제 (고아 객체 제거 기능이 되지 않아 직접 delete)
            Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
            item.setMainImage(null);
            List<ItemImage> itemImageList = item.getItemImageList();
            itemImageRepository.deleteAll(itemImageList);
            itemImageList.clear();

            // GCP 이미지 업로드
            itemImageList = gcpStorageService.uploadImages(requestItem.getImageList(), item);

            // 상품 정보 수정 및 이미지 새로 삽입
            itemRepository.modifyByProviderItems(provider, requestItem, itemImageList);
            LOGGER.info("{} 수정 완료", requestItem.getName());

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    @Override
    public Long modifyPriceAndQuantityItems(Provider provider, List<ItemRequest.PriceAndQuantity> priceAndQuantityList) {
        AtomicInteger modifiedCnt = new AtomicInteger(0);

        priceAndQuantityList.forEach(priceAndQuantity -> {
            itemRepository.modifyByPriceAndQuantity(provider, priceAndQuantity);
            LOGGER.info("상품 ID:{} 가격, 수량 수정 완료", priceAndQuantity.getId());
            modifiedCnt.getAndIncrement();
        });

        return modifiedCnt.longValue();
    }

    @Transactional
    @Override
    public Long deleteItems(Provider provider, List<Long> itemIds) {
        String brandName = provider.getBrandName();
        for (Long itemId : itemIds) {
            gcpStorageService.deleteImages(brandName, itemId);
        }

        // 로그인 한 판매자의 상품들만 삭제
        return itemRepository.deleteByProviderItems(provider.getId(), itemIds);
    }

    @Transactional
    @Override
    public Boolean checkItemCode(String brandName, String itemCode) {
        // 존재하지 않으면 true 반환 (사용 가능)
        return !itemRepository.existsByItemCode(brandName, itemCode);
    }
}
