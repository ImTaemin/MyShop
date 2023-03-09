import {getNameByValue} from "../../pages/OrderPage";
import {useCallback} from "react";
import {useDispatch} from "react-redux";
import {checkOrders} from "../../modules/orders";
import {CustomCheckBox, ImgNameWrap, ItemNameDiv} from "../common/StyledComponents";

const OrderListItem = ({order, checked}) => {
  const dispatch = useDispatch();
  const {cnt, orderNo, quantity, payment, orderStatus, orderDate, item} = order;

  // 체크박스 체크
  const onCheckOrder = useCallback(
    (cnt, orderNo) => {
      dispatch(checkOrders({cnt, orderNo}))
    },
    [dispatch]
  );

  const onCheck = (e) => {
    onCheckOrder(cnt, orderNo);
  };

  return (
    <tr align="center" onClick={onCheck}>
      <td>
        <CustomCheckBox type="checkbox" checked={checked} onChange={() => {}}/>
      </td>
      <td>{orderNo}-{cnt}</td>
      <td>
        <ImgNameWrap>
          <div>
            <img
              src={item.mainImage}
              className="rounded-3"
              width="50px"
              height="50px"
            />
          </div>
          <ItemNameDiv>
            {item.name}
            <br/>
            {item.code}
          </ItemNameDiv>
        </ImgNameWrap>
      </td>
      <td>{quantity}</td>
      <td>\ {payment.toLocaleString()}</td>
      <td>{orderDate}</td>
      <td>{getNameByValue(orderStatus)}</td>
    </tr>
  )
}

export default OrderListItem;