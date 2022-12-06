package com.myshop.api.dto;

import com.myshop.api.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemImageDto {

    private Long id;
    private Item item;
    private String path;
    private String name;
    private int sequence;

}
