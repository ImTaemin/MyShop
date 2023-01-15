package com.myshop.api.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.myshop.api.domain.entity.Item;
import com.myshop.api.domain.entity.ItemImage;
import com.myshop.api.exception.FileNameException;
import com.myshop.api.exception.GCPFileException;
import com.myshop.api.exception.InvalidFileTypeException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GCPStorageServiceImpl implements GCPStorageService{

    private final Logger LOGGER = LoggerFactory.getLogger(GCPStorageServiceImpl.class);

    @Value("${gcp.bucket.name}")
    private String gcpBucketName;

    @Value("${gcp.dir.image.main}")
    private String gcpMainDirectory;

    @Value("${gcp.dir.image.detail}")
    private String gcpDetailDirectory;

    private final Storage storage;

    @Override
    public List<ItemImage> uploadImages(List<MultipartFile> images, Item item) {
        Bucket bucket = storage.get(gcpBucketName);

        List<ItemImage> itemImageList = item.getItemImageList();

        String brandName = item.getBrandName();
        Long itemId = item.getId();

        /**
         * 대표 이미지: image/main/브랜드명/상품고유번호/상품고유번호.확장자
         * 나머지 이미지: image/detail/브랜드명/상품고유번호/순서.확장자
         */
        int cnt = 0;
        for (MultipartFile image : images) {
            try {
                String filename = image.getOriginalFilename();
                if(filename == null) throw  new FileNameException();

                // MIME 타입을 확인하기 위한 Path
                Path path = new File(filename).toPath();

                // 디렉터리 + 이미지명 지정
                StringBuilder imgDirBuilder = new StringBuilder();
                imgDirBuilder.append(cnt == 0 ? gcpMainDirectory : gcpDetailDirectory)
                        .append("/")
                        .append(brandName)
                        .append("/")
                        .append(itemId)
                        .append("/")
                        .append(cnt == 0 ? itemId : cnt)
                        .append(checkFileExtension(filename));

                // 디렉터리 + 이미지명 업로드
                Blob blob = bucket.create(
                        imgDirBuilder.toString(),
                        image.getInputStream(),
                        Files.probeContentType(path));

                LOGGER.info("{}/{}, {}번 업로드 완료", brandName, itemId, cnt);

                // 클라우드에 업로드 된 이미지 정보
                ItemImage itemImage = ItemImage.builder()
                        .item(item)
                        .path(blob.getMediaLink())
                        .sequence(cnt)
                        .build();

                // 메인 이미지는 상품 자체에 저장
                if(cnt == 0) {
                    item.setMainImage(itemImage.getPath());
                } else {
                    itemImageList.add(itemImage);
                }

                cnt++;
            } catch (IOException e) {
                throw new GCPFileException();
            }
        }

        return itemImageList;
    }

    @Override
    public void deleteImages(String brandName, Long itemId) {
        StringBuilder brandItemDir = new StringBuilder();
        brandItemDir.append("/")
                .append(brandName)
                .append("/")
                .append(itemId);
        
        Bucket bucket = storage.get(gcpBucketName);

        // 메인 이미지 삭제
        bucket.list(Storage.BlobListOption.prefix(gcpMainDirectory + brandItemDir))
                .iterateAll()
                .forEach(Blob::delete);
        LOGGER.info("메인 이미지 삭제 완료");

        // 디테일 이미지 삭제
        bucket.list(Storage.BlobListOption.prefix(gcpDetailDirectory + brandItemDir))
                .iterateAll()
                .forEach(Blob::delete);
        LOGGER.info("상세 이미지 삭제 완료");
    }

    // 이미지 파일인지 확인
    private String checkFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            String[] extensionList = {".png", ".jpeg", ".jpg", ".gif"};

            for (String extension : extensionList) {
                if (fileName.endsWith(extension)) {
                    return extension;
                }
            }
        }

        throw new InvalidFileTypeException();
    }
}
