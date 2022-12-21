package com.myshop.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignUpResultDto {

    private boolean success;

    private int code;

    private String msg;

}
