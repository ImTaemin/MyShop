import {combineReducers} from "@reduxjs/toolkit";
import orders, {ordersSaga} from "./orders";
import loading from "./loading";
import { all } from 'redux-saga/effects';
import items, {itemsSaga} from "./items";
import item, {itemSaga} from "./item";

const rootReducer = combineReducers({
    loading,
    orders,
    items,
    item,
});

export function* rootSaga() {
    yield all([ordersSaga(), itemsSaga(), itemSaga()]);
}

export default rootReducer;