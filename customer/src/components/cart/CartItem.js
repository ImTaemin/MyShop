import {Link} from "react-router-dom";
import React, {useCallback, useEffect, useState} from "react";
import {AiOutlineMinus, AiOutlinePlus} from "react-icons/ai";
import {deleteCartItem, updateCartQuantity} from "../../lib/api/cart";

const CartItem = ({item, checked, onToggleCheck}) => {

  const [quantity, setQuantity] = useState(1);
  const [price, setPrice] = useState(0);

  useEffect(() => {
    if(!item) return;

    setQuantity(item.quantity);
    setPrice(item.price);
  }, [item]);

  const updateCartItemQuantity = useCallback((quantity) => {
    const reqFormData = new FormData();
    reqFormData.append("itemId", item.id);
    reqFormData.append("quantity", quantity);

    updateCartQuantity(reqFormData)
      .then(response => {
        if(response.status === 200) {
          alert("수량이 수정되었습니다.");
        }
      });
  }, [quantity]);

  const decrease = useCallback((e) => {
    if(quantity <= 1) return;

    const qty = quantity - 1;
    setQuantity(qty);
    setPrice(item.price * qty);

    updateCartItemQuantity(qty);
  }, [quantity, price]);

  const increase = useCallback((e) => {
    const qty = quantity + 1;

    setQuantity(qty);
    setPrice(item.price * qty);

    updateCartItemQuantity(qty);
  }, [quantity, price]);

  const quantityHandler = useCallback((e) => {
    const value = e.target.value;

    if(isNaN(value) || value <= 0) return;

    setQuantity(Number(value));
    setPrice(Number(item.price * value));

    updateCartItemQuantity(Number(value));
  }, [quantity, price]);

  const deleteCartItemHandler = useCallback((e) => {
    const itemId = e.target.dataset.item;

    deleteCartItem(itemId)
      .then(response => {
        alert("삭제되었습니다.");
        window.location.reload();
      });

  }, []);

  return (
    <>
      {item && (
        <tr>
          <td>
            <input
              type="checkbox"
              checked={checked}
              onChange={() => onToggleCheck()}
            />
          </td>
          <td>
            <div className="cart-item-wrap">
              <div className="item-image">
                <Link to={"/item/" + item.id}>
                  <img src={item.mainImage} alt=""/>
                </Link>
              </div>
              <div className="item-article">
                [{item.brandName}] {item.name}
              </div>
            </div>
          </td>
          <td>{item.price.toLocaleString()}</td>
          <td>
            <div className="quantity-control">
              <button className="decrease" onClick={decrease}><AiOutlineMinus /></button>
              <input className="quantity" onChange={quantityHandler} type="text" value={quantity} />
              <button className="increase" onClick={increase}><AiOutlinePlus/></button>
            </div>
          </td>
          <td>
            {(item.price * quantity).toLocaleString()}
          </td>
          <td>
            <button
              data-item={item.id}
              className="btn-custom-border no-wrap"
              onClick={deleteCartItemHandler}
            >
              삭제하기
            </button>
          </td>
        </tr>
      )}
    </>
  )
}

export default CartItem;