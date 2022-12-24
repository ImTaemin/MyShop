package com.myshop.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignUpResultDto {

    @ApiModelProperty(value = "가입 성공 여부. true - 성공")
    private boolean success;

    @ApiModelProperty(value = "가입 성공 코드. 0 - 성공")
    private int code;

    @ApiModelProperty(value = "가입 성공 메세지. Success - 성공")
    private String msg;

}
