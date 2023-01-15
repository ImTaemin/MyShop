package com.myshop.api.util;

public class ApiValueUtils {

    public static final String USER_ID = "아이디";
    public static final String PASSWORD = "비밀번호";
    public static final String MODIFY_PASSWORD = "변경 비밀번호";
    public static final String PHONE = "휴대폰 번호";
    public static final String CREATE_DATE = "가입일";
    public static final String USER_ROLE = "사용자 권한";
    
    
    public static class Provider {
        public static final String BRAND_NAME = "브랜드명";
    }

    public static class Customer {
        public static final String NAME = "이름";
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
        public static final String UPLOAD_DATE = "상품 등록일";
    }

    public static class ItemImage {
        public static final String ID = "상품 이미지 고유번호";
        public static final String PATH = "상품 이미지 경로";
        public static final String SEQ = "상품 이미지 순서";
    }

    public static class Sign {
        public static final String SUCCESS = "가입 성공 여부. true - 성공";
        public static final String CODE = "가입 성공 코드. 0 - 성공";
        public static final String MSG = "가입 성공 메세지. Success - 성공";
        public static final String TOKEN = "로그인에 성공했을 때 받는 토큰";
    }

    public static class Page {
        public static final String PAGE = "페이지 번호";
        public static final String SIZE = "페이지 총 개수";
    }
}
