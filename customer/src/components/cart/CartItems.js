import CartItem from "./CartItem";
import React, {useCallback, useEffect, useState} from "react";
import {useSelector} from "react-redux";
import {deleteCartItems} from "../../lib/api/cart";
import {CustomCheckBox} from "../../common/StyledComponents";
import {useNavigate} from "react-router-dom";

const CartItems = () => {
  const navigate = useNavigate();

  const {cartItems} = useSelector( ({cartItems}) => ({
    cartItems: cartItems.cartItems,
  }));

  const [checkedItems, setCheckedItems] = useState([]);

  useEffect(() => {
    if (cartItems) {
      setCheckedItems(cartItems.map(item => item.id));
    }
  }, [cartItems]);

  /**
   * 장바구니 상품 전체 체크
   */
  const toggleAllCheck = useCallback((e) => {
    if (e.target.checked) {
      setCheckedItems(cartItems.map(item => item.id));
    } else {
      setCheckedItems([]);
    }
  }, [checkedItems]);

  /**
   * 장바구니 상품 개별 체크
   */
  const toggleCheck = useCallback((itemId) => {
    if(!checkedItems) return;

    if (checkedItems.includes(itemId)) {
      setCheckedItems(prevItems => {
        return prevItems.filter(id => id !== itemId);
      });
    } else {
      setCheckedItems(prevItems => [
        ...prevItems,
        itemId
      ]);
    }
  }, [checkedItems]);

  // 장바구니 상품 개별 삭제
  const deleteCartItemsHandler = useCallback((e) => {
    deleteCartItems(checkedItems)
      .then(response => {
        alert("삭제되었습니다.");
        window.location.reload();
      });
  }, [checkedItems]);

  const orderBtnHandler = useCallback((e) => {
    navigate("/order/order-form", {
      state: {
        itemInfo: {
          itemIds: checkedItems,
        }
      }
    });
  }, [checkedItems]);

  return (
    <>
      <div className="d-flex justify-content-between align-items-center">
        <h5 className="title">장바구니 정보 / {cartItems && (cartItems.length+"개")}</h5>
        <button
          className="btn-custom-border no-wrap mb-2"
          onClick={deleteCartItemsHandler}
        >
          선택 삭제
        </button>
      </div>
      <table
        className="cart-item-table"
      >
        <thead>
        <tr>
          <th width="10%">
            <CustomCheckBox
              type="checkbox"
              checked={checkedItems?.length === cartItems?.length}
              onChange={toggleAllCheck}
            />
          </th>
          <th width="40%">상품 정보</th>
          <th width="15%">판매가</th>
          <th width="15%">수량</th>
          <th width="15%">주문금액</th>
          <th width="15%">주문관리</th>
        </tr>
        </thead>
        <tbody>
        {cartItems && cartItems.map((item, index) => (
          <CartItem
            item={item}
            key={index}
            checked={checkedItems?.includes(item.id)}
            onToggleCheck={() => toggleCheck(item.id)}
          />
        ))}
        </tbody>
      </table>

      <button className="btn-order" onClick={orderBtnHandler}>주문구매</button>
    </>
  );
}

export default CartItems;