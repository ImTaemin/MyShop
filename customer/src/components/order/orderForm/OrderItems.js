import React from "react";
import "../../../assets/scss/order-item.scss";
import {useSelector} from "react-redux";
import "../../../assets/scss/order-coupon.scss";
import OrderItem from "./OrderItem";

const OrderItems = () => {
  const {items, error, loading} = useSelector( ({orderForm, loading}) => ({
    items: orderForm.items,
    error: orderForm.error,
    loading: loading['orderForm/LOAD_ITEM'],
  }));

  return (
    <>
      <h5 className="title">상품 정보</h5>
      <table
        className="order-item-table"
      >
        <thead>
          <tr>
            <th width="60%">상품 정보</th>
            <th width="10%">수량</th>
            <th width="20%">가격</th>
            <th width="10%">쿠폰 선택</th>
          </tr>
        </thead>
        <tbody>
          {items && items.map((item, index) => (
            <OrderItem item={item} key={index}/>
          ))}
        </tbody>
      </table>
    </>
  );
}

export default OrderItems;