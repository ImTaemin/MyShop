import {combineReducers} from "@reduxjs/toolkit";
import categoryItems, {categoryItemSaga} from "./categoryItems";
import { all } from 'redux-saga/effects';
import loading from "./loading";
import orderForm, {orderFormSaga} from "./orderForm";
import favoriteItems, {favoriteItemsSaga} from "./favoriteItems";
import cartItems, {cartItemsSaga} from "./cartItems";
import orderedList, {orderedListSaga} from "./orderedList";
import mypage, {authInfoSaga} from "./mypage";

const rootReducer = combineReducers({
  loading,
  categoryItems,
  orderForm,
  favoriteItems,
  cartItems,
  orderedList,
  mypage,
});

export function* rootSaga() {
  yield all([
    categoryItemSaga(),
    orderFormSaga(),
    favoriteItemsSaga(),
    cartItemsSaga(),
    orderedListSaga(),
    authInfoSaga(),
  ]);
}

export default rootReducer;