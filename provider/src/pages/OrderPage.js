import {ButtonGroup, Card, ToggleButton} from "react-bootstrap";
import React, {useState} from "react";
import {BiClipboard} from "react-icons/bi";
import "../scss/Order.scss";
import {TableHeader, TableNav, TableTitle} from "../components/common/Table";
import OrderList from "../components/orders/OrderList";
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

  const [orderStatus, setOrderStatus] = useState('RECEIVED');

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
        <OrderList orderStatus={orderStatus} />
      </Card.Body>
      
      <Card.Footer>
        <OrderStatusChange />
      </Card.Footer>

    </Card>
  )
}

export default React.memo(OrderPage);