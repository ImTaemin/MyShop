import React, {useEffect, useState} from "react";
import client from "../lib/api/client";
import "../assets/scss/order-item.scss";
import {Link, useNavigate} from "react-router-dom";

const OrderSuccessPage = () => {
  const navigate = useNavigate();

  const [orders, setOrders] = useState(null);

  useEffect(() => {
    client.defaults.headers.common['X-AUTH-TOKEN'] = localStorage.getItem("accessToken");

    const params = new URLSearchParams(window.location.search);

    client.post("/customer/order", params.get("orderId"))
      .then(response => {
        if(!response.data.data){
          alert("주문번호에 해당하는 상세 주문이 없습니다.");
          navigate("/");
        }
        setOrders(response.data.data);
      });
  }, []);

  return (
    <>
      {orders && (
        <div className="order-form-container">
          <p className="order-title">
            주문번호 / {orders && (<>{orders.orderNo}</>)}
          </p>
          <hr />

          <div className="order-form-wrap">
            <h5 className="title">배송지 정보</h5>
            <table
              className="order-item-container"
            >
              <thead>
                <tr>
                  <th width="40%">배송지</th>
                  <th width="20%">주문 건수</th>
                  <th width="20%">총 지불액</th>
                  <th width="20%">주문일</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>
                    ({orders.postalCode}) {orders.roadName}
                    <br/>
                    {orders.detail}
                  </td>
                  <td>{orders.orderItemDataList.length} 건</td>
                  <td>{orders.totalPayment.toLocaleString()}원</td>
                  <td>{orders.orderDate}</td>
                </tr>
              </tbody>
            </table>
          </div>

          <div className="order-form-wrap">
            <h5 className="title">주문 상세</h5>
            <table
              className="order-item-container"
            >
              <thead>
              <tr>
                <th width="50%">상품 정보</th>
                <th width="15%">수량</th>
                <th width="15%">지불액</th>
                <th width="20%">사용 쿠폰</th>
              </tr>
              </thead>
              <tbody>
                {orders.orderItemDataList.map((order, index) => (
                  <tr key={index}>
                    <td>
                      <Link to={"/item/" + order.item.id}>
                        <div className="order-item-wrap">
                          <div className="item-image">
                            <img src={order.item.mainImage} alt=""/>
                          </div>
                          <div className="item-article">
                            [{order.item.brandName}] {order.item.name}_{order.item.code}
                          </div>
                        </div>
                      </Link>
                    </td>
                    <td>{order.quantity}개</td>
                    <td>{order.payment.toLocaleString()}원</td>
                    <td>
                      {!order.coupon.id && "✖"}
                      {order.coupon.id && (
                        <>{order.coupon.content}</>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </>
  );
}

export default OrderSuccessPage;