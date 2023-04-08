package com.myshop.api.service;

import com.myshop.api.domain.dto.pay.kakao.ApproveResponse;
import com.myshop.api.domain.dto.pay.kakao.ReadyResponse;
import com.myshop.api.domain.dto.request.OrderRequest;
import com.myshop.api.domain.entity.*;
import com.myshop.api.domain.entity.embedded.Address;
import com.myshop.api.enumeration.OrderStatus;
import com.myshop.api.exception.ItemNotFoundException;
import com.myshop.api.exception.NotExistCouponException;
import com.myshop.api.repository.CouponRepository;
import com.myshop.api.repository.ItemRepository;
import com.myshop.api.repository.OrderRepository;
import com.myshop.api.repository.UsedCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KakaoPayServiceImpl implements KakaoPayService{

    /**
     * bean간에 주입을 모두 마친 후 @Value 작업 시작하기 때문에 yml 값을 못 읽는 상황이 벌어진다.
     * Environment env를 사용하는 쪽에서 생성자로 보낼 수 있지만, 나는 static 사용
     */
    @Value("${kakao.url.ready}")
    private static String readyUrl;

    @Value("${kakao.url.approve}")
    private String approveUrl;

    @Value("${kakao.key.admin}")
    private String adminKey;

    @Value("${api.url.baseUrl}")
    private String apiBaseUrl;

    private final ItemRepository itemRepository;
    private final CouponRepository couponRepository;
    private final OrderRepository orderRepository;
    private final UsedCouponRepository usedCouponRepository;

    @Override
    public ReadyResponse ready(Customer customer, String orderId, OrderRequest.Order orderRequest) {
        StringBuilder itemCodeBuilder = new StringBuilder();
        StringBuilder itemNameBuilder = new StringBuilder();

        // 주문번호랑 같은 주문시간을 갖게하기 위한 코드
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String date = orderId.substring(0, orderId.length() - 4);
        LocalDateTime orderDate = LocalDateTime.parse(date, formatter);

        // 주문한 정보
        Address address = new Address();
        address.setRoadName(orderRequest.getRoadAddress());
        address.setDetail(orderRequest.getDetailAddress());
        address.setPostalCode(orderRequest.getPostalCode());

        Orders order = Orders.builder()
                .id(orderId)
                .orderDate(orderDate)
                .address(address)
                .customer(customer)
                .build();

        /*
         * JPA save()
         * 식별자가 null이면 em.persist()를 호출
         * 식별자가 null이 아니면 em.merge()를 호출
         * 저장한 후에 orderItem에 mergedOrder 지정
         */
        Orders mergedOrder = orderRepository.save(order);

        // 주문한 상품들 엔티티
        List<OrderItem> orderItemList = new ArrayList<>();

        int orderItemCnt = 1; // 주문 상품 번호 ex) 1,2,3
        boolean itemNameFlag = true;
        int totalPrice = 0;
        int totalQuantity = 0;
        for (OrderRequest.OrderItem orderItemRequest : orderRequest.getOrderItemList()) {
            Item item = itemRepository.findById(orderItemRequest.getItemId())
                    .orElseThrow(ItemNotFoundException::new);

            // 상품명 준비
            if (itemNameFlag) {
                itemNameBuilder.append(item.getName());
                itemNameFlag = false;
            }
            
            // 상품코드 준비
            itemCodeBuilder.append(orderItemRequest.getItemId());
            itemCodeBuilder.append(",");

            Coupon coupon = null;

            // 할인율
            float discount = 1F;
            if(orderItemRequest.getCouponCode() != null) {
                // 쿠폰 사용
                coupon = couponRepository.findByCodeAndItemId(orderItemRequest.getCouponCode(), orderItemRequest.getItemId())
                        .orElseThrow(NotExistCouponException::new);
                discount = 1-coupon.getDiscount();

                // 사용 쿠폰 저장
                UsedCoupon usedCoupon = UsedCoupon.builder()
                        .coupon(coupon)
                        .customer(customer)
                        .item(item)
                        .build();

                usedCouponRepository.save(usedCoupon);
            }

            int itemPrice = item.getPrice();
            int orderQuantity = orderItemRequest.getQuantity();

            // 할인가 = 원가 * (1-할인율) --> ex) 3500 - (3500 * 0.9)
            // 아니면 원가 들어감
            int salePrice = (int) Math.floor(itemPrice * discount);
            // 상품 수량에 대한 가격(카카오 페이에서 사용) = 원가 * 수량
            int totalCostPrice = itemPrice * orderQuantity;

            OrderItem orderItem = OrderItem.builder()
                    .orders(mergedOrder)
                    .cnt(orderItemCnt)
                    .quantity(orderQuantity)
                    .payment(salePrice)
                    .coupon(coupon)
                    .orderStatus(OrderStatus.REQUESTED)
                    .orderDate(orderDate)
                    .item(item)
                    .build();
            orderItemList.add(orderItem);

            totalPrice += orderQuantity == 1
                    ? salePrice
                    : totalCostPrice - salePrice;
            totalQuantity += orderQuantity;

            orderItemCnt++;
        }

        // 마지막 ',' 제거
        itemCodeBuilder.deleteCharAt(itemCodeBuilder.length() - 1);

        /*
         * ex) 티셔츠ZXC 상품 2개, 바지ABC 상품 3개면
         * 티셔츠ZXC 외 1개
         */
        int orderItemSize = orderRequest.getOrderItemList().size();
        if(orderItemSize > 1) {
            itemNameBuilder.append(" 외 ");
            itemNameBuilder.append(orderItemSize - 1);
            itemNameBuilder.append("건");
        }

        // 카카오페이에 보낼 정보들
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", orderId);
        params.add("partner_user_id", customer.getUserId());
        params.add("item_name", itemNameBuilder.toString());
        params.add("item_code", itemCodeBuilder.toString());
        params.add("quantity", String.valueOf(totalQuantity));
        params.add("total_amount", String.valueOf(totalPrice));
        params.add("tax_free_amount", "0");
        params.add("approval_url", apiBaseUrl + "/customer/order/kakao/approval?orderId=" + orderId);
        params.add("cancel_url", apiBaseUrl + "/customer/order/kakao/cancel?orderId=" + orderId);
        params.add("fail_url", apiBaseUrl + "/customer/order/kakao/fail?orderId=" + orderId);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "x-www-form-urlencoded", StandardCharsets.UTF_8));
        headers.set("Authorization", "KakaoAK " + adminKey);

        // 외부 통신(카카오)
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> readyRequestEntity = new HttpEntity<>(params ,headers);
        ReadyResponse readyResponse = restTemplate.postForObject(readyUrl, readyRequestEntity, ReadyResponse.class);
        if (readyResponse != null) {
            readyResponse.setPartner_order_id(orderId);
        }

        mergedOrder.setTid(readyResponse.getTid());
        mergedOrder.setTotalPayment(totalPrice);
        mergedOrder.setOrderItemList(orderItemList);

        readyResponse.setOrder(mergedOrder);

        return readyResponse;
    }

    @Override
    public void approve(String pgToken, Orders order) {
        String cid = order.getOrderItemList().get(0).getItem().getProvider().getCid();
        String partnerUserId = order.getCustomer().getUserId();
        String orderId = order.getId();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", cid);
        params.add("tid", order.getTid());
        params.add("partner_order_id", orderId);
        params.add("partner_user_id", partnerUserId);
        params.add("pg_token", pgToken);

        System.out.println(params);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "x-www-form-urlencoded", StandardCharsets.UTF_8));
        headers.set("Authorization", "KakaoAK " + adminKey);

        // 외부 통신(카카오)
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> approveRequestEntity = new HttpEntity<>(params ,headers);
        restTemplate.postForObject(approveUrl, approveRequestEntity, ApproveResponse.class);

        // 준비에서 저장 후 성공 시 상태만 바꿔주는 방식임.
        order.getOrderItemList().forEach(orderItem -> {
            orderItem.setOrderStatus(OrderStatus.PAY_SUCCESS);
        });
    }
}
