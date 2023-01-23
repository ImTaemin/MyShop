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
        @ApiModelProperty(value = ApiValueUtils.Sign.TOKEN)
        private String token;

        public SignInResponse(boolean status, String msg, String token) {
            super(status, msg);
            this.token = token;
        }
    }

}