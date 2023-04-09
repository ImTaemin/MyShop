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
import java.util.ArrayList;
import java.util.List;

@ApiModel
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProviderRequest implements UserDto{

    @NotBlank
    @ApiModelProperty(value = ApiValueUtils.User.USER_ID, required = true)
    private String userId;

    @NotBlank
    @ApiModelProperty(value = ApiValueUtils.User.PASSWORD, required = true)
    private String password;

    @NotBlank
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$")
    @ApiModelProperty(value = ApiValueUtils.User.PHONE, required = true)
    private String phone;

    @NotBlank
    @ApiModelProperty(value = ApiValueUtils.User.Provider.BRAND_NAME, required = true)
    private String brandName;

}
