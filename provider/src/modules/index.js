import {combineReducers} from "@reduxjs/toolkit";
import orders, {ordersSaga} from "./orders";
import loading from "./loading";
import { all } from 'redux-saga/effects';
import items, {itemsSaga} from "./items";
import item, {itemSaga} from "./item";
import coupons, {couponsSaga} from "./coupons";

const rootReducer = combineReducers({
    loading,
    orders,
    items,
    item,
    coupons,
});

export function* rootSaga() {
    yield all([ordersSaga(), itemsSaga(), itemSaga(), couponsSaga()]);
}

export default rootReducer;