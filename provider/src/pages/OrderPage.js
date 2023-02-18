import {ButtonGroup, Card, ToggleButton} from "react-bootstrap";
import React, {useCallback, useEffect, useState} from "react";
import {BiClipboard} from "react-icons/bi";
import "../scss/Order.scss";
import {TableHeader, TableNav, TableTitle} from "../components/common/Table";
import OrderList from "../components/orders/OrderList";
import {useDispatch, useSelector} from "react-redux";
import {changeOrders, checkOrders, listOrders, unLoadOrders} from "../modules/orders";
import Loader from "../components/loader/Loader";
import OrderStatusChange from "../components/orders/OrderStatusChange";

const status = [
  { name: '주문 요청', value: 'REQUESTED', variant: 'outline-secondary' },
  { name: '결제 완료', value: 'PAY_SUCCESS', variant: 'outline-success' },
  { name: '주문 접수', value: 'RECEIVED', variant: 'outline-primary' },
  { name: '주문 취소', value: 'CANCELED', variant: 'outline-danger' },
  { name: '배송 진행', value: 'DELIVERING', variant: 'outline-warning' },
  { name: '배송 완료', value: 'DELIVERED', variant: 'outline-dark' },
];

export const getNameByValue = (value) => {
  const result = status.find((element) => element.value === value);
  return result ? result.name : null;
}

const OrderPage = () => {
  const dispatch = useDispatch();

  const [orderStatus, setOrderStatus] = useState('PAY_SUCCESS');

  const {orders, error, page, checkOrderList, loading} = useSelector(({orders, loading}) => ({
    orders: orders.orders,
    error: orders.error,
    page: orders.page,
    checkOrderList: orders.checkOrders,
    loading: loading['orders/LIST_ORDERS'],
  }));

  // 주문내역 불러오기
  useEffect(() => {
    dispatch(listOrders({page, orderStatus}));
    return () => {
      dispatch(unLoadOrders())
    }
  }, [dispatch, orderStatus]);

  // 체크박스 체크
  const onCheckOrder = useCallback(
    (id) => dispatch(checkOrders(id)),
    [dispatch]
  );

  // 주문 상태 변경
  const onChangeOrders = (orderStatus) => {
    dispatch(changeOrders({checkOrderList, orderStatus}));
  }

  return (
    <Card style={{height: "100%"}}>
      <TableHeader>
        <TableNav>
          <TableTitle><BiClipboard style={{marginRight: "0.6em"}} />주문 관리</TableTitle>
          <ButtonGroup style={{display:"flex", alignItems:"center"}}>
            {status.map((radio, idx) => (
              <ToggleButton
                className="btn-status"
                style={{width:"4.6em"}}
                key={idx}
                id={`${radio.value}`}
                type="radio"
                variant={radio.variant}
                name="radio"
                value={radio.value}
                checked={orderStatus === radio.value}
                onChange={(e) => setOrderStatus(e.currentTarget.value)}>
                {radio.name}
              </ToggleButton>
            ))}
          </ButtonGroup>
        </TableNav>
      </TableHeader>

      <Card.Body>
        {loading && (
          <Loader />
        )}
        {error && (
          <>{error.msg}</>
        )}
        {!loading && orders && (
          <OrderList orders={orders} loading={loading} onCheckOrder={onCheckOrder} />
        )}
      </Card.Body>
      
      <Card.Footer>
        <OrderStatusChange onChangeOrders={onChangeOrders} />
      </Card.Footer>

    </Card>
  )
}

export default React.memo(OrderPage);