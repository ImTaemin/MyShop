import styled from "styled-components";
import {getNameByValue} from "../../pages/OrderPage";

const ImgNameWrap = styled.div`
  padding: 0 0% 0 20%;
  display: flex;
  align-items: center;
`;

const ImgDiv = styled.div`
`;

const CustomCheckBox = styled.input`
  &[type='checkbox'] {
    transform : scale(2);
    accent-color: forestgreen;
  }
`;

const ItemNameDiv = styled.div`
  margin-left: 5%;
  width: 100%;
  word-break:break-all;
`;

const OrderListItem = ({order, onCheckOrder}) => {
  const { cnt, orderNo, quantity, payment, orderStatus, orderDate, item} = order;

  const onCheck = (e) => {
    onCheckOrder(e.target.id);
  }

  return (
    <tr align="center">
      <td>
        <CustomCheckBox type="checkbox" id={orderNo.concat("-",cnt)} onChange={onCheck} />
      </td>
      <td>{orderNo}-{cnt}</td>
      <td>
        <ImgNameWrap>
          <ImgDiv>
            <img
              src={item.mainImage}
              className="rounded-3"
              width="50px"
              height="50px"
            />
          </ImgDiv>
          <ItemNameDiv>
            {item.name}
            <br/>
            {item.code}
          </ItemNameDiv>
        </ImgNameWrap>
      </td>
      <td>{quantity}</td>
      <td>{payment}</td>
      <td>{orderDate}</td>
      <td>{getNameByValue(orderStatus)}</td>
    </tr>
  )
}

export default OrderListItem;