import {combineReducers} from "@reduxjs/toolkit";
import categoryItems, {categoryItemSaga} from "./categoryItems";
import { all } from 'redux-saga/effects';
import loading from "./loading";
import orderForm, {orderFormSaga} from "./orderForm";
import favoriteItems, {favoriteItemsSaga} from "./favoriteItems";

const rootReducer = combineReducers({
  loading,
  categoryItems,
  orderForm,
  favoriteItems
});

export function* rootSaga() {
  yield all([categoryItemSaga(), orderFormSaga(), favoriteItemsSaga()]);
}

export default rootReducer;