package com.myshop.api.domain.dto.request;

import com.myshop.api.util.ApiValueUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@ApiModel
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerRequest implements UserDto{

    @ApiModelProperty(value = ApiValueUtils.User.USER_ID, required = true)
    private String userId;

    @ApiModelProperty(value = ApiValueUtils.User.PASSWORD, required = true)
    private String password;

    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$")
    @ApiModelProperty(value = ApiValueUtils.User.PHONE, required = true)
    private String phone;

    @ApiModelProperty(value = ApiValueUtils.User.Customer.NAME, required = true)
    private String name;

}
