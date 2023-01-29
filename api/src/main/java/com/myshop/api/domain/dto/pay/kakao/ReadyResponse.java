package com.myshop.api.domain.dto.pay.kakao;

import com.myshop.api.domain.entity.Orders;
import lombok.Data;

/**
 * 카카오페이에서 응답하는 내용으로 인해 스네이크 표기법 사용
 */
@Data
public class ReadyResponse {
    private String tid;
    private String next_redirect_pc_url;
    private String partner_order_id;
    private Orders order;
}
