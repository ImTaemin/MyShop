import {Table} from "react-bootstrap";
import OrderListItem from "./OrderListItem";
import {useDispatch, useSelector} from "react-redux";
import {useEffect} from "react";
import client from "../../lib/api/client";
import {listOrders, unLoadOrders} from "../../modules/orders";
import Loader from "../loader/Loader";

const OrderList = ({orderStatus}) => {
  const dispatch = useDispatch();

  const {orders, error, page, checkOrderList, loading} = useSelector(({orders, loading}) => ({
    orders: orders.orders,
    error: orders.error,
    page: orders.page,
    checkOrderList: orders.checkOrders,
    loading: loading['orders/LIST_ORDERS'],
  }));

  // 주문내역 불러오기
  useEffect(() => {
    client.defaults.headers.common['X-AUTH-TOKEN'] = localStorage.getItem("accessToken");

    dispatch(listOrders({page, orderStatus}));

    return () => {
      dispatch(unLoadOrders())
    }
  }, [dispatch, page, orderStatus]);

  return (
    <>
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
        {error && (
          <>{error.msg}</>
        )}
        <tbody>
        {
          !loading && orders && (
            orders.map((order, index) => (
              <OrderListItem
                order={order}
                checked={checkOrderList.some(checkOrder => {
                    return checkOrder.cnt === order.cnt && checkOrder.orderNo === order.orderNo
                  }
                )}
                key={index} />
            ))
          )}
        </tbody>
        <tfoot>

        </tfoot>
      </Table>
      {loading && (
        <Loader />
      )}
    </>
  )
}

export default OrderList;