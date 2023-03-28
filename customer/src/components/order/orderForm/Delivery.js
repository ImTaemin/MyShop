import PostcodeModal from "./modal/PostcodeModal";
import React, {useCallback, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {changeField} from "../../../modules/orderForm";
import "../../../assets/scss/order-delivery.scss";

const Delivery = () => {
  const dispatch = useDispatch();

  const [postcodePopup, setPostcodePopup] = useState(false);

  const {name, phone, roadAddress, postalCode, detailAddress} = useSelector(({orderForm}) => ({
    name: orderForm.name,
    phone: orderForm.phone,
    roadAddress: orderForm.roadAddress,
    postalCode: orderForm.postalCode,
    detailAddress: orderForm.detailAddress,
  }));

  const inputHandler = useCallback((e) => {
    const {name, value} = e.target;

    dispatch(changeField({name,value}));

  }, [dispatch]);

  return (
    <>
      <h5 className="title">배송 정보</h5>
      <div className="delivery-info-container">
        <div className="delivery-info-wrap">
          <p>배송지</p>
          <button
            className="btn-custom-border"
            onClick={() => {setPostcodePopup(true)}}>
            배송지 등록
          </button>
          {postcodePopup && (
            <PostcodeModal
              closePostcodePopup={() => setPostcodePopup(false)} />
          )}
        </div>
        <div className="delivery-info-wrap">
          <p>이름 / 연락처</p>
          <span className="delivery-item">
              <input
                name="name"
                type="text"
                value={name || ''}
                className="form-control"
                autoComplete='off'
                placeholder="수령인"
                onChange={inputHandler}
              />
            </span>
          <hr className="vertical-line"/>
          <span className="delivery-item">
              <input
                name="phone"
                type="text"
                value={phone || ''}
                maxLength={13}
                className="form-control"
                autoComplete='off'
                placeholder="수령인 번호"
                onChange={inputHandler}
              />
            </span>
        </div>
        <div className="delivery-info-wrap">
          <p>주소</p>
          <span className="delivery-item">{postalCode && ("("+postalCode+")")}</span>
          <span className="delivery-item">{roadAddress}</span>
          <span className="delivery-item address-detail">
            {postalCode && (
              <input
                name="detailAddress"
                type="text"
                value={detailAddress || ''}
                className="form-control"
                placeholder="상세주소를 입력하세요"
                onChange={inputHandler}
              />
            )}
          </span>
        </div>
      </div>
    </>
  );
}

export default Delivery;