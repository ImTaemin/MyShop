import CartItem from "./CartItem";
import React, {useCallback, useEffect, useState} from "react";
import {useSelector} from "react-redux";

const CartItems = () => {
  const {cartItems} = useSelector( ({cartItems}) => ({
    cartItems: cartItems.cartItems,
  }));

  const [checkedItems, setCheckedItems] = useState([]);

  useEffect(() => {
    if (cartItems) {
      setCheckedItems(cartItems.map(item => item.id));
    }
  }, [cartItems]);

  const toggleAllCheck = useCallback((e) => {
    if (e.target.checked) {
      setCheckedItems(cartItems.map(item => item.id));
    } else {
      setCheckedItems([]);
    }
  }, [checkedItems]);

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

  return (
    <>
      <h5 className="title">장바구니 정보</h5>
      <table
        className="cart-item-table"
      >
        <thead>
        <tr>
          <th width="10%">
            <input
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
    </>
  );
}

export default CartItems;