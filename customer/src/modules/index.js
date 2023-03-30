import {combineReducers} from "@reduxjs/toolkit";
import categoryItems, {categoryItemSaga} from "./categoryItems";
import { all } from 'redux-saga/effects';
import loading from "./loading";
import orderForm, {orderFormSaga} from "./orderForm";
import favoriteItems, {favoriteItemsSaga} from "./favoriteItems";
import cartItems, {cartItemsSaga} from "./cartItems";

const rootReducer = combineReducers({
  loading,
  categoryItems,
  orderForm,
  favoriteItems,
  cartItems,
});

export function* rootSaga() {
  yield all([categoryItemSaga(), orderFormSaga(), favoriteItemsSaga(), cartItemsSaga()]);
}

export default rootReducer;