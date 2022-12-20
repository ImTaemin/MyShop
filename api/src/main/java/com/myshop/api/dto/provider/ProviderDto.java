package com.myshop.api.dto.provider;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myshop.api.dto.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@ApiModel
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProviderDto implements User {

    @ApiModelProperty(value = "판매자 아이디", required = true)
    private String loginId;

    @ApiModelProperty(value = "판매자 비밀번호", required = true)
    private String password;

    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$")
    @ApiModelProperty(value = "판매자 대표 번호", required = true)
    private String phone;

    @ApiModelProperty(value = "판매자 브랜드명", required = true)
    private String brandName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "생성일")
    private LocalDateTime createDate;

}
