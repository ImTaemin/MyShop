import * as orderFormAPI from "../lib/api/orderForm";

// action
import {createAction, handleActions} from "redux-actions";
import createRequestSaga, {createRequestActionTypes} from "../lib/createRequestSaga";
import {takeLatest} from "redux-saga/effects";

const [LOAD_ITEM, LOAD_ITEM_SUCCESS, LOAD_ITEM_FAILURE] =
  createRequestActionTypes("orderForm/LOAD_ITEM");
const CHANGE_FIELD = "orderForm/CHANGE_FIELD";
const REGISTER_COUPON = "orderForm/REGISTER_COUPON";
const CHANGE_PAY_METHOD = "orderForm/CHANGE_PAY_METHOD";
const UNREGISTER_COUPON = "orderForm/UNREGISTER_COUPON";
const UNLOAD_ORDER_FORM = "orderForm/UNLOAD_ORDER_FORM";

// action creators
export const loadItem = createAction(LOAD_ITEM, ({itemId, quantity}) => ({itemId, quantity}));
export const changeField = createAction(
  CHANGE_FIELD,
  ({name, value, itemId}) => ({name, value, itemId})
);
export const registerCoupon = createAction(
  REGISTER_COUPON,
  ({coupon, itemId}) => ({coupon, itemId})
);
export const changePayMethod = createAction(CHANGE_PAY_METHOD, (method) => (method));
export const unRegisterCoupon = createAction(
  UNREGISTER_COUPON,
  (itemId) => (itemId)
);
export const unloadOrderForm = createAction(UNLOAD_ORDER_FORM);

// redux-saga
const loadItemSaga = createRequestSaga(LOAD_ITEM, orderFormAPI.loadItem);

export function* orderFormSaga() {
  yield takeLatest(LOAD_ITEM, loadItemSaga);
}

// init
const initialState = {
  items: [],
  name: "",
  phone: "",
  roadAddress: "",
  postalCode: "",
  detailAddress: "",
  payMethod: "",
};

const orderForm = handleActions(
  {
    [LOAD_ITEM_SUCCESS]: (state, { payload: {item, quantity} }) => ({
      ...state,
      items: state.items.concat({
        id: item.data.id,
        data: item.data.data,
        coupon: null,
        quantity: quantity,
      }),
    }),

    [LOAD_ITEM_FAILURE]: (state, {payload: error}) => ({
      ...state,
      error,
    }),

    [CHANGE_FIELD]: (state, {payload: {name, value} }) => ({
      ...state,
      [name]: value
    }),

    [REGISTER_COUPON]: (state, { payload: {coupon, itemId} }) => ({
      ...state,
      items: state.items.map((item) => (
        item.id === Number(itemId)
          ? { ...item, coupon:coupon }
          : item)
      ),
    }),

    [CHANGE_PAY_METHOD]: (state, {payload: paymethod}) => ({
      ...state,
      payMethod: paymethod
    }),

    [UNREGISTER_COUPON]: (state, {payload: itemId}) => ({
      ...state,
      items: state.items.map((item) => (
        item.id === Number(itemId)
          ? { ...item, coupon: null }
          : item
      )),
    }),

    [UNLOAD_ORDER_FORM]: () => initialState,
  },
  initialState
);

export default orderForm;