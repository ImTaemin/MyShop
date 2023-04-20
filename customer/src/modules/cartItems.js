import createRequestSaga, {createRequestActionTypes} from "../lib/createRequestSaga";
import {createAction, handleActions} from "redux-actions";
import * as cartAPI from "../lib/api/cart";
import {takeLatest} from "redux-saga/effects";

// action
const [LOAD_CART_ITEMS, LOAD_CART_ITEMS_SUCCES, LOAD_CART_ITEMS_FAILURE] =
  createRequestActionTypes("cartItems/LOAD_CART_ITEMS");
const UNLOAD_CART_ITEMS = "cartItems/UNLOAD_CART_ITEMS";

// action creators
export const loadCartItems = createAction(LOAD_CART_ITEMS);
export const unloadCartItems = createAction(UNLOAD_CART_ITEMS);

// redux-saga
const loadCartItemsSaga = createRequestSaga(LOAD_CART_ITEMS, cartAPI.loadCartItems);

export function* cartItemsSaga() {
  yield takeLatest(LOAD_CART_ITEMS, loadCartItemsSaga);
}

// init
const initialState = {
  cartItems: [],
  error: null,
}

// reducer
const cartItems = handleActions({
    [LOAD_CART_ITEMS_SUCCES]: (state, {payload: cartItems}) => ({
      ...state,
      cartItems: cartItems.data.data,
    }),

    [LOAD_CART_ITEMS_FAILURE]: (state, {payload: error}) => ({
      ...state,
      error: error.response.data,
    }),

    [UNLOAD_CART_ITEMS]: () => initialState,
  },

  initialState
);

export default cartItems;