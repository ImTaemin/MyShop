import "../assets/scss/order-form.scss";
import React, {useCallback, useEffect} from "react";
import Delivery from "../components/order/orderForm/Delivery";
import OrderItems from "../components/order/orderForm/OrderItems";
import {useDispatch, useSelector} from "react-redux";
import {loadItem, unloadOrderForm} from "../modules/orderForm";
import {useLocation} from "react-router";
import client from "../lib/api/client";
import DiscountInfo from "../components/order/orderForm/DiscountInfo";
import PaymentInfo from "../components/order/orderForm/PaymentInfo";

const OrderFormPage = () => {

  const dispatch = useDispatch();
  const location = useLocation();
  const {itemId, quantity} = location.state?.itemInfo;

  const {items, name, phone, roadAddress, postalCode, detailAddress, payMethod} = useSelector( ({orderForm}) => ({
    items: orderForm.items,
    name: orderForm.name,
    phone: orderForm.phone,
    roadAddress: orderForm.roadAddress,
    postalCode: orderForm.postalCode,
    detailAddress: orderForm.detailAddress,
    payMethod: orderForm.payMethod,
  }));

  const submitPaymentHandler = useCallback(async (e) => {
    const isBlank = (value) => {
      return value === undefined || value === '' || value === null || value.trim() === '';
    };

    if (isBlank(name) || isBlank(phone) || isBlank(roadAddress) || isBlank(postalCode) || isBlank(detailAddress) || isBlank(payMethod)) {
      alert("모든 항목을 작성해주세요")
      return;
    }

    const pattern = /^\d{2,3}-\d{3,4}-\d{4}$/;
    if(!pattern.test(phone)){
      alert("연락처를 확인해주세요")
      return;
    }

    // 결제 진행 (모든 항목 작성상태)
    const orderRequest = {
      orderItemList: items.map((item) => {
        return {
          itemId: item.id,
          quantity: item.quantity,
          couponCode: item.coupon === null ? null : item.coupon.code
        };
      }),
      name: name,
      phone: phone,
      roadAddress: roadAddress,
      postalCode: postalCode,
      detailAddress: detailAddress,
      payMethod: payMethod
    };

    const response = await client.post("/customer/order/kakao/ready", orderRequest);

    if(response.status === 200) {
      const {tid, partner_order_id, next_redirect_pc_url} = response.data.data;
      window.location.href = next_redirect_pc_url;
    }

  }, [items, name, phone, roadAddress, postalCode, detailAddress, payMethod]);


  useEffect(() => {
    client.defaults.headers.common['X-AUTH-TOKEN'] = localStorage.getItem("accessToken");

    dispatch(loadItem({itemId, quantity}));

    return () => {
      dispatch(unloadOrderForm());
    }
  }, [dispatch]);

  return (
    <div className="order-form-container">
      <p className="order-title">Order / Payment</p>

      <div className="order-form-wrap">
        <hr/>
        <Delivery />
      </div>

      <div className="order-form-wrap">
        <OrderItems />
      </div>

      <div className="order-form-wrap">
        <DiscountInfo />
      </div>

      <div className="order-form-wrap">
        <PaymentInfo />
      </div>

      <div className="order-form-wrap flex-column">
        <hr />
        <button
          className="btn-order"
          onClick={submitPaymentHandler}
        >
          결제하기
        </button>
      </div>
    </div>
  );
}

export default OrderFormPage;