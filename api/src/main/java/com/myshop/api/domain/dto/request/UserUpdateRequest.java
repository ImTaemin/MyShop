package com.myshop.api.domain.dto.request;

import com.myshop.api.util.ApiValueUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@ApiModel
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateRequest {

    @NotBlank
    @ApiModelProperty(value = ApiValueUtils.User.USER_ID, required = true)
    private String userId;

    @NotBlank
    @ApiModelProperty(value = ApiValueUtils.User.PASSWORD, required = true)
    private String password;

    @NotBlank
    @ApiModelProperty(value = ApiValueUtils.User.MODIFY_PASSWORD, required = true)
    private String modifyPassword;

    @NotBlank
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$")
    @ApiModelProperty(value = ApiValueUtils.User.PHONE, required = true)
    private String phone;
}
