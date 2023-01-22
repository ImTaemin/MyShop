package com.myshop.api.domain.dto.response;

import com.myshop.api.enumeration.CommonResponse;
import com.myshop.api.util.ApiValueUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Data
@NoArgsConstructor
public class BaseResponse {

    @ApiModelProperty(value = ApiValueUtils.CommonResponse.STATUS, example = "true")
    private boolean status;

    @ApiModelProperty(value = ApiValueUtils.CommonResponse.MSG, example = "success")
    private String msg;

    @ApiModelProperty(value = ApiValueUtils.CommonResponse.DATA)
    private Object data;

    public BaseResponse(boolean status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static ResponseEntity<BaseResponse> ok() {
        BaseResponse baseResponse = new BaseResponse(CommonResponse.SUCCESS.getStatus(), CommonResponse.SUCCESS.getMsg(), null);
        return ResponseEntity.ok(baseResponse);
    }

    public static ResponseEntity<BaseResponse> ok(String msg) {
        BaseResponse baseResponse = new BaseResponse(CommonResponse.SUCCESS.getStatus(), msg, null);
        return ResponseEntity.ok(baseResponse);
    }

    public static ResponseEntity<BaseResponse> ok(Object data) {
        BaseResponse baseResponse = new BaseResponse(CommonResponse.SUCCESS.getStatus(), CommonResponse.SUCCESS.getMsg(), data);
        return ResponseEntity.ok(baseResponse);
    }

    public static ResponseEntity<BaseResponse> ok(String msg, Object data) {
        BaseResponse baseResponse = new BaseResponse(CommonResponse.SUCCESS.getStatus(), msg, data);
        return ResponseEntity.ok(baseResponse);
    }

    public static ResponseEntity<BaseResponse> fail() {
        BaseResponse baseResponse = new BaseResponse(CommonResponse.FAIL.getStatus(), CommonResponse.FAIL.getMsg(), null);;
        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<BaseResponse> fail(String msg) {
        BaseResponse baseResponse = new BaseResponse(CommonResponse.FAIL.getStatus(), msg, null);;
        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<BaseResponse> error(String msg) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        BaseResponse baseResponse = new BaseResponse(CommonResponse.FAIL.getStatus(), msg, null);;

        return new ResponseEntity<>(baseResponse, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
