import Loader from "../components/loader/Loader";
import {useDispatch, useSelector} from "react-redux";
import React, {useEffect} from "react";
import client from "../lib/api/client";
import {loadCartItems, unloadCartItems} from "../modules/cartItems";
import CartItems from "../components/cart/CartItems";

/** order-form.scss에 테이블 스타일 있음.
 *
 */
const CartPage = () => {
  const dispatch= useDispatch();

  const {cartItems, error, loading} = useSelector( ({cartItems, loading}) => ({
    cartItems: cartItems.cartItems,
    error: cartItems.error,
    loading: loading["cartItems/LOAD_CART_ITEMS"],
  }));

  useEffect(() => {
    client.defaults.headers.common['X-AUTH-TOKEN'] = localStorage.getItem("customerAccessToken");

    dispatch(loadCartItems());

    return () => {
      dispatch(unloadCartItems());
    }
  }, [dispatch]);

  return (
    <>
      {!loading && cartItems.length === 0 && (
        <div className="text-xxl-center py-3 fs-4">장바구니에 추가한 상품이 없습니다.</div>
      )}
      {error && (
        <div>{error.msg}</div>
      )}
      {loading && (
        <Loader />
      )}
      {!loading && cartItems.length > 0 && (
        <>
          <div className="cart-item-container">
            <CartItems />
          </div>
        </>
      )}
    </>
  );
}

export default CartPage;