import {ButtonGroup, Card, ToggleButton} from "react-bootstrap";
import React, {useState} from "react";
import {BiClipboard} from "react-icons/bi";
import {TableHeader, TableNav, TableTitle} from "../components/common/Table";
import OrderList from "../components/orders/OrderList";
import OrderStatusButtons from "../components/orders/OrderStatusButtons";
import {Helmet} from "react-helmet-async";
import {orderStatusMap} from "../components/common/Types";
const OrderPage = () => {

  const [orderStatus, setOrderStatus] = useState('PAY_SUCCESS');

  return (
    <>
      <Helmet>
        <title>주문 관리</title>
      </Helmet>
      <Card style={{height: "100%"}}>
        <TableHeader>
          <TableNav>
            <TableTitle><BiClipboard style={{marginRight: "0.6em"}} />주문 관리</TableTitle>
            <ButtonGroup style={{display:"flex", alignItems:"center"}}>
              {orderStatusMap.map((radio, idx) => (
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
          <OrderStatusButtons />
        </Card.Footer>

      </Card>
    </>
  )
}

export default React.memo(OrderPage);