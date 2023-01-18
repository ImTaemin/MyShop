package com.myshop.api.service;

import com.myshop.api.domain.dto.request.ItemRequest;
import com.myshop.api.domain.dto.response.data.ItemData;
import com.myshop.api.domain.entity.Item;
import com.myshop.api.domain.entity.ItemImage;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.exception.ItemNotFoundException;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final Logger LOGGER = LoggerFactory.getLogger(ItemServiceImpl.class);

    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;

    private final GCPStorageService gcpStorageService;

    @Transactional
    @Override
    public ItemData.Item getItem(Long itemCode) {
        Item item = itemRepository.findById(itemCode).orElseThrow(ItemNotFoundException::new);
        LOGGER.info("상품 조회 완료");

        ItemData.Item resItem = new ItemData.Item(item);

        return resItem;
    }

    @Transactional
    @Override
    public PageImpl<ItemData.ItemSimple> getItemsByBrandName(String brandName, Pageable pageable) {
        List<ItemData.ItemSimple> simpleItemList = itemRepository.selectByBrandName(brandName, pageable)
                .stream()
                .map(ItemData.ItemSimple::new)
                .collect(Collectors.toList());

        LOGGER.info("브랜드 상품 페이징 조회 완료");

        return new PageImpl<>(simpleItemList, pageable, simpleItemList.size());
    }

    @Transactional
    @Override
    public Boolean insertItems(Provider provider, List<ItemRequest.Item> itemUploadParam) {

        List<Item> itemList = new ArrayList<>();

        // 클라우드에 이미지 업로드 병렬처리
        itemUploadParam.parallelStream().forEach(reqItem -> {
            Item item = Item.builder()
                    .id(reqItem.getId())
                    .code(reqItem.getCode())
                    .name(reqItem.getName())
                    .brandName(reqItem.getBrandName())
                    .price(reqItem.getPrice())
                    .quantity(reqItem.getQuantity())
                    .content(reqItem.getContent())
                    .itemType(reqItem.getItemType())
                    .genderType(reqItem.getGenderType())
                    .provider(provider)
                    .itemImageList(new ArrayList<>())
                    .build();

            // save() 후에 이미지에 사용할 id를 알 수 있음
            itemRepository.save(item);

            List<ItemImage> itemImageList = gcpStorageService.uploadImages(reqItem.getImageList(), item);

            item.setMainImage(itemImageList.get(0).getPath());

            itemImageRepository.saveAll(itemImageList);
            itemList.add(item);
        });

        itemRepository.saveAll(itemList);
        LOGGER.info("상품 등록 완료");

        return true;
    }

    @Transactional
    @Override
    public Long modifyItems(Provider provider, List<ItemRequest.Item> reqItemList) {

        AtomicInteger modifiedCnt = new AtomicInteger(0);
        reqItemList.forEach(reqItem -> {
            // GCP 이미지 삭제
            Long itemId = reqItem.getId();
            gcpStorageService.deleteImages(provider.getBrandName(), itemId);

            // DB 이미지 정보 삭제 (고아 객체 제거 기능이 되지 않아 직접 delete)
            Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
            item.setMainImage(null);
            List<ItemImage> itemImageList = item.getItemImageList();
            itemImageRepository.deleteAll(itemImageList);
            itemImageList.clear();

            // GCP 이미지 업로드
            itemImageList = gcpStorageService.uploadImages(reqItem.getImageList(), item);

            // 상품 정보 수정 및 이미지 새로 삽입
            itemRepository.modifyByProviderItems(provider, reqItem, itemImageList);
            LOGGER.info("{} 수정 완료", reqItem.getName());

            modifiedCnt.getAndIncrement();
        });

        return modifiedCnt.longValue();
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

}
