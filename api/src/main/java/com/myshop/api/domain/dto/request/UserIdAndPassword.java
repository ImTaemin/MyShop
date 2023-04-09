package com.myshop.api.domain.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel
public class UserIdAndPassword {

    @NotBlank
    @ApiModelProperty(value = "아이디", required = true)
    private String userId;

    @NotBlank
    @ApiModelProperty(value = "비밀번호", required = true)
    private String password;
}