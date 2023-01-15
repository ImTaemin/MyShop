package com.myshop.api.domain.dto.request;

import com.myshop.api.util.ApiValueUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@ApiModel
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProviderRequest implements UserDto{

    @ApiModelProperty(value = ApiValueUtils.USER_ID, required = true)
    private String userId;

    @ApiModelProperty(value = ApiValueUtils.PASSWORD, required = true)
    private String password;

    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$")
    @ApiModelProperty(value = ApiValueUtils.PHONE, required = true)
    private String phone;

    @ApiModelProperty(value = ApiValueUtils.Provider.BRAND_NAME, required = true)
    private String brandName;

    @ApiModelProperty(value = ApiValueUtils.USER_ROLE, hidden = true)
    private Set<String> roles = new HashSet<>();

}
