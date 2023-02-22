package com.myshop.api.domain.dto.response.data;

import com.myshop.api.util.ApiValueUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SignData {

    @ApiModel
    @Data
    @NoArgsConstructor
    public static class SignUpResponse {
        @ApiModelProperty(value = ApiValueUtils.Sign.STATUS)
        private boolean status;

        @ApiModelProperty(value = ApiValueUtils.Sign.MSG)
        private String msg;

        public SignUpResponse(boolean status, String msg) {
            this.status = status;
            this.msg = msg;
        }
    }

    @ApiModel
    @Data
    @NoArgsConstructor
    public static class SignInResponse extends SignUpResponse {
        @ApiModelProperty(value = ApiValueUtils.Sign.ACCESS_TOKEN)
        private String accessToken;

        @ApiModelProperty(value = ApiValueUtils.Sign.REFRESH_TOKEN)
        private String refreshToken;

        public SignInResponse(boolean status, String msg, String accessToken, String refreshToken) {
            super(status, msg);
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }

}
