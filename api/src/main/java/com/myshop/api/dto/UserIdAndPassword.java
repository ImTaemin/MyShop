package com.myshop.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class UserIdAndPassword {

    @ApiModelProperty(value = "아이디", required = true)
    private String userId;

    @ApiModelProperty(value = "비밀번호", required = true)
    private String password;
}
