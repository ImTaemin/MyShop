package com.myshop.api.service;

import com.myshop.api.domain.entity.Item;
import com.myshop.api.domain.entity.ItemImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GCPStorageService {

    List<ItemImage> uploadImages(List<MultipartFile> images, Item item);
    void deleteImages(String brandName, Long itemId);

}
