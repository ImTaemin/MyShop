import {Link} from "react-router-dom";
import React from "react";
import {getNameByValue} from "../../common/Types";

const OrderedItem = ({item}) => {

  return (
    <>
      {item && (
        <tr>
          <td>
              <div className="order-item-wrap">
                <Link to={"/item/" + item.item.id}>
                  <div className="item-image">
                    <img src={item.item.mainImage} alt=""/>
                  </div>
                </Link>
                <div className="item-article">
                  [{item.item.brandName}] {item.item.name}_{item.item.code}
                </div>
              </div>
          </td>
          <td>
            {item.orderDate.substr(0, 10)}
          </td>
          <td>
            <Link to={`/order?orderId=${item.orderNo}`} className="link-hover">
              {item.orderNo}
            </Link>
          </td>
          <td>
            {item.payment.toLocaleString()}원
            <br/>
            {item.quantity}개
          </td>
          <td>
            {getNameByValue(item.orderStatus)}
          </td>
        </tr>
      )}
    </>
  )
}

export default OrderedItem;