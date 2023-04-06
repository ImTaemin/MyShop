import {Table} from "react-bootstrap";
import OrderListItem from "./OrderListItem";
import {useDispatch, useSelector} from "react-redux";
import {useCallback, useEffect, useRef} from "react";
import client from "../../lib/api/client";
import {listOrders, unLoadOrders} from "../../modules/orders";
import Loader from "../loader/Loader";

const OrderList = ({orderStatus}) => {
  const dispatch = useDispatch();

  const {orders, error, page, isLast, checkOrderList, loading} = useSelector(({orders, loading}) => ({
    orders: orders.orders,
    error: orders.error,
    page: orders.page,
    isLast: orders.isLast,
    checkOrderList: orders.checkOrders,
    loading: loading['orders/LIST_ORDERS'],
  }));

  const observerRef = useRef(null);
  const observerCallback = useCallback((entries) => {
    const target = entries[0];
    if (target.isIntersecting && !loading && !isLast) {
      dispatch(listOrders({page, orderStatus}));
    }
  }, [page, isLast, orderStatus, dispatch]);

  useEffect(() => {
    client.defaults.headers.common['X-AUTH-TOKEN'] = localStorage.getItem("providerAccessToken");

    if (observerRef.current) {
      const observer = new IntersectionObserver(observerCallback);
      observer.observe(observerRef.current);

      return () => {
        observer.disconnect();
      }
    }
  }, [observerRef.current, observerCallback, page, isLast, orderStatus]);

  useEffect(() => {
    dispatch(unLoadOrders());
  }, [orderStatus]);

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

        <tbody>
        {
          !loading && orders && (
            <>
              {orders.map((order, index) => (
                <OrderListItem
                  order={order}
                  checked={checkOrderList.some(checkOrder => {
                      return checkOrder.cnt === order.cnt && checkOrder.orderNo === order.orderNo
                    }
                  )}
                  key={index}/>
              ))}
            </>
          )}
        </tbody>
      </Table>
      {!error && !isLast && (
        <div style={{width: "100%", height: "30px"}} ref={observerRef} />
      )}
      {loading && (
        <Loader />
      )}
      {error && (
        <div>{error.msg}</div>
      )}
    </>
  )
}

export default OrderList;