package com.myshop.api.domain.dto.request;

import com.myshop.api.util.ApiValueUtils;
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
public class UserUpdateRequest {

    @ApiModelProperty(value = ApiValueUtils.USER_ID, required = true)
    private String userId;

    @ApiModelProperty(value = ApiValueUtils.PASSWORD, required = true)
    private String password;

    @ApiModelProperty(value = ApiValueUtils.MODIFY_PASSWORD, required = true)
    private String modifyPassword;

    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$")
    @ApiModelProperty(value = ApiValueUtils.PHONE, required = true)
    private String phone;
}
