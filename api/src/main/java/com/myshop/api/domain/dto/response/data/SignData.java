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
        @ApiModelProperty(value = ApiValueUtils.Sign.SUCCESS)
        private boolean success;

        @ApiModelProperty(value = ApiValueUtils.Sign.CODE)
        private int code;

        @ApiModelProperty(value = ApiValueUtils.Sign.MSG)
        private String msg;

        public SignUpResponse(boolean success, int code, String msg) {
            this.success = success;
            this.code = code;
            this.msg = msg;
        }
    }

    @ApiModel
    @Data
    @NoArgsConstructor
    public static class SignInResponse extends SignUpResponse {
        @ApiModelProperty(value = ApiValueUtils.Sign.TOKEN)
        private String token;

        public SignInResponse(boolean success, int code, String msg, String token) {
            super(success, code, msg);
            this.token = token;
        }
    }

}
