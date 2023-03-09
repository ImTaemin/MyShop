import createRequestSaga, {createRequestActionTypes} from "../lib/createRequestSaga";
import * as ordersAPI from "../lib/api/orders";
import {takeLatest} from 'redux-saga/effects';
import {createAction, handleActions} from "redux-actions";

// action
const [LIST_ORDERS, LIST_ORDERS_SUCCESS, LIST_ORDERS_FAILURE] =
  createRequestActionTypes('orders/LIST_ORDERS');
const CHECK_ORDERS = 'orders/CHECK_ORDERS';
const CHANGE_ORDERS = 'orders/CHANGE_ORDERS'
const UNLOAD_ORDERS = 'orders/UNLOAD_ORDERS';

// action creators
export const listOrders = createAction(
  LIST_ORDERS,
  ({page, orderStatus}) => ({page, orderStatus})
);
export const checkOrders = createAction(CHECK_ORDERS, (id) => (id));
export const changeOrders = createAction(CHANGE_ORDERS, ({checkOrderList, orderStatus}) => ({
  checkOrderList,
  orderStatus
}));
export const unLoadOrders = createAction(UNLOAD_ORDERS);

// redux-saga
const listOrdersSaga = createRequestSaga(LIST_ORDERS, ordersAPI.listOrders);
const changeOrdersSaga = createRequestSaga(CHANGE_ORDERS, ordersAPI.changeOrders);

export function* ordersSaga() {
  yield takeLatest(LIST_ORDERS, listOrdersSaga);
  yield takeLatest(CHANGE_ORDERS, changeOrdersSaga);
}

// init
const initialState = {
  orders: null,
  error: null,
  page: 0,
  checkOrders: []
};

// reducer
const orders = handleActions({
    [LIST_ORDERS_SUCCESS]: (state, {payload: orders}) => ({
      ...state,
      orders: orders.data.content,
      page: orders.data.pageable.pageNumber
    }),

    [LIST_ORDERS_FAILURE]: (state, {payload: error}) => ({
      ...state,
      error: error.response.data,
    }),

    [CHECK_ORDERS]: (state, {payload: {cnt, orderNo}}) => ({
      ...state,
      checkOrders: state.checkOrders.some(
        order => order.cnt === cnt && order.orderNo === orderNo
      )
        ? state.checkOrders.filter(order => {
          return !(order.cnt === cnt && order.orderNo === orderNo)
        })
        : state.checkOrders.concat({cnt, orderNo})
    }),

    [CHANGE_ORDERS]: (state, {payload: {checkOrderList}}) => ({
      ...state,
      orders: state.orders.filter(order =>
        !checkOrderList.some(checkOrder =>
          checkOrder.cnt === order.cnt
          && checkOrder.orderNo === order.orderNo)
      ),
      checkOrders: []
    }),

    [UNLOAD_ORDERS]: () => initialState,
  },

  initialState
);

export default orders;