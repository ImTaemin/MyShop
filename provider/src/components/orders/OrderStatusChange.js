import {ButtonGroup, DropdownButton} from "react-bootstrap";
import DropdownItem from "react-bootstrap/DropdownItem";
import React, {useCallback} from "react";
import {changeOrders} from "../../modules/orders";
import {useDispatch, useSelector} from "react-redux";

const OrderStatusChange = () => {

  const dispatch = useDispatch();

  const {checkOrderList} = useSelector(({orders}) => ({
    checkOrderList: orders.checkOrders
  }));

  // 체크한 주문 상태 변경
  const onChangeOrders = useCallback(
    (orderStatus) => {
      dispatch(changeOrders({checkOrderList, orderStatus}));
    },
    [checkOrderList, dispatch]
  );

  return (
    <DropdownButton as={ButtonGroup} title="상태 변경" onSelect={onChangeOrders}>
      <DropdownItem eventKey="CANCELED" style={{color: "#dc3545"}}>선택 취소</DropdownItem>
      <DropdownItem eventKey="DELIVERING" style={{color: "#ffc107"}}>선택 발송</DropdownItem>
      <DropdownItem eventKey="RECEIVED" style={{color: "#0d6efd"}}>선택 접수</DropdownItem>
    </DropdownButton>
  );
}

export default OrderStatusChange;
