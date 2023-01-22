package com.myshop.api.util;

public class ApiValueUtils {

    public static class CommonResponse {
        public static final String STATUS = "결과 코드";
        public static final String MSG = "상세 메세지";
        public static final String DATA = "내용";
    }

    public static class User {
        public static final String USER_ID = "아이디";
        public static final String PASSWORD = "비밀번호";
        public static final String MODIFY_PASSWORD = "변경 비밀번호";
        public static final String PHONE = "휴대폰 번호";
        public static final String CREATE_DATE = "가입일";
        public static final String USER_ROLE = "사용자 권한";
        public static final String GENDER_TYPE = "MEN, WOMEN";

        public static class Provider {
            public static final String BRAND_NAME = "브랜드명";
        }

        public static class Customer {
            public static final String NAME = "이름";
        }
    }

    public static class Item {
        public static final String ID = "상품 고유번호";
        public static final String ID_LIST = "상품 고유번호 목록";
        public static final String CODE = "상품 코드";
        public static final String NAME = "상품명";
        public static final String BRAND_NAME = "브랜드명";
        public static final String PRICE = "상품 가격";
        public static final String CONTENT = "상품 내용";
        public static final String ITEM_TYPE = "상품 타입";
        public static final String QUANTITY = "상품 재고";
        public static final String GENDER_TYPE = "상품 성별";
        public static final String MAIN_IMAGE = "상품 대표 이미지";
        public static final String IMAGE_LIST = "상품 이미지 목록";
        public static final String ITEM_LIST = "상품 목록";
        public static final String PRICE_QUANTITY = "상품 가격, 재고수량 목록";
        public static final String CREATE_DATE = "상품 등록일";
        public static final String ITEM_TYPE_ENUM = "TOP, OUTER, PANTS,\n" +
                "ONEPIECE, SKIRT, SNEAKERS,\n" +
                "SHOES, BAG, SPORTS,\n" +
                "HEADWEAR, SOCKS_LEGWEAR, UNDERWEAR,\n" +
                "EYEWEAR, ACCESSORY, WATCH,\n" +
                "JEWELRY, BEAUTY, DIGITAL_TECH,\n" +
                "LIFE, CULTURE, PET";
    }

    public static class ItemImage {
        public static final String ID = "상품 이미지 고유번호";
        public static final String PATH = "상품 이미지 경로";
        public static final String SEQ = "상품 이미지 순서";
    }

    public static class Sign {
        public static final String SUCCESS = "가입 성공 여부. true - 성공";
        public static final String STATUS = "가입 성공 코드. 0 - 성공";
        public static final String MSG = "가입 성공 메세지. Success - 성공";
        public static final String TOKEN = "로그인에 성공했을 때 받는 토큰";
    }

    public static class Order {
        public static final String ID = "주문 고유번호";
        public static final String ORDER_NO = "주문 번호";
        public static final String TID = "거래 번호";
        public static final String TOTAL_PAYMENT = "총 결제 금액";
        public static final String ORDER_DATE = "주문일";
        public static final String CANCEL_DATE = "주문 취소일";
        public static final String CUSTOMER = "구매자";
        public static final String ORDER_ITEM_LIST = "주문 목록";
        public static final String ORDER_STATUS_ENUM = "RECEIVED,\n" +
                "CANCELED,\n" +
                "DELIVERING,\n" +
                "DELIVERED";

        public static class Address {
            public static final String LOAD = "도로명 주소";
            public static final String DETAIL = "상세 주소";
            public static final String POSTAL_CODE = "우편번호";
        }
   }
   
   public static class OrderItem {
        public static final String ID = "주문 상품 고유번호";
        public static final String QUANTITY = "주문 수량";
        public static final String PAYMENT = "주문 금액(상품금액 - 할인금액)";
        public static final String COUPON = "사용 쿠폰";
        public static final String ORDER_STATUS = "주문 상태";
   }

    public static class Page {
        public static final String PAGE = "페이지 번호";
        public static final String SIZE = "페이지 총 개수";
    }
    
    public static class Cart {
        public static final String ID = "장바구니 상품 고유번호";
        public static final String ITEM_ID = "상품 고유번호";
        public static final String QUANTITY = "수량";
    }
    
}
