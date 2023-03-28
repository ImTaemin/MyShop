import {combineReducers} from "@reduxjs/toolkit";
import categoryItems, {categoryItemSaga} from "./categoryItems";
import { all } from 'redux-saga/effects';
import loading from "./loading";
import orderForm, {orderFormSaga} from "./orderForm";

const rootReducer = combineReducers({
  loading,
  categoryItems,
  orderForm,
});

export function* rootSaga() {
  yield all([categoryItemSaga(), orderFormSaga()]);
}

export default rootReducer;