import React, {useCallback} from "react";
import "../../../assets/scss/order-payment-info.scss";
import {useDispatch} from "react-redux";
import {changePayMethod} from "../../../modules/orderForm";

const PaymentInfo = (props) => {
  const dispatch = useDispatch();

  const payMethodHandler = useCallback((e) => {
    const method = e.target.value;

    // 네이버페이 미구현
    if(method === "NAVERPAY") return;

    dispatch(changePayMethod(method));
  }, [props]);

  return (
    <>
      <h5 className="title">결제 정보</h5>
      <div className="payment-info-container">
        <div className="payment-info-wrap">
          <p>결제 수단</p>
          <span className="payment-item">
            <div className="payment-area-wrap">
              <input
                type="radio"
                id="btn-paymethod-KAKAOPAY"
                name="paymethod"
                value="KAKAOPAY"
                data-pay-kind-name="카카오페이"
                onClick={payMethodHandler}
              />
              <label htmlFor="btn-paymethod-KAKAOPAY" className="box-choice">카카오페이</label>

              <input
                type="radio"
                id="btn-paymethod-NAVERPAY"
                name="paymethod"
                value="NAVERPAY"
                data-pay-kind-name="네이버페이"
                onClick={payMethodHandler}
              />
              <label htmlFor="btn-paymethod-NAVERPAY" className="box-choice">네이버페이</label>
            </div>
          </span>
        </div>

        <div className="payment-info-wrap">
          <p>픔절 시 환불 안내</p>
          <span className="payment-item">
            <span>결제하신 수단으로 취소됩니다.</span>
            <div>
              <ul className="cell_detail-list">
                <li>· 입점업체 배송은 낮은 확률로 상품이 품절일 가능성이 있습니다. 이에 품절 시 빠르게 환불 처리해드립니다.</li>
                <li>· 현금 환불의 경우, 예금정보가 일치해야 환불 처리가 가능합니다. 은행명, 계좌번호, 예금주명을 정확히 기재 부탁드립니다.</li>
                <li>· 환불 받으신 날짜 기준으로 3~5일(주말 제외) 후 결제대행사에서 직접 고객님의 계좌로 환불 처리됩니다.</li>
              </ul>
            </div>
          </span>
        </div>
      </div>
    </>
  );
}

export default PaymentInfo;