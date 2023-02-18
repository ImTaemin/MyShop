import {Table} from "react-bootstrap";
import OrderListItem from "./OrderListItem";
import throttle from "../../util/throttle";

const OrderList = ({loading, orders, onCheckOrder}) => {

  return (
    <Table hover="true">
      <thead>
      <tr align="center">
        <th width="3%">　</th>
        <th width="15%">주문번호-순서</th>
        <th width="35%">상품명 / 상품코드</th>
        <th width="7%">수량</th>
        <th width="10%">결제 금액</th>
        <th width="20%">주문일</th>
        <th width="10%">상태</th>
      </tr>
      </thead>

      <tbody>
      {
        !loading && orders && (
          orders.map((order, index) => (
            <OrderListItem order={order} key={index} onCheckOrder={onCheckOrder}/>
          ))
        )}
      </tbody>
      <tfoot>

      </tfoot>
    </Table>
  )
}

export default OrderList;