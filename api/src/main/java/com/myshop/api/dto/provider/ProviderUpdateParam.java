package com.myshop.api.dto.provider;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@ApiModel
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProviderUpdateParam {

    @ApiModelProperty(value = "판매자 아이디", required = true)
    private String loginId;

    @ApiModelProperty(value = "판매자 비밀번호", required = true)
    private String password;

    @ApiModelProperty(value = "판매자 변경 비밀번호", required = true)
    private String modifyPassword;

    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$")
    @ApiModelProperty(value = "판매자 대표 번호", required = true)
    private String phone;
}
