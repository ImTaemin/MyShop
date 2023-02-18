import {combineReducers} from "@reduxjs/toolkit";
import orders, {ordersSaga} from "./orders";
import loading from "./loading";
import { all } from 'redux-saga/effects';

const rootReducer = combineReducers({
    loading,
    orders
});

export function* rootSaga() {
    yield all([ordersSaga()]);
}

export default rootReducer;