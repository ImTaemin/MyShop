package com.myshop.api.domain.dto.request;

import com.myshop.api.util.ApiValueUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

@ApiModel
@Data
@NoArgsConstructor
public class CustomPageRequest {

    @ApiModelProperty(value = ApiValueUtils.Page.PAGE, example = "1")
    private int page = 0;

    @ApiModelProperty(value = ApiValueUtils.Page.SIZE, example = "30")
    private int size = 30;
    
    private Direction direction = Direction.DESC;

//    public void setPage(int page) {
//        this.page = page <= 0 ? 1 : page;
//    }

    public void setSize(int size) {
        int default_size = 10;
        int max_size = 100;
        this.size = size > max_size ? default_size : size;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public PageRequest of() {
        return PageRequest.of(page, size, direction, "create_date");
    }
}