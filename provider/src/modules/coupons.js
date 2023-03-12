import createRequestSaga, {createRequestActionTypes} from "../lib/createRequestSaga";
import * as couponsAPI from "../lib/api/coupons.js";
import {createAction, handleActions} from "redux-actions";
import {takeLatest} from "redux-saga/effects";

// action
const [LIST_COUPON, LIST_COUPON_SUCCESS, LIST_COUPON_FAILURE] =
  createRequestActionTypes("coupons/LIST_COUPON");
const UNLOAD_COUPONS = "coupons/UNLOAD_COUPON";

// action creators
export const listCoupon = createAction(LIST_COUPON);
export const unLoadCoupons = createAction(UNLOAD_COUPONS);

// redux-saga
const listCouponSaga = createRequestSaga(LIST_COUPON, couponsAPI.listCoupon);

export function* couponsSaga() {
  yield takeLatest(LIST_COUPON, listCouponSaga);
}

// init
const initState = {
  coupons: null,
  error: null
}

const coupons = handleActions({
    [LIST_COUPON_SUCCESS]: (state, {payload: coupons}) => ({
      ...state,
      coupons: coupons.data.data,
    }),

    [LIST_COUPON_FAILURE]: (state, {payload: error}) => ({
      ...state,
      error: error.response.data
    }),

    [UNLOAD_COUPONS]: () => initState,
  },
  initState
);

export default coupons;