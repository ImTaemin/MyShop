import React, {useEffect, useState} from "react";
import {useSelector} from "react-redux";

const DiscountInfo = () => {
  const [originalPrice, setOriginalPrice] = useState(0);
  const [discountedPrice, setDiscountedPrice] = useState(0);
  const [percentDiscount, setPercentDiscount] = useState(0);

  const {items} = useSelector( ({orderForm, loading}) => ({
    items: orderForm.items,
  }));

  /**
   * 할인은 한 상품의 1개의 상품에만 등록 가능
   */
  useEffect(() => {
    setOriginalPrice(items.reduce((total, item) => total + item.data.price * item.quantity, 0));
    setDiscountedPrice(items.reduce((total, item) => {
      const originalTotalPrice = item.data.price * item.quantity;
      if (item.coupon) {
        return total + (item.data.price * (item.coupon.discount / 100));
      } else {
        return total + originalTotalPrice - originalTotalPrice;
      }
    }, 0));
  }, [items]);

  useEffect(() => {
    setPercentDiscount(((originalPrice - discountedPrice) / originalPrice) * 100);
  }, [originalPrice, discountedPrice]);

  // TODO: 상품 여러 개 불러와서 적용되는지 확인(api에서 통으로 가져오는 코드 필요)
  
  return (
    <>
      <h5 className="title">할인 정보</h5>
      <div className="discount-info-container">
        <div className="discount-info-wrap">
          <p>상품 금액</p>
          <span className="discount-item">
            {originalPrice.toLocaleString()} 원
          </span>
        </div>
        <div className="discount-info-wrap">
          <p>할인 금액</p>
          <span className="discount-item">
            {discountedPrice.toLocaleString()} 원
          </span>
          <hr className="vertical-line"/>
          <span className="discount-item">
            (
            {!isNaN(percentDiscount) && (
              <>
                 {(100 - percentDiscount).toFixed(2)}
              </>
            )}
            %)
          </span>
        </div>
        <div className="discount-info-wrap">
          <p>최종 결제 금액</p>
          <span className="discount-item">
            {(originalPrice - discountedPrice).toLocaleString()} 원
          </span>
        </div>
      </div>
    </>
  );
}

export default DiscountInfo;