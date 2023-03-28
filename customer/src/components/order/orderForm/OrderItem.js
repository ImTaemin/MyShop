import React, {useCallback, useState} from "react";
import {useDispatch} from "react-redux";
import "../../../assets/scss/order-coupon.scss";
import {registerCoupon, unRegisterCoupon} from "../../../modules/orderForm";
import {Button, Form, Modal} from "react-bootstrap";
import qs from "qs";
import client from "../../../lib/api/client";
import {Link} from "react-router-dom";

const OrderItem = ({item}) => {
  const dispatch = useDispatch();

  // 쿠폰 검색 모달
  const [show, setShow] = useState(false);
  const handleClose = () => setShow(false);
  const handleShow = (e) => {
    const selectItemId = e.target.dataset.item;
    setCouponSelectItem(selectItemId);
    setShow(true);
  };
  const [searchResult, setSearchResult] = useState("");
  const [couponSelectItem, setCouponSelectItem] = useState(null);

  // 쿠폰 코드 핸들러
  const [couponCode, setCouponCode] = useState("");
  const couponCodeInputHandler = useCallback((e) => {
    setCouponCode(e.target.value);
  }, [couponCode]);

  // 쿠폰 검색
  const couponSearchHandler = useCallback(async (e) => {
    setSearchResult("");
    if(!couponSelectItem || !couponCode) {
      setSearchResult("쿠폰 번호를 입력해주세요");
      return;
    }

    const params = qs.stringify({
      couponCode: couponCode,
      itemId: couponSelectItem
    })
    const response = await client.get(`/coupon/search?${params}`);

    if(!response.data.status){
      setSearchResult("사용 불가");
      return;
    }

    // 쿠폰 등록 (사용 가능 상태)
    dispatch(registerCoupon({
      coupon: response.data.data,
      itemId: couponSelectItem
    }));

    handleClose();
  }, [couponCode, couponSelectItem]);

  const couponDelete = useCallback((e) => {
    const itemId = e.target.dataset.item;
    dispatch(unRegisterCoupon(itemId));
  }, []);

  return (
    <>
      {item && (
        <tr>
          <td>
            <Link to={"/item/" + item.data.id}>
              <div className="order-item-wrap">
                <div className="item-image">
                  <img src={item.data.mainImage} alt=""/>
                </div>
                <div className="item-article">
                  [{item.data.brandName}] {item.data.name}_{item.data.code}
                </div>
              </div>
            </Link>
          </td>
          <td>{item.quantity}개</td>
          <td>
            {item.coupon && (
              <>
                <span className="line-through">
                  {(item.data.price * item.quantity).toLocaleString()} 원
                </span>
                <br/>
                {(item.data.price * item.quantity - (item.data.price * (item.coupon.discount / 100))).toLocaleString()} 원
                <br/>
              </>
            )}
            {!item.coupon && (
              <>
                {(item.data.price * item.quantity).toLocaleString()} 원
              </>
            )}
          </td>
          <td>
            {item.coupon && (
              <>
                {item.coupon.discount}%
                <br/>
                <button
                  data-item={item.data.id}
                  className="btn-custom-border no-wrap"
                  onClick={couponDelete}
                >
                  삭제
                </button>
              </>
            )}
            {!item.coupon && (
              <button
                data-item={item.data.id}
                className="btn-custom-border no-wrap"
                onClick={handleShow}
              >
                쿠폰선택
              </button>
            )}
          </td>
        </tr>
      )}

      {/*쿠폰 코드 모달*/}
      <Modal show={show} onHide={handleClose}>
        <Modal.Body>
          <Form.Group className="mb-3" controlId="couponModal.ControlInput1">
            <Form.Label>쿠폰 코드를 입력해주세요(유효 시 창 닫힘)</Form.Label>
            <Form.Control
              type="text"
              value={couponCode}
              onChange={couponCodeInputHandler}
              autoFocus
            />
          </Form.Group>
        </Modal.Body>

        <Modal.Footer>
          <div className="search-result">
            {searchResult && (
              <>{searchResult}</>
            )}
          </div>
          <Button variant="secondary" onClick={handleClose}>
            닫기
          </Button>
          <Button variant="primary" onClick={couponSearchHandler}>
            적용
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}

export default OrderItem;